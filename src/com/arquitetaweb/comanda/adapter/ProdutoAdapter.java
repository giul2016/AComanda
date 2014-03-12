package com.arquitetaweb.comanda.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
		
		final TextView quantidade = (TextView) vi
				.findViewById(R.id.txt_quantidade_produto);

		ImageButton decrementa = (ImageButton) vi
				.findViewById(R.id.btnRemoveProduto);
		decrementa.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				int qtde = Integer.parseInt(quantidade.getText().toString());
				if (qtde > 0) {
					qtde--;
					quantidade.setText(Integer.toString(qtde));
				}
			}
		});
		
		ImageButton incrementa = (ImageButton) vi
				.findViewById(R.id.btnAddProduto);		
		incrementa.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				int qtde = Integer.parseInt(quantidade.getText().toString());
				qtde++;
				quantidade.setText(Integer.toString(qtde));
			}
		});
		
		// get item from position
		ProdutoModel item = new ProdutoModel();
		item = data.get(position);

		// setting all values
		descricao.setText(item.descricao);
		codigo.setText(item.codigo);
		quantidade.setText("0");

		return vi;
	}
}
