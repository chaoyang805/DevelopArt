package com.chaoyang805.chapterfive_remoteviews;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.NoiseSuppressor;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendNotification(View view) {
        Notification notification = new Notification();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("This is a notification");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("This is a title");
        builder.setContentText("This is a content text");
        builder.setContentIntent(pendingIntent);

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.layout_notification);
        notificationView.setTextViewText(R.id.text, "This is a custom title");
        notificationView.setTextColor(R.id.text, Color.BLACK);
        builder.setContent(notificationView);

        notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }
}
