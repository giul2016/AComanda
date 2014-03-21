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
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comanda.model.model_enum.SituacaoMesa;

public class PedidoController {

	private ProgressDialog progressDialog;
	private Context context;
	private Fragment fragment;
	private String URL_API = "Mesa";
	private List<ConsumoModel> lst;
	private MesaModel mesa;

	public PedidoController(Fragment fragment, ProgressDialog progressDialog,
			MesaModel mesa) {
		this.progressDialog = progressDialog;
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.mesa = mesa;
	}

	public void fecharConta(PedidoAdapter adapter) {
		if ((mesa.situacao != SituacaoMesa.EmConta)
				&& (mesa.situacao != SituacaoMesa.Limpar)) {

			lst = new ArrayList<ConsumoModel>();
			for (ConsumoModel item : adapter.getItens()) {
				int qtde = Integer.parseInt(item.quantidade);
				if (qtde > 0) {
					item.deviceid = (long) 1;
					item.mesaid = mesa.id;
					lst.add(item);
				}
			}

			if (lst.size() > 0)
				new EnviarPedido(fragment).execute();
			else {
				Toast toast = Toast.makeText(context,
						"N�o existe item a enviar.", Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			Toast toast = Toast.makeText(context, "A situa��o da mesa n�o permite lan�amentos. (Situa��o: " + mesa.situacao + ")",
					Toast.LENGTH_LONG);
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
