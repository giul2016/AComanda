package com.arquitetaweb.comanda.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.ProdutoComplementoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.comanda.popup.PedidoComplementoDialog;
import com.arquitetaweb.helper.sqllite.ProdutoComplementoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;

import java.util.ArrayList;
import java.util.List;

public class ProdutoComplementoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ConsumoModel> data;
	private List<ProdutoModel> produtoModelList;
	private static LayoutInflater inflater = null;

	public ProdutoComplementoAdapter(Activity activity, List<ConsumoModel> consumoModelList) {
		this.activity = activity;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        produtoModelList = new ArrayList<ProdutoModel>();
        ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
        for (ConsumoModel consumoModel : consumoModelList) {
            ProdutoModel produto = dbProduto
                    .selectById((long) consumoModel.produtoId);
            produtoModelList.add(produto);
        }
        dbProduto.closeDB();

        data = consumoModelList;
	}

	public List<ConsumoModel> getItens() {
		return data;
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
		return data.get(position).mesaId;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.pedido_info, null);
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
			if (produtoModel.id == getItem(position).produtoId) {
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
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView descricao;
		TextView codigo;
		TextView quantidade;
		ImageButton decrementa;
		ImageButton incrementa;
	}
}
