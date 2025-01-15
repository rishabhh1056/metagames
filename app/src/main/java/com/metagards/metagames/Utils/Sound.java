package com.metagards.metagames.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class Sound {
    private static MediaPlayer currentlyPlayingSong,
            currentlyPlayingFX;

    public Sound() {}

    public void PlayFX(int fxId, boolean shouldLoop,Context context)
    {
        MediaPlayer fx = MediaPlayer.create(context, fxId);

        if (currentlyPlayingFX != fx)
        {
            StopFX();

            currentlyPlayingFX = fx;

            currentlyPlayingFX.start();

            currentlyPlayingFX.setLooping(shouldLoop);
        }
    }

    public void PlaySong(int songId, boolean shouldLoop, Context context)
    {
        MediaPlayer song = MediaPlayer.create(context, songId);

        if (currentlyPlayingSong != song)
        {
            StopSong();

            currentlyPlayingSong = song;

            currentlyPlayingSong.start();

            currentlyPlayingSong.setLooping(shouldLoop);
        }
    }

    public void PlaySong(Uri songId, boolean shouldLoop, Context context)
    {
        MediaPlayer song = MediaPlayer.create(context, songId);

        if (currentlyPlayingSong != song)
        {
            StopSong();

            currentlyPlayingSong = song;

            currentlyPlayingSong.start();

            currentlyPlayingSong.setLooping(shouldLoop);
        }
    }

    public void StopFX()
    {
        if (currentlyPlayingFX != null)
        {
            currentlyPlayingFX.stop();

            currentlyPlayingFX.release();

            currentlyPlayingFX = null;
        }
    }

    public void StopSong()
    {
        if (currentlyPlayingSong != null)
        {
            currentlyPlayingSong.stop();

            currentlyPlayingSong.release();

            currentlyPlayingSong = null;
        }
    }

    public boolean isSoundPlaying(){

        return currentlyPlayingSong != null ? currentlyPlayingSong.isPlaying() :  false;
    }

    public boolean isSoundPlayingFX(){

        return currentlyPlayingFX != null ? currentlyPlayingFX.isPlaying() :  false;
    }
}
