package com.opyruso.propertiesmanager.data.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.opyruso.propertiesmanager.data.entity.pk.PropertiesFilePK;

@Entity
@Table(name = "properties_file")
public class PropertiesFile implements Serializable {

	private static final long serialVersionUID = 3289408806382667213L;

	@Id
	@Embedded
	private PropertiesFilePK pk = new PropertiesFilePK();

	@Lob
	@Column(name = "content", nullable = false, columnDefinition = "longtext")
	private String content;

	@Column(name = "creation_date", nullable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", nullable = false)
	@CreationTimestamp
	private Timestamp updateDate;

	public PropertiesFilePK getPk() {
		return pk;
	}

	public void setPk(PropertiesFilePK pk) {
		this.pk = pk;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
