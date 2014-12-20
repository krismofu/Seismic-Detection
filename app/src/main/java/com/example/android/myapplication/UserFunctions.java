package com.example.android.myapplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maruli on 21/09/14.
 */
public class UserFunctions {
    private JSONParser jsonParser;

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    public JSONObject tambahData(String registerURL, String deviceId,  String magnitude, String x, String y, String z) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "insert"));
        params.add(new BasicNameValuePair("deviceId", deviceId));
        params.add(new BasicNameValuePair("magnitude", magnitude));
        params.add(new BasicNameValuePair("x", x));
        params.add(new BasicNameValuePair("y", y));
        params.add(new BasicNameValuePair("z", z));


        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
}
