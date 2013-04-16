package com.mobileuni.helpers.asynctasks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import com.mobileuni.model.Course;
import com.mobileuni.model.Session;
import com.mobileuni.other.Constants;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadFileTask extends AsyncTask<Object, Object, File> {
	
	String TAG = Constants.LOG_DOCUMENTS;
	Course course;

	@Override
	protected File doInBackground(Object... params) {
		File file = null;
		
		//TODO make Moodle independent
		String fileURL = (String) params[0] + "&token=" + Session.getCourseManager().getToken();
		String fileName = (String) params[1];
		String directory = (String) params[2];
		course = (Course) params[3];
		
		try {
			URL url = new URL(fileURL); 
			
			boolean mExternalStorageAvailable = false;
        	boolean mExternalStorageWriteable = false;

        	if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        	    mExternalStorageAvailable = mExternalStorageWriteable = true;
        	else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
        	    mExternalStorageAvailable = true;
        	    mExternalStorageWriteable = false;
        	} else
        	    mExternalStorageAvailable = mExternalStorageWriteable = false;

        	
        	if (mExternalStorageAvailable || mExternalStorageWriteable) {
			
				// create a File object for the parent directory 
        		//TODO make moodle independent
				File fileDirectory = new File(Constants.FILE_STORAGE_FOLDER_NAME + directory); 
				// have the object build the directory structure, if needed. 
				fileDirectory.mkdirs(); 
				// create a File object for the output file 
				file = new File(fileDirectory, fileName); 
				
				long startTime = System.currentTimeMillis();			
				Log.d(TAG, "download begining");
				Log.d(TAG, "download url:" + url);
				Log.d(TAG, "downloaded file name:" + fileName);
				
				/* Open a connection to that URL. */
				URLConnection ucon = url.openConnection();
				
				/*
				 * Define InputStreams to read from the URLConnection.
				 */
				InputStream is = ucon.getInputStream();						
				BufferedInputStream bis = new BufferedInputStream(is);
				
				/*
				 * Read bytes to the Buffer until there is nothing more to read(-1).
				 */
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {
						baf.append((byte) current);
				}
				
				/* Convert the Bytes read to a String. */
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baf.toByteArray());
				fos.close();
				
				Log.d(TAG, "download ready in"
								+ ((System.currentTimeMillis() - startTime) / 1000)
								+ " sec");	
        	}
			
		} catch (IOException e) {
				e.printStackTrace();
		}
		return file;
	}
	
	@Override
	public void onPostExecute(File file) {
		if(file != null) {
			Log.d(TAG, "File downloaded: " + file.getAbsolutePath());
			course.addAbsoluteFilePath(file.getAbsolutePath());
		} else
			Log.d(TAG, "File not downloaded, set to NULL");
	}

}
