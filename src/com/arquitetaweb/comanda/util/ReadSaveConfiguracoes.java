package com.arquitetaweb.comanda.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.arquitetaweb.comanda.model.ConfiguracoesModel;
import com.google.gson.Gson;

public class ReadSaveConfiguracoes {

	protected Context context;
	private String fileName = "Configuracoes.arqweb";
	
	public ReadSaveConfiguracoes(Context context){
		this.context = context;
    }
	
	public ConfiguracoesModel getData() {
		SharedPreferences pm = PreferenceManager
				.getDefaultSharedPreferences(context);
		String jsonString = pm.getString(fileName, "{}");		

		StringBuilder builder = new StringBuilder();        	
		builder.append(jsonString);
		Gson gson = new Gson();
		ConfiguracoesModel response = gson.fromJson(builder.toString(), ConfiguracoesModel.class);
		
		return response;		
	}

	public void saveData(ConfiguracoesModel objModel) {
		SharedPreferences pm = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edt = pm.edit();

		//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		Gson gson = new Gson();
		String jsonFile = gson.toJson(objModel);
				
		edt.putString(fileName, jsonFile);
		edt.commit();
	}
}
