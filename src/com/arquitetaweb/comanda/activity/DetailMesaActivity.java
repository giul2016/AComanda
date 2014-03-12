package com.arquitetaweb.comanda.activity;

import java.util.Arrays;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.fragment.AboutFragment;
import com.arquitetaweb.comanda.fragment.DetailsFragment;
import com.arquitetaweb.comanda.fragment.MainFragment;
import com.arquitetaweb.comanda.fragment.SettingsFragment;
import com.arquitetaweb.comanda.fragment.SincronizationFragment;
import com.arquitetaweb.comum.messages.AlertaToast;

public class DetailMesaActivity extends Activity {

	private static final String SCHEME = "settings";
	private static final String AUTHORITY = "details";

	public static final Uri URI = new Uri.Builder().scheme(SCHEME)
			.authority(AUTHORITY).build();

	// acima ja existia

	private Fragment fragment = null;
	private String currentFragmentTag = null;
	private static final String STATE_URI = "state:uri";
	private static final String STATE_FRAGMENT_TAG = "state:fragment_tag";
	private Uri currentUri = DetailsFragment.DETAIL_URI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState != null) {
			currentUri = Uri.parse(savedInstanceState.getString(STATE_URI));
			currentFragmentTag = savedInstanceState
					.getString(STATE_FRAGMENT_TAG);
		}

		selectItem(currentUri);
	}

	private void selectItem(Uri uri) {
		if (DetailsFragment.DETAIL_URI.equals(uri)) {
			currentFragmentTag = DetailsFragment.TAG;
			fragment = new DetailsFragment();
		}

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		//String[] mActionLinks = getResources().getStringArray(
		//		R.array.actions_link);
		//Integer position = Arrays.asList(mActionLinks).indexOf(uri.toString());
		// setTitle(mActionTitles[position]);

		currentUri = uri;
	}

	@Override
	public void setTitle(CharSequence title) {
		// mTitle = title;
		getActionBar().setTitle("teste");
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(STATE_URI, currentUri.toString());
		outState.putString(STATE_FRAGMENT_TAG, currentFragmentTag);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}
}