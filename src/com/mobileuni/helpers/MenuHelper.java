package com.mobileuni.helpers;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

/**
 * A simple helper class that centralises the use of the 2 menus : course menu & general menu
 * @author Joshua Wöhle
 */
public class MenuHelper {

	/**
	 * A list of all available sections in a course and their related activities
	 */
	public static ArrayList<CourseSectionItem> courseSectionList;
	private static ArrayList<SlidingMenu> history = new ArrayList<SlidingMenu>();

	/**
	 * Takes the current activity context and re-arranges it to attach the sliding menu
	 * @param a - the activity responsible for treating the menu
	 * @param resource - the resource of the "main" screen to put alongside the menu
	 * @param titleResource - the title of the resource to be loaded (a resource identifier of it's string)
	 */
	public static void setContentViewAndSlideMenu(Activity a, int resource, int titleResource) {
		
		// Include menu bar and then set the appropriate view
		a.setContentView(R.layout.main_layout);
		LinearLayout main = (LinearLayout) a.findViewById(R.id.main_layout);
		TextView titleView = (TextView) a.findViewById(R.id.main_title);
		titleView.setText(a.getResources().getString(titleResource));
		
		// Put it all in a scrollview, to make sure we don't run out of space :)
		ScrollView sv = new ScrollView(a);
		View child = (View) a.getLayoutInflater().inflate(resource, null);
		sv.addView(child);
		main.addView(sv);
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
		((TextView) menuView.findViewById(R.id.menu_select_course))
				.setOnClickListener(ml);
		/*((TextView) menuView.findViewById(R.id.menu_upload_document))
				.setOnClickListener(ml);*/
		((TextView) menuView.findViewById(R.id.menu_calendar))
			.setOnClickListener(ml);
		((TextView) menuView.findViewById(R.id.menu_settings))
				.setOnClickListener(ml);
		((TextView) menuView.findViewById(R.id.menu_selected_course))
				.setOnClickListener(ml);
		((TextView) menuView.findViewById(R.id.menu_logout))
				.setOnClickListener(ml);
		
		//Set scroll view for menu
		ScrollView svMenu = new ScrollView(a);
		svMenu.addView(menuView);
		menu.setMenu(svMenu);

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
	
	/**
	 * @param checkedClass - the class to be checked
	 * @return true if the class if part of the course sections, false otherwise
	 * Checks if the class is really one of the course sections
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isCourseSection(Class checkedClass) {
		for(CourseSectionItem section : courseSectionList) 
			if(section.targetActivity == checkedClass)
				return true;
		
		return false;
	}
}
