package com.mobileuni.model;

import java.util.Calendar;

public class Assignment {
	
	private String name;
	private String description;
	private String url;
	private Calendar startDate;
	private Calendar deadline;
	
	public Assignment() {
		
	}
	
	public Assignment(String name, String description, String url, Calendar startDate, Calendar deadline) {
		this.name = name;
		this.description = description;
		this.url = url;
		this.startDate = startDate;
		this.deadline = deadline;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getDeadline() {
		return deadline;
	}

	public void setDeadline(Calendar deadline) {
		this.deadline = deadline;
	}
}
