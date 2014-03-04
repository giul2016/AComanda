package com.arquitetaweb.helper.sqllite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.arquitetaweb.comanda.util.Utils;
import com.arquitetaweb.comum.messages.Alerta;

public abstract class DatabaseGenericHelper<T> extends SQLiteOpenHelper {

	// Database Version
	protected static final int DATABASE_VERSION = 1;

	protected ProgressDialog progressDialog;

	protected SQLiteDatabase db;
	
	protected Context context;

	// Table Names
	protected static String TABLE = "";

	// Table Create Statements
	protected static String CREATE_TABLE = "";

	protected static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE;

	// Database Name
	protected static final String DATABASE_NAME = "ArqW_Comanda";

	// Generic Common column names
	protected static final String KEY_ID = "id";
	protected static final String KEY_CREATED_AT = "created_at";

	public DatabaseGenericHelper(Context context, ProgressDialog progressDialog) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		this.progressDialog = progressDialog;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		// db.execSQL(CREATE_TABLE_GARCOM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// create new tables
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
	public void sincronizar(List<T> produtos) {		
		new Sincronizar().execute(produtos);		
	}
	
	protected class Sincronizar extends AsyncTask<List<T>, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setCancelable(false);
			progressDialog.setMessage("sincronizando produtos...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(List<T>... produtos) {
			if (Utils.isConnected(context)) {
				sincronizarAbstract(produtos[0]);
			} else {
				errorConnectServer();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();

			super.onPostExecute(result);
		}
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
	
	protected abstract void sincronizarAbstract(List<T> listModel);

	protected abstract ContentValues getValuesModel(T model);
}
