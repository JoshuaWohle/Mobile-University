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

package com.mobileuni.controller;

import com.mobileuni.R;
import com.mobileuni.helpers.CalendarSyncHelper;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.model.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Controls the user settings in a basic way
 * @author Joshua Wöhle
 */
public class SettingsController extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.settings, R.string.menu_settings);
		
		ToggleButton autoDownloadFiles = (ToggleButton) findViewById(R.id.settings_auto_download_files);
		ToggleButton autoDownloadContent = (ToggleButton) findViewById(R.id.settings_auto_download_content);
		ToggleButton syncDeadlines = (ToggleButton) findViewById(R.id.settings_sync_deadlines);
		autoDownloadFiles.setChecked(Settings.isAutoDownloadFiles());
		autoDownloadContent.setChecked(Settings.isAutoDownloadContent());
		syncDeadlines.setChecked(Settings.isSyncDeadlines());
	}
	
	public void onToggleClicked(View v) {
	    
	    switch(v.getId()) {
	    	case R.id.settings_auto_download_files:
	    		Settings.setAutoDownloadFiles(((ToggleButton) v).isChecked());
	    		break;
	    	case R.id.settings_sync_deadlines:
	    		Settings.setSyncDeadlines(((ToggleButton) v).isChecked());
	    		if(Settings.isSyncDeadlines())
	    			CalendarSyncHelper.synchronizeDeadlines(this, true);
	    		break;
	    	case R.id.settings_sync_courses:
	    		Settings.setSyncCourseSchedules(((ToggleButton) v).isChecked());
	    		if(Settings.isSyncCourseSchedules())
	    			CalendarSyncHelper.synchronizeCourseSchedules(this, true);
	    	case R.id.settings_auto_download_content:
	    		Settings.setAutoDownloadContent(((ToggleButton) v).isChecked());
	    		break;
	    	default:
	    		break;
	    }
	}

}
