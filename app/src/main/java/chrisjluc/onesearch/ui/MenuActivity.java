package chrisjluc.onesearch.ui;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.ads.GoogleAds;
import chrisjluc.onesearch.animation.BounceTouch;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.models.GameMode;
import chrisjluc.onesearch.models.GameType;
import chrisjluc.onesearch.service.MyService;
import chrisjluc.onesearch.sound.util.AudioManagerUtils;
import chrisjluc.onesearch.ui.gameplay.WordSearchActivity;

public class MenuActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {

    private final static String MENU_PREF_NAME = "menu_prefs";
    private final static String FIRST_TIME = "first_time";

    private final static long ROUND_TIME_IN_MS = 60000;
    private BounceTouch bounceTouch;
    private ImageButton soundBtn, rateButton;
    private AdView mAdView;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = R.string.ga_menu_screen;
        // Check if first time opening app, show splash screen
        SharedPreferences prefs = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean(FIRST_TIME, true);
        if (isFirstTime) {
            finish();
            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(i);
        }
        setContentView(R.layout.activity_menu);
        mAdView = (AdView) findViewById(R.id.ad_view);
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                // Save app state before going to the ad overlay.
            }
        });
        GoogleAds.getGoogleAds(this).requestNewInterstitial();
        //startService(new Intent(this, MyService.class));
        bounceTouch = new BounceTouch(this);
        soundBtn = (ImageButton) findViewById(R.id.sound);
        rateButton = (ImageButton) findViewById(R.id.rate);
        rateButton.setOnTouchListener(bounceTouch);
        soundBtn.setOnTouchListener(bounceTouch);

        findViewById(R.id.bMenuEasy).setOnClickListener(this);
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
        long time = ROUND_TIME_IN_MS;
        switch (view.getId()) {
            case R.id.sound:
                AudioManagerUtils.getInstance().soundToggle(this, soundBtn);
                AudioManagerUtils.getInstance().setSound(this, soundBtn, R.raw.menu_background_music, true, 100);
                return;
            case R.id.rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                return;
            case R.id.bMenuEasy:
                gd = GameDifficulty.Easy;
                ga_button_id = R.string.ga_click_easy;
                break;
            case R.id.bMenuMedium:
                gd = GameDifficulty.Medium;
                ga_button_id = R.string.ga_click_medium;
                time = (long) (1.5*time);
                break;
            case R.id.bMenuHard:
                gd = GameDifficulty.Hard;
                ga_button_id = R.string.ga_click_hard;
                time = 2L*time;
                break;
//            case R.id.bMenuAdvanced:
//                gd = GameDifficulty.Advanced;
//                break;
        }
        //analyticsTrackEvent(ga_button_id);
        WordSearchManager wsm = WordSearchManager.getInstance();
        wsm.Initialize(new GameMode(GameType.Timed, gd, time), getApplicationContext());
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
        AudioManagerUtils.getInstance().setSound(this, soundBtn, R.raw.menu_background_music, true, 100);        //analyticsTrackScreen(getString(categoryId));
        WordSearchManager.nullify();
        if (mAdView != null) {
            mAdView.resume();
        }

    }

    @Override
    protected void onPause() {
        AudioManagerUtils.getInstance().stopBackgroundMusic();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
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
