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
 * Created by Szczur on 06.11.2017. Used by ISSK.
 */

final class NoticeEditActivityTask extends AsyncTask<String[], Void, String> {
    @Override
    protected String doInBackground(String[]... strings) {
        try {
            StringBuilder SB = new StringBuilder();
            URL noticeURL;
            String dataToSend;

            if(strings[0].length > 3 && strings[0][3] != null && Integer.valueOf(strings[0][3]) != null) {
                noticeURL = new URL(BasicMethods.getMainURL() + "notice/edit.php?api=1");

                dataToSend = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(strings[0][3], "UTF-8");
                dataToSend += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(strings[0][0], "UTF-8");
            }
            else {
                noticeURL = new URL(BasicMethods.getMainURL() + "notice/add.php?api=1");

                dataToSend = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(strings[0][0], "UTF-8");
            }

            dataToSend += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(strings[0][1], "UTF-8");
            dataToSend += "&" + URLEncoder.encode("archive", "UTF-8") + "=" + URLEncoder.encode(strings[0][2], "UTF-8");

            LoginActivityTask.HTTPSURLCONNECT = (HttpsURLConnection) noticeURL.openConnection();
            LoginActivityTask.HTTPSURLCONNECT.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(LoginActivityTask.HTTPSURLCONNECT.getOutputStream());
            outputStreamWriter.write(dataToSend);
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
                return SB.toString().split(":")[1].substring(0, SB.toString().split(":")[1].lastIndexOf("}"));
        }
        catch(IOException e){
            return "Exception: " + e.getLocalizedMessage();
        }
    }
}
