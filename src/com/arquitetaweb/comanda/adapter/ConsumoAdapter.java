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
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;

public class ConsumoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ConsumoModel> data;
	private static LayoutInflater inflater = null;

	public ConsumoAdapter(Activity activity, List<ConsumoModel> lista) {
		this.activity = activity;
		data = lista;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.consumo_info, null);

			// get item from position
			ConsumoModel item = new ConsumoModel();
			item = data.get(position);

			// number table
			TextView nomeProduto = (TextView) vi
					.findViewById(R.id.txtnomeproduto);
			// Get Produto from DB SQLLite
			ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
			ProdutoModel produto = dbProduto.selectById((long) item.produtoid);
			dbProduto.closeDB();

			nomeProduto.setText(produto.descricao);

			TextView quantidade = (TextView) vi
					.findViewById(R.id.txtquantidade);
			quantidade.setText(item.quantidade);

			TextView valorUnitario = (TextView) vi
					.findViewById(R.id.txtvalorunitario);
			valorUnitario.setText("R$ 1,20");

			TextView valorTotal = (TextView) vi
					.findViewById(R.id.txtvalortotal);
			Double total = (1.20 * Integer.parseInt(item.quantidade));
			valorTotal.setText(String.format("R$ %.2f", total));
		}

		return vi;
	}
}
