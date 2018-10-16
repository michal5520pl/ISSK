package com.michal.nowicki.issk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Szczur on 04.07.2017. Used by ISSK.
 * This fragment is used for showing annoucements data.
 */

public class AnnouncementFragment extends Fragment {

    public AnnouncementFragment(){}

    private class NoticeItem {
        String id, title, author_id, author, publish, archive, changed, content;

        NoticeItem(HashMap<String, String> hashMap){
            this.id = hashMap.get("id");
            this.title = hashMap.get("title") + "\n";
            this.author_id = hashMap.get("author_id");
            this.author = hashMap.get("author");
            this.publish = hashMap.get("publish");
            this.content = hashMap.get("content");

            if(MainActivity.getPermsString().contains("ogl_edit")){
                this.archive = hashMap.get("archive");
                this.changed = hashMap.get("changed");
            }
        }
    }

    private class ListNoticeAdapter extends BaseAdapter {
        Context context;
        ArrayList<NoticeItem> noticeArray;

        ListNoticeAdapter(Context context, ArrayList<NoticeItem> noticeArray){
            this.context = context;
            this.noticeArray = noticeArray;
        }

        @Override
        public int getCount() {
            return noticeArray.size();
        }

        @Override
        public Object getItem(int position) {
            return noticeArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            View view;

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                view = inflater.inflate(R.layout.annoucement_tab, viewGroup, false);
            }
            else {
                view = convertView;
            }

            TextView titleView = view.findViewById(R.id.title);
            TextView authorView = view.findViewById(R.id.author);
            TextView publishView = view.findViewById(R.id.publish);
            TextView archiveView = view.findViewById(R.id.archive);
            TextView changedView = view.findViewById(R.id.changed);
            TextView contentView = view.findViewById(R.id.content);

            titleView.setText(noticeArray.get(position).title);
            authorView.setText(noticeArray.get(position).author);
            publishView.setText(noticeArray.get(position).publish);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                contentView.setText(Html.fromHtml(noticeArray.get(position).content, 16));
            else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                //noinspection deprecation
                contentView.setText(Html.fromHtml(noticeArray.get(position).content));

            if(MainActivity.getPermsString().contains("ogl_edit") && noticeArray.get(position).archive != null){
                String archive = "Archiwizacja: " + noticeArray.get(position).archive;
                archiveView.setText(archive);

                if(!(noticeArray.get(position).changed.contains("null"))){
                    String changed = "Edytowane: " + noticeArray.get(position).changed;
                    changedView.setText(changed);
                }
                else
                    changedView.setVisibility(View.GONE);
            }
            else if(! MainActivity.getPermsString().contains("ogl_edit")){
                archiveView.setVisibility(View.GONE);
                changedView.setVisibility(View.GONE);
            }
            else
                Toast.makeText(context, "Archive == null", Toast.LENGTH_LONG).show();

            if(MainActivity.getPermsString().contains("ogl_edit")) {
                if(noticeArray.get(position).changed.equals("01 stycznia 1970 r.") || noticeArray.get(position).changed.contains("null"))
                    changedView.setVisibility(View.GONE);
            }

            Button delete_bttn = view.findViewById(R.id.delete_notice_bttn);
            Button archive_bttn = view.findViewById(R.id.archive_notice_bttn);
            Button edit_bttn = view.findViewById(R.id.edit_notice_bttn);

            if(noticeArray.get(position).author.equals(MainActivity.getPermsString().split(",")[0]) || MainActivity.getPermsString().split(",")[0].equals("Dominik Pych")){
                delete_bttn.setVisibility(View.VISIBLE);

                delete_bttn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        NoticeDeleteOrArchiveTask NDoAT = new NoticeDeleteOrArchiveTask();

                        try {
                            NDoAT.execute(Integer.valueOf(noticeArray.get(position).id), 0);
                            String data = NDoAT.get();

                            if(!(data.equals("false")))
                                Toast.makeText(getContext(), "Exception: " + data, Toast.LENGTH_LONG).show();
                            else {
                                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                                assert fragment != null;
                                getActivity().getSupportFragmentManager().beginTransaction().detach(fragment).commit();
                                getActivity().getSupportFragmentManager().beginTransaction().attach(fragment).commit();
                            }
                        }
                        catch(InterruptedException | ExecutionException e){
                            Toast.makeText(getContext(), "Exception: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else
                delete_bttn.setVisibility(View.INVISIBLE);

            if(MainActivity.getPermsString().contains("ogl_edit")){
                archive_bttn.setVisibility(View.VISIBLE);
                edit_bttn.setVisibility(View.VISIBLE);

                archive_bttn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        NoticeDeleteOrArchiveTask NDoAT = new NoticeDeleteOrArchiveTask();

                        try {
                            NDoAT.execute(Integer.valueOf(noticeArray.get(position).id), 1);
                            String data = NDoAT.get();

                            if(!(data.equals("false")))
                                Toast.makeText(getContext(), "Exception: " + data, Toast.LENGTH_LONG).show();
                            else {
                                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
                                assert fragment != null;
                                getActivity().getSupportFragmentManager().beginTransaction().detach(fragment).commit();
                                getActivity().getSupportFragmentManager().beginTransaction().attach(fragment).commit();
                            }
                        }
                        catch(InterruptedException | ExecutionException e){
                            Toast.makeText(getContext(), "Exception: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                edit_bttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("title", noticeArray.get(position).title);
                            object.put("content", noticeArray.get(position).content);
                            object.put("archive", noticeArray.get(position).archive);
                            object.put("id", noticeArray.get(position).id);

                            Intent intent = new Intent(view.getContext(), NoticeEditActivity.class).putExtra("data", object.toString());
                            startActivity(intent);
                        }
                        catch(JSONException e){
                            Toast.makeText(getContext(), "Exception: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                archive_bttn.setVisibility(View.INVISIBLE);
                edit_bttn.setVisibility(View.INVISIBLE);
            }

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                Date archiveDate = dateFormat.parse(BasicMethods.StringDateToNumberDate(noticeArray.get(position).archive));
                Date currentDate = Calendar.getInstance().getTime();

                if(currentDate.after(archiveDate)){
                    archive_bttn.setOnClickListener(null);
                    archive_bttn.setVisibility(View.INVISIBLE);
                }
            }
            catch(ParseException e){
                Toast.makeText(context, "Exception: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            return view;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcement, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        TabHost tabHost = view.findViewWithTag("tabhost");
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingButton);

        if(MainActivity.getPermsString().contains("ogl_archive")){
            tabHost.setup();

            TabHost.TabSpec tab1 = tabHost.newTabSpec("Aktualne");
            TabHost.TabSpec tab2 = tabHost.newTabSpec("Archiwalne");

            tab1.setIndicator("Aktualne");
            tab1.setContent(R.id.tab1);

            tab2.setIndicator("Archiwalne");
            tab2.setContent(R.id.tab2);

            tabHost.addTab(tab1);
            tabHost.addTab(tab2);
        }
        else {
            tabHost.setVisibility(View.GONE);
        }

        if(MainActivity.getPermsString().contains("ogl_add")){
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NoticeEditActivity.class);
                    startActivity(intent);
                }
            });
        }
        else
            floatingActionButton.setVisibility(View.GONE);

        switch(BasicMethods.checkSession()){
            case 0:
            {
                try {
                    // ---- actual announcements ---- //
                    AnnouncementFragmentTask AFT = new AnnouncementFragmentTask();

                    AFT.execute();
                    ArrayList<HashMap<String, String>> receivedArray = AFT.get();

                    if(receivedArray == null){
                        Toast.makeText(view.getContext(), "FATAL EXCEPTION: AFT.get() == null! Skontaktuj się z twórcą aplikacji.", Toast.LENGTH_LONG).show();
                        break;
                    }

                    if(receivedArray.size() == 0){
                        TextView textView_no_notices = new TextView(this.getContext());
                        textView_no_notices.setText(R.string.no_notices);

                        if(! MainActivity.getPermsString().contains("ogl_archive")){
                            LinearLayout announcementList = getActivity().findViewById(R.id.announcementList);
                            announcementList.removeAllViews();
                            announcementList.addView(textView_no_notices);
                            break;
                        }
                        else {
                            LinearLayout tab1 = view.findViewById(R.id.tab1);
                            tab1.removeAllViews();
                            tab1.addView(textView_no_notices);
                        }
                    }

                    if(receivedArray.size() != 0){
                        if(receivedArray.get(0).containsKey("error"))
                            throw new DataNotReceivedException("Received error from AFT.");

                        ArrayList<NoticeItem> noticeItemArrayList = new ArrayList<>();

                        for(int i = 0; i < receivedArray.size(); i++){
                            noticeItemArrayList.add(new NoticeItem(receivedArray.get(i)));
                        }

                        ListNoticeAdapter actualAdapter = new ListNoticeAdapter(getContext(), noticeItemArrayList);
                        ListView actualListView = view.findViewById(R.id.navlist_actual);

                        actualListView.setAdapter(actualAdapter);
                    }

                    // ---- archive announcements ---- //

                    if(MainActivity.getPermsString().contains("ogl_archive")){
                        ArchiveAnnouncementFragmentTask AAFT = new ArchiveAnnouncementFragmentTask();

                        AAFT.execute();
                        ArrayList<HashMap<String, String>> receivedArchiveArray = AAFT.get();

                        if(receivedArchiveArray == null){
                            Toast.makeText(view.getContext(), R.string.process_exception, Toast.LENGTH_LONG).show();
                            break;
                        }

                        if(receivedArchiveArray.size() == 0){
                            TextView textView_no_archive_notices = new TextView(this.getContext());
                            textView_no_archive_notices.setText(R.string.no_notices);

                            LinearLayout tab2 = view.findViewById(R.id.tab2);
                            tab2.removeAllViews();
                            tab2.addView(textView_no_archive_notices);
                        }

                        if(receivedArchiveArray.size() != 0){
                            if(receivedArchiveArray.get(0).containsKey("error"))
                                throw new DataNotReceivedException("Received error from AAFT.");

                            ArrayList<NoticeItem> archiveNoticeItemArrayList = new ArrayList<>();

                            for(int j= 0; j < receivedArchiveArray.size(); j++){
                                archiveNoticeItemArrayList.add(new NoticeItem(receivedArchiveArray.get(j)));
                            }

                            ListNoticeAdapter archiveAdapter = new ListNoticeAdapter(getContext(), archiveNoticeItemArrayList);
                            ListView archiveListView = view.findViewById(R.id.navlist_archive);

                            archiveListView.setAdapter(archiveAdapter);
                        }
                    }
                }
                catch (InterruptedException | ExecutionException | DataNotReceivedException e){
                    Toast.makeText(view.getContext(), R.string.somethings_not_ok + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                break;
                }
            case 1:
            {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(view.getContext(), R.string.not_logged_in, Toast.LENGTH_LONG).show();
                break;
            }
            case 2:
            {
                Toast.makeText(view.getContext(), R.string.session_verification_failed, Toast.LENGTH_LONG).show();
                break;
            }
            case 3:
            {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(view.getContext(), R.string.session_passed, Toast.LENGTH_LONG).show();
                break;
            }
            case 4:
            {
                Toast.makeText(view.getContext(), R.string.database_error, Toast.LENGTH_LONG).show();
                break;
            }
            default:
            {
                Toast.makeText(view.getContext(), R.string.process_exception + " Moduł SC", Toast.LENGTH_LONG).show();
            }
        }
    }
}
