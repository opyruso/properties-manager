package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.data.entity.pk.ApplicationPK;

@Entity
@Table(
		name = "applications"
		,uniqueConstraints = 
				@UniqueConstraint(columnNames = { "app_label" })
	)
public class Application implements Serializable {

	private static final long serialVersionUID = 477055896768625360L;

	@Id
	@Embedded
	private ApplicationPK pk = new ApplicationPK();

	@Column(name = "app_label", nullable = false)
	private String appLabel;

	@Column(name = "app_product_owner", nullable = false)
	private String productOwner;

	@Column(name = "creation_date", nullable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", nullable = false)
	@CreationTimestamp
	private Timestamp updateDate;

	@Transient
	private List<String> existingVersions = new ArrayList<String>();

	@Transient
	private Map<String, String> versions;

	@Transient
	private Map<String, Long> lastReleaseDates;

	@Transient
	private List<Property> properties = null;

	@Transient
	private Map<String, Map<String, Map<String, PropertyValue>>> propertiesValue = null;

	public ApplicationPK getPk() {
		return pk;
	}

	public void setPk(ApplicationPK pk) {
		this.pk = pk;
	}

	public String getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}

	public String getProductOwner() {
		return productOwner;
	}

	public void setProductOwner(String productOwner) {
		this.productOwner = productOwner;
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

	public List<String> getExistingVersions() {
		return existingVersions;
	}

	public void setExistingVersions(List<String> existingVersions) {
		this.existingVersions = existingVersions;
	}

	public Map<String, String> getVersions() {
		return versions;
	}

	public void setVersions(Map<String, String> versions) {
		this.versions = versions;
	}

	public Map<String, Long> getLastReleaseDates() {
		return lastReleaseDates;
	}

	public void setLastReleaseDates(Map<String, Long> lastReleaseDates) {
		this.lastReleaseDates = lastReleaseDates;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Map<String, Map<String, Map<String, PropertyValue>>> getPropertiesValue() {
		return propertiesValue;
	}

	public void setPropertiesValue(Map<String, Map<String, Map<String, PropertyValue>>> propertiesValue) {
		this.propertiesValue = propertiesValue;
	}

}
