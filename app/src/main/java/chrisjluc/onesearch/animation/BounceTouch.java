package chrisjluc.onesearch.animation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import chrisjluc.onesearch.R;

/**
 * Created by bjit-16 on 4/18/17.
 */

public class BounceTouch implements View.OnTouchListener {
    private Context mContext;
    public BounceTouch(Context context){
        mContext = context;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.reduce_size);
                reducer.setTarget(view);
                reducer.start();
                break;

            case MotionEvent.ACTION_UP:
                AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(mContext,R.animator.regain_size);
                regainer.setTarget(view);
                BounceInterpolator interpolator = new BounceInterpolator(0.5, 20);
                regainer.setInterpolator(interpolator);
                regainer.start();
                break;
        }
        return true;
    }
}
