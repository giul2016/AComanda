package com.arquitetaweb.comanda.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.dados.PutMesa;

public class DetailsActivity extends FragmentActivity {

	private static final String SCHEME = "settings";
	private static final String AUTHORITY = "details";
	public static final Uri URI = new Uri.Builder().scheme(SCHEME)
			.authority(AUTHORITY).build();

	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.details);

		// get the action bar
		ActionBar actionBar = getActionBar();
		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);

		final String idString = getIntent().getExtras().get("id").toString();

		final TextView id = (TextView) findViewById(R.id.teste);
		id.setText(idString);

		final Button btnLivre = (Button) findViewById(R.id.btnLivre);
		btnLivre.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idString, "1");	
			}
		});

		final Button btnOcupada = (Button) findViewById(R.id.btnCcupada);
		btnOcupada.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idString, "2");
			}
		});

		final Button btnOciosa = (Button) findViewById(R.id.btnOciosa);
		btnOciosa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {				
				atualizarMesa(idString, "7");
			}
		});
	}

	private Void atualizarMesa(String idMesa, String situacao) {
		PutMesa putMesa = new PutMesa(DetailsActivity.this);
		putMesa.atualizaMesa(idMesa, situacao);
		return null;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

}
