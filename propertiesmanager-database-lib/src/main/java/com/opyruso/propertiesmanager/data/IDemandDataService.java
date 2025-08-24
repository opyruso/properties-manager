package com.opyruso.propertiesmanager.data;

import java.sql.SQLException;
import java.util.List;

import jakarta.transaction.Transactional;

import com.opyruso.propertiesmanager.data.entity.UserRightsDemand;

public interface IDemandDataService {
	
	List<UserRightsDemand> selectDemands() throws SQLException;

	List<UserRightsDemand> selectDemands(String userId) throws SQLException;

	@Transactional
	void insertOrUpdateDemand(UserRightsDemand request) throws SQLException;

	@Transactional
	void deleteDemand(UserRightsDemand request) throws SQLException;

}
