package com.mobileuni.listeners;

public interface UserChangeListener {

	/**
	 * Notifies the listening partners that the user has changed course. This is generally used to say we have a result from
	 * the server and can thus display it.
	 * @param gotCourses
	 */
	public void courseChange(boolean gotCourses);
	
}
