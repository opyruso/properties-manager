package com.opyruso.propertiesmanager.data.entity.pk;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GlobalVariableValuePK implements Serializable {

	private static final long serialVersionUID = 7081931518771053844L;

	@Column(name = "globalvariable_key", nullable = false)
	private String globalVariableKey;

	@Column(name = "env_id", nullable = false)
	private String envId;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public String getGlobalVariableKey() {
		return globalVariableKey;
	}

	public void setGlobalVariableKey(String globalVariableKey) {
		this.globalVariableKey = globalVariableKey;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

}
