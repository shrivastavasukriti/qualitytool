package com.sopra.backend.qualitytool.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity object for storing FileDetails
 * 
 * @author Sshrivastava
 *
 */
@Entity
public class FileDetail implements Serializable {

	private static final long serialVersionUID = -8303022254622892653L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String fileName;

	public FileDetail(Long id, String fileName) {
		super();
		this.id = id;
		this.fileName = fileName;
	}

	public Long getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public String toString() {
		return "FileDetails [id=" + id + ", fileName=" + fileName + "]";
	}

}
