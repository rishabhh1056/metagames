package com.first_player_games.Menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.first_player_games.Home_Activity;
import com.first_player_games.R;

public class LocalGameMenu {

    int[][] checkboxIDs = new int[][]{
            {R.id.alertdialogueplaylocalbox1,R.id.alertdialogueplaylocalcheck1},
            {R.id.alertdialogueplaylocalbox2,R.id.alertdialogueplaylocalcheck2},
            {R.id.alertdialogueplaylocalbox3,R.id.alertdialogueplaylocalcheck3},
            {R.id.alertdialogueplaylocalbox4,R.id.alertdialogueplaylocalcheck4}
    };

    boolean[] players;
    AlertDialog alert;
    RelativeLayout relativeLayout;
    Activity activity;
    public LocalGameMenu(Activity activity){
        this.activity = activity;
        relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_local_game_options,null);
        players = new boolean[4];
        for(int i=0;i<4;i++)
            players[i] = true;
        alert = new AlertDialog.Builder(activity,R.style.CustomDialog).setView(relativeLayout).create();


        final TextView playersCountVier = relativeLayout.findViewById(R.id.localgamemenuplayercountview);
        for(int i=0;i<4;i++)
            relativeLayout.findViewById(checkboxIDs[i][0])
                    .setOnClickListener(new CheckButtonClickListner(relativeLayout,checkboxIDs[i][1], i) {
                        @SuppressLint("SetTextI18n")
                        public void changeValue(boolean value, int num) {
                            players[num] = value;
                            playersCountVier.setText(getPLayerCount()+" player game");
                        }
                    });
        relativeLayout.findViewById(R.id.localgamemenustartbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(getPLayerCount()) > 1)
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

    public String getPLayerCount(){
        int a=0;
        for(int i=0;i<players.length;i++)
            if(players[i]) a++;
        return String.valueOf(a);
    }

    public class CheckButtonClickListner implements View.OnClickListener {
        ImageView check;
        int num;
        public CheckButtonClickListner(RelativeLayout layout,int imgid,int num){
            this.check = layout.findViewById(imgid);
            this.num = num;
        }

        @Override
        public void onClick(View view) {
            if(check.getVisibility() == View.VISIBLE) {
                check.setVisibility(View.GONE);
                changeValue(false,num);
            }
            else {
                check.setVisibility(View.VISIBLE);
                changeValue(true,num);
            }
        }

        public void changeValue(boolean value,int num){}
    }

    public void onGameStartAction(boolean[] players){ }
}
