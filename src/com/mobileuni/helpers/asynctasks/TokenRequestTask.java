package com.mobileuni.helpers.asynctasks;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobileuni.config.UserSettings;

import android.os.AsyncTask;
import android.util.Log;

public class TokenRequestTask extends AsyncTask {
	
	public static String get(String url) {
		try {
			return (String) new TokenRequestTask().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Object doInBackground(Object... urls) {
		String responseBody = "";

		DefaultHttpClient httpClient = new DefaultHttpClient();

		// Creating HTTP Post
		HttpGet httpPost = new HttpGet((String) urls[0]);

		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpClient.execute(httpPost, responseHandler);

			JSONObject json = new JSONObject(responseBody);
			UserSettings.userToken = json.getString("token");
			Log.d("authentication", "Got a new token : " + UserSettings.userToken);
			return UserSettings.userToken;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		}
		return null;
	}

}
