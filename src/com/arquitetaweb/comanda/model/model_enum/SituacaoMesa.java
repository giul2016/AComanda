package com.arquitetaweb.comanda.model.model_enum;

import com.google.gson.annotations.SerializedName;

public enum SituacaoMesa {

	@SerializedName("1")
	LIVRE,

	@SerializedName("2")
	OCUPADA,

	@SerializedName("3")
	ABRIR,

	@SerializedName("4")
	VISUALIZAR,

	@SerializedName("5")
	FECHAR,

	@SerializedName("6")
	LIMPAR,

	@SerializedName("7")
	OCIOSA,

	@SerializedName("8")
	TESTE;
}
