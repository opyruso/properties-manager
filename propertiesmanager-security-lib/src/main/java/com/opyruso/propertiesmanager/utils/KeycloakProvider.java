package com.opyruso.propertiesmanager.utils;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

@ApplicationScoped
public class KeycloakProvider {

	protected static String server_url = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.server_url", String.class);
	protected static String realm = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.realm", String.class);
	protected static String client_id = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.client_id", String.class);;
	protected static String client_secret = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.client_secret", String.class);;

	public static Keycloak provide() {
		Keycloak keycloak = KeycloakBuilder.builder().serverUrl(server_url).realm(realm).clientId(client_id).clientSecret(client_secret)
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS).build();

		return keycloak;
	}
}