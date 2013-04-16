package com.mobileuni.model;

import java.util.Calendar;

public class CourseSchedule implements Comparable {

	private Course course;
	private Calendar start;
	private Calendar end;
	private String location;
	
	public CourseSchedule(Calendar start, Calendar end, String location) {
		this.start = start;
		this.end = end;
		this.location = location;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int compareTo(Object another) {
		CourseSchedule compare = (CourseSchedule) another;
		return (int) (compare.getStart().getTimeInMillis() - this.getStart().getTimeInMillis());
	}
}
