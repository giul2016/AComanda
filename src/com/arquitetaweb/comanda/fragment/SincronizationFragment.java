package com.arquitetaweb.comanda.fragment;

import java.util.List;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.GetGarcom;
import com.arquitetaweb.comanda.model.GarcomModel;

public class SincronizationFragment extends Fragment implements
		View.OnClickListener {
	public static final String TAG = SincronizationFragment.class
			.getSimpleName();
	private static final String SETTINGS_SCHEME = "settings";
	private static final String SETTINGS_AUTHORITY = "synchronize";
	public static final Uri SINCRONIZATION_URI = new Uri.Builder()
			.scheme(SETTINGS_SCHEME).authority(SETTINGS_AUTHORITY).build();

	private View viewRoot;
	private TextView txt;
			
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
		
		txt = (TextView) viewRoot.findViewById(R.id.textView1);
		Button btnSalvar = (Button) viewRoot.findViewById(R.id.btnSynchronize);
		btnSalvar.setOnClickListener(this);
		
		return viewRoot;
	}

	@Override
	public void onClick(View v) {
		GetGarcom garcom = new GetGarcom(this.getActivity());
		List<GarcomModel> teste = garcom.carregarGarcom();
		txt.setText(teste.get(0).nome);
	}
}
