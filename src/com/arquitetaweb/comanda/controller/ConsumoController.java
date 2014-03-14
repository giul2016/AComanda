package com.arquitetaweb.comanda.controller;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.arquitetaweb.comanda.adapter.ProdutoAdapter;
import com.arquitetaweb.comanda.adapter.ProdutoGrupoAdapter;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.comum.component.ListViewCustom;
import com.arquitetaweb.comum.messages.AlertaToast;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;

public class ConsumoController {

	private Context context;
	private ListViewCustom list;
	
	public ConsumoController(Context context) {
		this.context = context;
	}

	public void sincronizarMesa() {
		//new SincronizarDados().execute();
		SincronizarMesa();
	}

	
	public List<ProdutoGrupoModel> sincron() {

		// Get List ProdutoGrupo from DB SQLLite
		ProdutoGrupoGenericHelper dbProdutoGrupo = new ProdutoGrupoGenericHelper(
				context);
		List<ProdutoGrupoModel> produtoGrupoList = dbProdutoGrupo.selectAll();
		dbProdutoGrupo.closeDB();

		return produtoGrupoList;
	}
	
	private void SincronizarMesa() {

		// Get List ProdutoGrupo from DB SQLLite
		ProdutoGrupoGenericHelper dbProdutoGrupo = new ProdutoGrupoGenericHelper(
				context);
		List<ProdutoGrupoModel> produtoGrupoList = dbProdutoGrupo.selectAll();
		dbProdutoGrupo.closeDB();

		//list = (ListView) view.findViewById(R.id.listDetail);

		ProdutoGrupoAdapter adapter = new ProdutoGrupoAdapter(
				(Activity) context, produtoGrupoList);

		updateListView(list, adapter);
	}


	private void updateListView(final ListViewCustom listView,
			final ProdutoGrupoAdapter adapter) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				listView.setAdapter(adapter);

				// Click event for single list row
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						abrirDetalhes(view, position);
					}

					private void abrirDetalhes(View view, Integer id) {
						ProdutoGrupoModel produtoGrupoObj = adapter.getItem(id);
						new AlertaToast().show(view.getContext(),
								produtoGrupoObj.codigo + " :: " + id.toString());
						// String mesaGson = new Gson().toJson(produtoGrupoObj);
						// intent.putExtra("mesa", mesaGson);
						// fragment.startActivityForResult(intent, 100);

						// Get List ProdutoGrupo from DB SQLLite
						ProdutoGenericHelper dbProdutoGrupo = new ProdutoGenericHelper(
								context);
						List<ProdutoModel> produtoGrupoList = dbProdutoGrupo
								.selectWhere("produtoGrupoId = " + id.toString());
						dbProdutoGrupo.closeDB();

						ProdutoAdapter adapter = new ProdutoAdapter(
								(Activity) context, produtoGrupoList);

						updateListView2(list, adapter);
					}
				});
			}
		});
	}

	private void updateListView2(final ListViewCustom listView,
			final ProdutoAdapter adapter) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				listView.setAdapter(adapter);

				// Click event for single list row
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						abrirDetalhes(view, position);
					}

					private void abrirDetalhes(View view, Integer id) {
						ProdutoModel produtoGrupoObj = adapter.getItem(id);
						new AlertaToast().show(view.getContext(),
								produtoGrupoObj.codigo + " :: " + id.toString());
						// String mesaGson = new Gson().toJson(produtoGrupoObj);
						// intent.putExtra("mesa", mesaGson);
						// fragment.startActivityForResult(intent, 100);
					}
				});
			}
		});
	}
}
