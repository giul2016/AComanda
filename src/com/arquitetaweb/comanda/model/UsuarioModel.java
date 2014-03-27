package com.arquitetaweb.comanda.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class UsuarioModel {	
	@SerializedName("Nome")
	public String email;
	
	@SerializedName("DeviceID")
    public String deviceID;
	
	@SerializedName("Verificado")
    public Boolean verificado;
	
	public Type getType() {
		return new TypeToken<ArrayList<UsuarioModel>>(){}.getType();
	}
}
