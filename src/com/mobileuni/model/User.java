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

package com.mobileuni.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import com.mobileuni.listeners.UserChangeListener;
import com.mobileuni.other.Constants;
import com.mobileuni.other.Session;

import android.content.Context;
import android.util.Log;

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String saveFileName = "user";
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String profilePictureURL;
	
	private ArrayList<Course> courses = new ArrayList<Course>();
	
	transient List<UserChangeListener> ucl = new ArrayList<UserChangeListener>();
	
	public User() {
		
	}
	
	public void save() {
		FileOutputStream fos;
		try {
			fos = Session.getContext().openFileOutput(saveFileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
		 	Log.d(Constants.LOG_USER, "Successfully saved user to disk");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static User load() {
		FileInputStream fis;
		try {
			fis = Session.getContext().openFileInput(saveFileName);

			ObjectInputStream is = new ObjectInputStream(fis);
			User user = (User) is.readObject();
			is.close();
			Log.d(Constants.LOG_USER, "User restored successfully");
			return user;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Failed to find the user
		return null;
	}
	
	public void addListener(UserChangeListener listener) {
		if(ucl == null)
			ucl = new ArrayList<UserChangeListener>();
		ucl.add(listener);
	}
	
	public void removeListener(UserChangeListener listener) {
		if(ucl == null)
			ucl = new ArrayList<UserChangeListener>();
		ucl.remove(listener);
	}

	// 99.9% of the time you can just ignore this
	public int describeContents() {
		return 0;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProfilePictureURL() {
		return profilePictureURL;
	}

	public void setProfilePictureURL(String profilePictureURL) {
		this.profilePictureURL = profilePictureURL;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
		boolean notify = false;
		if(courses.size() > 0)
			notify = true;
		
		for(UserChangeListener listener : ucl) {
			listener.courseChange(notify);
		}
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public Course getCourse(int id) {
		for (Course course : courses) {
			if (course.getId() == id) {
				return course;
			}
		}
		Log.d(Constants.LOG_COURSE, "Tried to get course with ID: " + id + ", but it doesn't exist in the list");
		return null; // course not found.
	}
}
