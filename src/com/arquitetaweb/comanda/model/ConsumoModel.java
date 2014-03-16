package com.arquitetaweb.comanda.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class ConsumoModel {		
	@SerializedName("MesaId")
	public Long mesaid;
	
	@SerializedName("DeviceId")
    public Long deviceid;
	
	@SerializedName("ProdutoId")
    public Long produtoid;
		
	@SerializedName("Quantidade")
    public String quantidade;

	public Type getType() {
		return new TypeToken<ArrayList<ConsumoModel>>(){}.getType();
	}	
}
