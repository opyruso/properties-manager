package com.opyruso.propertiesmanager.data.entity.pk;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ApplicationPK implements Serializable {

	private static final long serialVersionUID = -6857143440338021902L;

       @Column(name = "app_id", nullable = false, length = 100)
       private String appId;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
