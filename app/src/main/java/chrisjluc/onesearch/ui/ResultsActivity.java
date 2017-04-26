package chrisjluc.onesearch.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.List;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.ads.GoogleAds;
import chrisjluc.onesearch.animation.BounceTouch;
import chrisjluc.onesearch.base.BaseGooglePlayServicesActivity;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.models.GameAchievement;
import chrisjluc.onesearch.models.GameDifficulty;
import chrisjluc.onesearch.models.GameMode;
import chrisjluc.onesearch.sound.AudioPlayer;
import chrisjluc.onesearch.sound.util.AudioManagerUtils;
import chrisjluc.onesearch.ui.gameplay.WordSearchActivity;
import chrisjluc.onesearch.utils.DeviceUtils;

public class ResultsActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {

    public final static int REQUEST_LEADERBOARD = 2;
    public final static int REQUEST_ACHIEVEMENTS = 3;

    // Shared pref constants
    public final static String PREF_NAME = "results_and_game_metrics";
    public final static String SCORE_PREFIX = "score_in_mode_";
    public final static String COMPLETED_ROUND_PREFIX = "completed_rounds_in_mode_";
    public final static String HIGHEST_SCORE_FOR_ACHIEVEMENT_PREFIX = "highest_score_for_achievement_in_mode_";

    private String mLeaderboardId;
    private int mScore = -1;
    private int mPreviousBestScore = -1;
    private int mSkipped = -1;
    private static final int BEGINNER = 10;
    private static final int INTERMEDIATE = 25;
    private static final int EXPERIENCED = 50;
    private static final int SPECIALIST = 100;
    private static final int EXPERT = 200;
    private AdView mAdView;
    private GameMode mGameMode;
    private Handler mHandler = new Handler();
    private BounceTouch mBounceTouch;
    private RelativeLayout container;
    //private final List<ConfettiManager> activeConfettiManagers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = R.string.ga_results_screen;
        setContentView(R.layout.activity_results);
        mBounceTouch = new BounceTouch(this);
        findViewById(R.id.bReplay).setOnTouchListener(mBounceTouch);
        findViewById(R.id.bReturnMenu).setOnTouchListener(mBounceTouch);
        mAdView = (AdView) findViewById(R.id.ad_view);
        container = (RelativeLayout) findViewById(R.id.container);
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.

        mAdView.loadAd(adRequest);
        GoogleAds.getGoogleAds(this).requestNewInterstitial();
        Bundle extras = getIntent().getExtras();
        //activeConfettiManagers.add(generateRain());
        if (extras != null) {
            mScore = extras.getInt("score");
            mSkipped = extras.getInt("skipped");

            mGameMode = WordSearchManager.getInstance().getGameMode();
            if (mGameMode != null) {
                switch (mGameMode.getDifficulty()) {
                    case GameDifficulty.Easy:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__easy);
                        break;
                    case GameDifficulty.Medium:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__medium);
                        break;
                    case GameDifficulty.Hard:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__hard);
                        break;
                    /*case GameDifficulty.Advanced:
                        mLeaderboardId = getString(R.string.leaderboard_highest_scores__advanced);
                        break;*/
                }

                // Track number of played rounds
                SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                int numRounds = prefs.getInt(COMPLETED_ROUND_PREFIX + mGameMode.getDifficulty(), 0);
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putInt(COMPLETED_ROUND_PREFIX + mGameMode.getDifficulty(), ++numRounds);
                editor.apply();
            }

            updateSavedScoreAndRenderViews();
        }


    }

    /*private ConfettiManager  generateRain(){
        final int[] colors = {R.color.blue, R.color.red_light, R.color.light_green};
        return CommonConfetti.rainingConfetti(container, colors)
                .stream(3000);
    }
*/
    private void updateSavedScoreAndRenderViews() {

        TextView scoreTextView = (TextView) findViewById(R.id.tvScoreResult);
        scoreTextView.setText(Integer.toString(mScore));
        final boolean isSoundOn = DeviceUtils.getSound(this);
        if (mGameMode != null) {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            int bestScore = prefs.getInt(SCORE_PREFIX + mGameMode.getDifficulty(), 0);
            mPreviousBestScore = bestScore;
            if (mScore > bestScore) {
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putInt(SCORE_PREFIX + mGameMode.getDifficulty(), mScore);
                editor.apply();

                findViewById(R.id.tvBestScoreResultNotify).setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(500);
                anim.setStartOffset(0);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(8);
                findViewById(R.id.tvBestScoreResultNotify).startAnimation(anim);
                ((TextView) findViewById(R.id.tvBestScoreResult)).setText(Integer.toString(mScore));
                if(isSoundOn) {
                    AudioPlayer.getInstance().play(this, R.raw.kids_scream);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            AudioManagerUtils.getInstance().setSound(ResultsActivity.this, null, R.raw.result_sound, false, 100);

                        }
                    }, 2000);
                }
            } else {
                if(isSoundOn) {
                    AudioManagerUtils.getInstance().setSound(this, null, R.raw.result_sound, false, 100);
                }
                ((TextView) findViewById(R.id.tvBestScoreResult)).setText(Integer.toString(bestScore));
            }
        }
    }

    private void updateLeaderboard() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, mLeaderboardId, mScore);
        }
    }

    private void updateAchievements(){
        int achievement = DeviceUtils.getAchievement(this);
        achievement++;
        DeviceUtils.setAchievement(this, achievement);
        if(achievement <= BEGINNER){
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.beginner_achievement),1);
        } else if(achievement <= INTERMEDIATE){
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.intermediate_achievement),1);
        } else if(achievement <= EXPERIENCED){
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.experienced_achievement),1);
        } else if(achievement <= SPECIALIST){
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.specialist_achievement),1);
        } else if(achievement <= EXPERT){
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.expert_achievement),1);
        }
    }

  /*  private void updateAchievements() {
        int score = Math.max(mPreviousBestScore, mScore);
        SparseArray<String> achievementsMap = null;
        switch (mGameMode.getDifficulty()) {
            case GameDifficulty.Easy:
                achievementsMap = GameAchievement.EASYACHIEVEMENTSMAP;
                break;
            case GameDifficulty.Medium:
                achievementsMap = GameAchievement.MEDIUMACHIEVEMENTSMAP;
                break;
            case GameDifficulty.Hard:
                achievementsMap = GameAchievement.HARDACHIEVEMENTSMAP;
                break;
            case GameDifficulty.Advanced:
                achievementsMap = GameAchievement.ADVANCEDACHIEVEMENTSMAP;
                break;
        }
        if (achievementsMap == null) return;
        // Remember the highest score associated with achievement unlocked, so we don't unlock it multiple times
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int highestScore = prefs.getInt(HIGHEST_SCORE_FOR_ACHIEVEMENT_PREFIX + mGameMode.getDifficulty(), 0);

        for (int i = 0; i < achievementsMap.size(); i++) {
            int key = achievementsMap.keyAt(i);
            if (key > score) break;
            if (key <= highestScore) continue;
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                Games.Achievements.unlock(mGoogleApiClient, achievementsMap.get(key, ""));
        }

        if (score > highestScore) {
            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            editor.putInt(HIGHEST_SCORE_FOR_ACHIEVEMENT_PREFIX + mGameMode.getDifficulty(), score);
            editor.apply();
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bResultSignIn:
                mInSignInFlow = true;
                mSignInClicked = true;
                mGoogleApiClient.connect();
                return;
            case R.id.bShowLeaderBoards:
                //analyticsTrackEvent(R.string.ga_click_leaderboard);
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLeaderboardId != null) {
                    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                            mLeaderboardId), REQUEST_LEADERBOARD);
                }
                return;
            case R.id.bShowAchievements:
                //analyticsTrackEvent(R.string.ga_click_achievement);
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
                }
                return;

        }
        switch (view.getId()) {
            case R.id.bReplay:
                //analyticsTrackEvent(R.string.ga_click_replay);
                Intent intent = new Intent(getApplicationContext(), WordSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.bReturnMenu:
                GoogleAds.getGoogleAds(this).showInterstitial();
               // analyticsTrackEvent(R.string.ga_click_return_to_menu);
                break;
        }
        finish();
    }

    @Override
    protected void onStart() {
        // Prevent from trying to force connection if they haven't signed in here
        mAutoStartSignInflow = false;
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        //analyticsTrackScreen(getString(categoryId));
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (mGameMode != null) {
            findViewById(R.id.bResultSignIn).setVisibility(View.GONE);
            findViewById(R.id.bShowLeaderBoards).setVisibility(View.VISIBLE);
            findViewById(R.id.bShowLeaderBoards).setOnTouchListener(mBounceTouch);
            findViewById(R.id.bShowAchievements).setVisibility(View.VISIBLE);
            findViewById(R.id.bShowAchievements).setOnTouchListener(mBounceTouch);
            updateLeaderboard();
            updateAchievements();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        findViewById(R.id.bResultSignIn).setVisibility(View.VISIBLE);
        findViewById(R.id.bShowLeaderBoards).setVisibility(View.GONE);
        findViewById(R.id.bShowAchievements).setVisibility(View.GONE);
        findViewById(R.id.bResultSignIn).setOnTouchListener(mBounceTouch);
    }
}
