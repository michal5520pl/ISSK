package com.michal.nowicki.issk;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Szczur on 05.07.2017.
 */

public class MyAccountFragment extends Fragment {

    public MyAccountFragment(){}

    private class InfoItem {
        String fst;
        String sncd;

        InfoItem(String fst, String sncd){
            this.fst = fst;
            this.sncd = sncd;
        }
    }

    private class InfoAdapter extends BaseAdapter {
        Context context;
        ArrayList<InfoItem> arrayList;

        InfoAdapter(Context context, ArrayList<InfoItem> arrayList){
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view;

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.myaccount_list, viewGroup, false);
            }
            else {
                view = convertView;
            }

            TextView fstView = view.findViewById(R.id.textView_fst);
            TextView sncdView = view.findViewById(R.id.textView_sncd);

            fstView.setWidth(viewGroup.getWidth() / 2);
            sncdView.setWidth(viewGroup.getWidth() / 2);
            fstView.setPadding(0, 8, 0, 8);
            sncdView.setPadding(0, 8, 0, 8);

            fstView.setTextSize(17);
            sncdView.setTextSize(17);

            fstView.setText(arrayList.get(i).fst);
            sncdView.setText(arrayList.get(i).sncd);

            return view;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myaccount, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        ListView listView = view.findViewById(R.id.myaccount_list);
        ArrayList<InfoItem> infoItems = new ArrayList<>();
        String[] info_array = MainActivity.getPermsString().split(",");
        InfoAdapter infoAdapter;

        for(int i = 0; i < BasicMethods.INFOKEYS.length; i++){
            if(info_array[i].matches("\\d+") && (i > 1))
                continue;

            if(BasicMethods.INFOKEYS[i].contains("Możliwość rezygnacji ze służby")){
                for(int j = 0; j < info_array.length; j++){
                    if(info_array[j].contains("kal_resign")) {
                        infoItems.add(new InfoItem(BasicMethods.INFOKEYS[i], "Tak"));
                        break;
                    }

                    if(j == info_array.length - 1)
                        infoItems.add(new InfoItem(BasicMethods.INFOKEYS[i], "Nie"));
                }
                continue;
            }

            if(BasicMethods.INFOKEYS[i].contains("Przewodnik")){
                for(int j = 0; j < info_array.length; j++){
                    if(info_array[j].contains("przewodnik")){
                        infoItems.add(new InfoItem(BasicMethods.INFOKEYS[i], "Tak"));
                        break;
                    }

                    if(j == info_array.length - 1)
                        infoItems.add(new InfoItem(BasicMethods.INFOKEYS[i], "Nie"));
                }
                continue;
            }

            infoItems.add(new InfoItem(BasicMethods.INFOKEYS[i], info_array[i].trim()));
        }

        infoAdapter = new InfoAdapter(getContext(), infoItems);

        listView.setAdapter(infoAdapter);
    }
}
