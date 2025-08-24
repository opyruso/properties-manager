package com.opyruso.propertiesmanager.utils;

import java.util.Set;

import jakarta.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.logging.Log;

public class KeycloakAttributesUtils {

        private static String getUserIdFromJWT(JsonWebToken jwt) throws Exception {
                return jwt.getClaim("sub");
        }

        public static boolean securityCheckCurrentUserAsBoolean(JsonWebToken jwt, String userId) {
                try {
                        boolean result = false;
                        String tokenUserId = getUserIdFromJWT(jwt);
                        if (tokenUserId != null && tokenUserId.equals(userId)) {
                                result = true;
                        }
                        return result;
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static void securityCheckCurrentUser(JsonWebToken jwt, String userId) {
                try {
                        if (!securityCheckCurrentUserAsBoolean(jwt, userId)) {
                                throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                        }
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static boolean securityCheckIsConnectorAsBoolean(JsonWebToken jwt) throws WebApplicationException {
                try {
                        boolean result = false;
                        result = jwt.containsClaim("propertiesmanager_connector");
                        return result;
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static void securityCheckIsConnector(JsonWebToken jwt) throws WebApplicationException {
                try {
                        if (!securityCheckIsConnectorAsBoolean(jwt)) {
                                throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                        }
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static boolean securityCheckIsAdminAsBoolean(JsonWebToken jwt) throws WebApplicationException {
                try {
                        return jwt.getGroups().contains("admin");
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static void securityCheckIsAdmin(JsonWebToken jwt) throws WebApplicationException {
                try {
                        if (!securityCheckIsAdminAsBoolean(jwt)) {
                                throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                        }
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static boolean securityCheckAsBoolean(JsonWebToken jwt, String appId) throws WebApplicationException {
                try {
                        if (securityCheckIsConnectorAsBoolean(jwt)) {
                                return true;
                        }
                        if (securityCheckIsAdminAsBoolean(jwt)) {
                                return true;
                        }
                        Set<String> groups = jwt.getGroups();
                        for (String g : groups) {
                                if (g.startsWith("env_")) {
                                        return true;
                                }
                        }
                        return false;
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static void securityCheck(JsonWebToken jwt, String appId) throws WebApplicationException {
                try {
                        if (!securityCheckAsBoolean(jwt, appId)) {
                                throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                        }
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static boolean securityCheckAsBoolean(JsonWebToken jwt, String appId, String env, String right) throws WebApplicationException {
                try {
                        if (securityCheckIsConnectorAsBoolean(jwt)) {
                                return true;
                        }
                        if (securityCheckIsAdminAsBoolean(jwt)) {
                                return true;
                        }
                        String role = "env_" + env + ("w".equals(right) ? "_write" : "_read");
                        return jwt.getGroups().contains(role);
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }

        public static void securityCheck(JsonWebToken jwt, String appId, String env, String right) throws WebApplicationException {
                try {
                        if (!securityCheckAsBoolean(jwt, appId, env, right)) {
                                throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                        }
                } catch (Exception e) {
                        Log.error("Error:", e);
                        throw new WebApplicationException(HttpStatus.SC_FORBIDDEN);
                }
        }
}

