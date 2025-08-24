package com.opyruso.propertiesmanager.data.entity.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InstalledVersionPK implements Serializable {

	private static final long serialVersionUID = 7389489035777551569L;

	@Column(name = "app_id", nullable = false)
	private String appId;

	@Column(name = "env_id", nullable = false)
	private String envId;

	@Column(name = "num_version", nullable = false)
	private String numVersion;

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

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	public String getNumVersion() {
		return numVersion;
	}

	public void setNumVersion(String numVersion) {
		this.numVersion = numVersion;
	}

}
