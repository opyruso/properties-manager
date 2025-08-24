package com.opyruso.propertiesmanager.api.entity.response;

import java.util.List;
import java.util.Map;

import com.opyruso.propertiesmanager.api.entity.ApiGlobalVariable;
import com.opyruso.propertiesmanager.api.entity.ApiGlobalVariableValue;

public class ApiAdminGlobalVariableResponse {

	public List<ApiGlobalVariable> keys;
	
	public Map<String, Map<String, ApiGlobalVariableValue>> values;
	
}

