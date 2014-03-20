package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.adapter.PedidoAdapter;
import com.arquitetaweb.comanda.dados.PostGenericApi;
import com.arquitetaweb.comanda.interfaces.AsyncTaskListener;
import com.arquitetaweb.comanda.model.ConsumoModel;

public class PedidoController {

	private ProgressDialog progressDialog;
	private Context context;
	private Fragment fragment;
	private String URL_API = "Mesa";
	private List<ConsumoModel> lst;
	private long mesaId;

	public PedidoController(Fragment fragment, ProgressDialog progressDialog,
			long mesaId) {
		this.progressDialog = progressDialog;
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.mesaId = mesaId;
	}

	public void fecharConta(PedidoAdapter adapter) {
		lst = new ArrayList<ConsumoModel>();
		for (ConsumoModel item : adapter.getItens()) {
			int qtde = Integer.parseInt(item.quantidade);
			if (qtde > 0) {
				item.deviceid = (long) 1;
				item.mesaid = mesaId;
				lst.add(item);
			}
		}

		if (lst.size() > 0)
			new EnviarPedido(fragment).execute();
		else {
			Toast toast = Toast.makeText(context,
					"N�o existe item a enviar.", Toast.LENGTH_SHORT);
			toast.show();
		}
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
			progressDialog.setMessage(context.getString(R.string.enviandoItem));
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
			PostGenericApi<List<ConsumoModel>> api = new PostGenericApi<List<ConsumoModel>>(
					context);
			return api.sendData(URL_API, lst);
		}
	}

}
