package com.michal.nowicki.issk;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Szczur on 25.06.2017. Used by ISSK.
 * This file has basic (for author) methods to use.
 */

final class BasicMethods {

    static final String[] PERMSNAMEINFO = new String[] {"konduktor_name", "numer", "przewodnik", "kal_resign", "id", "name", "ogl_add", "ogl_edit", "ogl_archive", "kal_show", "kal_hidden", "kal_edit", "kal_register", "kal_workedit", "kal_gdp", "szkol_show",
            "szkol_edit", "bhp_show", "bhp_edit", "rozkl_show", "rozkl_edit", "user_show", "user_add", "user_lock", "user_del", "user_edit", "user_level", "rep_show", "konduktor", "instruktor", "koordynator"};
    static final String[] ANNOUCEMENTVALUESNAMES = new String[] {"id", "title", "author_id", "author", "publish", "archive", "changed", "content"};
    static final String[] DRAWERMENUVALUES = new String[] {"Ogłoszenia", "Kalendarz", "Lista szkoleń", "Rozkłady jazdy", "Dodawanie rozkładów", "Konta użytkowników", "Raporty", "Grafik", "Regulamin służby", "Informacje dodatkowe", "Moje konto", "Wyloguj się"};
    static final String[] INFOKEYS = new String[] {"Imię i nazwisko", "Numer służbowy", "Przewodnik", "Możliwość rezygnacji ze służby", "Uprawnienia" /*, "E-mail*/};

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

    static Integer checkSession(){
        try {
            SessionCheckTask SCT = new SessionCheckTask();
            SCT.execute();
            return SCT.get();
        }
        catch(InterruptedException | ExecutionException e){
            return 5;
        }
    }
}
