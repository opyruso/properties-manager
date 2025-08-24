package com.opyruso.propertiesmanager.api;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.ApiUserDetails;
import com.opyruso.propertiesmanager.api.entity.ApiUserRightsDemand;
import com.opyruso.propertiesmanager.services.IAdminService;
import com.opyruso.propertiesmanager.services.IApplicationService;
import com.opyruso.propertiesmanager.services.IUserService;
import com.opyruso.propertiesmanager.utils.KeycloakAttributesUtils;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;

@Authenticated
public class AdminResources implements IAdminResources {

	@Inject
	protected JsonWebToken jwt;

	@Inject
	protected IAdminService adminService;

	@Inject
	protected IUserService userService;

	@Inject
	protected IApplicationService applicationService;

	@Override
	public Response getUsers(@PathParam("filter") String filter) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			List<ApiUserDetails> response = adminService.getUsers(filter);

			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response getAllRightRequests() throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			List<ApiUserRightsDemand> response = adminService.getRightDemands();

			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response toggleRight(@PathParam("userId") String userId, @PathParam("appId") String appId, @PathParam("envId") String envId, @PathParam("level") String level) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			adminService.toggleRight(userId, appId, envId, level);

			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response acceptRight(@PathParam("userId") String userId, @PathParam("appId") String appId, @PathParam("envId") String envId, @PathParam("level") String level) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			adminService.addRight(userId, appId, envId, level);
			adminService.removeDemand(userId, appId, envId, level);

			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response refuseRight(@PathParam("userId") String userId, @PathParam("appId") String appId, @PathParam("envId") String envId, @PathParam("level") String level) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		try {
			adminService.removeDemand(userId, appId, envId, level);

			return Response.noContent().build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
