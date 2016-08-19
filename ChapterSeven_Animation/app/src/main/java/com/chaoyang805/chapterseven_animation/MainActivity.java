package com.chaoyang805.chapterseven_animation;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.chaoyang805.chapterseven_animation.view.animation.Rotate3dAnimation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = findViewById(R.id.frame_animation_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) v.getBackground();
        animationDrawable.start();
    }


    public void animateView(View view) {
        AnimationSet set = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.animation_test);
        findViewById(R.id.animatable_view).startAnimation(set);
    }

    public void animateViewByCode(View view) {
        Animation translateAnimation = new TranslateAnimation(0, 100, 0, 100);
        translateAnimation.setDuration(1000);
        translateAnimation.setInterpolator(new DecelerateInterpolator());

        Animation rotateAnimation = new RotateAnimation(0, 90,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(400);

        AnimationSet set = new AnimationSet(false);

        set.addAnimation(translateAnimation);
        set.addAnimation(rotateAnimation);
        findViewById(R.id.animatable_view).startAnimation(set);
    }

    public void rotate3D(View view) {
        Rotate3dAnimation animation = new Rotate3dAnimation(0, 190, 100, 100, 100, true);
        animation.setDuration(1000);
        findViewById(R.id.animatable_view).startAnimation(animation);
    }

    public void showLayoutAnim(View view) {
        Intent intent = new Intent(this, LayoutAnimActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }

    public void enterObjectAniamtorActivity(View view) {
        Intent intent = new Intent(this, ObjectAnimatorActivity.class);
        startActivity(intent);
    }
}
