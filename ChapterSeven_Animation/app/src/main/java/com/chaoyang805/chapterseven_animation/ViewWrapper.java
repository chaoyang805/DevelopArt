package com.chaoyang805.chapterseven_animation;

import android.view.View;

/**
 * Created by chaoyang805 on 16/8/19.
 */

public class ViewWrapper {

    private View mTarget;

    public ViewWrapper(View target) {
        this.mTarget = target;
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();
    }
}
