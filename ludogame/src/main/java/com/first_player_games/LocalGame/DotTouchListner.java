package com.first_player_games.LocalGame;

import android.view.MotionEvent;
import android.view.View;

public class DotTouchListner implements View.OnTouchListener {

    Player[] player;
    float px,py;
    float cw;
    boolean[] allowedplayers;
    Gamestate state;
    int diff = 0;

    public DotTouchListner(Player[] player,boolean[] allowedplayers,Gamestate state){
        this.player = player;
        this.allowedplayers = allowedplayers;
        this.state = state;
        int width =  player[0].position.width;
        diff = (int) ((float) width*0.1);
        diff = diff/2;
        cw = player[0].position.width/10;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                px = motionEvent.getX();
                py = motionEvent.getY();
                return true;
            case MotionEvent.ACTION_UP:
                search(motionEvent.getX(),motionEvent.getY());
                px = -1;
                py = -1;
                return true;
        }
        return false;
    }



    public void search(float x,float y){
                for(int j=0;j<4;j++)
                    if(check(new int[]{player[state.turn].cp[j][0],player[state.turn].cp[j][1]+diff},x,y))
                    {
                        touchDetected(state.turn, j);
                        return;
                    }
    }

    public boolean check(int[] pos,float cx,float cy){
        return (Math.abs(pos[1] - cy) < cw && Math.abs(pos[1] - py) < cw)
                && (Math.abs(pos[0]-cx) < cw && Math.abs(pos[0]-px) < cw);
    }

    public void touchDetected(int playernumber,int piecenumber){ }




}
