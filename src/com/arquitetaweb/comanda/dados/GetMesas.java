package com.arquitetaweb.comanda.dados;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.activity.DetailsActivity;
import com.arquitetaweb.comanda.adapter.MesaAdapter;
import com.arquitetaweb.comanda.util.JSONParser;
import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;

public class GetMesas {

	protected Context context;	
	protected Fragment fragment;
	JSONArray json = null; 

	// JSON Keys
	public static final String KEY_ID = "Id";
	public static final String KEY_NUMEROMESA = "NumeroMesa";
	public static final String KEY_CODIGOEXTERNO = "CodigoExterno";
	public static final String KEY_SITUACAO = "Situacao";	
	GridView mesas;
	public static MesaAdapter adapter;
	Handler handler;
	ProgressDialog progressDialog;
	
	public GetMesas(Fragment fragment) {
		this.context = fragment.getActivity();
		this.fragment = fragment;
	}
	
	public void carregarDadosJson() {		

		handler = new Handler();

		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("atualizando mapa das mesas...");
		progressDialog.show();

		Thread thread = new Thread() {
			public void run() {
				if (Utils.isConnected(context)) {
					String url = Utils.getUrlServico(context)+"/Api/SituacaoMesas";
					JSONParser jParser = new JSONParser();
					json = jParser.getJSONFromUrl(url);	
					
					((Activity) context).runOnUiThread(new Runnable() {
						public void run() {
							ArrayList<HashMap<String, String>> mesasLista = new ArrayList<HashMap<String, String>>();
							for (int i = 0; i < json.length(); i++) {
								try {
									JSONObject c = json.getJSONObject(i);
	
									HashMap<String, String> map = new HashMap<String, String>();
	
									map.put(KEY_ID, c.getString(KEY_ID));
									map.put(KEY_NUMEROMESA, c.getString(KEY_NUMEROMESA));
									map.put(KEY_CODIGOEXTERNO, c.getString(KEY_CODIGOEXTERNO));
									map.put(KEY_SITUACAO, c.getString(KEY_SITUACAO));
	
									mesasLista.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
	
							mesas = (GridView) fragment.getView().findViewById(R.id.list);
							adapter = new MesaAdapter((Activity) context, mesasLista);						
							mesas.setAdapter(adapter);												
	
							// Click event for single list row
							mesas.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
																	
									TextView idItem =(TextView) view.findViewById(R.id.idItem);
									
									Bundle bun = new Bundle();								
									bun.putString("id", (String)idItem.getText());
									
									abrirDetalhes(view, bun);							
								}
	
								private void abrirDetalhes(View view, Bundle bun) {
									Intent intent = new Intent(view.getContext(), DetailsActivity.class);								
									intent.putExtras(bun);
									fragment.startActivityForResult(intent, 100);
								}												
							});
							progressDialog.dismiss();
						}
					});	
					
				} else {				
					((Activity) context).runOnUiThread(new Runnable() {
	                    public void run() {
	                    	progressDialog.dismiss();	                    	
	                    	new Alerta().show(context, "",  "Não Foi Localizado o Servidor!" +
	                    			"\nCausas:" +
	                    			"\nConexão OK?" +
	                    			"\nServidor correto?");	                    	
	                    }
	                });																		
				}
			}
		};
		thread.start();
	}	 
}
