package com.michal.nowicki.issk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Szczur on 05.07.2017.
 */

public class MyAccountFragment extends Fragment {

    public MyAccountFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myaccount, container, false);
    }
}
