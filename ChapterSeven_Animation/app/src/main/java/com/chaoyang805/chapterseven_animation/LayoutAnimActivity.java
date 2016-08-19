package com.chaoyang805.chapterseven_animation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyang805 on 16/8/16.
 */

public class LayoutAnimActivity extends AppCompatActivity {
    private ListView mListView;
    private List<String> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_anim);
        mListView = (ListView) findViewById(R.id.animtable_list);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_animation);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.5f);
        controller.setInterpolator(new DecelerateInterpolator());
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

        mListView.setLayoutAnimation(controller);
        generateDatas();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, mDatas);

        mListView.setAdapter(adapter);
    }

    private void generateDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mDatas.add("item " + i);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }
}
