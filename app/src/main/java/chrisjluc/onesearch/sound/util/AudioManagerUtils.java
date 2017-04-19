package chrisjluc.onesearch.sound.util;

import android.content.Context;
import android.widget.ImageButton;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.sound.AudioPlayer;
import chrisjluc.onesearch.utils.DeviceUtils;

/**
 * Created by bjit-16 on 4/19/17.
 */

public class AudioManagerUtils {

    private static AudioManagerUtils sAudioManagerUtils;

    public static AudioManagerUtils getInstance(){
        if(sAudioManagerUtils == null){
            sAudioManagerUtils = new AudioManagerUtils();
        }
        return sAudioManagerUtils;
    }

    public void setSound(Context context, ImageButton soundBtn, int backgroundSoundId){
        boolean sound = DeviceUtils.getSound(context);
        if(sound){
            soundBtn.setBackgroundResource(R.drawable.volume);
            soundBtn.setTag(R.drawable.volume);
            AudioPlayer.getInstance().playBackgroundMusic(context,backgroundSoundId);
        }else{
            soundBtn.setBackgroundResource(R.drawable.mute);
            soundBtn.setTag(R.drawable.mute);
            AudioPlayer.getInstance().stopBackgroundMusic();
        }
    }

    public void soundToggle(Context context, ImageButton soundBtn){
        if((int)soundBtn.getTag() == R.drawable.volume){
            DeviceUtils.setSound(context, false);
        }else{
            DeviceUtils.setSound(context,true);
        }
    }

    public void stopBackgroundMusic(){
        AudioPlayer.getInstance().stopBackgroundMusic();
    }

}
