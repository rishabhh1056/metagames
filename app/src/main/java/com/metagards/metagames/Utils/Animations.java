package com.metagards.metagames.Utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.metagards.metagames.Interface.Callback;

public class Animations {
    private View animationView;
    public TranslateAnimation fromAtoB(float fromX, float fromY, float toX, float toY, Animation.AnimationListener l, int speed, final Callback callback){


        TranslateAnimation animation = null;
        animation = new TranslateAnimation(fromX, toX,
                fromY, toY);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setRepeatMode(0);
        animation.setDuration(speed);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.Responce("","",null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        return animation;
    }

    public void setAnimationView(View animationView) {
        this.animationView = animationView;
    }
}
