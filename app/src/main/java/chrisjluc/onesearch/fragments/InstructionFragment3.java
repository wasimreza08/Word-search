package chrisjluc.onesearch.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chrisjluc.onesearch.R;

/**
 * Created by bjit-16 on 4/13/17.
 */

public class InstructionFragment3 extends Fragment {
    private final static String MENU_PREF_NAME = "menu_prefs";
    private final static String FIRST_TIME = "first_time";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.splash_screen, container, false);
        rootView.findViewById(R.id.bReadySplash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MENU_PREF_NAME, getActivity().MODE_PRIVATE).edit();
                editor.putBoolean(FIRST_TIME, false);
                editor.apply();
                getActivity().finish();
            }
        });
        return rootView;
    }
}
