package com.arquitetaweb.comanda.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.controller.SincronizationController;

public class SincronizationFragment extends Fragment implements
		View.OnClickListener {

	private static final String SETTINGS_SCHEME = "settings";
	private static final String SETTINGS_AUTHORITY = "synchronize";
	private ProgressDialog progressDialog;
	private View viewRoot;

	private SincronizationController controller;

	public static final String TAG = SincronizationFragment.class
			.getSimpleName();
	public static final Uri SINCRONIZATION_URI = new Uri.Builder()
			.scheme(SETTINGS_SCHEME).authority(SETTINGS_AUTHORITY).build();

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

		Button btnSalvar = (Button) viewRoot.findViewById(R.id.btnSynchronize);
		btnSalvar.setOnClickListener(this);

		return viewRoot;
	}

	@Override
	public void onClick(View v) {
		progressDialog = new ProgressDialog(this.getActivity());		
		controller = new SincronizationController(this, progressDialog);
		controller.updateTextViewSincSucess();
		
		controller.sincronizarDados();
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
