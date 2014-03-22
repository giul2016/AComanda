package com.arquitetaweb.comanda.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class ConsumoModel {	
	@SerializedName("MesaId")
	public long mesaId;
	
	@SerializedName("DeviceId")
    public long deviceId;
	
	@SerializedName("ProdutoId")
    public long produtoId;
		
	@SerializedName("Quantidade")
    public String quantidade;

	@SerializedName("DataHoraPedido")
    public Date dataHora;
	
	public Type getType() {
		return new TypeToken<ArrayList<ConsumoModel>>(){}.getType();
	}	
}
