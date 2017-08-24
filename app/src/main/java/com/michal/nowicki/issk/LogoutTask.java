package com.michal.nowicki.issk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Szczur on 05.07.2017. Used by ISSK.
 * LogoutTask is used to logout user from ISSK.
 */

final class LogoutTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            URL logoutURL = new URL(BasicMethods.getMainURL() + "logout.php?api=1");

            URLConnection logoutconnect = logoutURL.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(logoutconnect.getInputStream()));

            return reader.readLine().equals("{\"error\":false}");
        }
        catch (IOException e){
            return false;
        }
    }
}