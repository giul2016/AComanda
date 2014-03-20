/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arquitetaweb.comanda.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.activity.PedidoActivity;
import com.arquitetaweb.comanda.adapter.PedidoAdapter;
import com.arquitetaweb.comanda.controller.PedidoController;
import com.arquitetaweb.comanda.interfaces.AsyncTaskListener;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comum.component.ListViewCustom;

public class PedidoFragment extends ListFragment implements AsyncTaskListener {

	private ProgressDialog progressDialog;
	private PedidoController controller;
	private MesaModel mesa;
	private long idProdutoGrupo;
	private Fragment fragment;
	private PedidoAdapter adapter; 
			
	private ListViewCustom mListView;
	private LinearLayout mQuickReturnView;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;

	private int mState = STATE_ONSCREEN;

	private int mQuickReturnHeight;
	private int mScrollY;
	private int mMinRawY = 0;

	private TranslateAnimation anim;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pedido, null);
		mQuickReturnView = (LinearLayout) view.findViewById(R.id.footer1);
		fragment = this;

		Button btnFechaConta = (Button) view.findViewById(
				R.id.lancar_pedido);
		btnFechaConta.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				controller.fecharConta(adapter); // onTaskComplete callback
			}		
		});
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mesa = ((PedidoActivity) this.getActivity()).getMesaModel();
		progressDialog = new ProgressDialog(this.getActivity());		
		controller = new PedidoController(fragment, progressDialog, mesa.id);
		
		idProdutoGrupo = ((PedidoActivity) this.getActivity())
				.getIdProdutoGrupo();

		adapter = new PedidoAdapter(this.getActivity(),
				idProdutoGrupo);

		setListAdapter(adapter);

		mListView = (ListViewCustom) getListView();

		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mQuickReturnHeight = mQuickReturnView.getHeight();
						mListView.computeScrollY();
					}
				});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				int translationY = bottonAnim();

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			private int bottonAnim() {
				// bottom
				mScrollY = 0;
				int translationY = 0;

				if (mListView.scrollYIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}

				int rawY = mScrollY;

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY >= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:
					translationY = (rawY - mMinRawY) + mQuickReturnHeight;
					if (translationY < 0) {
						translationY = 0;
						mMinRawY = rawY + mQuickReturnHeight;
					}

					if (rawY == 0) {
						mState = STATE_ONSCREEN;
						translationY = 0;
					}

					if (translationY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}
				return translationY;
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//
			}
		});
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
	public void onClosedComplete(Boolean result) {
		if (result) {
			((PedidoActivity) this.getActivity()).onBackPressed();
			Toast toast = Toast.makeText(getActivity(), "Conta fechada com sucesso!",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	@Override
	public void onTaskComplete(List<ConsumoModel> result) {
		// TODO Auto-generated method stub
		
	}
}