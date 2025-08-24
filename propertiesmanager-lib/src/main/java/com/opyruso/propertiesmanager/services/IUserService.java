package com.opyruso.propertiesmanager.services;

import java.util.List;

import jakarta.ws.rs.WebApplicationException;

import com.opyruso.propertiesmanager.api.entity.ApiUserRightsDemand;

public interface IUserService {

	List<ApiUserRightsDemand> getRightsDemands() throws WebApplicationException;

	List<ApiUserRightsDemand> getRightsDemands(String userId) throws WebApplicationException;

	public boolean createRightsDemand(ApiUserRightsDemand request) throws WebApplicationException;

	public void removeRightsDemand(ApiUserRightsDemand request) throws WebApplicationException;

}
