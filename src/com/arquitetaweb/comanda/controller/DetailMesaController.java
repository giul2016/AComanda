package com.arquitetaweb.comanda.controller;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.adapter.MesaAdapter;
import com.arquitetaweb.comanda.adapter.ProdutoAdapter;
import com.arquitetaweb.comanda.adapter.ProdutoGrupoAdapter;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.comum.messages.AlertaToast;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;

public class DetailMesaController {

	private ProgressDialog progressDialog;
	private Context context;
	private Fragment fragment;
	private View view;
	private String URL_API = "Mesas";
	private ListView list;

	public DetailMesaController(Fragment fragment, ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
		this.fragment = fragment;
		this.context = fragment.getActivity();
		this.view = fragment.getView();
	}

	public void sincronizarMesa() {
		new SincronizarDados().execute();
	}

	public void atualizarMesa() {
		SincronizarMesa();
	}

	private class SincronizarDados extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage("atualizando mesas...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... produtos) {
			SincronizarMesa();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	private void SincronizarMesa() {

		// Get List ProdutoGrupo from DB SQLLite
		ProdutoGrupoGenericHelper dbProdutoGrupo = new ProdutoGrupoGenericHelper(
				context);
		List<ProdutoGrupoModel> produtoGrupoList = dbProdutoGrupo.selectAll();
		dbProdutoGrupo.closeDB();

		list = (ListView) view.findViewById(R.id.listDetail);

		ProdutoGrupoAdapter adapter = new ProdutoGrupoAdapter(
				(Activity) context, produtoGrupoList);

		updateListView(list, adapter);
	}

	private void updateListView(final ListView listView,
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
								.selectAll();
						dbProdutoGrupo.closeDB();

						ProdutoAdapter adapter = new ProdutoAdapter(
								(Activity) context, produtoGrupoList);

						updateListView2(list, adapter);

					}
				});
			}
		});
	}

	private void updateListView2(final ListView listView,
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
