package com.arquitetaweb.helper.sqllite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;

public abstract class DatabaseGenericHelper<T> extends SQLiteOpenHelper {

	// Database Version
	protected static final int DATABASE_VERSION = 1;

	protected SQLiteDatabase db;

	protected Context context;

	// Table Names
	protected static String TABLE = "";
	protected static String TABLE_FK = "";

	// Table Create Statements
	protected static String CREATE_TABLE = "";

	protected static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;

	// Database Name
	protected static final String DATABASE_NAME = "ArqW_Comanda";

	// Generic Common column names
	protected static final String KEY_ID = "id";
	protected static final String KEY_CREATED_AT = "created_at";

	public DatabaseGenericHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// create new tables
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		onCreate(db);
	}

	// closing database
	public void closeDB() {
		db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	protected String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	@SuppressWarnings("unchecked")
	public void sincronizarAsync(List<T> produtos) {
		new Sincronizar().execute(produtos);
	}

	public void sincronizar(List<T> produtos) {
		sincronizarDados(produtos);
	}

	public void sincronizar(T model) {
		sincronizarDados(model);
	}

	@SuppressWarnings("unchecked")
	public void sincronizar() {
		new Sincronizar().execute();
	}

	protected class Sincronizar extends AsyncTask<List<T>, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(List<T>... produtos) {
			if (Utils.isConnected(context)) {
				sincronizarDados(produtos[0]);
			} else {
				errorConnectServer();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}
	}

	protected void sincronizarDados(List<T> listModel) {
		Log.d("DatabaseGenericHelper", "Sincronizando...." + TABLE);
		db = this.getWritableDatabase();
		db.execSQL(DROP_TABLE);
		db.execSQL(CREATE_TABLE);
		for (T model : listModel) {
			db.insert(TABLE, null, insertFromValuesGeneric(model));
		}
	}

	protected void sincronizarDados(T model) {
		Log.d("DatabaseGenericHelper", "Sincronizando...." + TABLE);
		db = this.getWritableDatabase();
		db.execSQL(DROP_TABLE);
		db.execSQL(CREATE_TABLE);
		db.insert(TABLE, null, insertFromValuesGeneric(model));
	}

	public List<T> selectAll() {
		Log.d("DatabaseGenericHelper", "Listando...." + TABLE);
		db = this.getReadableDatabase();

		// select
		List<T> lista = new ArrayList<T>();
		String selectQuery = "SELECT  * FROM " + TABLE;

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to todo list
				lista.add(selectFromObjectGeneric(c));
			} while (c.moveToNext());
		}

		return lista;
	}

	public List<T> selectWhere(String where) {
		Log.i("DatabaseGenericHelper", "Listando.... selectWhere " + TABLE);
		db = this.getReadableDatabase();

		// select
		List<T> garcomLista = new ArrayList<T>();
		String selectQuery = "SELECT  * FROM " + TABLE + " WHERE " + where;

		// Log.i("DatabaseGenericHelper", selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to todo list
				T obj = selectFromObjectGeneric(c);
				// Log.d("DatabaseGenericHelper", obj);
				garcomLista.add(obj);
			} while (c.moveToNext());
		}

		return garcomLista;
	}

	public T selectById(Long id) {
		Log.d("DatabaseGenericHelper", "Listando.... selectById " + TABLE);
		db = this.getReadableDatabase();

		// select
		String selectQuery = "SELECT  * FROM " + TABLE + " WHERE " + KEY_ID
				+ " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null) {
			c.moveToFirst();
			// Log.d("DatabaseGenericHelper", selectQuery);
			return selectFromObjectGeneric(c);
		}
		return null;
	}

	public T selectOne() {
		Log.d("DatabaseGenericHelper", "Listando.... selectById " + TABLE);
		db = this.getReadableDatabase();

		// select
		String selectQuery = "SELECT  * FROM " + TABLE;

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c != null) {
			c.moveToFirst();
			// Log.d("DatabaseGenericHelper", selectQuery);
			return selectFromObjectGeneric(c);
		}
		return null;
	}

	public int getCount() {
		String countQuery = "SELECT  * FROM " + TABLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	protected void errorConnectServer() {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				new Alerta()
						.show(context, "", "Não Foi Localizado o Servidor!"
								+ "\nCausas:" + "\nConexão OK?"
								+ "\nServidor correto?");
			}
		});
	}

	/**
	 * Método para enviar os valores da coluna a ser inserido no banco
	 * 
	 * @param model
	 * @return ContentValues - Nome/Valores das colunas
	 */
	protected abstract ContentValues insertFromValuesGeneric(T model);

	/**
	 * Método para gerar os Objeto Tipados
	 * 
	 * @param c
	 * @return Objeto Tipado
	 */
	protected abstract T selectFromObjectGeneric(Cursor c);
}
