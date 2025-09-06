package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.data.entity.pk.VersionPK;
import com.opyruso.propertiesmanager.constants.StatusEnum;

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

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        public StatusEnum status = StatusEnum.ACTIVE;

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

        public StatusEnum getStatus() {
                return status;
        }

        public void setStatus(StatusEnum status) {
                this.status = status;
        }

}
