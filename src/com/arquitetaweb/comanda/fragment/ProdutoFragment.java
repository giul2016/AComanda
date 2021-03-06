package com.arquitetaweb.comanda.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comanda.activity.DetalhesMesaActivity;
import com.arquitetaweb.comanda.activity.PedidoActivity;
import com.arquitetaweb.comanda.adapter.ProdutoGrupoAdapter;
import com.arquitetaweb.comanda.controller.ProdutoController;
import com.arquitetaweb.comanda.model.MesaModel;
import com.arquitetaweb.comum.component.ListViewCustom;

public class ProdutoFragment extends ListFragment {

	private ProdutoController controller;

	private ListViewCustom mListView;
	private LinearLayout mQuickReturnViewTop;
	private int mQuickReturnHeightTop;

	private View mPlaceHolder;
	private View mHeader;

	private int mCachedVerticalScrollRange;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private static final int STATE_EXPANDED = 3;

	private int mState = STATE_ONSCREEN;
	private int mScrollYtop;
	private int mMinRawYtop = 0;
	private int rawY;
	private boolean noAnimation = false;

	private TranslateAnimation animTop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_produto, null);
		mHeader = inflater.inflate(R.layout.produto_header, null);

		mQuickReturnViewTop = (LinearLayout) view
				.findViewById(R.id.comanda_layout);

		mPlaceHolder = mHeader.findViewById(R.id.placeholder);

		Button btnProdutoRecente = (Button) view.findViewById(R.id.produtos_recentes);
		btnProdutoRecente.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				abrirPedido(view, 0, true);
			}			
		});

		View mInfoHeader = mHeader.findViewById(R.id.infoheader);
		MesaModel mesa = ((DetalhesMesaActivity) this.getActivity())
				.getMesaModel();
		TextView id = (TextView) mInfoHeader.findViewById(R.id.txtidmesaheader);
		id.setText("Id: " + mesa.id);
		TextView numero = (TextView) mInfoHeader
				.findViewById(R.id.txtmesaheader);
		numero.setText("Número: " + mesa.numero_mesa.toString());
		TextView situacao = (TextView) mInfoHeader
				.findViewById(R.id.txtdetailmesaheader);
		situacao.setText("Situação: " + mesa.situacao.toString());

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		controller = new ProdutoController(this.getActivity());
		ProdutoGrupoAdapter adapter = new ProdutoGrupoAdapter(
				this.getActivity(), controller.sincronizar());

		mListView = (ListViewCustom) getListView();
		mListView.addHeaderView(mHeader);
		// setListAdapter(adapter);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				abrirPedido(view, id, false);
			}
		});			

		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mQuickReturnHeightTop = mQuickReturnViewTop.getHeight();
						mListView.computeScrollY();
						mCachedVerticalScrollRange = mListView.getListHeight();
					}
				});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				int translationY = 0;
				translationY = noAnimation(); // used
				// translationY = animation();

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					animTop = new TranslateAnimation(0, 0, translationY,
							translationY);
					animTop.setFillAfter(true);
					animTop.setDuration(0);
					mQuickReturnViewTop.startAnimation(animTop);
				} else {
					mQuickReturnViewTop.setTranslationY(translationY);
				}
			}

			@SuppressWarnings("unused")
			private int animation() {
				mScrollYtop = 0;
				int translationY = 0;

				if (mListView.scrollYIsComputed()) {
					mScrollYtop = mListView.getComputedScrollY();
				}

				rawY = mPlaceHolder.getTop()
						- Math.min(
								mCachedVerticalScrollRange
										- mListView.getHeight(), mScrollYtop);

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY <= mMinRawYtop) {
						mMinRawYtop = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY < -mQuickReturnHeightTop) {
						mState = STATE_OFFSCREEN;
						mMinRawYtop = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:

					if (translationY > 0) {
						translationY = 0;
						mMinRawYtop = rawY - mQuickReturnHeightTop;
					}

					else if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					else if (translationY < -mQuickReturnHeightTop) {
						mState = STATE_OFFSCREEN;
						mMinRawYtop = rawY;

					} else if (mQuickReturnViewTop.getTranslationY() != 0
							&& !noAnimation) {
						noAnimation = true;
						animTop = new TranslateAnimation(0, 0,
								-mQuickReturnHeightTop, 0);
						animTop.setFillAfter(true);
						animTop.setDuration(250);
						mQuickReturnViewTop.startAnimation(animTop);
						animTop.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								noAnimation = false;
								mMinRawYtop = rawY;
								mState = STATE_EXPANDED;
							}
						});
					}
					break;

				case STATE_EXPANDED:
					if (rawY < mMinRawYtop - 2 && !noAnimation) {
						noAnimation = true;
						animTop = new TranslateAnimation(0, 0, 0,
								-mQuickReturnHeightTop);
						animTop.setFillAfter(true);
						animTop.setDuration(250);
						animTop.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								noAnimation = false;
								mState = STATE_OFFSCREEN;
							}
						});
						mQuickReturnViewTop.startAnimation(animTop);
					} else if (translationY > 0) {
						translationY = 0;
						mMinRawYtop = rawY - mQuickReturnHeightTop;
					}

					else if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					else if (translationY < -mQuickReturnHeightTop) {
						mState = STATE_OFFSCREEN;
						mMinRawYtop = rawY;
					} else {
						mMinRawYtop = rawY;
					}
				}
				return translationY;
			}

			private int noAnimation() {
				mScrollYtop = 0;
				int translationYtop = 0;

				if (mListView.scrollYIsComputed()) {
					mScrollYtop = mListView.getComputedScrollY();
				}

				int rawYtop = mPlaceHolder.getTop()
						- Math.min(
								mCachedVerticalScrollRange
										- mListView.getHeight(), mScrollYtop);

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawYtop <= mMinRawYtop) {
						mMinRawYtop = rawYtop;
					} else {
						mState = STATE_RETURNING;
					}
					translationYtop = rawYtop;
					break;

				case STATE_ONSCREEN:
					if (rawYtop < -mQuickReturnHeightTop) {
						mState = STATE_OFFSCREEN;
						mMinRawYtop = rawYtop;
					}
					translationYtop = rawYtop;
					break;

				case STATE_RETURNING:
					translationYtop = (rawYtop - mMinRawYtop)
							- mQuickReturnHeightTop;
					if (translationYtop > 0) {
						translationYtop = 0;
						mMinRawYtop = rawYtop - mQuickReturnHeightTop;
					}

					if (rawYtop > 0) {
						mState = STATE_ONSCREEN;
						translationYtop = rawYtop;
					}

					if (translationYtop < -mQuickReturnHeightTop) {
						mState = STATE_OFFSCREEN;
						mMinRawYtop = rawYtop;
					}
					break;
				}
				return translationYtop;
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}
		
	
	private void abrirPedido(View view, long idProdutoGrupo, boolean produtoRecente) {
		Intent intent = new Intent(view.getContext(),
				PedidoActivity.class);
		MesaModel mesaObj = ((DetalhesMesaActivity) getView()
				.getContext()).getMesaModel();
		intent.putExtra("mesa", mesaObj);
		intent.putExtra("IdProdutoGrupo", idProdutoGrupo);
		intent.putExtra("produtoRecente", produtoRecente);
		startActivityForResult(intent, 100);
	}
}