package com.michal.nowicki.issk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Szczur on 07.11.2017. Used by ISSK.
 */

final class NoticeDeleteOrArchiveTask extends AsyncTask<Integer, Void, String> {
    @Override
    protected String doInBackground(Integer... integers) {
        try {
            if(integers[1] != 0 && integers[1] != 1)
                return "Niepoprawny typ operacji!";

            if(integers[0] == null || integers[0] < 0)
                return "Niepoprawne ID ogłoszenia!";

            StringBuilder SB = new StringBuilder();
            String idToSend = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(integers[0]), "UTF-8");
            URL noticeURL;

            if(integers[1].equals(0)){
                noticeURL = new URL(BasicMethods.getMainURL() + "notice/delete.php?api=1");
            }
            else if(integers[1].equals(1)){
                noticeURL = new URL(BasicMethods.getMainURL() + "notice/archive.php?api=1");
            }
            else {
                return "Niepoprawny typ operacji!";
            }

            LoginActivityTask.HTTPSURLCONNECT = (HttpsURLConnection) noticeURL.openConnection();
            LoginActivityTask.HTTPSURLCONNECT.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(LoginActivityTask.HTTPSURLCONNECT.getOutputStream());
            outputStreamWriter.write(idToSend);
            outputStreamWriter.flush();

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

            if(SB.toString().contains("\"error\":false"))
                return "false";
            else
                return SB.toString();//.split(":")[1].substring(0, SB.toString().split(":")[1].lastIndexOf("}"));
        }
        catch(IOException e){
            return "Exception: " + e.getLocalizedMessage();
        }
    }
}
