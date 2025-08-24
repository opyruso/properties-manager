package com.opyruso.propertiesmanager.api.entity;

public class ApiEnvironment {

	public String id;

	public String name;

	public boolean hasAutoRegisterRights;

	public ApiEnvironment(String id, String name, boolean hasAutoRegisterRights) {
		super();
		this.id = id;
		this.name = name;
		this.hasAutoRegisterRights = hasAutoRegisterRights;
	}
}