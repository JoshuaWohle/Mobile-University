package com.mobileuni.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.CalendarContract.Calendars;
import android.widget.ListView;

import com.mobileuni.R;
import com.mobileuni.model.Course;
import com.mobileuni.model.CourseSchedule;
import com.mobileuni.model.Module;
import com.mobileuni.model.Session;
import com.mobileuni.model.Settings;
import com.mobileuni.other.ModuleType;
import com.mobileuni.other.SyncType;

/**
 * Handles the synchronization of course deadlines & assignments with the user calendar depending on the settings of the user
 * @author Joshua Wöhle
 */
public class CalendarSyncHelper {

	/**
	 * Synchronizes the course schedules if possible
	 * @param a - the activity responsible for user-interaction
	 * @param showCalendarSelect - should we show the calendars or has the user already chosen a calendar to sync with ?
	 */
	public static void synchronizeCourseSchedules(Activity a, Boolean showCalendarSelect) {
		if (VERSION.SDK_INT < 14) {
			DialogHelper.androidSDKTooLow(a).show();
			Settings.setSyncCourseSchedules(false);
			return;
		}

		if (showCalendarSelect)
			showCalendarSelect(a, SyncType.COURSE_SCHEDULES);
		else
			synchronize(SyncType.COURSE_SCHEDULES);
	}

	/**
	 * Synchronizes the course deadlines if possible
	 * @param a - the activity responsible for user-interaction
	 * @param showCalendarSelect - - should we show the calendars or has the user already chosen a calendar to sync with ?
	 */
	public static void synchronizeDeadlines(Activity a, Boolean showCalendarSelect) {
		if (VERSION.SDK_INT < 14) {
			DialogHelper.androidSDKTooLow(a).show();
			Settings.setSyncDeadlines(false);
			return;
		}
		
		if (showCalendarSelect)
			showCalendarSelect(a, SyncType.COURSE_DEADLINES);
		else
			synchronize(SyncType.COURSE_DEADLINES);
	}
	
	private static void synchronize(SyncType type) {
		switch(type) {
		case COURSE_SCHEDULES:
			for (Course course : Session.getUser().getCourses()) {
				for (@SuppressWarnings("unused") CourseSchedule schedule : course.getDates()) {
					// TODO sync courses
					// addEvent with appropriate information
				}
			}
			break;
		case COURSE_DEADLINES:
			for (@SuppressWarnings("unused") Module upcoming : Session.getCourseManager().getModules(null,
					ModuleType.ASSIGNMENT)) {
					// TODO implement deadline synchronization
					// addEvent with appropriate information
			}
			break;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void addEvent(Activity a, long calendarId, ContentValues cv) {
		ContentResolver cr = a.getContentResolver();
		Uri calUri = ContentUris.withAppendedId(Calendars.CONTENT_URI, calendarId);
		cr.update(calUri, cv, null, null);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void showCalendarSelect(Activity a, final SyncType type) {
		ArrayList<String> calendars = new ArrayList<String>();

		String[] projection = new String[] { Calendars._ID, Calendars.NAME,
				Calendars.ACCOUNT_NAME, Calendars.ACCOUNT_TYPE };
		Cursor calCursor = a.getContentResolver().query(Calendars.CONTENT_URI,
				projection, Calendars.VISIBLE + " = 1", null,
				Calendars._ID + " ASC");
		
		// A hashmap of calendar names linked to IDs
		final HashMap<String, Long> calendarNameID = new HashMap<String, Long>();
		
		if (calCursor.moveToFirst()) {
			do {
				long id = calCursor.getLong(0);
				String displayName = calCursor.getString(1);
				if (displayName != null) {
					calendars.add(displayName);
					calendarNameID.put(displayName, id);
				}
			} while (calCursor.moveToNext());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setTitle(R.string.pick_calendar).setItems(
				(CharSequence[]) calendars.toArray(new CharSequence[calendars.size()]),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ListView lv = ((AlertDialog) dialog).getListView();
						Settings.setSyncCalendarId((Long)calendarNameID.get(lv.getItemAtPosition(which)));
						synchronize(type);
					}
				});
		builder.create().show();
	}
}
