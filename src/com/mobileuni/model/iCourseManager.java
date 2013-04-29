package com.mobileuni.model;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.mobileuni.listeners.iCourseManagerListener;
import com.mobileuni.other.ContentType;
import com.mobileuni.other.ModuleType;


/**
 * An interface representing what it means to be a course manager. Any course manager (such as moodle) implements this interface
 * in order to work with the application.
 * @author Joshua Wöhle
 */
public interface iCourseManager {
	
	List<iCourseManagerListener> icml = new ArrayList<iCourseManagerListener>();

	/**
	 * Logs the user into the system
	 * @param user - the user that has to log in
	 */
	public void login(User user);
	
	/**
	 * Obtains the user's courses from the system if null is passed for the json object, sets them on the user if an
	 * actual jsonobject is passed. Also, if autodownload is specified, it automatically downlaods all documents
	 * related to the user's courses
	 * @param jsonObject - contains the courses received from the system
	 * @param autoDownload - should all documents be downloaded automatically ?
	 */
	public void setCourses(JSONObject jsonObject, boolean autoDownload);
	
	/**
	 * If the jsonobject is null, it fetches the course details of a certain course. If the jsonobject is not null, it sets the
	 * course details correctly
	 * @param jsonObject - contains the course details of the course
	 * @param courseId - the id of the course the details belong to
	 */
	public void setCourseDetails(JSONObject jsonObject, int courseId);
	
	/**
	 * Sets the main information of the system such as name, etc.
	 * @param jsonObject - null if the information has to be pulled, a jsonobject representing the server response if not
	 */
	public void setMainInfo(JSONObject jsonObject);
	
	/**
	 * gets the content items of a certain type from a specific course. For example, this could get the assignments 
	 * of course number 23.
	 * @param course - the course to pull content from
	 * @param type - the content type that has to be pulled
	 * @return an arraylist of ContentItem objects
	 */
	public ArrayList<ContentItem> getCourseContentTypeByCourse(Course course, ContentType type);
	
	/**
	 * Downloads a document from a specific course
	 * @param course - the course to which the document belongs
	 * @param fileName - the name of the file to be downloaded
	 * @return null if the file has to be downloaded, FILE if the file already exists
	 */
	public File downloadDocument(Course course, String fileName);
	
	/**
	 * Synchronizes all documents with offline storage
	 */
	public void syncAllDocuments();
	
	/**
	 * If a course is passed, it returns all of the modules of the course, if not, it simply returns ALL modules
	 * @param course
	 * @return
	 */
	public ArrayList<Module> getModules(Course course, ModuleType type);
	
	/**
	 * gets the token needed to sign api requests
	 * @return a string representing the requested token
	 */
	public String getToken();
	
	/**
	 * Sets a token to sign future requests to the api
	 * @param token
	 */
	public void setToken(String token);
	
	/**
	 * adds a listener to the course manager
	 * @param listener - the listener to be added
	 */
	public void addListener(iCourseManagerListener listener);
	
	/**
	 * removes a listener from the course manager
	 * @param listener - the listener to be removed
	 */
	public void removeListener(iCourseManagerListener listener);

}
