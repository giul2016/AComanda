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
		super.onActivityCreated(savedInstanceState);
		progressDialog = new ProgressDialog(this.getActivity());
		controller = new MainController(this, progressDialog);
		controller.sincronizarMesa();		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (Activity.RESULT_OK == resultCode) {
//			controller.sincronizarMesa();
//		}
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
