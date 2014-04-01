package com.arquitetaweb.comanda.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arquitetaweb.comanda.R;

public class AboutFragment extends Fragment implements View.OnClickListener {
	public static final String TAG = AboutFragment.class.getSimpleName();
	private static final String SETTINGS_SCHEME = "category";
	private static final String SETTINGS_AUTHORITY = "about";
	public static final Uri ABOUT_URI = new Uri.Builder()
			.scheme(SETTINGS_SCHEME).authority(SETTINGS_AUTHORITY).build();

	private View viewRoot;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewRoot = inflater.inflate(R.layout.fragment_about, container, false);
		return viewRoot;
	}

	@Override
	public void onClick(View v) {
		
	}	
}
