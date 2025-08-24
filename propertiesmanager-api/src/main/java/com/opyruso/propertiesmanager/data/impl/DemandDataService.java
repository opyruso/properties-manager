package com.opyruso.propertiesmanager.data.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.data.IDemandDataService;
import com.opyruso.propertiesmanager.data.entity.UserRightsDemand;
import com.opyruso.propertiesmanager.data.entity.repository.UserRightsDemandRepository;
import com.opyruso.propertiesmanager.utils.KeycloakAttributesUtils;

import io.quarkus.arc.DefaultBean;
import io.quarkus.logging.Log;

@DefaultBean
@ApplicationScoped
public class DemandDataService implements IDemandDataService {

	@Inject
	protected JsonWebToken jwt;
	
	@Inject
	protected UserRightsDemandRepository userRightsDemandRepository;

	@Override
	public List<UserRightsDemand> selectDemands() throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		return userRightsDemandRepository.listAll();
	}

	@Override
	public List<UserRightsDemand> selectDemands(String userId) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckCurrentUser(jwt, userId);
		userRightsDemandRepository.count("user_id = ?1", userId);
		return userRightsDemandRepository.list("user_id = ?1", userId);
	}

	@Override
	@Transactional
	public void insertOrUpdateDemand(UserRightsDemand request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckCurrentUser(jwt, request.getPk().getUserId());
		long old = userRightsDemandRepository.find("pk = ?1", request.getPk()).count();
		if (old == 0) {
			request.setCreationDate(Timestamp.from(Calendar.getInstance().toInstant()));
			request.setUpdateDate(Timestamp.from(Calendar.getInstance().toInstant()));
			userRightsDemandRepository.persist(request);
		}
	}

	@Override
	@Transactional
	public void deleteDemand(UserRightsDemand request) throws WebApplicationException {
		KeycloakAttributesUtils.securityCheckIsAdmin(jwt);
		Log.debug("deleteDemand : " + request.getPk().getUserId());
		Log.debug("deleteDemand : " + request.getPk().getAppId());
		Log.debug("deleteDemand : " + request.getPk().getEnvId());
		Log.debug("deleteDemand : " + request.getPk().getLevel());
		Log.debug("deleteDemand : " + request.getCreationDate());
		Log.debug("deleteDemand : " + request.getUpdateDate());
		userRightsDemandRepository.delete("pk = ?1", request.getPk());
	}

}
