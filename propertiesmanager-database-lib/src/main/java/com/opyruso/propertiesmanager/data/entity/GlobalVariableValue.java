package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.data.entity.pk.GlobalVariableValuePK;

@Entity
@Table(
		name = "globalvariable_value"
	)
public class GlobalVariableValue implements Serializable {

	private static final long serialVersionUID = 4534387349853541854L;

	@Id
	@Embedded
	private GlobalVariableValuePK pk = new GlobalVariableValuePK();

	@Column(name = "new_value", nullable = false)
	private String newValue;

	@Column(name = "is_protected", nullable = false)
	private boolean isProtected = false;

	@Column(name = "creation_date", nullable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", nullable = false)
	@CreationTimestamp
	private Timestamp updateDate;

	public GlobalVariableValuePK getPk() {
		return pk;
	}

	public void setPk(GlobalVariableValuePK pk) {
		this.pk = pk;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
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
