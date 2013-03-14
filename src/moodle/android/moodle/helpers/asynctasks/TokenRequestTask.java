package moodle.android.moodle.helpers.asynctasks;

import java.io.IOException;

import moodle.android.moodle.config.UserSettings;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class TokenRequestTask extends AsyncTask {

	@Override
	protected Object doInBackground(Object... urls) {
		String responseBody = "";
		String token = "";

		DefaultHttpClient httpClient = new DefaultHttpClient();

		// Creating HTTP Post
		HttpGet httpPost = new HttpGet((String) urls[0]);

		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpClient.execute(httpPost, responseHandler);

			JSONObject jObject = new JSONObject(responseBody);
			token = jObject.getString("token");
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
		UserSettings.userToken = token;
		return null;
	}

}
