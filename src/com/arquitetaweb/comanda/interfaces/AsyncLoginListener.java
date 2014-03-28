package com.arquitetaweb.comanda.interfaces;

import android.util.Pair;

public interface AsyncLoginListener {
	public void onLoginComplete(Pair<Boolean, String> result);
}