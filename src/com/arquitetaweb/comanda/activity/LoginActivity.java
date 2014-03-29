package com.arquitetaweb.comanda.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.controller.LoginController;
import com.arquitetaweb.comanda.interfaces.AsyncLoginListener;
import com.arquitetaweb.comanda.model.UsuarioModel;

public class LoginActivity extends Activity implements View.OnClickListener,
		AsyncLoginListener {

	private LoginController controller;
	private AutoCompleteTextView edtEmail;
	private ProgressDialog progressDialog;
	private UsuarioModel usuario;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle("AComanda Login/Cadastro");

		progressDialog = new ProgressDialog(this);
		controller = new LoginController(progressDialog, this);

		Button btn = (Button) findViewById(R.id.entrar);
		edtEmail = (AutoCompleteTextView) findViewById(R.id.edtEmailLogin);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		usuario = new UsuarioModel();
		usuario.email = edtEmail.getText().toString();
		String devId = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		usuario.deviceID = devId;
		controller.loginRegister(usuario);
	}

	@Override
	public void onLoginComplete(Pair<Boolean, String> result) {
		if (result.first) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			notAuthorized(result.second);
		}
	}

	private void notAuthorized(String retorno) {
		if (retorno.toLowerCase(Locale.getDefault()).contains(
				"user not authorized")) {
			showDialog("Ops, usuário não autorizado\nenviamos um email de confirmação para "
					+ usuario.email
					+ "\nconfirme o email e tente logar novamente, obrigado.");
		} else if (retorno.toLowerCase(Locale.getDefault()).contains(
				"user inserted")) {
			showDialog("Usuário foi cadastrado com sucesso, confirme o email "
					+ usuario.email + " e tente logar novamente, obrigado.");
		}
	}

	private void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setMessage(msg).setCancelable(false)
				.setTitle("Login/Register")
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

}
