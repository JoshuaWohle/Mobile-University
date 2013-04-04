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

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mobileuni.listeners.CourseChangeListener;

public class Course implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;	
	transient private ArrayList<CourseChangeListener> cls = new ArrayList<CourseChangeListener>();
	private String shortname;	
	private String fullname;	
    private int enrolledusercount;	
    private String idnumber;	
    private int visible;	
    private ArrayList<CourseContent> coursecontents = new ArrayList<CourseContent>();	
    private ArrayList<String> absoluteFilePaths = new ArrayList<String>();

	public Course() {
		
	}
	
	public void addListener(CourseChangeListener listener) {
		if(cls == null)
			cls = new ArrayList<CourseChangeListener>();
		cls.add(listener);
	}
	
	public void removeListener(CourseChangeListener listener) {
		if(cls == null)
			cls = new ArrayList<CourseChangeListener>();
		cls.remove(listener);
	}
	public void setId(int id) {
       this.id = id;
    }

    public int getId() {
       return id;
    }
    
	public void setShortName(String shortname) {
       this.shortname = shortname;
    }

    public String getShortName() {
       return shortname;
    }
    
	public void setFullname(String fullname) {
       this.fullname = fullname;
    }

    public String getFullname() {
       return fullname;
    }
    
	public void setEnrolledUserCount(int enrolledusercount) {
       this.enrolledusercount = enrolledusercount;
    }

    public int getEnrolledUserCount() {
       return enrolledusercount;
    }
    
	public void setIdNumber(String idnumber) {
       this.idnumber = idnumber;
    }

    public String getIdNumber() {
       return idnumber;
    }
    
	public void setVisible(int visible) {
       this.visible = visible;
    }

    public int getVisible() {
       return visible;
    }
    
	public void setCourseContent(ArrayList<CourseContent> coursecontents) {
       this.coursecontents = coursecontents;
       
       for(CourseChangeListener listener : cls) {
    	   listener.courseContentsChanged();
       }
    }

    public ArrayList<CourseContent> getCourseContent() {
       return coursecontents;
    }

    public void populateCourse(JSONObject jsonObject) {    
    	    	 
    	try {  
    		if (jsonObject != null) {
    			
	    		String id = jsonObject.getString("id"); 
	    		this.setId(Integer.valueOf(id));
		        String shortname = jsonObject.optString("shortname"); 
		        if (shortname != null && shortname.trim().length() > 0)
		        	this.setShortName(shortname);
		        String fullname = jsonObject.optString("fullname");  
		        if (fullname != null && fullname.trim().length() > 0)
		        	this.setFullname(fullname);
		        String enrolledusercount = jsonObject.optString("enrolledusercount");  
		        if (enrolledusercount != null && enrolledusercount.trim().length() > 0)
		        	this.setEnrolledUserCount(Integer.valueOf(enrolledusercount));
		        String idnumber = jsonObject.optString("idnumber"); 
		        if (idnumber != null && idnumber.trim().length() > 0) 
		        	this.setIdNumber(idnumber);
		        String visible = jsonObject.optString("visible");  
		        if (visible != null && visible.trim().length() > 0)
		        	this.setVisible(Integer.valueOf(visible));
		        
    		}
    	} catch (JSONException e) { 
    	    e.printStackTrace(); 
    	}
    }
    
    /* everything below here is for implementing Parcelable */ 
	 
    // 99.9% of the time you can just ignore this 
    public int describeContents() { 
        return 0; 
    }
	
	public ArrayList<String> getAbsoluteFilePaths() {
		return absoluteFilePaths;
	}

	public void setAbsoluteFilePaths(ArrayList<String> absoluteFilePaths) {
		this.absoluteFilePaths = absoluteFilePaths;
	}

	public void addAbsoluteFilePath(String fielPath) {
		if(!this.absoluteFilePaths.contains(fielPath)) {
			this.absoluteFilePaths.add(fielPath);

			for(CourseChangeListener listener : cls)
				listener.fileChanged(fielPath);
		}
	}
	
	public void removeAbsoluteFilePath(String fielPath) {
		if(this.absoluteFilePaths.contains(fielPath))
			this.absoluteFilePaths.remove(fielPath);
	}
    
}
