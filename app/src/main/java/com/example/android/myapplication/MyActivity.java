package com.example.android.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity {

    Button btnStartService, btnStopService;
    ComponentName receiver;
    PackageManager pm;
    public static TextView value;
    private receiveBroadcast myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        value = (TextView)findViewById(R.id.value);

        receiver = new ComponentName(this, BootCompleted.class);
        pm = this.getPackageManager();

        btnStartService = (Button) findViewById(R.id.btnStartService);
        btnStopService = (Button) findViewById(R.id.btnStopService);

        myReceiver = new receiveBroadcast();

        updateButtonState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent i;

        switch (item.getItemId()){
            case R.id.action_settings :
                i = new Intent(this, Settings.class);
                startActivity(i);
            break;

            case R.id.action_about :
                i = new Intent(this, About.class);
                startActivity(i);
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
        updateButtonState();

        //enable auto start
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
        updateButtonState();

        //disable auto start
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void updateButtonState() {
        if (isMyServiceRunning(MyService.class)) {
            btnStopService.setClickable(true);
            btnStopService.setEnabled(true);

            btnStartService.setClickable(false);
            btnStartService.setEnabled(false);
        }
        else {
            btnStopService.setClickable(false);
            btnStopService.setEnabled(false);

            btnStartService.setClickable(true);
            btnStartService.setEnabled(true);
        }


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
