package com.opyruso.propertiesmanager.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pm-api/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IAdminResources {

	@GET
	@Path("/users/{filter}")
	public Response getUsers(@PathParam("filter") String filter) throws WebApplicationException;

	@GET
	@Path("/right/demands")
	public Response getAllRightRequests() throws WebApplicationException;

	@GET
	@Path("/users/{userId}/app/{appId}/env/{envId}/level/{level}")
	public Response toggleRight(@PathParam("userId") String userId, @PathParam("appId") String appId, @PathParam("envId") String envId, @PathParam("level") String level) throws WebApplicationException;

	@GET
	@Path("/users/{userId}/app/{appId}/env/{envId}/level/{level}/accept")
	public Response acceptRight(@PathParam("userId") String userId, @PathParam("appId") String appId, @PathParam("envId") String envId, @PathParam("level") String level) throws WebApplicationException ;

	@GET
	@Path("/users/{userId}/app/{appId}/env/{envId}/level/{level}/refuse")
	public Response refuseRight(@PathParam("userId") String userId, @PathParam("appId") String appId, @PathParam("envId") String envId, @PathParam("level") String level) throws WebApplicationException;	

}

