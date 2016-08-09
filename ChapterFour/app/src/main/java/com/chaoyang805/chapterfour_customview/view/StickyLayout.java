package com.chaoyang805.chapterfour_customview.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import java.security.PrivilegedAction;
import java.util.EventListener;
import java.util.NoSuchElementException;

/**
 * Created by chaoyang805 on 16/8/9.
 */

public class StickyLayout extends LinearLayout {

    private static final String TAG = "StickyLayout";
    private static final boolean DEBUG = true;

    private View mHeader;
    private View mContent;

    private OnGiveUpTouchEventListener mGiveUpTouchEventListener;

    // header的高度 单位: px
    private int mOriginalHeaderHeight;
    private int mHeaderHeight;

    private int mStatus = STATUS_EXPANDED;
    private static final int STATUS_EXPANDED = 1;
    private static final int STATUS_COLLAPSED = 2;

    private int mTouchSlop;

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private boolean mInitDataSucceed = false;
    private boolean mDisallowInterceptTouchEventOnHeader = true;
    private boolean mIsSticky = true;

    public StickyLayout(Context context) {
        this(context, null);
    }

    public StickyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StickyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
        mGiveUpTouchEventListener = l;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && (mHeader == null || mContent == null)) {
            initData();
        }
    }

    private void initData() {
        int headerId = getResources().getIdentifier("sticky_header","id", getContext().getPackageName());
        int contentId = getResources().getIdentifier("sticky_content", "id", getContext().getPackageName());

        if (headerId != 0 && contentId != 0) {
            mHeader = findViewById(headerId);
            mContent = findViewById(contentId);

            mOriginalHeaderHeight = mHeader.getMeasuredHeight();
            mHeaderHeight = mOriginalHeaderHeight;

            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            if (mHeaderHeight > 0) {
                mInitDataSucceed = true;
            }
            if (DEBUG) {
                Log.d(TAG, "mTouchSlop =" + mTouchSlop + " mHeaderHeight = " + mHeaderHeight);
            }
        } else {
            throw new NoSuchElementException("you need to set your header id to sticky_header and content id to sticky_content");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int intercepted = 0;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                mLastXIntercept = (int) ev.getX();
                mLastYIntercept= (int) ev.getY();
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                // 父布局不应该拦截ACTION_DOWN 事件,否则后续的事件都会由父布局处理
                intercepted = 0;
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;

                // 事件落在header 上时不拦截
                if (mDisallowInterceptTouchEventOnHeader && y < getHeaderHeight()) {
                    intercepted = 0;
                // 竖直距离小于水平距离不拦截
                } else if (Math.abs(deltaY) <= Math.abs(deltaX)) {
                    intercepted = 0;
                // header是展开状态且向上滑动时拦截事件
                } else if (mStatus == STATUS_EXPANDED && deltaY < -mTouchSlop) {
                    intercepted = 1;
                // Listview滑动到了顶部且向上滑动时也拦截事件
                } else if (mGiveUpTouchEventListener.giveUpTouchEvent(ev) && deltaY >= mTouchSlop) {
                    intercepted = 1;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {

                intercepted = 0;
                mLastXIntercept = mLastYIntercept = 0;
                break;
            }
            default:
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "intercepted=" + intercepted);
        }

        return intercepted != 0 && mIsSticky;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (DEBUG) {
                    Log.d(TAG, "mHeaderHeight=" + mHeaderHeight + " deltaY=" + deltaY + " mlastY=" + mLastY);
                }
                mHeaderHeight += deltaY;
                Log.d(TAG, "ACTION_MOVE setHeaderHeight");
                setHeaderHeight(mHeaderHeight);
                break;
            }
            case MotionEvent.ACTION_UP: {
                int destHeight = 0;
                if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) {
                    destHeight = 0;
                    mStatus = STATUS_COLLAPSED;
                } else {
                    destHeight = mOriginalHeaderHeight;
                    mStatus = STATUS_EXPANDED;
                }
                smoothSetHeaderHeight(mHeaderHeight,destHeight, 500);
                break;
            }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        if (!mInitDataSucceed) {
            initData();
        }

        if (DEBUG) {
            Log.d(TAG, "setHeaderHeight height=" + height);
        }

        if (height < 0) {
            height = 0;
        } else if (height > mOriginalHeaderHeight) {
            height = mOriginalHeaderHeight;
        }

        if (height == 0) {
            mStatus = STATUS_COLLAPSED;
        } else {
            mStatus = STATUS_EXPANDED;
        }

        if (mHeader != null && mHeader.getLayoutParams() != null) {
            mHeader.getLayoutParams().height = height;
            mHeader.requestLayout();
            mHeaderHeight = height;
        } else {
            if (DEBUG) {
                Log.e(TAG, "null layoutparams when setHeaderHeight");
            }
        }
    }

    private void smoothSetHeaderHeight(int from, int to, long duration) {
        smoothSetHeaderHeight(from, to, duration, false);
    }

    private void smoothSetHeaderHeight(final int from, final int to, long duration, final boolean modifyOriginalHeaderHeight) {
        final int frameCount = (int)((duration / 1000f) * 30) + 1;
        Log.d(TAG, "frameCOunt=" + frameCount);
        final float partation = (to - from) / (float) frameCount;
        new Thread("Thread#smoothSetHeaderHeight"){
            @Override
            public void run() {
                for (int i = 0; i < frameCount; i++) {
                    final int height;
                    if (i == frameCount - 1) {
                        height = to;
                    }else {
                        height = (int) (from + partation * i);
                    }

                    post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Run setHeaderHeight");
                            setHeaderHeight(height);
                        }
                    });
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (modifyOriginalHeaderHeight) {
                    setOriginalHeaderHeight(to);
                }
            }
        }.start();
    }

    public void setOriginalHeaderHeight(int originalHeaderHeight) {
        mOriginalHeaderHeight = originalHeaderHeight;
    }

    public interface OnGiveUpTouchEventListener {
        boolean giveUpTouchEvent(MotionEvent event);
    }
}
