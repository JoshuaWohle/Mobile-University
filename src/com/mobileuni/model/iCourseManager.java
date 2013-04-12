package com.mobileuni.model;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.mobileuni.listeners.iCourseManagerListener;
import com.mobileuni.other.ContentType;
import com.mobileuni.other.ModuleType;


public interface iCourseManager {
	
	List<iCourseManagerListener> icml = new ArrayList<iCourseManagerListener>();

	public void login(User user);
	public void setCourses(JSONObject jsonObject, boolean autoDownload);
	public void setCourseDetails(JSONObject jsonObject, int courseId);
	public void setMainInfo(JSONObject jsonObject);
	public ArrayList<ContentItem> getCourseContentTypeByCourse(Course course, ContentType type);
	
	/**
	 * @param course
	 * @param fileName
	 * @return null if the file has to be downloaded, FILE if the file already exists
	 */
	public File downloadDocument(Course course, String fileName);
	public void syncAllDocuments();
	/**
	 * If a course is passed, it returns all of the modules of the course, if not, it simply returns ALL modules
	 * @param course
	 * @return
	 */
	public ArrayList<Module> getModules(Course course, ModuleType type);
	
	public String getToken();
	public void setToken(String token);
	
	public void addListener(iCourseManagerListener listener);
	public void removeListener(iCourseManagerListener listener);

}
