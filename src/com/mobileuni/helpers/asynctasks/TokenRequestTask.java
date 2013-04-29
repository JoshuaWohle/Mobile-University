package com.mobileuni.helpers.asynctasks;

import java.io.IOException;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobileuni.R;
import com.mobileuni.model.Session;
import com.mobileuni.model.iCourseManager;
import com.mobileuni.other.Constants;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Asks (downloads) a token from the requested iCourseManager for the user
 * @author Joshua Wöhle
 */
public class TokenRequestTask extends AsyncTask<Object, Object, JSONObject> {
	
	iCourseManager cm;
	
	@Override
	protected JSONObject doInBackground(Object... params) {
		Log.d(Constants.LOG_AUTHENTICATION, "Processing login in background");
		String tokenUrl = (String) params[0];

		cm = (iCourseManager) params[1];
		String responseBody = "";

		DefaultHttpClient httpClient = new DefaultHttpClient();

		// Creating HTTP Post
		HttpGet httpPost = new HttpGet(tokenUrl);
		Log.d(Constants.LOG_WSR, tokenUrl);
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpClient.execute(httpPost, responseHandler);

			JSONObject json = new JSONObject(responseBody);
			
			return json;
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostExecute(JSONObject jsonObject) {
		try {
			Log.d(Constants.LOG_AUTHENTICATION, "Got a new token : " + jsonObject.getString("token"));
			cm.setToken(jsonObject.getString("token"));
		} catch (JSONException e) {
			Log.d(Constants.LOG_AUTHENTICATION, "JSON Exception, setting token to null");
			cm.setToken(null);
			e.printStackTrace();
		} catch (Exception e) {
			Log.d(Constants.LOG_AUTHENTICATION, "Something else went wrong while authenticating");
			Toast.makeText(Session.getContext(), R.string.login_incorrect, Toast.LENGTH_SHORT).show();
			cm.setToken(null);
			e.printStackTrace();
		}
	}

}
