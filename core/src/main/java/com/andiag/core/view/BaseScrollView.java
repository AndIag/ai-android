package com.andiag.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ScrollView;


public class BaseScrollView extends ScrollView {
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 50;
    private static final int ANIMATION_DURATION = 300;
    private static final float SLOW_COEFFICIENT = 0.8F;
    private static final boolean HAS_SLOW_EFFECT = true;
    static final int DELTA_ON_CLICK = 5;

    private int mOverscrollDistance;
    private float mSlowCoefficient;
    private int mMaxYOverscrollDistance;
    private boolean mSlowEffect;
    private int mAnimationTime;

    private void setAttr(Context ctx, AttributeSet attrs) {
        mOverscrollDistance = MAX_Y_OVERSCROLL_DISTANCE;
        mSlowEffect = HAS_SLOW_EFFECT;
        mAnimationTime = ANIMATION_DURATION;
        mSlowCoefficient = SLOW_COEFFICIENT;
    }

    public BaseScrollView(Context context) {
        super(context);

        mOverscrollDistance = MAX_Y_OVERSCROLL_DISTANCE;
        mSlowEffect = false;
        mAnimationTime = ANIMATION_DURATION;
        mSlowCoefficient = SLOW_COEFFICIENT;
        initOverscrollListView();
    }

    public BaseScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttr(context, attrs);
        initOverscrollListView();
    }

    public BaseScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttr(context, attrs);
        initOverscrollListView();
    }

    private void initOverscrollListView() {
        final DisplayMetrics metrics = getContext().getResources()
                .getDisplayMetrics();
        final float density = metrics.density;
        mMaxYOverscrollDistance = (int) (density * mOverscrollDistance);
    }

    public void setSlowEffect(boolean slowEffect) {
        mSlowEffect = slowEffect;
    }

    public void setSlowCoefficient(int coef) {
        mSlowCoefficient = coef;
    }

    public void setCollapseAnimationDuration(int duration) {
        mAnimationTime = duration;
    }

    private double getOverScrollYWithSlow(float y) {
        return Math.pow(Math.abs(y), mSlowCoefficient) * (y > 0 ? 1 : -1);
    }

    private double getReverseOverScrollYWithSlow(float y) {
        return Math.pow(Math.abs(y), 1 / mSlowCoefficient) * (y > 0 ? 1 : -1);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {


        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                mMaxYOverscrollDistance, isTouchEvent);
    }

    private float mFirstY;
    private int mLastScroll = 0;
    long mHistoricTime;
    private int mOffset = 0;
    int mOldLastScroll = 0;
    int mLastY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float newY = ev.getRawY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mHistoricTime = System.currentTimeMillis();
            mFirstY = ev.getRawY();
            clearColapseAnimation();
            mLastScroll = 0;
            mOffset = 0;
            mOldLastScroll = 0;
            mLastY = 0;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            mLastScroll = (int) (mFirstY - newY);
            // fix for case when simple scroll transforming to overscroll (when
            // touching down from middle/bottom)
            if (mLastScroll < 0)
                if (getScrollY() <= 0) {
                    // we should get offset for future, if we started scrolling
                    // from middle/bottom
                    if (mOffset == 0) {
                        mOffset = mOldLastScroll;
                        mLastScroll -= mOffset;
                    } else
                        mLastScroll -= mOffset;

                }

            // fix for case when simple scroll transforming to overscroll (when
            // touching down from middle/top)
            if (mLastScroll > 0) {
                // ScrollView has always 1 child, so...
                if (getScrollY() + getHeight() >= getChildAt(0).getHeight()) {
                    if (mOffset == 0) {
                        mOffset = mOldLastScroll;
                        mLastScroll -= mOffset;
                    } else
                        mLastScroll -= mOffset;
                }

            }

            mOldLastScroll = mLastScroll;
            if (mSlowEffect) {
                if (getOverScrollYWithSlow(mLastScroll) < -mMaxYOverscrollDistance)
                    mLastScroll = -(int) getReverseOverScrollYWithSlow(mMaxYOverscrollDistance);
                if (getOverScrollYWithSlow(mLastScroll) > mMaxYOverscrollDistance)
                    mLastScroll = (int) getReverseOverScrollYWithSlow(mMaxYOverscrollDistance);

            } else {
                if (mLastScroll < -mMaxYOverscrollDistance)
                    mLastScroll = -mMaxYOverscrollDistance;
                if (mLastScroll > mMaxYOverscrollDistance)
                    mLastScroll = mMaxYOverscrollDistance;
            }

            if (mLastScroll < 0) {

                if (getScrollY() <= 0) {
                    pullDown(mLastScroll, 0);
                    return true;

                }
            } else {


                // ScrollView has always 1 child, so...
                if (getScrollY() + getHeight() >= getChildAt(0).getHeight()) {
                    if (mLastY == 0)
                        mLastY = getScrollY();
                    pullDown(mLastScroll, mLastY);
                    return true;
                }

            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {

            //onCLick
            if (Math.abs(mLastScroll) < DELTA_ON_CLICK)
                return super.dispatchTouchEvent(ev);

            if (getScrollY() <= 0) {
                pullUp(mLastScroll, 0);

                return false;
            }
            // ScrollView has always 1 child, so...
            if (getScrollY() + getHeight() >= getChildAt(0).getHeight()) {
                pullUp(mLastScroll, mLastY);
                return false;
            }

        }

        return super.dispatchTouchEvent(ev);
    }

    private void clearColapseAnimation() {
        Animation a = getAnimation();

        if (a != null) {
            this.clearAnimation();
            if (a instanceof ColapseAnimation) {
                mLastScroll = getScrollY();
                mFirstY += mLastScroll;
                pullDown(this.mLastScroll, 0);
            }

        }
    }


    private void pullDown(int deltaY, int lastY) {
        overScrollBy(0, mSlowEffect ? (int) getOverScrollYWithSlow(deltaY)
                : deltaY, 0, lastY, 0, getChildAt(0).getHeight(), 0, mMaxYOverscrollDistance, true);

    }

    private void pullUp(int lastScroll, int endY) {
        Animation a = new ColapseAnimation(
                ((mSlowEffect) ? (int) getOverScrollYWithSlow(lastScroll) : lastScroll) + endY, endY, getChildAt(0).getHeight());
        a.setDuration(mAnimationTime);
        startAnimation(a);
    }

    private class ColapseAnimation extends Animation {
        private final int mStartY;
        private final int mDeltaY;
        private final int mScrollRangeY;

        ColapseAnimation(int startY, int endY, int scrollRangeY) {
            mStartY = startY;
            mDeltaY = endY - startY;
            this.mScrollRangeY = scrollRangeY;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newDelta = (int) (mDeltaY * interpolatedTime);
            overScrollBy(0, newDelta, 0, mStartY, 5, mScrollRangeY, 0,
                    mMaxYOverscrollDistance, true);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}