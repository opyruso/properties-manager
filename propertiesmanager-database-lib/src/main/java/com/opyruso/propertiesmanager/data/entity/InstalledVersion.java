package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.data.entity.pk.InstalledVersionPK;

@Entity
@Table(
		name = "installed_version"
	)
public class InstalledVersion implements Serializable {

	private static final long serialVersionUID = -2726800399125422849L;
	@Id
	@Embedded
	private InstalledVersionPK pk = new InstalledVersionPK();

	@Column(name = "creation_date", nullable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", nullable = false)
	@CreationTimestamp
	private Timestamp updateDate;

	public InstalledVersionPK getPk() {
		return pk;
	}

	public void setPk(InstalledVersionPK pk) {
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
