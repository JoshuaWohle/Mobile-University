package com.mobileuni.helpers;

import com.mobileuni.model.Course;
import com.mobileuni.model.CourseSchedule;
import com.mobileuni.model.User;

public class CalendarSyncHelper {
	
	public static void synchronizeCourseSchedules(User user) {
		for(Course course : user.getCourses()) {
			for(CourseSchedule schedule : course.getDates()) {
				//TODO sync courses
			}
		}
	}
	
	public static void synchronizeDeadlines(User user) {
		//TODO implement deadline synchronization
	}

}
