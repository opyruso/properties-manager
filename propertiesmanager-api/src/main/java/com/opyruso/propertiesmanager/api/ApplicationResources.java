package com.opyruso.propertiesmanager.api;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.ApiApplicationFull;
import com.opyruso.propertiesmanager.api.entity.ApiApplicationShort;
import com.opyruso.propertiesmanager.api.entity.ApiProperty;
import com.opyruso.propertiesmanager.api.entity.request.ApiAddOrUpdateFileRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiAdminGlobalVariableRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiApplicationUpdateRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiPropertyUpdateRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiTestFileRequest;
import com.opyruso.propertiesmanager.api.entity.response.ApiAdminGlobalVariableResponse;
import com.opyruso.propertiesmanager.api.entity.response.ApiTestFileResponse;
import com.opyruso.propertiesmanager.constants.EnvironmentConfig;
import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.propertyStatusEnum;
import com.opyruso.propertiesmanager.constants.propertyTypeEnum;
import com.opyruso.propertiesmanager.services.IApplicationService;
import com.opyruso.propertiesmanager.services.ITransformerService;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.utils.FileUtils;
import com.opyruso.propertiesmanager.utils.KeycloakAttributesUtils;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;

@Authenticated
public class ApplicationResources implements IApplicationResources {

	@Inject
	protected EnvironmentConfig environmentConfig;

	@Inject
	protected JsonWebToken jwt;

	@Inject
	protected IApplicationService applicationService;

	@Inject
	protected ITransformerService transformerService;

	@Override
	public Response apps() throws WebApplicationException {
		try {
			List<ApiApplicationShort> response = applicationService.getApplications();
			if (response == null) {
				throw new WebApplicationException(HttpStatus.SC_NOT_FOUND);
			}
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response versions(@PathParam("appId") String appId) throws WebApplicationException {
		try {
			List<String> response = applicationService.getApplicationVersions(appId);
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response app(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion) throws WebApplicationException {
		try {
			ApiApplicationFull response = applicationService.getApplicationDetails(appId, numVersion);
			if (response == null) {
				throw new WebApplicationException(HttpStatus.SC_NOT_FOUND);
			}
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response filenames(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion) throws WebApplicationException {
		try {
			List<String> response = applicationService.getApplicationFilenames(appId, numVersion);
			if (response == null) {
				throw new WebApplicationException(HttpStatus.SC_NOT_FOUND);
			}
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response files(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion) throws WebApplicationException {
		try {
			Map<String, String> response = applicationService.getApplicationFiles(appId, numVersion);
			if (response == null) {
				throw new WebApplicationException(HttpStatus.SC_NOT_FOUND);
			}
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response addOrUpdateFile(@PathParam("appId") String appId, @PathParam("numVersion") String numVersion, ApiAddOrUpdateFileRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheck(jwt, appId);
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
	public Response appUpdate(@PathParam("appId") String appId, ApiApplicationUpdateRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			applicationService.appUpdate(appId, request);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response appPropertyUpdate(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheck(jwt, appId, request.envId, "w");
		try {
			request.appId = appId;
			applicationService.propertyUpdate(request);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response appPropertyAdd(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException {
		try {
			request.appId = appId;
			ApiProperty p = new ApiProperty();
			p.appId = request.appId;
			p.numVersion = request.numVersion;
			p.filename = request.filename;
			p.propertyKey = request.propertyKey;
			p.propertyType = propertyTypeEnum.NEW;
			applicationService.propertyAdd(p);
			request.status = propertyStatusEnum.TO_VALIDATE;
			request.operationType = OperationTypeEnum.ADD;
			request.newValue = request.newValue;
			for (String envId : environmentConfig.environments().keySet()) {
				request.envId = envId;
				applicationService.propertyUpdate(request);
			}
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response appPropertyAllEnvDelete(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException {
		for (String envId : environmentConfig.environments().keySet()) KeycloakAttributesUtils.securityCheck(jwt, appId, envId, "w");
		try {
			applicationService.propertyAllEnvDelete(appId, request.numVersion, request.filename, request.propertyKey);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response appPropertyPermanentDelete(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException {
		for (String envId : environmentConfig.environments().keySet()) KeycloakAttributesUtils.securityCheck(jwt, appId, envId, "w");
		try {
			applicationService.propertyPermanentDelete(appId, request.numVersion, request.filename, request.propertyKey);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response appPropertyAllEnvAdd(@PathParam("appId") String appId, ApiPropertyUpdateRequest request) throws WebApplicationException {
		for (String envId : environmentConfig.environments().keySet()) KeycloakAttributesUtils.securityCheck(jwt, appId, envId, "w");
		try {
			applicationService.propertyAllEnvAdd(appId, request.numVersion, request.filename, request.propertyKey);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response testFile(ApiTestFileRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheck(jwt, request.appId, request.envId, "r");
		try {
			String fileContentAsString = new String(Base64.getDecoder().decode(request.fileContentAsBase64.getBytes()));
			ITransformerFactory factory = transformerService.processTransformation(request.appId, request.version, request.envId, request.filename, FileUtils.createTempFile(fileContentAsString));
			ApiTestFileResponse response = new ApiTestFileResponse();
			response.fileContentAsBase64 = Base64.getEncoder().encodeToString(FileUtils.getFileContent(factory.getResult()).getBytes());
			response.logs = factory.getLog();
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response getGlobalVariables() throws WebApplicationException {
		try {
			ApiAdminGlobalVariableResponse response = new ApiAdminGlobalVariableResponse();
			response.keys = applicationService.getGlobalVariables();
			response.values = applicationService.getGlobalVariableValues();
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response addGlobalVariable(ApiAdminGlobalVariableRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			applicationService.addGlobalVariable(request.key);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response updateGlobalVariable(ApiAdminGlobalVariableRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			applicationService.updateGlobalVariable(request.key, request.env, request.value);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response updateGlobalVariableProtection(ApiAdminGlobalVariableRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			applicationService.updateGlobalVariableProtection(request.key, request.env, request.isProtected);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response removeGlobalVariable(ApiAdminGlobalVariableRequest request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			applicationService.removeGlobalVariable(request.key);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response replacePropertiesByVersion(String appId, String numVersion, String toVersion) throws WebApplicationException {
		for (String envId : environmentConfig.environments().keySet()) KeycloakAttributesUtils.securityCheck(jwt, appId, envId, "w");
		try {
			applicationService.replaceAllPropertiesFromVersionToVersion(appId, numVersion, toVersion);
			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
