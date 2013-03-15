/**
 *	an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
 *	Copyright (C) 2012  Justin Stevanz, Andrew Kelson, Matthias Peitsch and Joshua Wöhle
 *
 *	Contact the.omega.online@gmail.com or jwohle@gmail.com for further information.
 *
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package moodle.android.moodle.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import moodle.android.moodle.R;
import moodle.android.moodle.helpers.asynctasks.WebServiceResponseTask;
import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.CourseContent;
import moodle.android.moodle.model.SiteInfo;
import moodle.android.moodle.other.WebServiceFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

public class MoodleWebService {

	public void getSiteinfo(String serverurl, SiteInfo siteInfo) {
		String urlParameters = ""; // moodle_webservice_get_siteinfo parameters
									// //core_webservice_get_site_info
		JSONObject jsonobj = WebServiceResponseTask.get("moodle_webservice_get_siteinfo", urlParameters,
				R.raw.siteinfoxsl);
		siteInfo.populateSiteInfo(jsonobj);
	}

	public void getUserCourses(String serverurl, int userId,
			ArrayList<Course> coursesArray) {

		String user = String.valueOf(userId);
		String urlParameters = "";

		try {
			urlParameters = "userid=" + URLEncoder.encode(user, "UTF-8");

			JSONObject jsonobj = WebServiceResponseTask.get(WebServiceFunction.moodle_enrol_get_users_courses, urlParameters,
					R.raw.coursesxsl);

			JSONArray courses = jsonobj.getJSONArray("courses");
			// looping through All Contacts
			for (int i = 0; i < courses.length(); i++) {
				JSONObject c = courses.getJSONObject(i);
				Course course = new Course();
				course.populateCourse(c);
				// Toast.makeText(context.getApplicationContext(),
				// course.getShortName(), Toast.LENGTH_LONG).show();
				// Storing each json item in variable
				coursesArray.add(course);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) { // moodle_enrol_get_users_courses
													// parameters
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void getCourseContents(String serverurl, int courseid,
			ArrayList<CourseContent> courseContentsArray) {

		String course = String.valueOf(courseid);
		String urlParameters = "";

		try {
			urlParameters = "courseid=" + URLEncoder.encode(course, "UTF-8");

			// core_course_get_contents
			JSONObject jsonobj = WebServiceResponseTask.get(WebServiceFunction.core_course_get_contents, urlParameters, R.raw.contentxsl);

			JSONArray coursecontents = jsonobj.getJSONArray("coursecontents");
			// looping through All Course Content
			for (int i = 0; i < coursecontents.length(); i++) {
				JSONObject c = coursecontents.getJSONObject(i);
				CourseContent coursecontent = new CourseContent();
				coursecontent.populateCourseContent(c);
				// Toast.makeText(context.getApplicationContext(),
				// coursecontent.getName(), Toast.LENGTH_LONG).show();
				// Storing each json item in variable
				courseContentsArray.add(coursecontent);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) { // moodle_enrol_get_users_courses
													// parameters
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// public void sendUploadFiles(String serverurl, File uploadFile) {
	// String urlParameters = ""; // moodle_webservice_get_siteinfo parameters
	// //core_webservice_get_site_info
	// JSONObject jsonobj = getWebServiceResponse(serverurl,
	// "moodle_files_upload", urlParameters, R.raw.uploadinfoxml);
	// siteInfo.populateSiteInfo(jsonobj);
	// }
}
