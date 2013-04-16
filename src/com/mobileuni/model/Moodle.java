package com.mobileuni.model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.mobileuni.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.mobileuni.config.Config;
import com.mobileuni.helpers.asynctasks.DownloadFileTask;
import com.mobileuni.helpers.asynctasks.TokenRequestTask;
import com.mobileuni.helpers.asynctasks.WebServiceResponseTask;
import com.mobileuni.listeners.iCourseManagerListener;
import com.mobileuni.other.Constants;
import com.mobileuni.other.ContentType;
import com.mobileuni.other.ModuleType;
import com.mobileuni.other.WebServiceFunction;

public class Moodle implements iCourseManager {
	
	private static final HashMap<ContentType, String> docTypes = new HashMap<ContentType, String>(); 
	private static final HashMap<ModuleType, String> modTypes = new HashMap<ModuleType, String>();
	static {
		docTypes.put(ContentType.DOCUMENT, "file");
		modTypes.put(ModuleType.ASSIGNMENT, "assign");
		modTypes.put(ModuleType.GRADE, "assign");
		modTypes.put(ModuleType.FORUM, "forum");
	}

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

	public void setCourses(JSONObject jsonObject, boolean autoDownload) {
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
				Toast.makeText(Session.getContext(), Session.getContext().getResources().getString(R.string.error_url_encoding), Toast.LENGTH_SHORT).show();
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
				// Make sure offline file paths keep persisted
				if (null != Session.getUser().getCourse(course.getId()))
					course.setAbsoluteFilePaths(Session.getUser()
							.getCourse(course.getId()).getAbsoluteFilePaths());
				courseArray.add(course);
			}
		} catch (JSONException e) {
			Toast.makeText(Session.getContext(), Session.getContext().getResources().getString(R.string.error_jason_encoding), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		if (autoDownload)
			for (Course course : courseArray)
				setCourseDetails(null, course.getId());

		Session.getUser().setCourses(courseArray);
		Session.getUser().save();
	}

	public void setCourseDetails(JSONObject jsonObject, int courseId) {

		if (null == jsonObject) {
			try {
				String urlParameters = "courseid="
						+ URLEncoder.encode(String.valueOf(courseId), "UTF-8");
				new WebServiceResponseTask().execute(
						WebServiceFunction.core_course_get_contents,
						urlParameters, R.raw.contentxsl, courseId);
			} catch (UnsupportedEncodingException e) {
				Toast.makeText(Session.getContext(), Session.getContext().getResources().getString(R.string.error_url_encoding), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			return;
		}

		ArrayList<CourseContents> courseContentsArray = new ArrayList<CourseContents>();
		Course course = Session.getUser().getCourse(courseId);
		try {
			JSONArray courseContents = jsonObject
					.getJSONArray("coursecontents");

			// looping through All Course Content
			for (int i = 0; i < courseContents.length(); i++) {
				JSONObject c = courseContents.getJSONObject(i);
				CourseContents courseContent = new CourseContents();
				courseContent.populateCourseContent(c);
				courseContentsArray.add(courseContent);
			}

			course.setCourseContent(courseContentsArray);
			if (Settings.isAutoDownloadFiles()) {
				for (CourseContents contents : courseContentsArray) {
					for (Module module : contents.getModules()) {
						for (ContentItem item : module.getContents()) {
							if (item.getType().equalsIgnoreCase("file")) {
								downloadDocument(course, item.getFileName());
							}
						}
					}
				}
			}

			Session.getUser().save();

		} catch (JSONException e) {
			Toast.makeText(Session.getContext(), Session.getContext().getResources().getString(R.string.error_jason_encoding), Toast.LENGTH_SHORT).show();
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

	public File downloadDocument(Course course, String fileName) {
		File file = null;
		for (CourseContents contents : course.getCourseContent()) {
			for (Module module : contents.getModules()) {
				for (ContentItem item : module.getContents()) {
					if (item.getFileName().equals(fileName)) {
						file = new File(Constants.FILE_STORAGE_FOLDER_NAME
								+ course.getFullname() + "/"
								+ item.getFileName());
						if (!file.exists()
								|| (file.lastModified() < item
										.getTimeModified())) {
							new DownloadFileTask().execute(item.getFileUrl(),
									fileName, course.getFullname() + "/",
									course);
							Log.d(Constants.LOG_DOCUMENTS,
									"Downloading new file: " + fileName);
							return null;
						}
					}
				}
			}
		}
		return file;
	}

	public ArrayList<Module> getModules(Course course, ModuleType type) {
		ArrayList<Course> courses = new ArrayList<Course>();
		if(course == null)
			courses = Session.getUser().getCourses();
		else
			courses.add(course);
		
		ArrayList<Module> items = new ArrayList<Module>();
		for (Course c : courses) {
			for (CourseContents content : c.getCourseContent()) {
				for (Module module : content.getModules()) {
					if ((module.getModName().startsWith(modTypes.get(type)))
							&& module.getVisible() == 1) {
						Assignment assignment = new Assignment();
						assignment.setName(module.getName());
						assignment.setUrl(module.getUrl());
						assignment.setDescription(module.getDescription());
						items.add(assignment);
					}
				}
			}
		}
		return items;
	}

	public ArrayList<ContentItem> getCourseContentTypeByCourse(
			Course course, ContentType type) {
		ArrayList<ContentItem> items = new ArrayList<ContentItem>();
		
		switch(type) {
		case DOCUMENT:
			for (CourseContents content : course.getCourseContent()) {
				for (Module module : content.getModules()) {
					if ((module.getModName().equalsIgnoreCase("resource"))
							&& module.getVisible() == 1) {
						for (ContentItem item : module.getContents()) {
							if (item.getType().equalsIgnoreCase("file"))
								items.add(item);
						}
					}
				}
			}
			break;
		default:
			break;
		}
		return items;
	}

	public void syncAllDocuments() {
		// Moodle integrates this through getting course details
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
			Log.d(Constants.LOG_AUTHENTICATION,
					"User token has been set to null");
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
