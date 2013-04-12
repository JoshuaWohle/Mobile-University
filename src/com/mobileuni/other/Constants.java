package com.mobileuni.other;

import java.io.File;

import android.os.Environment;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteSession.EvernoteService;

public class Constants {
	public static final String testURL = "http://moodle.joshuawohle.com";
	public static final String testUser = "kcl_test_account";
	public static final String testPassword = "90bB1960a3/";
	
	public static final String FILE_STORAGE_FOLDER_NAME = Environment.getExternalStorageDirectory().getPath() 
			+ "/" + "Mobile University" + "/";
	
	public static final String PREFERENCE_NAME = "MUSettings";
	
	// Third-party integration
	public static final String EVERNOTE_CONSUMER_KEY = "jwohle-1026";
	public static final String EVERNOTE_CONSUMER_SECRET = "b73d16cb79bb43a9";
	public static final EvernoteService EVERNOTE_HOST = EvernoteSession.EvernoteService.SANDBOX;
	
	// Log tags
	public static final String LOG_NOTES = "Notes";
	public static final String LOG_DOCUMENTS = "Documents";
	public static final String LOG_WSR = "Web Service Request";
	public static final String LOG_USER = "User";
	public static final String LOG_UNIVERSITY = "University";
	public static final String LOG_COURSE = "Course";
	public static final String LOG_AUTHENTICATION = "Authentication";
	public static final String LOG_SESSION = "Session";
	public static final String LOG_CONNECTION_STATUS = "Connection";
	public static final String LOG_CALENDAR = "Calendar";
	public static final String LOG_MENU = "Menu";
	
}
