package com.opyruso.propertiesmanager.services.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;

import com.opyruso.propertiesmanager.constants.EnvironmentConfig;
import com.opyruso.propertiesmanager.services.IKeycloakService;
import com.opyruso.propertiesmanager.utils.KeycloakProvider;

import io.quarkus.logging.Log;

@ApplicationScoped
public class KeycloakService implements IKeycloakService {

	protected static String admin_realm = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.realm", String.class);

	@Inject
	protected EnvironmentConfig environmentConfig;

	@Override
	public String getUsername(String userId) throws WebApplicationException {
		try (Keycloak keycloak = KeycloakProvider.provide()) {
			RealmResource realm = keycloak.realm(admin_realm);
			String result = realm.users().get(userId).toRepresentation().getUsername();
			return result;
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
