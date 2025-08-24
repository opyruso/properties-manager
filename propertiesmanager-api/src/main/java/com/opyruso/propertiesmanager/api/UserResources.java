package com.opyruso.propertiesmanager.api;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.opyruso.propertiesmanager.api.entity.ApiUserRightsDemand;
import com.opyruso.propertiesmanager.api.entity.response.ApiDemandNewRightResponse;
import com.opyruso.propertiesmanager.services.IUserService;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;

@Authenticated
public class UserResources implements IUserResources {

	@Inject
	protected IUserService userService;

	@Override
	public Response getRightRequests(@PathParam("userId") String userId) throws WebApplicationException {
		try {
			return Response.ok(userService.getRightsDemands(userId)).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Response demandNewRight(@PathParam("userId") String userId, ApiUserRightsDemand request) throws WebApplicationException {
		try {
			ApiDemandNewRightResponse response = new ApiDemandNewRightResponse();
			request.userId = userId;
			response.result = userService.createRightsDemand(request);
			return Response.ok(response).build();
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
