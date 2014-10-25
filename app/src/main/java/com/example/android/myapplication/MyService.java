package com.example.android.myapplication;

import android.app.Service;
import android.content.Intent;
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
}
