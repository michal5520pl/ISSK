package com.michal.nowicki.issk;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.net.URL;
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
            calendarGetConnect.setRequestProperty("Cookie", new MainActivity().getCookie() + "; domain=" + BasicMethods.getMainURL() + "; path=/");

            BufferedReader reader = new BufferedReader(new InputStreamReader(calendarGetConnect.getInputStream()));

            String receivedData;
            while((receivedData = reader.readLine()) != null){
                SB.append(receivedData);
            }

            if(SB.toString().contains("\"error\":false")){
                JSONObject jsonObject = new JSONObject(SB.toString().trim());
                JSONObject normalDays = jsonObject.getJSONObject("value");
                JSONObject hiddenDays = jsonObject.getJSONObject("hidden");
                ArrayList<String> arrayList = new ArrayList<>();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                if(normalDays != null){
                    Iterator<String> normalDaysIterator = normalDays.keys();
                    while(normalDaysIterator.hasNext()){
                        arrayList.add(dateFormatter.format(dateFormatter.parse(normalDaysIterator.next())));
                    }
                }
                if(hiddenDays != null){
                    Iterator<String> hiddenDaysIterator = hiddenDays.keys();
                    while(hiddenDaysIterator.hasNext()){
                        arrayList.add(dateFormatter.format(dateFormatter.parse(hiddenDaysIterator.next())));
                    }
                }

                return arrayList;
            }
            else {
                JSONObject errorObject = new JSONObject(SB.toString().trim());
                ArrayList<String> errorList = new ArrayList<>();
                errorList.add("error" + errorObject.getString("error"));
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
