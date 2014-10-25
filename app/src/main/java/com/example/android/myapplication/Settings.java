package com.example.android.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.view.View;


public class Settings extends Activity {

    TextView host ;
    TextView threshold;
    public static final String Host = "host";
    public static final String Threshold = "threshold";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        host = (TextView) findViewById(R.id.editTextHost);
        threshold = (TextView) findViewById(R.id.editTextThreshold);

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedpreferences.contains(Host))
        {
            host.setText(sharedpreferences.getString(Host, ""));

        }
        if (sharedpreferences.contains(Threshold))
        {
            int thresholdint = sharedpreferences.getInt(Threshold,0);
            String myString = String.valueOf(thresholdint);
            threshold.setText(myString);


        }

    }

    public void run(View view){
        String n  = host.getText().toString();
        String ph  = threshold.getText().toString();
        Editor editor = sharedpreferences.edit();
        editor.putString(Host, n);
        editor.putInt(Threshold,Integer.parseInt(ph));
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
