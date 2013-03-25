/**
 *  an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
 *  Copyright (C) 2012  Justin Stevanz, Andrew Kelson and Matthias Peitsch
 *
 *	Contact the.omega.online@gmail.com for further information.
 *
 *   This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.mobileuni;

import java.util.ArrayList;
import java.util.HashMap;

import com.mobileuni.controller.CourseAssignmentController;
import com.mobileuni.controller.CourseContentController;
import com.mobileuni.controller.CourseForumController;
import com.mobileuni.controller.CourseGradeController;
import com.mobileuni.helpers.CourseDetailsListHelper;
import com.mobileuni.helpers.LazyAdapter;
import com.mobileuni.listeners.UserChangeListener;
import com.mobileuni.model.Course;
import com.mobileuni.model.CourseContent;
import com.mobileuni.other.Session;

import com.mobileuni.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CourseDetail extends Activity implements OnClickListener, UserChangeListener {

	Button home, courseSelect, upload, setting;
	TextView footerCourseHdr, documents, assignments, grades, forum, offline;
	LazyAdapter adapter;
	ListView list;
	Intent nextPage;
	ProgressDialog dialog;
	ArrayList<HashMap<String, String>> courseDetailList = new ArrayList<HashMap<String, String>>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_detail);
		Session.getUser().addListener(this);
		
	}
	
	public void displayCourseChoice() {
		dialog.dismiss();
		try {
			Intent i = getIntent();

			footerCourseHdr = (TextView) findViewById(R.id.course_ftr_view);

			home = (Button) findViewById(R.id.coursework_home_view);
			courseSelect = (Button) findViewById(R.id.select_course);
			setting = (Button) findViewById(R.id.settings_view);
			upload = (Button) findViewById(R.id.upload_view);

			if (Session.getUser() != null && Session.getUser().getCourses().size() == 1) {
				Session.getUser().setSelectedCourseId(Session.getUser().getCourses().get(0).getId());
				Log.d("Course", "Selected course: " + Session.getUser().getCourses().get(0).getId());
				courseSelect.setEnabled(false);
			} else
				courseSelect.setEnabled(true);

			if (Session.getUser() != null && Session.getUser().getSelectedCourseId() == 99999) {
				i = new Intent(this, CourseSelect.class);
				i.putExtra("userObject", Session.getUser());
				startActivityForResult(i, COURSE_SELECT_REQUEST_CODE);
			}

			if (Session.getUser() != null && Session.getUser().getSelectedCourseId() != 99999)
				footerCourseHdr.setText(Session.getUser().getCourse(
						Session.getUser().getSelectedCourseId()).getShortName());

			getCourseDetails();

			home.setOnClickListener(this);
			if (courseSelect.isEnabled())
				courseSelect.setOnClickListener(this);
			setting.setOnClickListener(this);
			upload.setOnClickListener(this);
		} catch (Exception e) {
			Log.e("Error 1", e.toString()
					+ "Issue with Course Detail functionality");
		}
	}

	public static final int COURSE_SELECT_REQUEST_CODE = 1;

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.coursework_home_view:
			nextPage = new Intent(this, CourseDetail.class);
			startActivity(nextPage);
			break;
		case R.id.select_course:
			nextPage = new Intent(this, CourseSelect.class);
			startActivityForResult(nextPage, COURSE_SELECT_REQUEST_CODE);
			break;
		case R.id.settings_view:
			nextPage = new Intent(this, Setting.class);
			startActivity(nextPage);
			break;
		case R.id.upload_view:
			nextPage = new Intent(this, FileUpload.class);
			startActivity(nextPage);
			break;
		default:

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COURSE_SELECT_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				if (Session.getUser() != null && Session.getUser().getSelectedCourseId() != 99999) {
					Course course = Session.getUser().getCourse(Session.getUser().getSelectedCourseId());
					footerCourseHdr.setText(course.getShortName());

					getCourseDetails();

				}
			}
		}
	}

	private void getCourseDetails() {
		ArrayList<CourseContent> coursecontent = new ArrayList<CourseContent>();
		if (Session.getUser() != null && Session.getUser().getCourse(Session.getUser().getSelectedCourseId()) != null) {
			coursecontent = Session.getUser().getCourse(Session.getUser().getSelectedCourseId())
					.getCourseContent();
			Log.d("Courses", "Got course content for course id: " + Session.getUser().getSelectedCourseId());
		}
		// Populate the course overview for the list
		courseDetailList = CourseDetailsListHelper.getInstance(this)
				.populateCourseOverview(coursecontent);

		list = (ListView) findViewById(R.id.course_home_list);
		
		adapter = new LazyAdapter(this, courseDetailList);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, String> selectedMap = courseDetailList
						.get(position);
				String value = selectedMap.get(LazyAdapter.KEY_ID);
				int selectedId = Integer.valueOf(value);

				switch (selectedId) {
				case 0: // DOCUMENTS
					nextPage = new Intent(parent.getContext(),
							CourseContentController.class);
					startActivity(nextPage);
					break;
				case 1: // ASSIGNMENTS
					nextPage = new Intent(parent.getContext(),
							CourseAssignmentController.class);
					startActivity(nextPage);
					break;
				case 2: // GRADES
					nextPage = new Intent(parent.getContext(),
							CourseGradeController.class);
					startActivity(nextPage);
					break;
				case 3: // FORUMS
					nextPage = new Intent(parent.getContext(),
							CourseForumController.class);
					startActivity(nextPage);
					break;
				case 4: // OFFLINE FILES
					nextPage = new Intent(parent.getContext(), Database.class);
					startActivity(nextPage);
					break;
				default:
				}
			}
		});
	}

	public void courseChange(boolean gotCourses) {
		if(gotCourses) {
			Log.d("Courses", "Displaying courses");
			this.displayCourseChoice();
		}
		else
			Log.d("Courses", "Something went wrong while setting the courses");
		
	}

}