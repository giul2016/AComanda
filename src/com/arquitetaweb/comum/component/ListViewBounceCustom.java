package com.arquitetaweb.comum.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;

public class ListViewBounceCustom extends ListView {

	private int mItemCount;
	private int mItemOffsetY[];
	private boolean scrollIsComputed = false;
	private int mHeight;

	private static final int MAX_Y_OVERSCROLL_DISTANCE = 0;

	private Context mContext;
	private int mMaxYOverscrollDistance;

	public ListViewBounceCustom(Context context) {
		super(context);
		mContext = context;
		initBounceListView();
	}

	public ListViewBounceCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}

	public ListViewBounceCustom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initBounceListView();
	}

	private void initBounceListView() {
		// get the density of the screen and do some maths with it on the max
		// overscroll distance
		// variable so that you get similar behaviors no matter what the screen
		// size

		final DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		final float density = metrics.density;

		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// This is where the magic happens, we have replaced the incoming
		// maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
		
//		Log.d("TESTE", "deltaX: "+ deltaX);
//		Log.d("TESTE", "deltaY: "+ deltaY);
//		Log.d("TESTE", "scrollX: "+ scrollX);
//		Log.d("TESTE", "scrollY: "+ scrollY);
//		Log.d("TESTE", "scrollRangeX: "+ scrollRangeX);
//		Log.d("TESTE", "scrollRangeY: "+ scrollRangeY);
//		
//		Log.d("TESTE", "maxOverScrollX: "+ maxOverScrollX);
//		Log.d("TESTE", "maxOverScrollY: "+ maxOverScrollY);
		
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX,
				mMaxYOverscrollDistance, isTouchEvent);
	}

	public int getListHeight() {
		return mHeight;
	}

	public void computeScrollY() {
		mHeight = 0;
		mItemCount = getAdapter().getCount();
		if (mItemOffsetY == null) {
			mItemOffsetY = new int[mItemCount];
		}
		for (int i = 0; i < mItemCount; ++i) {
			View view = getAdapter().getView(i, null, this);
			view.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			mItemOffsetY[i] = mHeight;
			mHeight += view.getMeasuredHeight();
		}
		scrollIsComputed = true;
	}

	public boolean scrollYIsComputed() {
		return scrollIsComputed;
	}

	public int getComputedScrollY() {
		int pos, nScrollY, nItemY;
		View view = null;
		pos = getFirstVisiblePosition();
		view = getChildAt(0);
		nItemY = (view == null) ? 0 : view.getTop();
		nScrollY = mItemOffsetY.length == 0 ? 0 : mItemOffsetY[pos] - nItemY;
		return nScrollY;
	}
}