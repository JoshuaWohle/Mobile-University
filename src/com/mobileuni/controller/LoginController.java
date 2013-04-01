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
import com.mobileuni.model.User;
import com.mobileuni.other.Constants;
import com.mobileuni.other.Session;

import com.mobileuni.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginController extends Activity implements OnClickListener, iCourseManagerListener {
	
	Button login;
	EditText serverUrl, username, password;
	SharedPreferences saved;
	String loginDetails;
	
	ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Session.setContext(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// TODO make really independent of Moodle
		Session.setCourseManager(new Moodle());
		Session.getCourseManager().addListener(this);
		Session.setUser(new User());

		try {
			serverUrl = (EditText) findViewById(R.id.moodle_url);
			username = (EditText) findViewById(R.id.username);
			password = (EditText) findViewById(R.id.password);
			login = (Button) findViewById(R.id.login_button);

			try {
				serverUrl.setHint(R.string.login_url_hint);
				username.setHint(R.string.login_username_hint);
				password.setHint(R.string.login_password_hint);

				serverUrl.setText(Constants.testURL);
				username.setText(Constants.testUser);
				password.setText(Constants.testPassword);
			} catch (Exception e) {
				Log.e("NoPreferences", e.toString());
			}

			login.setOnClickListener(this);

		} catch (Exception e) {
			Log.e("Error With Login", e.toString());
		}

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login_button:

			/*
			 * new Thread(new Runnable(){ public void run(){
			 */

			if (AppStatus.getInstance(LoginController.this).isOnline(LoginController.this)) {
				String conType = AppStatus.getInstance(LoginController.this)
						.getConnectionType(LoginController.this);
				conType = conType == null ? "Unknown" : conType;
				Toast.makeText(getApplicationContext(),
						R.string.not_online + "(" + conType + ")",
						Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(getApplicationContext(),
						R.string.not_online, Toast.LENGTH_LONG).show();
				// enter details for offline access to some files. Restore the
				// database.
			}
			
			login();
			
			saved = getSharedPreferences(loginDetails, MODE_PRIVATE);

			SharedPreferences.Editor e = saved.edit();
			e.putString("siteUrlVal", Config.serverUrl);
			e.putString("usr", Session.getUser().getUsername());
			e.putString("pwd", Session.getUser().getPassword());
			e.commit();

			break;
		default:

		}
	}
	
	public void login() {
		// Weirdly enough you need the getResources() here...
		dialog = ProgressDialog.show(this, getResources().getString(R.string.loading), getResources().getString(R.string.wait_while_login));
		Config.serverUrl = serverUrl.getText().toString();
		
		Session.getUser().setUsername(username.getText().toString());
		Session.getUser().setPassword(password.getText().toString());
		Session.getCourseManager().login(Session.getUser());
	}

	public void loginChange(boolean loggedIn) {
		dialog.dismiss();
		Log.d("authentication", "dismissed login dialog.");
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