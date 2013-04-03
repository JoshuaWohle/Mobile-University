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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Module implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;	
	private String url;
    private String name;
    private String description;	
    private int visible;	
	private String modicon;	
    private String modname;	
    private String modplural;	
    private int availablefrom;	
    private int availableuntil;	
    private int indent;	
    private ArrayList<Content> contents = new ArrayList<Content>();	
	
	public Module() {

	}

    public void populateModule(JSONObject jsonObject) {
    
    	    	 
    	try {  
    		if (jsonObject != null) {
    			
	    		String id = jsonObject.getString("id"); 
	    		this.setId(Integer.valueOf(id));
		        String url = jsonObject.optString("url"); 
		        if (url != null && url.trim().length() > 0)
		        	this.setUrl(url);
		        String name = jsonObject.optString("name");  
		        if (name != null && name.trim().length() > 0)
		        	this.setName(name);
		        String description = jsonObject.optString("description"); 
		        if (description != null && description.trim().length() > 0) 
		        	this.setDescription(description);
		        String visible = jsonObject.optString("visible");  
		        if (visible != null && visible.trim().length() > 0)
		        	this.setVisible(Integer.valueOf(visible));
		        String modicon = jsonObject.optString("modicon");  
		        if (modicon != null && modicon.trim().length() > 0)
		        	this.setModIcon(modicon);
		        String modname = jsonObject.optString("modname"); 
		        if (modname != null && modname.trim().length() > 0)
		        	this.setModName(modname);
		        String modplural = jsonObject.optString("modplural");  
		        if (modplural != null && modplural.trim().length() > 0)
		        	this.setModPlural(modplural);	
		        String availablefrom = jsonObject.optString("availablefrom");  
		        if (availablefrom != null && availablefrom.trim().length() > 0)
		        	this.setAvailableFrom(Integer.valueOf(availablefrom));
		        String availableuntil = jsonObject.optString("availableuntil");  
		        if (availableuntil != null && availableuntil.trim().length() > 0)
		        	this.setAvailableUntil(Integer.valueOf(availableuntil));
		        String indent = jsonObject.optString("indent");  
		        if (indent != null && indent.trim().length() > 0)
		        	this.setIndent(Integer.valueOf(indent));
		        
		        JSONArray contents = jsonObject.getJSONArray("contents");
		        ArrayList<Content> contentsArray = new ArrayList<Content>();
	    	    // looping through all Contents 
	    	    for(int i = 0; i < contents.length(); i++){ 
	    	        JSONObject c = contents.getJSONObject(i); 
	    	        Content content = new Content();
	    	        content.populateContent(c);
	    	        //Toast.makeText(context.getApplicationContext(), course.getShortName(), Toast.LENGTH_LONG).show();
	    	        // Storing each json item in variable 
	    	        contentsArray.add(content);
	    	    } 	
	    	    
	    	    if (contentsArray.size() > 0) {
	    	    	this.setContents(contentsArray);
	    	    }
    		}
    	} catch (JSONException e) { 
    	    e.printStackTrace(); 
    	}
    }
    
	public void setId(int id) {
       this.id = id;
    }

    public int getId() {
       return id;
    }

	public void setUrl(String url) {
       this.url = url;
    }

    public String getUrl() {
       return url;
    }
        	
	public void setName(String name) {
       this.name = name;
    }

    public String getName() {
       return name;
    }
    
	public void setDescription(String description) {
       this.description = description;
    }

    public String getDescription() {
       return description;
    }
	    
	public void setVisible(int visible) {
       this.visible = visible;
    }

    public int getVisible() {
       return visible;
    }
        
	public void setModIcon(String modicon) {
	   this.modicon = modicon;
	}
	
	public String getModIcon() {
	   return modicon;
	}
    
	public void setModName(String modname) {
       this.modname = modname;
    }

    public String getModName() {
       return modname;
    }
    
	public void setModPlural(String modplural) {
       this.modplural = modplural;
    }

    public String getModPlural() {
       return modplural;
    }
    
	public void setAvailableFrom(int availablefrom) {
       this.availablefrom = availablefrom;
    }

    public int getAvailableFrom() {
       return availablefrom;
    }
    
	public void setAvailableUntil(int availableuntil) {
       this.availableuntil = availableuntil;
    }

    public int getAvailableUntil() {
       return availableuntil;
    }
    
	public void setIndent(int indent) {
       this.indent = indent;
    }

    public int getIndent() {
       return indent;
    }
    
	public void setContents(ArrayList<Content> contents) {
       this.contents = contents;
    }

    public ArrayList<Content> getContents() {
       return contents;
    }
}
