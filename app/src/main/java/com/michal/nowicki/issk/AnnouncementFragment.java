package com.michal.nowicki.issk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Szczur on 04.07.2017.
 */

public class AnnouncementFragment extends Fragment {

    public AnnouncementFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcement, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        try {
            SessionCheckTask SC = new SessionCheckTask();

            SC.execute(((MainActivity)getActivity()).getCookie());

            switch(SC.get()){
                case 0:
                {
                    AnnouncementFragmentTask AT = new AnnouncementFragmentTask();
                    AT.execute();
                    break;
                }
                case 1:
                {
                    if(getView() != null){
                        Intent intent = new Intent(getView().getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getView().getContext(), "Nie jesteś zalogowany/a.", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                case 2:
                {
                    if(getView() != null){
                        Toast.makeText(getView().getContext(), "Wystąpił błąd weryfikacji sesji.", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                case 3:
                {
                    if(getView() != null){
                        Intent intent = new Intent(getView().getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getView().getContext(), "Sesja wygasła", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                case 4:
                {
                    if(getView() != null){
                        Toast.makeText(getView().getContext(), "Wystąpił błąd połączenia z bazą danych", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                default:
                {
                    if(getView() != null){
                        Toast.makeText(getView().getContext(), "Wystąpił błąd realizacji wątku SC.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch(Exception e){
            if(getView() != null){
                Toast.makeText(getView().getContext(), "Coś poszło nie tak. " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
