package com.opyruso.propertiesmanager.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opyruso.propertiesmanager.constants.StatusEnum;
import com.opyruso.propertiesmanager.data.entity.Application;

public class ApiApplicationFull {

	public String appId;
	public String appLabel;
	public String productOwner;

        public List<String> existingVersions = new ArrayList<String>();
        public Map<String, String> versions;
        public Map<String, Long> lastReleaseDates;
        public StatusEnum appStatus;
        public StatusEnum versionStatus;

        public List<ApiProperty> properties = null;
        public Map<String, Map<String, Map<String, ApiPropertyValue>>> propertiesValue = null;

	public static ApiApplicationFull mapEntityToApi(Application applicationDetails) {
		ApiApplicationFull result = new ApiApplicationFull();
		result.appId = applicationDetails.getPk().getAppId();
		result.appLabel = applicationDetails.getAppLabel();
                result.productOwner = applicationDetails.getProductOwner();
                result.existingVersions = applicationDetails.getExistingVersions();
                result.versions = applicationDetails.getVersions();
                result.lastReleaseDates = applicationDetails.getLastReleaseDates();
                result.appStatus = applicationDetails.getStatus();
                result.properties = ApiProperty.mapEntityToApi(applicationDetails.getProperties());
                result.propertiesValue = ApiPropertyValue.mapEntityToApi(applicationDetails.getPropertiesValue());
                return result;
        }

}
