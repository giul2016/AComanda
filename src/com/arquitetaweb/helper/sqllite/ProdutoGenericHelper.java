package com.arquitetaweb.helper.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.arquitetaweb.comanda.model.ProdutoModel;

public class ProdutoGenericHelper extends DatabaseGenericHelper<ProdutoModel> {

	// Table - column names
	private static final String KEY_PRODUTO_PRODUTOGRUPOID = "produtoGrupoId";
	private static final String KEY_PRODUTO_CODIGO = "codigo";
	private static final String KEY_PRODUTO_DESCRICAO = "descricao";

	public ProdutoGenericHelper(Context context) {
		super(context);

		TABLE = "produto";
		TABLE_FK = "produto_grupo";

		CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_PRODUTO_PRODUTOGRUPOID
				+ " INTEGER," + KEY_PRODUTO_CODIGO + " TEXT,"
				+ KEY_PRODUTO_DESCRICAO + " TEXT," + KEY_CREATED_AT
				+ " DATETIME," + "FOREIGN KEY (" + KEY_PRODUTO_PRODUTOGRUPOID
				+ ") REFERENCES " + TABLE_FK + "(" + KEY_ID + ")" + ");";

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
	protected ContentValues insertFromValuesGeneric(ProdutoModel model) {
		ContentValues values = new ContentValues();
		values.put(KEY_ID, model.id);
		values.put(KEY_PRODUTO_PRODUTOGRUPOID, model.produto_grupo_id);
		values.put(KEY_PRODUTO_CODIGO, model.codigo);
		values.put(KEY_PRODUTO_DESCRICAO, model.descricao);
		values.put(KEY_CREATED_AT, getDateTime());
		return values;
	}

	@Override
	protected ProdutoModel selectFromObjectGeneric(Cursor c) {
		ProdutoModel garcom = new ProdutoModel();
		garcom.id = (c.getLong((c.getColumnIndex(KEY_ID))));
		garcom.produto_grupo_id = (c.getLong((c
				.getColumnIndex(KEY_PRODUTO_PRODUTOGRUPOID))));
		garcom.codigo = ((c.getString(c.getColumnIndex(KEY_PRODUTO_CODIGO))));
		garcom.descricao = ((c.getString(c
				.getColumnIndex(KEY_PRODUTO_DESCRICAO))));

		Log.d("selectFromObjectGeneric", garcom.id + " - " + garcom.codigo);
		
		return garcom;
	}
}
