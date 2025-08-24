package com.opyruso.propertiesmanager.api.entity;

import java.util.ArrayList;
import java.util.List;

import com.opyruso.propertiesmanager.data.entity.GlobalVariable;

public class ApiGlobalVariable {

	public String globalVariableKey;

	public static ApiGlobalVariable mapEntityToApi(GlobalVariable globalVariable) {
		ApiGlobalVariable result = null;
		if (globalVariable != null) {
			ApiGlobalVariable n = new ApiGlobalVariable();
			n.globalVariableKey = globalVariable.getPk().getGlobalVariableKey();
			result = n;
		}
		return result;
	}

	public static List<ApiGlobalVariable> mapEntityToApi(List<GlobalVariable> globalVariables) {
		List<ApiGlobalVariable> result = new ArrayList<ApiGlobalVariable>();
		if (globalVariables != null) {
			for (GlobalVariable gv : globalVariables) {
				result.add(mapEntityToApi(gv));
			}
		}
		return result;
	}

	public static GlobalVariable mapApiToEntity(ApiGlobalVariable globalVariable) {
		GlobalVariable result = null;
		if (globalVariable != null) {
			GlobalVariable n = new GlobalVariable();
			n.getPk().setGlobalVariableKey(globalVariable.globalVariableKey);
			result = n;
		}
		return result;
	}

}
