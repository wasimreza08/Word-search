package chrisjluc.onesearch.sound;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by bjit-16 on 4/18/17.
 */

public class AudioPlayer {
    private MediaPlayer
            mMediaPlayer;
    private static AudioPlayer sAudioPlayer;

    public static AudioPlayer getInstance(){
        if(sAudioPlayer == null){
            sAudioPlayer = new AudioPlayer();
        }
        return sAudioPlayer;
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(Context c, int rid) {
        stop();

        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mMediaPlayer.start();
    }
}
