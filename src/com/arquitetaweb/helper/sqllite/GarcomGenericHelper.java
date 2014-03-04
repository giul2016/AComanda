package com.arquitetaweb.helper.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arquitetaweb.comanda.model.GarcomModel;

public class GarcomGenericHelper extends DatabaseGenericHelper<GarcomModel> {

	// Table - column names
	private static final String KEY_GARCOM_CODIGO = "codigo";
	private static final String KEY_GARCOM_DESCRICAO = "descricao";

	public GarcomGenericHelper(Context context) {
		super(context);

		TABLE = "garcom";

		CREATE_TABLE = "CREATE TABLE " + " IF NOT EXISTS " + TABLE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_GARCOM_CODIGO
				+ " TEXT," + KEY_GARCOM_DESCRICAO + " TEXT," + KEY_CREATED_AT
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
	protected ContentValues insertFromValuesGeneric(GarcomModel model) {
		ContentValues values = new ContentValues();
		values.put(KEY_GARCOM_CODIGO, model.codigo);
		values.put(KEY_GARCOM_DESCRICAO, model.nome);
		values.put(KEY_CREATED_AT, getDateTime());
		return values;
	}

	@Override
	protected GarcomModel selectFromObjectGeneric(Cursor c) {
		GarcomModel garcom = new GarcomModel();
		garcom.id = (c.getLong((c.getColumnIndex(KEY_ID))));
		garcom.codigo = ((c.getString(c.getColumnIndex(KEY_GARCOM_CODIGO))));
		garcom.nome = ((c.getString(c.getColumnIndex(KEY_GARCOM_DESCRICAO))));

		return garcom;
	}
}
