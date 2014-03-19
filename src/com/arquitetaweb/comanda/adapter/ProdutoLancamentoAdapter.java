package com.arquitetaweb.comanda.adapter;

import java.util.ArrayList;
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
import android.widget.Toast;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;

public class ProdutoLancamentoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ConsumoModel> data;
	private static LayoutInflater inflater = null;

	public ProdutoLancamentoAdapter(Activity activity, long idProduto) {
		this.activity = activity;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
		List<ProdutoModel> produtoList = dbProduto
				.selectWhere("produtoGrupoId = " + idProduto);
		dbProduto.closeDB();

		data = new ArrayList<ConsumoModel>();
		for (ProdutoModel produtoModel : produtoList) {

			ConsumoModel item = new ConsumoModel();
			item.mesaid = (long) 1;
			item.deviceid = (long) 1;
			item.produtoid = produtoModel.id;
			item.quantidade = "0";
			
			Log.i("AQUI", "Prod. " + produtoModel.id);
			
			data.add(item);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public ConsumoModel getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).mesaid;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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
						data.get(position).quantidade = Integer.toString(qtde);
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
					data.get(position).quantidade = Integer.toString(qtde);

					Log.i("AQUI", Integer.toString(position));
					Log.i("AQUI", "idPrdo " + getItem(position).produtoid);

					notifyDataSetChanged();
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Get Produto from DB SQLLite
		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
		ProdutoModel produto = dbProduto
				.selectById((long) data.get(position).produtoid);
		dbProduto.closeDB();

		holder.descricao.setText(produto.descricao);
		holder.codigo.setText(produto.codigo);
		holder.quantidade.setText(data.get(position).quantidade);

		return convertView;
	}
}
