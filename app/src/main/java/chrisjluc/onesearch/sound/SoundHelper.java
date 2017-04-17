package chrisjluc.onesearch.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;



public class SoundHelper {

    private Context context;
    private static MediaPlayer sMediaPlayer;

    public SoundHelper(Context context){
        this.context = context;
    }
    public MediaPlayer getsMediaPlayer(){
        if(sMediaPlayer == null){
            sMediaPlayer = new MediaPlayer();
        }

        return  sMediaPlayer;
    }

    public void getSoundFilePath(String filename){
        getsMediaPlayer();
        AssetManager mgr = context.getAssets();
        AssetFileDescriptor fileDescriptors = null;
        try {
      //      Log.d(TAG, "SoundHelper file name " + filename);
            fileDescriptors = mgr.openFd("audio/"+filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //final MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            if(fileDescriptors != null){
                Log.e("media", "reset");
                sMediaPlayer.reset();
                sMediaPlayer.setDataSource(fileDescriptors.getFileDescriptor(), fileDescriptors.getStartOffset(), fileDescriptors.getLength());
                sMediaPlayer.prepare();
               // sMediaPlayer.prepareAsync();
                //sMediaPlayer.setLooping(false);
                sMediaPlayer.start();
               // fileDescriptors.close();
                sMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        //if(mediaPlayer != null){
                            //mediaPlayer.stop();
                            //mediaPlayer.release();
                            Log.e("media", "complete");
                        //}
                           // sMediaPlayer.release();


                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
