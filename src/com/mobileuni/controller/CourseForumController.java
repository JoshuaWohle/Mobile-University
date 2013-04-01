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

import com.mobileuni.helpers.CourseContentsListHelper;
import com.mobileuni.helpers.SectionListAdapter;
import com.mobileuni.helpers.SectionListItem;
import com.mobileuni.helpers.SectionListView;
import com.mobileuni.helpers.StandardArrayAdapter;
import com.mobileuni.listeners.MenuListener;
import com.mobileuni.model.CourseContent;
import com.mobileuni.model.User;
import com.mobileuni.other.Session;

import com.mobileuni.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CourseForumController extends Activity {

	Button home, courseSelect, upload, setting;
	TextView footerCourseHdr;
	LinearLayout emptyLayout;
	FrameLayout courseworkLayout;
	User user;

	SectionListItem[] forumArray;
	StandardArrayAdapter arrayAdapter;
	SectionListAdapter sectionAdapter;
	SectionListView listView;
	
	MenuListener ml;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_forum);
		ml = new MenuListener(this);

		try {
			Intent i = getIntent();
			user = Session.getUser();

			footerCourseHdr = (TextView) findViewById(R.id.course_ftr_view);

			home = (Button) findViewById(R.id.coursework_home_view);
			courseSelect = (Button) findViewById(R.id.select_course);
			setting = (Button) findViewById(R.id.settings_view);
			upload = (Button) findViewById(R.id.upload_view);

			if (user != null && Session.getCurrentSelectedCourse() == null) {
				i = new Intent(this, CourseSelectController.class);
				i.putExtra("userObject", user);
				startActivity(i);
			}

			footerCourseHdr.setText(Session.getCurrentSelectedCourse().getShortName());

			getCourseAssignments();

			home.setOnClickListener(ml);
			if (courseSelect.isEnabled())
				courseSelect.setOnClickListener(ml);
			setting.setOnClickListener(ml);
			upload.setOnClickListener(ml);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getCourseAssignments() {
		emptyLayout = (LinearLayout) findViewById(R.id.coursework_forumitem_empty);
		emptyLayout.setVisibility(View.INVISIBLE);
		courseworkLayout = (FrameLayout) findViewById(R.id.forumlistView);
		courseworkLayout.setVisibility(View.VISIBLE);

		ArrayList<CourseContent> coursecontent = new ArrayList<CourseContent>();
		coursecontent = Session.getCurrentSelectedCourse().getCourseContent();

		forumArray = CourseContentsListHelper.getInstance(this)
				.populateCourseForums(coursecontent);

		if (forumArray != null && forumArray.length > 0) {
			arrayAdapter = new StandardArrayAdapter(this, forumArray);
			sectionAdapter = new SectionListAdapter(getLayoutInflater(),
					arrayAdapter);
			listView = (SectionListView) findViewById(R.id.forum_section_list_view);
			listView.setAdapter(sectionAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Object obj = sectionAdapter.getItem(position);
					if (obj instanceof SectionListItem) {

						SectionListItem selectedMap = (SectionListItem) obj;
						@SuppressWarnings("unchecked")
						// String value = ((HashMap<String,
						// String>)selectedMap.item).get("id");
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
							Log.e("Browser Error", e.toString()
									+ " No browser on device");
							// Raise on activity not found
							Toast.makeText(getApplicationContext(),
									"Browser not found.", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}
			});
		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			courseworkLayout.setVisibility(View.INVISIBLE);
		}
	}

}