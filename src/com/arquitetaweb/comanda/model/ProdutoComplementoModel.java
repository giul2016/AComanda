package com.arquitetaweb.comanda.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProdutoComplementoModel {
	@SerializedName("Id")
	public long id;
	
	@SerializedName("ProdutoId")
    public long produto_id;
	
	@SerializedName("ProdutoComplementoId")
    public long produto_complemento_id;

	public Type getType() {
		return new TypeToken<ArrayList<ProdutoComplementoModel>>(){}.getType();
	}
}
