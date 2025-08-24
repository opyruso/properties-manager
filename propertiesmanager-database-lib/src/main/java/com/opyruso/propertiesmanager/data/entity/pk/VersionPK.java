package com.opyruso.propertiesmanager.data.entity.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VersionPK implements Serializable {

	private static final long serialVersionUID = -3761646265074666388L;

	@Column(name = "app_id", nullable = false)
	private String appId;

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

	public String getNumVersion() {
		return numVersion;
	}

	public void setNumVersion(String numVersion) {
		this.numVersion = numVersion;
	}

}
