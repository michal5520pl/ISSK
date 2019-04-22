package com.michal.nowicki.issk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Szczur on 05.07.2017.
 * Calendar.
 */

public class CalendarFragment extends Fragment {

    public CalendarFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        CalendarView calView = view.findViewById(R.id.calendView);
        final ArrayList<String> receivedArray;
        if((receivedArray = taskExecute()) != null && !(receivedArray.contains("Exception"))){
            final ArrayList<Integer> dayArray = new ArrayList<>();
            final ArrayList<Integer> monthArray = new ArrayList<>();
            final ArrayList<Integer> yearArray = new ArrayList<>();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar temp_cal = Calendar.getInstance();
            try {
                for (int i = 0; i < receivedArray.size(); i++) {
                    temp_cal.setTime(dateFormatter.parse(receivedArray.get(i)));
                    dayArray.add(temp_cal.get(Calendar.DAY_OF_MONTH));
                    monthArray.add(temp_cal.get(Calendar.MONTH));
                    yearArray.add(temp_cal.get(Calendar.YEAR));
                }
            }
            catch(Exception e){
                CharSequence text = "Coś poszło nie tak " + e.getLocalizedMessage();
                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
            }
            calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                    Toast.makeText(getContext(), "Year: " + i + " Month: " + (i1 + 1) + " Day: " + i2, Toast.LENGTH_LONG).show();

                    /*if(dayArray.contains(i2) && monthArray.contains(i1 + 1) && yearArray.contains(i) && receivedArray.contains(i + "-" + (i1 + 1) + "-" + i2)){
                    }*/
                }
            });
        }
        else {
            if(receivedArray != null){
                Toast.makeText(getContext(), "Coś poszło nie tak " + receivedArray.get(0) + " " + receivedArray.get(1), Toast.LENGTH_LONG).show();
            }
        }
    }

    public ArrayList<String> taskExecute(){
        CommonInternetTask CIT = new CommonInternetTask();

        try {
            CIT.execute("callendar/get", false);
            Object[] receivedArr = CIT.get();
            ArrayList<String> retArr = new ArrayList<>(((ArrayList)receivedArr[1]).size());

            if((Boolean) receivedArr[0]){
                throw new DataNotReceivedException((String) receivedArr[1]);
            }

            while(((ArrayList)receivedArr[1]).iterator().hasNext()){
                retArr.add((String) ((ArrayList)receivedArr[1]).iterator().next());
            }

            return retArr;
        }
        catch(Exception e){
            Context context = getContext();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            ArrayList<String> errorList = new ArrayList<>();
            errorList.add(e.getLocalizedMessage());
            return errorList;
        }
    }
}
