package chrisjluc.onesearch.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.WindowManager;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.models.GameState;


public class DeviceUtils {
    private static Boolean mIsTablet;
    private static Boolean mIsSmallScreen;
    public static boolean isTablet(Context context) {
        if (mIsTablet == null) {
            boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
            boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
            mIsTablet = (xlarge || large);
        }
        return mIsTablet;
    }

    public static boolean isSmallScreen(Context context) {
        if(mIsSmallScreen == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mIsSmallScreen = size.x <= 768;
        }
        return mIsSmallScreen;
    }
  /*  public static Notification createNotification(Context context) {
        Intent nextIntent = new Intent(context, MyService.class);
        // nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(context, 0,
                nextIntent, 0);
        NotificationCompat.Builder notificationI = new NotificationCompat.Builder(context)
                .setContentTitle("")
                .setTicker("")
                .setContentText("")
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(false)
                .setContentIntent(pnextIntent);
        Notification notification = notificationI.build();
        notification.priority = Notification.PRIORITY_MIN;

        return notification;
    }*/
    public static boolean getSound(Context context){
        SharedPreferences prefs = context.getSharedPreferences(GameState.PREF_NAME, context.MODE_PRIVATE);
       return prefs.getBoolean(GameState.SOUND_PREF, true);
    }

    public static void setSound(Context context, boolean sound){
        SharedPreferences.Editor editor = context.getSharedPreferences(GameState.PREF_NAME, context.MODE_PRIVATE).edit();
        editor.putBoolean(GameState.SOUND_PREF, sound);
        editor.apply();
    }

    public static int getAchievement(Context context){
        SharedPreferences prefs = context.getSharedPreferences(GameState.PREF_NAME, context.MODE_PRIVATE);
        return prefs.getInt(GameState.ACHIEVEMENT, 0);
    }

    public static void setAchievement(Context context, int sound){
        SharedPreferences.Editor editor = context.getSharedPreferences(GameState.PREF_NAME, context.MODE_PRIVATE).edit();
        editor.putInt(GameState.ACHIEVEMENT, sound);
        editor.apply();
    }
}
