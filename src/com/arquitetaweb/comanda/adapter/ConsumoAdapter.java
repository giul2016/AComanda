package com.arquitetaweb.comanda.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;

public class ConsumoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ConsumoModel> consumoLista;
	private List<ProdutoModel> produtoModelList;
	private static LayoutInflater inflater = null;

	public ConsumoAdapter(Activity activity, List<ConsumoModel> lista) {
		this.activity = activity;
		consumoLista = lista;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		produtoModelList = new ArrayList<ProdutoModel>();
		ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
		for (ConsumoModel consumoModel : consumoLista) {
			ProdutoModel produto = dbProduto
					.selectById((long) consumoModel.produtoid);
			produtoModelList.add(produto);
		}
		dbProduto.closeDB();
	}

	@Override
	public int getCount() {
		return consumoLista.size();
	}

	@Override
	public ConsumoModel getItem(int position) {
		return consumoLista.get(position);
	}

	@Override
	public long getItemId(int position) {
		return consumoLista.get(position).mesaid;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.consumo_info, null);

			holder = new ViewHolder();
			holder.descricao = (TextView) convertView
					.findViewById(R.id.txtnomeproduto);
			holder.quantidade = (TextView) convertView
					.findViewById(R.id.txtquantidade);
			holder.valorUnitario = (TextView) convertView
					.findViewById(R.id.txtvalorunitario);
			holder.valorTotal = (TextView) convertView
					.findViewById(R.id.txtvalortotal);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// get item from position
		ConsumoModel item = new ConsumoModel();
		item = consumoLista.get(position);

		for (ProdutoModel produtoModel : produtoModelList) {
			if (produtoModel.id == item.produtoid) {
				holder.descricao.setText(produtoModel.descricao);
				holder.quantidade.setText(item.quantidade);
				holder.valorUnitario.setText("R$ 1,99");

				Double total = (1.99 * Integer.parseInt(item.quantidade));
				holder.valorTotal.setText(String.format("R$ %.2f", total));
			}
		}
		return convertView;
	}

	static class ViewHolder {
		TextView descricao;
		TextView codigo;
		TextView quantidade;
		TextView valorUnitario;
		TextView valorTotal;
	}
}
