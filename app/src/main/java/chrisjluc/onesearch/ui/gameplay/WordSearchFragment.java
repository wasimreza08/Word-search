package chrisjluc.onesearch.ui.gameplay;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.utils.DeviceUtils;

public class WordSearchFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private WordSearchGridView mGrid;
    private Handler mHandler = new Handler();
    private TextView tv;

    public static WordSearchFragment newInstance(int sectionNumber) {
        WordSearchFragment fragment = new WordSearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WordSearchFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wordsearch_fragment, container, false);
        tv = (TextView) rootView.findViewById(R.id.section_label);
        mGrid = (WordSearchGridView) rootView.findViewById(R.id.gridView);
        mGrid.setWordFoundListener((WordSearchGridView.WordFoundListener) getActivity());
        tv.setText(mGrid.getWord());
        speakOut();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // speakOut();
            }
        },500);

        return rootView;
    }

    public void speakOut(){
        if(DeviceUtils.getSound(getActivity()) == true  && getUserVisibleHint()){
            TextToSpeech ttObject= ((WordSearchActivity)getActivity()).getTTObject();
            ttObject.speak(tv.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void highlightWord() {
        mGrid.highlightWord();
    }
}
