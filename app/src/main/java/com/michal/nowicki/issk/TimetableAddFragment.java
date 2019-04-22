package com.michal.nowicki.issk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Szczur on 15.08.2017. Used by ISSK.
 * This Fragment displays adding timetable page.
 */

public class TimetableAddFragment extends Fragment {
    static boolean open = false;

    public TimetableAddFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetableadd, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        if(open){
            WebView webV;

            if (view.findViewById(R.id.webview) != null) {
                webV = view.findViewById(R.id.webview);
                webV.setVisibility(View.INVISIBLE);
                webV.loadUrl("https://drive.google.com/drive/folders/0B5wPhEU3v_0IQzN6Tlh6VFhXZ2M?usp=sharing");
                open = false;
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        open = true;
    }
}
