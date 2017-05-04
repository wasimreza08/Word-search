package chrisjluc.onesearch.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import chrisjluc.onesearch.R;

/**
 * Created by bjit-16 on 4/13/17.
 */

public class InstructionFragment extends Fragment {
    private Handler mHandler = new Handler();
    private ImageView indexFinger;
    private int tutorialRepeat;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(tutorialRepeat < 6){
                Animation animation = new TranslateAnimation(100, 700, 0, 0);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                indexFinger.startAnimation(animation);
                mHandler.postDelayed(runnable, 1200);
                tutorialRepeat++;
            } else{
                mHandler.removeCallbacks(runnable);
            }

        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instruction_fragment, container, false);
        indexFinger = (ImageView) rootView.findViewById(R.id.index);
        indexFinger.setVisibility(View.VISIBLE);
        mHandler.postDelayed(runnable, 1200);
        //indexFinger.setVisibility(0);
        return rootView;
    }
}
