package com.opyruso.propertiesmanager.api.entity.request;

import com.opyruso.propertiesmanager.data.entity.Application;

public class ApiApplicationUpdateRequest {

	public String appId;
	public String appLabel;
	public String productOwner;

	public static Application mapApiToEntity(ApiApplicationUpdateRequest request) {
		Application result = new Application();
		result.getPk().setAppId(request.appId);
		result.setAppLabel(request.appLabel);
		result.setProductOwner(request.productOwner);
		return result;
	}

}
