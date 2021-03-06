package com.mobileuni.model;

import android.content.Context;

import com.evernote.client.android.EvernoteSession;
import com.mobileuni.other.Constants;

/**
 * Represents the session of the application and handles objects that we want to avoid to have to throw around.
 * @author Joshua W�hle
 */
public class Session {
	
	private static User user = null;
	private static iCourseManager courseManager = null;
	private static Course currentSelectedCourse = null;
	private static Context context;
	private static EvernoteSession es = null;
	
	public static User getUser() {
		if(user == null) 
			return null;
		
		return user;
	}

	public static void setUser(User user) {
		Session.user = user;
	}

	public static iCourseManager getCourseManager() {
		if(courseManager == null)
			return null;
		
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

	public static EvernoteSession getEs() {
		if(es == null)
			es = EvernoteSession.getInstance(Session.getContext(), Constants.EVERNOTE_CONSUMER_KEY, Constants.EVERNOTE_CONSUMER_SECRET, Constants.EVERNOTE_HOST);
		return es;
	}
}
