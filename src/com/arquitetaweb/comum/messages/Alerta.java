package com.arquitetaweb.comum.messages;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class Alerta extends MessagesBase {	
	public void show(Context context, String title, String message) {
		super.show(context);
		Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(!title.equals("") ? title : "Erro - ArquitetaWeb");
		alert.setMessage(message);
		alert.setPositiveButton("OK",null);
		alert.show();			
    }		
}
