package com.arquitetaweb.comanda.dados;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.arquitetaweb.comanda.util.JSONParser;
import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;
import com.google.gson.Gson;

public class GetGenericApi<T> {

	private Context context;
	private String URL_API = "GetMethod"; // GetProdutoGrupo
	private Type type;

	public GetGenericApi(Context context) {
		this.context = context;
	}

	public List<T> LoadListApiFromUrl(String urlApi, Type type) {
		this.type = type;
		URL_API = urlApi;
		return getObject();
	}

	public boolean LoadListApiFromUrl_(String urlApi) {
		URL_API = urlApi;
		return getUrl();
	}

	private List<T> getObject() {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context) + "/Api/" + URL_API;
			JSONParser jParser = new JSONParser();
			final String json = jParser.getJSONFromApi(urlApi);
			List<T> list = new ArrayList<T>();
			Gson gson = new Gson();
			list = gson.fromJson(json, type);

			return list;
		} else {
			errorConnectServer();
		}
		return new ArrayList<T>();
	}

	private boolean getUrl() {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context) + "/Api/" + URL_API;
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromApi(urlApi);
			Log.i("AQUI",json +" - "+ urlApi);
			if (json.contains("sucess"))
				return true;
			return false;
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
