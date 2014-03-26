package com.arquitetaweb.comanda.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.controller.MainController;

public class LoginFragment extends Fragment implements  View.OnClickListener  {

	private ProgressDialog progressDialog;
	private MainController controller;

	private static final String LOGIN_SCHEME = "category";
	private static final String LOGIN_AUTHORITY = "login";

	public static final String TAG = LoginFragment.class.getSimpleName();
	public static final Uri LOGIN_URI = new Uri.Builder().scheme(LOGIN_SCHEME)
			.authority(LOGIN_AUTHORITY).build();

	public LoginFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_login, container,
				false);

		return rootView;
	}

	@Override
	public void onDestroy() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {	
		
	}
}
