package com.mobileuni.controller;

import com.mobileuni.R;
import com.mobileuni.listeners.UserChangeListener;
import com.mobileuni.model.Course;
import com.mobileuni.other.Session;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CourseSelectController extends Activity implements UserChangeListener {

	ProgressDialog dialog;
	String[] courses = new String[] {"No courses"};
	ArrayAdapter<String> adapter;

	public void onCreate(Bundle savedInstanceState) {
		
		if(Session.getUser() == null) {
			Log.d("Session", "No user is set, cannot use the application");
			return;
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_select);
		dialog = ProgressDialog.show(this,
				getResources().getString(R.string.loading), getResources()
						.getString(R.string.wait_while_get_courses));
		
		Session.getUser().addListener(this);
		Session.getCourseManager().setCourses(null);

	}

	public void courseChange(boolean gotCourses) {
		Log.d("Courses", "Setting courses on list");
		int i = 0;
		for(Course course : Session.getUser().getCourses()) {
			courses[i] = course.getFullname();
			i++;
		}
		ListView listView = (ListView) findViewById(R.id.course_list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1, courses);
		adapter.setNotifyOnChange(true);
		listView.setAdapter(adapter);
		dialog.dismiss();
	}
}
