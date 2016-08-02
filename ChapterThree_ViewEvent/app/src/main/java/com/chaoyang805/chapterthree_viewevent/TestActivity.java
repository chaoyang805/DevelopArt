package com.chaoyang805.chapterthree_viewevent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by chaoyang805 on 16/8/2.
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestActivity";
    private static final int MSG_SCROLL_TO = 1;
    private static final int DELAY_TIME = 33;
    private static final int FRAME_COUNT = 30;
    private Button mButton1;
    private int mCount = 0;
    @SuppressLint("HandlerLeaks")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCROLL_TO:
                    mCount++;
                    if (mCount < FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (100 / (float) FRAME_COUNT);
                        mButton1.scrollBy(scrollX, 0);
                        mHandler.sendEmptyMessageDelayed(MSG_SCROLL_TO, DELAY_TIME);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // x = translationX + left translation 初始为零
        // setTranslation 不会改变 view的left
//        if (view.getId() == R.id.button1) {
//            Log.d(TAG, "Before move:mButton1.left=" + mButton1.getLeft());
//            Log.d(TAG, "Before move:mButton1.x=" + mButton1.getX());
//            mButton1.setTranslationX(100);
//            Log.d(TAG, "After move:mButton1.left=" + mButton1.getLeft());
//            Log.d(TAG, "After move:mButton1.x=" + mButton1.getX());
//        }


        // 使用 scrollTo
//        int x = -10;
//        mButton1.scrollTo(x,0);

        // 改变布局参数
//        LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) mButton1.getLayoutParams();
//        params.width += 100;
//        params.leftMargin += 100;
//        mButton1.requestLayout(); // mButton1.setLayoutParams(params);

        // 弹性滑动
        // 使用动画平移
//        ObjectAnimator.ofFloat(mButton1, "translationX", 0, 100)
//            .setDuration(1000).start();

        // 使用 ValueAnimator + scrollTo
//        final int startX = 0;
//        final int deltaX = 100;
//        ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(2000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float fraction = valueAnimator.getAnimatedFraction();
//                mButton1.scrollTo((int) (startX + deltaX * fraction), 0);
//            }
//        });
//        animator.start();

        // 使用 Handler
//        mHandler.sendEmptyMessageDelayed(MSG_SCROLL_TO, DELAY_TIME);
    }
}
