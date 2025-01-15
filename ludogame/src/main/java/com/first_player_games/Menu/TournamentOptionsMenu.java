package com.first_player_games.Menu;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import com.first_player_games.Home_Activity;
import com.first_player_games.R;

public class TournamentOptionsMenu {
    AlertDialog alert;
    public TournamentOptionsMenu(Home_Activity activity){
        RelativeLayout relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_tournament_find_alert,null);
        alert = new AlertDialog.Builder(activity).setView(relativeLayout).create();
        relativeLayout.findViewById(R.id.home_create_room_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findGame(2);
            }
        });
        relativeLayout.findViewById(R.id.home_join_room_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findGame(4);
            }
        });
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void showMenu(){ alert.show();}
    public void dismiss(){ alert.dismiss();}

    public void findGame(int players){ }
}
