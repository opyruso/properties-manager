package com.opyruso.propertiesmanager.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.opyruso.propertiesmanager.api.entity.request.ApiAddOrUpdateFileRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiAdminGlobalVariableRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiApplicationUpdateRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiPropertyUpdateRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiTestFileRequest;

@Path("/pm-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IApplicationResources {

	@GET
	@Path("/apps")
	public Response apps() throws WebApplicationException;

	@GET
	@Path("/app/{appId}/versions")
	public Response versions(@PathParam("appId") String appId) throws WebApplicationException;

	@GET
	@Path("/app/{appId}/version/{numVersion}")
	public Response app(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion) throws WebApplicationException;

	@GET
	@Path("/app/{appId}/version/{toVersion}/replaceby/{numVersion}")
	public Response replacePropertiesByVersion(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, @PathParam("toVersion") String toVersion) throws WebApplicationException;

	@GET
	@Path("/app/{appId}/version/{numVersion}/filenames")
	public Response filenames(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion) throws WebApplicationException;

	@GET
	@Path("/app/{appId}/version/{numVersion}/files")
	public Response files(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}/version/{numVersion}/file")
	public Response addOrUpdateFile(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, ApiAddOrUpdateFileRequest request) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}")
	public Response appUpdate(@PathParam("appId") String appId, ApiApplicationUpdateRequest request) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}/updateproperty")
	public Response appPropertyUpdate(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}/addproperty")
	public Response appPropertyAdd(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException;

	@DELETE
	@Path("/app/{appId}/property/deleteall")
	public Response appPropertyAllEnvDelete(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException;

	@DELETE
	@Path("/app/{appId}/property/deletepermanent")
	public Response appPropertyPermanentDelete(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException;

	@PUT
	@Path("/app/{appId}/property/addall")
	public Response appPropertyAllEnvAdd(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException;

	@POST
	@Path("/test/process")
	public Response testFile(ApiTestFileRequest request) throws WebApplicationException;

	@GET
	@Path("/globalvariables")
	public Response getGlobalVariables() throws WebApplicationException;

	@POST
	@Path("/globalvariables")
	public Response addGlobalVariable(ApiAdminGlobalVariableRequest request) throws WebApplicationException;

	@PUT
	@Path("/globalvariables")
	public Response updateGlobalVariable(ApiAdminGlobalVariableRequest request) throws WebApplicationException;

	@PUT
	@Path("/globalvariables/protection")
	Response updateGlobalVariableProtection(ApiAdminGlobalVariableRequest request) throws WebApplicationException;

	@DELETE
	@Path("/globalvariables")
	public Response removeGlobalVariable(ApiAdminGlobalVariableRequest request) throws WebApplicationException;


}