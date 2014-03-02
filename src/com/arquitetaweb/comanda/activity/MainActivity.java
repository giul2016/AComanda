package com.arquitetaweb.comanda.activity;

import java.util.Arrays;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.adapter.MesaAdapter;
import com.arquitetaweb.comanda.dados.GetMesas;
import com.arquitetaweb.comanda.fragment.AboutFragment;
import com.arquitetaweb.comanda.fragment.MainFragment;
import com.arquitetaweb.comanda.fragment.SettingsFragment;
import com.arquitetaweb.comanda.fragment.SincronizationFragment;
import com.arquitetaweb.comanda.util.KeyboardAction;
import com.arquitetaweb.comum.messages.AlertaToast;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private SearchView searchView;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mActionTitles;
	private Fragment fragment = null;
	private String currentFragmentTag = null;
	private KeyboardAction kb = new KeyboardAction();

	// Refresh menu item
	private MenuItem refreshMenuItem;

	private static final String STATE_URI = "state:uri";
	private static final String STATE_FRAGMENT_TAG = "state:fragment_tag";
	private Uri currentUri = MainFragment.MESAS_URI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = "Menu Comanda"; // getTitle();
		mActionTitles = getResources().getStringArray(R.array.actions_titles);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mActionTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState != null) {
			currentUri = Uri.parse(savedInstanceState.getString(STATE_URI));
			currentFragmentTag = savedInstanceState
					.getString(STATE_FRAGMENT_TAG);
		}
		selectItem(currentUri);
	}

	private void clearSearch() {
		if ((Build.VERSION.SDK_INT >= 11)
				&& (currentFragmentTag.equals(MainFragment.TAG))) {
			searchView.setQuery("", false);
			searchView.setIconified(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		if ((Build.VERSION.SDK_INT >= 11)
				&& (currentFragmentTag.equals(MainFragment.TAG))) {
			// SearchView
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

			searchView = (SearchView) menu.findItem(R.id.action_search)
					.getActionView();
			// searchView.setQueryHint("Digite o Número da Mesa");
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));

			searchView.setOnQueryTextListener(new OnQueryTextListener() {

				@Override
				public boolean onQueryTextChange(String arg0) {
					GridView mesas = (GridView) fragment.getView()
							.findViewById(R.id.list);
					MesaAdapter v = (MesaAdapter) mesas.getAdapter();
					if (v != null) {
						v.getFilter().filter(arg0);
					}

					return true;
				}

				@Override
				public boolean onQueryTextSubmit(String arg0) {
					GridView mesaGrid = (GridView) fragment.getView()
							.findViewById(R.id.list);
					MesaAdapter mesaAdapter = (MesaAdapter) mesaGrid
							.getAdapter();
					if (mesaAdapter.getCount() > 0) {
						Long idItem = mesaAdapter.getItemId(0);

						Bundle bun = new Bundle();
						bun.putString("id", idItem.toString());
						Intent intent = new Intent(fragment.getView()
								.getContext(), DetailsActivity.class);
						intent.putExtras(bun);
						fragment.startActivityForResult(intent, 100);
					} else {
						new AlertaToast().show(fragment.getActivity(),
								"Nenhuma mesa localizada pelo critério: "
										+ arg0 + "\nVerifique sua consulta.");
					}
					return true;
				}

			});
		}

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList)
				|| (!MainFragment.TAG.equals(currentFragmentTag));

		// menu search mesa e atualizar mesa
		menu.findItem(R.id.action_refresh_mapa).setVisible(!drawerOpen);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);

		clearSearch();
		kb.hide(this);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_refresh_mapa:
			// // create intent to perform web search for this planet
			// Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			// intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// // catch event that there's no activity to handle intent
			// if (intent.resolveActivity(getPackageManager()) != null) {
			// startActivity(intent);
			// } else {
			// Toast.makeText(this, R.string.app_not_available,
			// Toast.LENGTH_LONG).show();
			// }

			refreshMenuItem = item;
			// load the data from server
			new SyncData().execute();
			clearSearch();
			return true;
		case R.id.action_search:
			// openSearch();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Uri uri = Uri.parse(getResources().getStringArray(
					R.array.actions_link)[position]);
			selectItem(uri);
		}
	}

	private void selectItem(Uri uri) {
		if (SettingsFragment.GENERAL_SETTINGS_URI.equals(uri)) {
			currentFragmentTag = SettingsFragment.TAG;
			fragment = new SettingsFragment();
		} else if (MainFragment.MESAS_URI.equals(uri)) {
			currentFragmentTag = MainFragment.TAG;
			fragment = new MainFragment();
		} else if (AboutFragment.ABOUT_URI.equals(uri)) {
			currentFragmentTag = AboutFragment.TAG;
			fragment = new AboutFragment();
		} else if (SincronizationFragment.SINCRONIZATION_URI.equals(uri)) {
			currentFragmentTag = SincronizationFragment.TAG;
			fragment = new SincronizationFragment();
		} else {
			currentFragmentTag = MainFragment.TAG;
			fragment = new MainFragment();
			new AlertaToast().show(this, "Fragment Not Found");
		}

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		String[] mActionLinks = getResources().getStringArray(
				R.array.actions_link);
		Integer position = Arrays.asList(mActionLinks).indexOf(uri.toString());
		setTitle(mActionTitles[position]);
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);

		currentUri = uri;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(STATE_URI, currentUri.toString());
		outState.putString(STATE_FRAGMENT_TAG, currentFragmentTag);
		super.onSaveInstanceState(outState);
	}

	/**
	 * Async task to load the data from server
	 * **/
	private class SyncData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			refreshMenuItem.setActionView(R.layout.action_progressbar);
			refreshMenuItem.expandActionView();
		}

		@Override
		protected String doInBackground(String... params) {
			GetMesas getMesas = new GetMesas(fragment);
			getMesas.reload();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			refreshMenuItem.collapseActionView();
			// remove the progress bar view
			refreshMenuItem.setActionView(null);
		}
	};
}