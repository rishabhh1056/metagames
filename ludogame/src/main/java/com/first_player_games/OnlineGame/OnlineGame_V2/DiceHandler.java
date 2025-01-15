package com.first_player_games.OnlineGame.OnlineGame_V2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
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
    ImageView dice;
    ImageView[] dices;
    CountDownTimer diceAnimationInitial;
    MediaPlayer rollingEffect;
    boolean isrolling = false;
    public DiceHandler(Context context, ImageView dice, ImageView[] dices, MediaPlayer rollingEffect){
        this.context = context;
        this.dice = dice;
        this.dices = dices;
        random = new Random();
        collectDots();
        this.rollingEffect = rollingEffect;
        diceAnimationInitial = new CountDownTimer(6000,50) {
            @Override
            public void onTick(long l) { getRandomNumber(); }
            @Override public void onFinish() { }
        };
    }

    public int rollDice(final int randomnumber){
        if(!rollingEffect.isPlaying())
            rollingEffect.start();
        isrolling = true;
        try {
            diceAnimationInitial.cancel();
        }catch (Exception e){}
        new CountDownTimer(600, 60) {
            @Override public void onTick(long l) {
                getRandomNumber();
            }
            @Override public void onFinish() {
                steps = randomnumber;
                updateDices(steps-1);
                onDiceResule();
            }
        }.start();
        return randomnumber;
    }

    /*
    public int rollDice(){
        final int randomnumber = getRan();
        new CountDownTimer(600, 60) {
            @Override public void onTick(long l) {
                getRandomNumber();
            }
            @Override public void onFinish() {
                steps = randomnumber;
                dice.setImageDrawable(dots[steps-1]);
                onDiceResule();
            }
        }.start();
        return randomnumber;
    }*/

    public int getRandomNumber(){
        int r = random.nextInt(6);
        updateDices(r);
        return r+1;
    }

    public void updateDices(int r){
        dice.setImageDrawable(dots[r]);
        for(int i=0;i<dices.length;i++)
            dices[i].setImageDrawable(dots[r]);
    }

    public int getRan(){
        return random.nextInt(6)+1;
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
