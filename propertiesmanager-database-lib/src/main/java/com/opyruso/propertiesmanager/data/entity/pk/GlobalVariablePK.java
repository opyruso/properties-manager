package com.opyruso.propertiesmanager.data.entity.pk;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GlobalVariablePK implements Serializable {

	private static final long serialVersionUID = -6607564208223563517L;

	@Column(name = "globalvariable_key", nullable = false)
	private String globalVariableKey;

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

}
