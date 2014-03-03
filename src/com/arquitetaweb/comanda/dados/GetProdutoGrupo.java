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

public class GetProdutoGrupo {

	private Context context;

	public GetProdutoGrupo(Context context) {
		this.context = context;
	}

	public List<ProdutoGrupoModel> carregarProdutoGrupo() {
		CarregaProdutoGrupo t = new CarregaProdutoGrupo();
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

	private class CarregaProdutoGrupo extends
			AsyncTask<String, Void, List<ProdutoGrupoModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<ProdutoGrupoModel> doInBackground(String... params) {
			if (Utils.isConnected(context)) {
				return getProdutoGrupo();
			} else {
				errorConnectServer();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<ProdutoGrupoModel> result) {
			super.onPostExecute(result);
		}
	}

	private List<ProdutoGrupoModel> getProdutoGrupo() {
		if (Utils.isConnected(context)) {
			String urlApi = Utils.getUrlServico(context)
					+ "/Api/GetProdutoGrupo";
			JSONParser jParser = new JSONParser();
			final String jsonGarcom = jParser.getJSONFromApi(urlApi);
			List<ProdutoGrupoModel> produtoGrupoLista = new ArrayList<ProdutoGrupoModel>();
			Gson gson = new Gson();
			produtoGrupoLista = gson.fromJson(jsonGarcom,
					new ProdutoGrupoModel().getType());

			return produtoGrupoLista;
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
