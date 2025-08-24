package com.opyruso.propertiesmanager.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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