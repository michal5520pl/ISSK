package com.michal.nowicki.issk;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Szczur on 08.10.2017. Used by ISSK.
 * This class downloads archive notices.
 */

final class ArchiveAnnouncementFragmentTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(Void... voids) {
        try {
            StringBuilder SB = new StringBuilder();
            URL archiveNoticeGetURL = new URL(BasicMethods.getMainURL() + "notice/archive_show.php");
            LoginActivityTask.HTTPSURLCONNECT = (HttpsURLConnection) archiveNoticeGetURL.openConnection();
            LoginActivityTask.HTTPSURLCONNECT.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(LoginActivityTask.HTTPSURLCONNECT.getInputStream()));
            String receivedData;
            int i = 0;
            char c;

            while((receivedData = reader.readLine()) != null){
                while(i < receivedData.length()){
                    c = receivedData.charAt(i++);
                    if(c == '\\'){
                        if(i < (receivedData.length() - 4)){
                            c = receivedData.charAt(i++);
                            if(c == 'u'){
                                c = (char) Integer.parseInt(receivedData.substring(i, i + 4), 16);
                                i += 4;
                            }
                        }
                    }
                    SB.append(c);
                }
            }

            if(SB.toString().contains("\"error\":false")){
                JSONArray jsonArray = new JSONArray(SB.toString().substring(23).trim());
                ArrayList<HashMap<String, String>> jsonObjects = new ArrayList<>();
                HashMap<String, String> tempMap = new HashMap<>();
                Integer k = 0;

                while(k < jsonArray.length()){
                    for(String annNameValue : BasicMethods.ANNOUCEMENTVALUESNAMES){
                        if(MainActivity.getPermsString().contains("ogl_edit"))
                            tempMap.put(annNameValue, (jsonArray.optJSONObject(k)).optString(annNameValue));
                        else if(!(annNameValue.equals("archive") || annNameValue.equals("changed"))){
                            tempMap.put(annNameValue, (jsonArray.optJSONObject(k)).optString(annNameValue));
                        }
                    }
                    jsonObjects.add(k, new HashMap<>(tempMap));
                    tempMap.clear();
                    k++;
                }

                return jsonObjects;
            }
            else {
                JSONObject jsonObject = new JSONObject(SB.toString().trim());
                ArrayList<HashMap<String, String>> errorArray = new ArrayList<>(1);
                HashMap<String, String> tempMap = new HashMap<>(1);
                tempMap.put("error", jsonObject.optString("error"));
                errorArray.add(0, tempMap);
                return errorArray;
            }
        }
        catch (IOException | JSONException e){
            return null;
        }
    }
}
