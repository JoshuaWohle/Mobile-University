package com.mobileuni.helpers.asynctasks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import org.json.JSONException;
import org.json.JSONObject;

import com.mobileuni.config.Config;
import com.mobileuni.controller.LoginController;
import com.mobileuni.model.Settings;
import com.mobileuni.other.Constants;
import com.mobileuni.other.Session;
import com.mobileuni.other.WebServiceFunction;

import android.os.AsyncTask;
import android.util.Log;

public class WebServiceResponseTask extends AsyncTask<Object, Object, JSONObject> {
	
	private String fn;
	private Object extra; 
	
	@Override
	protected JSONObject doInBackground(Object... params) {
		fn = (String) params[0];
		String urlParameters = (String) params[1];
		int xslRawId = (Integer) params[2];
		if(params.length > 3) {
			extra = params[3];
		}
		HttpURLConnection con;
		try {
			String url = Config.apiUrl + fn;
			Log.d(Constants.LOG_WSR, "Sent: " + url);
			con = (HttpURLConnection) new URL(url)
					.openConnection();

			con.setConnectTimeout(30000);
			con.setReadTimeout(30000);

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Language", "en-US");
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDoInput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			Log.d(Constants.LOG_WSR, "URL parameters: " + urlParameters.toString());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = con.getInputStream();

			Source xmlSource = new StreamSource(is);
			Source xsltSource = new StreamSource(Session.getContext().getResources()
					.openRawResource(xslRawId));

			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer(xsltSource);
			StringWriter writer = new StringWriter();
			trans.transform(xmlSource, new StreamResult(writer));

			String jsonstr = writer.toString();
			jsonstr = jsonstr.replace("<div class=\"no-overflow\"><p>", "");
			jsonstr = jsonstr.replace("</p></div>", "");
			jsonstr = jsonstr.replace("<p>", "");
			jsonstr = jsonstr.replace("</p>", "");
			return new JSONObject(jsonstr);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(JSONObject jsonObject) {
		if(jsonObject == null)
			return;
		
		Log.d(Constants.LOG_WSR, "Received: " + jsonObject.toString());
		
		// Have to use a lot of if-else, as switch over Strings doesn't work in Java 6 yet :(
		if(fn.equals(WebServiceFunction.moodle_webservice_get_siteinfo))
			Session.getCourseManager().setMainInfo(jsonObject);
		else if(fn.equals(WebServiceFunction.moodle_enrol_get_users_courses))
			Session.getCourseManager().setCourses(jsonObject, Settings.isAutoDownloadContent());
		else if(fn.equals(WebServiceFunction.core_course_get_contents))
			Session.getCourseManager().setCourseDetails(jsonObject, (Integer)extra);
		else
			Log.d(Constants.LOG_WSR, "Some unknown request was executed, please specify where to send the result to");
	}

}
