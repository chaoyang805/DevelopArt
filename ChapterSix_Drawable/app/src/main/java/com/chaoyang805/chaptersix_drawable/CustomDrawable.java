package com.chaoyang805.chaptersix_drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * Created by chaoyang805 on 16/8/12.
 */

public class CustomDrawable extends Drawable {

    private Paint mPaint;

    public CustomDrawable(@ColorInt int color) {
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect r = getBounds();
        float centerX = r.exactCenterX();
        float centerY = r.exactCenterY();
        canvas.drawCircle(centerX, centerY, Math.min(centerX, centerY), mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
