package com.mobileuni.listeners;

/**
 * A simple interface used to keep track of changes on a course
 * @author Joshua Wöhle
 */
public interface CourseChangeListener {

	/**
	 * The contents of a course changed and are ready to be treated
	 */
	public void courseContentsChanged();
	
	/**
	 * A file was changed (probably just downloaded)
	 * @param filePath
	 */
	public void fileChanged(String filePath);
	
	/**
	 * Notes of a certain course changed (probably a new note got created or old ones got downloaded)
	 */
	public void notesChanged();
	
}
