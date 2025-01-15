package com.first_player_games.LocalGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.first_player_games.R;

import java.util.HashMap;
import java.util.List;

public class DotsView extends View {
    public int width,height,diff;
    int centerwidth,centerheight;
    int dw,cw;

    public int dotRadius , dotSpacing;
    int dr,dr2;

    Paint playerPaint;
    Paint white;
    Paint red;
    Paint strokePaint;

    Paint[] colors;

    PositionHandler positionHandler;
    Gamestate state = null;
    Player[] player;
    boolean[] allowedplayers;

    int smallspacing;
    float smallfactor;



    public DotsView(Context context) {
        super(context);
    }

    public DotsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        white = new Paint();
        white.setColor(Color.WHITE);
        white.setAntiAlias(true);

        playerPaint = new Paint();
        playerPaint.setColor(getResources().getColor(R.color.white));
        playerPaint.setShadowLayer(8.0f, 2.0f, 2.0f, 0x99000000);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(3f);
        strokePaint.setAntiAlias(true);

        width = w;
        height = h;
        diff = h-w;
        diff = diff/2;
        centerheight = h/2;
        centerwidth = w/2;

        dw = w/15;
        cw = dw/2;

        dr = (int) (cw*0.8);
        dr2 = (int) (dr*0.6);

        dotRadius = (int) (dw*0.6);
        dotSpacing = (int) (dw);
        smallspacing = (int) (cw*0.45);
        smallfactor = 0.5f;

        positionHandler = new PositionHandler(w);

        colors = new Paint[4];
        colors[0] = new Paint();
        colors[0].setColor(getResources().getColor(R.color.red9));
        colors[1] = new Paint();
        colors[1].setColor(getResources().getColor(R.color.yellow9));
        colors[2] = new Paint();
        colors[2].setColor(getResources().getColor(R.color.blue9));
        colors[3] = new Paint();
        colors[3].setColor(getResources().getColor(R.color.green9));
    }

    public void setGamestate(Gamestate state){
        this.state = state;
        this.player = state.player;
        this.allowedplayers = state.allowedplayers;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMainDots(canvas);
        if(state != null)
            updateView(canvas);
        invalidate();
    }

    public void updateView(Canvas canvas){
        if(state.overlappingData != null)
            for(HashMap.Entry<Integer,List<int[]>> entry: state.overlappingData.entrySet())
                if(entry.getKey()<57)
                    drawDifferentPlayersLessThan4(canvas,entry.getValue(),entry.getKey());

        for(int i=0;i<4;i++) {
            if(!allowedplayers[i]) continue;
            player[i].clearDrawn();
            for (int j = 0; j < 4; j++)
                if(checkHashmap(player[i].position(j)))
                    if (player[i].isMultiple(j) && !player[i].isDrawn(j) && player[i].p[j]<57) {
                        player[i].setDrawn(j);
                        if(player[i].somethingisMoving(j))
                            drawMultipleSamePLayers(canvas, player[i].cp[j], player[i].getSameCount(j) -1, player[i].paint);
                        else
                            drawMultipleSamePLayers(canvas, player[i].cp[j], player[i].getSameCount(j), player[i].paint);
                    }
        }

        for(int i=0;i<4;i++)
            if(allowedplayers[i])
                for (int j = 0; j < 4; j++)
                    if(player[i].p[j]<57)
                        if (player[i].isSingle(j) || player[i].isInitial(j)|| player[i].isMoving(j))
                            if(checkHashmap(player[i].position(j)) || player[i].moving[j])
                                drawPlayers(canvas, player[i].cp[j], player[i].paint);

        for(int i=0;i<4;i++)
            if(allowedplayers[i])
                drawCompleteDots(canvas,i,player[i].getCompeteCount());

    }

    public boolean checkHashmap(int j){
        if(state.overlappingData != null)
            if(state.overlappingData.size()>0)
                for(HashMap.Entry<Integer,List<int[]>> entry: state.overlappingData.entrySet())
                    if (entry.getKey() == j)
                        return false;
        return true;
    }

    public void drawPlayers(Canvas canvas,int[] pos,Paint paint){
        //canvas.drawCircle(pos[0], pos[1], dr, playerPaint);
        //canvas.drawCircle(pos[0], pos[1], dr2, paint);
        //pos[1] +=diff;
        drawPlayers(canvas,new int[]{pos[0],pos[1]+diff},paint,1);
    }

    public void drawPlayers(Canvas canvas,int[] pos,Paint paint,float factor){
        //canvas.drawCircle(pos[0], pos[1], dr * factor, playerPaint);
        //canvas.drawCircle(pos[0], pos[1], dr2 * factor, paint);

        //canvas.drawCircle(pos[0], pos[1], dr * factor, strokePaint);
        //canvas.drawCircle(pos[0], pos[1], dr2 * factor, strokePaint);
        factor = 1;

        int x = pos[0];
        int y = pos[1]- 2*dr;
        int dy = 4;
        Path path = new Path();
        path.moveTo(pos[0],pos[1]);
        path.lineTo(x + dr, y+dy);
        path.lineTo(x - dr, y+dy);
        path.lineTo(pos[0],pos[1]);
        canvas.drawCircle(x, y, dr * factor, playerPaint);
        canvas.drawCircle(x, y, dr * factor, strokePaint);
        canvas.drawPath(path,playerPaint);
        canvas.drawPath(path,strokePaint);
        canvas.drawCircle(x, y, dr * factor - 2, white);
        canvas.drawCircle(x, y, dr2 * factor, paint);
        canvas.drawCircle(x, y, dr2 * factor, strokePaint);

    }


    public void drawMainDots(Canvas canvas){
        drawDotsWithQuards(canvas,3*dw,3*dw + diff,colors[0]);
        drawDotsWithQuards(canvas,12*dw,3*dw + diff,colors[1]);
        drawDotsWithQuards(canvas,12*dw,12*dw + diff,colors[2]);
        drawDotsWithQuards(canvas,3*dw,12*dw + diff,colors[3]);

    }

    public void drawDotsWithQuards(Canvas canvas,int x,int y,Paint paint){
        canvas.drawCircle(x+dotSpacing,y+dotSpacing,dotRadius,paint);
        canvas.drawCircle(x+dotSpacing,y-dotSpacing,dotRadius,paint);
        canvas.drawCircle(x-dotSpacing,y+dotSpacing,dotRadius,paint);
        canvas.drawCircle(x-dotSpacing,y-dotSpacing,dotRadius,paint);

        canvas.drawCircle(x+dotSpacing,y+dotSpacing,dotRadius,strokePaint);
        canvas.drawCircle(x+dotSpacing,y-dotSpacing,dotRadius,strokePaint);
        canvas.drawCircle(x-dotSpacing,y+dotSpacing,dotRadius,strokePaint);
        canvas.drawCircle(x-dotSpacing,y-dotSpacing,dotRadius,strokePaint);

    }

    public void drawMultipleSamePLayers(Canvas canvas,int[] pos,int num,Paint paint){
        int x = pos[0], y = pos[1];

        if(num == 1){
            drawPlayers(canvas , new int[]{x-smallspacing,y-smallspacing + diff},paint,smallfactor);
        }
        else if(num == 2){
            drawPlayers(canvas , new int[]{x-smallspacing,y-smallspacing + diff},paint,smallfactor);
            drawPlayers(canvas , new int[]{x+smallspacing,y+smallspacing + diff},paint,smallfactor);
        }
        else if(num == 3){
            drawPlayers(canvas , new int[]{x-smallspacing,y-smallspacing + diff},paint,smallfactor);
            drawPlayers(canvas , new int[]{x+smallspacing,y-smallspacing + diff},paint,smallfactor);
            drawPlayers(canvas , new int[]{x+smallspacing,y+smallspacing + diff},paint,smallfactor);
        }
        else if(num == 4){
            drawPlayers(canvas , new int[]{x-smallspacing,y-smallspacing + diff},paint,smallfactor);
            drawPlayers(canvas , new int[]{x+smallspacing,y-smallspacing + diff},paint,smallfactor);
            drawPlayers(canvas , new int[]{x+smallspacing,y+smallspacing + diff},paint,smallfactor);
            drawPlayers(canvas , new int[]{x-smallspacing,y+smallspacing + diff},paint,smallfactor);
        }
        /*
        drawPlayers(canvas , new int[]{x-smallspacing,y-smallspacing + diff},paint,smallfactor);
        if(--num == 0) return;
        drawPlayers(canvas , new int[]{x+smallspacing,y+smallspacing + diff},paint,smallfactor);
        if(--num == 0) return;
        drawPlayers(canvas , new int[]{x-smallspacing,y+smallspacing + diff},paint,smallfactor);
        if(--num == 0) return;
        drawPlayers(canvas , new int[]{x+smallspacing,y-smallspacing + diff},paint,smallfactor);*/
    }

    public void drawDifferentPlayersLessThan4(Canvas canvas,List<int[]> list,int p){
        int[] pos = positionHandler.getPos(p);
        int x = pos[0], y = pos[1];
        int[][] quad = new int[][]{
                {x-smallspacing,y-smallspacing  + diff},
                {x+smallspacing,y-smallspacing  + diff},
                {x+smallspacing,y+smallspacing  + diff},
               // {x+smallspacing,y-smallspacing  + diff}
                {x-smallspacing,y+smallspacing  + diff}
        };
        for(int i=0;i<list.size();i++) {
            if (i == 4) break;
            drawPlayers(canvas, quad[i], player[list.get(i)[0]].paint, smallfactor);
        }
    }

    public void drawCompleteDots(Canvas canvas,int playernumber,int number){
        switch (playernumber){
            case 0: drawCompletedDotsRed(canvas,number);return;
            case 1: drawCompletedDotsYellow(canvas,number);return;
            case 2: drawCompletedDotsBlue(canvas,number);return;
            case 3: drawCompletedDotsGreen(canvas,number);
        }
    }

    public void drawCompletedDotsRed(Canvas canvas,int dots){
        if(dots == 0)
            return;
        int x = 6*dw;
        int y = centerheight;
        if(dots == 1) {
            canvas.drawCircle((float) (x + 0.5*cw),y,cw/3,white);
            canvas.drawCircle((float) (x + 0.5*cw),y,cw/3,strokePaint);
            return;
        }
        if(dots--<=0)return;
        canvas.drawCircle((float) (x + 0.5*cw), (float) (y + cw*0.5),cw/3,white);
        canvas.drawCircle((float) (x + 0.5*cw), (float) (y + cw*0.5),cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle((float) (x + 0.5*cw), (float) (y - cw*0.5),cw/3,white);
        canvas.drawCircle((float) (x + 0.5*cw), (float) (y - cw*0.5),cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle((float) (x + 1.2*cw),y,cw/3,white);
        canvas.drawCircle((float) (x + 1.2*cw),y,cw/3,strokePaint);
    }

    public void drawCompletedDotsYellow(Canvas canvas,int dots){
        if(dots == 0)
            return;
        int y = 6*dw + diff;
        int x = centerwidth;
        if(dots == 1) {
            canvas.drawCircle(x,(float) (y + 0.5*cw),cw/3,white);
            canvas.drawCircle(x,(float) (y + 0.5*cw),cw/3,strokePaint);
            return;
        }
        if(dots--<=0)return;
        canvas.drawCircle((float) (x + cw*0.5),(float) (y + 0.5*cw), cw/3,white);
        canvas.drawCircle((float) (x + cw*0.5),(float) (y + 0.5*cw), cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle((float) (x - cw*0.5),(float) (y + 0.5*cw), cw/3,white);
        canvas.drawCircle((float) (x - cw*0.5),(float) (y + 0.5*cw), cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle(x,(float) (y + 1.2*cw),cw/3,white);
        canvas.drawCircle(x,(float) (y + 1.2*cw),cw/3,strokePaint);
    }
    public void drawCompletedDotsBlue(Canvas canvas,int dots){
        if(dots == 0)
            return;
        int x = 6*dw;
        int y = centerheight;
        if(dots == 1) {
            canvas.drawCircle(width - (float) (x + 0.5*cw),y,cw/3,white);
            canvas.drawCircle(width - (float) (x + 0.5*cw),y,cw/3,strokePaint);
            return;
        }
        if(dots--<=0)return;
        canvas.drawCircle(width - (float) (x + 0.5*cw), (float) (y + cw*0.5),cw/3,white);
        canvas.drawCircle(width - (float) (x + 0.5*cw), (float) (y + cw*0.5),cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle(width - (float) (x + 0.5*cw), (float) (y - cw*0.5),cw/3,white);
        canvas.drawCircle(width - (float) (x + 0.5*cw), (float) (y - cw*0.5),cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle(width - (float) (x + 1.2*cw),y,cw/3,white);
        canvas.drawCircle(width - (float) (x + 1.2*cw),y,cw/3,strokePaint);
    }
    public void drawCompletedDotsGreen(Canvas canvas,int dots){

        if(dots == 0)
            return;
        int y = 6*dw -diff;
        int x = centerwidth;
        if(dots == 1) {
            Log.e("Open_function", "dots == 1");

//            canvas.drawCircle(x,(float) (y - 0.5*cw),cw/3,white);
//            canvas.drawCircle(x, (float) (y - 0.5*cw),cw/3,strokePaint);
            canvas.drawCircle(x,height/2 + cw*2,cw/3,white);
            canvas.drawCircle(x,height/2 + cw*2,cw/3,strokePaint);
            Log.e("values_points", "dots == 1 ,"+"height = "+height+"y = "+y+"cw = "+cw);


            return;
        }
        if(dots--<=0)return;
        canvas.drawCircle((float) (x + cw*0.5), height/2 + cw*2, cw/3,white);
        canvas.drawCircle((float) (x + cw*0.5), height/2 + cw*2, cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle((float) (x - cw*0.5), height/2 + cw*2, cw/3,white);
        canvas.drawCircle((float) (x - cw*0.5), height/2 + cw*2, cw/3,strokePaint);
        if(dots--<=0)return;
        canvas.drawCircle(x, height/2 + cw*2,cw/3,white);
        canvas.drawCircle(x,height/2 + cw*2,cw/3,strokePaint);
    }
}
