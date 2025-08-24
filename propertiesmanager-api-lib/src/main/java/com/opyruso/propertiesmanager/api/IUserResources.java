package com.opyruso.propertiesmanager.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.opyruso.propertiesmanager.api.entity.ApiUserRightsDemand;

@Path("/pm-api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IUserResources {

	@GET
	@Path("/{userId}/right/demands")
	Response getRightRequests(@PathParam("userId") String userId) throws WebApplicationException;

	@PUT
	@Path("/{userId}/right/demand")
	Response demandNewRight(@PathParam("userId") String userId, ApiUserRightsDemand request) throws WebApplicationException;

}