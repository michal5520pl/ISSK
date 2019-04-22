package com.michal.nowicki.issk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Szczur on 05.07.2017.
 */

public class ScheduleRulesFragment extends Fragment {

    public ScheduleRulesFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedulerules, container, false);
    }
}
