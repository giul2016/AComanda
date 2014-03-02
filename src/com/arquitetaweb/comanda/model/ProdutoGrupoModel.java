package com.arquitetaweb.comanda.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class ProdutoGrupoModel {
	@SerializedName("Id")
	public Long id;
	
	@SerializedName("Codigo")
    public String codigo;
	
	@SerializedName("Descricao")
    public String descricao;

	public Type getType() {
		return new TypeToken<ArrayList<ProdutoGrupoModel>>(){}.getType();
	}
}
