package com.arquitetaweb.comanda.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.controller.ProdutoComplementoController;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.ProdutoComplementoModel;
import com.arquitetaweb.comanda.model.ProdutoModel;
import com.arquitetaweb.comanda.popup.PedidoComplementoDialog;
import com.arquitetaweb.helper.sqllite.ProdutoComplementoGenericHelper;
import com.arquitetaweb.helper.sqllite.ProdutoGenericHelper;

public class PedidoAdapter extends BaseAdapter {
	protected Activity activity;
	private List<ConsumoModel> data;
	private List<ProdutoModel> produtoModelList;
	private static LayoutInflater inflater = null;

	public PedidoAdapter(Activity activity, long idProdutoGrupo,
			List<ConsumoModel> consumoModelList) {
		this.activity = activity;
		data = consumoModelList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        init(activity, consumoModelList);
	}

    private void init(Activity activity, List<ConsumoModel> consumoModelList) {
        produtoModelList = new ArrayList<ProdutoModel>();
        for (ConsumoModel consumoModel : consumoModelList) {
            ProdutoGenericHelper dbProduto = new ProdutoGenericHelper(activity);
            ProdutoModel produto = dbProduto
                    .selectById((long) consumoModel.produtoId);
            dbProduto.closeDB();

            ProdutoComplementoGenericHelper dbProdutoComplemento = new ProdutoComplementoGenericHelper(activity);
            List<ProdutoComplementoModel> listaProduto = dbProdutoComplemento.selectWhere("produtoId = " + consumoModel.produtoId);
            dbProdutoComplemento.closeDB();

            produto.tem_complemento  =!listaProduto.isEmpty();

            produtoModelList.add(produto);
        }
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


                if (produtoModel.tem_complemento) {
                    holder.codigo.setTextColor(Color.BLUE);
                } else {
                    holder.codigo.setTextColor(Color.BLACK);
                }
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
                openProdutoComplemento(getItem(position).produtoId);
			}
		});
		return convertView;
	}

    private void openProdutoComplemento(long idProduto) {

        ProdutoComplementoController controller = new ProdutoComplementoController(activity);
        List<ConsumoModel> listConsumo = controller.sincronizar(idProduto);

        if (!listConsumo.isEmpty()) {
            FragmentManager fragmentManager = activity.getFragmentManager();
            DialogFragment newFragment = new PedidoComplementoDialog(listConsumo);
            newFragment.show(fragmentManager, null);
        }
    }

	static class ViewHolder {
		TextView descricao;
		TextView codigo;
		TextView quantidade;
		ImageButton decrementa;
		ImageButton incrementa;
	}
}
