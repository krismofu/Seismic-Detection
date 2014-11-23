package com.example.android.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    Detector detection;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //aktifkan detektor
        detection = new Detector(this);

        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started Host", Toast.LENGTH_SHORT).show();

        //showNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //non aktifkan detektor
        detection.turnOff();
        detection = null;
        System.gc();

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    public void showNotification() {
        //define soune URI, the sound to be played when there's a notification
        //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUri = Uri.parse("android.resource://com.example.android.myapplication/"+R.raw.alarm_collision);

        // intent triggered, you can add other intent for other actions
        //Intent intent = new Intent(MyActivity.this, MyActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MyActivity.class), 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)
                .setContentTitle("ALERT!")
                .setContentText("Terjadi Gempa!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(soundUri).addAction(R.drawable.ic_launcher, "View", pIntent)
                        //.addAction(0, "Remind", pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, mNotification);
    }
}
