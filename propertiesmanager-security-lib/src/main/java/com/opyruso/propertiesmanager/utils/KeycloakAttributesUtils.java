package com.opyruso.propertiesmanager.utils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opyruso.propertiesmanager.api.entity.ApiUserAttributeRights;

import io.quarkus.logging.Log;

public class KeycloakAttributesUtils {

	private static String getUserIdFromJWT(JsonWebToken jwt) throws Exception {
		return jwt.getClaim("sub");
	}

	private static List<ApiUserAttributeRights> getApplicationClaimSFromJWT(JsonWebToken jwt) throws Exception {
		List<ApiUserAttributeRights> result = new ArrayList<ApiUserAttributeRights>();
		ObjectMapper mapper = new ObjectMapper();
		String claims = null;
		if (jwt.containsClaim("propertiesmanager_rights")) claims = jwt.getClaim("propertiesmanager_rights").toString();
		if (claims == null) claims = "[]";
//		Log.debug("user propertiesmanager_rights claims : " + claims);
		List<ApiUserAttributeRights> rights = mapper.readValue(claims, new TypeReference<List<ApiUserAttributeRights>>(){});
		for (ApiUserAttributeRights r : rights) {
//			Log.debug(r);
//			ApiUserAttributeRights r = mapper.readValue(rightAsString, ApiUserAttributeRights.class);
			result.add(r);
		}
		return result;
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
			boolean result = false;
			List<ApiUserAttributeRights> rights = getApplicationClaimSFromJWT(jwt);
			for (ApiUserAttributeRights r : rights) {
				if (r.admin) {
					result = true;
					break;
				}
			}
			return result;
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
			boolean result = false;
			if (securityCheckIsConnectorAsBoolean(jwt)) {
				result = true;
			} else {
				List<ApiUserAttributeRights> rights = getApplicationClaimSFromJWT(jwt);
				for (ApiUserAttributeRights r : rights) {
					if (r.admin
							|| (r.all_app != null && r.all_app.size() > 0)
							|| (r.app != null && r.app.get(appId) != null && r.app.get(appId).size() > 0)
						) {
						result = true;
						break;
					}
				}
			}
			return result;
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
			boolean result = false;
			if (securityCheckIsConnectorAsBoolean(jwt)) {
				result = true;
			} else {
				List<ApiUserAttributeRights> rights = getApplicationClaimSFromJWT(jwt);
				for (ApiUserAttributeRights r : rights) {
					if (r.admin
							|| (r.all_app != null && r.all_app.get(env) != null && r.all_app.get(env).contains(right))
							|| ((r.app != null && r.app.get(appId) != null && r.app.get(appId).get("all_env") != null && r.app.get(appId).get("all_env").contains(right)))
							|| ((r.app != null && r.app.get(appId) != null && r.app.get(appId).get(env) != null && r.app.get(appId).get(env).contains(right)))
						) {
						result = true;
						break;
					}
				}
			}
			return result;
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
