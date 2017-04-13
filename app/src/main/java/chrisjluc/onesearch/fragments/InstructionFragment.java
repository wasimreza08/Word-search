package chrisjluc.onesearch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chrisjluc.onesearch.R;

/**
 * Created by bjit-16 on 4/13/17.
 */

public class InstructionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instruction_fragment, container, false);
        return rootView;
    }
}
