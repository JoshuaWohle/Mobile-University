/**
 *  an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
 *  	Copyright (C) 2012  Justin Stevanz, Andrew Kelson, Matthias Peitsch and Joshua Wöhle
 *
 *	Contact the.omega.online@gmail.com or jwohle@gmail.com for further information.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.mobileuni.controller;

import com.mobileuni.config.Config;
import com.mobileuni.helpers.AppStatus;
import com.mobileuni.listeners.iCourseManagerListener;
import com.mobileuni.model.Moodle;
import com.mobileuni.model.Session;
import com.mobileuni.model.University;
import com.mobileuni.model.User;
import com.mobileuni.other.Constants;

import com.mobileuni.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginController extends Activity implements OnClickListener, iCourseManagerListener {
	
	EditText username, password;
	Spinner universitySelect;
	EditText courseManagerURL;
	
	ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Session.setContext(this);
		setContentView(R.layout.login);
		AppStatus.setChangeOrientation(this);
		University.init();
		universitySelect = (Spinner) findViewById(R.id.login_university_select);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		        University.getUniversityNameList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		universitySelect.setAdapter(adapter);
		
		// TODO make really independent of Moodle
		Session.setCourseManager(new Moodle());
		Session.getCourseManager().addListener(this);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
		username.setText(Constants.testUser);
		password.setText(Constants.testPassword);

		((Button) findViewById(R.id.login_button)).setOnClickListener(this);
		((TextView) findViewById(R.id.login_advanced_config)).setOnClickListener(this);
	}

	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.login_button:
				login();
				break;
			case R.id.login_advanced_config:
				if(courseManagerURL == null) {
					courseManagerURL = new EditText(this);
					courseManagerURL.setHint(R.string.login_url);
					LinearLayout parent = (LinearLayout) this.findViewById(R.id.login_main_layout);
					parent.addView(courseManagerURL, 4);
				}
				break;
			default:
				Log.d(Constants.LOG_AUTHENTICATION, "Some button / view was clicked that we do not know of.");
				break;
		}
	}
	
	public void login() {
		// Weirdly enough you need the getResources() here...
		dialog = ProgressDialog.show(this, getResources().getString(R.string.loading), 
				getResources().getString(R.string.wait_while_login), true, true,
				new DialogInterface.OnCancelListener() {
					
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		
		if (AppStatus.isOnline()) {
			Session.setUser(User.load());
			
			if(Session.getUser() == null)
				Session.setUser(new User());
			
			if(courseManagerURL == null)
				Config.serverUrl = University.getCourseManagerURL(universitySelect.getSelectedItem().toString());
			else
				Config.serverUrl = courseManagerURL.getText().toString();
			
			Session.getUser().setUsername(username.getText().toString());
			Session.getUser().setPassword(password.getText().toString());
			Session.getUser().save();
			Session.getCourseManager().login(Session.getUser());
		} else {
			// Offline usage
			Log.d(Constants.LOG_SESSION, "Proceding with offline session");
			Session.setUser(User.load());
			if(Session.getUser() == null) {
				Toast.makeText(this, "No connection and no previous data (so cannot browse offline)."
						+ " Please make sure you have a data connection and try again.", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				return;
			}
			loginChange(true);
		}
	}

	public void loginChange(boolean loggedIn) {
		dialog.dismiss();
		Log.d(Constants.LOG_AUTHENTICATION, "dismissed login dialog.");
		if(loggedIn) {
			Intent intent = new Intent(LoginController.this, CourseSelectController.class);
			startActivity(intent);
		}
		else {
			Toast.makeText(getApplicationContext(), R.string.login_incorrect,
					Toast.LENGTH_LONG).show();
		}
	}
	
	
}