package com.andiag.core.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class BaseSmartViewPager extends ViewPager {

    public BaseSmartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }

    private GestureDetector mGestureDetector;
    private boolean mIsLockOnHorizontalAxis = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Decide if horizontal axis is locked already or we need to check the scrolling direction
        if (!mIsLockOnHorizontalAxis)
            mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(event);

        // Release the lock when finger is up
        if (event.getAction() == MotionEvent.ACTION_UP)
            mIsLockOnHorizontalAxis = false;

        getParent().requestDisallowInterceptTouchEvent(mIsLockOnHorizontalAxis);
        return super.onTouchEvent(event);
    }

    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceX) > Math.abs(distanceY);
        }

    }
}
