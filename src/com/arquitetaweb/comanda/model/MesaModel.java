package com.arquitetaweb.comanda.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class MesaModel {
	@SerializedName("Id")
	public Long id;
	
	@SerializedName("NumeroMesa")
    public String numeroMesa;
	
	@SerializedName("Situacao")
    public String situacao;

	public Type getType() {
		return new TypeToken<ArrayList<MesaModel>>(){}.getType();
	}
}
