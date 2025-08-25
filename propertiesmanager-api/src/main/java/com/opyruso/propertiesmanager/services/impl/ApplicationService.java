package com.opyruso.propertiesmanager.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;

import com.opyruso.propertiesmanager.api.entity.ApiApplicationFull;
import com.opyruso.propertiesmanager.api.entity.ApiApplicationShort;
import com.opyruso.propertiesmanager.api.entity.ApiEnvironment;
import com.opyruso.propertiesmanager.api.entity.ApiGlobalVariable;
import com.opyruso.propertiesmanager.api.entity.ApiGlobalVariableValue;
import com.opyruso.propertiesmanager.api.entity.ApiInstalledVersion;
import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.api.entity.ApiProperty;
import com.opyruso.propertiesmanager.api.entity.ApiPropertyValue;
import com.opyruso.propertiesmanager.api.entity.request.ApiApplicationUpdateRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiNewApplicationRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiPropertyUpdateRequest;
import com.opyruso.propertiesmanager.constants.EnvironmentConfig;
import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.constants.propertyStatusEnum;
import com.opyruso.propertiesmanager.constants.propertyTypeEnum;
import com.opyruso.propertiesmanager.data.IApplicationDataService;
import com.opyruso.propertiesmanager.data.entity.Application;
import com.opyruso.propertiesmanager.data.entity.GlobalVariable;
import com.opyruso.propertiesmanager.data.entity.GlobalVariableValue;
import com.opyruso.propertiesmanager.data.entity.InstalledVersion;
import com.opyruso.propertiesmanager.data.entity.Property;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.data.entity.Version;
import com.opyruso.propertiesmanager.data.entity.pk.PropertyValuePK;
import com.opyruso.propertiesmanager.services.IApplicationService;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.transformers.impl.PropertiesTransformerFactory;
import com.opyruso.propertiesmanager.utils.ApplicationUtils;
import com.opyruso.propertiesmanager.utils.FileUtils;

import io.quarkus.logging.Log;

@ApplicationScoped
public class ApplicationService implements IApplicationService {

	@Inject
	protected EnvironmentConfig environmentConfig;
	
	@Inject
	protected IApplicationDataService dataService;
	
	@Inject
	protected ApplicationUtils applicationUtils;

	@Inject
	@Named(value = "propertiesTransformerFactory")
	protected PropertiesTransformerFactory propertiesTransformerFactory;
	
	@Override
	public List<ApiApplicationShort> getApplications() throws WebApplicationException {
		try {
			List<ApiApplicationShort> result = ApiApplicationShort.mapEntityToApi(dataService.selectApplications());
			for (ApiApplicationShort application : result) {
				application.versions = new HashMap<String, String>();
				application.lastReleaseDates = new HashMap<String, Long>();
				Map<String, ApiInstalledVersion> tmp = getApplicationInstalledVersions(application.appId);
				for (ApiInstalledVersion iv : tmp.values()) {
					application.versions.put(iv.envId, iv.numVersion);
					application.lastReleaseDates.put(iv.envId, iv.updateDate.getTime());
				}
			}
			return result;
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<String> getApplicationVersions(String appId) throws WebApplicationException {
		try {
			return dataService.selectVersions(appId);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<String> getApplicationFilenames(String appId, String numVersion) throws WebApplicationException {
		try {
			return dataService.selectFilename(appId, numVersion);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Map<String, String> getApplicationFiles(String appId, String numVersion) throws WebApplicationException {
		try {
			return dataService.selectFilenameAndContent(appId, numVersion);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Map<String, ApiInstalledVersion> getApplicationInstalledVersions(String appId) throws WebApplicationException {
		try {
			Map<String, ApiInstalledVersion> result = ApiInstalledVersion.mapEntityToApi(dataService.selectLastInstalledVersion(appId));
			return result;
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Map<String, Long> getApplicationLastReleaseDate(String appId) throws WebApplicationException {
		try {
			Map<String, Long> result = dataService.selectLastReleaseDate(appId);
			return result;
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ApiApplicationFull getApplicationDetails(String appId, String numVersion) throws WebApplicationException {
		try {
			ApiApplicationFull result = ApiApplicationFull.mapEntityToApi(dataService.selectApplication(appId));
			result.versions = new HashMap<String, String>();
			result.lastReleaseDates = new HashMap<String, Long>();
			Map<String, ApiInstalledVersion> tmp = getApplicationInstalledVersions(result.appId);
			for (ApiInstalledVersion iv : tmp.values()) {
				result.versions.put(iv.envId, iv.numVersion);
				result.lastReleaseDates.put(iv.envId, iv.updateDate.getTime());
			}
			result.existingVersions = getApplicationVersions(appId);
			result.lastReleaseDates = getApplicationLastReleaseDate(appId);
			List<ApiProperty> tmpProp = new ArrayList<ApiProperty>();
			tmpProp.addAll(ApiProperty.mapEntityToApi(dataService.selectProperties(appId, numVersion).values()));
			result.properties = tmpProp;
			result.propertiesValue = ApiPropertyValue.mapEntityToApi(dataService.selectPropertiesValue(appId, numVersion));
			return result;
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void appUpdate(String appId, ApiApplicationUpdateRequest request) throws WebApplicationException {
		try {
			if (request.appLabel != null) dataService.updateAppLabel(appId, request.appLabel);
			if (request.productOwner != null) dataService.updateProductOwner(appId, request.productOwner);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void propertyUpdate(ApiPropertyUpdateRequest request) throws WebApplicationException {
		try {
			PropertyValue np = dataService.selectPropertyValue(request.appId, request.numVersion, request.envId, request.filename, request.propertyKey);
			if (np == null) {
				np = new PropertyValue();
				np.getPk().setAppId(request.appId);
				np.getPk().setNumVersion(request.numVersion);
				np.getPk().setFilename(request.filename);
				np.getPk().setPropertyKey(request.propertyKey);
				np.getPk().setEnvId(request.envId);
				np.setNewValue(request.newValue==null?"":request.newValue);
				np.setStatus(propertyStatusEnum.TO_VALIDATE);
				np.setOperationType(OperationTypeEnum.ADD);
				np.setProtected(false);
				dataService.addPropertyValue(np);
			} else {
				np.setStatus(propertyStatusEnum.VALID);
			}
			if (request.status != null) np.setStatus(request.status);
			if (request.operationType != null) np.setOperationType(request.operationType);
			if (request.newValue != null) np.setNewValue(request.newValue);
			if (request.isProtected != null) np.setProtected(request.isProtected);
			dataService.updatePropertyValue(np);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void propertyAdd(ApiProperty request) throws WebApplicationException {
		try {
			if (request.propertyType != null) {
				Property np = new Property();
				np.getPk().setAppId(request.appId);
				np.getPk().setNumVersion(request.numVersion);
				np.getPk().setFilename(request.filename);
				np.getPk().setPropertyKey(request.propertyKey);
				np.setPropertyType(request.propertyType);
				dataService.addProperty(np);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void propertyAllEnvDelete(String appId, String numVersion, String filename, String propertyKey) throws WebApplicationException {
		try {
			PropertyValue np = new PropertyValue();
			np.getPk().setAppId(appId);
			np.getPk().setNumVersion(numVersion);
			np.getPk().setFilename(filename);
			np.getPk().setPropertyKey(propertyKey);
			dataService.updateAllPropertyValueOperationType(np.getPk(), OperationTypeEnum.DEL);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void propertyPermanentDelete(String appId, String numVersion, String filename, String propertyKey) throws WebApplicationException {
		try {
			Property np = new Property();
			np.getPk().setAppId(appId);
			np.getPk().setNumVersion(numVersion);
			np.getPk().setFilename(filename);
			np.getPk().setPropertyKey(propertyKey);
			dataService.deletePermanentProperty(np.getPk());
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void propertyAllEnvAdd(String appId, String numVersion, String filename, String propertyKey) throws WebApplicationException {
		try {
			PropertyValue np = new PropertyValue();
			np.getPk().setAppId(appId);
			np.getPk().setNumVersion(numVersion);
			np.getPk().setFilename(filename);
			np.getPk().setPropertyKey(propertyKey);
			dataService.updateAllPropertyValueOperationType(np.getPk(), OperationTypeEnum.ADD);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void addOrUpdateFile(String appId, String numVersion, String filename, String content) throws WebApplicationException {
		try {
			if (content != null) {
				Log.info("check BOM : " + content.substring(0, 25) + "...");
				if (content.startsWith("7u/")) {
					Log.info("remove BOM 1");
					content = content.substring(3);
				} else if (content.startsWith("77u/")) {
					Log.info("remove BOM 2");
					content = content.substring(4);
				}
	        }
			List<String> filenames = dataService.selectFilename(appId, numVersion);
			if (!filenames.contains(filename)) {
				dataService.addNewPropertiesFile(appId, numVersion, filename, content);
			} else {
				dataService.updatePropertiesFile(appId, numVersion, filename, content);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        Log.error(e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void addInstalledApplicationVersion(String appId, String numVersion, String envId) throws WebApplicationException {
		try {
			if (dataService.selectInstalledVersions(appId, envId).contains(numVersion)) {
				dataService.updateInstalledVersionUpdateDate(appId, envId, numVersion);
			} else {
				InstalledVersion installedVersion = new InstalledVersion();
				installedVersion.getPk().setAppId(appId);
				installedVersion.getPk().setEnvId(envId);
				installedVersion.getPk().setNumVersion(numVersion);
				dataService.addNewInstalledVersion(installedVersion);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}
	

	public String pvToString(PropertyValue pv) {
		return "PropertyValue [pk=" + pv.getPk().getAppId() + ", getEnvId=" + 
				pv.getPk().getEnvId() + ", operationType=" + pv.getPk().getFilename() + ", getNumVersion=" + pv.getPk().getNumVersion() + ", getPropertyKey=" + pv.getPk().getPropertyKey() + ", operationType=" + 
				pv.getOperationType() + ", status=" + pv.getStatus() + ", newValue="
				+ pv.getNewValue() + ", isProtected=" + pv.isProtected() + ", creationDate=" + pv.getCreationDate() + ", updateDate="
				+ pv.getUpdateDate() + "]";
	}
	

	public String pToString(Property pv) {
		return "PropertyValue [pk=" + pv.getPk().getAppId() + 
				", operationType=" + pv.getPk().getFilename() + ", getNumVersion=" + pv.getPk().getNumVersion() + ", getPropertyKey=" + pv.getPk().getPropertyKey() 
				+ ", getPropertyType=" + pv.getPropertyType() + ", creationDate=" + pv.getCreationDate() + ", updateDate="
				+ pv.getUpdateDate() + "]";
	}

	@Override
        public void createNewApplicationVersion(String appId, String numVersion, String filename, String content) throws WebApplicationException {
		try {
			if (numVersion.equals("snapshot")) {
				if (filename.endsWith(".properties")) {
					List<String> propsCache = new ArrayList<String>();
					
					List<Property> propsToAdd = new ArrayList<Property>();
					List<PropertyValue> propValueToAdd = new ArrayList<PropertyValue>();
					
					ITransformerFactory factory = propertiesTransformerFactory.init(appId, numVersion,
							environmentConfig.environments().get(environmentConfig.environments().keySet().iterator().next()).id, filename, FileUtils.createTempFile(content));
					factory.process();
					List<ApiLog> apiLog = factory.getLog();
					if (!dataService.selectVersions(appId).contains("snapshot")) {
						Version version = new Version();
						version.getPk().setAppId(appId);
						version.getPk().setNumVersion("snapshot");
						dataService.addNewVersion(version);
        }

        @Override
        public void createSnapshotVersion(String appId) throws WebApplicationException {
                try {
                        if (!dataService.selectVersions(appId).contains("snapshot")) {
                                Version version = new Version();
                                version.getPk().setAppId(appId);
                                version.getPk().setNumVersion("snapshot");
                                dataService.addNewVersion(version);
                        }
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
        }
					Map<String, Property> props = dataService.selectProperties(appId, "snapshot");
					for (ApiLog log : apiLog) {
						if (log.data != null && log.data.containsKey("propertyKey")
								&& !log.status.equals(TransformerLogStatusEnum.ADDED_TO_VALIDATE)
								&& !log.status.equals(TransformerLogStatusEnum.ADDED_VALID)) {
							String key = log.data.get("propertyKey");
							String value = log.data.get("value");
							if (!log.data.containsKey("value")) {
								value = "";
							}
							propsCache.add(filename + "@" + key);
							if (!props.containsKey(filename + "@" + key)) {
								Property p = new Property();
								p.getPk().setAppId(appId);
								p.getPk().setNumVersion(numVersion);
								p.getPk().setFilename(filename);
								p.getPk().setPropertyKey(key);
								p.setPropertyType(propertyTypeEnum.IMPORTED);
								propsToAdd.add(p);
								for (ApiEnvironment env : environmentConfig.environments().values()) {
									PropertyValue pv = new PropertyValue();
									pv.getPk().setAppId(appId);
									pv.getPk().setNumVersion(numVersion);
									pv.getPk().setEnvId(env.id);
									pv.getPk().setFilename(filename);
									pv.getPk().setPropertyKey(key);
									pv.setNewValue(value);
									pv.setOperationType(OperationTypeEnum.ADD);
									pv.setProtected(false);
									pv.setStatus(propertyStatusEnum.TO_VALIDATE);
									propValueToAdd.add(pv);
								}
							}
						}
					}
					for (Property p : propsToAdd) {
						try {
							dataService.addProperty(p);
						} catch (Exception e) {
                        Log.error("Error:", e);
                        Log.error(pToString(p));
						}
					}
					for (PropertyValue pv : propValueToAdd) {
						try {
							dataService.addPropertyValue(pv);
						} catch (Exception e) {
                        Log.error("Error:", e);
                        Log.error(pvToString(pv));
						}
					}
					for (String cache : propsCache) {
						Log.warn("cache content : " + cache);
					}
					for (Property p : props.values()) {
						Log.warn("Checking for delete : " + pToString(p));
						if (!propsCache.contains(p.getPk().getFilename() + "@" + p.getPk().getPropertyKey())) {
							Log.warn("NOT FOUND IN CACHE : " + pToString(p));
							PropertyValuePK pk = new PropertyValuePK();
							pk.setAppId(appId);
							pk.setNumVersion(numVersion);
							pk.setFilename(filename);
							pk.setPropertyKey(p.getPk().getPropertyKey());
							dataService.updateAllPropertyValueOperationType(pk, OperationTypeEnum.DEL);
						}
					}
				}
			} else {
				if (dataService.selectVersions(appId).contains(numVersion)) return;
				Version lastVersion = dataService.selectLastVersionGlobal(appId);
				
				Version version = new Version();
				version.getPk().setAppId(appId);
				version.getPk().setNumVersion(numVersion);
				dataService.addNewVersion(version);
				
				if (lastVersion != null) {
					String refVersion = lastVersion.getPk().getNumVersion();
					dataService.copyAllPropertiesValueFromVersionToVersion(appId, refVersion, numVersion);
				} else {
					dataService.copyAllPropertiesValueFromVersionToVersion(appId, "snapshot", numVersion);
				}
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        Log.error(e, e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void createNewApplication(ApiNewApplicationRequest request) throws WebApplicationException {
		try {
			Application old = dataService.selectApplication(request.appId);
			if (old == null) {
				Application application = new Application();
				application.getPk().setAppId(request.appId);
				application.setAppLabel(request.appLabel);
				application.setProductOwner(request.productOwner);
				dataService.addNewApplication(application);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<ApiGlobalVariable> getGlobalVariables() throws WebApplicationException {
		try {
			List<ApiGlobalVariable> result = ApiGlobalVariable.mapEntityToApi(dataService.selectGlobalVariables());
			return result;
		} catch (Exception e) {
                        Log.error("Error:", e);
                        e.printStackTrace();
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Map<String, Map<String, ApiGlobalVariableValue>> getGlobalVariableValues() throws WebApplicationException {
		try {
			Map<String, Map<String, ApiGlobalVariableValue>> result = new HashMap<String, Map<String, ApiGlobalVariableValue>>();
			Map<String, Map<String, GlobalVariableValue>> resultData = dataService.selectGlobalVariableValues();
			result = ApiGlobalVariableValue.mapEntityToApi(resultData);
			return result;
		} catch (Exception e) {
                        Log.error("Error:", e);
                        e.printStackTrace();
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void addGlobalVariable(String key) throws WebApplicationException {
		try {
			GlobalVariable gv = dataService.selectGlobalVariable(key);
			if (gv == null) {
				GlobalVariable globalVariable = new GlobalVariable();
				globalVariable.getPk().setGlobalVariableKey(key);
				dataService.addGlobalVariable(globalVariable);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        e.printStackTrace();
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void updateGlobalVariable(String key, String env, String value) throws WebApplicationException {
		try {
			GlobalVariableValue gv = dataService.selectGlobalVariableValue(key, env);
			if (gv == null) {
				GlobalVariableValue globalVariableValue = new GlobalVariableValue();
				globalVariableValue.getPk().setGlobalVariableKey(key);
				globalVariableValue.getPk().setEnvId(env);
				globalVariableValue.setNewValue(value);
				globalVariableValue.setProtected(false);
				dataService.addNewGlobalVariableValue(globalVariableValue);
			} else {
				gv.setNewValue(value);
				dataService.updateGlobalVariableValue(gv);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        e.printStackTrace();
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void updateGlobalVariableProtection(String key, String env, boolean isProtected) throws WebApplicationException {
		try {
			GlobalVariableValue gv = dataService.selectGlobalVariableValue(key, env);
			if (gv != null) {
				gv.setProtected(isProtected);
				dataService.updateGlobalVariableValue(gv);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        e.printStackTrace();
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void removeGlobalVariable(String key) throws WebApplicationException {
		try {
			GlobalVariable gv = dataService.selectGlobalVariable(key);
			if (gv != null) {
				dataService.removeGlobalVariableValues(key);
				dataService.removeGlobalVariable(key);
			}
		} catch (Exception e) {
                        Log.error("Error:", e);
                        e.printStackTrace();
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void replaceAllPropertiesFromVersionToVersion(String appId, String fromVersion, String toVersion) throws WebApplicationException {
		try {
			dataService.cleanPropertiesByVersion(appId, toVersion);
			dataService.copyAllPropertiesValueFromVersionToVersion(appId, fromVersion, toVersion);
		} catch (Exception e) {
                        Log.error("Error:", e);
                        e.printStackTrace();
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
