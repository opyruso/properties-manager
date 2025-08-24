package com.opyruso.propertiesmanager.api;

import java.util.Base64;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.request.ApiAddOrUpdateFileRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiGenerateConfigRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiNewApplicationRequest;
import com.opyruso.propertiesmanager.api.entity.response.ApiGenerateConfigResponse;
import com.opyruso.propertiesmanager.services.IApplicationService;
import com.opyruso.propertiesmanager.services.ITransformerService;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.utils.FileUtils;

import io.quarkus.logging.Log;

@RolesAllowed("connector")
public class ConnectorResources implements IConnectorResources {

	@Inject
	protected JsonWebToken jwt;

	@Inject
	protected IApplicationService applicationService;

	@Inject
	protected ITransformerService transformerService;

	@Override
	public Response createApplication(ApiNewApplicationRequest request) throws WebApplicationException {
		try {
			applicationService.createNewApplication(request);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response addVersion(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, ApiAddOrUpdateFileRequest request) throws WebApplicationException {
		try {
			if (numVersion.toLowerCase().contains("snapshot")) {
				numVersion = "snapshot";
			}
			String fileContentAsString = new String(Base64.getDecoder().decode(request.contentAsBase64.getBytes()));
			applicationService.createNewApplicationVersion(appId, numVersion, request.filename, fileContentAsString);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response addInstalledVersion(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, @PathParam("envId") String envId) throws WebApplicationException {
		try {
			if (numVersion.toLowerCase().contains("snapshot")) {
				numVersion = "snapshot";
			}
			applicationService.addInstalledApplicationVersion(appId, numVersion, envId);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response addOrUpdateFile(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, ApiAddOrUpdateFileRequest request) throws WebApplicationException {
		try {
			if (numVersion.toLowerCase().contains("snapshot")) {
				numVersion = "snapshot";
			}
			applicationService.addOrUpdateFile(appId, numVersion, request.filename, request.contentAsBase64);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ApiGenerateConfigResponse getGeneratedConfig(ApiGenerateConfigRequest request) throws WebApplicationException {
		try {
			if (request.version.toLowerCase().contains("snapshot")) {
				request.version = "snapshot";
			}
			String fileContentAsString = new String(Base64.getDecoder().decode(request.fileContentAsBase64.getBytes()));
			ITransformerFactory factory = transformerService.processTransformation(request.appId, request.version, request.envId, request.filename, FileUtils.createTempFile(fileContentAsString));
			ApiGenerateConfigResponse response = new ApiGenerateConfigResponse();
			response.fileContentAsBase64 = Base64.getEncoder().encodeToString(FileUtils.getFileContent(factory.getResult()).getBytes());
			response.logs = factory.getLog();
			return response;
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
