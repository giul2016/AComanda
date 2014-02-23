package com.arquitetaweb.comanda.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.util.JSONParser;

public class DetailsActivity extends FragmentActivity {

	private static final String STATE_POSITION = "state:layout_id";

	private static final String SCHEME = "settings";
	private static final String AUTHORITY = "details";
	public static final Uri URI = new Uri.Builder().scheme(SCHEME)
			.authority(AUTHORITY).build();

	private ListView viewList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.details);

		// get the action bar
		ActionBar actionBar = getActionBar();
		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);

		final String idString = getIntent().getExtras().get("id").toString();

		final TextView id = (TextView) findViewById(R.id.teste);
		id.setText(idString);

		final Button btnLivre = (Button) findViewById(R.id.btnLivre);
		btnLivre.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Livre",
						Toast.LENGTH_SHORT).show();

				new AtualizarMesa(v.getContext()).execute("1", idString);
			}
		});

		final Button btnOcupada = (Button) findViewById(R.id.btnCcupada);
		btnOcupada.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Ocupada",
						Toast.LENGTH_SHORT).show();

				new AtualizarMesa(v.getContext()).execute("2", idString);
			}
		});

		final Button btnOciosa = (Button) findViewById(R.id.btnOciosa);
		btnOciosa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Ociosa",
						Toast.LENGTH_SHORT).show();
				new AtualizarMesa(v.getContext()).execute("7", idString);
			}
		});

	}

	private class AtualizarMesa extends AsyncTask<String, Void, Void> {
		private Context myCtx;

		@Override
		protected Void doInBackground(String... params) {
			final JSONParser jParser = new JSONParser();
			jParser.atualizaMesa(params[0], params[1], (Activity) myCtx);

			setResult(Activity.RESULT_OK);
			finish();
			return null;
		}

		public AtualizarMesa(Context ctx) {
			// Now set context
			this.myCtx = ctx;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		final int position = viewList.getSelectedItemPosition();
		if (position != ListView.INVALID_POSITION)
			outState.putInt(STATE_POSITION, position);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

}
