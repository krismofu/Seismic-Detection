package com.example.android.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;
import android.content.SharedPreferences;


/**
 * Created by maruli on 21/09/14.
 */
public class MyService extends Service {

    public static final String Name = "host";
    public static final String Threshold = "threshold";

    SharedPreferences preferences;
    String host, threshold;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        host = preferences.getString(Name, "");
        threshold = preferences.getString(Threshold, "");

        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started Host = "+host+" threshold = "+threshold, Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
