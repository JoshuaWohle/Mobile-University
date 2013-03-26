package com.mobileuni.other;

import com.mobileuni.model.Course;
import com.mobileuni.model.User;
import com.mobileuni.model.iCourseManager;

public class Session {
	private static User user = null;
	private static iCourseManager courseManager = null;
	private static Course currentSelectedCourse = null;
	
	public static User getUser() {
		if(user == null) {/*TODO handle case when user is empty and called for*/ return null;}
		return user;
	}

	public static void setUser(User user) {
		if(Session.user == null)
			Session.user = user;
	}

	public static iCourseManager getCourseManager() {
		if(courseManager == null) {/*TODO handle case when course manager is empty and called for*/ return null;}
		return courseManager;
	}

	public static void setCourseManager(iCourseManager courseManager) {
		if(Session.courseManager == null)
			Session.courseManager = courseManager;
	}

	public static Course getCurrentSelectedCourse() {
		return currentSelectedCourse;
	}

	public static void setCurrentSelectedCourse(Course currentSelectedCourse) {
		Session.currentSelectedCourse = currentSelectedCourse;
	}
}
