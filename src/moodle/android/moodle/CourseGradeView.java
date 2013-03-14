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

package moodle.android.moodle;

import java.util.ArrayList;
import java.util.HashMap;

import moodle.android.moodle.helpers.CourseContentsListHelper;
import moodle.android.moodle.helpers.SectionListAdapter;
import moodle.android.moodle.helpers.SectionListItem;
import moodle.android.moodle.helpers.SectionListView;
import moodle.android.moodle.helpers.StandardArrayAdapter;
import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.CourseContent;
import moodle.android.moodle.model.User;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CourseGradeView extends Activity implements OnClickListener {

	Button home, courseSelect, upload, setting;
	TextView footerCourseHdr;
	LinearLayout emptyLayout;
	FrameLayout courseworkLayout;
	User user;

	SectionListItem[] gradeArray;
	StandardArrayAdapter arrayAdapter;
	SectionListAdapter sectionAdapter;
	SectionListView listView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_grade);

		try {
			Intent i = getIntent();
			user = (User) i.getParcelableExtra("userObject");

			footerCourseHdr = (TextView) findViewById(R.id.course_ftr_view);

			home = (Button) findViewById(R.id.coursework_home_view);
			courseSelect = (Button) findViewById(R.id.select_course);
			setting = (Button) findViewById(R.id.settings_view);
			upload = (Button) findViewById(R.id.upload_view);

			if (user != null && user.getCourses().size() == 1) {
				user.setSelectedCourseId(user.getCourses().get(0).getId());
				courseSelect.setEnabled(false);
			} else
				courseSelect.setEnabled(true);

			if (user != null && user.getSelectedCourseId() == 99999) {
				i = new Intent(this, CourseSelect.class);
				i.putExtra("userObject", user);
				startActivityForResult(i, COURSE_SELECT_REQUEST_CODE);
			}

			if (user != null && user.getSelectedCourseId() != 99999)
				footerCourseHdr.setText(user.getCourse(
						user.getSelectedCourseId()).getShortName());

			getCourseGrades();

			home.setOnClickListener(this);
			if (courseSelect.isEnabled())
				courseSelect.setOnClickListener(this);
			setting.setOnClickListener(this);
			upload.setOnClickListener(this);
		} catch (Exception e) {
			Log.e("Error 1", e.toString());
		}

	}

	public static final int COURSE_SELECT_REQUEST_CODE = 1;

	public void onClick(View v) {
		Intent nextPage;

		switch (v.getId()) {
		case R.id.coursework_home_view:
			nextPage = new Intent(this, CourseDetail.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		case R.id.select_course:
			nextPage = new Intent(this, CourseSelect.class);
			nextPage.putExtra("userObject", user);
			startActivityForResult(nextPage, COURSE_SELECT_REQUEST_CODE);
			break;
		case R.id.settings_view:
			nextPage = new Intent(this, Setting.class);
			startActivity(nextPage);
			break;
		case R.id.upload_view:
			nextPage = new Intent(this, FileUpload.class);
			nextPage.putExtra("userObject", user);
			startActivity(nextPage);
			break;
		default:

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COURSE_SELECT_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				user = (User) data.getParcelableExtra("userObject");
				if (user != null && user.getSelectedCourseId() != 99999) {
					Course course = user.getCourse(user.getSelectedCourseId());
					footerCourseHdr.setText(course.getShortName());

					getCourseGrades();
				}
			}
		}
	}

	private void getCourseGrades() {
		emptyLayout = (LinearLayout) findViewById(R.id.coursework_gradeitem_empty);
		emptyLayout.setVisibility(View.INVISIBLE);
		courseworkLayout = (FrameLayout) findViewById(R.id.gradelistView);
		courseworkLayout.setVisibility(View.VISIBLE);

		ArrayList<CourseContent> coursecontent = new ArrayList<CourseContent>();
		if (user != null && user.getCourse(user.getSelectedCourseId()) != null) {
			coursecontent = user.getCourse(user.getSelectedCourseId())
					.getCourseContent();
		}

		gradeArray = CourseContentsListHelper.getInstance(this)
				.populateCourseGrades(coursecontent);

		if (gradeArray != null && gradeArray.length > 0) {
			arrayAdapter = new StandardArrayAdapter(this, gradeArray);
			sectionAdapter = new SectionListAdapter(getLayoutInflater(),
					arrayAdapter);
			listView = (SectionListView) findViewById(getResources()
					.getIdentifier("grade_section_list_view", "id",
							this.getClass().getPackage().getName()));
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