package com.arquitetaweb.comanda.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.arquitetaweb.comanda.model.ConfiguracoesModel;

public class Utils {

	public final static boolean isConnected(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connectivityManager
				.getActiveNetworkInfo();

		boolean ativo = networkInfo != null && networkInfo.isConnected();
		if (ativo)
			return isServerActive(context);
		return false;
	}

	private final static boolean isServerActive(Context context) {
		try {
			URL myUrl = new URL(getUrlServico(context));
			URLConnection connection = myUrl.openConnection();
			connection.setConnectTimeout(3000);
			connection.connect();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String getUrlServico(Context context) {
		String product = Build.PRODUCT;
		boolean isEmulator = false;
		if (product != null) {
			isEmulator = product.equals("sdk") || product.contains("_sdk")
					|| product.contains("sdk_");
		}
		if (!isEmulator) {
			return MontarUrl(context);
		}
		return "http://10.0.2.2:81";
	}

	private static String MontarUrl(Context context) {
		
		ReadSaveConfiguracoes configuracoes = new ReadSaveConfiguracoes(context);;
		ConfiguracoesModel configModel = configuracoes.getData();		
		
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(configModel.getUrlServico());
		sb.append(":");
		sb.append(configModel.getPortaServico().toString());
		return sb.toString();
	}
}