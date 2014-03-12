package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.activity.DetailMesaActivity;
import com.arquitetaweb.comanda.activity.TestePutActivity;
import com.arquitetaweb.comanda.adapter.MesaAdapter;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.model.MesaModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainController {

	private ProgressDialog progressDialog;
	private Context context;
	private Fragment fragment;
	private View view;
	private String URL_API = "Mesas";

	public MainController(Fragment fragment, ProgressDialog progressDialog) {
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
		
		GetGenericApi<MesaModel> mesaApi = new GetGenericApi<MesaModel>(
				context);
		
		List<MesaModel> mesaList = mesaApi.LoadListApiFromUrl(URL_API,
				new TypeToken<ArrayList<MesaModel>>() {
				}.getType());

		GridView mesas = (GridView) view.findViewById(R.id.mapa_mesa);
		MesaAdapter adapter = new MesaAdapter((Activity) context, mesaList);

		updateListView(mesas, fragment, adapter);
	}

	private void updateListView(final GridView mesas, final Fragment fragment,
			final MesaAdapter adapter) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				mesas.setAdapter(adapter);

				mesas.setOnItemLongClickListener(new OnItemLongClickListener() {
					
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						abrirDetalhes(view, arg2);
						return true;
					}
					
					private void abrirDetalhes(View view, Integer idMesa) {
						Intent intent = new Intent(view.getContext(),
								TestePutActivity.class);

						MesaModel mesaObj = adapter.getItem(idMesa);						
						String mesaGson = new Gson().toJson(mesaObj);
						intent.putExtra("mesa", mesaGson);
						fragment.startActivityForResult(intent, 100);
					}					
				});
				
				// Click event for single list row
				mesas.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						abrirDetalhes(view, position);
					}

					private void abrirDetalhes(View view, Integer idMesa) {
						Intent intent = new Intent(view.getContext(),
								DetailMesaActivity.class);
						MesaModel mesaObj = adapter.getItem(idMesa);						
						String mesaGson = new Gson().toJson(mesaObj);
						intent.putExtra("mesa", mesaGson);
						fragment.startActivityForResult(intent, 100);
						
//						Intent intent = new Intent(view.getContext(),
//								DetailsActivity.class);
//
//						MesaModel mesaObj = adapter.getItem(idMesa);						
//						String mesaGson = new Gson().toJson(mesaObj);
//						intent.putExtra("mesa", mesaGson);
//						fragment.startActivityForResult(intent, 100);
					}
				});
			}
		});
	}

	public void atualizarMesa(MenuItem refreshMenuItem) {
		new AtualizarMesa(refreshMenuItem).execute();
	}

	/**
	 * Async task to load the data from server
	 * **/
	private class AtualizarMesa extends AsyncTask<Void, Void, Void> {

		private MenuItem refreshMenuItem;

		public AtualizarMesa(MenuItem refreshMenuItem) {
			this.refreshMenuItem = refreshMenuItem;
		}

		@Override
		protected void onPreExecute() {
			refreshMenuItem.setActionView(R.layout.action_progressbar);
			refreshMenuItem.expandActionView();
		}

		@Override
		protected Void doInBackground(Void... params) {
			atualizarMesa();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			refreshMenuItem.collapseActionView();
			// remove the progress bar view
			refreshMenuItem.setActionView(null);
		}
	};
}
