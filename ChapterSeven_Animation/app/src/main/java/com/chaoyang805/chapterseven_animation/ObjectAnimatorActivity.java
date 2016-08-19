package com.chaoyang805.chapterseven_animation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by chaoyang805 on 16/8/19.
 */

public class ObjectAnimatorActivity extends AppCompatActivity {

    private View mAnimateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        mAnimateView = findViewById(R.id.animator_view);
    }

    public void aniamteView(View view) {
        @ColorInt int startColor = 0xffff8080;
        @ColorInt int endColor = 0xff8080ff;
        ObjectAnimator animator = ObjectAnimator.ofInt(mAnimateView, "backgroundColor",startColor, endColor);
        animator.setDuration(3000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatCount(ValueAnimator.REVERSE);
        animator.start();
    }

    public void animateGroup(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(

            ObjectAnimator.ofFloat(mAnimateView, "rotationX", 0, 360),
            ObjectAnimator.ofFloat(mAnimateView, "rotationY", 0, 180),
            ObjectAnimator.ofFloat(mAnimateView, "rotation", 0, 90),
            ObjectAnimator.ofFloat(mAnimateView, "translationX", 0, 90),
            ObjectAnimator.ofFloat(mAnimateView, "translationY", 0, 90),
            ObjectAnimator.ofFloat(mAnimateView, "scaleX", 1, 1.5f),
            ObjectAnimator.ofFloat(mAnimateView, "scaleY", 1, 0.5f),
            ObjectAnimator.ofFloat(mAnimateView, "alpha", 1, 0.25f, 1)
        );
        set.setDuration(5000).start();

    }

    public void animateXML(View view) {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.property_animator);
        set.setTarget(mAnimateView);
        set.start();
    }

    public void animateWidth(View view) {

//        ViewWrapper wrapper = new ViewWrapper(view);
//        ObjectAnimator animator = ObjectAnimator.ofInt(wrapper, "width", 100,200);
//        animator.setDuration(1000);
//        animator.start();
        performAnimate(view, view.getWidth(), 500);
    }

    private IntEvaluator mIntEvaluator = new IntEvaluator();

    public void performAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);


        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int currentValue = (int) valueAnimator.getAnimatedValue();

                float fraction = valueAnimator.getAnimatedFraction();

                target.getLayoutParams().width = mIntEvaluator.evaluate(fraction, start, end);

                target.requestLayout();

            }
        });

        valueAnimator.setDuration(1000).start();
    }
}
