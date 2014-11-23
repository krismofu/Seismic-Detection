package com.example.android.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;
import android.hardware.SensorEvent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class Detector implements SensorEventListener {
    private float mLastX, mLastY, mLastZ;
   // private static final double accl;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    private long now = 0;
    private long time = 0;
    private int temp = 0;
    private static String KEY_SUCCESS = "success";
    private final String Host = "host";
    private final String Threshold = "threshold";
    private String host;
    private int threshold;
    private SharedPreferences preferences;
    private String androidId;
    private Context context;

    public Detector(Context mContext) {
        context = mContext;
        mInitialized = false;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        threshold = preferences.getInt(Threshold,0);
        host = preferences.getString(Host, "");
        androidId  = "" + android.provider.Settings.Secure.getString( mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if (!mInitialized) {
                mLastX = x;
                mLastY = y;
                mLastZ = z;
                mInitialized = true;
            } else {
                //hitung delta perpindahn axix x, y, z
                float deltaX = Math.abs(mLastX - x);
                float deltaY = Math.abs(mLastY - y);
                float deltaZ = Math.abs(mLastZ - z);

                if (deltaX < NOISE)
                    deltaX = (float) 0.0;
                if (deltaY < NOISE)
                    deltaY = (float) 0.0;
                if (deltaZ < NOISE)
                    deltaZ = (float) 0.0;

                mLastX = x;
                mLastY = y;
                mLastZ = z;

                double mvalue = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                // jika delta x atau y atau z lebih besar dari 5
                // kirim data ke server
                if(mvalue >= threshold && mvalue > 0 ){

                    Log.i("VALUE", Double.toString(mvalue));

                    doJson requestJson = new doJson();
                    requestJson.execute(host, androidId, Double.toString(mvalue));
                    //requestJson.execute(host, androidId, Float.toString(deltaX), Float.toString(deltaY), Float.toString(deltaZ), Double.toString(mvalue));

                    JSONObject json = null;
                    try {
                        json = requestJson.get();
                        JSONObject uniObject = json.getJSONObject("response");
                        String  alert = uniObject.getString("alert");
                        Log.i("ALERT", alert);
                        if(alert == "true") {
                            Toast.makeText(context, "Terjadi Gempa", Toast.LENGTH_SHORT).show();

                            showNotification();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void turnOff() {
        mSensorManager.unregisterListener(this);
    }

    public void showNotification() {
        //define soune URI, the sound to be played when there's a notification
        //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUri = Uri.parse("android.resource://com.example.android.myapplication/"+R.raw.alarm_collision);

        // intent triggered, you can add other intent for other actions
        //Intent intent = new Intent(MyActivity.this, MyActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MyActivity.class), 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(context)
                .setContentTitle("ALERT!")
                .setContentText("Terjadi Gempa!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(soundUri).addAction(R.drawable.ic_launcher, "View", pIntent)
                        //.addAction(0, "Remind", pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, mNotification);
    }
}
