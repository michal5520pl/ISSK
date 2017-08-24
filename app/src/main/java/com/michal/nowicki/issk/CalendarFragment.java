package com.michal.nowicki.issk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Szczur on 05.07.2017.
 * Calendar.
 */

public class CalendarFragment extends Fragment {

    public CalendarFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        if(getView() != null && getView().findViewById(R.id.calendView)!= null){
            final CalendarView calView = getView().findViewById(R.id.calendView);
            ArrayList<String> receivedArray;
            if((receivedArray = taskExecute()) != null && !(receivedArray.contains("Exception"))){
                final Long date = calView.getDate();
                ArrayList<Integer> dayArray = new ArrayList<>();
                ArrayList<Integer> monthArray = new ArrayList<>();
                ArrayList<Integer> yearArray = new ArrayList<>();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar temp_cal = Calendar.getInstance();
                try {
                    for (int i = 0; i < receivedArray.size(); i++) {
                        temp_cal.setTime(dateFormatter.parse(receivedArray.get(i)));
                        dayArray.add(temp_cal.get(Calendar.DAY_OF_MONTH));
                        monthArray.add(temp_cal.get(Calendar.DAY_OF_MONTH));
                        yearArray.add(temp_cal.get(Calendar.DAY_OF_MONTH));
                    }
                }
                catch(Exception e){
                    CharSequence text = "Coś poszło nie tak " + e.getLocalizedMessage();
                    Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                }
                calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                        if(calView.getDate() != date){
                            Long date = calView.getDate();
                            Toast.makeText(getContext(), date.toString() + "   Year: " + i + " Month: " + (i1 + 1) + " Day: " + i2, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                if(receivedArray != null){
                    Toast.makeText(getContext(), "Coś poszło nie tak " + receivedArray.get(0) + " " + receivedArray.get(1), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public ArrayList<String> taskExecute(){
        CalendarFragmentTask CFT = new CalendarFragmentTask();

        try {
            CFT.execute();
            return CFT.get();
        }
        catch(Exception e){
            Context context = getContext();
            CharSequence text = "Coś poszło nie tak " + e.getLocalizedMessage();
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();

            ArrayList<String> errorList = new ArrayList<>();
            errorList.add("Exception");
            errorList.add(e.getLocalizedMessage());
            return errorList;
        }
    }
}
