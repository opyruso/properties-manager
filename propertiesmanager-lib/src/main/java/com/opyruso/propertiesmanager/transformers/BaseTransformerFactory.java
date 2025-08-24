package com.opyruso.propertiesmanager.transformers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.data.IApplicationDataService;
import com.opyruso.propertiesmanager.data.entity.GlobalVariable;
import com.opyruso.propertiesmanager.data.entity.GlobalVariableValue;
import com.opyruso.propertiesmanager.data.entity.Property;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.utils.FileUtils;
import com.opyruso.propertiesmanager.utils.KeycloakAttributesUtils;

public abstract class BaseTransformerFactory implements ITransformerFactory {

	@Inject
	protected IApplicationDataService applicationDataService;

	@Inject
	protected JsonWebToken jwt;

//	protected String source;
	protected String result;
	protected List<ApiLog> log;

	protected String appId;
	protected String version;
	protected String envId;
	protected String filename;
	protected File file;

	protected List<GlobalVariable> databaseGlobalKeys;
	protected Map<String, Map<String, GlobalVariableValue>> databaseGlobalValues;
	protected Map<String, Property> databaseKeys;
	protected Map<String, PropertyValue> databaseValues;

	protected List<Property> fileKeys;
	protected Map<String, PropertyValue> fileValues;

	public ITransformerFactory init(String appId, String version, String envId, String filename, File file) throws Exception {
		if (appId != null && version != null && envId != null && file != null) {
			this.appId = appId;
			this.version = version;
			this.envId = envId;
			this.filename = filename;
			this.file = file;
//			source = FileUtils.getFileContent(file);
			result = "";
			log = new ArrayList<ApiLog>();
			this.databaseGlobalKeys = applicationDataService.selectGlobalVariables();
			this.databaseGlobalValues = applicationDataService.selectGlobalVariableValues();
			this.databaseKeys = applicationDataService.selectProperties(appId, version);
			this.databaseValues = applicationDataService.selectPropertiesValue(appId, version, envId);
			fileKeys = new ArrayList<Property>();
			fileValues = new HashMap<String, PropertyValue>();
		} else {
			throw new WebApplicationException(HttpStatus.SC_BAD_REQUEST);
		}
		return this;
	}

	@Override
	public void process() throws Exception {
		preTransform();
		globalVariableReplace();
		List<String> usedCache = transform();
		for (String key : getDatabaseKeys().keySet()) {
			if (!usedCache.contains(key)) {
				usedCache.add(key);
				Property prop = getDatabaseKeys().get(key);
				if (!prop.getPk().getFilename().equals(getFilename())) continue;
				log.add(new ApiLog(TransformerLogStatusEnum.NOT_FOUND, prop.getPk().getPropertyKey()));
			}
		}

		postTransform();
	}
	
	private void globalVariableReplace() throws Exception {
		String content = FileUtils.getFileContent(file);
		for (GlobalVariable globalVariable : databaseGlobalKeys) {
			String key = globalVariable.getPk().getGlobalVariableKey();
			if (databaseGlobalValues.containsKey(globalVariable.getPk().getGlobalVariableKey())
					&& databaseGlobalValues.get(globalVariable.getPk().getGlobalVariableKey()).containsKey(envId)
					&& databaseGlobalValues.get(globalVariable.getPk().getGlobalVariableKey()).get(envId) != null) {
				String value = databaseGlobalValues.get(globalVariable.getPk().getGlobalVariableKey()).get(envId).getNewValue();
				if (!content.contains(key)) {
					log.add(new ApiLog(TransformerLogStatusEnum.GLOBAL_NOT_FOUND, key));
				} else {
					content = content.replace(globalVariable.getPk().getGlobalVariableKey(), value);
					log.add(new ApiLog(TransformerLogStatusEnum.GLOBAL_UPDATED, key + " -> " + value));
				}
			} else {
				log.add(new ApiLog(TransformerLogStatusEnum.GLOBAL_ENV_NOT_FOUND, key));	
			}
		}
		file = FileUtils.createTempFile(content);
	}

	public String secureValue(PropertyValue propertyValue) {
		String result = "HIDDEN";
		if (!propertyValue.isProtected()
				|| KeycloakAttributesUtils.securityCheckIsAdminAsBoolean(jwt)
			) {
			if (!KeycloakAttributesUtils.securityCheckIsConnectorAsBoolean(jwt)) {
				result = propertyValue.getNewValue();
			}
		}
		return result;
	}

//	public File getSource() throws Exception {
//		return FileUtils.createTempFile(source);
//	}

	public File getResult() throws Exception {
		return FileUtils.createTempFile(result);
	}

	public List<ApiLog> getLog() throws Exception {
		return log;
	}

	public String getAppId() {
		return appId;
	}

	public String getVersion() {
		return version;
	}

	public String getEnvId() {
		return envId;
	}

	public String getFilename() {
		return filename;
	}

	public File getFile() {
		return file;
	}

	public Map<String, Property> getDatabaseKeys() {
		return databaseKeys;
	}

	public Map<String, PropertyValue> getDatabaseValues() {
		return databaseValues;
	}

	public List<Property> getFileKeys() {
		return fileKeys;
	}

	public Map<String, PropertyValue> getFileValues() {
		return fileValues;
	}

}
