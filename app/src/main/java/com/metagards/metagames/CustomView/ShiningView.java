package com.metagards.metagames.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.metagards.metagames.R;

public class ShiningView extends androidx.appcompat.widget.AppCompatImageView {
    public ShiningView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public ShiningView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public ShiningView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    void init(AttributeSet attrs, int defStyle){

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ShiningView, defStyle, 0);
        
        boolean disableAnimation = a.getBoolean(R.styleable.ShiningView_disableAnimation,false);
        a.recycle();

//        if(!disableAnimation)
//        {
//            setBackground(Functions.getDrawable(getContext(),R.drawable.bg_shine));
//            shineAnimation(this);
//        }


    }

    private void shineAnimation(final View view) {
        final Animation animationUtils = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_righ);
        animationUtils.setDuration(1500);
        view.startAnimation(animationUtils);

        animationUtils.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(animationUtils);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
