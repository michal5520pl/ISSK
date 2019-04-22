package com.michal.nowicki.issk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Szczur on 05.07.2017. Used by ISSK.
 * TimetableFragment
 */

public class TimetableFragment extends Fragment {
    static boolean open = true;

    public TimetableFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        if(open){
            WebView webV;

            if (view.findViewById(R.id.webview) != null) {
                webV = view.findViewById(R.id.webview);
                webV.setVisibility(View.INVISIBLE);
                webV.loadUrl("https://drive.google.com/open?id=0B5wPhEU3v_0INlEteEJIVWhxLUE");
                open = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        open = true;
    }
}
