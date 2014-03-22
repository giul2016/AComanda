package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.adapter.PedidoAdapter;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.dados.PostGenericApi;
import com.arquitetaweb.comanda.interfaces.AsyncTaskListener;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.comanda.model.model_enum.SituacaoMesa;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
import com.google.gson.reflect.TypeToken;

public class PedidoController {

	private ProgressDialog progressDialog;
	private Context context;
	private Fragment fragment;
	private String URL_API = "Mesa";
	private List<ConsumoModel> consumoModelList;
	private MesaModel mesa;

	public PedidoController(Fragment fragment, ProgressDialog progressDialog,
			MesaModel mesa) {
		this.progressDialog = progressDialog;
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.mesa = mesa;
	}

	public void enviarPedido(PedidoAdapter adapter) {
		if ((mesa.situacao != SituacaoMesa.EmConta)
				&& (mesa.situacao != SituacaoMesa.Limpar)) {

			consumoModelList = new ArrayList<ConsumoModel>();
			for (ConsumoModel item : adapter.getItens()) {
				int qtde = Integer.parseInt(item.quantidade);
				if (qtde > 0) {
					item.deviceId = (long) 1;
					item.mesaId = mesa.id;
					item.dataHora = new Date();
					consumoModelList.add(item);
				}
			}

			if (consumoModelList.size() > 0)
				new EnviarPedido(fragment).execute();
			else {
				Toast toast = Toast.makeText(context,
						"Não existe item a enviar.", Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			Toast toast = Toast.makeText(context,
					"A situação da mesa não permite lançamentos. (Situação: "
							+ mesa.situacao + ")", Toast.LENGTH_LONG);
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
			URL_API = "Mesa";
			PostGenericApi<List<ConsumoModel>> api = new PostGenericApi<List<ConsumoModel>>(
					context);
			return api.sendData(URL_API, consumoModelList);
		}
	}

	public void sincronizar(long idProduto, boolean produtoRecente) {
		URL_API = "Mesa/" + mesa.id + "/10"; // 10 itens no máximo
		new SincronizarDados(fragment).execute(idProduto, produtoRecente);
	}

	private class SincronizarDados extends
			AsyncTask<Object, Void, List<ConsumoModel>> {

		private AsyncTaskListener callback;

		public SincronizarDados(Fragment fragment) {
			this.callback = (AsyncTaskListener) fragment;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage(context
					.getString(R.string.carregandoRecentes));
			progressDialog.show();
		}

		@Override
		protected List<ConsumoModel> doInBackground(Object... params) {
			return SincronizarConsumo((Long) params[0], (Boolean) params[1]);
		}

		@Override
		protected void onPostExecute(List<ConsumoModel> result) {
			super.onPostExecute(result);
			callback.onTaskComplete(result);
			progressDialog.dismiss();
		}

		private List<ConsumoModel> SincronizarConsumo(long idProduto,
				boolean produtoRecente) {
			consumoModelList = new ArrayList<ConsumoModel>();

			if (produtoRecente) {
				GetGenericApi<ConsumoModel> api = new GetGenericApi<ConsumoModel>(
						context);
				consumoModelList = api.LoadListApiFromUrl(URL_API,
						new TypeToken<ArrayList<ConsumoModel>>() {
						}.getType());

				for (ConsumoModel item : consumoModelList) {
					item.quantidade = "0";
				}

			} else {
				ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(
						context);
				List<ProdutoModel> produtoModelList = dbProduto
						.selectWhere("produtoGrupoId = " + idProduto);
				dbProduto.closeDB();

				for (ProdutoModel produtoModel : produtoModelList) {
					ConsumoModel item = new ConsumoModel();
					item.produtoId = produtoModel.id;
					item.quantidade = "0";
					consumoModelList.add(item);
				}
			}

			return consumoModelList;
		}
	}

}
