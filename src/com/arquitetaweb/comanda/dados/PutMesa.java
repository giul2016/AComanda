package com.arquitetaweb.comanda.dados;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;

public class PutMesa {

	protected Activity activity;

	public PutMesa(Activity activity) {
		this.activity = activity;
	}

	public void atualizaMesa(String mesa, String situacao) {
		new AtualizaMesa().execute(mesa, situacao);
	}

	private void errorConnectServer() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				new Alerta().show(activity, "",
						"Não Foi Localizado o Servidor!");
			}
		});
	}

	private class AtualizaMesa extends AsyncTask<String, Void, Boolean> {
		protected ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(activity, AlertDialog.THEME_HOLO_DARK);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("enviando dados...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if (Utils.isConnected(activity)) {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPut put = new HttpPut(Utils.getUrlServico(activity)
							+ "/Api/AtualizarMesa?id=" + params[0]
							+ "&situacao=" + params[1]);

					HttpResponse response = client.execute(put);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						Log.i("Sucess!", "Sucesso");
					}
				} catch (ClientProtocolException e1) {
					Log.e("ClientProtocolException....", e1.getMessage());
				} catch (IOException e1) {
					Log.e("IOException....", e1.getMessage());
				}
			} else {
				errorConnectServer();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {			
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result) {
				activity.setResult(Activity.RESULT_OK);
				activity.finish();				
			}			
		}
	}
}
