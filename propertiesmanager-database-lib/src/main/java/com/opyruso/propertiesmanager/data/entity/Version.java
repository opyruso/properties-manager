package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.data.entity.pk.VersionPK;

@Entity
@Table(
		name = "application_version"
	)
public class Version implements Serializable {

	private static final long serialVersionUID = 8755110934606259356L;

	@Id
	@Embedded
	private VersionPK pk = new VersionPK();

	@Column(name = "creation_date", nullable = false)
	@CreationTimestamp
	public Timestamp creationDate;

	@Column(name = "update_date", nullable = false)
	@CreationTimestamp
	public Timestamp updateDate;

	public VersionPK getPk() {
		return pk;
	}

	public void setPk(VersionPK pk) {
		this.pk = pk;
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
