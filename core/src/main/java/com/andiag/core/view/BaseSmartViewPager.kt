package com.andiag.core.view

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class BaseSmartViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private val mGestureDetector: GestureDetector
    private var mIsLockOnHorizontalAxis = false

    init {
        mGestureDetector = GestureDetector(context, XScrollDetector())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Decide if horizontal axis is locked already or we need to check the scrolling direction
        if (!mIsLockOnHorizontalAxis)
            mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(event)

        // Release the lock when finger is up
        if (event.action == MotionEvent.ACTION_UP)
            mIsLockOnHorizontalAxis = false

        parent.requestDisallowInterceptTouchEvent(mIsLockOnHorizontalAxis)
        return super.onTouchEvent(event)
    }

    private inner class XScrollDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            return Math.abs(distanceX) > Math.abs(distanceY)
        }

    }
}
