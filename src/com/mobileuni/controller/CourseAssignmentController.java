/**
 *  an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
 *  Copyright (C) 2012  Justin Stevanz, Andrew Kelson, Matthias Peitsch and Joshua Wöhle
 *
 *	Contact the.omega.online@gmail.com or jwohle@gmail.com for further information.
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

import com.mobileuni.helpers.CourseContentsListHelper;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.helpers.SectionListAdapter;
import com.mobileuni.helpers.SectionListItem;
import com.mobileuni.helpers.SectionListView;
import com.mobileuni.helpers.StandardArrayAdapter;
import com.mobileuni.model.CourseContents;
import com.mobileuni.other.Session;

import com.mobileuni.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CourseAssignmentController extends Activity {

	FrameLayout courseworkLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.course_assignment, R.string.menu_assignments);

		Intent i = getIntent();
		
		if (Session.getCurrentSelectedCourse() == null) {
			i = new Intent(this, CourseSelectController.class);
			startActivity(i);
		}
		getCourseAssignments();
		
	}

	private void getCourseAssignments() {
		courseworkLayout = (FrameLayout) findViewById(R.id.assignlistView);

		ArrayList<CourseContents> coursecontent = new ArrayList<CourseContents>();
		coursecontent = Session.getCurrentSelectedCourse().getCourseContent();

		SectionListItem[] assignArray = CourseContentsListHelper.getInstance(this)
				.populateCourseAssignments(coursecontent);

		if (assignArray != null && assignArray.length > 0) {
			StandardArrayAdapter arrayAdapter = new StandardArrayAdapter(this, assignArray);
			final SectionListAdapter sectionAdapter = new SectionListAdapter(getLayoutInflater(),
					arrayAdapter);
			SectionListView listView = (SectionListView) findViewById(R.id.assign_section_list_view);
			listView.setAdapter(sectionAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Object obj = sectionAdapter.getItem(position);
					if (obj instanceof SectionListItem) {

						SectionListItem selectedMap = (SectionListItem) obj;
						@SuppressWarnings("unchecked")

						String url = ((HashMap<String, String>) selectedMap.item)
								.get("url");

						if (!url.startsWith("http://")
								&& !url.startsWith("https://"))
							url = "http://" + url;

						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(url));

						try {
							// Start the activity
							startActivity(browserIntent);
						} catch (ActivityNotFoundException e) {
							// Raise on activity not found
							Toast.makeText(getApplicationContext(),
									"Browser not found.", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}
			});
		}
	}

}