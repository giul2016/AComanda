package com.arquitetaweb.comanda.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.GetMesas;

public class MainFragment extends Fragment {
	public static final String TAG = MainFragment.class.getSimpleName();
	private static final String ABOUT_SCHEME = "category";
	private static final String ABOUT_AUTHORITY = "mesas";
	public static final Uri MESAS_URI = new Uri.Builder().scheme(ABOUT_SCHEME)
			.authority(ABOUT_AUTHORITY).build();

	// private Context context;
	GetMesas getMesas;

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
		getMesas = new GetMesas(this);
		getMesas.carregarDadosJson(true);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Activity.RESULT_OK == resultCode) {
			getMesas.carregarDadosJson(true);
		}
	}
}
