package com.arquitetaweb.comanda.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.SettingsModel;
import com.arquitetaweb.comanda.util.ReadSaveConfiguracoes;
import com.arquitetaweb.comum.messages.AlertaToast;
import com.arquitetaweb.comum.messages.SucessoToast;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;
import com.arquitetaweb.helper.sqllite.SettingsGenericHelper;

public class SettingsFragment extends Fragment implements View.OnClickListener {
	public static final String TAG = SettingsFragment.class.getSimpleName();
	private static final String SETTINGS_SCHEME = "settings";
	private static final String SETTINGS_AUTHORITY = "general";
	public static final Uri GENERAL_SETTINGS_URI = new Uri.Builder()
			.scheme(SETTINGS_SCHEME).authority(SETTINGS_AUTHORITY).build();

	private View viewRoot;
	private AutoCompleteTextView urlServico;
	private EditText portaServico;

	private ReadSaveConfiguracoes configuracoes;
	private SettingsModel configModel;

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
		viewRoot = inflater.inflate(R.layout.fragment_settings, container,
				false);

		EditText deviceId = (EditText) viewRoot.findViewById(R.id.edtDeviceId);
		String devId = Secure.getString(viewRoot.getContext()
				.getContentResolver(), Secure.ANDROID_ID);
		deviceId.setText(devId);

		urlServico = (AutoCompleteTextView) viewRoot
				.findViewById(R.id.autoCompleteUrlServico);
		portaServico = (EditText) viewRoot.findViewById(R.id.edtPortaServico);

		initSettings();
		initAutoComplete();

		Button btnSalvar = (Button) viewRoot.findViewById(R.id.btnSalvarConfig);
		btnSalvar.setOnClickListener(this);

		return viewRoot;
	}

	private void initAutoComplete() {
		// Get the string array
		String[] urlsServico = getResources().getStringArray(
				R.array.config_urls_array);
		// Create the adapter and set it to the AutoCompleteTextView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1,
				urlsServico);
		urlServico.setAdapter(adapter);
	}

	private void initSettings() {		
		SettingsGenericHelper dbSettings = new SettingsGenericHelper(
				this.getActivity());
		configModel = dbSettings.selectOne();	
		
		urlServico.setText(configModel.getUrlServico());
		portaServico.setText(configModel.getPortaServico().toString());
	}

	@Override
	public void onClick(View v) {
		try {
			SettingsGenericHelper dbSettings = new SettingsGenericHelper(
					this.getActivity());
			
			configModel = new SettingsModel();

			configModel.setUrlServico(urlServico.getText().toString());
			configModel.setPortaServico(Integer.parseInt(portaServico.getText()
					.toString()));

			//configuracoes.saveData(configModel);
			List<SettingsModel> saveObj = new ArrayList<SettingsModel>();  
			saveObj.add(configModel);
			
			dbSettings.sincronizar(saveObj);
			
			new SucessoToast().show(this.getActivity());
		} catch (Exception e) {
			new AlertaToast().show(this.getActivity(),
					"Erro ao Salvar:\n" + e.getMessage());
		}
	}
}
