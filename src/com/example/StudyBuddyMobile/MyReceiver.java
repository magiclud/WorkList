package com.example.StudyBuddyMobile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {
    public  static final String TITLE_KEY = "TITLE_KEY";
    public static final String BODY_KEY = "body_key";
    public static final String SUBJECT_KEY = "subject_key";
    int notifyID = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(TITLE_KEY);
        String subject = intent.getStringExtra(SUBJECT_KEY);
        String body = intent.getStringExtra(BODY_KEY);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pending = PendingIntent.getActivity(
                context, 0,
                new Intent(context, MyActivity.class),  0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle(title).setContentText(body).setTicker(subject)
                .setContentIntent(pending)
                .setWhen(System.currentTimeMillis());
        mBuilder.setNumber(notifyID);

        NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NM.notify(notifyID, mBuilder.build());

        notifyID++;

    }

}