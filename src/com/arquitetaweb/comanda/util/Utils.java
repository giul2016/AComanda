package com.arquitetaweb.comanda.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.arquitetaweb.comanda.model.SettingsModel;
import com.arquitetaweb.helper.sqllite.SettingsGenericHelper;

public class Utils {

	public final static boolean isConnected(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connectivityManager
				.getActiveNetworkInfo();

		boolean ativo = (networkInfo != null && networkInfo.isConnected());
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

	public final static boolean isValidEmail(CharSequence target) {
		String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    if (TextUtils.isEmpty(target)) {
	        return false;
	    } else {
	        //return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    	return Pattern.matches(EMAIL_REGEX, target);
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
		return MontarUrl(context);
	}

//	private static boolean isEmulator() {
//		String product = Build.PRODUCT;
//		boolean isEmulator = false;
//		if (product != null) {
//			isEmulator = product.equals("sdk") || product.contains("_sdk")
//					|| product.contains("sdk_");
//		}
//		return isEmulator;
//	}

	private static String MontarUrl(Context context) {
		SettingsGenericHelper dbSettings = new SettingsGenericHelper(
				context);		
		SettingsModel configModel = new SettingsModel();
		if (dbSettings.getCount() > 0)
			configModel = dbSettings.selectOne();	
		dbSettings.closeDB();
		
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(configModel.getUrlServico());
		sb.append(":");
		sb.append(configModel.getPortaServico().toString());
		return sb.toString();
	}
}