package com.arquitetaweb.comanda.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class GarcomModel {
	@SerializedName("Codigo")
	public String codigo;
	
	@SerializedName("Nome")
    public String nome;
	
	@SerializedName("Senha")
    public String senha;	
	
	public Type getType() {
		return new TypeToken<ArrayList<GarcomModel>>(){}.getType();
	}
}
