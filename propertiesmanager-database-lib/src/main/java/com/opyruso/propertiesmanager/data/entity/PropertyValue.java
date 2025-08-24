package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.propertyStatusEnum;
import com.opyruso.propertiesmanager.data.entity.pk.PropertyValuePK;

@Entity
@Table(
		name = "property_value"
	)
public class PropertyValue implements Serializable {

	private static final long serialVersionUID = -2724813810676441151L;

	@Id
	@Embedded
	private PropertyValuePK pk = new PropertyValuePK();

	@Enumerated(EnumType.STRING)
	@Column(name = "operation_type", nullable = false)
	private OperationTypeEnum operationType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private propertyStatusEnum status;

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

	public PropertyValuePK getPk() {
		return pk;
	}

	public void setPk(PropertyValuePK pk) {
		this.pk = pk;
	}

	public OperationTypeEnum getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationTypeEnum operationType) {
		this.operationType = operationType;
	}

	public propertyStatusEnum getStatus() {
		return status;
	}

	public void setStatus(propertyStatusEnum status) {
		this.status = status;
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
