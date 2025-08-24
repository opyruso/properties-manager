package com.opyruso.propertiesmanager.services;

import javax.ws.rs.WebApplicationException;

import com.opyruso.propertiesmanager.data.entity.UserRightsDemand;

public interface IKeycloakAdminService {

	void addRight(UserRightsDemand request) throws WebApplicationException;

	void removeRight(UserRightsDemand request) throws WebApplicationException;

	void toggleRight(UserRightsDemand request) throws WebApplicationException;

}
