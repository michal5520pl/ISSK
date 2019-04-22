package com.michal.nowicki.issk;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Szczur on 20.06.2018. Used by ISSK.
 */
final class CommonInternetTask extends AsyncTask<Object, Void, Object[]> {
    private static HttpsURLConnection HTTPSURLCONNECT;

    /**
     * @param objects
     *  first - activity to do (the last URL part [without .php])
     *  second - true if there is any output, false if not
     *  third - array of headers to send to server (nullable)
     *  fourth - array of objects to send to server (nullable)
     * @return First element is boolean, second one of these: ArrayList, String or int.
     */
    @Override
    protected Object[] doInBackground(@NonNull Object... objects){
        StringBuilder SBsend = new StringBuilder();
        URL url;

        if(objects.length != 2 && objects.length != 4){
            return new Object[]{true, "ERROR: incorrect number of arguments."};
        }
        else {
            if(objects.length == 4 && objects[2] != null && objects[3] != null && (boolean) objects[1]){
                try {
                    for(int i = 0; i < ((Object[]) objects[2]).length; ++i){
                        SBsend.append("&").append(URLEncoder.encode((String) (((Object[]) objects[2])[i]), "UTF-8"));
                        SBsend.append("=");
                        SBsend.append(URLEncoder.encode((String) (((Object[]) objects[3])[i]), "UTF-8"));
                    }
                }
                catch(UnsupportedEncodingException e){
                    return new Object[]{true, "Unsupported encoding!" + e.getLocalizedMessage()};
                }
            }

            try {
                url = new URL(BasicMethods.getMainURL() + (String) objects[0] + ".php?api=1");

                CommonInternetTask.HTTPSURLCONNECT = (HttpsURLConnection) url.openConnection();
                CommonInternetTask.HTTPSURLCONNECT.setDoOutput(true);
                CommonInternetTask.HTTPSURLCONNECT.connect();

                if((boolean) objects[1]){
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(CommonInternetTask.HTTPSURLCONNECT.getOutputStream());
                    outputStreamWriter.write(SBsend.toString());
                    outputStreamWriter.flush();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(CommonInternetTask.HTTPSURLCONNECT.getInputStream()));
                String receivedData = BasicMethods.webToUTF8Convert(bufferedReader).trim();

                if(receivedData.contains("\"error\":false") || objects[0].equals("session_verify")){
                    if(objects[0].equals("login")){
                        JSONObject jsonObject = new JSONObject(receivedData);
                        jsonObject = jsonObject.getJSONObject("perm");
                        ArrayList<String> array = new ArrayList<>();

                        for(String aName : BasicMethods.PERMSNAMEINFO){
                            switch(aName){
                                case "konduktor_name":
                                case "name":
                                case "numer":
                                    array.add(jsonObject.optString(aName));
                                    break;
                                case "id":
                                    int result = jsonObject.getInt(aName);
                                    array.add(String.valueOf(result));
                                    break;
                                default:
                                    if(jsonObject.getInt(aName) != 0){
                                        array.add(aName);
                                    }
                                    break;
                            }
                        }
                        return new Object[]{false, array};
                    }
                    else if(objects[0].equals("callendar/get")){
                        JSONObject jsonObject = new JSONObject(receivedData);
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

                        return new Object[]{false, arrayList};
                    }
                    else if(objects[0].equals("notice/archive_show")){
                        JSONArray jsonArray = new JSONArray(receivedData.substring(23, receivedData.lastIndexOf("]") + 1).trim());
                        ArrayList<HashMap<String, String>> jsonObjects = new ArrayList<>();
                        HashMap<String, String> tempMap = new HashMap<>();
                        int k = 0;

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

                        return new Object[]{false, jsonObjects};
                    }

                    else if(objects[0].equals("notice/getall")){
                        if((receivedData.lastIndexOf("]") - receivedData.indexOf("[")) == 1){
                            return new Object[]{false, new ArrayList<String>()};
                        }
                        JSONArray jsonArray = new JSONArray(receivedData.substring(23, receivedData.lastIndexOf("]") + 1).trim());
                        ArrayList<HashMap<String, String>> jsonObjects = new ArrayList<>();
                        HashMap<String, String> tempMap = new HashMap<>();
                        int k = 0;

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

                        return new Object[]{false, jsonObjects};
                    }

                    else if(objects[0].equals("session_verify")){
                        JSONObject jsonObject = new JSONObject(receivedData);
                        return new Object[]{false, jsonObject.getInt("error")};
                    }
                    else {
                        return new Object[]{false, receivedData};
                    }
                }
                else {
                    return new Object[]{true, receivedData};
                }
            }
            catch(IOException | JSONException | ParseException e){
                return new Object[]{true, e.getLocalizedMessage()};
            }
        }
    }
}
