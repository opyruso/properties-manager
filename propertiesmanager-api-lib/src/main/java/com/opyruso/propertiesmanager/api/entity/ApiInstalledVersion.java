package com.opyruso.propertiesmanager.api.entity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.opyruso.propertiesmanager.data.entity.InstalledVersion;

public class ApiInstalledVersion {

	public String appId;

	public String envId;

	public String numVersion;

	public Timestamp updateDate;

	public static ApiInstalledVersion mapEntityToApi(InstalledVersion lastInstalledVersion) {
		ApiInstalledVersion result = new ApiInstalledVersion();
		result.appId = lastInstalledVersion.getPk().getAppId();
		result.envId = lastInstalledVersion.getPk().getEnvId();
		result.numVersion = lastInstalledVersion.getPk().getNumVersion();
		result.updateDate = lastInstalledVersion.getUpdateDate();
		return result;
	};

	public static Map<String, ApiInstalledVersion> mapEntityToApi(Map<String, InstalledVersion> lastInstalledVersions) {
		Map<String, ApiInstalledVersion> result = new HashMap<String, ApiInstalledVersion>();
		if (lastInstalledVersions != null) {
			for (String e : lastInstalledVersions.keySet()) {
					result.put(e, mapEntityToApi(lastInstalledVersions.get(e)));
			}
		}
		return result;
	};

}