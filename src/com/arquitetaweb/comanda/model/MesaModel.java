package com.arquitetaweb.comanda.model;

import com.google.gson.annotations.SerializedName;

public class MesaModel {
	@SerializedName("Id")
	public String id;
	
	@SerializedName("NumeroMesa")
    public String numeroMesa;
	
	@SerializedName("Situacao")
    public String situacao;
}
