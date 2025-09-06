package com.opyruso.propertiesmanager.api.entity.request;

import com.opyruso.propertiesmanager.data.entity.Application;
import com.opyruso.propertiesmanager.constants.StatusEnum;

public class ApiApplicationUpdateRequest {

        public String appId;
        public String appLabel;
        public String productOwner;
        public StatusEnum status;

	public static Application mapApiToEntity(ApiApplicationUpdateRequest request) {
                Application result = new Application();
                result.getPk().setAppId(request.appId);
                result.setAppLabel(request.appLabel);
                result.setProductOwner(request.productOwner);
                result.setStatus(request.status);
                return result;
        }

}
