package com.opyruso.propertiesmanager.api.entity.request;

import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.propertyStatusEnum;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;

public class ApiPropertyUpdateRequest {

	public String appId;
	public String numVersion;
	public String filename;
	public String propertyKey;
	public String envId;

	public OperationTypeEnum operationType;
	public propertyStatusEnum status;
	public Boolean isProtected;
	public String newValue;

	public static PropertyValue mapApiToEntity(ApiPropertyUpdateRequest request) {
		PropertyValue result = new PropertyValue();
		result.getPk().setAppId(request.appId);
		result.getPk().setNumVersion(request.numVersion);
		result.getPk().setFilename(request.filename);
		result.getPk().setPropertyKey(request.propertyKey);
		result.getPk().setEnvId(request.envId);
		if (request.operationType != null) result.setOperationType(request.operationType);
		if (request.status != null) result.setStatus(request.status);
		result.setNewValue(request.newValue);
		if (request.isProtected != null) result.setProtected(request.isProtected);
		return result;
	}

}
