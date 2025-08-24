package com.opyruso.propertiesmanager.services;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.opyruso.propertiesmanager.api.entity.ApiUserDetails;
import com.opyruso.propertiesmanager.api.entity.ApiUserRightsDemand;

public interface IAdminService {

	List<ApiUserDetails> getUsers(String filter) throws WebApplicationException;

	List<ApiUserRightsDemand> getRightDemands() throws WebApplicationException;

	void toggleRight(String userId, String appId, String envId, String level) throws WebApplicationException;

	void removeDemand(String userId, String appId, String envId, String level) throws WebApplicationException;

	void addRight(String userId, String appId, String envId, String level) throws WebApplicationException;

	void removeRight(String userId, String appId, String envId, String level) throws WebApplicationException;
}
