package com.mobileuni.listeners;

import com.mobileuni.R;
import com.mobileuni.controller.CalendarController;
import com.mobileuni.controller.CourseDetailController;
import com.mobileuni.controller.CourseSelectController;
import com.mobileuni.controller.FileUploadController;
import com.mobileuni.controller.LoginController;
import com.mobileuni.controller.SettingsController;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.other.Constants;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuListener implements OnClickListener {
	
	Activity a;
	
	public MenuListener(Activity a) {
		this.a = a;
	}

	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
		case R.id.main_menu_button:
			MenuHelper.openMenu(a);
			break;
		case R.id.menu_selected_course:
			i = new Intent(a, CourseDetailController.class);
			break;
		case R.id.menu_select_course:
			i = new Intent(a, CourseSelectController.class);
			break;
		case R.id.menu_calendar:
			i = new Intent(a, CalendarController.class);
			break;
		case R.id.menu_upload_document:
			i = new Intent(a, FileUploadController.class);
			break;
		case R.id.menu_settings:
			i = new Intent(a, SettingsController.class);
			break;
		case R.id.menu_logout:
			i = new Intent(a, LoginController.class);
			break;
		default:
			Log.d(Constants.LOG_MENU, "Non catched click on " + Integer.toString(v.getId()));
			break;
		}
		
		if(i != null)
			a.startActivity(i);
	}

}
