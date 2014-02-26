package com.arquitetaweb.comum.messages;

import android.content.Context;
import android.widget.Toast;

public class AlertaToast extends MessagesBase {

	public void show(Context context, String message) {
		super.show(context);
		Toast toast = Toast.makeText(context, message,
				Toast.LENGTH_SHORT);
		//toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
