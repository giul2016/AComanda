package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.model.GarcomModel;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.GarcomGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;
import com.google.gson.reflect.TypeToken;

public class SincronizationController {

	private ProgressDialog progressDialog;
	private Context context;
	private CheckBox checkGarcom, checkProdutoGrupo;
	private TextView txtSincSucess;
	private TextView txtSincSucessTime;

	public SincronizationController(Fragment fragment,
			ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
		this.context = fragment.getActivity();

		checkGarcom = (CheckBox) fragment.getView().findViewById(
				R.id.checkBoxSincGarcom);
		checkProdutoGrupo = (CheckBox) fragment.getView().findViewById(
				R.id.checkBoxSincProdutoGrupo);

		txtSincSucess = (TextView) fragment.getView().findViewById(
				R.id.txtSincronizadoSucesso);
		txtSincSucessTime = (TextView) fragment.getView().findViewById(
				R.id.txtSincronizadoSucessoTempo);
	}

	public void sincronizarDados() {
		new SincronizarDados().execute();
	}

	public void updateTextViewSincSucess() {
		if (txtSincSucess.getVisibility() == View.VISIBLE) {
			txtSincSucess.setTextColor(Color.parseColor("#000066"));
			txtSincSucess.setText("sincronizando aguarde...");
			txtSincSucessTime.setVisibility(View.INVISIBLE);
		}
	}

	private class SincronizarDados extends AsyncTask<Void, Integer, Void> {
		String[] messages = new String[5];
		long mStartTime;

		private void createMessages() {
			messages[0] = "sincronizando dados....";
			messages[1] = "sincronizando garçom....";
			messages[2] = "sincronizando grupo de produto....";
			messages[3] = "sincronizando produto....";
			messages[4] = "finalizando...";
		}

		private void updateMessage(Integer pos) {
			publishProgress(pos);
		}

		@Override
		protected void onPreExecute() {
			mStartTime = System.currentTimeMillis();
			super.onPreExecute();
			createMessages();
			progressDialog.setCancelable(false);
			progressDialog.setMessage(messages[0]);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... produtos) {
			try {
				publishProgress(0);
				Thread.sleep(1000);

				updateMessage(1);
				if (checkGarcom.isChecked())
					SincronizarGarcom();

				if (checkProdutoGrupo.isChecked()) {
					updateMessage(2);
					SincronizarProdutoGrupo();

					updateMessage(3);
					SincronizarProduto();
				}

				updateMessage(4);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();

			long mEndTime = System.currentTimeMillis();
			long diff = (mEndTime - mStartTime);
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;

			txtSincSucess.setVisibility(View.VISIBLE);
			txtSincSucess.setTextColor(Color.parseColor("#007300"));
			txtSincSucess.setText("sincronizado com sucesso");

			txtSincSucessTime.setVisibility(View.VISIBLE);
			String timeSucess = diffMinutes + " min. e " + diffSeconds
					+ " seg.";
			txtSincSucessTime.setText(timeSucess);

			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressDialog.setMessage(messages[values[0]]);
			txtSincSucess.setText(messages[values[0]]);
		}
	}

	private void SincronizarGarcom() {
		String URL_API = "Garcom";
		GetGenericApi<GarcomModel> garcomApi = new GetGenericApi<GarcomModel>(
				context);
		List<GarcomModel> garcomList = garcomApi.LoadListApiFromUrl(URL_API,
				new TypeToken<ArrayList<GarcomModel>>() {
				}.getType());

		Log.d("SincronizarGarcom", "Iniciando SincronizarGarcom....");

		GarcomGenericHelper dbGarcom = new GarcomGenericHelper(context);
		dbGarcom.sincronizar(garcomList);

		garcomList = dbGarcom.selectAll();
		for (GarcomModel garcom : garcomList) {
			Log.d("GarcomModel", garcom.id + " - " + garcom.codigo + " - "
					+ garcom.nome);
		}

		// Don't forget to close database connection
		dbGarcom.closeDB();
		Log.d("SincronizarGarcom", "Finalizando SincronizarGarcom....");
	}

	private void SincronizarProdutoGrupo() {
		String URL_API = "ProdutoGrupo";
		GetGenericApi<ProdutoGrupoModel> produtoGrupoApi = new GetGenericApi<ProdutoGrupoModel>(
				context);

		List<ProdutoGrupoModel> produtoGrupoList = produtoGrupoApi
				.LoadListApiFromUrl(URL_API,
						new TypeToken<ArrayList<ProdutoGrupoModel>>() {
						}.getType());

		Log.d("SincronizarProdutoGrupo",
				"Iniciando SincronizarProdutoGrupo....");

		ProdutoGrupoGenericHelper dbProdutoGrupo = new ProdutoGrupoGenericHelper(
				context);
		dbProdutoGrupo.sincronizar(produtoGrupoList);

		produtoGrupoList = dbProdutoGrupo.selectAll();
		for (ProdutoGrupoModel produtoGrupo : produtoGrupoList) {
			Log.d("getAllProduto", produtoGrupo.id + " - "
					+ produtoGrupo.codigo + " - " + produtoGrupo.descricao);
		}

		dbProdutoGrupo.closeDB();
		Log.d("SincronizarProdutoGrupo",
				"Finalizando SincronizarProdutoGrupo....");
	}

	private void SincronizarProduto() {
		String URL_API = "Produto";
		GetGenericApi<ProdutoModel> produtoApi = new GetGenericApi<ProdutoModel>(
				context);

		List<ProdutoModel> produtoList = produtoApi.LoadListApiFromUrl(URL_API,
				new TypeToken<ArrayList<ProdutoModel>>() {
				}.getType());

		Log.d("SincronizarProduto", "Iniciando SincronizarProduto....");

		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(context);
		dbProduto.sincronizar(produtoList);

		produtoList = dbProduto.selectAll();
		for (ProdutoModel produto : produtoList) {
			Log.d("getAllProduto", produto.id + " - "
					+ produto.produto_grupo_id + " - " + produto.codigo + " - "
					+ produto.descricao);
		}

		dbProduto.closeDB();
		Log.d("SincronizarProduto", "Finalizando SincronizarProduto....");
	}
}
