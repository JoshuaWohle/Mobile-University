package com.mobileuni.controller;

import com.mobileuni.R;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.model.Module;
import com.mobileuni.model.Session;
import com.mobileuni.other.ModuleType;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarController extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.calendar_layout, R.string.menu_calendar);
		fillUpcoming();
	}
	
	private void fillUpcoming() {
		LinearLayout main = (LinearLayout) findViewById(R.id.calendar_upcoming_items_list);
		for(Module upcoming : Session.getCourseManager().getModules(null, ModuleType.ASSIGNMENT)) {
			LinearLayout child = (LinearLayout) this.getLayoutInflater().inflate(R.layout.list_item_single_line, null);
			TextView content = (TextView) child.findViewById(R.id.item_content);
			content.setText(upcoming.getName());
			main.addView(child);
		}
	}
}
