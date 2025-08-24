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

import com.opyruso.propertiesmanager.constants.propertyTypeEnum;
import com.opyruso.propertiesmanager.data.entity.pk.PropertyPK;

@Entity
@Table(
		name = "property"
	)
public class Property implements Serializable {

	private static final long serialVersionUID = 4737144526404709154L;

	@Id
	@Embedded
	private PropertyPK pk = new PropertyPK();

	@Enumerated(EnumType.STRING)
	@Column(name = "property_type", nullable = false)
	private propertyTypeEnum propertyType;

	@Column(name = "creation_date", nullable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", nullable = false)
	@CreationTimestamp
	private Timestamp updateDate;

	public PropertyPK getPk() {
		return pk;
	}

	public void setPk(PropertyPK pk) {
		this.pk = pk;
	}

	public propertyTypeEnum getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(propertyTypeEnum propertyType) {
		this.propertyType = propertyType;
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
