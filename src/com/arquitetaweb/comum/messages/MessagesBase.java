package com.arquitetaweb.comum.messages;

import android.app.Activity;
import android.content.Context;

import com.arquitetaweb.comanda.util.KeyboardAction;

public class MessagesBase {
	public void show(Context context) {
		KeyboardAction kb = new KeyboardAction();
		//kb.hide((Activity) context);
	}
//	
//	public void show(Context context, String message) {
//		show(context);
//	}
}
