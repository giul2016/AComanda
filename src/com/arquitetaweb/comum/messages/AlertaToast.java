package com.arquitetaweb.comum.messages;

import android.content.Context;
import android.widget.Toast;

public class AlertaToast extends MessagesBase {	

	public void show(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		super.show(context);
	}
}
