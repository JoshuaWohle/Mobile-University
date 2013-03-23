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

package com.mobileuni.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import moodle.android.moodle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobileuni.helpers.asynctasks.WebServiceResponseTask;
import com.mobileuni.model.CourseContent;
import com.mobileuni.other.WebServiceFunction;

public class MoodleWebService {

	public void getCourseContents(String serverurl, int courseid,
			ArrayList<CourseContent> courseContentsArray) {

		String course = String.valueOf(courseid);
		String urlParameters = "";

		try {
			urlParameters = "courseid=" + URLEncoder.encode(course, "UTF-8");

			// core_course_get_contents
			JSONObject jsonobj = WebServiceResponseTask.get(WebServiceFunction.core_course_get_contents, urlParameters, R.raw.contentxsl);

			JSONArray courseContents = jsonobj.getJSONArray("coursecontents");
			// looping through All Course Content
			for (int i = 0; i < courseContents.length(); i++) {
				JSONObject c = courseContents.getJSONObject(i);
				CourseContent courseContent = new CourseContent();
				courseContent.populateCourseContent(c);
				// Toast.makeText(context.getApplicationContext(),
				// coursecontent.getName(), Toast.LENGTH_LONG).show();
				// Storing each json item in variable
				courseContentsArray.add(courseContent);
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
