package com.opyruso.propertiesmanager.services.impl;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;

import com.opyruso.propertiesmanager.api.entity.ApiUserRightsDemand;
import com.opyruso.propertiesmanager.constants.EnvironmentConfig;
import com.opyruso.propertiesmanager.data.IDemandDataService;
import com.opyruso.propertiesmanager.services.IKeycloakAdminService;
import com.opyruso.propertiesmanager.services.IKeycloakService;
import com.opyruso.propertiesmanager.services.IUserService;

@ApplicationScoped
public class UserService implements IUserService {

	@Inject
	protected EnvironmentConfig environmentConfig;

	@Inject
	protected IKeycloakAdminService keycloakAdminService;

	@Inject
	protected IKeycloakService keycloakService;

	@Inject
	protected IDemandDataService dataService;

	@Override
	public List<ApiUserRightsDemand> getRightsDemands() throws WebApplicationException {
		try {
			List<ApiUserRightsDemand> result = ApiUserRightsDemand.mapEntityToApi(dataService.selectDemands());
			return result;
		} catch (Exception e) {
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<ApiUserRightsDemand> getRightsDemands(String userId) throws WebApplicationException {
		try {
			List<ApiUserRightsDemand> result = ApiUserRightsDemand.mapEntityToApi(dataService.selectDemands(userId));
			return result;
		} catch (Exception e) {
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public boolean createRightsDemand(ApiUserRightsDemand request) throws WebApplicationException {
		try {
			boolean response = false;
			request.username = keycloakService.getUsername(request.userId);
			if (environmentConfig.environments().get(request.envId).hasAutoRegisterRights) {
				keycloakAdminService.toggleRight(ApiUserRightsDemand.mapApiToEntity(request));
				response = true;
			} else {
				dataService.insertOrUpdateDemand(ApiUserRightsDemand.mapApiToEntity(request));
				response = false;
			}
			return response;
		} catch (Exception e) {
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void removeRightsDemand(ApiUserRightsDemand request) throws WebApplicationException {
		try {
			dataService.deleteDemand(ApiUserRightsDemand.mapApiToEntity(request));
		} catch (Exception e) {
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
