package com.opyruso.propertiesmanager.constants;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Singleton;

import org.eclipse.microprofile.config.ConfigProvider;

import com.opyruso.propertiesmanager.api.entity.ApiEnvironment;

import io.quarkus.logging.Log;

@Singleton
public class EnvironmentConfig {

	String ids = ConfigProvider.getConfig().getValue("propertiesmanager.configuration.ids", String.class);

	String names = ConfigProvider.getConfig().getValue("propertiesmanager.configuration.names", String.class);

	String hasAutoRegisterRights = ConfigProvider.getConfig().getValue("propertiesmanager.configuration.hasAutoRegisterRights", String.class);

	private Map<String, ApiEnvironment> environments;

	public Map<String, ApiEnvironment> environments() {
		if (environments == null) {
			Log.info("Initializing EnvironmentConfig  " + ids);
			environments = new HashMap<String, ApiEnvironment>();
			String[] ids = this.ids.split(",");
			String[] names = this.names.split(",");
			String[] hasAutoRegisterRights = this.hasAutoRegisterRights.split(",");
			for (int i = 0; i < ids.length; i++) {
				environments.put(ids[i], new ApiEnvironment(ids[i], names[i], Boolean.valueOf(hasAutoRegisterRights[i])));
			}
		}
		return environments;
	}

}
