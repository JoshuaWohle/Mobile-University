package com.mobileuni.model;

import java.io.Serializable;
import java.util.Calendar;

public class MetaNote implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String excerpt;
	private Calendar dateCreated;
	private String evernoteId;
	private String shareLink;
	
	public MetaNote() {
		
	}
	
	public MetaNote(String name, String excerpt, Calendar dateCreated, String evernoteId) {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getEvernoteId() {
		return evernoteId;
	}

	public void setEvernoteId(String evernoteId) {
		this.evernoteId = evernoteId;
	}
}
