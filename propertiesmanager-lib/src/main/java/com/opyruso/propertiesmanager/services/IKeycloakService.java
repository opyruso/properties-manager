package com.opyruso.propertiesmanager.services;

import jakarta.ws.rs.WebApplicationException;

public interface IKeycloakService {

	public String getUsername(String userId) throws WebApplicationException;

}
