package com.first_player_games.OnlineGame.OnlineGame_V2;

import java.util.ArrayList;
import java.util.List;

public class PositionHandler {
    int width,centerWidth;
    int dw,cw;
    List<int[]> pointsList;

    int[] specialposQuad;
    int lastX,lastY;

    Positions[] positions;

    public PositionHandler(int width){
        pointsList = new ArrayList<>();
        this.width = width;
        centerWidth = width/2;
        dw = width/15;
        cw = dw/2;
        specialposQuad = new int[4];
        record();
    }

    public int[] getPos(int p){
        int[] b = new int[]{pointsList.get(p)[0],pointsList.get(p)[1]};
        return b;
    }

    public void record(){
        recordPos();
        positions = new Positions[4];
        positions[0] = new Positions(0,recordRed(),width);
        positions[1] = new Positions(1,recordYellow(),width);
        positions[2] = new Positions(2,recordBlue(),width);
        positions[3] = new Positions(3,recordGreen(),width);
    }
    public List<Integer> recordRed(){
        List<Integer> list = new ArrayList<>();
        list.add(-1);
        for(int i=0;i<51;i++)
            list.add(i);
        for(int i=specialposQuad[0];i<specialposQuad[0]+6;i++)
            list.add(i);
        return list;
    }

    public List<Integer> recordYellow(){
        List<Integer> list = new ArrayList<>();
        list.add(-1);
        for(int i=13;i<52;i++)
            list.add(i);
        for(int i=0;i<12;i++)
            list.add(i);
        for(int i=specialposQuad[1];i<specialposQuad[1]+6;i++)
            list.add(i);
        return list;
    }

    public List<Integer> recordBlue(){
        List<Integer> list = new ArrayList<>();
        list.add(-1);
        for(int i=26;i<52;i++)
            list.add(i);
        for(int i=0;i<25;i++)
            list.add(i);
        for(int i=specialposQuad[2];i<specialposQuad[2]+6;i++)
            list.add(i);
        return list;
    }

    public List<Integer> recordGreen(){
        List<Integer> list = new ArrayList<>();
        list.add(-1);
        for(int i=39;i<52;i++)
            list.add(i);
        for(int i=0;i<38;i++)
            list.add(i);
        for(int i=specialposQuad[3];i<specialposQuad[3]+6;i++)
            list.add(i);
        return list;
    }


    public void recordPos(){
        int x = cw;
        int y = centerWidth;

        x +=dw;
        y -=dw;
        start(pointsList,x,y);
        addRight(pointsList,4);
        addTopArc(pointsList);
        addRightArc(pointsList);
        addBottomtArc(pointsList);
        add_125(pointsList);
        addLeft(pointsList,5);
        addTop(pointsList,2);

        lastX = cw ;
        lastY = centerWidth;
        specialposQuad[0] = pointsList.size();
        addRight(pointsList,5);
        addCenter(pointsList);

        lastX = centerWidth;
        lastY = cw;
        specialposQuad[1] = pointsList.size();
        addBottom(pointsList,5);
        addCenter(pointsList);

        lastX = width - cw;
        lastY = centerWidth;
        specialposQuad[2] = pointsList.size();
        addLeft(pointsList,5);
        addCenter(pointsList);

        lastX = centerWidth;
        lastY = width-cw;
        specialposQuad[3] = pointsList.size();
        addTop(pointsList,5);
        addCenter(pointsList);

    }

    public void addTopArc(List<int[]> list){
        add_45(list);
        addTop(list,5);
        addRight(list,2);
        addBottom(list,5);
    }

    public void addRightArc(List<int[]> list){
        addM_45(list);
        addRight(list,5);
        addBottom(list,2);
        addLeft(list,5);
    }

    public void addBottomtArc(List<int[]> list){
        addM_125(list);
        addBottom(list,5);
        addLeft(list,2);
        addTop(list,5);
    }

    public void addLeftArc(List<int[]> list){
        add_125(list);
        addLeft(list,5);
        addTop(list,2);
        addRight(list,5);
    }



    public void start(List<int[]> list,int x,int y){
        lastX = x;
        lastY = y;
        addPoints(list);
    }


    public void addTop(List<int[]> list,int n){
        for(int i=0;i<n;i++) {
            lastY -= dw;
            addPoints(list);
        }
    }

    public void addRight(List<int[]> list,int n){
        for(int i=0;i<n;i++) {
            lastX += dw;
            addPoints(list);
        }
    }

    public void addBottom(List<int[]> list,int n){
        for(int i=0;i<n;i++) {
            lastY += dw;
            addPoints(list);
        }
    }

    public void addLeft(List<int[]> list,int n){
        for(int i=0;i<n;i++) {
            lastX -= dw;
            addPoints(list);
        }
    }



    public void add_45(List<int[]> list){
        lastX+=dw;
        lastY-=dw;
        addPoints(list);
    }

    public void addM_45(List<int[]> list){
        lastX+=dw;
        lastY+=dw;
        addPoints(list);
    }

    public void add_125(List<int[]> list){
        lastX-=dw;
        lastY-=dw;
        addPoints(list);
    }

    public void addM_125(List<int[]> list){
        lastX-=dw;
        lastY+=dw;
        addPoints(list);
    }

    public void addCenter(List<int[]> list){
        list.add(new int[]{centerWidth,centerWidth});
    }

    public void addPoints(List<int[]> list){
        list.add(new int[]{lastX,lastY});
    }


    public static int[][] convertToArray(List<int[]> list){
        int[][] arr = new int[list.size()][2];
        for(int i=0;i<list.size();i++)
            arr[i] = list.get(i);
        return arr;
    }

    public int[] getRedPos(int i){
        return pointsList.get(i);
    }

}
