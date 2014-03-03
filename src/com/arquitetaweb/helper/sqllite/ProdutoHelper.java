package com.arquitetaweb.helper.sqllite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arquitetaweb.comanda.model.ProdutoModel;

public class ProdutoHelper extends DatabaseHelper {

	// Table - column names
	private static final String KEY_PRODUTO_PRODUTOGRUPOID = "produtoGrupoId";
	private static final String KEY_PRODUTO_CODIGO = "codigo";
	private static final String KEY_PRODUTO_DESCRICAO = "descricao";

	// Table Names
	private static final String TABLE_PRODUTO = "produto";

	// Table Create Statements
	private static final String CREATE_TABLE_PRODUTO = "CREATE TABLE "
			+ " IF NOT EXISTS " + TABLE_PRODUTO + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY," + KEY_PRODUTO_CODIGO + " TEXT,"
			+ KEY_PRODUTO_DESCRICAO + " TEXT," + KEY_CREATED_AT + " DATETIME"
			+ ")";

	private static final String DROP_TABLE_PRODUTO = "DROP TABLE IF EXISTS "
			+ TABLE_PRODUTO;

	public ProdutoHelper(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		db.execSQL(CREATE_TABLE_PRODUTO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		db.execSQL(DROP_TABLE_PRODUTO);
	}

	/*
	 * Creating a objectModel from listModel
	 */
	public void createProduto(List<ProdutoModel> produtos) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		for (ProdutoModel produto : produtos) {
			values.put(KEY_ID, produto.id);
			values.put(KEY_PRODUTO_CODIGO, produto.codigo);
			values.put(KEY_PRODUTO_DESCRICAO, produto.descricao);
			values.put(KEY_CREATED_AT, getDateTime());

			db.insert(TABLE_PRODUTO, null, values);
		}
	}

	/*
	 * getting all objects
	 */
	public List<ProdutoModel> getAllProduto() {
		List<ProdutoModel> produtoLista = new ArrayList<ProdutoModel>();
		String selectQuery = "SELECT  * FROM " + TABLE_PRODUTO;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				ProdutoModel produto = new ProdutoModel();
				produto.id = (c.getLong((c.getColumnIndex(KEY_ID))));
				produto.codigo = c.getString(c
						.getColumnIndex(KEY_PRODUTO_CODIGO));
				produto.descricao = c.getString(c
						.getColumnIndex(KEY_PRODUTO_DESCRICAO));

				// adding to todo list
				produtoLista.add(produto);
			} while (c.moveToNext());
		}

		return produtoLista;
	}

	public void sincronizar(List<ProdutoModel> produtos) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL(DROP_TABLE_PRODUTO);

		db.execSQL(CREATE_TABLE_PRODUTO);

		ContentValues values = new ContentValues();

		// produtos.removeAll(Collections.singleton(null));
		// produtos.toArray(new String[produtos.size()]);

		// produtos.removeAll(Arrays.asList(new Object[] { null }));

//		for (Iterator<ProdutoModel> itr = produtos.iterator(); itr.hasNext();) {
//			if (itr.next() == null) {
//				itr.remove();
//			}
//		}

//		List s1=new ArrayList();
//		s1.add(null);
//
//		produtos.removeAll(s1);		
		
		for (ProdutoModel produto : produtos) {
			// values.put(KEY_ID, produto.id);
			if (produto != null) {
				values.put(KEY_PRODUTO_CODIGO, produto.codigo);
				values.put(KEY_PRODUTO_DESCRICAO, produto.descricao);
				values.put(KEY_CREATED_AT, getDateTime());

				db.insert(TABLE_PRODUTO, null, values);
			}
		}
	}
}
