package com.opyruso.propertiesmanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;

import com.opyruso.propertiesmanager.api.entity.ApiUserDetails;
import com.opyruso.propertiesmanager.services.IAdminService;
import com.opyruso.propertiesmanager.utils.KeycloakProvider;

import io.quarkus.logging.Log;

@ApplicationScoped
public class AdminService implements IAdminService {

        protected static String admin_realm = ConfigProvider.getConfig().getValue("propertiesmanager.client.keycloak.realm", String.class);

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
                                result.add(newUser);
                        }
                        return result;
                } catch (Exception e) {
                        Log.error("Error:", e);
                        return null;
                }
        }

}
