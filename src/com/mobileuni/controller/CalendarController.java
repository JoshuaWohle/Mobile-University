package com.mobileuni.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mobileuni.R;
import com.mobileuni.helpers.AppStatus;
import com.mobileuni.helpers.MenuHelper;
import com.mobileuni.model.Module;
import com.mobileuni.model.Session;
import com.mobileuni.other.Constants;
import com.mobileuni.other.ModuleType;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CalendarController extends Activity implements OnClickListener {
	
	public static Calendar selectedMonth = Calendar.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MenuHelper.setContentViewAndSlideMenu(this, R.layout.calendar_layout, R.string.menu_calendar);
		AppStatus.setChangeOrientation(this);
		drawCalendarView();
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
	
	private void drawCalendarView() {
		Calendar cal = (Calendar) selectedMonth.clone();
		TableLayout grid = (TableLayout) findViewById(R.id.calendar_view);
		grid.removeAllViews();
		int rows = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		int cols = 7;
		
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		
		SimpleDateFormat day = new SimpleDateFormat("EEE");
		SimpleDateFormat month = new SimpleDateFormat("MMMMM yyyy");
		
		TableRow monthRow = new TableRow(this);
		monthRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		LinearLayout layout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.calendar_month_item, null);
		TextView mv = (TextView) layout.findViewById(R.id.calendar_month_title);
		
		((TextView) layout.findViewById(R.id.calendar_month_backward)).setOnClickListener(this);
		((TextView) layout.findViewById(R.id.calendar_month_forward)).setOnClickListener(this);
		
		mv.setText(month.format(cal.getTime()));
		grid.addView(layout);
		
		Calendar temp = (Calendar) cal.clone();
		TableRow dayRow = new TableRow(this);
		dayRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		for(int i = 1; i <= temp.getActualMaximum(Calendar.DAY_OF_WEEK); i++) {
			TextView tv = (TextView) this.getLayoutInflater().inflate(R.layout.calendar_day_item, null);
			temp.set(Calendar.DAY_OF_WEEK, i+1%7);
			tv.setText(day.format(temp.getTime()));
			dayRow.addView(tv);
		}
		
		grid.addView(dayRow);
		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		int date = 1;
		
		for(int i = 0; i < rows; i++) {
			TableRow row = new TableRow(this);
			row.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			for(int j = 1; j <= cols; j++) {
				TextView tv = (TextView) this.getLayoutInflater().inflate(R.layout.calendar_date_item, null);
				
				// If the day is after the start and before the finish, paint date
				// -1 is because the week starts on Sunday for Java :)
				Log.d(Constants.LOG_CALENDAR, Integer.toString(cal.get(Calendar.DAY_OF_WEEK)-1));
				if(!(j < cal.get(Calendar.DAY_OF_WEEK)-1 && i < 1) &&
						date <= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					tv.setText(Integer.toString(date));
					date++;
				}
				row.addView(tv);
			}
			grid.addView(row);
		}
	}

	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.calendar_month_backward:
			selectedMonth.add(Calendar.MONTH, -1);
			break;
		case R.id.calendar_month_forward:
			selectedMonth.add(Calendar.MONTH, 1);
			break;
		default:
			break;
		}
		drawCalendarView();
		
	}
}
