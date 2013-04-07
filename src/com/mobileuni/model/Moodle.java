package com.mobileuni.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import com.mobileuni.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import com.mobileuni.config.Config;
import com.mobileuni.helpers.asynctasks.TokenRequestTask;
import com.mobileuni.helpers.asynctasks.WebServiceResponseTask;
import com.mobileuni.listeners.iCourseManagerListener;
import com.mobileuni.other.Constants;
import com.mobileuni.other.Session;
import com.mobileuni.other.WebServiceFunction;

public class Moodle implements iCourseManager {
	
	private String tokenURL = "";
	private String token = "";
	private int currentMoodleUserID = 0;
	private String siteName = "";
	private String siteUrl = "";
	private Map<String, String> availableFunctions = Collections
			.synchronizedMap(new TreeMap<String, String>());

	public void login(User user) {
		String usr = Uri.encode(user.getUsername());
		String pwd = Uri.encode(user.getPassword());
		tokenURL = Config.serverUrl + "/login/token.php?username=" + usr
				+ "&password=" + pwd + "&service=moodle_mobile_app";
		new TokenRequestTask().execute(tokenURL, this);
	}

	public void setCourses(JSONObject jsonObject) {
		if (jsonObject == null) {
			try {
				String urlParameters = "userid="
						+ URLEncoder.encode(
								String.valueOf(this.getCurrentMoodleUserID()),
								"UTF-8");
				Log.d(Constants.LOG_COURSE,
						"Starting request to get courses with moodle ID: "
								+ this.getCurrentMoodleUserID());
				new WebServiceResponseTask().execute(
						WebServiceFunction.moodle_enrol_get_users_courses,
						urlParameters, R.raw.coursesxsl);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		Log.d(Constants.LOG_COURSE, "Got courses, now populating them");
		ArrayList<Course> courseArray = new ArrayList<Course>();

		JSONArray courses;
		try {
			courses = jsonObject.getJSONArray("courses");
			// looping through All Courses
			for (int i = 0; i < courses.length(); i++) {
				JSONObject c = courses.getJSONObject(i);
				Course course = new Course();
				course.populateCourse(c);
				courseArray.add(course);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Session.getUser().setCourses(courseArray);
		Session.getUser().save();
	}

	public void setCourseDetails(JSONObject jsonObject, int courseId) {
		
		if(null == jsonObject) {
			try {
				String urlParameters = "courseid="
						+ URLEncoder.encode(String.valueOf(courseId),
								"UTF-8");
				new WebServiceResponseTask().execute(
						WebServiceFunction.core_course_get_contents,
						urlParameters, R.raw.contentxsl, courseId);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return;
		}
		
		ArrayList<CourseContent> courseContentsArray = new ArrayList<CourseContent>();
		try {
			JSONArray courseContents = jsonObject
					.getJSONArray("coursecontents");
			
			// looping through All Course Content
			for (int i = 0; i < courseContents.length(); i++) {
				JSONObject c = courseContents.getJSONObject(i);
				CourseContent courseContent = new CourseContent();
				courseContent.populateCourseContent(c);
				courseContentsArray.add(courseContent);
			}
			
			Session.getUser().getCourse(courseId).setCourseContent(courseContentsArray);
			Session.getUser().save();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initMainInfo() {
		new WebServiceResponseTask().execute(
				WebServiceFunction.moodle_webservice_get_siteinfo, "",
				R.raw.siteinfoxsl);
	}

	public void setMainInfo(JSONObject jsonObject) {

		try {
			if (jsonObject != null) {

				this.setSiteName(jsonObject.getString("sitename"));
				this.setCurrentMoodleUserID(Integer.valueOf(jsonObject
						.getString("userid")));
				this.setSiteUrl(jsonObject.getString("siteurl"));
				// String downloadfiles = jsonObject.getString("downloadfiles");
				// this.setDownloadFiles((downloadfiles.equals("1")) ?
				// Boolean.TRUE : Boolean.FALSE);

				JSONArray functions = jsonObject.getJSONArray("functions");
				
				// looping through All Functions
				Map<String, String> temp = Collections
						.synchronizedMap(new TreeMap<String, String>());
				for (int i = 0; i < functions.length(); i++) {
					JSONObject c = functions.getJSONObject(i);

					// Storing each JSON item in variable
					String name = c.getString("name");
					String version = c.getString("version");
					temp.put(name, version);
				}
				
				this.setAvailableFunctions(temp);
				for (iCourseManagerListener listener : icml) {
					listener.loginChange(true);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getTokenURL() {
		return tokenURL;
	}

	public void setTokenURL(String tokenURL) {
		this.tokenURL = tokenURL;
	}

	public int getCurrentMoodleUserID() {
		return currentMoodleUserID;
	}

	public void setCurrentMoodleUserID(int currentMoodleUserID) {
		this.currentMoodleUserID = currentMoodleUserID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;

		if (this.token != null && token != "") {
			Config.apiUrl = Config.serverUrl + "/webservice/rest/server.php"
					+ "?wstoken=" + token + "&wsfunction=";
			initMainInfo();
		} else {
			Log.d(Constants.LOG_AUTHENTICATION, "User token has been set to null");
			for (iCourseManagerListener listener : icml) {
				listener.loginChange(false);
			}
		}

	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public Map<String, String> getAvailableFunctions() {
		return availableFunctions;
	}

	public void setAvailableFunctions(Map<String, String> availableFunctions) {
		this.availableFunctions = availableFunctions;
	}

	public boolean editAvailableFunction(String oldVersion, String newVersion,
			String name) {

		if (availableFunctions.containsKey(oldVersion)) {
			availableFunctions.remove(oldVersion);
			availableFunctions.put(newVersion, name);
			return true;
		}

		return false;
	}

	public void addListener(iCourseManagerListener listener) {
		icml.add(listener);
	}

	public void removeListener(iCourseManagerListener listener) {
		icml.remove(listener);
	}
}
