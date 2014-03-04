package com.arquitetaweb.comanda.dados;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.arquitetaweb.comanda.util.JSONParser;
import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;
import com.google.gson.Gson;

public class GetGenericApi<T> {

	private Context context;
	private static String URL_API = "GetMethod"; // GetProdutoGrupo
	private Type type;

	public GetGenericApi(Context context) {
		this.context = context;
	}

	public List<T> LoadListApiFromUrl(String urlApi, Type type) {
		this.type = type;
		URL_API = urlApi;
		return getObject();
	}

	private List<T> getObject() {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context) + "/Api/" + URL_API;
			JSONParser jParser = new JSONParser();
			final String jsonGarcom = jParser.getJSONFromApi(urlApi);
			List<T> list = new ArrayList<T>();
			Gson gson = new Gson();
			list = gson.fromJson(jsonGarcom, type);

			return list;
		} else {
			errorConnectServer();
		}
		return null;
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
