package com.metagards.metagames._CoinFlip.cointoss;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.metagards.metagames.R;

import java.util.Random;

public class Cointoss {
    Context context;
    private Random r;
    private int coinSide;
    private int userSelectedcoinSide;
    private MediaPlayer mp;
    private int curSide = R.drawable.heads;
    ImageView coinImage;

    public Cointoss(Context context,ImageView coinImage,int coinSide,int userSelectedcoinSide) {
        this.context = context;
        this.coinImage = coinImage;
        this.coinSide = coinSide;
        this.userSelectedcoinSide = userSelectedcoinSide;
        flipCoin();
    }

    private long animateCoin(boolean stayTheSame) {

        Rotate3dAnimation animation;

        if (curSide == R.drawable.heads) {

            animation = new Rotate3dAnimation(coinImage, R.drawable.heads, R.drawable.tails, 0, 180, 0, 0, 0, 0);
            //Toast.makeText(context, "Head wins!", Toast.LENGTH_SHORT).show();
        } else {

            animation = new Rotate3dAnimation(coinImage, R.drawable.tails, R.drawable.heads, 0, 180, 0, 0, 0, 0);
           // Toast.makeText(context, "Tail wins!", Toast.LENGTH_SHORT).show();
        }
        if (stayTheSame) {
            animation.setRepeatCount(5); // must be odd (5+1 = 6 flips so the side will stay the same)
        } else {
            animation.setRepeatCount(6); // must be even (6+1 = 7 flips so the side will not stay the same)
        }

        animation.setDuration(110);
        animation.setInterpolator(new LinearInterpolator());



        coinImage.startAnimation(animation);


        return animation.getDuration() * (animation.getRepeatCount() + 1);
    }

    public void flipCoin() {

        stopPlaying();
        mp = MediaPlayer.create(context, R.raw.coin_flip);
        mp.start();

        if (coinSide == 0) {  // We have Tails

            boolean stayTheSame = (curSide == R.drawable.heads);
            long timeOfAnimation = animateCoin(stayTheSame);
            curSide = R.drawable.heads;
            Log.d("head_win_","head_win_");
            Toast.makeText(context, "Head wins!", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (userSelectedcoinSide != coinSide) {  // User guessed Heads (WRONG)


                    } else {  // User guessed Tails (CORRECT)


                    }

                }


            }, timeOfAnimation + 100);


        } else {  // We have Heads

            boolean stayTheSame = (curSide == R.drawable.tails);
            long timeOfAnimation = animateCoin(stayTheSame);
            curSide = R.drawable.tails;

            Log.d("Tail_win_","Tail_win_");
            Toast.makeText(context, "Tail wins!", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (userSelectedcoinSide != coinSide) {  // User guessed Tails (WRONG)



                    } else {  // User guessed Heads (CORRECT)


                    }


                }

            }, timeOfAnimation + 100);

        }

    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }


}
