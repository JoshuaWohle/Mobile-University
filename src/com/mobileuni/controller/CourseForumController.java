/**
 *  an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
 *  Copyright (C) 2012  Justin Stevanz, Andrew Kelson, Matthias Peitsch and Joshua W�hle
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

import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.model.Assignment;
import com.mobileuni.model.Module;
import com.mobileuni.model.Session;
import com.mobileuni.model.User;
import com.mobileuni.other.ModuleType;

import com.mobileuni.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple controller to show the forums related to a certain course
 * @author Joshua W�hle
 */
public class CourseForumController extends Activity implements OnClickListener {

	User user;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.item_list, R.string.menu_forums);

		Intent i = getIntent();
		user = Session.getUser();

		if (user != null && Session.getCurrentSelectedCourse() == null) {
			i = new Intent(this, CourseSelectController.class);
			startActivity(i);
		}
		setForums();

	}

	private void setForums() {
		ArrayList<Module> forums = Session.getCourseManager().getModules(Session.getCurrentSelectedCourse(), ModuleType.FORUM);
		
		LinearLayout main = (LinearLayout) findViewById(R.id.item_list);
		main.removeAllViews();
		// Set title of the view
		for(Module forum : forums){
			LinearLayout child = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item, null);
			child.setTag(forum);
			child.setOnClickListener(this);
			((TextView) child.findViewById(R.id.item_title)).setText(forum.getName());
			((TextView) child.findViewById(R.id.item_content)).setText("Description: " + forum.getDescription());
			main.addView(child);
		}
	}

	public void onClick(View v) {
		Assignment forum = (Assignment) v.getTag();
		String url = forum.getUrl();
		
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			url = "http://" + url;

		startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
		
	}

}