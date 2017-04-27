package chrisjluc.onesearch.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import chrisjluc.onesearch.LockScreenActivity;
import chrisjluc.onesearch.ads.GoogleAds;
import chrisjluc.onesearch.receiver.ScreenReceiver;
import chrisjluc.onesearch.utils.CommonUtil;
import chrisjluc.onesearch.utils.DeviceUtils;

/**
 * Created by ASUS on 4/24/2017.
 */

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private ScreenReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new ScreenReceiver();
        IntentFilter i = new IntentFilter();
        i.addAction(Intent.ACTION_SCREEN_ON);
        i.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, i);
        GoogleAds.getGoogleAds(this).requestNewInterstitial();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            isScreenOn = pm.isInteractive();
        } else {
            isScreenOn = pm.isScreenOn();
        }
        if (isScreenOn) {
            startActivity(new Intent(this, LockScreenActivity.class));
        }
        IntentFilter protectorCompleteFilter = new IntentFilter(
                CommonUtil.PROTECTOR_COMPLETE_BROADCAST_KEY);
        registerReceiver(mProtectComplete, protectorCompleteFilter);
       // startForeground(CommonUtil.FOREGROUND_NOTIFICATION_ID, DeviceUtils.createNotification(this));
    }

    @Override
    public void onDestroy() {
        GoogleAds.getGoogleAds(this).showInterstitial();
        unregisterReceiver(mProtectComplete);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mProtectComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopService(new Intent(context, ProtectorLockService.class));
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService(new Intent(this, ProtectorLockService.class));
       return Service.START_STICKY;
    }

}
