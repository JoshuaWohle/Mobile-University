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

package com.mobileuni.helpers;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.mobileuni.model.ContentItem;
import com.mobileuni.model.Course;
import com.mobileuni.model.CourseContents;
import com.mobileuni.model.Module;
import com.mobileuni.other.Constants;

import com.mobileuni.R;

import android.content.Context;
import android.util.Log;

public class CourseContentsListHelper {

	private static CourseContentsListHelper instance = new CourseContentsListHelper(); 
	static Context context;
	
	public static CourseContentsListHelper getInstance(Context ctx) { 
        context = ctx; 
        return instance; 
    }
	
	public SectionListItem[] populateCourseForums(ArrayList<CourseContents> coursecontent) {
		
		SectionListItem[] assignmentArray = null;
		int assignCount = 0;
		if (coursecontent.size() > 0) {
			for (CourseContents content : coursecontent) {
				for (Module module : content.getModules()) {
					if (module.getModName().equalsIgnoreCase("forum")) {
						assignCount++;
					}
				}
			}
			assignmentArray = new SectionListItem[assignCount];
			
			int elementCount = 0;
			for (CourseContents content : coursecontent) {
				String sectionName = content.getName();
				if (content.getVisible() == 1) {					
					for (Module module : content.getModules()) {
						if (module.getModName().equalsIgnoreCase("forum") && module.getVisible() == 1) {
							HashMap<String, String> map = new HashMap<String, String>();

							map.put("id", String.valueOf(elementCount));
				    		map.put("header", module.getName());
				    		map.put("description", module.getDescription());
				    		map.put("availability", "");
				    		map.put("thumb", String.valueOf(R.drawable.forum_icon));
				    		map.put("url", module.getUrl());
				    		map.put("traffic", String.valueOf(R.drawable.light_emtpy_icon));
				    		//map.put("modifieddate", String.valueOf(module.g.getTimeModified()));
				    		
				    		assignmentArray[elementCount] = new SectionListItem(map, sectionName);
				    		elementCount++;
						}
					}
				}
			}
		}
		
		return assignmentArray;		
	}
}
