package com.arquitetaweb.comanda.controller;

import java.util.List;

import android.content.Context;

import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;

public class ProdutoController {

	private Context context;

	public ProdutoController(Context context) {
		this.context = context;
	}

	public List<ProdutoGrupoModel> sincronizar() {

		// Get List ProdutoGrupo from DB SQLLite
		ProdutoGrupoGenericHelper dbProdutoGrupo = new ProdutoGrupoGenericHelper(
				context);
		List<ProdutoGrupoModel> produtoGrupoList = dbProdutoGrupo.selectAll();
		dbProdutoGrupo.closeDB();

		return produtoGrupoList;
	}
}
