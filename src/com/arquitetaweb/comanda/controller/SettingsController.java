package com.arquitetaweb.comanda.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Pair;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.PostGenericApi;
import com.arquitetaweb.comanda.interfaces.AsyncLoginListener;
import com.arquitetaweb.comanda.model.UsuarioModel;
import com.arquitetaweb.helper.sqllite.UsuarioGenericHelper;

public class SettingsController {

	private ProgressDialog progressDialog;
	private Activity activity;
	private String URL_API = "Device";
	private UsuarioModel objectLogin;

	public SettingsController(ProgressDialog progressDialog, Activity activity) {
		this.progressDialog = progressDialog;
		this.activity = activity;
	}

	public List<String> getUsuario(String novoUsuario) {
		UsuarioGenericHelper dbDevice = new UsuarioGenericHelper(activity);
		List<UsuarioModel> usuarioList = dbDevice.selectAll();

		List<String> emails = new ArrayList<String>();
		if (novoUsuario != null)
			emails.add(novoUsuario);
		for (UsuarioModel usuarioModel : usuarioList) {
			emails.add(usuarioModel.email);
		}

		HashSet<String> hashSet = new HashSet<String>(emails); // remove duplicates
		emails = new ArrayList<String>(hashSet);

		if (novoUsuario != null) {
			usuarioList = new ArrayList<UsuarioModel>();
			for (String email : emails) {
				UsuarioModel model = new UsuarioModel();
				model.email = email;
				usuarioList.add(model);
			}

			dbDevice.sincronizar(usuarioList);
			dbDevice.closeDB();
		}

		return emails;
	}

	public void loginRegister(UsuarioModel objectLogin) {
		this.objectLogin = objectLogin;
		new LoginRegister(activity).execute();
	}

	private class LoginRegister extends
			AsyncTask<Void, Void, Pair<Boolean, String>> {
		private AsyncLoginListener callback;

		public LoginRegister(Activity activity) {
			this.callback = (AsyncLoginListener) activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage(activity
					.getString(R.string.efetuandoLogin));
			progressDialog.show();
		}

		@Override
		protected Pair<Boolean, String> doInBackground(Void... params) {
			return fecharConta();
		}

		@Override
		protected void onPostExecute(Pair<Boolean, String> result) {
			super.onPostExecute(result);
			callback.onLoginComplete(result);
			progressDialog.dismiss();
		}

		private Pair<Boolean, String> fecharConta() {
			PostGenericApi<UsuarioModel> api = new PostGenericApi<UsuarioModel>(
					activity);
			return api.sendDataReturn(URL_API, objectLogin);
		}
	}

}
