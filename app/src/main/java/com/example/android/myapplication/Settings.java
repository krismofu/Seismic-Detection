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

    TextView name ;
    TextView phone;
    public static final String Name = "host";
    public static final String Phone = "threshold";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name = (TextView) findViewById(R.id.editTextHost);
        phone = (TextView) findViewById(R.id.editTextThreshold);

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedpreferences.contains(Name))
        {
            name.setText(sharedpreferences.getString(Name, ""));

        }
        if (sharedpreferences.contains(Phone))
        {
            phone.setText(sharedpreferences.getString(Phone, ""));

        }

    }

    public void run(View view){
        String n  = name.getText().toString();
        String ph  = phone.getText().toString();
        Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.putString(Phone, ph);
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
