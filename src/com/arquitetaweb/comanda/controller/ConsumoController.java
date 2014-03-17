package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.interfaces.AsyncTaskListener;
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

	public void sincronizar(Fragment fragment) {
		new SincronizarDados(fragment).execute();
	}

	public class SincronizarDados extends
			AsyncTask<Void, Void, List<ConsumoModel>> {

		private AsyncTaskListener callback;

		public SincronizarDados(Fragment fragment) {
			this.callback = (AsyncTaskListener) fragment;
		}

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
			super.onPostExecute(result);
			callback.onTaskComplete(result);			
			progressDialog.dismiss();
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
