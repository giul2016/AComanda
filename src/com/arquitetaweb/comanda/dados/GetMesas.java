package com.arquitetaweb.comanda.dados;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.activity.DetailsActivity;
import com.arquitetaweb.comanda.adapter.MesaAdapter;
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comanda.util.JSONParser;
import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;
import com.google.gson.Gson;

public class GetMesas {

	protected Context context;
	protected Fragment fragment;
	protected View view;

	GridView mesas;
	public static MesaAdapter adapter;

	public GetMesas(Fragment fragment) {
		this.fragment = fragment;
		this.context = fragment.getActivity();
		this.view = fragment.getView();
	}

	public void carregarMesas() {
		new CarregaMesa().execute();
	}

	private class CarregaMesa extends AsyncTask<String, Void, Boolean> {
		protected ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("atualizando mapa das mesas...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if (Utils.isConnected(context)) {
				String urlApi = Utils.getUrlServico(context)
						+ "/Api/SituacaoMesas";
				JSONParser jParser = new JSONParser();
				final String jsonMesas = jParser.getJSONFromApi(urlApi);
				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						List<MesaModel> mesasLista = new ArrayList<MesaModel>();
						Gson gson = new Gson();
						mesasLista = gson.fromJson(jsonMesas,
								new MesaModel().getType()); // converte pra
															// ArrayList de
															// mesas

						mesas = (GridView) view.findViewById(R.id.list);
						adapter = new MesaAdapter((Activity) context,
								mesasLista);
						mesas.setAdapter(adapter);

						// Click event for single list row
						mesas.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {

								abrirDetalhes(view, position);
							}

							private void abrirDetalhes(View view, Integer idMesa) {
								Intent intent = new Intent(view.getContext(),
										DetailsActivity.class);

								MesaModel mesaObj = adapter.getItem(idMesa);
								String mesaGson = new Gson().toJson(mesaObj);
								intent.putExtra("mesa", mesaGson);
								fragment.startActivityForResult(intent, 100);
							}
						});
					}
				});
			} else {
				errorConnectServer();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	public void reload() {
		if (Utils.isConnected(context)) {
			getMesas();
		} else {
			errorConnectServer();
		}
	}

	private void getMesas() {
		String urlApi = Utils.getUrlServico(context) + "/Api/SituacaoMesas";
		JSONParser jParser = new JSONParser();
		final String jsonMesas = jParser.getJSONFromApi(urlApi);
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				List<MesaModel> mesasLista = new ArrayList<MesaModel>();
				Gson gson = new Gson();
				mesasLista = gson.fromJson(jsonMesas, new MesaModel().getType()); // converte
																					// pra
																					// ArrayList
																					// de
																					// mesas

				mesas = (GridView) view.findViewById(R.id.list);
				adapter = new MesaAdapter((Activity) context, mesasLista);
				mesas.setAdapter(adapter);

				// Click event for single list row
				mesas.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						abrirDetalhes(view, position);
					}

					private void abrirDetalhes(View view, Integer idMesa) {
						Intent intent = new Intent(view.getContext(),
								DetailsActivity.class);

						MesaModel mesaObj = adapter.getItem(idMesa);
						String mesaGson = new Gson().toJson(mesaObj);
						intent.putExtra("mesa", mesaGson);
						fragment.startActivityForResult(intent, 100);
					}
				});
			}
		});
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
