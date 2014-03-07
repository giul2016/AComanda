package com.arquitetaweb.comanda.model.model_enum;

import com.google.gson.annotations.SerializedName;

public enum SituacaoMesa {

	@SerializedName("1")
	Livre,

	@SerializedName("2")
	Ocupada,

	@SerializedName("3")
	EmConta,

	@SerializedName("4")
	Limpar,

	@SerializedName("5")
	Agrupada,

	@SerializedName("6")
	Reservada,

	@SerializedName("7")
	Ociosa,

	@SerializedName("8")
	Indefinido;
}