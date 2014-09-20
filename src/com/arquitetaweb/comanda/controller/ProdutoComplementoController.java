package com.arquitetaweb.comanda.controller;

import android.content.Context;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.ProdutoComplementoModel;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.ProdutoComplementoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGrupoGenericHelper;

import java.util.ArrayList;
import java.util.List;

public class ProdutoComplementoController {

	private Context context;

	public ProdutoComplementoController(Context context) {
		this.context = context;
	}

	public List<ConsumoModel> sincronizar(long idProduto) {

		ProdutoComplementoGenericHelper dbProdutoComplemento = new ProdutoComplementoGenericHelper(context);
        List<ProdutoComplementoModel> listaProduto = dbProdutoComplemento.selectWhere("produtoId = " + idProduto);
        dbProdutoComplemento.closeDB();

        List<ProdutoModel> produtoModelList = new ArrayList<ProdutoModel>();
        ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(context);
        for (ProdutoComplementoModel complementoModel : listaProduto) {
            ProdutoModel produto = dbProduto
                    .selectById((long) complementoModel.produto_complemento_id);
            produtoModelList.add(produto);
        }
        dbProduto.closeDB();

        List<ConsumoModel> lista = new ArrayList<ConsumoModel>();
        for (ProdutoModel produtoModel : produtoModelList) {
            ConsumoModel item = new ConsumoModel();
            item.produtoId = produtoModel.id;
            item.quantidade = "0";
            lista.add(item);
        }

        return lista;
	}
}
