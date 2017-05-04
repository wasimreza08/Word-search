package chrisjluc.onesearch.ads;

import android.content.Context;
import android.os.CountDownTimer;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import chrisjluc.onesearch.R;

/**
 * Created by ASUS on 2/2/2016.
 */
public class GoogleAds {

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mAd;
    private static final long GAME_LENGTH_MILLISECONDS = 3000;
    private CountDownTimer mCountDownTimer;
    private boolean mGameIsInProgress;
    private long mTimerMilliseconds;
    private static GoogleAds sGoogleAds;

    public static GoogleAds getGoogleAds(Context context){
        if(sGoogleAds == null){
            sGoogleAds = new GoogleAds(context);
        }
        return sGoogleAds;
    }
    public GoogleAds(Context context){
        mInterstitialAd = new InterstitialAd(context);
        //mInterstitialAd.setAdUnitId("ca-app-pub-6308034132891583/1980304352");
        mInterstitialAd.setAdUnitId(context.getString(R.string.inter_ad_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // requestNewInterstitial();
                showInterstitial();
            }
            /*@Override
            public void onAdLoaded(){
                showInterstitial();
            }*/
        });


    }


    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            // Toast.makeText(MainActivity.this, "Ad did not load", Toast.LENGTH_SHORT).show();
            requestNewInterstitial();
        }
    }
    public void requestNewInterstitial() {
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
            //Log.e("ad", "request for ad... done");
        }


        resumeGame(GAME_LENGTH_MILLISECONDS);
    }

    private void resumeGame(long milliseconds) {
        // Create a new timer for the correct length and start it.
        mGameIsInProgress = true;
        mTimerMilliseconds = milliseconds;
        createTimer(milliseconds);
        mCountDownTimer.start();
    }

    private void createTimer(final long milliseconds) {
        // Create the game timer, which counts down to the end of the level
        // and shows the "retry" button.
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        //  final TextView textView = ((TextView) findViewById(R.id.timer));

        mCountDownTimer = new CountDownTimer(milliseconds, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                mTimerMilliseconds = millisUnitFinished;
                // textView.setText("seconds remaining: " + ((millisUnitFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                mGameIsInProgress = false;
                //   textView.setText("done!");
                // mRetryButton.setVisibility(View.VISIBLE);
            }
        };
    }

}
