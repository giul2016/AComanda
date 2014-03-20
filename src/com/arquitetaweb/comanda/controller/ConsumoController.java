package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.interfaces.AsyncTaskListener;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.google.gson.reflect.TypeToken;

public class ConsumoController {

	private ProgressDialog progressDialog;
	private Context context;
	private Fragment fragment;
	private String URL_API = "Mesa/";
	private String URL_API_FECHAR = "Conta/";
	private long MESA_ID = 0;

	public ConsumoController(Fragment fragment, ProgressDialog progressDialog,
			Long mesaId) {
		this.progressDialog = progressDialog;
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.MESA_ID = mesaId;
	}

	public void sincronizar() {
		new SincronizarDados(fragment).execute();
	}

	public void fecharConta() {
		new FecharConta(fragment).execute();
	}

	private class SincronizarDados extends
			AsyncTask<Void, Void, List<ConsumoModel>> {

		private AsyncTaskListener callback;

		public SincronizarDados(Fragment fragment) {
			this.callback = (AsyncTaskListener) fragment;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage(context.getString(R.string.carregandoConsumo));
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

		private List<ConsumoModel> SincronizarConsumo() {
			GetGenericApi<ConsumoModel> api = new GetGenericApi<ConsumoModel>(
					context);
			List<ConsumoModel> mesaList = api.LoadListApiFromUrl(URL_API
					+ MESA_ID, new TypeToken<ArrayList<ConsumoModel>>() {
			}.getType());
			return mesaList;
		}
	}

	private class FecharConta extends AsyncTask<Void, Void, Boolean> {
		private AsyncTaskListener callback;

		public FecharConta(Fragment fragment) {
			this.callback = (AsyncTaskListener) fragment;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage(context.getString(R.string.fechandoConta));
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return fecharConta();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			callback.onClosedComplete(result);
			progressDialog.dismiss();
		}

		private Boolean fecharConta() {
			GetGenericApi<ConsumoModel> api = new GetGenericApi<ConsumoModel>(
					context);
			return api.LoadListApiFromUrl_(URL_API + URL_API_FECHAR + MESA_ID);
		}
	}

}
