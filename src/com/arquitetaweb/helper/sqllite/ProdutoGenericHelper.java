package com.arquitetaweb.helper.sqllite;

import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.arquitetaweb.comanda.model.ProdutoModel;

public class ProdutoGenericHelper extends DatabaseGenericHelper<ProdutoModel> {

	// Table - column names
	private static final String KEY_PRODUTO_PRODUTOGRUPOID = "produtoGrupoId";
	private static final String KEY_PRODUTO_CODIGO = "codigo";
	private static final String KEY_PRODUTO_DESCRICAO = "descricao";

	public ProdutoGenericHelper(Context context, ProgressDialog progressDialog) {
		super(context, progressDialog);

		TABLE = "produto";

		CREATE_TABLE = "CREATE TABLE " + " IF NOT EXISTS " + TABLE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUTO_CODIGO
				+ " TEXT," + KEY_PRODUTO_DESCRICAO + " TEXT," + KEY_CREATED_AT
				+ " DATETIME" + ")";
		
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
	protected void sincronizarAbstract(List<ProdutoModel> listModel) {
		db = this.getWritableDatabase();
		db.execSQL(DROP_TABLE);
		db.execSQL(CREATE_TABLE);
		for (ProdutoModel model : listModel) {
			db.insert(TABLE, null, getValuesModel(model));
		}				
		
		// select
				// List<ProdutoGrupoModel> allToDos =
				// dbProdutoGrupo.getAllProdutoGrupo();
				// for (ProdutoGrupoModel todo : allToDos) {
				// Log.d("getAllProdutoGrupo", todo.id + " - " + todo.codigo + " - "
				// + todo.descricao);
				// }
	}
	
	@Override
	protected ContentValues getValuesModel(ProdutoModel model) {
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUTO_CODIGO, model.codigo);
		values.put(KEY_PRODUTO_DESCRICAO, model.descricao);
		values.put(KEY_CREATED_AT, getDateTime());
		return values;
	}
}
