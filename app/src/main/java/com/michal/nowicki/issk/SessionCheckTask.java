package com.michal.nowicki.issk;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Created by Szczur on 16.08.2017. Used by ISSK.
 * This class checks, if session is valid.
 */

class SessionCheckTask extends AsyncTask<String, Void, Integer> {
    @Override
    protected Integer doInBackground(String... strings) {
        try {

            //test
            LoginActivity LA = new LoginActivity();
            HttpResponseCache responseCache = LA.httpCacheFileCreate();

            StringBuilder SB = new StringBuilder();
            URL verifyURL = new URL(BasicMethods.getMainURL() + "session_verify.php");
            HttpURLConnection verifyURLConnection = (HttpURLConnection) verifyURL.openConnection();
            verifyURLConnection.setUseCaches(true);
            verifyURLConnection.setDoOutput(true);
            verifyURLConnection.setRequestProperty("Cookie", "PHPSESSID=" + strings[0] +"; domain=" + BasicMethods.getMainURL() + "; path=/");
            verifyURLConnection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(verifyURLConnection.getInputStream()));

            String receivedData;
            while((receivedData = reader.readLine()) != null){
                SB.append(receivedData);
            }

            JSONObject receivedJSONObject = new JSONObject(SB.toString().trim());
            return receivedJSONObject.getInt("error");
        }
        catch(Exception e){
            return null;
        }
    }
}
