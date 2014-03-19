package com.arquitetaweb.comanda.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
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

	static class ViewHolder {
		TextView descricao;
		TextView codigo;
		TextView quantidade;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.produto_info, null);
			holder = new ViewHolder();
			holder.codigo = (TextView) vi.findViewById(R.id.txtprodutocodigo);
			holder.descricao = (TextView) vi.findViewById(R.id.txtprodutodescricao);
			holder.quantidade = (TextView) vi.findViewById(R.id.txt_quantidade_produto);
			
			convertView.setTag(holder);
			
			holder.codigo.setTag(holder.codigo);
			holder.descricao.setTag(holder.descricao);
			holder.quantidade.setTag(holder.quantidade);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
			
//			// number table
//			TextView descricao = (TextView) vi
//					.findViewById(R.id.txtprodutodescricao);
//			TextView codigo = (TextView) vi.findViewById(R.id.txtprodutocodigo);
//
//			final TextView quantidade = (TextView) vi
//					.findViewById(R.id.txt_quantidade_produto);
//
			ImageButton decrementa = (ImageButton) vi
					.findViewById(R.id.btnRemoveProduto);
			decrementa.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int qtde = Integer
							.parseInt(holder.quantidade.getText().toString());
					if (qtde > 0) {
						qtde--;
						holder.quantidade.setText(Integer.toString(qtde));
					}
				}
			});

			ImageButton incrementa = (ImageButton) vi
					.findViewById(R.id.btnAddProduto);
			incrementa.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int qtde = Integer
							.parseInt(holder.quantidade.getText().toString());
					qtde++;
					holder.quantidade.setText(Integer.toString(qtde));
				}
			});

			// get item from position
			ProdutoModel item = new ProdutoModel();
			item = data.get(position);

			Log.d("selectFromObjectGeneric", item.id + " - " + item.codigo
					+ " - " + position);

			// setting all values
			holder.descricao.setText(item.descricao);
			holder.codigo.setText(item.codigo);
			holder.quantidade.setText("0");
		}
		return vi;
	}
}
