package com.opyruso.propertiesmanager.api.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.opyruso.propertiesmanager.constants.propertyTypeEnum;
import com.opyruso.propertiesmanager.data.entity.Property;

public class ApiProperty {

	public String appId;
	public String numVersion;
	public String filename;
	public String propertyKey;
	
	public propertyTypeEnum propertyType;

	public static ApiProperty mapEntityToApi(Property propertyValue) {
		ApiProperty result = null;
		if (propertyValue != null) {
			ApiProperty n = new ApiProperty();
			n.appId = propertyValue.getPk().getAppId();
			n.numVersion = propertyValue.getPk().getNumVersion();
			n.filename = propertyValue.getPk().getFilename();
			n.propertyKey = propertyValue.getPk().getPropertyKey();
			n.propertyType = propertyValue.getPropertyType();
			result = n;
		}
		return result;
	}

	public static Property mapApiToEntity(ApiProperty propertyValue) {
		Property result = null;
		if (propertyValue != null) {
			Property n = new Property();
			n.getPk().setAppId(propertyValue.appId);
			n.getPk().setNumVersion(propertyValue.numVersion);
			n.getPk().setFilename(propertyValue.filename);
			n.getPk().setPropertyKey(propertyValue.propertyKey);
			n.setPropertyType(propertyValue.propertyType);
			result = n;
		}
		return result;
	}

	public static List<ApiProperty> mapEntityToApi(List<Property> values) {
		List<ApiProperty> result = new ArrayList<ApiProperty>();
		if (values != null) for (Property property : values) {
			result.add(mapEntityToApi(property));
		}
		return result;
	}

	public static Collection<? extends ApiProperty> mapEntityToApi(Collection<Property> values) {
		List<ApiProperty> result = new ArrayList<ApiProperty>();
		if (values != null) for (Property property : values) {
			result.add(mapEntityToApi(property));
		}
		return result;
	}

}
