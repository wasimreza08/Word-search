package chrisjluc.onesearch.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.animation.BounceTouch;

/**
 * Created by bjit-16 on 4/13/17.
 */

public class InstructionFragment3 extends Fragment{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.splash_screen, container, false);
        rootView.findViewById(R.id.bReadySplash).setOnTouchListener(new BounceTouch(getActivity()));
        return rootView;
    }


}
