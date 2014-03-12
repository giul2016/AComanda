package com.arquitetaweb.comanda.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;

public class ProdutoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ProdutoModel> data;
	private static LayoutInflater inflater = null;

	public ProdutoAdapter(Activity activity,
			List<ProdutoModel> listaProdutoGrupo) {
		this.activity = activity;
		data = listaProdutoGrupo;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public ProdutoModel getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).id;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.produto_info, null);
		}

		// number table
		TextView descricao = (TextView) vi
				.findViewById(R.id.txtprodutodescricao);
		TextView codigo = (TextView) vi
				.findViewById(R.id.txtprodutocodigo);

		// get item from position
		ProdutoModel item = new ProdutoModel();
		item = data.get(position);

		// setting all values in gridview
		descricao.setText(item.descricao);
		codigo.setText(item.codigo);

		return vi;
	}
}
