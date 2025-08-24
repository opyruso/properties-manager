package com.opyruso.propertiesmanager.api.entity;

import java.util.HashMap;
import java.util.Map;

import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.propertyStatusEnum;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;

public class ApiPropertyValue {

	public String appId;
	public String numVersion;
	public String filename;
	public String propertyKey;
	public String envId;

	public OperationTypeEnum operationType;
	public propertyStatusEnum status;
	public String newValue;
	public boolean isProtected;

	public static ApiPropertyValue mapEntityToApi(PropertyValue propertyValue) {
		ApiPropertyValue result = null;
		if (propertyValue != null) {
			ApiPropertyValue n = new ApiPropertyValue();
			n.appId = propertyValue.getPk().getAppId();
			n.numVersion = propertyValue.getPk().getNumVersion();
			n.filename = propertyValue.getPk().getFilename();
			n.propertyKey = propertyValue.getPk().getPropertyKey();
			n.envId = propertyValue.getPk().getEnvId();
			n.operationType = propertyValue.getOperationType();
			n.status = propertyValue.getStatus();
			n.newValue = propertyValue.getNewValue();
			n.isProtected = propertyValue.isProtected();
			result = n;
		}
		return result;
	}

	public static Map<String, Map<String, Map<String, ApiPropertyValue>>> mapEntityToApi(Map<String, Map<String, Map<String, PropertyValue>>> propertiesValue) {
		Map<String, Map<String, Map<String, ApiPropertyValue>>> result = new HashMap<String, Map<String, Map<String, ApiPropertyValue>>>();
		if (propertiesValue != null) {
			for (String e : propertiesValue.keySet()) {
				if (e != null) {
					if (!result.containsKey(e)) result.put(e, new HashMap<String, Map<String, ApiPropertyValue>>());
					for (String fn : propertiesValue.get(e).keySet()) {
						if (!result.get(e).containsKey(fn)) result.get(e).put(fn, new HashMap<String, ApiPropertyValue>());
						for (PropertyValue p : propertiesValue.get(e).get(fn).values()) {
							result.get(e).get(fn).put(p.getPk().getPropertyKey(), mapEntityToApi(p));
						}
					}
				}
			}
		}
		return result;
	}

	public static PropertyValue mapApiToEntity(ApiPropertyValue propertyValue) {
		PropertyValue result = null;
		if (propertyValue != null) {
			PropertyValue n = new PropertyValue();
			n.getPk().setAppId(propertyValue.appId);
			n.getPk().setNumVersion(propertyValue.numVersion);
			n.getPk().setFilename(propertyValue.filename);
			n.getPk().setPropertyKey(propertyValue.propertyKey);
			n.getPk().setEnvId(propertyValue.envId);
			n.setOperationType(propertyValue.operationType);
			n.setStatus(propertyValue.status);
			n.setNewValue(propertyValue.newValue);
			n.setProtected(propertyValue.isProtected);
			result = n;
		}
		return result;
	}

}
