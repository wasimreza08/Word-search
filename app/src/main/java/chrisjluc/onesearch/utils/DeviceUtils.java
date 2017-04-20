package chrisjluc.onesearch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

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
