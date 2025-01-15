package com.first_player_games.LocalGame;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Gamestate {
    int a,p;
    Player[] player;
    PositionHandler positionHandler;
    DiceHandler diceHandler;
    Context context;
    int turn = 0;
    boolean[] allowedplayers;
    int[] playersIdentity;
    HashMap<Integer,List<int[]>> overlappingData;
    public Gamestate(PositionHandler positionHandler, Context context,boolean[] allowedplayers)
    {
        this.allowedplayers = allowedplayers;
        this.positionHandler = positionHandler;
        player = new Player[4];
        this.context = context;
        for(int i=0;i<4;i++)
            //1
            if(allowedplayers[i])
                player[i] = new Player(positionHandler,i,context){
                    public void movementFinished(boolean next){
                        movementFinishedCheck(true,next);
                    }
                };
    }

    public int diceRolled(){
        if(diceHandler.steps != 6 && !player[turn].isAnyUnlocked()) {nextTurn(); return 0;}
        else if(player[turn].hasSingleMovable(diceHandler.steps) && diceHandler.steps != 6) {
            player[turn].move(player[turn].getSingleMovable(diceHandler.steps), diceHandler.steps);return 0;
        }
        else if(!player[turn].checkMovementAvailable(diceHandler.steps) && !player[turn].allLastOrComplete(diceHandler.steps)) {nextTurn();return 0;}
        else if(player[turn].allLastOrComplete(diceHandler.steps) && diceHandler.steps == 6) {
            diceHandler.rolled = false;
            return -1;
        }
        else if(player[turn].nothingMoveable(diceHandler.steps) && diceHandler.steps != 6) {
            nextTurn();
            return 0;
        }

        return 1;
    }

    public void movementFinishedCheck(boolean shouldNext,boolean waslast){

        overlappingData = getOverlappingData();
        List<int[]> overlaps = checkOverlappingPlayers();
        if(overlaps.size()>0)
            for(int i=0;i<overlaps.size();i++)
                if (overlaps.get(i)[0] != turn )
                    if(overlaps.get(i)[2]%13 != 0 && (overlaps.get(i)[2] - 8)%13 != 0 ) {
                        player[overlaps.get(i)[0]].returnToZero(overlaps.get(i)[1]);
                        // movementFinishedCheck(false,waslast);
                        nextTurn(waslast,shouldNext=false);
                        break;
                    }
        diceHandler.rolled = false;

        if(shouldNext) {
            if (diceHandler.steps != 6 && !waslast) nextTurn();
            else onMovementFinished();
        }
        if(checkWon() != -1)
            onGameWon(checkWon());
    }

    private void nextTurn(boolean waslast, boolean b) {
    }

    public int checkWon(){
        for(int i=0;i<4;i++)
            if(allowedplayers[i]) {
                a=0;
                for(int j=0;j<4;j++)
                    if(player[i].p[j]>=57)
                        a++;
                if(a==4)
                    return i;
            }
        return -1;
    }

    public void setDiceHandler(DiceHandler diceHandler){
        this.diceHandler = diceHandler;
    }


    public void pieceClicked(int playernumber,int piece){
        player[playernumber].move(piece,diceHandler.steps);
    }

    public List<int[]> checkOverlappingPlayers(){
        List<int[]> overlapping = new ArrayList<>();
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                if(allowedplayers[i]){
                    p = player[i].position(j);
                    if(p!=-1)
                        if(check(i,p))
                            overlapping.add(new int[]{i,j,p});
                }
        return overlapping;
    }

    public HashMap<Integer,List<int[]>> getOverlappingData(){
        HashMap<Integer,List<int[]>> data = new HashMap<>();
        List<int[]> overlapping = new ArrayList<>();
        for(int i=0;i<4;i++)
            if(allowedplayers[i])
                for(int j=0;j<4;j++) {
                    int p = player[i].position(j);
                    if(p!=-1)
                        if(check(i,p))
                            overlapping.add(new int[]{i,j,p});
                }
        for(int i=0;i<overlapping.size();i++)
            if(data.containsKey(overlapping.get(i)[2])) {
                List<int[]> a = data.get(overlapping.get(i)[2]);
                if(a!=null) a.add(new int[]{overlapping.get(i)[0],overlapping.get(i)[1]});
                data.put(overlapping.get(i)[2],a);
            }
            else {
                List<int[]> a = new ArrayList<>();
                a.add(new int[]{overlapping.get(i)[0],overlapping.get(i)[1]});
                data.put(overlapping.get(i)[2],a);
            }
        return data;
    }

    public boolean check(int i,int p){
        for(int k=0;k<4;k++)
            if(allowedplayers[k])
                for(int l=0;l<4;l++)
                    if(i!=k)
                        if(player[k].position(l) == p)
                            return true;
        return false;
    }

    public void initTurns(){
        turn = 0;
        while (!allowedplayers[turn])
            turn++;
    }

    public void nextTurn(){
        turn = (turn+1)%4;
        while (!allowedplayers[turn])
            turn = (turn+1)%4;
        diceHandler.rolled = false;
        onNextTurn();
    }

    public void startNewGame(){
        for(int i=0;i<4;i++)
            if(allowedplayers[i])
                for(int j=0;j<4;j++)
                    player[i].returnToZero(j);
    }

    public void setPlayersIdentity(int[] playersIdentity){
        this.playersIdentity = playersIdentity;
    }

    public boolean isComputer(){
        return playersIdentity[turn] == -1;
    }

    public void makeRandomMove(){
        List<Integer> list = player[turn].getMoveablePiecesList(diceHandler.steps);
        if(list.size()>0)
            player[turn].move(
                    list.get(diceHandler.random.nextInt(list.size())),
                    diceHandler.steps
            );

    }

    public boolean checkForMovingItems(){
        for(int i=0;i<4;i++)
            if(allowedplayers[i])
                for(int j=0;j<4;j++)
                    if(player[i].isMoving(j))
                        return true;
        return false;
    }


    public void onGameWon(int playernumber){ }
    public void onNextTurn(){ }
    public void onMovementFinished(){ }

}

