package com.mobileuni.model;

import com.mobileuni.other.Constants;

import android.content.SharedPreferences;

/**
 * A simple class to represent the user application settings
 * @author Joshua Wöhle
 */
public class Settings {
	/**
	 * Tells us if the settings have been initiated (loaded) or not 
	 */
	private static boolean isInitiated = false;
	private static boolean autoDownloadFiles = false;
	private static boolean syncDeadlines = false;
	private static boolean syncCourseSchedules = false;
	private static boolean autoDownloadContent = false;
	private static long syncCalendarId = 0;

	private static void loadSettings() {
		SharedPreferences settings = Session.getContext().getSharedPreferences(
				Constants.PREFERENCE_NAME, 0);
		autoDownloadFiles = settings.getBoolean("auto_download_files", false);
		autoDownloadContent = settings.getBoolean("auto_download_content", false);
		syncDeadlines = settings.getBoolean("sync_deadlines", false);
		syncCourseSchedules = settings.getBoolean("sync_course_schedules", false);
		syncCalendarId = settings.getLong("sync_calendarId", 0);
		isInitiated = true;
	}

	private static void save() {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = Session.getContext().getSharedPreferences(
				Constants.PREFERENCE_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("auto_download_files", isAutoDownloadFiles());
		editor.putBoolean("auto_download_content", isAutoDownloadContent());
		editor.putBoolean("sync_deadlines", isSyncDeadlines());
		editor.putBoolean("sync_course_schedules", isSyncCourseSchedules());
		editor.putLong("sync_calendarId", syncCalendarId);
		// Commit the edits!
		editor.commit();
	}

	public static boolean isAutoDownloadFiles() {
		if (!isInitiated)
			loadSettings();
		return autoDownloadFiles;
	}

	public static void setAutoDownloadFiles(boolean autoDownloadFiles) {
		Settings.autoDownloadFiles = autoDownloadFiles;
		save();
	}

	public static boolean isSyncDeadlines() {
		if (!isInitiated)
			loadSettings();
		return syncDeadlines;
	}

	public static void setSyncDeadlines(boolean syncDeadlines) {
		Settings.syncDeadlines = syncDeadlines;
		save();
	}

	public static boolean isSyncCourseSchedules() {
		if (!isInitiated)
			loadSettings();
		return syncCourseSchedules;
	}

	public static void setSyncCourseSchedules(boolean syncCourseSchedules) {
		Settings.syncCourseSchedules = syncCourseSchedules;
		save();
	}

	public static boolean isAutoDownloadContent() {
		if (!isInitiated)
			loadSettings();
		return autoDownloadContent;
	}

	public static void setAutoDownloadContent(boolean autoDownloadContent) {
		Settings.autoDownloadContent = autoDownloadContent;
		save();
	}

	public static long getSyncCalendarId() {
		if (!isInitiated)
			loadSettings();
		return syncCalendarId;
	}

	public static void setSyncCalendarId(long syncCalendarId) {
		Settings.syncCalendarId = syncCalendarId;
		save();
	}
}
