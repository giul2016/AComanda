package com.arquitetaweb.comanda.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
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
import com.arquitetaweb.comanda.adapter.ProdutoGrupoAdapter;
import com.arquitetaweb.comanda.controller.ConsumoController;
import com.arquitetaweb.comum.component.ListViewCustom;

public class ProdutoFragment extends ListFragment {

	private ConsumoController controller; 
	
	private ListViewCustom mListView;
	private LinearLayout mQuickReturnViewTop;
	private int mQuickReturnHeightTop;

	private View mPlaceHolder;
	private View mHeader;

	private int mCachedVerticalScrollRange;
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollYtop;
	private int mMinRawYtop = 0;

	private TranslateAnimation animTop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//		View view = inflater.inflate(R.layout.fragment, null);
		View view = inflater.inflate(R.layout.fragment_produto, null);
		mHeader = inflater.inflate(R.layout.produto_header, null);

		mQuickReturnViewTop = (LinearLayout) view
				.findViewById(R.id.comanda_layout);

		mPlaceHolder = mHeader.findViewById(R.id.placeholder);

		Button btn1 = (Button) view.findViewById(R.id.produtos_recentes);
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast toast = Toast.makeText(getActivity(), "teste 1 btn",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


		controller = new ConsumoController(this.getActivity());
		controller.sincron();
		
		mListView = (ListViewCustom) getListView();
		mListView.addHeaderView(mHeader);
		
		ProdutoGrupoAdapter adapter = new ProdutoGrupoAdapter(
				this.getActivity(), controller.sincron());

		setListAdapter(adapter);
		
		mListView = (ListViewCustom) getListView();

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

				int translationYtop = topAnim();

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					animTop = new TranslateAnimation(0, 0, translationYtop,
							translationYtop);
					animTop.setFillAfter(true);
					animTop.setDuration(0);
					mQuickReturnViewTop.startAnimation(animTop);
				} else {
					mQuickReturnViewTop.setTranslationY(translationYtop);
				}

			}

			private int topAnim() {
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
}