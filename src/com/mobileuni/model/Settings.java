package com.mobileuni.model;

import com.mobileuni.other.Constants;
import com.mobileuni.other.Session;

import android.content.SharedPreferences;

public class Settings {
	private static boolean isInitiated = false;
	private static boolean autoDownload = false;
	private static boolean syncDeadlines = false;

	private static void loadSettings() {
		SharedPreferences settings = Session.getContext().getSharedPreferences(
				Constants.PREFERENCE_NAME, 0);
		autoDownload = settings.getBoolean("auto_download", false);
		syncDeadlines = settings.getBoolean("sync_deadlines", false);
		isInitiated = true;
	}

	private static void save() {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = Session.getContext().getSharedPreferences(
				Constants.PREFERENCE_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("auto_download", isAutoDownload());
		editor.putBoolean("sync_deadlines", isSyncDeadlines());
		// Commit the edits!
		editor.commit();
	}

	public static boolean isAutoDownload() {
		if (!isInitiated)
			loadSettings();
		return autoDownload;
	}

	public static void setAutoDownload(boolean autoDownload) {
		Settings.autoDownload = autoDownload;
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
}
