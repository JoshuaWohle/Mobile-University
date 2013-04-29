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

import java.io.Serializable;

import org.json.JSONObject;

/**
 * A class to represent a content item. A content item can be any actual item such as a document, a grade, etc.
 * @author Joshua Wöhle
 */
public class ContentItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String type = "";
    private String filename = "";
    private String filepath = "";
	private int filesize = 0;
	private String fileurl = "";
    private String content = "";
    private long timecreated = 0;
    private long timemodified = 0;
    private int sortorder = 0;
    private int userid = 0;
    private String author = "";
    private String license = "";
	
	public ContentItem() {
	}
	
    public void populateContent(JSONObject jsonObject) {
     
		if (jsonObject != null) {    			
	        String type = jsonObject.optString("type"); 
	        if (type != null && type.trim().length() > 0)
	        	this.setType(type);
	        String filename = jsonObject.optString("filename"); 
	        if (filename != null && filename.trim().length() > 0)
	        	this.setFileName(filename);
	        String filepath = jsonObject.optString("filepath"); 
	        if (filepath != null && filepath.trim().length() > 0)
	        	this.setFilePath(filepath);
	        String filesize = jsonObject.optString("filesize"); 
	        if (filesize != null && filesize.trim().length() > 0)
	        	this.setFileSize(Integer.valueOf(filesize));
	        String fileurl = jsonObject.optString("fileurl"); 
	        if (fileurl != null && fileurl.trim().length() > 0)
	        	this.setFileUrl(fileurl);
	        String content = jsonObject.optString("content"); 
	        if (content != null && content.trim().length() > 0) 
	        	this.setContent(content);		        
	        String timecreated = jsonObject.optString("timecreated");  
	        if (timecreated != null && timecreated.trim().length() > 0)
	        	this.setTimeCreated(Long.valueOf(timecreated)*1000);
	        String timemodified = jsonObject.optString("timemodified");  
	        if (timemodified != null && timemodified.trim().length() > 0)
	        	this.setTimeModified(Long.valueOf(timemodified)*1000);		        
	        String sortorder = jsonObject.optString("sortorder");  
	        if (sortorder != null && sortorder.trim().length() > 0)
	        	this.setSortOrder(Integer.valueOf(sortorder));
	        String userid = jsonObject.optString("userid");  
	        if (userid != null && userid.trim().length() > 0)
	        	this.setUserId(Integer.valueOf(userid));
	        String author = jsonObject.optString("author");  
	        if (author != null && author.trim().length() > 0)
	        	this.setAuthor(author);
	        String license = jsonObject.optString("license");  
	        if (license != null && license.trim().length() > 0)
	        	this.setLicense(license);
		}
    }
	
	public void setType(String type) {
       this.type = type;
    }

    public String getType() {
       return type;
    }
    	
	public void setFileName(String filename) {
       this.filename = filename;
    }

    public String getFileName() {
       return filename;
    }
    
	public void setFilePath(String filepath) {
       this.filepath = filepath;
    }

    public String getFilePath() {
       return filepath;
    }
	
	public void setFileSize(int filesize) {
       this.filesize = filesize;
    }

    public int getFileSize() {
       return filesize;
    }

	public void setFileUrl(String fileurl) {
       this.fileurl = fileurl;
    }

    public String getFileUrl() {
       return fileurl;
    }
    
	public void setContent(String content) {
       this.content = content;
    }

    public String getContent() {
       return content;
    }
    
	public void setTimeCreated(long timecreated) {
       this.timecreated = timecreated;
    }

    public long getTimeCreated() {
       return timecreated;
    }
    
	public void setTimeModified(long timemodified) {
       this.timemodified = timemodified;
    }

    public long getTimeModified() {
       return timemodified;
    }
    
	public void setSortOrder(int sortorder) {
       this.sortorder = sortorder;
    }

    public int getSortOrder() {
       return sortorder;
    }
    
	public void setUserId(int userid) {
       this.userid = userid;
    }

    public int getUserId() {
       return userid;
    }
	    
	public void setAuthor(String author) {
       this.author = author;
    }

    public String getAuthor() {
       return author;
    }
    
	public void setLicense(String license) {
       this.license = license;
    }

    public String getLicense() {
       return license;
    }
}
