package com.arquitetaweb.comanda.model;

import com.google.gson.annotations.SerializedName;

public class ConfiguracoesModel {
	
	@SerializedName("UrlServico")
	public String urlServico;
	
	@SerializedName("PortaServico")
    public Integer portaServico;

    public String getUrlServico() {
        return urlServico == null ? "mocksapi.herokuapp.com" : urlServico;
    }

    public void setUrlServico(String urlServidor) {
        this.urlServico = urlServidor;
    }

    public Integer getPortaServico() {
        return portaServico == null ? 80 : portaServico;
    }

    public void setPortaServico(Integer portaServico) {
        this.portaServico = portaServico;
    }
}
