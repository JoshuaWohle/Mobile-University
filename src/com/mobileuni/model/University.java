package com.mobileuni.model;

import java.util.ArrayList;
import java.util.List;

import com.mobileuni.other.Constants;

import android.util.Log;

public class University {
	
	private String name;
	private String courseManagerURL;
	private String courseScheduleURL;
	
	private static ArrayList<University> universities = new ArrayList<University>();

	public University(String name, String courseManagerURL, String courseScheduleURL) {
		this.name = name;
		this.courseManagerURL = courseManagerURL;
		this.courseScheduleURL = courseScheduleURL;
	}
	
	public static void init() {
		universities.add(new University("Joshua's Test University", "http://moodle.joshuawohle.com", ""));
		universities.add(new University("King's College University", "", ""));
	}
	
	public static String getCourseManagerURL(String name) {
		for(University university : universities) {
			if(university.name.equals(name))
				return university.courseManagerURL;
		}
		return null;
	}
	
	public static List<String> getUniversityNameList() {
		Log.d(Constants.LOG_UNIVERSITY, "University list size: " + universities.size());
		List<String> temp = new ArrayList<String>();
		for(University university : universities) {
			temp.add(university.name);
			Log.d(Constants.LOG_UNIVERSITY, "Added a university to the list: " + university.name);
		}
		return temp;
	}
	
}
