package com.arquitetaweb.comanda.dados;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

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
	String json = null;

	GridView mesas;
	public static MesaAdapter adapter;

	ProgressDialog progressDialog;

	public GetMesas(Fragment fragment) {
		this.fragment = fragment;
		this.context = fragment.getActivity();
		this.view = fragment.getView();
	}

	public void carregarDadosJson(final Boolean showMsgRefresh) {

		if (showMsgRefresh) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("atualizando mapa das mesas...");
			progressDialog.show();
		}

		Thread thread = new Thread() {
			public void run() {
				if (Utils.isConnected(context)) {
					String url = Utils.getUrlServico(context)
							+ "/Api/SituacaoMesas";
					JSONParser jParser = new JSONParser();
					json = jParser.getJSONFromUrlString(url);

					((Activity) context).runOnUiThread(new Runnable() {
						public void run() {
							List<MesaModel> mesasLista = new ArrayList<MesaModel>();
							Gson gson = new Gson();
							mesasLista = gson.fromJson(json,
									new MesaModel().getType());

							mesas = (GridView) view.findViewById(R.id.list);
							adapter = new MesaAdapter((Activity) context,
									mesasLista);
							mesas.setAdapter(adapter);

							// Click event for single list row
							mesas.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {

									TextView idItem = (TextView) view
											.findViewById(R.id.idItem);

									Bundle bun = new Bundle();
									bun.putString("id",
											(String) idItem.getText());

									abrirDetalhes(view, bun);
								}

								private void abrirDetalhes(View view, Bundle bun) {
									Intent intent = new Intent(view
											.getContext(),
											DetailsActivity.class);
									intent.putExtras(bun);
									fragment.startActivityForResult(intent, 100);
								}
							});
							if (showMsgRefresh)
								progressDialog.dismiss();
						}
					});

				} else {
					((Activity) context).runOnUiThread(new Runnable() {
						public void run() {
							if (showMsgRefresh)
								progressDialog.dismiss();
							new Alerta().show(context, "",
									"Não Foi Localizado o Servidor!"
											+ "\nCausas:" + "\nConexão OK?"
											+ "\nServidor correto?");
						}
					});
				}
			}
		};
		thread.start();
	}
}
