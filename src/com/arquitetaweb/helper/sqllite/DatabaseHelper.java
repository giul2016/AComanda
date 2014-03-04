package com.arquitetaweb.helper.sqllite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	protected static final int DATABASE_VERSION = 1;

	// Database Name
	protected static final String DATABASE_NAME = "ArqW_Comanda";

	// Generic Common column names
	protected static final String KEY_ID = "id";
	protected static final String KEY_CREATED_AT = "created_at";

	protected ProgressDialog progressDialog;

	protected Context context;

	public DatabaseHelper(Context context, ProgressDialog progressDialog) {
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
		SQLiteDatabase db = this.getReadableDatabase();
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
}
