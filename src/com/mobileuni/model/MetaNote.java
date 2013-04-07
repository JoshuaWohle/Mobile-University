package com.mobileuni.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MetaNote implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String content;
	private Calendar dateCreated;
	private String evernoteId;
	
	public MetaNote() {
		
	}
	
	public MetaNote(String name, String content, Calendar dateCreated, String evernoteId) {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String excerpt) {
		this.content = excerpt;
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
