package com.first_player_games.LocalGame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.first_player_games.R;

import java.util.Random;

public class DiceHandler {

    Random random;
    Drawable[] dots;
    int steps = 1;
    boolean rolled = false;
    Context context;
    ImageView[] dices;
    MediaPlayer rollingEffect;
    Animation animBounce;
    public DiceHandler(Context context, ImageView[] dices,MediaPlayer rollingEffect){
        this.context = context;
        this.dices = dices;
        random = new Random();
        collectDots();
        this.rollingEffect = rollingEffect;
        animBounce = AnimationUtils.loadAnimation(context,
                R.anim.bounce);
        animBounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void rollDice(){
        rollingEffect.start();
        new CountDownTimer(600, 60) {
            @Override public void onTick(long l) {
                getRandomNumber();
            }
            @Override public void onFinish() {
                int b = getRandomNumber();
                while (b == steps)
                    b  = getRandomNumber();
                steps = b;
                onDiceResule();
            }
        }.start();
    }

    public int getRandomNumber(){
        int r = random.nextInt(6);
        for(int i=0;i<dices.length;i++) {

            dices[i].setImageDrawable(dots[r]);
        }
        return r+1;
    }

    public void collectDots(){
        dots = new Drawable[6];
        dots[0] = ContextCompat.getDrawable(context, R.drawable.dots_1);
        dots[1] = ContextCompat.getDrawable(context, R.drawable.dots_2);
        dots[2] = ContextCompat.getDrawable(context, R.drawable.dots_3);
        dots[3] = ContextCompat.getDrawable(context, R.drawable.dots_4);
        dots[4] = ContextCompat.getDrawable(context, R.drawable.dots_5);
        dots[5] = ContextCompat.getDrawable(context, R.drawable.dots_6);
    }

    public void onDiceResule(){ }
}
