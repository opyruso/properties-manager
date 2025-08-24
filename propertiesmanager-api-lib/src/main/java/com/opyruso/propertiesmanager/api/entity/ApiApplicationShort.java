package com.opyruso.propertiesmanager.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opyruso.propertiesmanager.data.entity.Application;

public class ApiApplicationShort {

	public String appId;
	public String appLabel;
	public String productOwner;
	public Map<String, String> versions;
	public Map<String, Long> lastReleaseDates;

	public static ApiApplicationShort mapEntityToApi(Application application) {
		ApiApplicationShort result = new ApiApplicationShort();
		result.appId = application.getPk().getAppId();
		result.appLabel = application.getAppLabel();
		result.productOwner = application.getProductOwner();
		result.versions = application.getVersions();
		result.lastReleaseDates = application.getLastReleaseDates();
		return result;
	}

	public static List<ApiApplicationShort> mapEntityToApi(List<Application> applications) {
		List<ApiApplicationShort> result = new ArrayList<ApiApplicationShort>();
		if (applications != null) {
			for (Application o : applications) {
				result.add(mapEntityToApi(o));
			}
		}
		return result;
	}

}
