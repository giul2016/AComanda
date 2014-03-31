package com.arquitetaweb.comanda.activity;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.controller.LoginController;
import com.arquitetaweb.comanda.interfaces.AsyncLoginListener;
import com.arquitetaweb.comanda.model.UsuarioModel;
import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comanda.util.Validation;

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
		btn.setOnClickListener(this);

		edtEmail = (AutoCompleteTextView) findViewById(R.id.edtEmailLogin);		
		createListeners();
		autoComplete(null);
	}

	private void createListeners() {
		edtEmail.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Validation.isEmailAddress(edtEmail, true);
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		});
		edtEmail.setOnEditorActionListener(new EditText.OnEditorActionListener() {			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO ||
		                actionId == EditorInfo.IME_ACTION_DONE ||
		                event.getAction() == KeyEvent.ACTION_DOWN &&
		                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					logar();
		            return true;
		        }
		        return false;
			}
		});
	}

	private void autoComplete(String novoUsuario) {
		// Get the string array
		List<String> emails = controller.getUsuario(novoUsuario);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, emails);
		edtEmail.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		logar();
	}

	private void logar() {
		String email = edtEmail.getText().toString();
		if (Utils.isValidEmail(email)) {
			usuario = new UsuarioModel();
			usuario.email = email;
			String devId = Secure.getString(this.getContentResolver(),
					Secure.ANDROID_ID);
			usuario.deviceID = devId;
			controller.loginRegister(usuario);
		}
	}

	@Override
	public void onLoginComplete(Pair<Boolean, String> result) {
		if (result.first) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			autoComplete(edtEmail.getText().toString());
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
