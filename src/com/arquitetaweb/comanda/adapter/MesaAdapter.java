package com.arquitetaweb.comanda.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.model.MesaModel;

public class MesaAdapter extends BaseAdapter implements Filterable {
	protected Activity activity;
	private List<MesaModel> dataOriginal;
	private List<MesaModel> data;
	private static LayoutInflater inflater = null;

	public MesaAdapter(Activity activity, List<MesaModel> listaMesa) {
		this.activity = activity;
		data = listaMesa;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
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
			vi = inflater.inflate(R.layout.mesa_grid, null);
		}
		TextView idItem = (TextView) vi.findViewById(R.id.idItem); // id
		TextView numero_mesa = (TextView) vi.findViewById(R.id.numero_mesa); // numero
		TextView situacao = (TextView) vi.findViewById(R.id.situacao); // situacao

		RelativeLayout layout = (RelativeLayout) vi.findViewById(R.id.mesa);		
		
		MesaModel item = new MesaModel();
		item = data.get(position);

		// Setting all values in gridview
		idItem.setText(item.id.toString());
		numero_mesa.setText(item.numeroMesa);
		situacao.setText(item.situacao);
		
		numero_mesa.setTextColor(Color.BLACK);
		String _situacao = situacao.getText().toString();
		if (_situacao.equals("1")) {
			layout.setBackgroundResource(R.drawable.bg_mesa_round_green);
			situacao.setTextColor(Color.parseColor("#008000"));
		} else if (_situacao.equals("2")) {
			layout.setBackgroundResource(R.drawable.bg_mesa_round_red);
			situacao.setTextColor(Color.parseColor("#FF0000"));
			numero_mesa.setTextColor(Color.WHITE);
		} else if (_situacao.equals("7")) {
			layout.setBackgroundResource(R.drawable.bg_mesa_round);
			situacao.setTextColor(Color.parseColor("#000080"));
		} else {
			layout.setBackgroundResource(R.drawable.bg_mesa_round);
			situacao.setTextColor(Color.parseColor("#000080"));
		}

		return vi;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence prefix) {
				FilterResults results = new FilterResults();
				List<MesaModel> FilteredByNumero = new ArrayList<MesaModel>();

				if(dataOriginal == null){
					dataOriginal = new ArrayList<MesaModel>(data);
                }
				
				if (prefix == null || prefix.length() == 0) {
					results.count = dataOriginal.size();
					results.values = dataOriginal;
				} else {
					for (int i = 0; i < dataOriginal.size(); i++) {
						MesaModel mesaObject = dataOriginal.get(i);
						// pesquisa pelo numero da mesa
						if (mesaObject.numeroMesa.startsWith(prefix.toString())) {
							FilteredByNumero.add(mesaObject);
						}
					}

					results.count = FilteredByNumero.size();
					System.out.println(results.count);

					results.values = FilteredByNumero;
				}

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
	
				//dataOriginal = (List<MesaModel>) results.values;
				data = (List<MesaModel>) results.values;
				notifyDataSetChanged();
			}
		};
	}

}
