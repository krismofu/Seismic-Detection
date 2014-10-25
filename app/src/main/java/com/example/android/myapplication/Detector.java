package com.example.android.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;
import android.hardware.SensorEvent;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
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
    public static final String Host = "host";
    public static final String Threshold = "threshold";
    SharedPreferences preferences;

    Context mContext;

    private int threshold;



/*
   @Override

    protected void onCreate(Bundle savedInstanceState) {

        /** Called when the activity is first created.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }
*/

    public Detector(Context mContext) {

        this.mContext = mContext;
        mInitialized = false;

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
/*

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }*/

 // @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

       // TextView tvX= (TextView)findViewById(R.id.x_axis);
       // TextView tvY= (TextView)findViewById(R.id.y_axis);
        //TextView tvZ= (TextView)findViewById(R.id.z_axis);
        //TextView accl = (TextView)findViewById(R.id.accl);

        String inputString = "";
        String  androidId  = "" + android.provider.Settings.Secure.getString( mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = sensorEvent.values[0];
            float y= sensorEvent.values[1];
            float z = sensorEvent.values[2];



            if (!mInitialized) {
                mLastX = x;
                mLastY = y;
                mLastZ = z;
                mInitialized = true;
            } else {

                /*
                    hitung delta perpindahn axix x, y, z

                 */

                float deltaX = Math.abs(mLastX - x);
                float deltaY = Math.abs(mLastY - y);
                float deltaZ = Math.abs(mLastZ - z);

                if (deltaX < NOISE)
                    deltaX = (float)0.0;
                if (deltaY < NOISE)
                    deltaY = (float)0.0;
                if (deltaZ < NOISE)
                    deltaZ = (float)0.0;

                mLastX = x;
                mLastY = y;
                mLastZ = z;



               double value = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                //accl.setText(Double.toString(value));


               /// String value = Double.toString(accl) +Threshold;

                /*
                    hitung refresh rate sensor
                    untuk dapatin berapa frequency nya

                long tS;

                // Get timestamp of the event
                tS = sensorEvent.timestamp;

                // Get the mean frequency for "nbElements" (=30) elements
                if (now != 0) {
                    temp++;
                    if (temp == nbElements) {
                        time = tS - now;
                        //value.setText(Double.toString(nbElements * 1000000000 / time));

                        temp = 0;
                    }
                }

                // To set up now on the first event and do not change it while we do not have "nbElements" events
                if (temp == 0) {
                    now = tS;
                }

                //tampilkan delta axix x, y dan z

                tvX.setText(Float.toString(deltaX));
                tvY.setText(Float.toString(deltaY));
                tvZ.setText(Float.toString(deltaZ));
                */
                // jika delta x atau y atau z lebih besar dari 5
                // kirim data ke server

                //Toast.makeText(mContext, "Service Destroyed Host = " +Host, Toast.LENGTH_LONG).show();
               // tvX.setText(Float.toString(deltaX));
               // tvY.setText(Float.toString(deltaY));
               // tvZ.setText(Float.toString(deltaZ));

// kirim data ke server

            threshold = preferences.getInt(Threshold,0);

                if(value >= threshold && value > 0){

                    Context context = mContext.getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    CharSequence text = "";
                    inputString = preferences.getString(Host, "");
                    if(inputString.matches("")){
                        text = "Please insert Server's IP !";
                    }
                    else{
                        UserFunctions userFunction = new UserFunctions();
                        doJson requestJson = new doJson();


                        requestJson.execute(inputString, androidId, Float.toString(deltaX) ,Float.toString(deltaY), Float.toString(deltaZ));

                        //text = "inputString "+inputString+" androidId "+androidId+" "+Float.toString(deltaX) +" "+ Float.toString(deltaY) +" "+ Float.toString(deltaZ);

                        JSONObject json = null;
                        try {
                            json = requestJson.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        text = androidId + " Sent Request!";
                        //text = Threshold + "tes";


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
    }

    @Override

    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void turnOff() {
        mSensorManager.unregisterListener(this);
    }
}
