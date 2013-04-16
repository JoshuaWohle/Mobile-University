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

import com.mobileuni.helpers.AppStatus;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.listeners.CourseChangeListener;
import com.mobileuni.model.CourseSectionItem;
import com.mobileuni.model.Session;

import com.mobileuni.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseDetailController extends Activity implements
		CourseChangeListener, OnClickListener {

	ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.item_list,
				R.string.app_name);
		dialog = ProgressDialog.show(this,
				getResources().getString(R.string.loading), getResources()
						.getString(R.string.wait_while_get_course_detail));

		if (Session.getCurrentSelectedCourse() == null)
			startActivity(new Intent(this, CourseSelectController.class));
		else {
			Session.getCurrentSelectedCourse().addListener(this);

			// Check if online
			if (AppStatus.isOnline())
				Session.getCourseManager().setCourseDetails(null,
						Session.getCurrentSelectedCourse().getId()); // Get new
																		// details
																		// of
																		// course
			else
				courseContentsChanged(); // Serve old content
		}
	}

	public void displayCourseChoice() {
		if (Session.getUser() != null
				&& Session.getCurrentSelectedCourse() != null)

			showCourseSections();
	}

	private void showCourseSections() {
		LinearLayout main = (LinearLayout) findViewById(R.id.item_list);
		main.removeAllViews();
		// Set title of the view
		for (CourseSectionItem courseSection : MenuHelper.courseSectionList) {
			LinearLayout child = (LinearLayout) getLayoutInflater().inflate(
					R.layout.list_item, null);
			child.setTag(courseSection.targetActivity);
			child.setOnClickListener(this);
			((TextView) child.findViewById(R.id.item_title))
					.setText(courseSection.name);
			((TextView) child.findViewById(R.id.item_content))
					.setText(courseSection.description);
			main.addView(child);
		}
	}

	public void courseContentsChanged() {
		dialog.dismiss();
		displayCourseChoice();
	}

	public void fileChanged(String filename) {
		// Nothing to do here
	}

	public void notesChanged() {
		// Nothing to do on this view

	}

	public void onClick(View v) {

		// If the clicked menu is a course section item, just go to its
		// corresponding section
		if (MenuHelper.isCourseSection((Class) v.getTag())) {
			Intent i = new Intent(this, (Class<?>) v.getTag());
			startActivity(i);
		}
	}

}