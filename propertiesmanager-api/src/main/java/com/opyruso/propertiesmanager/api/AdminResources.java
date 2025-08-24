package com.opyruso.propertiesmanager.api;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.ApiUserDetails;
import com.opyruso.propertiesmanager.services.IAdminService;
import com.opyruso.propertiesmanager.utils.KeycloakAttributesUtils;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;

@Authenticated
public class AdminResources implements IAdminResources {

        @Inject
        protected JsonWebToken jwt;

        @Inject
        protected IAdminService adminService;

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

}
