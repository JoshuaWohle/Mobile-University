package com.mobileuni.model;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.mobileuni.listeners.iCourseManagerListener;


public interface iCourseManager {
	
	List<iCourseManagerListener> icml = new ArrayList<iCourseManagerListener>();

	public void login(User user);
	public void setCourses(JSONObject jsonObject, boolean autoDownload);
	public void setCourseDetails(JSONObject jsonObject, int courseId);
	public void setMainInfo(JSONObject jsonObject);
	public void downloadDocument(Course course, String fileName);
	public void syncAllDocuments();
	public ArrayList<Assignment> getAssignments();
	
	public String getToken();
	public void setToken(String token);
	
	public void addListener(iCourseManagerListener listener);
	public void removeListener(iCourseManagerListener listener);

}
