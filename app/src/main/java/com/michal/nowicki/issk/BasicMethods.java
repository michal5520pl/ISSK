package com.michal.nowicki.issk;

import java.net.URL;

/**
 * Created by Szczur on 25.06.2017. Used by ISSK.
 * This file has basic (for author) methods to use.
 */

final class BasicMethods {

    static URL getMainURL(){
        URL[] url = new URL[1];
        try {
            URL mainURL = new URL("https://issk.pl/issk/");
            url[0] = mainURL;
            return url[0];
        }
        catch (java.net.MalformedURLException e) {
            return null;
        }
    }
}
