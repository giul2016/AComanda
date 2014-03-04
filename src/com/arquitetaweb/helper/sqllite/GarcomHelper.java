package com.arquitetaweb.helper.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arquitetaweb.comanda.model.GarcomModel;

public class GarcomHelper extends DatabaseHelper {

	// GARCOM Table - column names
	private static final String KEY_GARCOM_CODIGO = "codigo";
	private static final String KEY_GARCOM_NOME = "nome";

	// Table Names
	private static final String TABLE_GARCOM = "garcom";

	// Table Create Statements
	// Todo table create statement
	private static final String CREATE_TABLE_GARCOM = "CREATE TABLE "
			+ " IF NOT EXISTS "
			+ TABLE_GARCOM + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_GARCOM_CODIGO + " TEXT," + KEY_GARCOM_NOME + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	private static final String DROP_TABLE_GARCOM = "DROP TABLE IF EXISTS " + TABLE_GARCOM;
	
	public GarcomHelper(Context context, ProgressDialog progressDialog) {
		super(context, progressDialog);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		db.execSQL(CREATE_TABLE_GARCOM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		db.execSQL(DROP_TABLE_GARCOM);
	}

	// ------------------------ "Garcom" table methods ----------------//

	/*
	 * Creating a Garcom from list
	 */
	public void createGarcom(List<GarcomModel> garcons) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		for (GarcomModel garcom : garcons) {
			values.put(KEY_GARCOM_CODIGO, garcom.codigo);
			values.put(KEY_GARCOM_NOME, garcom.nome);
			values.put(KEY_CREATED_AT, getDateTime());

			db.insert(TABLE_GARCOM, null, values);
		}
	}

	/*
	 * get single garcom
	 */
	public GarcomModel getGarcom(String codigo) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_GARCOM + " WHERE "
				+ KEY_GARCOM_CODIGO + " = " + codigo;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		GarcomModel garcom = new GarcomModel();
		garcom.codigo = c.getString(c.getColumnIndex(KEY_GARCOM_CODIGO));
		garcom.nome = c.getString(c.getColumnIndex(KEY_GARCOM_NOME));
		// garcom.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

		return garcom;
	}

	/**
	 * getting all garcom
	 * */
	public List<GarcomModel> getAllGarcom() {
		List<GarcomModel> garcomLista = new ArrayList<GarcomModel>();
		String selectQuery = "SELECT  * FROM " + TABLE_GARCOM;

		// if (Debug.isDebuggerConnected()) {
		// Log.e(LOG, selectQuery);
		// }

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				GarcomModel garcom = new GarcomModel();
				garcom.id = (c.getLong((c.getColumnIndex(KEY_ID))));
				garcom.codigo = ((c.getString(c
						.getColumnIndex(KEY_GARCOM_CODIGO))));
				garcom.nome = ((c.getString(c.getColumnIndex(KEY_GARCOM_NOME))));

				// adding to todo list
				garcomLista.add(garcom);
			} while (c.moveToNext());
		}

		return garcomLista;
	}

	/*
	 * getting garcom count
	 */
	public int getGarcomCount() {
		String countQuery = "SELECT  * FROM " + TABLE_GARCOM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a todo
	 */
	public int updateGarcom(GarcomModel garcom) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_GARCOM_NOME, garcom.nome);

		// updating row
		return db.update(TABLE_GARCOM, values, KEY_ID + " = ?",
				new String[] { String.valueOf(garcom.id) });
	}

	/*
	 * Deleting a todo
	 */
	public void deleteGarcom(long tado_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GARCOM, KEY_ID + " = ?",
				new String[] { String.valueOf(tado_id) });
	}
	
	public void sincronizar(List<GarcomModel> garcomLista) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL(DROP_TABLE_GARCOM);
		
		db.execSQL(CREATE_TABLE_GARCOM);
		
		ContentValues values = new ContentValues();

		for (GarcomModel garcom : garcomLista) {
			values.put(KEY_ID, garcom.id);
			values.put(KEY_GARCOM_CODIGO, garcom.codigo);
			values.put(KEY_GARCOM_NOME, garcom.nome);
			values.put(KEY_CREATED_AT, getDateTime());

			db.insert(TABLE_GARCOM, null, values);
		}
	}
}
