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

package moodle.android.moodle;

import java.util.ArrayList;

import moodle.android.moodle.config.Config;
import moodle.android.moodle.config.UserSettings;
import moodle.android.moodle.helpers.AppStatus;
import moodle.android.moodle.helpers.MoodleWebService;
import moodle.android.moodle.helpers.asynctasks.TokenRequestTask;
import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.CourseContent;
import moodle.android.moodle.model.SiteInfo;
import moodle.android.moodle.model.User;
import moodle.android.moodle.other.Constants;

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
			String siteUrlVal = Config.serverUrl;

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
			e.putString("siteUrlVal", siteUrlVal);
			e.putString("usr", usr);
			e.putString("pwd", pwd);
			e.commit();

			String url = siteUrlVal + "/login/token.php?username=" + usrUri
					+ "&password=" + pwdUri + "&service=moodle_mobile_app";

			new TokenRequestTask().execute(url);
			String token = UserSettings.userToken;

			// Toast.makeText(getApplicationContext(), token,
			// Toast.LENGTH_LONG).show();

			if (token != null && token != "") {

				String serverurl = siteUrlVal + "/webservice/rest/server.php"
						+ "?wstoken=" + token + "&wsfunction=";

				user = new User();
				user.setUsername(usr);
				user.setPassword(pwd);
				user.setToken(token);
				user.setTokenCreateDate();
				user.setUrl(url);

				MoodleWebService webService = new MoodleWebService();
				SiteInfo siteInfo = new SiteInfo();
				webService.getSiteinfo(serverurl, siteInfo);
				user.setSiteInfo(siteInfo);
				ArrayList<Course> courses = new ArrayList<Course>();
				webService.getUserCourses(serverurl, siteInfo.getUserid(),
						courses);

				if (courses.size() > 0) {
					for (int i = 0; i < courses.size(); i++) {
						Course c = courses.get(i);
						ArrayList<CourseContent> coursecontents = new ArrayList<CourseContent>();
						webService.getCourseContents(serverurl, c.getId(),
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