package com.opyruso.propertiesmanager.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opyruso.propertiesmanager.api.entity.ApiEnvironment;
import com.opyruso.propertiesmanager.api.entity.ApiUserAttributeRights;
import com.opyruso.propertiesmanager.constants.EnvironmentConfig;
import com.opyruso.propertiesmanager.data.entity.UserRightsDemand;
import com.opyruso.propertiesmanager.services.IKeycloakAdminService;
import com.opyruso.propertiesmanager.utils.KeycloakProvider;

import io.quarkus.logging.Log;

@ApplicationScoped
public class KeycloakAdminService implements IKeycloakAdminService {

	protected static String admin_realm = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.realm", String.class);

	@Inject
	protected EnvironmentConfig environmentConfig;

	@Override
	public void addRight(UserRightsDemand request) throws WebApplicationException {
		if (!request.getPk().getLevel().equals("r") && !request.getPk().getLevel().equals("w")) throw new WebApplicationException(HttpStatus.SC_BAD_REQUEST);
		try (Keycloak keycloak = KeycloakProvider.provide()) {
			RealmResource realm = keycloak.realm(admin_realm);
			UserRepresentation userRepresentation = new UserRepresentation();
			userRepresentation.setAttributes(realm.users().get(request.getPk().getUserId()).toRepresentation().getAttributes());
			if (userRepresentation.getAttributes() == null) {
				userRepresentation.setAttributes(new HashMap<String, List<String>>());
			}
			if (!userRepresentation.getAttributes().containsKey("propertiesmanager_rights")) {
				userRepresentation.getAttributes().put("propertiesmanager_rights", new ArrayList<String>());
			}
			if (userRepresentation.getAttributes().get("propertiesmanager_rights").size() == 0) {
				userRepresentation.getAttributes().get("propertiesmanager_rights").add("{\"admin\": false}");
			}
			String rightsAsString = userRepresentation.getAttributes().get("propertiesmanager_rights").get(0);
			ObjectMapper mapper = new ObjectMapper();
			ApiUserAttributeRights rights = mapper.readValue(rightsAsString, ApiUserAttributeRights.class);
			
			if (request.getPk().getAppId().equals("all_app")) {
				if (rights.all_app == null) {
					rights.all_app = new HashMap<String, String>();
				}
				if (rights.all_app.containsKey(request.getPk().getEnvId())) {
					String actualLevel = rights.all_app.get(request.getPk().getEnvId());
					actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
					actualLevel += request.getPk().getLevel();
					rights.all_app.put(request.getPk().getEnvId(), actualLevel);	
				} else {
					rights.all_app.put(request.getPk().getEnvId(), request.getPk().getLevel());	
				}

			} else if (request.getPk().getEnvId().equals("all_env")) {
				if (rights.app == null) {
					rights.app = new HashMap<String, Map<String, String>>();
				}
				if (!rights.app.containsKey(request.getPk().getAppId())) {
					rights.app.put(request.getPk().getAppId(), new HashMap<String, String>());
				}
				if (!rights.app.get(request.getPk().getAppId()).containsKey("all_env")) {
					rights.app.get(request.getPk().getAppId()).put("all_env", "");
				}
				if (rights.app.get(request.getPk().getAppId()).containsKey("all_env")) {
					String actualLevel = rights.app.get(request.getPk().getAppId()).get("all_env");
					actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
					actualLevel += request.getPk().getLevel();
					rights.app.get(request.getPk().getAppId()).put("all_env", actualLevel);
				}

			} else {
				if (rights.app == null) {
					rights.app = new HashMap<String, Map<String, String>>();
				}
				if (!rights.app.containsKey(request.getPk().getAppId())) {
					rights.app.put(request.getPk().getAppId(), new HashMap<String, String>());
				}
				if (!rights.app.get(request.getPk().getAppId()).containsKey(request.getPk().getEnvId())) {
					rights.app.get(request.getPk().getAppId()).put(request.getPk().getEnvId(), "");
				}
				if (rights.app.get(request.getPk().getAppId()).containsKey(request.getPk().getEnvId())) {
					String actualLevel = rights.app.get(request.getPk().getAppId()).get(request.getPk().getEnvId());
					actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
					actualLevel += request.getPk().getLevel();
					rights.app.get(request.getPk().getAppId()).put(request.getPk().getEnvId(), actualLevel);
				}
			}

			rightsAsString = mapper.writeValueAsString(rights);
			userRepresentation.getAttributes().put("propertiesmanager_rights", Arrays.asList(rightsAsString));
			realm.users().get(request.getPk().getUserId()).update(userRepresentation);
	
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void removeRight(UserRightsDemand request) throws WebApplicationException {
		if (!request.getPk().getLevel().equals("r") && !request.getPk().getLevel().equals("w")) throw new WebApplicationException(HttpStatus.SC_BAD_REQUEST);
		try (Keycloak keycloak = KeycloakProvider.provide()) {
			RealmResource realm = keycloak.realm(admin_realm);
			UserRepresentation userRepresentation = new UserRepresentation();
			userRepresentation.setAttributes(realm.users().get(request.getPk().getUserId()).toRepresentation().getAttributes());
			if (userRepresentation.getAttributes() == null) {
				userRepresentation.setAttributes(new HashMap<String, List<String>>());
			}
			if (!userRepresentation.getAttributes().containsKey("propertiesmanager_rights")) {
				userRepresentation.getAttributes().put("propertiesmanager_rights", new ArrayList<String>());
			}
			if (userRepresentation.getAttributes().get("propertiesmanager_rights").size() == 0) {
				userRepresentation.getAttributes().get("propertiesmanager_rights").add("{\"admin\": false}");
			}
			String rightsAsString = userRepresentation.getAttributes().get("propertiesmanager_rights").get(0);
			ObjectMapper mapper = new ObjectMapper();
			ApiUserAttributeRights rights = mapper.readValue(rightsAsString, ApiUserAttributeRights.class);
			
			if (request.getPk().getAppId().equals("all_app")) {
				if (rights.all_app == null) {
					rights.all_app = new HashMap<String, String>();
				}
				if (rights.all_app.containsKey(request.getPk().getEnvId())) {
					String actualLevel = rights.all_app.get(request.getPk().getEnvId());
					actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
					rights.all_app.put(request.getPk().getEnvId(), actualLevel);	
				}

			} else if (request.getPk().getEnvId().equals("all_env")) {
				if (rights.app == null) {
					rights.app = new HashMap<String, Map<String, String>>();
				}
				if (!rights.app.containsKey(request.getPk().getAppId())) {
					rights.app.put(request.getPk().getAppId(), new HashMap<String, String>());
				}
				if (!rights.app.get(request.getPk().getAppId()).containsKey("all_env")) {
					rights.app.get(request.getPk().getAppId()).put("all_env", "");
				}
				if (rights.app.get(request.getPk().getAppId()).containsKey("all_env")) {
					String actualLevel = rights.app.get(request.getPk().getAppId()).get("all_env");
					actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
					rights.app.get(request.getPk().getAppId()).put("all_env", actualLevel);
				}

			} else {
				if (rights.app == null) {
					rights.app = new HashMap<String, Map<String, String>>();
				}
				if (!rights.app.containsKey(request.getPk().getAppId())) {
					rights.app.put(request.getPk().getAppId(), new HashMap<String, String>());
				}
				if (!rights.app.get(request.getPk().getAppId()).containsKey(request.getPk().getEnvId())) {
					rights.app.get(request.getPk().getAppId()).put(request.getPk().getEnvId(), "");
				}
				if (rights.app.get(request.getPk().getAppId()).containsKey(request.getPk().getEnvId())) {
					String actualLevel = rights.app.get(request.getPk().getAppId()).get(request.getPk().getEnvId());
					actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
					rights.app.get(request.getPk().getAppId()).put(request.getPk().getEnvId(), actualLevel);
				}
			}
			Log.debug("old rights : " + rightsAsString);
			rightsAsString = mapper.writeValueAsString(rights);
			Log.debug("new rights : " + rightsAsString);
			userRepresentation.getAttributes().put("propertiesmanager_rights", Arrays.asList(rightsAsString));
			realm.users().get(request.getPk().getUserId()).update(userRepresentation);
	
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void toggleRight(UserRightsDemand request) throws WebApplicationException {
		if (!request.getPk().getLevel().equals("r") && !request.getPk().getLevel().equals("w")) throw new WebApplicationException(HttpStatus.SC_BAD_REQUEST);
		try (Keycloak keycloak = KeycloakProvider.provide()) {
			RealmResource realm = keycloak.realm(admin_realm);
			UserRepresentation userRepresentation = new UserRepresentation();
			userRepresentation.setAttributes(realm.users().get(request.getPk().getUserId()).toRepresentation().getAttributes());
			if (userRepresentation.getAttributes() == null) {
				userRepresentation.setAttributes(new HashMap<String, List<String>>());
			}
			if (!userRepresentation.getAttributes().containsKey("propertiesmanager_rights")) {
				userRepresentation.getAttributes().put("propertiesmanager_rights", new ArrayList<String>());
			}
			if (userRepresentation.getAttributes().get("propertiesmanager_rights").size() == 0) {
				userRepresentation.getAttributes().get("propertiesmanager_rights").add("{\"admin\": false}");
			}
			String rightsAsString = userRepresentation.getAttributes().get("propertiesmanager_rights").get(0);
			ObjectMapper mapper = new ObjectMapper();
			ApiUserAttributeRights rights = mapper.readValue(rightsAsString, ApiUserAttributeRights.class);
			
			if (request.getPk().getAppId().equals("all_app")) {
				if (rights.all_app == null) {
					rights.all_app = new HashMap<String, String>();
				}
				if (rights.all_app.containsKey(request.getPk().getEnvId())) {
					String actualLevel = rights.all_app.get(request.getPk().getEnvId());
					if (actualLevel.contains(request.getPk().getLevel())) {
						actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
						rights.all_app.put(request.getPk().getEnvId(), actualLevel);	
					} else {
						actualLevel += request.getPk().getLevel();
						rights.all_app.put(request.getPk().getEnvId(), actualLevel);
					}
				} else {
					rights.all_app.put(request.getPk().getEnvId(), request.getPk().getLevel());	
				}

			} else if (request.getPk().getEnvId().equals("all_env")) {
				if (rights.app == null) {
					rights.app = new HashMap<String, Map<String, String>>();
				}
				if (!rights.app.containsKey(request.getPk().getAppId())) {
					rights.app.put(request.getPk().getAppId(), new HashMap<String, String>());
				}
				if (!rights.app.get(request.getPk().getAppId()).containsKey("all_env")) {
					rights.app.get(request.getPk().getAppId()).put("all_env", "");
				}
				if (rights.app.get(request.getPk().getAppId()).containsKey("all_env")) {
					String actualLevel = rights.app.get(request.getPk().getAppId()).get("all_env");
					if (actualLevel.contains(request.getPk().getLevel())) {
						actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
						rights.app.get(request.getPk().getAppId()).put("all_env", actualLevel);
					} else {
						actualLevel += request.getPk().getLevel();
						rights.app.get(request.getPk().getAppId()).put("all_env", actualLevel);
					}
				}

			} else {
				if (rights.app == null) {
					rights.app = new HashMap<String, Map<String, String>>();
				}
				if (!rights.app.containsKey(request.getPk().getAppId())) {
					rights.app.put(request.getPk().getAppId(), new HashMap<String, String>());
				}
				if (!rights.app.get(request.getPk().getAppId()).containsKey(request.getPk().getEnvId())) {
					rights.app.get(request.getPk().getAppId()).put(request.getPk().getEnvId(), "");
				}
				if (rights.app.get(request.getPk().getAppId()).containsKey(request.getPk().getEnvId())) {
					String actualLevel = rights.app.get(request.getPk().getAppId()).get(request.getPk().getEnvId());
					if (actualLevel.contains(request.getPk().getLevel())) {
						actualLevel = actualLevel.replaceAll(request.getPk().getLevel(), "");
						rights.app.get(request.getPk().getAppId()).put(request.getPk().getEnvId(), actualLevel);
					} else {
						actualLevel += request.getPk().getLevel();
						rights.app.get(request.getPk().getAppId()).put(request.getPk().getEnvId(), actualLevel);
					}
				}
			}

			Log.debug("toggle : old rights : " + rightsAsString);
			rights = cleanRights(rights);
			rightsAsString = mapper.writeValueAsString(rights);
			Log.debug("toggle : new rights : " + rightsAsString);
			userRepresentation.getAttributes().put("propertiesmanager_rights", Arrays.asList(rightsAsString));
			realm.users().get(request.getPk().getUserId()).update(userRepresentation);
	
		} catch (Exception e) {
			Log.error("Error:", e);
			throw new WebApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private ApiUserAttributeRights cleanRights(ApiUserAttributeRights rights) {
		try {
			if (rights.all_app != null) {
				if (!haseActiveRight(rights.all_app)) {
					rights.all_app = null;
				}
			}
			if (rights.app != null) {
				List<String> keyList = new ArrayList<String>(rights.app.keySet());
				for (String key : keyList) {
					if (rights.all_app != null) {
						if (rights.app.get(key).containsKey("all_env") && rights.app.get(key).get("all_env").trim().equals("")) {
							rights.app.get(key).remove("all_env");
						}
					}
					if (!haseActiveRight(rights.app.get(key))) {
						rights.app.remove(key);
					}
					
				}
			}
		} catch (Exception e) {
			Log.error("Error:", e);
		}
		return rights;
	}
	
	private boolean haseActiveRight(Map<String, String> r) {
		boolean result = false;
		for (ApiEnvironment env : environmentConfig.environments().values()) {
			if (!r.containsKey(env.id) || r.get(env.id).trim().equals("")) {
				result = true;
				break;
			}
		}
		return result;
	}

}
