package com.mobileuni.other;

import android.content.Context;

import com.mobileuni.model.Course;
import com.mobileuni.model.User;
import com.mobileuni.model.iCourseManager;

public class Session {
	private static User user = null;
	private static iCourseManager courseManager = null;
	private static Course currentSelectedCourse = null;
	private static Context context;
	
	public static User getUser() {
		if(user == null) {/*TODO handle case when user is empty and called for*/ return null;}
		return user;
	}

	public static void setUser(User user) {
		Session.user = user;
	}

	public static iCourseManager getCourseManager() {
		if(courseManager == null) {/*TODO handle case when course manager is empty and called for*/ return null;}
		return courseManager;
	}

	public static void setCourseManager(iCourseManager courseManager) {
		Session.courseManager = courseManager;
	}

	public static Course getCurrentSelectedCourse() {
		return currentSelectedCourse;
	}

	public static void setCurrentSelectedCourse(Course currentSelectedCourse) {
		Session.currentSelectedCourse = currentSelectedCourse;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		Session.context = context;
	}
}
