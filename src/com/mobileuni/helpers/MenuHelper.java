package com.mobileuni.helpers;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileuni.R;
import com.mobileuni.controller.CourseAssignmentController;
import com.mobileuni.controller.CourseDocumentController;
import com.mobileuni.controller.CourseForumController;
import com.mobileuni.controller.CourseGradeController;
import com.mobileuni.controller.CourseNoteController;
import com.mobileuni.listeners.MenuListener;
import com.mobileuni.model.CourseSectionItem;
import com.slidingmenu.lib.SlidingMenu;

public class MenuHelper {

	public static ArrayList<CourseSectionItem> courseSectionList;
	private static ArrayList<SlidingMenu> history = new ArrayList<SlidingMenu>();

	public static void setContentViewAndSlideMenu(Activity a, int resource, int titleResource) {
		
		// Include menu bar and then set the appropriate view
		a.setContentView(R.layout.main_layout);
		LinearLayout main = (LinearLayout) a.findViewById(R.id.main_layout);
		TextView titleView = (TextView) a.findViewById(R.id.main_title);
		titleView.setText(a.getResources().getString(titleResource));
		
		View child = (View) a.getLayoutInflater().inflate(resource, null);
		main.addView(child);
		MenuListener ml = new MenuListener(a);
		
		SlidingMenu menu = new SlidingMenu(a);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		if(AppStatus.isTablet(a))
			menu.setBehindWidthRes(R.dimen.menu_size_tablet);
		else
			menu.setBehindOffsetRes(R.dimen.menu_offset_right);
		menu.attachToActivity(a, SlidingMenu.SLIDING_CONTENT);
		addSlidingMenu(menu);
		
		((Button) a.findViewById(R.id.main_menu_button)).setOnClickListener(ml);
		
		LinearLayout menuView = (LinearLayout) a.getLayoutInflater().inflate(
				R.layout.menu, null);
		((LinearLayout) menuView.findViewById(R.id.menu_select_course))
				.setOnClickListener(ml);
		((LinearLayout) menuView.findViewById(R.id.menu_upload_document))
				.setOnClickListener(ml);
		((LinearLayout) menuView.findViewById(R.id.menu_calendar))
			.setOnClickListener(ml);
		((LinearLayout) menuView.findViewById(R.id.menu_settings))
				.setOnClickListener(ml);
		((LinearLayout) menuView.findViewById(R.id.menu_selected_course))
				.setOnClickListener(ml);
		((LinearLayout) menuView.findViewById(R.id.menu_logout))
				.setOnClickListener(ml);
		menu.setMenu(menuView);

		if (courseSectionList == null) {
			courseSectionList = new ArrayList<CourseSectionItem>();
			courseSectionList
					.add(new CourseSectionItem(
							a.getResources().getString(
									R.string.course_section_document_title),
							a.getResources()
									.getString(
											R.string.course_section_document_description),
							CourseDocumentController.class));
			courseSectionList
					.add(new CourseSectionItem(
							a.getResources().getString(
									R.string.course_section_assignments_title),
							a.getResources()
									.getString(
											R.string.course_section_assignments_description),
							CourseAssignmentController.class));
			courseSectionList
					.add(new CourseSectionItem(a.getResources().getString(
							R.string.course_section_grades_title), a
							.getResources().getString(
									R.string.course_section_grades_description),
							CourseGradeController.class));
			courseSectionList
					.add(new CourseSectionItem(a.getResources().getString(
							R.string.course_section_forums_title), a
							.getResources().getString(
									R.string.course_section_forums_description),
							CourseForumController.class));
			courseSectionList
					.add(new CourseSectionItem(a.getResources().getString(
							R.string.course_section_notes_title), a
							.getResources().getString(
									R.string.course_section_notes_description),
							CourseNoteController.class));
		}

	}
	
	public static void openMenu(Activity a) {
		for(SlidingMenu menu : history) {
			if(a == menu.getContext())
				menu.showMenu();
		}
	}
	
	private static void addSlidingMenu(SlidingMenu menu) {
		for(SlidingMenu hist : history) {
			if(hist.getContext() == menu.getContext())
				return;
		}
		history.add(menu);
	}
	
	public static boolean isCourseSection(Class checkedClass) {
		for(CourseSectionItem section : courseSectionList) 
			if(section.targetActivity == checkedClass)
				return true;
		
		return false;
	}
}
