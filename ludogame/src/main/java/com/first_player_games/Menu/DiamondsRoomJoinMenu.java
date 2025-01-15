package com.first_player_games.Menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
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

public class DiamondsRoomJoinMenu {
    AlertDialog alert;
    FirebaseFunctions functions;
    ProgressDialog dialog;
    TextView errorview;
    String roomid;
    RelativeLayout relativeLayout;
    boolean joined = false;
    public DiamondsRoomJoinMenu(Home_Activity activity){
        dialog = ProgressPleaseWait.getDialogue(activity);
        functions = FirebaseFunctions.getInstance();
        relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_room_search_results,null);
        errorview = relativeLayout.findViewById(R.id.errormessageview);
        alert = new AlertDialog.Builder(activity).setView(relativeLayout).create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        relativeLayout.findViewById(R.id.join_room_button_paid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                relativeLayout.findViewById(R.id.errormessageview).setVisibility(View.INVISIBLE);
                if(!joined)
                functions.getHttpsCallable("getDiamonds").call().continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (String) task.getResult().getData().toString();
                    }
                }).addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(task.isSuccessful()){
                            if(!task.equals("error")){
                                final int diamonds_available = Integer.parseInt(task.getResult());
                                functions.getHttpsCallable("getDiamondsInGame").call(roomid).continueWith(new Continuation<HttpsCallableResult, String>() {
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
                                                int diamonds_in_game = Integer.parseInt(task.getResult());
                                                if(diamonds_in_game <=diamonds_available) {
                                                    joined = true;
                                                    canJoinGame(roomid, diamonds_in_game);
                                                }
                                                else
                                                    relativeLayout.findViewById(R.id.errormessageview).setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                });
                            }
                            else dialog.dismiss();
                        }
                        else dialog.dismiss();
                    }
                });
            }
        });
    }
    public void setRoomid(String roomid){
        this.roomid = roomid;
        dialog.show();
        functions.getHttpsCallable("getDiamondsInGame").call(roomid).continueWith(new Continuation<HttpsCallableResult, String>() {
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
                        ((TextView) relativeLayout.findViewById(R.id.number_of_diamonds_field))
                                .setText(task.getResult()+" diamonds");
                    }
                }
            }
        });
    }

    public void canJoinGame(String roomid,int diamonds){ }
    public void show(){alert.show();}
    public void dismiss(){alert.dismiss();}
}
