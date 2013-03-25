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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.mobileuni.CourseDetail;
import com.mobileuni.CourseSelect;
import com.mobileuni.FileUpload;
import com.mobileuni.Setting;
import com.mobileuni.helpers.CourseContentsListHelper;
import com.mobileuni.helpers.FileManager;
import com.mobileuni.helpers.SectionListAdapter;
import com.mobileuni.helpers.SectionListItem;
import com.mobileuni.helpers.SectionListView;
import com.mobileuni.helpers.StandardArrayAdapter;
import com.mobileuni.listeners.MenuListener;
import com.mobileuni.model.Course;
import com.mobileuni.model.CourseContent;
import com.mobileuni.model.User;
import com.mobileuni.other.Session;

import com.mobileuni.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CourseContentController extends Activity {

	Button home, courseSelect, upload, setting;
	TextView footerCourseHdr;
	LinearLayout emptyLayout;
	FrameLayout courseworkLayout;
	User user;

	SectionListItem[] documentArray;
	StandardArrayAdapter arrayAdapter;
	SectionListAdapter sectionAdapter;
	SectionListView listView;
	
	Course selectedCourse;
	MenuListener ml;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_material);
		ml = new MenuListener(this);
		user = Session.getUser();
		Intent intent = getIntent();
		selectedCourse = Session.getUser().getCourse(intent.getIntExtra("selected_course_id", 0));

		try {

			footerCourseHdr = (TextView) findViewById(R.id.course_ftr_view);

			home = (Button) findViewById(R.id.coursework_home_view);
			courseSelect = (Button) findViewById(R.id.select_course);
			setting = (Button) findViewById(R.id.settings_view);
			upload = (Button) findViewById(R.id.upload_view);

			if (user != null && selectedCourse.getId() != 99999)
				footerCourseHdr.setText(selectedCourse.getShortName());

			getCourseDetails();

			home.setOnClickListener(ml);
			courseSelect.setOnClickListener(ml);
			setting.setOnClickListener(ml);
			upload.setOnClickListener(ml);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static final int COURSE_SELECT_REQUEST_CODE = 1;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COURSE_SELECT_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				user = (User) data.getParcelableExtra("userObject");
				if (user != null && user.getSelectedCourseId() != 99999) {
					Course course = selectedCourse;
					footerCourseHdr.setText(course.getShortName());

					getCourseDetails();
				}
			}
		}
	}

	private void getCourseDetails() {
		emptyLayout = (LinearLayout) findViewById(R.id.coursework_item_empty);
		emptyLayout.setVisibility(View.INVISIBLE);
		courseworkLayout = (FrameLayout) findViewById(R.id.listView);
		courseworkLayout.setVisibility(View.VISIBLE);

		ArrayList<CourseContent> coursecontent = new ArrayList<CourseContent>();
		if (user != null && selectedCourse != null) {
			coursecontent = selectedCourse.getCourseContent();
			Log.d("Course", "Got course contents for course : " + selectedCourse.getId());
		}

		documentArray = CourseContentsListHelper.getInstance(this)
				.populateCourseDocuments(
						coursecontent,
						selectedCourse.getFullname());

		if (documentArray != null && documentArray.length > 0) {
			arrayAdapter = new StandardArrayAdapter(this, documentArray);
			sectionAdapter = new SectionListAdapter(getLayoutInflater(),
					arrayAdapter);
			listView = (SectionListView) findViewById(getResources()
					.getIdentifier("document_section_list_view", "id",
							this.getClass().getPackage().getName()));
			listView.setAdapter(sectionAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@SuppressLint("ShowToast")
				@SuppressWarnings("static-access")
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Object obj = sectionAdapter.getItem(position);
					if (obj instanceof SectionListItem) {

						SectionListItem selectedMap = (SectionListItem) obj;
						@SuppressWarnings("unchecked")
						HashMap<String, String> selectedItem = (HashMap<String, String>) selectedMap.item;
						String fileURL = selectedItem.get("url");
						String fileName = selectedItem.get("filename");
						String courseDirectoryAndType = selectedCourse.getFullname()
								+ "/Documents/";

						File file = FileManager.getInstance(
								CourseContentController.this).DownloadFromUrl(
								fileURL + "&token=" ,//TODO add token + user.getToken(),
								fileName, courseDirectoryAndType);

						if (file != null) {
							MimeTypeMap myMime = MimeTypeMap.getSingleton();
							Intent newIntent = new Intent(
									android.content.Intent.ACTION_VIEW);

							// Intent newIntent = new
							// Intent(Intent.ACTION_VIEW);
							String mimeType = myMime
									.getMimeTypeFromExtension(fileExt(
											file.toString()).substring(1));
							newIntent.setDataAndType(Uri.fromFile(file),
									mimeType);
							newIntent
									.setFlags(newIntent.FLAG_ACTIVITY_NEW_TASK);
							try {
								CourseContentController.this.startActivity(newIntent);
							} catch (android.content.ActivityNotFoundException e) {
								Log.e("MIME Error",
										e.toString()
												+ " default program for this filetype not found");
								// Raise on activity not found
								Toast.makeText(
										CourseContentController.this,
										"A suitable Application to access the file "
												+ mimeType + " not found.",
										4000).show();
							}
						} else {
							Toast.makeText(
									CourseContentController.this,
									"There is no SD Card installed to save the file to. Please insert to view the file.",
									4000).show();
						}
					}
				}
			});
		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			courseworkLayout.setVisibility(View.INVISIBLE);
		}
	}

	public String fileExt(String url) {
		if (url.indexOf("?") > -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf("."));
			if (ext.indexOf("%") > -1) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.indexOf("/") > -1) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();

		}
	}

}