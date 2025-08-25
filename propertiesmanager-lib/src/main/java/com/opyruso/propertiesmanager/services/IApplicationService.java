package com.opyruso.propertiesmanager.services;

import java.util.List;
import java.util.Map;

import jakarta.ws.rs.WebApplicationException;

import com.opyruso.propertiesmanager.api.entity.ApiApplicationFull;
import com.opyruso.propertiesmanager.api.entity.ApiApplicationShort;
import com.opyruso.propertiesmanager.api.entity.ApiGlobalVariable;
import com.opyruso.propertiesmanager.api.entity.ApiInstalledVersion;
import com.opyruso.propertiesmanager.api.entity.ApiProperty;
import com.opyruso.propertiesmanager.api.entity.request.ApiApplicationUpdateRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiNewApplicationRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiPropertyUpdateRequest;
import com.opyruso.propertiesmanager.api.entity.ApiGlobalVariableValue;

public interface IApplicationService {
	
	public List<ApiApplicationShort> getApplications() throws WebApplicationException;

	public List<String> getApplicationVersions(String appId) throws WebApplicationException;

	public List<String> getApplicationFilenames(String appId, String numVersion) throws WebApplicationException;

	public Map<String, String> getApplicationFiles(String appId, String numVersion) throws WebApplicationException;

	public Map<String, ApiInstalledVersion> getApplicationInstalledVersions(String appId) throws WebApplicationException;

	public Map<String, Long> getApplicationLastReleaseDate(String appId) throws WebApplicationException;

	public ApiApplicationFull getApplicationDetails(String appId, String numVersion) throws WebApplicationException;

	public void appUpdate(String appId, ApiApplicationUpdateRequest request) throws WebApplicationException;

	public void propertyAdd(ApiProperty request) throws WebApplicationException;

	public void propertyUpdate(ApiPropertyUpdateRequest request) throws WebApplicationException;

	public void propertyAllEnvAdd(String appId, String numVersion, String filename, String propertyKey) throws WebApplicationException;

	public void propertyAllEnvDelete(String appId, String numVersion, String filename, String propertyKey) throws WebApplicationException;

	public void propertyPermanentDelete(String appId, String numVersion, String filename, String propertyKey) throws WebApplicationException;

	public void addOrUpdateFile(String appId, String numVersion, String filename, String content) throws WebApplicationException;

	public void addInstalledApplicationVersion(String appId, String numVersion, String envId) throws WebApplicationException;

        public void replaceAllPropertiesFromVersionToVersion(String appId, String fromVersion, String toVersion) throws WebApplicationException;

        public void createNewApplicationVersion(String appId, String numVersion, String filename, String content) throws WebApplicationException;

        public void createSnapshotVersion(String appId) throws WebApplicationException;

	public void createNewApplication(ApiNewApplicationRequest request) throws WebApplicationException;

	public List<ApiGlobalVariable> getGlobalVariables() throws WebApplicationException;

	public Map<String, Map<String, ApiGlobalVariableValue>> getGlobalVariableValues() throws WebApplicationException;

	public void addGlobalVariable(String key) throws WebApplicationException;

	public void updateGlobalVariable(String key, String env, String value) throws WebApplicationException;

	public void removeGlobalVariable(String key) throws WebApplicationException;

	void updateGlobalVariableProtection(String key, String env, boolean isProtected) throws WebApplicationException;
}

































