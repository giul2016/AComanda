package com.arquitetaweb.comanda.dados;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.util.JSONParser;
import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;
import com.google.gson.Gson;

public class GetGenericApi<T> {

	private Context context;
	private static String URL_API = "GetMethod"; //GetProdutoGrupo

	public GetGenericApi(Context context) {
		this.context = context;
	}

	public List<T> LoadListApiFromUrl(String urlApi) {
		LoadListAsync list = new LoadListAsync();
		try {
			URL_API = urlApi;
			return (List<T>) list.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	private class LoadListAsync extends
			AsyncTask<String, Void, List<T>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<T> doInBackground(String... params) {
			if (Utils.isConnected(context)) {
				return getObject();
			} else {
				errorConnectServer();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<T> result) {
			super.onPostExecute(result);
		}
	}

	private List<T> getObject() {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context)
					+ "/Api/" +  URL_API;
			JSONParser jParser = new JSONParser();
			final String jsonGarcom = jParser.getJSONFromApi(urlApi);
			List<T> list = new ArrayList<T>();
			Gson gson = new Gson();
			list = gson.fromJson(jsonGarcom,
					new ProdutoGrupoModel().getType());

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
