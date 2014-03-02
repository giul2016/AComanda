package com.arquitetaweb.comanda.dados;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.arquitetaweb.comanda.model.GarcomModel;
import com.arquitetaweb.comanda.util.JSONParser;
import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;
import com.google.gson.Gson;

public class GetGarcom {

	private Context context;

	public GetGarcom(Context context) {
		this.context = context;
	}

	public List<GarcomModel> carregarGarcom() {
		CarregaGarcom t = new CarregaGarcom();
		try {
			return t.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	private class CarregaGarcom extends AsyncTask<String, Void, List<GarcomModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<GarcomModel> doInBackground(String... params) {
			if (Utils.isConnected(context)) {
				return getGarcom();
			} else {
				errorConnectServer();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<GarcomModel> result) {
			super.onPostExecute(result);
		}
	}

	private List<GarcomModel> getGarcom() {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context) + "/Api/GetGarcom";
			JSONParser jParser = new JSONParser();
			final String jsonGarcom = jParser.getJSONFromApi(urlApi);
			List<GarcomModel> garcomLista = new ArrayList<GarcomModel>();
			Gson gson = new Gson();
			garcomLista = gson
					.fromJson(jsonGarcom, new GarcomModel().getType());

			return garcomLista;
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
