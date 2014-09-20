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
import com.arquitetaweb.comanda.model.MesaModel;

public class TestePutActivity extends FragmentActivity {
 
	private ProgressDialog progressDialog;	
	
	private static final String SCHEME = "settings";
	private static final String AUTHORITY = "details";
	
	public static final Uri URI = new Uri.Builder().scheme(SCHEME)
			.authority(AUTHORITY).build();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.teste_put);

		// get the action bar
		ActionBar actionBar = getActionBar();
		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);

		MesaModel mesa = (MesaModel) getIntent().getSerializableExtra("mesa");
		
		final String idMesa = Long.toString(mesa.id);

		TextView txtIdMesa = (TextView) findViewById(R.id.idMesa_teste);
		txtIdMesa.setText("Id Mesa: " + idMesa);

		TextView txtNumeroMesa = (TextView) findViewById(R.id.idNumeroMesa_teste);
		txtNumeroMesa.setText("Número Mesa: " + mesa.numero_mesa);

		TextView txtSituacaoMesa = (TextView) findViewById(R.id.idSituacao_teste);
		txtSituacaoMesa.setText("Situação Mesa: " + mesa.situacao);

		final Button btnLivre = (Button) findViewById(R.id.btnLivre_teste);
		btnLivre.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "1");
			}
		});

		final Button btnOcupada = (Button) findViewById(R.id.btnOcupada_teste);
		btnOcupada.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "2");
			}
		});

		final Button btnEmConta = (Button) findViewById(R.id.btnEmConta_teste);
		btnEmConta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "3");
			}
		});
		
		final Button btnLimpar = (Button) findViewById(R.id.btnLimpar_teste);
		btnLimpar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "4");
			}
		});
		
		final Button btnAgrupada = (Button) findViewById(R.id.btnAgrupada_teste);
		btnAgrupada.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "5");
			}
		});
		
		final Button btnReservada = (Button) findViewById(R.id.btnReservada_teste);
		btnReservada.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "6");
			}
		});
				
		final Button btnOciosa = (Button) findViewById(R.id.btnOciosa_teste);
		btnOciosa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "7");
			}
		});
		
		final Button btnIndefinido = (Button) findViewById(R.id.btnIndefinido_teste);
		btnIndefinido.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				atualizarMesa(idMesa, "8");
			}
		});
	}

	private Void atualizarMesa(String idMesa, String situacao) {
		progressDialog = new ProgressDialog(this); 
		PutMesa putMesa = new PutMesa(TestePutActivity.this,progressDialog);
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
	
	@Override
	protected void onDestroy() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.onDestroy();
	}

}
