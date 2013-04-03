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

package com.mobileuni.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.mobileuni.helpers.AppStatus;
import com.mobileuni.helpers.CourseDetailsListHelper;
import com.mobileuni.helpers.LazyAdapter;
import com.mobileuni.listeners.CourseChangeListener;
import com.mobileuni.listeners.MenuListener;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CourseDetailController extends Activity implements CourseChangeListener {

	Button home, courseSelect, upload, setting;
	TextView footerCourseHdr, documents, assignments, grades, forum, offline;
	LazyAdapter adapter;
	ListView list;
	ProgressDialog dialog;
	ArrayList<HashMap<String, String>> courseDetailList = new ArrayList<HashMap<String, String>>();
	
	Course selectedCourse;
	MenuListener ml;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_detail);
		dialog = ProgressDialog.show(this, 
					getResources().getString(R.string.loading), 
					getResources().getString(R.string.wait_while_get_course_detail));
		ml = new MenuListener(this);
		selectedCourse = Session.getCurrentSelectedCourse();
		selectedCourse.addListener(this);
		
		// Check if online
		if(AppStatus.isOnline())
			Session.getCourseManager().setCourseDetails(null, selectedCourse.getId()); // Get new details of course
		else
			courseContentsChanged(); // Serve old content
	}
	
	public void displayCourseChoice() {
		try {
			footerCourseHdr = (TextView) findViewById(R.id.course_ftr_view);

			home = (Button) findViewById(R.id.coursework_home_view);
			courseSelect = (Button) findViewById(R.id.select_course);
			setting = (Button) findViewById(R.id.settings_view);
			upload = (Button) findViewById(R.id.upload_view);

			if (Session.getUser() != null && Session.getCurrentSelectedCourse() != null)
				footerCourseHdr.setText(selectedCourse.getShortName());

			getCourseDetails();
			
			courseSelect.setOnClickListener(ml);
			home.setOnClickListener(ml);
			setting.setOnClickListener(ml);
			upload.setOnClickListener(ml);
		} catch (Exception e) {
			Log.e("Error 1", e.toString()
					+ "Issue with Course Detail functionality");
		}
	}

	private void getCourseDetails() {
		ArrayList<CourseContent> courseContent = new ArrayList<CourseContent>();
		if (Session.getUser() != null && selectedCourse != null) {
			courseContent = selectedCourse.getCourseContent();
			Log.d("Courses", "Got course content for course id: " + selectedCourse.getId());
		}
		// Populate the course overview for the list
		courseDetailList = CourseDetailsListHelper.getInstance(this)
				.populateCourseOverview(courseContent);

		list = (ListView) findViewById(R.id.course_home_list);
		
		adapter = new LazyAdapter(this, courseDetailList);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {
			Intent intent;
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, String> selectedMap = courseDetailList
						.get(position);
				String value = selectedMap.get(LazyAdapter.KEY_ID);
				int selectedId = Integer.valueOf(value);

				switch (selectedId) {
				case 0: // DOCUMENTS
					intent = new Intent(parent.getContext(),
							CourseContentController.class);
					startActivity(intent);
					break;
				case 1: // ASSIGNMENTS
					intent = new Intent(parent.getContext(),
							CourseAssignmentController.class);
					startActivity(intent);
					break;
				case 2: // GRADES
					intent = new Intent(parent.getContext(),
							CourseGradeController.class);
					startActivity(intent);
					break;
				case 3: // FORUMS
					intent = new Intent(parent.getContext(),
							CourseForumController.class);
					startActivity(intent);
					break;
				case 4: // OFFLINE FILES
					intent = new Intent(parent.getContext(), DatabaseController.class);
					startActivity(intent);
					break;
				default:
				}
			}
		});
	}

	public void courseContentsChanged() {
		dialog.dismiss();
		displayCourseChoice();		
	}

}