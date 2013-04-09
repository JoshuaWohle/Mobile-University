package com.mobileuni.listeners;

import com.mobileuni.R;
import com.mobileuni.controller.CourseDetailController;
import com.mobileuni.controller.CourseSelectController;
import com.mobileuni.controller.FileUploadController;
import com.mobileuni.controller.SettingsController;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuListener implements OnClickListener {
	
	Activity a;
	
	public MenuListener(Activity a) {
		this.a = a;
	}

	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {
		case R.id.menu_selected_course:
			intent = new Intent(a, CourseDetailController.class);
			a.startActivity(intent);
			break;
		case R.id.menu_select_course:
			intent = new Intent(a, CourseSelectController.class);
			a.startActivity(intent);
			break;
		case R.id.menu_upload_document:
			intent = new Intent(a, FileUploadController.class);
			a.startActivity(intent);
			break;
		case R.id.menu_settings:
			intent = new Intent(a, SettingsController.class);
			a.startActivity(intent);
			break;
		default:

		}
		
	}

}
