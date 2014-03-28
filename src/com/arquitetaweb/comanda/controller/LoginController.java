package com.arquitetaweb.comanda.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.PostGenericApi;
import com.arquitetaweb.comanda.interfaces.AsyncLoginListener;
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comanda.model.UsuarioModel;

public class LoginController {

	private ProgressDialog progressDialog;
	private Activity activity;
	private String URL_API = "Device";
	private UsuarioModel objectLogin;

	public LoginController(ProgressDialog progressDialog, Activity activity) {
		this.progressDialog = progressDialog;
		this.activity = activity;
	}

	public void loginRegister(UsuarioModel objectLogin) {
		this.objectLogin = objectLogin;
		new LoginRegister(activity).execute();
	}

	private class LoginRegister extends AsyncTask<Void, Void, Pair<Boolean, String>> {
		private AsyncLoginListener callback;

		public LoginRegister(Activity activity) {
			this.callback = (AsyncLoginListener) activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage(activity.getString(R.string.efetuandoLogin));
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
