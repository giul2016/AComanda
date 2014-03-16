package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.google.gson.reflect.TypeToken;

public class ConsumoController {

	private ProgressDialog progressDialog;
	private Context context;
	private String URL_API = "Mesa/";
	private long MESA_ID = 0;

	public ConsumoController(Context context, ProgressDialog progressDialog,
			Long mesaId) {
		this.progressDialog = progressDialog;
		this.context = context;
		this.MESA_ID = mesaId;
		URL_API = URL_API + MESA_ID;
	}

	public List<ConsumoModel> sincronizar() {
		try {
			return new SincronizarDados().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<ConsumoModel>();
	}

	private class SincronizarDados extends
			AsyncTask<Void, Void, List<ConsumoModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage("carregando consumo...");
			progressDialog.show();
		}

		@Override
		protected List<ConsumoModel> doInBackground(Void... produtos) {
			return SincronizarConsumo();
		}

		@Override
		protected void onPostExecute(List<ConsumoModel> result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	private List<ConsumoModel> SincronizarConsumo() {
		GetGenericApi<ConsumoModel> api = new GetGenericApi<ConsumoModel>(
				context);
		List<ConsumoModel> mesaList = api.LoadListApiFromUrl(URL_API,
				new TypeToken<ArrayList<ConsumoModel>>() {
				}.getType());
		return mesaList;
	}

}
