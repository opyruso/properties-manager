package com.opyruso.propertiesmanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opyruso.propertiesmanager.api.entity.ApiUserAttributeRights;
import com.opyruso.propertiesmanager.api.entity.ApiUserDetails;
import com.opyruso.propertiesmanager.api.entity.ApiUserRightsDemand;
import com.opyruso.propertiesmanager.data.entity.UserRightsDemand;
import com.opyruso.propertiesmanager.services.IAdminService;
import com.opyruso.propertiesmanager.services.IKeycloakAdminService;
import com.opyruso.propertiesmanager.services.IKeycloakService;
import com.opyruso.propertiesmanager.services.IUserService;
import com.opyruso.propertiesmanager.utils.KeycloakProvider;

import io.quarkus.logging.Log;

@ApplicationScoped
public class AdminService implements IAdminService {

	protected static String admin_realm = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.realm", String.class);
	
	@Inject
	protected IKeycloakService keycloakService;
	
	@Inject
	protected IKeycloakAdminService keycloakAdminService;

	@Inject
	protected IUserService userService;

	public List<ApiUserDetails> getUsers(String filter) {
		try (Keycloak keycloak = KeycloakProvider.provide()) {
			RealmResource realm = keycloak.realm(admin_realm);
			List<ApiUserDetails> result = new ArrayList<ApiUserDetails>();
			List<UserRepresentation> kcList = realm.users().list(0, 100000);
			for (UserRepresentation kcUser : kcList) {
				if (kcUser == null || (
						(kcUser.getEmail() == null || !kcUser.getEmail().contains(filter))
						&& (kcUser.getFirstName() == null || !kcUser.getFirstName().contains(filter))
						&& (kcUser.getLastName() == null || !kcUser.getLastName().contains(filter))
						&& (kcUser.getUsername() == null || !kcUser.getUsername().contains(filter))
					)) continue;
				ApiUserDetails newUser = new ApiUserDetails();
				newUser.userId = kcUser.getId();
				newUser.name = kcUser.getUsername();
				ObjectMapper mapper = new ObjectMapper();
				ApiUserAttributeRights rights = new ApiUserAttributeRights();
				if (kcUser.getAttributes() != null
						&& kcUser.getAttributes().containsKey("propertiesmanager_rights")
						&& kcUser.getAttributes().get("propertiesmanager_rights").size() > 0
						&& !kcUser.getAttributes().get("propertiesmanager_rights").get(0).trim().equals("")) {
					rights = mapper.readValue(kcUser.getAttributes().get("propertiesmanager_rights").get(0), ApiUserAttributeRights.class);
				}
				newUser.rights = rights;
				result.add(newUser);
			}
			return result;
		} catch (Exception e) {
			Log.error("Error:", e);
			return null;
		}
	}

	@Override
	public List<ApiUserRightsDemand> getRightDemands() throws WebApplicationException {
		List<ApiUserRightsDemand> result = userService.getRightsDemands();
		for (ApiUserRightsDemand d : result) {
			d.username = keycloakService.getUsername(d.userId);
		}
		return result;
	}

	@Override
	public void toggleRight(String userId, String appId, String envId, String level) throws WebApplicationException {
		UserRightsDemand tmp = new UserRightsDemand();
		tmp.getPk().setUserId(userId);
		tmp.getPk().setAppId(appId);
		tmp.getPk().setEnvId(envId);
		tmp.getPk().setLevel(level);
		keycloakAdminService.toggleRight(tmp);
	}

	@Override
	public void removeDemand(String userId, String appId, String envId, String level) throws WebApplicationException {
		ApiUserRightsDemand tmp = new ApiUserRightsDemand();
		tmp.userId = userId;
		tmp.appId = appId;
		tmp.envId = envId;
		tmp.level = level;
		userService.removeRightsDemand(tmp);
	}

	@Override
	public void addRight(String userId, String appId, String envId, String level) {
		UserRightsDemand tmp = new UserRightsDemand();
		tmp.getPk().setUserId(userId);
		tmp.getPk().setAppId(appId);
		tmp.getPk().setEnvId(envId);
		tmp.getPk().setLevel(level);
		keycloakAdminService.addRight(tmp);
	}

	@Override
	public void removeRight(String userId, String appId, String envId, String level) throws WebApplicationException {
		UserRightsDemand tmp = new UserRightsDemand();
		tmp.getPk().setUserId(userId);
		tmp.getPk().setAppId(appId);
		tmp.getPk().setEnvId(envId);
		tmp.getPk().setLevel(level);
		keycloakAdminService.removeRight(tmp);
	}
	


}
