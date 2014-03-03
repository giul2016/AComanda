package com.arquitetaweb.helper.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arquitetaweb.comanda.model.ProdutoGrupoModel;

public class ProdutoGrupoHelper extends DatabaseHelper {

	// Table - column names
	private static final String KEY_PRODUTOGRUPO_CODIGO = "codigo";
	private static final String KEY_PRODUTOGRUPO_DESCRICAO = "descricao";

	// Table Names
	private static final String TABLE_PRODUTOGRUPO = "produto_grupo";
	
	// Table Create Statements
	private static final String CREATE_TABLE_PRODUTOGRUPO = "CREATE TABLE "
			+ " IF NOT EXISTS "
			+ TABLE_PRODUTOGRUPO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PRODUTOGRUPO_CODIGO + " TEXT," + KEY_PRODUTOGRUPO_DESCRICAO
			+ " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";
	
	// Table DROP Statements
	private static final String DROP_TABLE_PRODUTOGRUPO = "DROP TABLE IF EXISTS " + TABLE_PRODUTOGRUPO;

	public ProdutoGrupoHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		db.execSQL(CREATE_TABLE_PRODUTOGRUPO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		db.execSQL(DROP_TABLE_PRODUTOGRUPO);
	}

	/*
	 * Creating a objectModel from listModel
	 */
	public void createProdutoGrupo(List<ProdutoGrupoModel> produtoGrupos) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();

		for (ProdutoGrupoModel produtoGrupo : produtoGrupos) {
			values.put(KEY_ID, produtoGrupo.id);
			values.put(KEY_PRODUTOGRUPO_CODIGO, produtoGrupo.codigo);
			values.put(KEY_PRODUTOGRUPO_DESCRICAO, produtoGrupo.descricao);
			values.put(KEY_CREATED_AT, getDateTime());

			db.insert(TABLE_PRODUTOGRUPO, null, values);
		}
	}

	/*
	 * getting all objects
	 */
	public List<ProdutoGrupoModel> getAllProdutoGrupo() {
		List<ProdutoGrupoModel> produtoGrupoLista = new ArrayList<ProdutoGrupoModel>();
		String selectQuery = "SELECT  * FROM " + TABLE_PRODUTOGRUPO;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				ProdutoGrupoModel produtoGrupo = new ProdutoGrupoModel();
				produtoGrupo.id = (c.getLong((c.getColumnIndex(KEY_ID))));
				produtoGrupo.codigo = c.getString(c
						.getColumnIndex(KEY_PRODUTOGRUPO_CODIGO));
				produtoGrupo.descricao = c.getString(c
						.getColumnIndex(KEY_PRODUTOGRUPO_DESCRICAO));

				// adding to todo list
				produtoGrupoLista.add(produtoGrupo);
			} while (c.moveToNext());
		}

		return produtoGrupoLista;
	}
	
	public void sincronizar(List<ProdutoGrupoModel> produtoGrupos) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL(DROP_TABLE_PRODUTOGRUPO);
		
		db.execSQL(CREATE_TABLE_PRODUTOGRUPO);
		
		ContentValues values = new ContentValues();

		for (ProdutoGrupoModel produtoGrupo : produtoGrupos) {
			values.put(KEY_ID, produtoGrupo.id);
			values.put(KEY_PRODUTOGRUPO_CODIGO, produtoGrupo.codigo);
			values.put(KEY_PRODUTOGRUPO_DESCRICAO, produtoGrupo.descricao);
			values.put(KEY_CREATED_AT, getDateTime());

			db.insert(TABLE_PRODUTOGRUPO, null, values);
		}
	}
}
