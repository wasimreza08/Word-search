package chrisjluc.onesearch;

import android.os.Bundle;
import android.view.WindowManager;

import chrisjluc.onesearch.ads.GoogleAds;
import chrisjluc.onesearch.base.BaseActivity;

/**
 * Created by ASUS on 4/24/2017.
 */

public class LockScreenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.grid_item);
        GoogleAds.getGoogleAds(this).showInterstitial();
        finish();
    }
}
