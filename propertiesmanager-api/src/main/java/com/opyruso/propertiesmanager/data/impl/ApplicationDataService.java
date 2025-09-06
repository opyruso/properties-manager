package com.opyruso.propertiesmanager.data.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.StatusEnum;
import com.opyruso.propertiesmanager.data.IApplicationDataService;
import com.opyruso.propertiesmanager.data.entity.Application;
import com.opyruso.propertiesmanager.data.entity.GlobalVariable;
import com.opyruso.propertiesmanager.data.entity.GlobalVariableValue;
import com.opyruso.propertiesmanager.data.entity.InstalledVersion;
import com.opyruso.propertiesmanager.data.entity.PropertiesFile;
import com.opyruso.propertiesmanager.data.entity.Property;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.data.entity.Version;
import com.opyruso.propertiesmanager.data.entity.pk.PropertiesFilePK;
import com.opyruso.propertiesmanager.data.entity.pk.PropertyPK;
import com.opyruso.propertiesmanager.data.entity.pk.PropertyValuePK;
import com.opyruso.propertiesmanager.data.entity.repository.ApplicationRepository;
import com.opyruso.propertiesmanager.data.entity.repository.GlobalVariableRepository;
import com.opyruso.propertiesmanager.data.entity.repository.GlobalVariableValueRepository;
import com.opyruso.propertiesmanager.data.entity.repository.InstalledVersionRepository;
import com.opyruso.propertiesmanager.data.entity.repository.PropertiesFileRepository;
import com.opyruso.propertiesmanager.data.entity.repository.PropertyRepository;
import com.opyruso.propertiesmanager.data.entity.repository.PropertyValueRepository;
import com.opyruso.propertiesmanager.data.entity.repository.VersionRepository;
import com.opyruso.propertiesmanager.utils.KeycloakAttributesUtils;

import io.quarkus.arc.DefaultBean;

@DefaultBean
@ApplicationScoped
public class ApplicationDataService implements IApplicationDataService {

	@Inject
	protected JsonWebToken jwt;
	
	@Inject
	protected ApplicationRepository applicationRepository;
	
	@Inject
	protected VersionRepository VersionRepository;
	
	@Inject
	protected InstalledVersionRepository installedVersionRepository;
	
	@Inject
	protected PropertiesFileRepository propertiesFileRepository;
	
	@Inject
	protected PropertyRepository propertyRepository;
	
	@Inject
	protected PropertyValueRepository propertyValueRepository;
	
	@Inject
	protected GlobalVariableRepository globalVariableRepository;
	
	@Inject
	protected GlobalVariableValueRepository globalVariableValueRepository;
	
	@Inject
	protected VersionRepository versionRepository;

        @Override
        public List<Application> selectApplications(boolean includeArchived) throws WebApplicationException {
                if (includeArchived) {
                        return applicationRepository.listAll();
                }
                return applicationRepository.list("status = ?1", StatusEnum.ACTIVE);
        }

	@Override
        public List<String> selectVersions(String appId, boolean includeArchived) throws WebApplicationException {
                List<String> result = new ArrayList<String>();
                List<Version> tmp = includeArchived ? versionRepository.list("pk.appId = ?1", appId)
                                : versionRepository.list("pk.appId = ?1 AND status = ?2", appId, StatusEnum.ACTIVE);
                for (Version version : tmp) {
                        result.add(version.getPk().getNumVersion());
                }
                return result;
        }

	@Override
	public List<String> selectInstalledVersions(String appId, String envId) throws WebApplicationException {
		List<String> result = new ArrayList<String>();
		List<InstalledVersion> tmp = installedVersionRepository.list("pk.appId = ?1 AND pk.envId = ?2", appId, envId);
		for (InstalledVersion version : tmp) {
			result.add(version.getPk().getNumVersion());
		}
		return result;
	}

	@Override
	public List<String> selectFilename(String appId, String numVersion) throws WebApplicationException {
		List<String> result = new ArrayList<String>();
		List<PropertiesFile> tmp = propertiesFileRepository.find("pk.appId = ?1 AND pk.numVersion = ?2", appId, numVersion).list();
		for (PropertiesFile p : tmp) {
			if (!result.contains(p.getPk().getFilename())) result.add(p.getPk().getFilename());
		}
		return result;
	}

	@Override
	public Map<String, String> selectFilenameAndContent(String appId, String numVersion) throws WebApplicationException {
		Map<String, String> result = new HashMap<String, String>();
		List<PropertiesFile> tmp = propertiesFileRepository.find("pk.appId = ?1 AND pk.numVersion = ?2", appId, numVersion).list();
		for (PropertiesFile p : tmp) {
			String tmpContent = p.getContent();
			if (tmpContent != null && tmpContent.startsWith("77u/")) {
				tmpContent = tmpContent.substring(1);
	        }
			if (!result.containsKey(p.getPk().getFilename())) result.put(p.getPk().getFilename(), tmpContent);
		}
		return result;
	}

	@Override
	@Transactional
	public void addNewPropertiesFile(String appId, String numVersion, String filename, String content) throws WebApplicationException {
		PropertiesFile tmp = new PropertiesFile();
		tmp.setPk(new PropertiesFilePK());
		tmp.getPk().setAppId(appId);
		tmp.getPk().setNumVersion(numVersion);
		tmp.getPk().setFilename(filename);
		tmp.setContent(content);
		propertiesFileRepository.persist(tmp);
	}

	@Override
	@Transactional
	public void updatePropertiesFile(String appId, String numVersion, String filename, String content) throws WebApplicationException {
		PropertiesFile tmp = propertiesFileRepository.find("pk.appId = ?1 AND pk.numVersion = ?2 AND pk.filename = ?3", appId, numVersion, filename).singleResult();
		tmp.setContent(content);
		tmp.setUpdateDate(Timestamp.from(Calendar.getInstance().toInstant()));
		propertiesFileRepository.persist(tmp);
	}

	@Override
	@Transactional
	public void updateInstalledVersionUpdateDate(String appId, String envId, String numVersion) throws SQLException {
		InstalledVersion tmp = installedVersionRepository.find("pk.appId = ?1 AND pk.numVersion = ?2 AND pk.envId = ?3", appId, numVersion, envId).singleResult();
		tmp.setUpdateDate(Timestamp.from(Calendar.getInstance().toInstant()));
		installedVersionRepository.persist(tmp);
	}

	@Override
	public Map<String, InstalledVersion> selectLastInstalledVersion(String appId) throws WebApplicationException {
		Map<String, InstalledVersion> result = new HashMap<String, InstalledVersion>();
		List<InstalledVersion> tmp = installedVersionRepository.find("FROM InstalledVersion i WHERE i.pk.appId = ?1 AND i.updateDate >= (SELECT MAX(iv.updateDate) FROM InstalledVersion iv WHERE iv.pk.appId = ?1 AND iv.pk.envId = i.pk.envId)", appId).list();
		for (InstalledVersion version : tmp) {
			result.put(version.getPk().getEnvId(), version);
		}
		return result;
	}

	@Override
	public InstalledVersion selectLastInstalledVersionGlobal(String appId) throws SQLException {
		Optional<InstalledVersion> tmp = installedVersionRepository.find(
				"pk.appId = ?1 "
					+ "AND creationDate = (SELECT MAX(iv2.creationDate) FROM InstalledVersion iv2 WHERE iv2.pk.appId = ?1) "
					+ "ORDER BY pk.numVersion desc "
				, appId).firstResultOptional();
		return tmp.orElse(null);
	}

	@Override
	public Version selectLastVersionGlobal(String appId) throws SQLException {
		Optional<Version> tmp = versionRepository.find(
				"pk.appId = ?1 "
					+ "AND creationDate = (SELECT MAX(iv2.creationDate) FROM Version iv2 WHERE iv2.pk.appId = ?1) "
					+ "AND pk.numVersion <> 'snapshot' "
					+ "ORDER BY updateDate desc "
				, appId).firstResultOptional();
		return tmp.orElse(null);
	}

	@Override
	public Map<String, Long> selectLastReleaseDate(String appId) throws WebApplicationException {
		Map<String, Long> result = new HashMap<String, Long>();
		List<InstalledVersion> tmp = installedVersionRepository.find("pk.appId = ?1 group by pk.appId, pk.envId HAVING updateDate = MAX(updateDate)", appId).list();
		for (InstalledVersion version : tmp) {
			result.put(version.getPk().getEnvId(), version.getUpdateDate().getTime());
		}
		return result;
	}

	@Override
        public Application selectApplication(String appId) throws WebApplicationException {
                return applicationRepository.find("pk.appId = ?1", appId).firstResult();
        }

	@Override
	public Map<String, Map<String, Map<String, PropertyValue>>> selectPropertiesValue(String appId, String numVersion) throws WebApplicationException {
		boolean isAdmin = KeycloakAttributesUtils.securityCheckIsAdminAsBoolean(jwt);
		boolean isConnector = KeycloakAttributesUtils.securityCheckIsConnectorAsBoolean(jwt);
		Map<String, Map<String, Map<String, PropertyValue>>> result = new HashMap<String, Map<String, Map<String, PropertyValue>>>();
		List<PropertyValue> tmp = propertyValueRepository.find("pk.appId = ?1 AND pk.numVersion = ?2 ORDER BY pk.filename, pk.propertyKey", appId, numVersion).list();
		for (PropertyValue pv : tmp) {
			if (isAdmin || isConnector || KeycloakAttributesUtils.securityCheckAsBoolean(jwt, appId, pv.getPk().getEnvId(), "r")) {
				if (!result.containsKey(pv.getPk().getEnvId())) result.put(pv.getPk().getEnvId(), new HashMap<String, Map<String, PropertyValue>>());
				if (!result.get(pv.getPk().getEnvId()).containsKey(pv.getPk().getFilename())) result.get(pv.getPk().getEnvId()).put(pv.getPk().getFilename(), new HashMap<String, PropertyValue>());
				if (!isAdmin && !isConnector && pv.isProtected()) {
					pv.setNewValue(null);
				}
				result.get(pv.getPk().getEnvId()).get(pv.getPk().getFilename()).put(pv.getPk().getPropertyKey(), pv);
			}
		}
		return result;
	}

	@Override
	public Map<String, PropertyValue> selectPropertiesValue(String appId, String version, String envId) throws WebApplicationException {
		boolean isAdmin = KeycloakAttributesUtils.securityCheckIsAdminAsBoolean(jwt);
		boolean isConnector = KeycloakAttributesUtils.securityCheckIsConnectorAsBoolean(jwt);
		Map<String, PropertyValue> result = new HashMap<String, PropertyValue>();
		List<PropertyValue> tmp = propertyValueRepository.find("pk.appId = ?1 AND pk.numVersion = ?2 AND pk.envId = ?3 ORDER BY pk.propertyKey", appId, version, envId).list();
		for (PropertyValue pv : tmp) {
			if (isAdmin || isConnector || KeycloakAttributesUtils.securityCheckAsBoolean(jwt, appId, pv.getPk().getEnvId(), "r")) {
				if (!isAdmin && !isConnector && pv.isProtected()) {
					pv.setNewValue(null);
				}
				result.put(pv.getPk().getFilename() + "@" + pv.getPk().getPropertyKey(), pv);
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void updateAppLabel(String appId, String appLabel) throws WebApplicationException {
		applicationRepository.update("UPDATE Application SET appLabel = ?2, updateDate = current_timestamp WHERE pk.appId = ?1", appId, appLabel);
	}

	@Override
	@Transactional
        public void updateProductOwner(String appId, String productOwner) throws WebApplicationException {
                applicationRepository.update("UPDATE Application SET productOwner = ?2, updateDate = current_timestamp WHERE pk.appId = ?1", appId, productOwner);
        }

        @Override
        @Transactional
        public void updateAppStatus(String appId, StatusEnum status) throws WebApplicationException {
                applicationRepository.update("UPDATE Application SET status = ?2, updateDate = current_timestamp WHERE pk.appId = ?1", appId, status);
        }

	@Override
	public PropertyValue selectPropertyValue(String appId, String numVersion, String envId, String filename, String propertyKey) throws WebApplicationException {
		boolean isAdmin = KeycloakAttributesUtils.securityCheckIsAdminAsBoolean(jwt);
		boolean isConnector = KeycloakAttributesUtils.securityCheckIsConnectorAsBoolean(jwt);
		PropertyValue result = null;
		Optional<PropertyValue> old = propertyValueRepository.find("pk.appId = ?1 AND pk.numVersion = ?2 AND pk.envId = ?3 AND pk.filename = ?4 AND pk.propertyKey = ?5", appId, numVersion, envId, filename, propertyKey).singleResultOptional();
		if (old.isPresent()) {
			result = old.get();
			if (!isAdmin && !isConnector && result.isProtected()) {
				result.setNewValue(null);
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void addPropertyValue(PropertyValue np) throws WebApplicationException {
		propertyValueRepository.persist(np);
	}

	@Override
	@Transactional
	public void updatePropertyValue(PropertyValue np) throws WebApplicationException {
		propertyValueRepository.update("UPDATE PropertyValue SET operationType = ?2, status = ?3, newValue = ?4, isProtected = ?5, updateDate = current_timestamp WHERE pk = ?1",
				np.getPk(), np.getOperationType(), np.getStatus(), np.getNewValue(), np.isProtected());
	}

	@Override
	@Transactional
	public void updateAllPropertyValueOperationType(PropertyValuePK pvpk, OperationTypeEnum operationType) throws WebApplicationException {
		propertyValueRepository.update("UPDATE PropertyValue SET operationType = ?5, status = 'TO_VALIDATE', updateDate = current_timestamp WHERE pk.appId = ?1 AND pk.numVersion = ?2 AND pk.filename = ?3 AND pk.propertyKey = ?4",
				pvpk.getAppId(), pvpk.getNumVersion(), pvpk.getFilename(), pvpk.getPropertyKey(), operationType);
	}

	@Override
	@Transactional
	public void deletePermanentProperty(PropertyPK ppk) throws WebApplicationException {
		propertyValueRepository.delete("DELETE FROM PropertyValue WHERE pk.appId = ?1 AND pk.numVersion = ?2 AND pk.filename = ?3 AND pk.propertyKey = ?4",
				ppk.getAppId(), ppk.getNumVersion(), ppk.getFilename(), ppk.getPropertyKey());
		propertyRepository.delete("DELETE FROM Property WHERE pk.appId = ?1 AND pk.numVersion = ?2 AND pk.filename = ?3 AND pk.propertyKey = ?4",
				ppk.getAppId(), ppk.getNumVersion(), ppk.getFilename(), ppk.getPropertyKey());
	}

	@Override
	public Map<String, Property> selectProperties(String appId, String numVersion) throws WebApplicationException {
		Map<String, Property> result = new HashMap<String, Property>();
		List<Property> tmp = propertyRepository.find("pk.appId = ?1 AND pk.numVersion = ?2 ORDER BY pk.filename, pk.propertyKey", appId, numVersion).list();
		for (Property p : tmp) {
			if (!result.containsKey(p.getPk().getFilename() + "@" + p.getPk().getPropertyKey())) result.put(p.getPk().getFilename() + "@" + p.getPk().getPropertyKey(), p);
		}
		return result;
	}

	@Override
	@Transactional
	public void addProperty(Property np) throws WebApplicationException {
		propertyRepository.persist(np);
	}

	@Override
	@Transactional
	public void addNewInstalledVersion(InstalledVersion installedVersion) throws SQLException {
		installedVersionRepository.persist(installedVersion);
	}

	@Override
	@Transactional
        public void addNewVersion(Version version) throws SQLException {
                versionRepository.persist(version);
        }

        @Override
        @Transactional
        public void updateVersionStatus(String appId, String numVersion, StatusEnum status) throws WebApplicationException {
                versionRepository.update("UPDATE Version SET status = ?3, updateDate = current_timestamp WHERE pk.appId = ?1 AND pk.numVersion = ?2", appId, numVersion, status);
        }

	@Override
	@Transactional
	public void addNewApplication(Application application) throws SQLException {
		applicationRepository.persist(application);
	}

	@Override
	@Transactional
	public void copyAllPropertiesValueFromVersionToVersion(String appId, String fromVersion, String toVersion) throws SQLException {
		Query qpf = propertyRepository.getEntityManager().createNativeQuery("INSERT into "
				+ "properties_file (app_id , num_version , filename , content , creation_date , update_date) "
				+ "SELECT app_id , ? , filename , content , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP "
				+ "FROM properties_file p WHERE p.app_id = ? AND p.num_version = ?");
		qpf.setParameter(1, toVersion);
		qpf.setParameter(2, appId);
		qpf.setParameter(3, fromVersion);
		qpf.executeUpdate();
		Query qp = propertyRepository.getEntityManager().createNativeQuery("INSERT into "
				+ "property (app_id , num_version , filename , property_key , property_type , creation_date , update_date) "
				+ "SELECT app_id , ? , filename , property_key , property_type , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP "
				+ "FROM property p WHERE p.app_id = ? AND p.num_version = ?");
		qp.setParameter(1, toVersion);
		qp.setParameter(2, appId);
		qp.setParameter(3, fromVersion);
		qp.executeUpdate();
		Query qpv = propertyValueRepository.getEntityManager().createNativeQuery("INSERT into "
				+ "property_value (app_id , env_id , num_version , filename , property_key , new_value , is_protected , operation_type , status , creation_date , update_date) "
				+ "SELECT app_id , env_id , ? , filename , property_key , new_value , is_protected , operation_type , status , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP "
				+ "FROM property_value p WHERE p.app_id = ? AND p.num_version = ?");
		qpv.setParameter(1, toVersion);
		qpv.setParameter(2, appId);
		qpv.setParameter(3, fromVersion);
		qpv.executeUpdate();
		Query qpvu = propertyValueRepository.getEntityManager().createNativeQuery(
				"UPDATE property_value SET new_value = ? "
				+ "WHERE property_key = ? AND app_id = ? AND num_version = ? ");
		qpvu.setParameter(1, toVersion);
		qpvu.setParameter(2, "propertiesmanager_version");
		qpvu.setParameter(3, appId);
		qpvu.setParameter(4, toVersion);
		qpvu.executeUpdate();
	}

	@Override
	@Transactional
	public void cleanPropertiesByVersion(String appId, String version) throws SQLException {
		Query qpv = propertyValueRepository.getEntityManager().createNativeQuery("DELETE FROM property_value WHERE app_id = ? AND num_version = ?");
		qpv.setParameter(1, appId);
		qpv.setParameter(2, version);
		qpv.executeUpdate();
		Query qp = propertyRepository.getEntityManager().createNativeQuery("DELETE FROM property WHERE app_id = ? AND num_version = ?");
		qp.setParameter(1, appId);
		qp.setParameter(2, version);
		qp.executeUpdate();
		Query qpf = propertiesFileRepository.getEntityManager().createNativeQuery("DELETE FROM properties_file WHERE app_id = ? AND num_version = ?");
		qpf.setParameter(1, appId);
		qpf.setParameter(2, version);
		qpf.executeUpdate();
	}

	@Override
	public List<GlobalVariable> selectGlobalVariables() throws SQLException {
		return globalVariableRepository.listAll();
	}

	@Override
	public Map<String, Map<String, GlobalVariableValue>> selectGlobalVariableValues() {
		Map<String, Map<String, GlobalVariableValue>> resultData = new HashMap<String, Map<String, GlobalVariableValue>>();
		List<GlobalVariableValue> tmp = globalVariableValueRepository.listAll();
		for (GlobalVariableValue gvv : tmp) {
			if (!resultData.containsKey(gvv.getPk().getGlobalVariableKey())) resultData.put(gvv.getPk().getGlobalVariableKey(), new HashMap<String, GlobalVariableValue>());
			resultData.get(gvv.getPk().getGlobalVariableKey()).put(gvv.getPk().getEnvId(), gvv);
		}
		return resultData;
	}

	@Override
	public GlobalVariable selectGlobalVariable(String key) {
		GlobalVariable result = null;
		Optional<GlobalVariable> tmp = globalVariableRepository.find("pk.globalVariableKey = ?1", key).singleResultOptional();
		if (tmp.isPresent()) result = tmp.get();
		return result;
	}

	@Override
	public GlobalVariableValue selectGlobalVariableValue(String key, String env) {
		GlobalVariableValue result = null;
		Optional<GlobalVariableValue> tmp = globalVariableValueRepository.find("pk.globalVariableKey = ?1 AND pk.envId = ?2", key, env).singleResultOptional();
		if (tmp.isPresent()) result = tmp.get();
		return result;
	}

	@Override
	@Transactional
	public void addGlobalVariable(GlobalVariable globalVariable) {
		globalVariableRepository.persist(globalVariable);
	}

	@Override
	@Transactional
	public void addNewGlobalVariableValue(GlobalVariableValue globalVariableValue) {
		globalVariableValueRepository.persist(globalVariableValue);
	}

	@Override
	@Transactional
	public void updateGlobalVariableValue(GlobalVariableValue gv) {
		globalVariableValueRepository.update("UPDATE GlobalVariableValue SET newValue = ?3, isProtected = ?4, updateDate = current_timestamp WHERE pk.globalVariableKey = ?1 AND  pk.envId = ?2",
				gv.getPk().getGlobalVariableKey(), gv.getPk().getEnvId(), gv.getNewValue(), gv.isProtected());
	}

	@Override
	@Transactional
	public void removeGlobalVariableValues(String key) {
		globalVariableValueRepository.delete("pk.globalVariableKey = ?1", key);
	}

	@Override
	@Transactional
	public void removeGlobalVariable(String key) {
		globalVariableRepository.delete("pk.globalVariableKey = ?1", key);
	}

}






















