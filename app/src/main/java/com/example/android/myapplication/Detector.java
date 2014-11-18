package com.example.android.myapplication;

import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;
import android.hardware.SensorEvent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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

    public Detector(Context mContext) {

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
                Log.i("VALUE", Double.toString(mvalue));

                // jika delta x atau y atau z lebih besar dari 5
                // kirim data ke server
                if(mvalue >= threshold && mvalue > 0 ){
                    doJson requestJson = new doJson();

                    requestJson.execute(host, androidId, Float.toString(deltaX), Float.toString(deltaY), Float.toString(deltaZ), Double.toString(mvalue));

                    JSONObject json = null;
                    try {
                        json = requestJson.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                        /*
                           periksa balasan dari server apakah proses input berhasil
                        */

                    // check for login response
                        /*
                        try {
                            if (json.getString(KEY_SUCCESS) != null) {
                                String res = json.getString(KEY_SUCCESS);
                                if(Integer.parseInt(res) == 1){

                                    text = androidId + " Sent Request!";

                                    finish();

                                }else{
                                    // Error in login
                                    text = "Gagal menambah data";
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        */
                    //  Toast toast = Toast.makeText(context, text, duration);
                    // toast.show();
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
}
