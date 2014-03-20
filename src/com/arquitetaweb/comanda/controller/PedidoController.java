package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.arquitetaweb.comanda.adapter.PedidoAdapter;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.interfaces.AsyncTaskListener;
import com.arquitetaweb.comanda.model.ConsumoModel;

public class PedidoController {

	private ProgressDialog progressDialog;
	private Context context;
	private Fragment fragment;
	private String URL_API = "Mesa/";
	private String URL_API_FECHAR = "Conta/";
	private long MESA_ID = 0;

	public PedidoController(Fragment fragment, ProgressDialog progressDialog,
			long mesaId) {
		this.progressDialog = progressDialog;
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.MESA_ID = mesaId;
	}

	public void fecharConta(PedidoAdapter adapter) {
		List<ConsumoModel> lst = new ArrayList<ConsumoModel>();
		for (ConsumoModel item : adapter.getItens()) {
			int qtde = Integer.parseInt(item.quantidade);
			if (qtde > 0) {
				lst.add(item);				
			}				
		}
		
		Toast toast = Toast.makeText(context, "Enviando " + lst.size() + " Objetos",
				Toast.LENGTH_SHORT);
		toast.show();

		new EnviarPedido(fragment).execute();
	}

	private class EnviarPedido extends AsyncTask<Void, Void, Boolean> {
		private AsyncTaskListener callback;

		public EnviarPedido(Fragment fragment) {
			this.callback = (AsyncTaskListener) fragment;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage("enviando conta...");
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
