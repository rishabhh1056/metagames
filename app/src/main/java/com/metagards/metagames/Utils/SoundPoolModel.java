package com.metagards.metagames.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;;
import java.util.HashMap;

public class SoundPoolModel {

    Activity context;
    SoundPool mSoundPool;
    int sound_id;
    public static final int SOUND_1 = 1;
    public static final int SOUND_2 = 2;
    HashMap<Integer, Integer> mSoundMap;
    public SoundPoolModel(Activity context) {
        this.context = context;
        Initiazalition();
    }

    public void setSounds(int SOUND_TYPE,int rawsound){
        if(mSoundPool != null){
            mSoundMap.put(SOUND_TYPE, mSoundPool.load(context, rawsound, 1));
        }
    }

    public void Initiazalition() {

        mSoundMap = new HashMap<>();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mSoundPool = createNewSoundPool();
//        } else {
            mSoundPool =  createOldSoundPool();
//        }

    }

    /*
     *Call this function from code with the sound you want e.g. playSound(SOUND_1);
     */
    public void playSound(int sound) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "0");

        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if(mSoundPool != null && value.equals("1")){
            mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, 0, 1.0f);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public android.media.SoundPool createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        SoundPool sounds = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();

        return sounds;
    }

    public android.media.SoundPool createOldSoundPool(){
        SoundPool sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        return sounds;
    }

}
