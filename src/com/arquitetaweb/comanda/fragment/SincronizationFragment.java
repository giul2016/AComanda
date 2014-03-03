package com.arquitetaweb.comanda.fragment;

import java.util.List;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.GetGarcom;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.dados.GetProdutoGrupo;
import com.arquitetaweb.comanda.model.GarcomModel;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.GarcomHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoHelper;
import com.arquitetaweb.helper.sqllite.ProdutoHelper;

public class SincronizationFragment extends Fragment implements
		View.OnClickListener {
	public static final String TAG = SincronizationFragment.class
			.getSimpleName();
	private static final String SETTINGS_SCHEME = "settings";
	private static final String SETTINGS_AUTHORITY = "synchronize";
	public static final Uri SINCRONIZATION_URI = new Uri.Builder()
			.scheme(SETTINGS_SCHEME).authority(SETTINGS_AUTHORITY).build();

	private View viewRoot;
	private TextView txt;

	// // Database Helper
	// GarcomHelper db;

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

		txt = (TextView) viewRoot.findViewById(R.id.textView1);
		Button btnSalvar = (Button) viewRoot.findViewById(R.id.btnSynchronize);
		btnSalvar.setOnClickListener(this);

		return viewRoot;
	}

	@Override
	public void onClick(View v) {
		SincronizarGarcom();
		SincronizarProdutoGrupo();
		SincronizarProduto(); // Generics
		//
		//
		//
		// Log.d("Garcom Count", " " + dbGarcom.getGarcomCount());
		//
		// List<GarcomModel> allToDos = dbGarcom.getAllGarcom();
		// for (GarcomModel todo : allToDos) {
		// Log.d("Garcom.. ", todo.id + " .. " + todo.nome + " .. " +
		// todo.codigo);
		// }
		//
		// // GarcomModel todo1 = new GarcomModel();
		// // todo1.id = (long) 15;
		// // todo1.nome = "sera quesaddsasaddsa vai att?";
		// // db.updateGarcom(todo1);
		//
		// allToDos = dbGarcom.getAllGarcom();
		// for (GarcomModel todo : allToDos) {
		// Log.d("Garcom.. ", todo.id + " .. " + todo.nome + " .. " +
		// todo.codigo);
		// }
		//
		// dbGarcom.deleteGarcom(15);
		//
		// allToDos = dbGarcom.getAllGarcom();
		// for (GarcomModel todo : allToDos) {
		// Log.d("Garcom.. ", todo.id + " .. " + todo.nome + " .. " +
		// todo.codigo);
		// }
		//
		// // Don't forget to close database connection
		// dbGarcom.closeDB();

	}

	private void SincronizarGarcom() {
		GetGarcom garcom = new GetGarcom(this.getActivity());
		List<GarcomModel> garcomLista = garcom.carregarGarcom();
		txt.setText(garcomLista.get(0).nome);

		GarcomHelper dbGarcom = new GarcomHelper(this.getActivity());
		dbGarcom.createGarcom(garcomLista);

		dbGarcom.closeDB();
	}

	private void SincronizarProdutoGrupo() {
		GetProdutoGrupo produtoGrupo = new GetProdutoGrupo(this.getActivity());
		List<ProdutoGrupoModel> produtoGrupoLista = produtoGrupo
				.carregarProdutoGrupo();

		ProdutoGrupoHelper dbProdutoGrupo = new ProdutoGrupoHelper(
				this.getActivity());
		dbProdutoGrupo.createProdutoGrupo(produtoGrupoLista);

		dbProdutoGrupo.closeDB();
	}

	private void SincronizarProduto() {
		GetGenericApi<ProdutoModel> produto = new GetGenericApi<ProdutoModel>(
				this.getActivity());
		List<ProdutoModel> produtoLista = produto
				.LoadListApiFromUrl("GetProduto");

		ProdutoHelper dbProduto = new ProdutoHelper(this.getActivity());
		dbProduto.createProduto(produtoLista);

		dbProduto.closeDB();
	}
}
