package com.arquitetaweb.comanda.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONArray jarray = null;
	static String json = "";

	public JSONParser() {
	}

	public void atualizaMesa(String codigo, String mesa, Activity activity) {
		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(Utils.getUrlServico(activity)
				+ "/Api/AtualizarMesa?id=" + mesa + "&situacao=" + codigo);

		try {
			HttpResponse response = client.execute(put);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				Log.i("Sucess!", "Sucesso");
			}
		} catch (ClientProtocolException e1) {
			Log.e("Error....", e1.getMessage());
		} catch (IOException e1) {
			Log.e("Error....", e1.getMessage());
		}
	}

	public String getJSONFromUrlString(String url) {
		HttpClient client = new DefaultHttpClient();
		StringBuilder builder = new StringBuilder();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e("Error....", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

	public JSONArray getJSONFromUrl(String url) {
		String builder = new String();		
		try {
			builder = getJSONFromUrlString(url);
			jarray = new JSONArray(builder);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jarray;
	}
}
