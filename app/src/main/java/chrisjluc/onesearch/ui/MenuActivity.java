package chrisjluc.onesearch.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.animation.BounceTouch;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.models.GameMode;
import chrisjluc.onesearch.models.GameType;
import chrisjluc.onesearch.ui.gameplay.WordSearchActivity;

public class MenuActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {

    private final static String MENU_PREF_NAME = "menu_prefs";
    private final static String FIRST_TIME = "first_time";

    private final static long ROUND_TIME_IN_MS = 60000;
    private BounceTouch bounceTouch;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = R.string.ga_menu_screen;
        // Check if first time opening app, show splash screen
        SharedPreferences prefs = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean(FIRST_TIME, true);
        if (isFirstTime) {
            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.activity_menu);
        bounceTouch = new BounceTouch(this);
        findViewById(R.id.settings).setOnTouchListener(bounceTouch);
        findViewById(R.id.bMenuEasy).setOnTouchListener(bounceTouch);
        findViewById(R.id.bMenuMedium).setOnClickListener(this);
        findViewById(R.id.bMenuHard).setOnClickListener(this);
        //TODO: Reimplement advanced after more efficient way of drawing out the grid
//        findViewById(R.id.bMenuAdvanced).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bMenuSignIn) {
            mInSignInFlow = true;
            mSignInClicked = true;
            mGoogleApiClient.connect();
            return;
        }
        String gd = null;
        int ga_button_id = -1;
        switch (view.getId()) {
            case R.id.settings:
                return;
            case R.id.bMenuEasy:
                gd = GameDifficulty.Easy;
                ga_button_id = R.string.ga_click_easy;
                break;
            case R.id.bMenuMedium:
                gd = GameDifficulty.Medium;
                ga_button_id = R.string.ga_click_medium;
                break;
            case R.id.bMenuHard:
                gd = GameDifficulty.Hard;
                ga_button_id = R.string.ga_click_hard;
                break;
//            case R.id.bMenuAdvanced:
//                gd = GameDifficulty.Advanced;
//                break;
        }
        analyticsTrackEvent(ga_button_id);
        WordSearchManager wsm = WordSearchManager.getInstance();
        wsm.Initialize(new GameMode(GameType.Timed, gd, ROUND_TIME_IN_MS), getApplicationContext());
        wsm.buildWordSearches();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), WordSearchActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left );
            }
        }, 500);

    }

    @Override
    protected void onResume() {
        super.onResume();
        analyticsTrackScreen(getString(categoryId));
        WordSearchManager.nullify();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        findViewById(R.id.bMenuSignIn).setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        findViewById(R.id.bMenuSignIn).setVisibility(View.VISIBLE);
        findViewById(R.id.bMenuSignIn).setOnClickListener(this);
    }
}
