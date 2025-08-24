package com.opyruso.propertiesmanager.data.entity.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PropertyPK implements Serializable {

	private static final long serialVersionUID = 3884974871349693735L;

	@Column(name = "app_id", nullable = false)
	private String appId;

	@Column(name = "num_version", nullable = false)
	private String numVersion;

	@Column(name = "filename", nullable = false)
	private String filename;

	@Column(name = "property_key", nullable = false)
	private String propertyKey;

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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

}
