/**
 *  an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
 *  Copyright (C) 2012  Justin Stevanz, Andrew Kelson and Matthias Peitsch
 *
 *	Contact the.omega.online@gmail.com for further information.
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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mobileuni.listeners.UserChangeListener;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String profilePictureURL;
	private ArrayList<Course> courses = new ArrayList<Course>();
	
	List<UserChangeListener> ucl = new ArrayList<UserChangeListener>();
	
	public User() {

	}
	
	public void getDataFromJSON(JSONObject jsonObject) {
		//TODO handle more than just moodle related strings
		 	try {
				this.setUsername(jsonObject.getString("username"));
			 	this.setFirstName(jsonObject.getString("firstname"));
			 	this.setLastName(jsonObject.getString("lastname"));
			 	this.setProfilePictureURL(jsonObject.getString("userpictureurl"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	// write your object's data to the passed-in Parcel
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(password);
		dest.writeTypedList(courses);
	}

	private User(Parcel in) {
		this.username = in.readString();
		this.password = in.readString();
		in.readTypedList(this.courses, Course.CREATOR);
	}
	
	public void addListener(UserChangeListener listener) {
		ucl.add(listener);
	}
	
	public void removeListener(UserChangeListener listener) {
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
		Log.d("Course", "Tried to get course with ID: " + id + ", but it doesn't exist in the list");
		return null; // course not found.
	}
}
