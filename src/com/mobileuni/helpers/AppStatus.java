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

import com.mobileuni.other.Constants;
import com.mobileuni.other.Session;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AppStatus { 
    
    public static String getConnectionType(Context con) { 
    	String haveConnectedWifi = null; 
    	String haveConnectedMobile = null; 
	 
	    ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE); 
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo(); 
	    for (NetworkInfo networkInfo : netInfo) { 
	        if (networkInfo.getTypeName().equalsIgnoreCase("WIFI")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedWifi = "WIFI"; 
	        if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedMobile = "MOBILE"; 
	    } 
	    return (haveConnectedWifi != null) ? haveConnectedWifi : haveConnectedMobile; 
	}
 
    public static boolean isOnline() { 
    	boolean haveConnectedWifi = false; 
	    boolean haveConnectedMobile = false;
	    
	    ConnectivityManager connectivityManager = (ConnectivityManager) Session.getContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo(); 
	    for (NetworkInfo networkInfo : netInfo) { 
	        if (networkInfo.getTypeName().equalsIgnoreCase("WIFI")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedWifi = true; 
	        if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedMobile = true; 
	    } 
	    
	    boolean connection = haveConnectedWifi || haveConnectedMobile;

	    Log.d(Constants.LOG_CONNECTION_STATUS, Boolean.toString(connection));
	    return connection; 
    } 
    
    public static void setChangeOrientation(Activity a) {
    	if(isTablet(a)) {
    		a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    	}
    }
    
    private static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
} 

