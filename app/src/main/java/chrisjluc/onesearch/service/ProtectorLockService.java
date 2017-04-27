package chrisjluc.onesearch.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import chrisjluc.onesearch.utils.CommonUtil;
import chrisjluc.onesearch.utils.DeviceUtils;

/**
 * Created by bjit-16 on 1/4/17.
 */

public class ProtectorLockService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //startForeground(CommonUtil.FOREGROUND_NOTIFICATION_ID, DeviceUtils.createNotification(this));
        sendBroadcast(new Intent(CommonUtil.PROTECTOR_COMPLETE_BROADCAST_KEY));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

}
