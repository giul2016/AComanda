package com.arquitetaweb.comanda.dados;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;
import com.google.gson.Gson;

public class PostGenericApi<T> {

	private Context context;
	private String URL_API = "PostMethod"; // Post Mesa

	public PostGenericApi(Context context) {
		this.context = context;
	}

	public boolean sendData(String urlApi, T obj) {
		URL_API = urlApi;
		return postData(obj);
	}

	public Pair<Boolean, String> sendDataReturn(String urlApi, T obj) {
		URL_API = urlApi;

		return postDataReturn(obj);
	}

	private Pair<Boolean, String> postDataReturn(T obj) {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context) + "/Api/" + URL_API;
			try {
				HttpResponse response;
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(urlApi);
				String jsonString = new Gson().toJson(obj);
				StringEntity se = new StringEntity(jsonString);
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);

				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					Log.i("Sucess!", "Sucesso");
					/* Checking response */
					if (response != null) {
						InputStream in = response.getEntity().getContent();						
						return new Pair<Boolean, String>(true, "aqui 200");
					}
				} else if (statusCode == 500) {
					if (response != null) {
						InputStream in = response.getEntity().getContent();						
						return new Pair<Boolean, String>(false, "aqui 500");
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			errorConnectServer();
		}
		return new Pair<Boolean, String>(false, "sem retorno");
	}

	private boolean postData(T obj) {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context) + "/Api/" + URL_API;
			try {
				HttpResponse response;
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(urlApi);
				String jsonString = new Gson().toJson(obj);
				StringEntity se = new StringEntity(jsonString);
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);

				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					Log.i("Sucess!", "Sucesso");
					return true;
				}

				/* Checking response */
				// if (response != null) {
				// InputStream in = response.getEntity().getContent(); // Get
				// // the
				// // data
				// // in
				// // the
				// // entity
				// }

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			errorConnectServer();
		}
		return false;
	}

	private void errorConnectServer() {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				new Alerta()
						.show(context, "", "Não Foi Localizado o Servidor!"
								+ "\nCausas:" + "\nConexão OK?"
								+ "\nServidor correto?");
			}
		});
	}
}
