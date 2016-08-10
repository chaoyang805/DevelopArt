package com.chaoyang805.chapterfive_remoteviews;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by chaoyang805 on 16/8/10.
 */

public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "WidgetProvider";
    private static final String CLICK_ACTION = "com.chaoyang805.action.CLICK";
    private static final String BUTTON_CLICK_ACTION = "com.chaoyang805.action.BUTTON_CLICK";

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.d(TAG, "onReceive + action=" + intent.getAction());
        if (intent.getAction().equals(CLICK_ACTION)) {

            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 10) % 360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
                        remoteViews.setImageViewBitmap(R.id.image, rotateBitmap(srcBitmap, degree));
                        Intent intent = new Intent();
                        intent.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                        remoteViews.setOnClickPendingIntent(R.id.image, pendingIntent);
                        manager.updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);

                        SystemClock.sleep(30);
                    }
                }
            }).start();
        } else if (intent.getAction().equals(BUTTON_CLICK_ACTION)) {
            Toast.makeText(context, "button clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "onUpdate");

        final int counter = appWidgetIds.length;
        Log.d(TAG, "counter=" + counter);
        for (int i = 0; i < counter; i++) {
            onWidgetUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        Intent intent = new Intent();
        intent.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.image, pendingIntent);

        Intent buttonIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, buttonIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.button, pendingIntent1);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    private Bitmap rotateBitmap(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
