package com.arquitetaweb.comanda.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.fragment.PedidoFragment;
import com.arquitetaweb.comanda.model.MesaModel;

public class PedidoActivity extends Activity {

	private Fragment fragment = null;
	private MesaModel mesa;
	private long idProdutoGrupo;
	private boolean produtoRecente;
	private SearchView searchView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedido);

		// button back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mesa = (MesaModel) getIntent().getSerializableExtra("mesa");
		idProdutoGrupo = getIntent().getLongExtra("IdProdutoGrupo", 0);
		produtoRecente = getIntent().getBooleanExtra("produtoRecente", false);

		setTitle("Pedido Para Mesa: " + mesa.numero_mesa);
		selectItem();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pedido_menu, menu);

		// SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		// searchView.setOnQueryTextListener(new OnQueryTextListener() {
		//
		// @Override
		// public boolean onQueryTextChange(String arg0) {
		// GridViewBounceCustom mesas = (GridViewBounceCustom) fragment
		// .getView().findViewById(android.R.id.list);
		// MesaAdapter v = (MesaAdapter) mesas.getAdapter();
		// if (v != null) {
		// v.getFilter().filter(arg0);
		// }
		//
		// return true;
		// }
		//
		// @Override
		// public boolean onQueryTextSubmit(String arg0) {
		// GridViewBounceCustom mesaGrid = (GridViewBounceCustom) fragment
		// .getView().findViewById(android.R.id.list);
		//
		// MesaAdapter mesaAdapter = (MesaAdapter) mesaGrid.getAdapter();
		// if (mesaAdapter.getCount() > 0) {
		// MesaModel mesaObj = mesaAdapter.getItem(0);
		// Intent intent = new Intent(fragment.getView().getContext(),
		// DetalhesMesaActivity.class);
		// intent.putExtra("mesa", mesaObj);
		// fragment.startActivityForResult(intent, 100);
		// } else {
		// new AlertaToast().show(fragment.getActivity(),
		// "Nenhuma mesa localizada pelo critério: " + arg0
		// + "\nVerifique sua consulta.");
		// }
		// return true;
		// }
		// });

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.action_search:
			return true;
		default:
			onBackPressed();
			return super.onOptionsItemSelected(menuItem);
		}
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

	public boolean getProdutoRecente() {
		return this.produtoRecente;
	}
}
