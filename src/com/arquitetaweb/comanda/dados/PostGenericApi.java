package com.arquitetaweb.comanda.dados;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private String responseStrJson;

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
						responseStrJson = getResponse(response);
						Log.i("Sucess!", responseStrJson);
						return new Pair<Boolean, String>(true, responseStrJson);
					}
				} else if (statusCode == 500) { // Algum erro ocorreu
					Log.i("Sucess!", "Retorno 500");
					if (response != null) {
						responseStrJson = getResponse(response);
						Log.i("Sucess!", responseStrJson);
						return new Pair<Boolean, String>(false, responseStrJson);
					}
				} else if (statusCode == 401) { // Sem autorizacao
					Log.i("Sucess!", "Retorno 401");
					if (response != null) {
						responseStrJson = getResponse(response);
						Log.i("Sucess!", responseStrJson);
						return new Pair<Boolean, String>(false, responseStrJson);
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
		return new Pair<Boolean, String>(false, "Sem conexão!");
	}

	private String getResponse(HttpResponse response) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuilder sb = new StringBuilder();
		sb.append(in.readLine() + "\n");
		String line = "0";
		while ((line = in.readLine()) != null) {
			sb.append(line + "\n");
		}
		String responseA = sb.toString();
		return responseA;
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
