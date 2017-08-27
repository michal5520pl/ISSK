package com.michal.nowicki.issk;

import android.app.Activity;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.*;
import java.net.CacheRequest;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Szczur on 25.06.2017. Used by ISSK.
 * LoginActivityTask - network task.
 * LoginActivityTask is used by LoginActivity to connect to login page and send/receive data.
 */

final class LoginActivityTask extends AsyncTask<Object, Void, Object> {

    static HttpsURLConnection HTTPSURLCONNECT;

    @Override
    protected Object doInBackground(Object[] objects){
        try {
            String[] name = new String[] {"konduktor_name", "numer", "przewodnik", "kal_resign", "id", "name", "ogl_add", "ogl_edit", "ogl_archive", "kal_show", "kal_hidden", "kal_edit", "kal_register", "kal_workedit", "kal_gdp", "szkol_show",
                    "szkol_edit", "bhp_show", "bhp_edit", "rozkl_show", "rozkl_edit", "user_show", "user_add", "user_lock", "user_del", "user_edit", "user_level", "rep_show", "konduktor", "instruktor", "koordynator"};

            StringBuilder SB = new StringBuilder();
            URL loginURL = new URL(BasicMethods.getMainURL() + "login.php?api=1"); //tworzenie URL

            String emailANDpassPOST = URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(objects[0].toString(),"UTF-8");
            emailANDpassPOST += "&" + URLEncoder.encode("haslo","UTF-8") + "=" + URLEncoder.encode(objects[1].toString(),"UTF-8"); //Enkodowanie danych

            LoginActivityTask.HTTPSURLCONNECT = (HttpsURLConnection) loginURL.openConnection(); //otwarcie połączenia
            HTTPSURLCONNECT.setDoOutput(true);

            OutputStreamWriter sendData = new OutputStreamWriter(HTTPSURLCONNECT.getOutputStream()); //do wysyłania
            sendData.write(emailANDpassPOST);
            sendData.flush(); //wysyłanie danych i czyszczenie bufora

            BufferedReader reader = new BufferedReader(new InputStreamReader(HTTPSURLCONNECT.getInputStream())); //do otrzymywania
            String receivedData;

            while((receivedData = reader.readLine()) != null){
                SB.append(receivedData);
            }

            if(SB.toString().contains("\"error\":false")){
                JSONObject jsonobject1 = new JSONObject(SB.toString().trim());
                JSONObject jsonobject = jsonobject1.getJSONObject("perm");
                ArrayList<String> array = new ArrayList<>();

                for(String aName : name){
                    if((aName.contains("konduktor_name")) || (aName.contains("name"))) {
                        array.add(jsonobject.optString(aName));
                    }
                    else if (aName.contains("id") || aName.contains("numer")) {
                        Integer result = jsonobject.getInt(aName);
                        array.add(result.toString());
                    }
                    else {
                        if (jsonobject.getInt(aName) != 0) {
                            array.add(aName);
                        }
                    }
                }
                return array;
            }
            else if(SB.toString().contains("\"error\":\"B\\u0142\\u0119dny login lub has\\u0142o!\"")){
                return "Error: 1";
            }
            else if(SB.toString().contains("\"error\":\"B\\u0142\\u0105d po\\u0142\\u0105czenia z baz\\u0105 danych!\"") || SB.toString().contains("Access denied for user")){
                return "Error: 2";
            }
            else if(SB.toString().contains("\"error\"") && !(SB.toString().contains("\"error\":false"))){
                return "Error: 3";
            }
            else throw new UnknownError();
        }
        catch (Exception e){
            return ("Exception:" + e.getMessage());
        }
    }
}
