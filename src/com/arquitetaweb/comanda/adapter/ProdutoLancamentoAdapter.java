package com.arquitetaweb.comanda.adapter;

import java.util.ArrayList;
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
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;

public class ProdutoLancamentoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ConsumoModel> data;
	private List<ProdutoModel> produtoModelList;
	private static LayoutInflater inflater = null;

	public ProdutoLancamentoAdapter(Activity activity, long idProduto) {
		this.activity = activity;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
		produtoModelList = dbProduto.selectWhere("produtoGrupoId = "
				+ idProduto);
		dbProduto.closeDB();

		data = new ArrayList<ConsumoModel>();
		for (ProdutoModel produtoModel : produtoModelList) {

			ConsumoModel item = new ConsumoModel();
			item.mesaid = (long) 1;
			item.deviceid = (long) 1;
			item.produtoid = produtoModel.id;
			item.quantidade = "0";
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

	public void notifyDataSetChangedThread() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView descricao;
		TextView codigo;
		TextView quantidade;
		ImageButton decrementa;
		ImageButton incrementa;
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

			holder.decrementa = (ImageButton) convertView
					.findViewById(R.id.btnRemoveProduto);
			holder.incrementa = (ImageButton) convertView
					.findViewById(R.id.btnAddProduto);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		for (ProdutoModel produtoModel : produtoModelList) {
			if (produtoModel.id == getItem(position).produtoid) {
				holder.descricao.setText(produtoModel.descricao);
				holder.codigo.setText(produtoModel.codigo);
				holder.quantidade.setText(getItem(position).quantidade);
			}
		}
		
		holder.decrementa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int qtde = Integer.parseInt(getItem(position).quantidade);
				if (qtde > 0) {
					qtde--;
					getItem(position).quantidade = Integer.toString(qtde);
					notifyDataSetChanged();
				}
			}
		});

		holder.incrementa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int qtde = Integer.parseInt(getItem(position).quantidade);
				qtde++;
				getItem(position).quantidade = Integer.toString(qtde);
				notifyDataSetChanged();
				// notifyDataSetChangedThread();
			}
		});
		return convertView;
	}
}
