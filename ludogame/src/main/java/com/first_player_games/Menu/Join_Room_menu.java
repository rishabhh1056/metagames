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
import com.first_player_games.Others.Constants;
import com.first_player_games.Others.ProgressPleaseWait;
import com.first_player_games.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

public class Join_Room_menu {
    AlertDialog alert;
    FirebaseFunctions functions;
    TextView warningtextview;
    ProgressDialog dialog;
    public Join_Room_menu(Home_Activity activity){

        final RelativeLayout relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_join_room_input,null);
        alert = new AlertDialog.Builder(activity).setView(relativeLayout).create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog = ProgressPleaseWait.getDialogue(activity);
        functions = FirebaseFunctions.getInstance();
        if(Constants.UseEmulator)
         functions.useEmulator("10.0.2.2",5001);
        warningtextview = relativeLayout.findViewById(R.id.room_join_box_warming_textview);
        relativeLayout.findViewById(R.id.join_room_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                warningtextview.setVisibility(View.INVISIBLE);
                final String roomid = ((EditText) relativeLayout.findViewById(R.id.join_room_roomid_edit_text)).getText().toString();
                functions.getHttpsCallable("searchRoom").call(roomid)
                        .continueWith(new Continuation<HttpsCallableResult, String>() {
                            @Override
                            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                return (String) task.getResult().getData().toString();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {

                                if(task.isSuccessful()) {
                                    final String roomresult = task.getResult();
                                    System.out.println("Room results     "+roomresult);
                                    functions.getHttpsCallable("getDiamondsInGame").call(roomid).continueWith(new Continuation<HttpsCallableResult, String>() {
                                        @Override
                                        public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                            return task.getResult().getData().toString();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            dialog.dismiss();
                                            if(task.isSuccessful())
                                            {
                                                int diamonds = -1;
                                                if(task.getResult().equals("not found"))
                                                    diamonds = -1;
                                                else diamonds = Integer.parseInt(task.getResult());

                                                System.out.println("DIAMONDS    "+diamonds);
                                                if(roomresult.equals("found") && diamonds > 0) {
                                                    System.out.println("Diamonds room found");
                                                    diamondsRoomFound(roomid);
                                                }
                                                else if(roomresult.equals("found") && diamonds == -1)
                                                    roomFound(roomid);
                                                else {
                                                    warningtextview.setVisibility(View.VISIBLE);
                                                    if (roomresult.equals("full"))
                                                        warningtextview.setText("Room is Full");
                                                    else
                                                        warningtextview.setText("Room no found");
                                                }

                                            }
                                            else
                                                System.out.println("ERROR     " + task.getException().getMessage());
                                        }
                                    });
                                }
                                else {
                                    dialog.dismiss();
                                    System.out.println("Exception   " + task.getException());
                                }
                            }
                });
            }
        });
    }

    public void showMenu(){alert.show();}
    public void dismiss(){alert.dismiss();}
    public void roomFound(String roomid){ }
    public void diamondsRoomFound(String roomid){ }
}
