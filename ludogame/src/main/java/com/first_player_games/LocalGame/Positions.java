package com.first_player_games.LocalGame;

import java.util.List;

public class Positions {
    int[] pos;
    int[][] initialpos;
    int playernum;
    int width;
    int diff;
    public Positions(int playernum, List<Integer> list, int width){
        this.playernum = playernum;
        pos = convertToArray(list);
        this.width = width;
        setToInitialPositions();
    }

    public int[] convertToArray(List<Integer> list){
        int[] a = new int[list.size()];
        for(int i=0;i<list.size();i++)
            a[i] = list.get(i);
        return a;
    }

    public int[][] getInitialpos(){
        int[][] a = new int[4][4];
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                a[i][j] = initialpos[i][j];
        return a;
    }

    public void setToInitialPositions(){
        int s = width/15;
        initialpos = new int[4][4];
        int d = width/15;
        int x = 0,y = 0;
        switch (playernum){
            case 0: x=3*d;y=3*d;break;
            case 1: x=12*d;y=3*d;break;
            case 2: x=12*d;y=12*d;break;
            case 3: x=3*d;y=12*d;break;
        }
        initialpos[0][0] = x+s;
        initialpos[0][1] = y+s;
        initialpos[1][0] = x-s;
        initialpos[1][1] = y-s;
        initialpos[2][0] = x+s;
        initialpos[2][1] = y-s;
        initialpos[3][0] = x-s;
        initialpos[3][1] = y+s;
    }
}
