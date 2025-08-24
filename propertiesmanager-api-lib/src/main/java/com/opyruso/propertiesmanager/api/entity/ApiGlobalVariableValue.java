package com.opyruso.propertiesmanager.api.entity;

import java.util.HashMap;
import java.util.Map;

import com.opyruso.propertiesmanager.data.entity.GlobalVariableValue;

public class ApiGlobalVariableValue {

	public String globalVariableKey;
	public String envId;
	public String newValue;
	public boolean isProtected;

	public static ApiGlobalVariableValue mapEntityToApi(GlobalVariableValue globalVariableValue) {
		ApiGlobalVariableValue result = null;
		if (globalVariableValue != null) {
			ApiGlobalVariableValue n = new ApiGlobalVariableValue();
			n.globalVariableKey = globalVariableValue.getPk().getGlobalVariableKey();
			n.envId = globalVariableValue.getPk().getEnvId();
			n.newValue = globalVariableValue.getNewValue();
			n.isProtected = globalVariableValue.isProtected();
			result = n;
		}
		return result;
	}

	public static Map<String, Map<String, ApiGlobalVariableValue>> mapEntityToApi(Map<String, Map<String, GlobalVariableValue>> globalVariableValue) {
		Map<String, Map<String, ApiGlobalVariableValue>> result = new HashMap<String, Map<String, ApiGlobalVariableValue>>();
		if (globalVariableValue != null) {
			for (String e : globalVariableValue.keySet()) {
				if (e != null) {
					if (!result.containsKey(e)) result.put(e, new HashMap<String, ApiGlobalVariableValue>());
					for (GlobalVariableValue gvv : globalVariableValue.get(e).values()) {
						result.get(e).put(gvv.getPk().getEnvId(), mapEntityToApi(gvv));
					}
				}
			}
		}
		return result;
	}

	public static GlobalVariableValue mapApiToEntity(ApiGlobalVariableValue globalVariableValue) {
		GlobalVariableValue result = null;
		if (globalVariableValue != null) {
			GlobalVariableValue n = new GlobalVariableValue();
			n.getPk().setGlobalVariableKey(globalVariableValue.globalVariableKey);
			n.getPk().setEnvId(globalVariableValue.envId);
			n.setNewValue(globalVariableValue.newValue);
			n.setProtected(globalVariableValue.isProtected);
			result = n;
		}
		return result;
	}

}
