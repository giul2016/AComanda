package com.arquitetaweb.comanda.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.controller.DetailMesaController;
import com.arquitetaweb.comanda.model.MesaModel;
import com.google.gson.Gson;

public class DetailsFragment extends Fragment {
	
	private ProgressDialog progressDialog;
	private DetailMesaController controller;
	
	public static final String TAG = AboutFragment.class.getSimpleName();
	private static final String SETTINGS_SCHEME = "settings";
	private static final String SETTINGS_AUTHORITY = "details";
	public static final Uri DETAIL_URI = new Uri.Builder()
			.scheme(SETTINGS_SCHEME).authority(SETTINGS_AUTHORITY).build();

	private View viewRoot;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewRoot = inflater.inflate(R.layout.mesa_details, container, false);
		return viewRoot;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Gson gson = new Gson();
		MesaModel mesa = gson.fromJson(getActivity().getIntent().getStringExtra("mesa"),
				MesaModel.class); // converte pra ArrayList de mesas
		
//		MesaModel mesa = gson.fromJson(this.getIntent().getStringExtra("mesa"),
//				MesaModel.class); // converte pra ArrayList de mesas
		
		progressDialog = new ProgressDialog(this.getActivity());

		controller = new DetailMesaController(this, progressDialog);
		controller.sincronizarMesa();
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
