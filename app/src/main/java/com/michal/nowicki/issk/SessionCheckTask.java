package com.michal.nowicki.issk;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Szczur on 16.08.2017. Used by ISSK.
 * This class checks, if session is valid.
 */

class SessionCheckTask extends AsyncTask<Void, Void, Integer> {
    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            StringBuilder SB = new StringBuilder();
            URL verifyURL = new URL(BasicMethods.getMainURL() + "session_verify.php");

            LoginActivityTask.HTTPSURLCONNECT = (HttpsURLConnection) verifyURL.openConnection();
            LoginActivityTask.HTTPSURLCONNECT.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(LoginActivityTask.HTTPSURLCONNECT.getInputStream()));

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
