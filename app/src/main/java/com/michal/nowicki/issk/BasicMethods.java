package com.michal.nowicki.issk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Szczur on 25.06.2017. Used by ISSK.
 * This file has basic (for author) methods to use.
 */

final class BasicMethods {
    static final String[] PERMSNAMEINFO = new String[] {"konduktor_name", "numer", "przewodnik", "kal_resign", "id", "name", "ogl_add", "ogl_edit", "ogl_archive", "ogl_delete", "kal_show", "kal_hidden", "kal_edit", "kal_register", "kal_workedit", "kal_gdp", "szkol_show",
            "szkol_edit", "bhp_show", "bhp_edit", "rozkl_show", "rozkl_edit", "user_show", "user_add", "user_lock", "user_del", "user_edit", "user_level", "rep_show", "konduktor", "instruktor", "koordynator"};
    static final String[] ANNOUCEMENTVALUESNAMES = new String[] {"id", "title", "author_id", "author", "publish", "archive", "changed", "content"};
    static final String[] DRAWERMENUVALUES = new String[] {"Ogłoszenia", "Kalendarz", "Lista szkoleń", "Rozkłady jazdy", "Dodawanie rozkładów", "Konta użytkowników", "Raporty", "Grafik", "Regulamin służby", "Informacje dodatkowe", "Moje konto", "Ustawienia", "Wyloguj się"};
    static final String[] INFOKEYS = new String[] {"Imię i nazwisko", "Numer służbowy", "Przewodnik", "Możliwość rezygnacji ze służby", "Uprawnienia" /*, "E-mail*/};

    @Nullable
    static URL getMainURL(){
        URL[] url = new URL[1];
        try {
            URL mainURL = new URL("https://kmkm.waw.pl/issk/");
            url[0] = mainURL;
            return url[0];
        }
        catch (java.net.MalformedURLException e) {
            return null;
        }
    }

    static Integer checkSession(){
        try {
            CommonInternetTask CIT = new CommonInternetTask();
            CIT.execute("session_verify", false);
            return (int) CIT.get()[1];
        }
        catch(InterruptedException | ExecutionException e){
            return 5;
        }
    }

    @NonNull
    static String StringDateToNumberDate(@NonNull String inputDate){
        String[] date = inputDate.substring(0, inputDate.lastIndexOf(" ")).split(" ");

        switch(date[1]){
            case "stycznia":
            {
                date[1] = "01";
                break;
            }

            case "lutego":
            {
                date[1] = "02";
                break;
            }

            case "marca":
            {
                date[1] = "03";
                break;
            }

            case "kwietnia":
            {
                date[1] = "04";
                break;
            }

            case "maja":
            {
                date[1] = "05";
                break;
            }

            case "czerwca":
            {
                date[1] = "06";
                break;
            }

            case "lipca":
            {
                date[1] = "07";
                break;
            }

            case "sierpnia":
            {
                date[1] = "08";
                break;
            }

            case "września":
            {
                date[1] = "09";
                break;
            }

            case "października":
            {
                date[1] = "10";
                break;
            }

            case "listopada":
            {
                date[1] = "11";
                break;
            }

            case "grudnia":
            {
                date[1] = "12";
                break;
            }

            default:
                return "Niepoprawny miesiąc";
        }

        Collections.reverse(Arrays.asList(date));

        return date[0] + "." + date[1] + "." + date[2];
    }

    @NonNull
    static String webToUTF8Convert(@NonNull BufferedReader bufferedReader){
        int i = 0;
        char c;
        String receivedData;
        StringBuilder receivedDataBuilder = new StringBuilder();

        try {
            while((receivedData = bufferedReader.readLine()) != null){
                while(i < receivedData.length()){
                    c = receivedData.charAt(i++);
                    if (c == '\\') {
                        if(i < (receivedData.length() - 4)){
                            c = receivedData.charAt(i++);
                            if(c == 'u'){
                                c = (char) Integer.parseInt(receivedData.substring(i, i + 4), 16);
                                i += 4;
                            }
                        }
                    }

                    if(((c == '\"') || (c == '\'') || (c == '\\')) && !((receivedData.charAt(i - 2) == ':') || (receivedData.charAt(i - 2) == '{') || (receivedData.charAt(i - 2) == ',') || (receivedData.charAt(i) == ',') || (receivedData.charAt(i) == ':') || (receivedData.charAt(i) == '}')))
                        receivedDataBuilder.append('\\');


                    receivedDataBuilder.append(c);
                }
            }

            return receivedDataBuilder.toString();
        }
        catch(IOException e){
            return "Error!" + e.getLocalizedMessage();
        }
    }

    @NonNull
    static ArrayList<HashMap<String, String>> objToArray(@NonNull Object arrObj){
        ArrayList array = (ArrayList) arrObj;
        ArrayList<HashMap<String, String>> retArr = new ArrayList<>(array.size());

        for(int i = 0; i < array.size(); i++){
            HashMap<String, String> tmpMap = new HashMap<>();

            for(int j = 0; j < ((HashMap) array.get(i)).entrySet().size(); ++j){
                if(array.get(i) != null){
                    Object[] tmpArray = ((HashMap) array.get(i)).entrySet().toArray();

                    if(tmpArray != null){
                        if(tmpArray.length > j) {
                            tmpMap.put(((String) ((Map.Entry) tmpArray[j]).getKey()), ((String) ((Map.Entry) tmpArray[j]).getValue()));
                        }
                    }
                }
            }

            retArr.add(tmpMap);
        }

        return retArr;
    }
}
