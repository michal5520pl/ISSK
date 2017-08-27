package com.michal.nowicki.issk;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Szczur on 20.07.2017.
 */

final class AnnouncementFragmentTask extends AsyncTask<Void, Void, Object> {
    @Override
    protected Object doInBackground(Void... voids) {
        try {
            StringBuilder SB = new StringBuilder();
            URL noticeGetURL = new URL(BasicMethods.getMainURL() + "notice/getall.php");
            LoginActivityTask.HTTPSURLCONNECT = (HttpsURLConnection) noticeGetURL.openConnection();
            LoginActivityTask.HTTPSURLCONNECT.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(LoginActivityTask.HTTPSURLCONNECT.getInputStream()));

            String receivedData;
            while((receivedData = reader.readLine()) != null){
                SB.append(receivedData);
            }

            if(SB.toString().contains("\"error\":false")){

                return null;
            }
            else {
                //gsghfsg
                return null;
            }
        }
        catch (Exception e){
            //allah
            return null;
        }
    }
}
