package com.arquitetaweb.comanda.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.model.GarcomModel;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.GarcomGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;
import com.google.gson.reflect.TypeToken;

public class SincronizationFragment extends Fragment implements
		View.OnClickListener {

	private static final String SETTINGS_SCHEME = "settings";
	private static final String SETTINGS_AUTHORITY = "synchronize";
	private ProgressDialog progressDialog;
	private View viewRoot;

	public static final String TAG = SincronizationFragment.class
			.getSimpleName();
	public static final Uri SINCRONIZATION_URI = new Uri.Builder()
			.scheme(SETTINGS_SCHEME).authority(SETTINGS_AUTHORITY).build();

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewRoot = inflater.inflate(R.layout.sincronization, container, false);

		Button btnSalvar = (Button) viewRoot.findViewById(R.id.btnSynchronize);
		btnSalvar.setOnClickListener(this);

		return viewRoot;
	}

	@Override
	public void onClick(View v) {
		progressDialog = new ProgressDialog(this.getActivity());
		new SincronizarDados().execute();
	}

	protected class SincronizarDados extends AsyncTask<Void, Integer, Void> {
		Integer positionProgress = 0;
		String[] messages = new String[5];

		private void createMessages() {
			messages[0] = "sincronizando dados....";
			messages[1] = "sincronizando gar�om....";
			messages[2] = "sincronizando grupo de produto....";
			messages[3] = "sincronizando produto....";
			messages[4] = "finalizando...";
		}

		private void updateMessage() {
			positionProgress++;
			publishProgress(positionProgress);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			createMessages();
			progressDialog.setCancelable(false);
			progressDialog.setMessage(messages[0]);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... produtos) {
			try {
				publishProgress(positionProgress);
				Thread.sleep(1000);

				updateMessage();
				SincronizarGarcom();

				updateMessage();
				SincronizarProdutoGrupo();

				updateMessage();
				SincronizarProduto();

				updateMessage();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressDialog.setMessage(messages[values[0]]);
		}
	}

	private void SincronizarGarcom() {
		GetGenericApi<GarcomModel> produto = new GetGenericApi<GarcomModel>(
				this.getActivity());
		List<GarcomModel> garcomLista = produto.LoadListApiFromUrl("GetGarcom",
				new TypeToken<ArrayList<GarcomModel>>() {
				}.getType());

		Log.d("SincronizarGarcom", "Iniciando SincronizarGarcom....");

		GarcomGenericHelper dbGarcom = new GarcomGenericHelper(
				this.getActivity());
		dbGarcom.sincronizar(garcomLista);

		List<GarcomModel> allToDos = dbGarcom.getAll();
		for (GarcomModel todo : allToDos) {
			Log.d("GarcomModel", todo.id + " - " + todo.codigo + " - "
					+ todo.nome);
		}

		// Don't forget to close database connection
		dbGarcom.closeDB();
		Log.d("SincronizarGarcom", "Finalizando SincronizarGarcom....");
	}

	private void SincronizarProdutoGrupo() {

		GetGenericApi<ProdutoGrupoModel> produto = new GetGenericApi<ProdutoGrupoModel>(
				this.getActivity());

		List<ProdutoGrupoModel> produtoLista = produto.LoadListApiFromUrl(
				"GetProduto", new TypeToken<ArrayList<ProdutoGrupoModel>>() {
				}.getType());

		Log.d("SincronizarProdutoGrupo",
				"Iniciando SincronizarProdutoGrupo....");

		ProdutoGrupoGenericHelper dbProdutoGrupo = new ProdutoGrupoGenericHelper(
				this.getActivity());
		dbProdutoGrupo.sincronizar(produtoLista);

		List<ProdutoGrupoModel> allToDos = dbProdutoGrupo.getAll();
		for (ProdutoGrupoModel todo : allToDos) {
			Log.d("getAllProduto", todo.id + " - " + todo.codigo + " - "
					+ todo.descricao);
		}

		dbProdutoGrupo.closeDB();
		Log.d("SincronizarProdutoGrupo",
				"Finalizando SincronizarProdutoGrupo....");
	}

	private void SincronizarProduto() {
		GetGenericApi<ProdutoModel> produto = new GetGenericApi<ProdutoModel>(
				this.getActivity());

		List<ProdutoModel> produtoLista = produto.LoadListApiFromUrl(
				"GetProduto", new TypeToken<ArrayList<ProdutoModel>>() {
				}.getType());

		Log.d("SincronizarProduto", "Iniciando SincronizarProduto....");

		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(
				this.getActivity());
		dbProduto.sincronizar(produtoLista);

		List<ProdutoModel> allToDos = dbProduto.getAll();
		for (ProdutoModel todo : allToDos) {
			Log.d("getAllProduto", todo.id + " - " + todo.produto_grupo_id
					+ " - " + todo.codigo + " - " + todo.descricao);
		}

		dbProduto.closeDB();
		Log.d("SincronizarProduto", "Finalizando SincronizarProduto....");
	}

	@Override
	public void onDestroy() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.onDestroy();
	}
}
