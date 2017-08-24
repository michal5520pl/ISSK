package com.michal.nowicki.issk;

import android.os.Bundle;
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

    public TimetableFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        WebView webV;

        if(getView() != null){
            if(getView().findViewById(R.id.webview) != null){
                webV = getView().findViewById(R.id.webview);
                webV.setVisibility(View.INVISIBLE);
                webV.loadUrl("https://drive.google.com/open?id=0B5wPhEU3v_0INlEteEJIVWhxLUE");
            }
        }
    }
}
