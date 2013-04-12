package com.mobileuni.model;

import com.mobileuni.other.Constants;
import com.mobileuni.other.Session;

import android.content.SharedPreferences;

public class Settings {
	private static boolean isInitiated = false;
	private static boolean autoDownloadFiles = false;
	private static boolean syncDeadlines = false;
	private static boolean autoDownloadContent = false;

	private static void loadSettings() {
		SharedPreferences settings = Session.getContext().getSharedPreferences(
				Constants.PREFERENCE_NAME, 0);
		autoDownloadFiles = settings.getBoolean("auto_download_files", false);
		autoDownloadContent = settings.getBoolean("auto_download_content", false);
		syncDeadlines = settings.getBoolean("sync_deadlines", false);
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

	public static boolean isAutoDownloadContent() {
		if (!isInitiated)
			loadSettings();
		return autoDownloadContent;
	}

	public static void setAutoDownloadContent(boolean autoDownloadContent) {
		Settings.autoDownloadContent = autoDownloadContent;
		save();
	}
}
