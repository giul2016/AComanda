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

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arquitetaweb.comanda.R;
import com.arquitetaweb.comum.component.ListViewCustom;

public class ConsumoFragment extends ListFragment {

	private ListViewCustom mListView;
	private LinearLayout mQuickReturnView;
	private int mQuickReturnHeight;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;

	private int mScrollY;
	private int mMinRawY = 0;

	private TranslateAnimation anim;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_consumo, null);

		mQuickReturnView = (LinearLayout) view.findViewById(R.id.footer1);

		Button btnFechaConta = (Button) view.findViewById(R.id.fechar_conta);
		btnFechaConta.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast toast = Toast.makeText(getActivity(), "fechar conta",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		});		
	    
		return view;
	}

	private void showFooter(Integer seconds) {
		Handler handler = null;
	    handler = new Handler(); 
	    handler.postDelayed(new Runnable(){ 
	         public void run(){
	        	 mQuickReturnView.setTranslationY(0);
	         }
	    }, seconds * 1000);
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mListView = (ListViewCustom) getListView();

		String[] array = new String[] { "Android", "Android", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android" };

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.produtogrupo_info, R.id.txtGrupoProdutoDescricao,
				array));

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

					System.out.println(translationY);
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
}