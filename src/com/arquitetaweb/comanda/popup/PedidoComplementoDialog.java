package com.arquitetaweb.comanda.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.adapter.ProdutoComplementoAdapter;
import com.arquitetaweb.comanda.model.ConsumoModel;

import java.util.List;

/**
 * Created by Marcos on 20/09/2014.
 */
public class PedidoComplementoDialog extends DialogFragment {

    private List<ConsumoModel> listConsumo;

    public PedidoComplementoDialog(List<ConsumoModel> listConsumo) {
        this.listConsumo = listConsumo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = getActivity().getLayoutInflater().inflate(R.layout.popup_pedido, null);

        ListView mComplementoList = (ListView)v.findViewById(R.id.listcomplemento);

        ProdutoComplementoAdapter adapter = new ProdutoComplementoAdapter(this.getActivity(), listConsumo);
        mComplementoList.setAdapter(adapter);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.btnSalvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.btnCancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PedidoComplementoDialog.this.getDialog().cancel();
                    }
                });

        //Button positiveButton = builder.getButton(DialogInterface.BUTTON_POSITIVE);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Button pButton =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        Button nButton =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
//        pButton.setBackgroundColor(Color.BLUE);
//        nButton.setBackgroundColor(Color.RED);
//        pButton.setTextSize(10);
//        pButton.setBackground(getResources().getDrawable(R.drawable.bg_login));
//        nButton.setBackground(getResources().getDrawable(R.drawable.bg_login));
    }
}
