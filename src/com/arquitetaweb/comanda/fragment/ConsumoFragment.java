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
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
import com.arquitetaweb.comanda.activity.DetailMesaActivity;
import com.arquitetaweb.comanda.adapter.ConsumoAdapter;
import com.arquitetaweb.comanda.controller.ConsumoController;
import com.arquitetaweb.comanda.interfaces.AsyncTaskListener;
import com.arquitetaweb.comanda.model.ConsumoModel;
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comanda.model.model_enum.SituacaoMesa;
import com.arquitetaweb.comum.component.ListViewCustom;

public class ConsumoFragment extends ListFragment implements AsyncTaskListener {

	private ProgressDialog progressDialog;
	private ConsumoController controller;
	private MesaModel mesa;
	private Fragment fragment;

	private ListViewCustom mListView;
	private LinearLayout mQuickReturnView;
	private View mHeader;

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
		View view = inflater.inflate(R.layout.fragment_consumo, null);
		mQuickReturnView = (LinearLayout) view.findViewById(R.id.footer1);
		mHeader = inflater.inflate(R.layout.consumo_header, null);
		fragment = this;
		return view;
	}

	@SuppressWarnings("unused")
	private void showFooter(Integer seconds) {
		Handler handler = null;
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				mQuickReturnView.setTranslationY(0);
			}
		}, seconds * 1000);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mesa = ((DetailMesaActivity) this.getActivity()).getMesaModel();
		progressDialog = new ProgressDialog(this.getActivity());
		controller = new ConsumoController(fragment, progressDialog, mesa.id);
		controller.sincronizar(); // onTaskComplete callback

		Button btnFechaConta = (Button) fragment.getView().findViewById(
				R.id.fechar_conta);

		if (mesa.situacao == SituacaoMesa.EmConta) {
			btnFechaConta.setText("Mesa Já Fechada");
			btnFechaConta.setBackgroundColor(Color.RED);
		} else {
			btnFechaConta.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					controller.fecharConta(); // onTaskComplete callback
				}
			});
		}

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
	public void onTaskComplete(List<ConsumoModel> result) {
		ConsumoAdapter adapter = new ConsumoAdapter(this.getActivity(), result);

		setListAdapter(adapter);

		mListView = (ListViewCustom) getListView();
		//mListView.addHeaderView(mHeader);

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

			// private int animation() {
			// mScrollYtop = 0;
			// int translationY = 0;
			//
			// if (mListView.scrollYIsComputed()) {
			// mScrollYtop = mListView.getComputedScrollY();
			// }
			//
			// rawY = mPlaceHolder.getTop()
			// - Math.min(
			// mCachedVerticalScrollRange
			// - mListView.getHeight(), mScrollYtop);
			//
			// switch (mState) {
			// case STATE_OFFSCREEN:
			// if (rawY <= mMinRawYtop) {
			// mMinRawYtop = rawY;
			// } else {
			// mState = STATE_RETURNING;
			// }
			// translationY = rawY;
			// break;
			//
			// case STATE_ONSCREEN:
			// if (rawY < -mQuickReturnHeightTop) {
			// mState = STATE_OFFSCREEN;
			// mMinRawYtop = rawY;
			// }
			// translationY = rawY;
			// break;
			//
			// case STATE_RETURNING:
			//
			// if (translationY > 0) {
			// translationY = 0;
			// mMinRawYtop = rawY - mQuickReturnHeightTop;
			// }
			//
			// else if (rawY > 0) {
			// mState = STATE_ONSCREEN;
			// translationY = rawY;
			// }
			//
			// else if (translationY < -mQuickReturnHeightTop) {
			// mState = STATE_OFFSCREEN;
			// mMinRawYtop = rawY;
			//
			// } else if (mQuickReturnViewTop.getTranslationY() != 0
			// && !noAnimation) {
			// noAnimation = true;
			// animTop = new TranslateAnimation(0, 0,
			// -mQuickReturnHeightTop, 0);
			// animTop.setFillAfter(true);
			// animTop.setDuration(250);
			// mQuickReturnViewTop.startAnimation(animTop);
			// animTop.setAnimationListener(new AnimationListener() {
			//
			// @Override
			// public void onAnimationStart(Animation animation) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public void onAnimationRepeat(Animation animation) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public void onAnimationEnd(Animation animation) {
			// noAnimation = false;
			// mMinRawYtop = rawY;
			// mState = STATE_EXPANDED;
			// }
			// });
			// }
			// break;
			//
			// case STATE_EXPANDED:
			// if (rawY < mMinRawYtop - 2 && !noAnimation) {
			// noAnimation = true;
			// animTop = new TranslateAnimation(0, 0, 0,
			// -mQuickReturnHeightTop);
			// animTop.setFillAfter(true);
			// animTop.setDuration(250);
			// animTop.setAnimationListener(new AnimationListener() {
			//
			// @Override
			// public void onAnimationStart(Animation animation) {
			// }
			//
			// @Override
			// public void onAnimationRepeat(Animation animation) {
			//
			// }
			//
			// @Override
			// public void onAnimationEnd(Animation animation) {
			// noAnimation = false;
			// mState = STATE_OFFSCREEN;
			// }
			// });
			// mQuickReturnViewTop.startAnimation(animTop);
			// } else if (translationY > 0) {
			// translationY = 0;
			// mMinRawYtop = rawY - mQuickReturnHeightTop;
			// }
			//
			// else if (rawY > 0) {
			// mState = STATE_ONSCREEN;
			// translationY = rawY;
			// }
			//
			// else if (translationY < -mQuickReturnHeightTop) {
			// mState = STATE_OFFSCREEN;
			// mMinRawYtop = rawY;
			// } else {
			// mMinRawYtop = rawY;
			// }
			// }
			// return translationY;
			// }

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
	public void onClosedComplete(Boolean result) {
		if (result) {
			((DetailMesaActivity) this.getActivity()).onBackPressed();
			Toast toast = Toast.makeText(getActivity(),
					"Conta fechada com sucesso!", Toast.LENGTH_SHORT);
			toast.show();
		}

	}
}