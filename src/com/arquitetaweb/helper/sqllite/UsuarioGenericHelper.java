package com.arquitetaweb.helper.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arquitetaweb.comanda.model.UsuarioModel;

public class UsuarioGenericHelper extends DatabaseGenericHelper<UsuarioModel> {

	// Table - column names
	private static final String KEY_USUARIO_EMAIL = "email";

	public UsuarioGenericHelper(Context context) {
		super(context);

		TABLE = "usuario";

		CREATE_TABLE = "CREATE TABLE " + " IF NOT EXISTS " + TABLE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USUARIO_EMAIL + " TEXT," + KEY_CREATED_AT
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
	protected ContentValues insertFromValuesGeneric(UsuarioModel model) {
		ContentValues values = new ContentValues();
		values.put(KEY_USUARIO_EMAIL, model.email);
		values.put(KEY_CREATED_AT, getDateTime());
		return values;
	}

	@Override
	protected UsuarioModel selectFromObjectGeneric(Cursor c) {
		UsuarioModel usuario = new UsuarioModel();
		usuario.email = ((c.getString(c.getColumnIndex(KEY_USUARIO_EMAIL))));
		return usuario;
	}
}
