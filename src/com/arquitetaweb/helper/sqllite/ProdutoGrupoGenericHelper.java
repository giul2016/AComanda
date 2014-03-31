package com.arquitetaweb.helper.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.arquitetaweb.comanda.model.ProdutoGrupoModel;

public class ProdutoGrupoGenericHelper extends
		DatabaseGenericHelper<ProdutoGrupoModel> {

	// Table - column names
	private static final String KEY_PRODUTOGRUPO_CODIGO = "codigo";
	private static final String KEY_PRODUTOGRUPO_DESCRICAO = "descricao";

	public ProdutoGrupoGenericHelper(Context context) {
		super(context);

		TABLE = "produto_grupo";

		CREATE_TABLE = "CREATE TABLE " + " IF NOT EXISTS " + TABLE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUTOGRUPO_CODIGO
				+ " TEXT," + KEY_PRODUTOGRUPO_DESCRICAO + " TEXT,"
				+ KEY_CREATED_AT + " DATETIME" + ")";

		DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		db.execSQL(DROP_TABLE);
	}	

	@Override
	protected ContentValues insertFromValuesGeneric(ProdutoGrupoModel model) {
		ContentValues values = new ContentValues();
		values.put(KEY_ID, model.id);
		values.put(KEY_PRODUTOGRUPO_CODIGO, model.codigo);
		values.put(KEY_PRODUTOGRUPO_DESCRICAO, model.descricao);
		values.put(KEY_CREATED_AT, getDateTime());
		return values;
	}

	@Override
	protected ProdutoGrupoModel selectFromObjectGeneric(Cursor c) {
		ProdutoGrupoModel garcom = new ProdutoGrupoModel();
		garcom.id = (c.getLong((c.getColumnIndex(KEY_ID))));
		garcom.codigo = ((c
				.getString(c.getColumnIndex(KEY_PRODUTOGRUPO_CODIGO))));
		garcom.descricao = ((c.getString(c
				.getColumnIndex(KEY_PRODUTOGRUPO_DESCRICAO))));

		Log.d("ProdutoGrupoModel", "Id: " + garcom.id + " - Codigo: "
				+ garcom.codigo);
		return garcom;
	}
}
