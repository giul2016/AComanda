package com.arquitetaweb.comanda.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.ProdutoGrupoModel;

public class ProdutoGrupoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ProdutoGrupoModel> data;
	private static LayoutInflater inflater = null;

	public ProdutoGrupoAdapter(Activity activity,
			List<ProdutoGrupoModel> listaProdutoGrupo) {
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
	public ProdutoGrupoModel getItem(int position) {
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.produtogrupo_info, null);
		}
		// get item from position
		ProdutoGrupoModel item = new ProdutoGrupoModel();
		item = data.get(position);

		// change the background color for the situation condition
		RelativeLayout layout = (RelativeLayout) convertView
				.findViewById(R.id.produtogrupo_layout);
		GradientDrawable bgShape = (GradientDrawable) layout.getBackground();

		if (item.codigo == null) {
			bgShape.setColor(Color.parseColor("#C5C1AA")); // gray
		} else {
			bgShape.setColor(Color.WHITE);
		}

		// number table
		TextView descricao = (TextView) convertView
				.findViewById(R.id.txtGrupoProdutoDescricao);
		TextView codigo = (TextView) convertView
				.findViewById(R.id.txtGrupoProdutoCodigo);

		// setting all values in gridview
		descricao.setText(item.descricao);
		codigo.setText(item.codigo);

		return convertView;
	}
}
