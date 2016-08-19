package com.chaoyang805.chapterseven_animation;

import android.animation.TypeEvaluator;
import android.graphics.Interpolator;

/**
 * Created by chaoyang805 on 16/8/19.
 */

public class SomeInterpolator implements android.view.animation.Interpolator {

    @Override
    public float getInterpolation(float v) {
        return 0;
    }
}
class InEnvaluator implements TypeEvaluator<Integer> {

    @Override
    public Integer evaluate(float v, Integer integer, Integer t1) {
        return null;
    }
}