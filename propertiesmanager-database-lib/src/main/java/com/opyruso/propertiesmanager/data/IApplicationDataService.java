package com.opyruso.propertiesmanager.data;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.StatusEnum;
import com.opyruso.propertiesmanager.data.entity.Application;
import com.opyruso.propertiesmanager.data.entity.GlobalVariable;
import com.opyruso.propertiesmanager.data.entity.GlobalVariableValue;
import com.opyruso.propertiesmanager.data.entity.InstalledVersion;
import com.opyruso.propertiesmanager.data.entity.Property;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.data.entity.Version;
import com.opyruso.propertiesmanager.data.entity.pk.PropertyPK;
import com.opyruso.propertiesmanager.data.entity.pk.PropertyValuePK;

public interface IApplicationDataService {

        List<Application> selectApplications(boolean includeArchived) throws SQLException;

        List<String> selectVersions(String appId, boolean includeArchived) throws SQLException;

	List<String> selectInstalledVersions(String appId, String envId) throws SQLException;

	Map<String, InstalledVersion> selectLastInstalledVersion(String appId) throws SQLException;

	Map<String, Long> selectLastReleaseDate(String appId) throws SQLException;

	Application selectApplication(String appId) throws SQLException;

	void updateAppLabel(String appId, String appLabel) throws SQLException;

        void updateProductOwner(String appId, String productOwner) throws SQLException;

        void updateAppStatus(String appId, StatusEnum status) throws SQLException;

	void addPropertyValue(PropertyValue np) throws SQLException;

	void updatePropertyValue(PropertyValue np) throws SQLException;

	Map<String, Property> selectProperties(String appId, String numVersion) throws SQLException;

	List<String> selectFilename(String appId, String numVersion) throws SQLException;

	Map<String, String> selectFilenameAndContent(String appId, String numVersion) throws SQLException;

	void addNewPropertiesFile(String appId, String numVersion, String filename, String content) throws SQLException;

	void updatePropertiesFile(String appId, String numVersion, String filename, String content) throws SQLException;

	Map<String, Map<String, Map<String, PropertyValue>>> selectPropertiesValue(String appId, String numVersion) throws SQLException;

	Map<String, PropertyValue> selectPropertiesValue(String appId, String version, String envId) throws SQLException;

	void addProperty(Property np) throws SQLException;

	PropertyValue selectPropertyValue(String appId, String numVersion, String envId, String filename, String propertyKey) throws SQLException;

	void updateAllPropertyValueOperationType(PropertyValuePK pk, OperationTypeEnum del) throws SQLException;

	void deletePermanentProperty(PropertyPK pk) throws SQLException;

	void addNewInstalledVersion(InstalledVersion installedVersion) throws SQLException;

        void addNewVersion(Version version) throws SQLException;

        void updateVersionStatus(String appId, String numVersion, StatusEnum status) throws SQLException;

	void addNewApplication(Application application) throws SQLException;

	void copyAllPropertiesValueFromVersionToVersion(String appId, String fromVersion, String toVersion) throws SQLException;

        InstalledVersion selectLastInstalledVersionGlobal(String appId) throws SQLException;

        Version selectLastVersionGlobal(String appId) throws SQLException;

        /**
         * Select the most recently created version that shares the same prefix as the
         * provided version. The prefix corresponds to all numeric segments before the
         * final dot of the target version.
         * 
         * @param appId      the application identifier
         * @param numVersion the target version
         * @return the most recent version matching the prefix, or {@code null} if none exists
         * @throws SQLException if a database access error occurs
         */
        Version selectLastVersionSameLevel(String appId, String numVersion) throws SQLException;

        Version selectVersion(String appId, String numVersion) throws SQLException;

        void updateInstalledVersionUpdateDate(String appId, String envId, String numVersion) throws SQLException;

	List<GlobalVariable> selectGlobalVariables() throws SQLException;

	Map<String, Map<String, GlobalVariableValue>> selectGlobalVariableValues() throws SQLException;

	GlobalVariable selectGlobalVariable(String key) throws SQLException;

	GlobalVariableValue selectGlobalVariableValue(String key, String env) throws SQLException;

	void addNewGlobalVariableValue(GlobalVariableValue globalVariableValue) throws SQLException;

	void updateGlobalVariableValue(GlobalVariableValue gv) throws SQLException;

	void removeGlobalVariableValues(String key) throws SQLException;

	void removeGlobalVariable(String key) throws SQLException;

        void addGlobalVariable(GlobalVariable globalVariable) throws SQLException;

        void deleteAllPropertyValues(String appId, String version) throws SQLException;

        void cleanPropertiesByVersion(String appId, String version) throws SQLException;

}
