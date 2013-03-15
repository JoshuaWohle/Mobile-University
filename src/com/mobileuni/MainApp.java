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

package com.mobileuni;

import java.util.ArrayList;

import com.mobileuni.config.Config;
import com.mobileuni.config.UserSettings;
import com.mobileuni.helpers.AppStatus;
import com.mobileuni.helpers.MoodleWebService;
import com.mobileuni.helpers.asynctasks.TokenRequestTask;
import com.mobileuni.model.Course;
import com.mobileuni.model.CourseContent;
import com.mobileuni.model.SiteInfo;
import com.mobileuni.model.User;
import com.mobileuni.other.Constants;

import moodle.android.moodle.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainApp extends Activity implements OnClickListener {
	
	public static Context context;
	//
	Button login;
	EditText serverUrl, username, password;
	User user;
	SharedPreferences saved;
	String loginDetails;

	ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		try {
			serverUrl = (EditText) findViewById(R.id.moodle_url);
			username = (EditText) findViewById(R.id.username);
			password = (EditText) findViewById(R.id.password);
			login = (Button) findViewById(R.id.login_button);

			try {
				
				serverUrl.setHint(R.string.login_url_hint);
				username.setHint(R.string.login_username_hint);
				password.setHint(R.string.login_password_hint);

				// siteUrl.setText(saved.getString("siteUrlVal",
				// "http://moodletest.shaftedartist.com"));
				// username.setText(saved.getString("usr", "guest@guest"));
				// password.setText(saved.getString("pwd","Gu3$t%000"));

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

			// this dialog box needs to be run separately....

			dialog = ProgressDialog.show(this, "", "Logging in, please wait",
					true);

			/*
			 * new Thread(new Runnable(){ public void run(){
			 */

			if (AppStatus.getInstance(MainApp.this).isOnline(MainApp.this)) {
				String conType = AppStatus.getInstance(MainApp.this)
						.getConnectionType(MainApp.this);
				conType = conType == null ? "Unknown" : conType;
				Toast.makeText(getApplicationContext(),
						"You are online (" + conType + ")!!!!",
						Toast.LENGTH_LONG).show();

			} else {
				messageHandler.sendEmptyMessage(0);

				Toast.makeText(getApplicationContext(),
						"You are not online!!!!", Toast.LENGTH_LONG).show();
				// enter details for offline access to some files. Restore the
				// database.

			}
			Config.serverUrl = serverUrl.getText().toString();

			// checks for http:// entry
			/*
			 * if(!(siteUrlVal.substring(0,
			 * 7).toLowerCase().compareTo("http://")==0)) { siteUrlVal =
			 * "http://"+siteUrlVal; siteUrl.setText(siteUrlVal); }
			 */

			String usr = username.getText().toString();
			String pwd = password.getText().toString();

			String usrUri = Uri.encode(usr);
			String pwdUri = Uri.encode(pwd);

			saved = getSharedPreferences(loginDetails, MODE_PRIVATE);

			SharedPreferences.Editor e = saved.edit();
			e.putString("siteUrlVal", Config.serverUrl);
			e.putString("usr", usr);
			e.putString("pwd", pwd);
			e.commit();

			String tokenUrl = Config.serverUrl + "/login/token.php?username=" + usrUri
					+ "&password=" + pwdUri + "&service=moodle_mobile_app";

			TokenRequestTask.get(tokenUrl);
			Log.d("authentication", "token set to : " + UserSettings.userToken);

			// Toast.makeText(getApplicationContext(), token,
			// Toast.LENGTH_LONG).show();

			if (UserSettings.userToken != null && UserSettings.userToken != "") {

				Config.apiUrl = Config.serverUrl + "/webservice/rest/server.php"
						+ "?wstoken=" + UserSettings.userToken + "&wsfunction=";

				user = new User();
				user.setUsername(usr);
				user.setPassword(pwd);
				user.setToken(UserSettings.userToken);
				user.setTokenCreateDate();
				user.setUrl(Config.apiUrl);

				MoodleWebService webService = new MoodleWebService();
				SiteInfo siteInfo = new SiteInfo();
				webService.getSiteinfo(siteInfo);
				user.setSiteInfo(siteInfo);
				ArrayList<Course> courses = new ArrayList<Course>();
				webService.getUserCourses(Config.apiUrl, siteInfo.getUserid(),
						courses);

				if (courses.size() > 0) {
					for (int i = 0; i < courses.size(); i++) {
						Course c = courses.get(i);
						ArrayList<CourseContent> coursecontents = new ArrayList<CourseContent>();
						webService.getCourseContents(Config.apiUrl, c.getId(),
								coursecontents);

						if (coursecontents.size() > 0) {
							c.setCourseContent(coursecontents);
						}
					}
					user.setCourses(courses);

					Intent nextPage;

					nextPage = new Intent(MainApp.this, CourseDetail.class);
					nextPage.putExtra("userObject", user);

					startActivity(nextPage);
				} else {
					messageHandler.sendEmptyMessage(0);

					Log.e("Course Error", "User is not enrolled in any courses");
					Toast.makeText(
							getApplicationContext(),
							R.string.user_no_courses,
							Toast.LENGTH_LONG).show();

				}

			} else {
				messageHandler.sendEmptyMessage(0);

				Toast.makeText(
						getApplicationContext(),
						R.string.login_incorrect,
						Toast.LENGTH_LONG).show();
			}

			/*
			 * } }).start();
			 */

			break;
		default:

		}
	}

	private Handler messageHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.dismiss();

		}
	};

}