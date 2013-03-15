package moodle.android.moodle.helpers.asynctasks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import moodle.android.moodle.MainApp;
import moodle.android.moodle.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class WebServiceResponseTask extends AsyncTask<Object, Object, JSONObject> {
	
	public static JSONObject get(String functionName, String urlParameters, int xslRawId) {
		try {
			return new WebServiceResponseTask().execute(functionName, urlParameters, xslRawId).get();
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
	protected JSONObject doInBackground(Object... params) {
		String serverUrl = (String) params[0];
		String functionName = (String) params[1];
		String urlParameters = (String) params[2];
		int xslRawId = (Integer) params[3];
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) new URL(Config.serverUrl + functionName)
					.openConnection();
			// HttpURLConnection con = (HttpURLConnection) new URL(serverurl +
			// functionName + "&moodlewsrestformat=json").openConnection();

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

			Log.d("URLParameters: ", urlParameters.toString());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = con.getInputStream();

			Source xmlSource = new StreamSource(is);
			Source xsltSource = new StreamSource(MainApp.context.getResources()
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
			Log.d("TransformObject: ", jsonstr);
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
		}
		
		return null;
	}

}
