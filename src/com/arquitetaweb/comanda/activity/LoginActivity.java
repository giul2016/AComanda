package com.arquitetaweb.comanda.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.controller.LoginController;
import com.arquitetaweb.comanda.interfaces.AsyncLoginListener;
import com.arquitetaweb.comanda.model.UsuarioModel;

public class LoginActivity extends Activity implements View.OnClickListener,
		AsyncLoginListener {

	private LoginController controller;
	private AutoCompleteTextView edtEmail;
	private ProgressDialog progressDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle("AComanda Login/Register");

		progressDialog = new ProgressDialog(this);
		controller = new LoginController(progressDialog, this);

		Button btn = (Button) findViewById(R.id.entrar);
		edtEmail = (AutoCompleteTextView) findViewById(R.id.edtEmailLogin);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		UsuarioModel usuario = new UsuarioModel();
		usuario.email = edtEmail.getText().toString();
		String devId = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		usuario.deviceID = devId;
		controller.loginRegister(usuario);
	}

	@Override
	public void onLoginComplete(Pair<Boolean, String> result) {
		Toast.makeText(getApplicationContext(), result.second,
				Toast.LENGTH_SHORT).show();
		if ((Boolean) result.first == true) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

	}
}
