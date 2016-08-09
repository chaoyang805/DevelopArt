package com.chaoyang805.chapterfour_customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

/**
 * Created by chaoyang805 on 16/8/9.
 */

public class PinnedHeaderExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "PinnedHeaderExpandable";

    private View mHeaderView;
    private int mHeaderHeight;
    private int mHeaderWidth;

    private View mTouchTarget;

    private boolean mActionDownHappened = false;

    private OnScrollListener mScrollListener;
    private boolean mIsHeaderGroupClickable = true;

    public interface OnHeaderUpdateListener {

        View getPinnedHeader();

        void updatePinnedHeader(View headerView, int firstVisibleGroupPosition);
    }
    private OnHeaderUpdateListener mHeaderUpdateListener;

    public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
        mHeaderUpdateListener = listener;
        if (listener == null) {
            mHeaderView = null;
            mHeaderHeight = mHeaderWidth = 0;
            return;
        }
        mHeaderView = listener.getPinnedHeader();
        int firstVisiblePosition = getFirstVisiblePosition();
        int firstVisibleGroupPosition = getPackedPositionGroup(getExpandableListPosition(firstVisiblePosition));
        listener.updatePinnedHeader(mHeaderView, firstVisibleGroupPosition);
        requestLayout();
        postInvalidate();

    }

    public PinnedHeaderExpandableListView(Context context) {
        this(context, null);
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setFadingEdgeLength(0);
        setOnScrollListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView == null) {
            return;
        }
        measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderView != null) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mHeaderView == null) {
            return;
        }
        int delta = mHeaderView.getTop();
        mHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int position = pointToPosition(x, y);

        if (mHeaderView != null && y >= mHeaderView.getTop() && y <= mHeaderView.getBottom()) {

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                mTouchTarget = getTouchTarget(mHeaderView, x, y);
                mActionDownHappened = true;
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                View touchTarget = getTouchTarget(mHeaderView, x, y);
                if (mTouchTarget == touchTarget && mTouchTarget.isClickable()) {
                    mTouchTarget.performClick();
                    invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
                } else if (mIsHeaderGroupClickable) {
                    int groupPosition = getPackedPositionGroup(getExpandableListPosition(position));
                    if (groupPosition != INVALID_POSITION && mActionDownHappened) {
                        if (isGroupExpanded(groupPosition)) {
                            collapseGroup(groupPosition);
                        } else {
                            expandGroup(groupPosition);
                        }
                    }
                }
                mActionDownHappened = false;
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private View getTouchTarget(View view, int x, int y) {
        if (!(view instanceof ViewGroup)) {
            return view;
        }

        ViewGroup parent = (ViewGroup)view;
        int childCount = parent.getChildCount();
        final boolean customOrder = isChildrenDrawingOrderEnabled();
        View target = null;
        for (int i = childCount - 1; i >= 0; i--) {
            final int childIndex = customOrder ? getChildDrawingOrder(childCount, i) : i;
            final View child = parent.getChildAt(childIndex);
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        }
        if (target == null) {
            target = parent;
        }
        return target;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        return view.isClickable() && y >= view.getTop() && y <= view.getBottom() && x >= view.getLeft() && x <= view.getRight();
    }

    public void requestRefreshHeader() {
        refreshHeader();
        invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
    }

    private void refreshHeader() {
        if (mHeaderView == null) {
            return;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int position = firstVisiblePosition + 1;
        int firstVisibleGroupPosition = getPackedPositionGroup(getExpandableListPosition(firstVisiblePosition));
        int group = getPackedPositionGroup(getExpandableListPosition(position));
        if (DEBUG) {
            Log.d(TAG, "refreshHeader firstVisibleGroupPosition=" + firstVisibleGroupPosition);
        }
        if (group == firstVisibleGroupPosition + 1) {
            View view = getChildAt(1);
            if (view == null) {
                Log.w(TAG, "Warning : refreshHeader getChildAt(1)=null");
                return;
            }
            if (view.getTop() <= mHeaderHeight) {
                int delta = mHeaderHeight - view.getTop();
                mHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight - delta);
            } else {
                mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
            }
        } else {
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }
        if (mHeaderUpdateListener != null) {
            mHeaderUpdateListener.updatePinnedHeader(mHeaderView, firstVisibleGroupPosition);
        }
    }

    // OnScrollListener
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if (l != this) {
            mScrollListener = l;
        } else {
            mScrollListener = null;
        }
        super.setOnScrollListener(l);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(absListView, i);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0) {
            refreshHeader();
        }
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
