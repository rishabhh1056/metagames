package com.first_player_games.Menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.first_player_games.Home_Activity;
import com.first_player_games.Others.ProgressPleaseWait;
import com.first_player_games.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

public class CreateRoomMenu {

    AlertDialog alert;
    FirebaseFunctions functions;
    ProgressDialog dialog;
    TextView errorview;
    public CreateRoomMenu(Home_Activity activity){
        dialog = ProgressPleaseWait.getDialogue(activity);
        functions = FirebaseFunctions.getInstance();
        final RelativeLayout relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_create_room_game_options,null);
        errorview = relativeLayout.findViewById(R.id.errormessageview);
        alert = new AlertDialog.Builder(activity).setView(relativeLayout).create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        relativeLayout.findViewById(R.id.create_room_button_free).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                craeteFreeRoom();
            }
        });
        relativeLayout.findViewById(R.id.create_room_button_paid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                errorview.setVisibility(View.INVISIBLE);
                final int diamonds = Integer.parseInt(((EditText) relativeLayout.findViewById(R.id.number_of_diamonds_field)).getText().toString());

                functions.getHttpsCallable("getDiamonds").call().continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (String) task.getResult().getData().toString();
                    }
                }).addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()){
                            if(!task.equals("error")){
                                int diamonds_available = Integer.parseInt(task.getResult());
                                System.out.println("AVAILABLE DIAMONDS ARE    "+diamonds_available);
                                if(diamonds_available >= diamonds)
                                    craetePaidRoom(diamonds);
                                else
                                    errorview.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });


            }
        });
    }

    public void craeteFreeRoom(){}
    public void craetePaidRoom(int diamonds){}

    public void show(){alert.show();}
    public void dismiss(){alert.dismiss();}
}
