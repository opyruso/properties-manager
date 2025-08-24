package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.data.entity.pk.UserRightsDemandPK;

@Entity
@Table(
		name = "user_right_demand"
	)
public class UserRightsDemand implements Serializable {

	private static final long serialVersionUID = 286613794931413576L;
	@Id
	@Embedded
	private UserRightsDemandPK pk = new UserRightsDemandPK();

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "creation_date", nullable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", nullable = false)
	@CreationTimestamp
	private Timestamp updateDate;

	public UserRightsDemandPK getPk() {
		return pk;
	}

	public void setPk(UserRightsDemandPK pk) {
		this.pk = pk;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

}
