package com.first_player_games.Menu;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;

import com.first_player_games.Home_Activity;
import com.first_player_games.R;

public class ComputerGameMenu {

    int[][] checkboxIDs = new int[][]{
            {R.id.alertdialogueplaylocalbox1,R.id.alertdialogueplaylocalbox1person,R.id.alertdialogueplaylocalbox1computer},
            {R.id.alertdialogueplaylocalbox2,R.id.alertdialogueplaylocalbox2person,R.id.alertdialogueplaylocalbox2computer},
            {R.id.alertdialogueplaylocalbox3,R.id.alertdialogueplaylocalbox3person,R.id.alertdialogueplaylocalbox3computer},
            {R.id.alertdialogueplaylocalbox4,R.id.alertdialogueplaylocalbox4person,R.id.alertdialogueplaylocalbox4computer}
    };

    int[] players;
    AlertDialog alert;
    RelativeLayout relativeLayout;
    Activity activity;
    public ComputerGameMenu(Activity activity){
        this.activity = activity;
        relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_vs_computer_options,null);
        players = new int[4];
        players[0] = 1;
        players[1] = -1;
        players[2] = -1;
        players[3] = -1;
        alert = new AlertDialog.Builder(activity,R.style.CustomDialog).setView(relativeLayout).create();
        alert.setView(relativeLayout);

        for(int i=0;i<4;i++)
            for(int j=1;j<=2;j++)
                relativeLayout.findViewById(checkboxIDs[i][j]).setOnClickListener(new CheckButtonClickListner(i,j){
                    public void optionClicked(int i,int j){
                        if((players[i] == 1 && j == 1)||(players[i] == -1 && j == 2)){
                            relativeLayout.findViewById(checkboxIDs[i][1]).setAlpha(0.4f);
                            relativeLayout.findViewById(checkboxIDs[i][2]).setAlpha(0.4f);
                            players[i] = 0;
                        }
                        else if(j == 1){
                            relativeLayout.findViewById(checkboxIDs[i][1]).setAlpha(1f);
                            relativeLayout.findViewById(checkboxIDs[i][2]).setAlpha(0.4f);
                            players[i] = 1;
                        }
                        else if(j == 2){
                            relativeLayout.findViewById(checkboxIDs[i][1]).setAlpha(0.4f);
                            relativeLayout.findViewById(checkboxIDs[i][2]).setAlpha(1f);
                            players[i] = -1;
                        }
                    }
                });
        relativeLayout.findViewById(R.id.vscomputergamemenustartbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canStartGame())
                    onGameStartAction(players);
            }
        });


    }

    public void showMenu(){
        alert.show();
    }

    public void dismiss(){
        alert.dismiss();
    }

    public boolean canStartGame(){
        int a=0,b=0;
        for(int i=0;i<4;i++)
            if(players[i] == 1) a++;
            else if(players[i] == -1) b++;
        return  a>0 && b>0;
    }


    public class CheckButtonClickListner implements View.OnClickListener {

        int i;
        int j;
        public CheckButtonClickListner(int i,int j){
            this.i = i;
            this.j = j;
        }
        @Override
        public void onClick(View view) {
            optionClicked(i,j);
        }

        public void optionClicked(int i,int j){}
    }

    public void onGameStartAction(int[] players){ }
}
