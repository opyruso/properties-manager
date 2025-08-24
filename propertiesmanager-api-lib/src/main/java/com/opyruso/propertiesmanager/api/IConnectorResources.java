package com.opyruso.propertiesmanager.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.opyruso.propertiesmanager.api.entity.request.ApiAddOrUpdateFileRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiGenerateConfigRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiNewApplicationRequest;
import com.opyruso.propertiesmanager.api.entity.response.ApiGenerateConfigResponse;

@Path("/pm-api/connector")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IConnectorResources {

	@PUT
	@Path("/app")
	Response createApplication(ApiNewApplicationRequest request) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}/version/{numVersion}")
	Response addVersion(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, ApiAddOrUpdateFileRequest request) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}/version/{numVersion}/env/{envId}")
	Response addInstalledVersion(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, @PathParam("envId") String envId) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}/version/{numVersion}/file")
	Response addOrUpdateFile(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, ApiAddOrUpdateFileRequest request) throws WebApplicationException;

	@POST
	@Path("/updatefile")
	ApiGenerateConfigResponse getGeneratedConfig(ApiGenerateConfigRequest request) throws WebApplicationException;

}