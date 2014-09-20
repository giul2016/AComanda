package com.arquitetaweb.helper.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.arquitetaweb.comanda.model.ProdutoComplementoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;

public class ProdutoComplementoGenericHelper extends DatabaseGenericHelper<ProdutoComplementoModel> {

	// Table - column names
	private static final String KEY_COMPLEMENTO_PRODUTOID = "produtoId";
    private static final String KEY_COMPLEMENTO_PRODUTOCOMPLEMENTOID = "produtoComplementoId";

	public ProdutoComplementoGenericHelper(Context context) {
		super(context);

		TABLE = "produto_complemento";
		TABLE_FK = "produto";

		CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_COMPLEMENTO_PRODUTOID
				+ " INTEGER," + KEY_COMPLEMENTO_PRODUTOCOMPLEMENTOID + " INTEGER,"
				+ KEY_CREATED_AT
				+ " DATETIME," + "FOREIGN KEY (" + KEY_COMPLEMENTO_PRODUTOID + ", "
                + KEY_COMPLEMENTO_PRODUTOCOMPLEMENTOID
				+ ") REFERENCES " + TABLE_FK + "(" + KEY_ID + "," + KEY_ID + ")" + ");";

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
	protected ContentValues insertFromValuesGeneric(ProdutoComplementoModel model) {
		ContentValues values = new ContentValues();
		values.put(KEY_ID, model.id);
		values.put(KEY_COMPLEMENTO_PRODUTOID, model.produto_id);
		values.put(KEY_COMPLEMENTO_PRODUTOCOMPLEMENTOID, model.produto_complemento_id);
		values.put(KEY_CREATED_AT, getDateTime());
		return values;
	}

	@Override
	protected ProdutoComplementoModel selectFromObjectGeneric(Cursor c) {
        ProdutoComplementoModel produtoComplemento = new ProdutoComplementoModel();
		produtoComplemento.id = (c.getLong((c.getColumnIndex(KEY_ID))));
		produtoComplemento.produto_id = (c.getLong((c
				.getColumnIndex(KEY_COMPLEMENTO_PRODUTOID))));
        produtoComplemento.produto_complemento_id = (c.getLong((c
                .getColumnIndex(KEY_COMPLEMENTO_PRODUTOCOMPLEMENTOID))));

		//Log.d("selectFromObjectGeneric", garcom.id + " - " + garcom.codigo);
		
		return produtoComplemento;
	}
}
