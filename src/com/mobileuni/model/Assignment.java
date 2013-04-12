package com.mobileuni.model;

import java.util.Calendar;

public class Assignment {
	
	private String name;
	private Calendar startDate;
	private Calendar deadline;
	
	public Assignment() {
		
	}
	
	public Assignment(String name, Calendar startDate, Calendar deadline) {
		this.name = name;
		this.startDate = startDate;
		this.deadline = deadline;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
