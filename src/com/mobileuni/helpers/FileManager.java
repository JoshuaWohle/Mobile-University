/**
*  an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
*  Copyright (C) 2012  Justin Stevanz, Andrew Kelson, Matthias Peitsch and Joshua Wöhle
*
*	Contact the.omega.online@gmail.com or jwohle@gmail.com for further information.
*
*   This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.mobileuni.helpers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.mobileuni.other.Constants;
//import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class FileManager {
	
	private static final String TAG = Constants.LOG_DOCUMENTS;
	
	private static FileManager instance = new FileManager(); 
    static Context context; 
 
    public static FileManager getInstance(Context ctx) { 
        context = ctx; 
        return instance; 
    }

	public void UploadToUrl(String siteUrl, String token, String filepath) {		
		
		String url = siteUrl + "/webservice/upload.php?token=" + token;
		HttpClient httpclient = new DefaultHttpClient(); 
	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1); 
	 
	    org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(url); 
	    File file = new File(filepath); 
	    
	    String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filepath.substring(filepath.lastIndexOf("."))));
	 
	    MultipartEntity mpEntity = new MultipartEntity(); 
	    ContentBody cbFile = new FileBody(file, mimetype); 
	    mpEntity.addPart("userfile", cbFile); 
	 
	 
	    httppost.setEntity(mpEntity); 
	    Log.d(TAG, "upload executing request " + httppost.getRequestLine());
	    try {
	    	
		    HttpResponse response = httpclient.execute(httppost);
	
		    HttpEntity resEntity = response.getEntity(); 
		 
		    Log.d(TAG, "upload line status " + response.getStatusLine()); 
		    if (resEntity != null) { 
		    	Log.d(TAG, "upload " + EntityUtils.toString(resEntity)); 
		    	//JSONObject jObject = new JSONObject(EntityUtils.toString(resEntity));
		    }
		    else {
		    	Log.d(TAG, "upload error: " + EntityUtils.toString(resEntity));
		    }		    	
		 
	    }
	    catch (Exception ex) {
	    	Log.d(TAG, "Error: " + ex);
	    }
	    
	    httpclient.getConnectionManager().shutdown(); 
	}
	
	
}
