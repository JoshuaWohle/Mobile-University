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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.mobileuni.helpers.AppStatus;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.listeners.CourseChangeListener;
import com.mobileuni.model.ContentItem;
import com.mobileuni.model.Course;
import com.mobileuni.model.User;
import com.mobileuni.other.Constants;
import com.mobileuni.other.ContentType;
import com.mobileuni.other.Session;

import com.mobileuni.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CourseDocumentController extends Activity implements CourseChangeListener, OnClickListener {

	User user;

	Course selectedCourse;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.item_list, R.string.menu_documents);
		user = Session.getUser();
		selectedCourse = Session.getCurrentSelectedCourse();
		selectedCourse.addListener(this);

		setCourseDocuments();

	}

	private void setCourseDocuments() {
		ArrayList<ContentItem> documents = Session.getCourseManager().getCourseContentTypeByCourse(
				Session.getCurrentSelectedCourse(), ContentType.DOCUMENT);
		
		LinearLayout main = (LinearLayout) findViewById(R.id.item_list);
		// Set title of the view
		for(ContentItem document : documents){
			Log.d(Constants.LOG_DOCUMENTS, "Adding course: " + document.getFileName());
			LinearLayout child = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item, null);
			child.setTag(document);
			child.setOnClickListener(this);
			((TextView) child.findViewById(R.id.item_title)).setText(document.getFileName());
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(document.getTimeCreated());
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
			((TextView) child.findViewById(R.id.item_content)).setText(
					"Size: " + document.getFileSize() + "B \n" +
					"Created: " + sdf.format(cal.getTime()) + " \n" +
					"Author: "	+ document.getAuthor());
			main.addView(child);
		}
	}

	public void downloadedFile(String absoluteFilePath) {
		File file = new File(absoluteFilePath);
		Log.d(Constants.LOG_DOCUMENTS, "Opening file: " + file.getAbsolutePath());
		if(!file.exists()) {
			Toast.makeText(this, getResources().getString(R.string.file_not_found), Toast.LENGTH_SHORT);
		}

		MimeTypeMap myMime = MimeTypeMap.getSingleton();
		Intent i = new Intent(android.content.Intent.ACTION_VIEW);

		// Intent newIntent = new
		// Intent(Intent.ACTION_VIEW);
		String mimeType = myMime.getMimeTypeFromExtension(fileExt(
				file.toString()).substring(1));
		i.setDataAndType(Uri.fromFile(file), mimeType);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			CourseDocumentController.this.startActivity(i);
		} catch (ActivityNotFoundException e) {
			Log.e("MIME Error", e.toString()
					+ " default program for this filetype not found");
			// Raise on activity not found
			Toast.makeText(
					CourseDocumentController.this,
					"A suitable Application to access the file " + mimeType
							+ " not found.", Toast.LENGTH_SHORT).show();
		}
	}

	public String fileExt(String url) {
		if (url.indexOf("?") > -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf("."));
			if (ext.indexOf("%") > -1) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.indexOf("/") > -1) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();

		}
	}

	public void onClick(View v) {
		ContentItem document = (ContentItem) v.getTag();
		File file = Session.getCourseManager().downloadDocument(Session.getCurrentSelectedCourse(), document.getFileName());
		if(AppStatus.isOnline()) {
			if(null != file)
				downloadedFile(file.getAbsolutePath());
		} else
			downloadedFile(file.getAbsolutePath());
		
	}

	public void courseContentsChanged() {
		// Nothing to do here
	}

	public void fileChanged(String filePath) {
		Log.d(Constants.LOG_DOCUMENTS, "Triggering response to downloaded file");
		downloadedFile(filePath);
	}

	public void notesChanged() {
		// Nothing to do on this view
		
	}

}