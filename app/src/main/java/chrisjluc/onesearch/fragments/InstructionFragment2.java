package chrisjluc.onesearch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import chrisjluc.onesearch.R;

/**
 * Created by bjit-16 on 4/13/17.
 */

public class InstructionFragment2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instruction_fragment, container, false);
        ImageView image = (ImageView)rootView.findViewById(R.id.tutorial);
        image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.vertical_tutorial));
        TextView text = (TextView) rootView.findViewById(R.id.t_text);
        text.setText("You can match vertically");
        return rootView;
    }
}
