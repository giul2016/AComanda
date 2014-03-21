package com.arquitetaweb.comanda.controller;

import java.util.List;

import android.content.Context;

import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
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
		return produtoSemGrupo(produtoGrupoList);
	}
	
	private List<ProdutoGrupoModel> produtoSemGrupo(
			List<ProdutoGrupoModel> produtoGrupoList) {
		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(context);
		List<ProdutoModel> produtoList = dbProduto
				.selectWhere("produtoGrupoId = 0");
		dbProduto.closeDB();
		if (produtoList.size() > 0) {
			ProdutoGrupoModel semGrupoProduto = new ProdutoGrupoModel();
			semGrupoProduto.descricao = "Sem Grupo de Produto";
			produtoGrupoList.add(semGrupoProduto);
		}

		return produtoGrupoList;
	}
}
