package com.mobileuni.model;

public class CourseSectionItem {
	
	public String name;
	public String description;
	public Class targetActivity;
	
	public CourseSectionItem(String name, String description, Class activity) {
		this.name = name; 
		this.description = description;
		this.targetActivity = activity;
	}

}
