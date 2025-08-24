package com.opyruso.propertiesmanager.api.entity;

import java.util.ArrayList;
import java.util.List;

import com.opyruso.propertiesmanager.data.entity.UserRightsDemand;

public class ApiUserRightsDemand {

	public String userId;
	public String username;

	public String appId;
	public String envId;
	public String level;

	public static UserRightsDemand mapApiToEntity(ApiUserRightsDemand rightDemand) {
		UserRightsDemand result = null;
		if (rightDemand != null) {
			UserRightsDemand n = new UserRightsDemand();
			n.getPk().setUserId(rightDemand.userId);
			n.getPk().setAppId(rightDemand.appId);
			n.getPk().setEnvId(rightDemand.envId);
			n.getPk().setLevel(rightDemand.level);
			n.setUsername(rightDemand.username);
			result = n;
		}
		return result;
	}

	public static ApiUserRightsDemand mapEntityToApi(UserRightsDemand rightDemand) {
		ApiUserRightsDemand result = null;
		if (rightDemand != null) {
			ApiUserRightsDemand n = new ApiUserRightsDemand();
			n.userId = rightDemand.getPk().getUserId();
			n.appId = rightDemand.getPk().getAppId();
			n.envId = rightDemand.getPk().getEnvId();
			n.level = rightDemand.getPk().getLevel();
			n.username = rightDemand.getUsername();
			result = n;
		}
		return result;
	}

	public static List<UserRightsDemand> mapApiToEntity(List<ApiUserRightsDemand> request) {
		List<UserRightsDemand> result = new ArrayList<UserRightsDemand>();
		if (request != null) {
			for (ApiUserRightsDemand o : request) {
				result.add(mapApiToEntity(o));
			}
		}
		return result;
	}

	public static List<ApiUserRightsDemand> mapEntityToApi(List<UserRightsDemand> request) {
		List<ApiUserRightsDemand> result = new ArrayList<ApiUserRightsDemand>();
		if (request != null) {
			for (UserRightsDemand o : request) {
				result.add(mapEntityToApi(o));
			}
		}
		return result;
	}
}
