package com.example.android.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

public class doJson extends AsyncTask<String, Void, JSONObject> {
    private JSONObject json;

    @Override
    protected JSONObject doInBackground(String... strings) {
        UserFunctions userFunction = new UserFunctions();
        json = userFunction.tambahData(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6]);
//        Log.d("ISI_JSON", json.toString());
        return json;
    }
}
