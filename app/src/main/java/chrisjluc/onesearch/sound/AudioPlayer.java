package chrisjluc.onesearch.sound;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by bjit-16 on 4/18/17.
 */

public class AudioPlayer {
    private MediaPlayer
            mMediaPlayer;
    private MediaPlayer mBackgroundMusic;
    private static AudioPlayer sAudioPlayer;
    private int length;

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

    public void stopBackgroundMusic() {
        if (mBackgroundMusic != null) {
            mBackgroundMusic.release();
            mBackgroundMusic = null;
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

    public void playBackgroundMusic(Context c, int rid, boolean loop){
        stopBackgroundMusic();
        mBackgroundMusic = MediaPlayer.create(c, rid);
        mBackgroundMusic.setLooping(loop);
        mBackgroundMusic.setVolume(50, 50);
        mBackgroundMusic.start();
    }
    public void musicPause(){
        if(mBackgroundMusic != null){
            mBackgroundMusic.pause();
            length = mBackgroundMusic.getCurrentPosition();
        }
    }
    public void musicResume(){
        if(mBackgroundMusic != null){
            mBackgroundMusic.seekTo(length);
            mBackgroundMusic.start();
        }
    }
}
