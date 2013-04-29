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
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class representing all contents related to a course
 * @author Joshua Wöhle
 */
public class CourseContents implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;	
    private String name;	
    private int visible;	
	private String summary;	
    private ArrayList<Module> modules = new ArrayList<Module>();	
	
	public CourseContents() {

	}
	
	public void setId(int id) {
       this.id = id;
    }

    public int getId() {
       return id;
    }
        
	public void setName(String name) {
       this.name = name;
    }

    public String getName() {
       return name;
    }
	    
	public void setVisible(int visible) {
       this.visible = visible;
    }

    public int getVisible() {
       return visible;
    }
        
	public void setSummary(String summary) {
	   this.summary = summary;
	}
	
	public String getSummary() {
	   return summary;
	}
    
	public void setModules(ArrayList<Module> modules) {
       this.modules = modules;
    }

    public ArrayList<Module> getModules() {
       return modules;
    }

    /**
     * populate the object based on a jsonobject. Extracts the necessary information. 
     * @param jsonObject
     */
    public void populateCourseContent(JSONObject jsonObject) {
    //TODO this should be moved to the iCM
    	try {  
    		if (jsonObject != null) {
    			
	    		String id = jsonObject.getString("id"); 
	    		this.setId(Integer.valueOf(id));
		        String name = jsonObject.optString("name");  
		        if (name != null && name.trim().length() > 0)
		        	this.setName(name);
		        String visible = jsonObject.optString("visible");  
		        if (visible != null && visible.trim().length() > 0)
		        	this.setVisible(Integer.valueOf(visible));
		        String summary = jsonObject.optString("summary");  
		        if (summary != null && summary.trim().length() > 0)
		        	this.setSummary(summary);
		        
		        JSONArray modules = jsonObject.getJSONArray("modules");
		        ArrayList<Module> modulesArray = new ArrayList<Module>();
	    	    // looping through all Modules 
	    	    for(int i = 0; i < modules.length(); i++){ 
	    	        JSONObject c = modules.getJSONObject(i); 
	    	        Module module = new Module();
	    	        module.populateModule(c);
	    	        modulesArray.add(module);
	    	    } 	
	    	    
	    	    if (modulesArray.size() > 0) {
	    	    	this.setModules(modulesArray);
	    	    }
    		}
    	} catch (JSONException e) { 
    	    e.printStackTrace(); 
    	}
    }
}
