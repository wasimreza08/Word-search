package chrisjluc.onesearch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import chrisjluc.onesearch.LockScreenActivity;
import chrisjluc.onesearch.ads.GoogleAds;

/**
 * Created by ASUS on 4/24/2017.
 */

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("Check","Screen went OFF");
            GoogleAds.getGoogleAds(context).requestNewInterstitial();
            //  Toast.makeText(context, "screen OFF",Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("Check","Screen went ON");
            context.startActivity(new Intent(context, LockScreenActivity.class));

            // Toast.makeText(context, "screen ON",Toast.LENGTH_LONG).show();
        }
    }
}
