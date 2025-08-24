package com.opyruso.propertiesmanager.services;

import jakarta.ws.rs.WebApplicationException;

import com.opyruso.propertiesmanager.data.entity.UserRightsDemand;

public interface IKeycloakAdminService {

	void addRight(UserRightsDemand request) throws WebApplicationException;

	void removeRight(UserRightsDemand request) throws WebApplicationException;

	void toggleRight(UserRightsDemand request) throws WebApplicationException;

}
