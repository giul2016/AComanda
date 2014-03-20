package com.arquitetaweb.comanda.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.fragment.PedidoFragment;
import com.arquitetaweb.comanda.model.MesaModel;
import com.google.gson.Gson;

public class PedidoActivity extends Activity {

	private Fragment fragment = null;
	private MesaModel mesa;
	private long idProdutoGrupo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedido);

		// button back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		Gson gson = new Gson();
		mesa = gson.fromJson(getIntent().getStringExtra("mesa"),
				MesaModel.class); // converte pra ArrayList de mesas		
		idProdutoGrupo = getIntent().getLongExtra("IdProdutoGrupo", 0);
		 
		setTitle("Pedido Para Mesa: " + mesa.numero_mesa);
		selectItem();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

	private void selectItem() {
		fragment = new PedidoFragment();

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}

	public MesaModel getMesaModel() {
		return this.mesa;
	}
	
	public long getIdProdutoGrupo() {
		return this.idProdutoGrupo;
	}
}
