package com.opyruso.propertiesmanager.utils;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.opyruso.propertiesmanager.api.entity.ApiEnvironment;
import com.opyruso.propertiesmanager.constants.EnvironmentConfig;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;

@Singleton
public class ApplicationUtils {

	@Inject
	protected EnvironmentConfig environmentConfig;

	public Map<String, Map<String, PropertyValue>> extractAllPropertyKeysFromPropertiesValue(Map<String, Map<String, Map<String, PropertyValue>>> propertiesValue) {
		Map<String, Map<String, PropertyValue>> result = new HashMap<String, Map<String, PropertyValue>>();
		if (propertiesValue != null) {
			for (ApiEnvironment env : environmentConfig.environments().values()) {
				if (propertiesValue.get(env.id) != null) {
					for (String filename : propertiesValue.get(env.id).keySet()) {
						if (propertiesValue.get(env.id).get(filename) != null) {
							for (PropertyValue p : propertiesValue.get(env.id).get(filename).values()) {
								if (!result.containsKey(p.getPk().getFilename())) result.put(p.getPk().getFilename(), new HashMap<String, PropertyValue>());
								if (!result.get(p.getPk().getFilename()).containsKey(p.getPk().getPropertyKey())) result.get(p.getPk().getFilename()).put(p.getPk().getPropertyKey(), p);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
}