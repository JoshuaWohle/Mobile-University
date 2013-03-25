package com.mobileuni.controller;

import com.mobileuni.CourseDetail;
import com.mobileuni.R;
import com.mobileuni.listeners.UserChangeListener;
import com.mobileuni.model.Course;
import com.mobileuni.other.Session;

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

		LinearLayout main = (LinearLayout) findViewById(R.id.main);
		for(Course course : Session.getUser().getCourses()){
			LinearLayout child = (LinearLayout) getLayoutInflater().inflate(R.layout.course_select_course_item, main);
			child.setTag(course.getId());
			child.setOnClickListener(this);
			((TextView) child.findViewById(R.id.course_item_title)).setText(course.getShortName());
			((TextView) child.findViewById(R.id.course_item_content)).setText(course.getFullname());
		}
		dialog.dismiss();
	}

	public void onClick(View v) {
		int courseId = (Integer) v.getTag();
		if(courseId != 0) {
			Intent intent = new Intent(CourseSelectController.this, CourseDetail.class);
			intent.putExtra("course_id", courseId);
			startActivity(intent);
		}
		
	}
}
