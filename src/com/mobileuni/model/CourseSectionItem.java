package com.mobileuni.model;

/**
 * A class representing a course section item to be used with the menu helper
 * @author Joshua Wöhle
 */
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
