package com.arquitetaweb.comanda.interfaces;

import java.util.List;

import com.arquitetaweb.comanda.model.ConsumoModel;

public interface AsyncTaskListener{
    public void onTaskComplete(List<ConsumoModel> result);
    
    public void onClosedComplete(Boolean result);
}