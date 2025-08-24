package com.opyruso.propertiesmanager.services;

import javax.ws.rs.WebApplicationException;

public interface IKeycloakService {

	public String getUsername(String userId) throws WebApplicationException;

}
