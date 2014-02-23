package com.arquitetaweb.comum.messages;

import android.content.Context;
import android.widget.Toast;

import com.arquitetaweb.comanda.R;

public class SucessoToast extends MessagesBase {		
	
	public void show(Context context) {
		Toast.makeText(context, R.string.msgSalvoSucesso,
				Toast.LENGTH_LONG).show();
		super.show(context);
	}
}
