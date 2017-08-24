package com.michal.nowicki.issk;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Szczur on 15.08.2017. Used by ISSK.
 * This Fragment displays adding timetable page.
 */

public class TimetableAddFragment extends Fragment {

    public TimetableAddFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetableadd, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        WebView webV;

        if(getView() != null){
            if(getView().findViewById(R.id.webview) != null){
                webV = getView().findViewById(R.id.webview);
                webV.setVisibility(View.INVISIBLE);
                webV.loadUrl("https://drive.google.com/drive/folders/0B5wPhEU3v_0IQzN6Tlh6VFhXZ2M?usp=sharing");
            }
        }
    }
}
