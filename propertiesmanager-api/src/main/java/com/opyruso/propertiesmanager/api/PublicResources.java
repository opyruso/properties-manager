package com.opyruso.propertiesmanager.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pm-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PublicResources {
	
	@OPTIONS
	@Path("{path: .*}")
	public Response preflight(@PathParam(value = "path") String path) {
		return Response.ok().build();
	}

}
