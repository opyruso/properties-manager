package com.opyruso.propertiesmanager.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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