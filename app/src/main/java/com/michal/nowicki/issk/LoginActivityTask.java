package com.michal.nowicki.issk;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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
                jsonObject = jsonObject.getJSONObject("perm");
                ArrayList<String> array = new ArrayList<>();

                for(String aName : BasicMethods.PERMSNAMEINFO){
                    switch (aName) {
                        case "konduktor_name":
                        case "name":
                        case "numer":
                            array.add(jsonObject.optString(aName));
                            break;
                        case "id":
                            Integer result = jsonObject.getInt(aName);
                            array.add(result.toString());
                            break;
                        default:
                            if (jsonObject.getInt(aName) != 0) {
                                array.add(aName);
                            }
                            break;
                    }
                }
                return array;
            }
            else if(SB.toString().contains("\"error\":\"B\u0142\u0119dny login lub has\u0142o!\"")){
                return "Error: 1";
            }
            else if(SB.toString().contains("\"error\":\"B\u0142\u0105d po\u0142\u0105czenia z baz\u0105 danych!\"") || SB.toString().contains("Access denied for user")){
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
