package com.chaoyang805.chaptersix_drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mClipView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ImageView)findViewById(R.id.imageView)).setImageLevel(1);
//        ScaleDrawable scaleDrawable = (ScaleDrawable) findViewById(R.id.scale_view).getBackground();
//        scaleDrawable.setLevel(1);
        mClipView = (ImageView) findViewById(R.id.clip_image_view);
    }

    public void transitionView(View view) {
        TransitionDrawable background = (TransitionDrawable) findViewById(R.id.transition_view).getBackground();
        background.startTransition(1000);
    }

    public void addLevel(View view) {
        ClipDrawable clipDrawable = (ClipDrawable) mClipView.getBackground();
        clipDrawable.setLevel(clipDrawable.getLevel()  + 100);
    }

    public void reduceLevel(View view) {

        ClipDrawable clipDrawable = (ClipDrawable) mClipView.getBackground();

        if (clipDrawable.getLevel() >= 100) {
            clipDrawable.setLevel(clipDrawable.getLevel() - 100);
        }
    }
}
