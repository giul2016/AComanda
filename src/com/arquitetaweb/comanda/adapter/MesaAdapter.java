package com.arquitetaweb.comanda.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
	public MesaModel getItem(int position) {
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
			vi = inflater.inflate(R.layout.mesa_info, null);
		}
		
		// number table
		TextView numero_mesa = (TextView) vi.findViewById(R.id.numero_mesa); 
		
		// change the background color for the situation condition
		RelativeLayout layout = (RelativeLayout) vi.findViewById(R.id.mesa);
		GradientDrawable bgShape = (GradientDrawable) layout.getBackground(); 

		// get item from position
		MesaModel item = new MesaModel();
		item = data.get(position);

		// setting all values in gridview
		numero_mesa.setText(item.numero_mesa);
		numero_mesa.setTextColor(Color.BLACK); // reset font color

		switch (item.situacao) {
			case Livre: // 1
				bgShape.setColor(Color.parseColor("#43CD80")); // green
				break;
			case Ocupada: // 2
				bgShape.setColor(Color.parseColor("#FF4040")); // red
				numero_mesa.setTextColor(Color.WHITE);
				break;
			case EmConta: // 3
				bgShape.setColor(Color.parseColor("#EEFF44")); // yellow
				break;
			case Limpar: // 4
				bgShape.setColor(Color.parseColor("#CF7C2C")); // brow
				numero_mesa.setTextColor(Color.WHITE); 
				break;
			case Agrupada: // 5
				bgShape.setColor(Color.parseColor("#C5C1AA")); // gray
				break;
			case Reservada: // 6
				bgShape.setColor(Color.parseColor("#946E6E")); // red
				numero_mesa.setTextColor(Color.WHITE);
				break;
			case Ociosa: // 7
				bgShape.setColor(Color.parseColor("#587AC4")); // blink blue
				numero_mesa.setTextColor(Color.WHITE);
				break;
			case Indefinido: // 8
				bgShape.setColor(Color.WHITE); // white 
				break;
			default:
				bgShape.setColor(Color.WHITE); // white
				break;
		}

		return vi;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence prefix) {
				FilterResults results = new FilterResults();
				List<MesaModel> FilteredByNumero = new ArrayList<MesaModel>();

				if (dataOriginal == null) {
					dataOriginal = new ArrayList<MesaModel>(data);
				}

				if (prefix == null || prefix.length() == 0) {
					results.count = dataOriginal.size();
					results.values = dataOriginal;
				} else {
					for (int i = 0; i < dataOriginal.size(); i++) {
						MesaModel mesaObject = dataOriginal.get(i);
						// search by condition table started by criterion
						if (mesaObject.numero_mesa
								.startsWith(prefix.toString())) {
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

				// dataOriginal = (List<MesaModel>) results.values;
				data = (List<MesaModel>) results.values;
				notifyDataSetChanged();
			}
		};
	}
}
