package com.arquitetaweb.comanda.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;

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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.produto_info, null);
			holder = new ViewHolder();
			holder.codigo = (TextView) convertView
					.findViewById(R.id.txtprodutocodigo);
			holder.descricao = (TextView) convertView
					.findViewById(R.id.txtprodutodescricao);
			holder.quantidade = (TextView) convertView
					.findViewById(R.id.txt_quantidade_produto);

			ImageButton decrementa = (ImageButton) convertView
					.findViewById(R.id.btnRemoveProduto);
			decrementa.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int qtde = Integer.parseInt(holder.quantidade.getText()
							.toString());
					if (qtde > 0) {
						qtde--;
						holder.quantidade.setText(Integer.toString(qtde));
						notifyDataSetChanged();
					}
				}
			});

			ImageButton incrementa = (ImageButton) convertView
					.findViewById(R.id.btnAddProduto);
			incrementa.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int qtde = Integer.parseInt(holder.quantidade.getText()
							.toString());
					qtde++;
					holder.quantidade.setText(Integer.toString(qtde));
				}
			});
			
			ProdutoModel item = new ProdutoModel();
			item = data.get(position);
			// Get Produto from DB SQLLite
			ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
			ProdutoModel produto = dbProduto.selectById((long) item.id);
			dbProduto.closeDB();

			holder.descricao.setText(produto.descricao);
			holder.codigo.setText(produto.codigo);
			holder.quantidade.setText("0");

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

			
		}
		// get item from position
		// ProdutoModel item = new ProdutoModel();
		// item = data.get(position);
		//
		// Log.d("selectFromObjectGeneric", item.id + " - " + item.codigo +
		// " - "
		// + position);
		//
		// // setting all values
		// holder.descricao.setText(item.descricao);
		// holder.codigo.setText(item.codigo);


		return convertView;
	}
}
