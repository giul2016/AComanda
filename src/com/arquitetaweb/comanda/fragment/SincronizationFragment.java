package com.arquitetaweb.comanda.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
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
import com.arquitetaweb.helper.sqllite.GarcomHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoHelper;
import com.arquitetaweb.helper.sqllite.ProdutoHelper;
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
		// SincronizarGarcom();
		// SincronizarProdutoGrupo();
		//SincronizarProduto(); // Generics
		
		SincronizarGenericProduto(); // Generics
	}

	private void SincronizarGarcom() {
		GetGenericApi<GarcomModel> produto = new GetGenericApi<GarcomModel>(
				this.getActivity());
		List<GarcomModel> garcomLista = produto.LoadListApiFromUrl("GetGarcom",
				new TypeToken<ArrayList<GarcomModel>>() {
				}.getType());

		Log.d("SincronizarGarcom", "Iniciando SincronizarGarcom....");
		GarcomHelper dbGarcom = new GarcomHelper(this.getActivity(),
				progressDialog);
		dbGarcom.sincronizar(garcomLista);

		// select
		// List<GarcomModel> allToDos = dbGarcom.getAllGarcom();
		// for (GarcomModel todo : allToDos) {
		// Log.d("getAllGarcom", todo.id + " - " + todo.codigo + " - "
		// + todo.nome);
		// }

		// Don't forget to close database connection
		dbGarcom.closeDB();
		Log.d("SincronizarGarcom", "Finalizando SincronizarGarcom....");
	}

	private void SincronizarProdutoGrupo() {
		GetGenericApi<ProdutoGrupoModel> produto = new GetGenericApi<ProdutoGrupoModel>(
				this.getActivity());
		List<ProdutoGrupoModel> produtoLista = produto.LoadListApiFromUrl(
				"GetProdutoGrupo",
				new TypeToken<ArrayList<ProdutoGrupoModel>>() {
				}.getType());

		Log.d("SincronizarProdutoGrupo",
				"Iniciando SincronizarProdutoGrupo....");
		ProdutoGrupoHelper dbProdutoGrupo = new ProdutoGrupoHelper(
				this.getActivity(), progressDialog);
		dbProdutoGrupo.sincronizar(produtoLista);

		// select
		// List<ProdutoGrupoModel> allToDos =
		// dbProdutoGrupo.getAllProdutoGrupo();
		// for (ProdutoGrupoModel todo : allToDos) {
		// Log.d("getAllProdutoGrupo", todo.id + " - " + todo.codigo + " - "
		// + todo.descricao);
		// }

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
		ProdutoHelper dbProduto = new ProdutoHelper(this.getActivity(),
				progressDialog);
		dbProduto.sincronizar(produtoLista);

		dbProduto.closeDB();
		Log.d("SincronizarProduto", "Finalizando SincronizarProduto....");
	}

	private void SincronizarGenericProduto() {
		GetGenericApi<ProdutoModel> produto = new GetGenericApi<ProdutoModel>(
				this.getActivity());
		
		List<ProdutoModel> produtoLista = produto.LoadListApiFromUrl(
				"GetProduto", new TypeToken<ArrayList<ProdutoModel>>() {
				}.getType());

		Log.d("SincronizarProduto", "Iniciando SincronizarProduto....");
		progressDialog = new ProgressDialog(this.getActivity());
		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(this.getActivity(),
				progressDialog);
		dbProduto.sincronizar(produtoLista);
				
//		List<ProdutoModel> allToDos = dbProduto.getAll();
//		for (ProdutoModel todo : allToDos) {
//			Log.d("getAllProduto", todo.id + " - " + todo.codigo + " - "
//					+ todo.descricao);
//		}
		
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
