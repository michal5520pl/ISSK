package com.michal.nowicki.issk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.CookieManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static String infoandperms;
    protected static CookieManager cookieManager;

    protected static String getPermsString(){
        return infoandperms;
    }

    protected static CookieManager getCookieManager(){
        return cookieManager = new CookieManager();
    }

    private class NavItem {
        String mTitle;

        NavItem(String mTitle){
            this.mTitle = mTitle;
        }
    }

    private class DrawerListAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<NavItem> mNavItems;
        //Spinner spinner;

        DrawerListAdapter(Context mContext, ArrayList<NavItem> mNavItems){
            this.mContext = mContext;
            this.mNavItems = mNavItems;
            //this.spinner = spinner;
        }

        @Override
        public int getCount(){
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position){
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view;

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                view = inflater.inflate(R.layout.drawer_item, parent, false);
            }
            else {
                view = convertView;
            }

            TextView titleView = view.findViewById(R.id.mTitle);
            titleView.setText(mNavItems.get(position).mTitle);

            return view;
        }
    }

    private void selectItemFromDrawer(int position, View v){

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(mNavItems.get(position).mTitle){
            case "Ogłoszenia": { fragmentManager.beginTransaction().replace(R.id.content_frame, new AnnouncementFragment()).commit(); break; }
            case "Kalendarz": { fragmentManager.beginTransaction().replace(R.id.content_frame, new CalendarFragment()).commit(); break; }
            case "Lista szkoleń": { fragmentManager.beginTransaction().replace(R.id.content_frame, new TrainingListFragment()).commit(); break; }
            case "Rozkłady jazdy": { fragmentManager.beginTransaction().replace(R.id.content_frame, new TimetableFragment()).commit(); break; }
            case "Dodawanie rozkładów": { fragmentManager.beginTransaction().replace(R.id.content_frame, new TimetableAddFragment()).commit(); break; }
            case "Konta użytkowników": { fragmentManager.beginTransaction().replace(R.id.content_frame, new UsersAccountFragment()).commit(); break; }
            case "Grafik": { fragmentManager.beginTransaction().replace(R.id.content_frame, new ScheduleFragment()).commit(); break; }
            case "Regulamin służby": { fragmentManager.beginTransaction().replace(R.id.content_frame, new ScheduleRulesFragment()).commit(); break; }
            case "Moje konto": { fragmentManager.beginTransaction().replace(R.id.content_frame, new MyAccountFragment()).commit(); break; }
            case "Ustawienia": { fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit(); break; }
            case "Wyloguj się": {
                LogoutTask LogoutT = new LogoutTask();
                try {
                    LogoutT.execute();
                    if(LogoutT.get().equals(false)){
                        throw new DataNotReceivedException();
                    }
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                catch (Exception e){
                    Context context = getApplicationContext();
                    CharSequence text = "Coś poszło nie tak";
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                }
            }
            default: {
                Context context = getApplicationContext();
                CharSequence text = "" + position;
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                break;
            }
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private static String TAG = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Intent intent = getIntent();
        infoandperms = intent.getStringExtra(LoginActivity.DATA);

        for (String mChoice : BasicMethods.DRAWERMENUVALUES){
            switch(mChoice){
                case "Kalendarz":
                {
                    if(infoandperms.contains("kal_show")){
                        mNavItems.add(new NavItem(mChoice));
                        break;
                    }
                }
                case "Lista szkoleń":
                {
                    if(infoandperms.contains("szkol_show") || infoandperms.contains("bhp_show")){
                        mNavItems.add(new NavItem(mChoice));
                        break;
                    }
                }
                case "Rozkłady jazdy":
                {
                    if(infoandperms.contains("rozkl_show")){
                        mNavItems.add(new NavItem(mChoice));
                        break;
                    }
                }
                case "Dodawanie rozkładów":
                {
                    if(infoandperms.contains("rozkl_edit")){
                        mNavItems.add(new NavItem(mChoice));
                        break;
                    }
                }
                case "Konta użytkowników":
                {
                    if(infoandperms.contains("user_show")){
                        mNavItems.add(new NavItem(mChoice));
                        break;
                    }
                }
                case "Raporty":
                {
                    if(infoandperms.contains("rep_show")){
                        mNavItems.add(new NavItem(mChoice));
                        break;
                    }
                }
                case "Ogłoszenia":
                case "Grafik":
                case "Regulamin służby":
                case "Informacje dodatkowe":
                case "Moje konto":
                case "Ustawienia":
                case "Wyloguj się":
                {
                    mNavItems.add(new NavItem(mChoice));
                    break;
                }
            }
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerPane = findViewById(R.id.drawerPane);
        mDrawerList = findViewById(R.id.navList);
        //Spinner spinner = new Spinner(getApplicationContext());

        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                selectItemFromDrawer(position, view);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AnnouncementFragment()).commit();

        //SQLiteDatabase localDB = SQLiteDatabase.openOrCreateDatabase(getApplication().getFilesDir().toString() + "local_data", null);
        //if();
        //localDB.close();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume(){
        super.onResume();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        getSupportFragmentManager().beginTransaction().detach(fragment).commit();
        getSupportFragmentManager().beginTransaction().attach(fragment).commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        LogoutTask LogoutT = new LogoutTask();
        try {
            LogoutT.execute();
            if(LogoutT.get().equals(false)){
                throw new DataNotReceivedException();
            }
        }
        catch (Exception e){
            Context context = getApplicationContext();
            CharSequence text = "Coś poszło nie tak";
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }
}
