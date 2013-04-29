package com.mobileuni.listeners;

public interface iCourseManagerListener {

	/**
	 * Notifies the listening parties that the system now has a user logged-in (and can thus proceed)
	 * @param loggedIn
	 */
	public void loginChange(boolean loggedIn);
	
}
