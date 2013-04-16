package com.mobileuni.controller;

import com.mobileuni.R;
import com.mobileuni.helpers.AppStatus;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.listeners.UserChangeListener;
import com.mobileuni.model.Course;
import com.mobileuni.model.Session;
import com.mobileuni.model.Settings;
import com.mobileuni.other.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseSelectController extends Activity implements UserChangeListener, OnClickListener {

	ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.item_list, R.string.menu_courses);
		
		if(Session.getUser() == null) {
			Log.d(Constants.LOG_SESSION, "No user is set, cannot use the application");
			return;
		}
		
		dialog = ProgressDialog.show(this,
				getResources().getString(R.string.loading), getResources()
						.getString(R.string.wait_while_get_courses));
		
		Session.getUser().addListener(this);
		if(AppStatus.isOnline())
			Session.getCourseManager().setCourses(null, Settings.isAutoDownloadContent());
		else
			courseChange(true);

	}

	public void courseChange(boolean gotCourses) {
		Log.d(Constants.LOG_COURSE, "Setting courses on list");

		LinearLayout main = (LinearLayout) findViewById(R.id.item_list);
		main.removeAllViews();
		// Set title of the view
		for(Course course : Session.getUser().getCourses()){
			Log.d(Constants.LOG_COURSE, "Adding course: " + course.getId());
			LinearLayout child = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item, null);
			child.setTag(course.getId());
			child.setOnClickListener(this);
			((TextView) child.findViewById(R.id.item_title)).setText(course.getShortName());
			((TextView) child.findViewById(R.id.item_content)).setText(course.getFullname());
			main.addView(child);
		}
		dialog.dismiss();
	}

	public void onClick(View v) {
		int courseId = (Integer) v.getTag();
		if(courseId != 0) {
			Intent intent = new Intent(CourseSelectController.this, CourseDetailController.class);
			Session.setCurrentSelectedCourse(Session.getUser().getCourse(courseId));
			startActivity(intent);
		}
		
	}
}
