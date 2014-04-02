package com.arquitetaweb.helper.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arquitetaweb.comanda.model.SettingsModel;

public class SettingsGenericHelper extends DatabaseGenericHelper<SettingsModel> {

	// Table - column names
	private static final String KEY_URL_SERVICO = "urlServico";
	private static final String KEY_PORTA_SERVICO = "portaServico";

	public SettingsGenericHelper(Context context) {
		super(context);

		TABLE = "configuracao";

		CREATE_TABLE = "CREATE TABLE " + " IF NOT EXISTS " + TABLE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_URL_SERVICO + " TEXT,"
				+ KEY_PORTA_SERVICO + " INTEGER," + KEY_CREATED_AT
				+ " DATETIME" + ")";

		DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		//db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		db.execSQL(DROP_TABLE);
	}

	@Override
	protected ContentValues insertFromValuesGeneric(SettingsModel model) {
		ContentValues values = new ContentValues();
		values.put(KEY_URL_SERVICO, model.urlServico);
		values.put(KEY_PORTA_SERVICO, model.portaServico);
		values.put(KEY_CREATED_AT, getDateTime());
		return values;
	}

	@Override
	protected SettingsModel selectFromObjectGeneric(Cursor c) {
		SettingsModel configuracao = new SettingsModel();
		configuracao.urlServico = ((c.getString(c.getColumnIndex(KEY_URL_SERVICO))));
		configuracao.portaServico = ((c.getInt(c.getColumnIndex(KEY_PORTA_SERVICO))));
		return configuracao;
	}
}
