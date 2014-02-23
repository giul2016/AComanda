package com.arquitetaweb.comanda.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import com.arquitetaweb.comanda.dados.GetMesas;

public class MesaAdapter extends BaseAdapter implements Filterable {

	private Activity activity;
	private ArrayList<HashMap<String, String>> dataOriginal;
	private ArrayList<HashMap<String, String>> dataFiltered;
	private static LayoutInflater inflater = null;

	public MesaAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		dataOriginal = d;
		dataFiltered = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return dataOriginal.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.mesa_grid, null);
		}
		TextView idItem = (TextView) vi.findViewById(R.id.idItem); // id
		TextView numero_mesa = (TextView) vi.findViewById(R.id.numero_mesa); // numero
		TextView situacao = (TextView) vi.findViewById(R.id.situacao); // situacao

		RelativeLayout layout = (RelativeLayout) vi.findViewById(R.id.mesa);

		HashMap<String, String> itens = new HashMap<String, String>();
		itens = dataOriginal.get(position);

		// Setting all values in listview
		idItem.setText(itens.get(GetMesas.KEY_ID));
		numero_mesa.setText(itens.get(GetMesas.KEY_NUMEROMESA));
		situacao.setText(itens.get(GetMesas.KEY_SITUACAO));

		numero_mesa.setTextColor(Color.BLACK);

		String _situacao = situacao.getText().toString();
		if (_situacao.equals("1")) {
			layout.setBackgroundColor(Color.GREEN);
			situacao.setTextColor(Color.parseColor("#008000"));
			// situacao_mesa.setImageResource(R.drawable.ic_link_github);
		} else if (_situacao.equals("2")) {
			layout.setBackgroundColor(Color.RED);
			situacao.setTextColor(Color.parseColor("#FF0000"));
			numero_mesa.setTextColor(Color.WHITE);
			// situacao_mesa.setImageResource(R.drawable.ic_link_evernote);
		} else if (_situacao.equals("7")) {
			layout.setBackgroundColor(Color.YELLOW);
			situacao.setTextColor(Color.parseColor("#000080"));
			// situacao_mesa.setImageResource(R.drawable.ic_link_stackoverflow);
		} else {
			layout.setBackgroundColor(Color.CYAN);
			situacao.setTextColor(Color.parseColor("#000080"));
			// situacao_mesa.setImageResource(R.drawable.ic_link_lastfm);
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

				if (prefix != null && prefix.toString().length() > 0) {
					ArrayList<HashMap<String, String>> founded = new ArrayList<HashMap<String, String>>();
					for (HashMap<String, String> item : dataOriginal) {
						if (item.toString().contains(prefix)) {
							founded.add(item);
						}
					}
					results.values = founded;
					results.count = founded.size();
				} else {
					results.values = dataOriginal;
					results.count = dataOriginal.size();
				}

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				dataOriginal.clear();
				//data.addAll((Collection<? extends HashMap<String, String>>) results.values);
				
				for (HashMap<String, String> item : (List<HashMap<String, String>>)results.values) {
	                 dataOriginal.add(item);
	           }
				notifyDataSetChanged();
			}
		};
	}

}
