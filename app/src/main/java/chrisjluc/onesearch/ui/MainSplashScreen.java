package chrisjluc.onesearch.ui;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.base.BaseActivity;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.utils.CommonUtil;
import io.codetail.widget.RevealFrameLayout;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by ASUS on 4/30/2017.
 */

public class MainSplashScreen extends BaseGooglePlayServicesActivity {
    //private int centerX, centerY;
    private RelativeLayout mainContainer;
    private final static String MENU_PREF_NAME = "menu_prefs";
    private final static String FIRST_TIME = "first_time";
    private SharedPreferences prefs;
    private Handler mHandler = new Handler();
    private  boolean isFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash);
        //LinearLayout main = (LinearLayout) findViewById(R.id.container);
        mainContainer = (RelativeLayout) findViewById(R.id.main_container);
        final RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        mainContainer.bringToFront();
        prefs = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE);
        isFirstTime = prefs.getBoolean(FIRST_TIME, true);
        mainContainer.postDelayed(new Runnable() {
           @Override
           public void run() {
               back.setVisibility(View.VISIBLE);
              final int cx = (mainContainer.getLeft() + mainContainer.getRight()) / 2;
               final int cy = (mainContainer.getTop() + mainContainer.getBottom()) / 2;

               // get the final radius for the clipping circle
               final int dx = Math.max(cx, mainContainer.getWidth() - cx);
               final  int dy = Math.max(cy, mainContainer.getHeight() - cy);
               final float finalRadius = (float) Math.hypot(dx, dy);

               // Android native animator
               Animator animator =
                       ViewAnimationUtils.createCircularReveal(back, cx, cy, 0, finalRadius);
               animator.setInterpolator(new AccelerateDecelerateInterpolator());
               animator.setDuration(700);
               animator.start();
               mainContainer.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       mainContainer.setVisibility(View.VISIBLE);
                       Animator animator =
                               ViewAnimationUtils.createCircularReveal(mainContainer, cx, cy, 0, finalRadius);
                       animator.setInterpolator(new AccelerateDecelerateInterpolator());
                       animator.setDuration(700);
                       animator.start();
                   }
               },300);
           }
       },250);

    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        setConnectivityStatus(true);
        goForNext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            setConnectivityStatus(true);
        }
        gotoNextScreen();
        super.onActivityResult(requestCode, resultCode, intent);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        setConnectivityStatus(false);
        goForNext();
    }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            gotoNextScreen();
        }
    };
    private void goForNext(){
        if(!isFirstTime){
            mHandler.postDelayed(mRunnable, 2000);
        }else{
            mHandler.postDelayed(mRunnable, 10000);
        }
    }



    private void gotoNextScreen(){
        finish();
        mHandler.removeCallbacks(mRunnable);
        if (isFirstTime) {
            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(i);
        } else{
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
        }

    }

    private void setConnectivityStatus(boolean isConnected){
        SharedPreferences.Editor editor = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(CommonUtil.IS_CONNECTED, isConnected);
        editor.apply();
    }
}
