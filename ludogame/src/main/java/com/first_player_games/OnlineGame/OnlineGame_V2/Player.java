package com.first_player_games.OnlineGame.OnlineGame_V2;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;

import com.first_player_games.R;

import java.util.ArrayList;
import java.util.List;

public class Player {

    int animduration = 200;
    int playernumber;
    Positions position;
    PositionHandler handler;

    int[] p;
    int[][] cp,dp,fp;
    boolean[] drawn,moving;

    Paint paint;
    int colorResId;
    Context context;

    public Player(PositionHandler handler, int playernumber, Context context){
        this.handler = handler;
        this.context = context;
        this.playernumber = playernumber;
        position = handler.positions[playernumber];
        p = new int[4];
        cp = new int[4][4];
        dp = new int[4][4];
        drawn = new boolean[4];
        moving = new boolean[4];
        cp = position.getInitialpos();
        fp = position.getInitialpos();
        for(int i=0;i<4;i++) {
            p[i] = 0;
            dp[i] = handler.getPos(position.pos[1]);
        }
        setColors();
    }


    public void setColors(){

        switch (playernumber){
            case 0: colorResId = R.color.red1;break;
            case 1: colorResId = R.color.yellow1;break;
            case 2: colorResId = R.color.blue1;break;
            case 3: colorResId = R.color.gree1;break;
        }

        paint = new Paint();
        paint.setColor(context.getResources().getColor(colorResId));
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
    }

    public void move(int piece,int steps){
        if(!moving[piece])
            if(p[piece] == 0 && steps == 6)
                animateMovement(piece,1,false,false);
            else if(p[piece] > 0 ) {
                if(p[piece]+steps<57)
                    animateMovement(piece, steps,false,false);
                else if(p[piece]+steps==57)
                    animateMovement(piece, steps,true,false);
            }

    }

    public void move(int piece,int steps,boolean delayed){
        if(!moving[piece])
            if(p[piece] == 0 && steps == 6)
                animateMovement(piece,1,false,delayed);
            else if(p[piece] > 0 ) {
                if(p[piece]+steps<57)
                    animateMovement(piece, steps,false,delayed);
                else if(p[piece]+steps==57)
                    animateMovement(piece, steps,true,delayed);
            }

    }

    public void animateMovement(final int piece, final int steps, final boolean last, final boolean delayed){
        resetTimer();
        moving[piece] = true;
        final int[] i = new int[]{cp[piece][0],cp[piece][1]};
        ValueAnimator a1 = ValueAnimator.ofFloat(0f,1f).setDuration(animduration);
        if (delayed)
            a1.setStartDelay(800);
        a1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cp[piece] = getSectionPoints(i,dp[piece],(float)valueAnimator.getAnimatedValue());
            }
        });
        a1.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) { }
            @Override public void onAnimationCancel(Animator animator) { }
            @Override public void onAnimationRepeat(Animator animator) { }
            @Override public void onAnimationEnd(Animator animator) {
                if(p[piece]<position.pos.length) {
                    p[piece]++;
                    cp[piece] = handler.getPos(position.pos[p[piece]]);
                    if (p[piece] + 1 < position.pos.length)
                        dp[piece] = handler.getPos(position.pos[p[piece] + 1]);
                }
                if(steps>1)
                    animateMovement(piece,steps -1,last,false);
                else {
                    moving[piece]  = false;
                    movementFinished(last);
                }
             }
        });
        a1.start();
    }

    public int position(int piece){
        return position.pos[p[piece]];
    }

    public int[] getSectionPoints(int[] i, int[] d,float m){
        return new int[]{(int) (d[0]*m + i[0]*(1-m)), (int) (d[1]*m + i[1]*(1-m)),};
    }

    public int getSameCount(int piece){
        int a=0;
        for(int i=0;i<4;i++)
            if(p[i] == p[piece] && p[piece]>0) a++;
        return a;
    }

    public boolean isInitial(int piece){
        return p[piece] == 0;
    }

    public boolean isSingle(int piece){
        return getSameCount(piece)==1;
    }
    public boolean isMultiple(int piece){
        return getSameCount(piece)>1  && !isMoving(piece);
    }
    public boolean isMoving(int piece){
        return moving[piece];
    }
    public boolean somethingisMoving(int piece){
        int a = p[piece];
        for(int i=0;i<4;i++)
            if(p[i] == p[piece] && isMoving(i))
                return true;
        return false;
    }

    public void setDrawn(int piece){
        for(int i=0;i<4;i++)
            if(p[i] == p[piece]) drawn[i] = true;
    }

    public void clearDrawn(){
        for(int i=0;i<4;i++)
            drawn[i] = false;
    }

    public boolean isDrawn(int piece){
        return drawn[piece];
    }

    public void returnToZero(int piece){
        p[piece] = 0;
        cp[piece][0] = fp[piece][0];
        cp[piece][1] = fp[piece][1];
        dp[piece] = handler.getPos(position.pos[1]);
    }


    public boolean isAnyUnlocked(){
        for(int i=0;i<4;i++)
            if(p[i]>0)
                return true;
        return false;
    }

    public boolean checkMovementAvailable(int steps){
        if(steps == 6) return true;
        for(int i=0;i<4;i++)
            if(p[i]>0 && p[i] + steps <=57)
                return true;
        return false;
    }

    public int movableSum(int steps){
        int a=0;
        for(int i=0;i<4;i++)
            if(p[i]>0 && p[i] + steps <= 57) a++;
        return a;
    }

    public boolean hasSingleMovable(int steps){
        return movableSum(steps)==1;
    }
    public int getSingleMovable(int steps){
        for(int i=0;i<4;i++)
            if(p[i]>0 && p[i]+steps <=57) return i;
        return -1;
    }
    public int getCompeteCount(){
        int a=0;
        for( int i=0;i<4;i++)
            if(p[i]>56) a++;
        return a;
    }
    public void movementFinished(boolean next){}

    public boolean isMoveable(int piece,int steps){
        return (p[piece]>0 && p[piece]+steps<=57) || (p[piece] == 0 && steps == 6);
    }

    public boolean allLastOrComplete(int steps){
        int a=0,b = 0;
        for(int i=0;i<4;i++) if(p[i]>56) a++;
        for(int i=0;i<4;i++) if(p[i]<=56 && p[i]+steps>56) b++;
        return a+b==4;
    }

    /*
    public void ifLastAndSix(int steps){
        //if(hasSingleMovable(1))
            //if(getSingleMovable(1) + steps)
    }*/

    public List<Integer> getMoveablePiecesList(int steps){
        List<Integer> a = new ArrayList<>();
        for(int i=0;i<4;i++)
            if(isMoveable(i,steps))
                a.add(i);
        return a;
    }

    public boolean nothingMoveable(int steps){
        return movableSum(steps) == 0;
    }

    public void resetTimer(){}






}
