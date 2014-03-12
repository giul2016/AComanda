package com.arquitetaweb.comanda.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.activity.DetailMesaActivity;
import com.arquitetaweb.comanda.adapter.MesaAdapter;
import com.arquitetaweb.comanda.controller.MainController;
import com.arquitetaweb.comanda.dados.GetGenericApi;
import com.arquitetaweb.comanda.model.MesaModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainFragment extends Fragment {

	private ProgressDialog progressDialog;
	private MainController controller;

	private static final String ABOUT_SCHEME = "category";
	private static final String ABOUT_AUTHORITY = "mesas";

	public static final String TAG = MainFragment.class.getSimpleName();
	public static final Uri MESAS_URI = new Uri.Builder().scheme(ABOUT_SCHEME)
			.authority(ABOUT_AUTHORITY).build();

	public MainFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.mesa_lista, container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		progressDialog = new ProgressDialog(this.getActivity());

		controller = new MainController(this, progressDialog);
		controller.sincronizarMesa();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Activity.RESULT_OK == resultCode) {
			controller.sincronizarMesa();
		}
	}

	@Override
	public void onDestroy() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.onDestroy();
	}

	protected class SincronizarDados extends AsyncTask<Void, Void, Void> {

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
		String URL_API = "GetMesas";
		GetGenericApi<MesaModel> garcomApi = new GetGenericApi<MesaModel>(
				this.getActivity());
		List<MesaModel> garcomList = garcomApi.LoadListApiFromUrl(URL_API,
				new TypeToken<ArrayList<MesaModel>>() {
				}.getType());

		GridView mesas = (GridView) this.getView().findViewById(R.id.mapa_mesa);
		MesaAdapter adapter = new MesaAdapter(this.getActivity(), garcomList);

		updateListView(mesas, this, adapter);
	}

	private void updateListView(final GridView mesas, final Fragment fragment,
			final MesaAdapter adapter) {
		this.getActivity().runOnUiThread(new Runnable() {
			public void run() {
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
								DetailMesaActivity.class);

						MesaModel mesaObj = adapter.getItem(idMesa);
						String mesaGson = new Gson().toJson(mesaObj);
						intent.putExtra("mesa", mesaGson);
						fragment.startActivityForResult(intent, 100);
					}
				});
			}
		});
	}
}
