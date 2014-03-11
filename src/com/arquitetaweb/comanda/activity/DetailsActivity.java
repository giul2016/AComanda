package com.arquitetaweb.comanda.activity;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.adapter.ProdutoGrupoAdapter;
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comum.messages.AlertaToast;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;
import com.google.gson.Gson;

public class DetailsActivity extends FragmentActivity {
	
	private static final String SCHEME = "settings";
	private static final String AUTHORITY = "details";
	
	public static final Uri URI = new Uri.Builder().scheme(SCHEME)
			.authority(AUTHORITY).build();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mesa_details);

		// get the action bar
		ActionBar actionBar = getActionBar();
		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);

		Gson gson = new Gson();
		MesaModel mesa = gson.fromJson(getIntent().getStringExtra("mesa"),
				MesaModel.class); // converte pra ArrayList de mesas
		
		// Get List ProdutoGrupo from DB SQLLite
		ProdutoGrupoGenericHelper dbProdutoGrupo = new ProdutoGrupoGenericHelper(this);
		List<ProdutoGrupoModel> produtoGrupoList = dbProdutoGrupo.selectAll();
		dbProdutoGrupo.closeDB();
		
		ListView list = (ListView) this.findViewById(R.id.listDetail);
		ProdutoGrupoAdapter adapter = new ProdutoGrupoAdapter(this, produtoGrupoList);
		
		updateListView(list, adapter);	
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
		super.onDestroy();
	}

	private void updateListView(final ListView listView, final ProdutoGrupoAdapter adapter) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				listView.setAdapter(adapter);

				// Click event for single list row
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						abrirDetalhes(view, position);
					}

					private void abrirDetalhes(View view, Integer id) {												
						ProdutoGrupoModel produtoGrupoObj = adapter.getItem(id);
						new AlertaToast().show(view.getContext(), produtoGrupoObj.codigo +" :: "+ id.toString());
						//String mesaGson = new Gson().toJson(produtoGrupoObj);
//						intent.putExtra("mesa", mesaGson);
//						fragment.startActivityForResult(intent, 100);
					}
				});
			}
		});
	}
}
