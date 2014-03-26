package com.arquitetaweb.comanda.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arquitetaweb.comanda.R;

public class LoginActivity extends Activity implements  View.OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);		
		setTitle("AComanda Login/Register");
		
		Button btn = (Button) findViewById(R.id.entrar);
		btn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(arg0.getContext(),
				MainActivity.class);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
