package com.opyruso.propertiesmanager.data.entity.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserRightsDemandPK implements Serializable {

	private static final long serialVersionUID = -7585064914012477092L;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "app_id", nullable = false)
	private String appId;

	@Column(name = "env_id", nullable = false)
	private String envId;

	@Column(name = "level", nullable = false)
	private String level;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
