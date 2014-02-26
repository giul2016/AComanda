package com.arquitetaweb.comanda.dados;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.Log;

import com.arquitetaweb.comanda.util.Utils;

public class PutMesa {
	
	public PutMesa() {		
	}

	public void atualizaMesa(String codigo, String mesa, Activity activity) {
		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(Utils.getUrlServico(activity)
				+ "/Api/AtualizarMesa?id=" + mesa + "&situacao=" + codigo);

		try {
			HttpResponse response = client.execute(put);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				Log.i("Sucess!", "Sucesso");
			}
		} catch (ClientProtocolException e1) {
			Log.e("Error....", e1.getMessage());
		} catch (IOException e1) {
			Log.e("Error....", e1.getMessage());
		}
	}
}
