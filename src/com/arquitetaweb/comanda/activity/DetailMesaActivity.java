package com.arquitetaweb.comanda.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.fragment.ConsumoFragment;
import com.arquitetaweb.comanda.fragment.ProdutoFragment;
import com.arquitetaweb.comanda.model.MesaModel;
import com.google.gson.Gson;

public class DetailMesaActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager mPager;
	private MesaModel mesa;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		// button back
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mesa = (MesaModel) getIntent().getSerializableExtra("mesa");

		setTitle("Mesa: " + mesa.numero_mesa);

		// TAB
		PagerAdapter adapter = new FragmentStatePagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				switch (position) {
				case 0:
					return new ProdutoFragment();
				case 1:
					return new ConsumoFragment();
				}
				return null;
			}

			@Override
			public int getCount() {
				return 2;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch (position) {
				case 0:
					return getString(R.string.tab_produto);
				case 1:
					return getString(R.string.tab_consumo);
				}
				return null;
			}
		};

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				getActionBar().setSelectedNavigationItem(position);
			}
		});

		mPager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.page_margin));

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (int position = 0; position < adapter.getCount(); position++) {
			getActionBar().addTab(
					getActionBar().newTab()
							.setText(adapter.getPageTitle(position))
							.setTabListener(this));
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

	public MesaModel getMesaModel() {
		return this.mesa;
	}
}
