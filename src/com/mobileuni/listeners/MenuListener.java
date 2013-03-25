package com.mobileuni.listeners;

import com.mobileuni.CourseDetail;
import com.mobileuni.FileUpload;
import com.mobileuni.R;
import com.mobileuni.Setting;
import com.mobileuni.controller.CourseSelectController;

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
		case R.id.coursework_home_view:
			intent = new Intent(a, CourseDetail.class);
			a.startActivity(intent);
			break;
		case R.id.select_course:
			intent = new Intent(a, CourseSelectController.class);
			a.startActivity(intent);
			break;
		case R.id.settings_view:
			intent = new Intent(a, Setting.class);
			a.startActivity(intent);
			break;
		case R.id.upload_view:
			intent = new Intent(a, FileUpload.class);
			a.startActivity(intent);
			break;
		default:

		}
		
	}

}
