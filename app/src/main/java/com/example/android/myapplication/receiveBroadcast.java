package com.example.android.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class receiveBroadcast extends BroadcastReceiver {

    public receiveBroadcast() {
        MyActivity.value.setText("0");
    } 

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("VALUE received ", "");
        MyActivity.value.setText(intent.getCharSequenceExtra("message"));
        // Extract data included in the Intent
        //CharSequence intentData = intent.getCharSequenceExtra("message");
        //Toast.makeText(context, "Javacodegeeks received the Intent's message: " + intentData, Toast.LENGTH_LONG).show();
    }
}
