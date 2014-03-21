package com.arquitetaweb.comanda.model;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.arquitetaweb.comanda.model.model_enum.SituacaoMesa;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class MesaModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4908902672708160542L;

	@SerializedName("Id")
	public long id;
	
	@SerializedName("NumeroMesa")
    public String numero_mesa;
	
	@SerializedName("Situacao")
    public SituacaoMesa situacao;

	public Type getType() {
		return new TypeToken<ArrayList<MesaModel>>(){}.getType();
	}	
}
