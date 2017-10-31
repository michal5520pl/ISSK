package com.michal.nowicki.issk;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Szczur on 15.08.2017. Used by ISSK.
 * CalendarFragmentTask - used by CalendarFragment
 */

final class CalendarFragmentTask extends AsyncTask<Void, Void, ArrayList<String>> {

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        try {
            StringBuilder SB = new StringBuilder();
            URL calendarGetURL = new URL(BasicMethods.getMainURL() + "callendar/get.php");
            URLConnection calendarGetConnect = calendarGetURL.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(calendarGetConnect.getInputStream()));

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
                JSONObject jsonObject = new JSONObject(SB.toString().trim());
                JSONArray normalDays = jsonObject.getJSONArray("value");
                JSONArray hiddenDays = jsonObject.getJSONArray("hidden");
                ArrayList<String> arrayList = new ArrayList<>();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                if(normalDays != null && normalDays.length() > 0){
                    for(int j = 0; j < normalDays.length(); j++){
                        arrayList.add(dateFormatter.format(dateFormatter.parse(normalDays.getString(j))));
                    }
                }
                if(hiddenDays != null && hiddenDays.length() > 0){
                    for(int j = 0; j < hiddenDays.length(); j++){
                        arrayList.add(dateFormatter.format(dateFormatter.parse(hiddenDays.getString(j))));
                    }
                }

                return arrayList;
            }
            else {
                JSONObject errorObject = new JSONObject(SB.toString().trim());
                ArrayList<String> errorList = new ArrayList<>();

                errorList.add("Error");
                errorList.add(errorObject.getString("error"));
                return errorList;
            }
        }
        catch (Exception e){
            ArrayList<String> error = new ArrayList<>();
            error.add("Exception");
            error.add(e.getMessage());
            return error;
        }
    }
}
