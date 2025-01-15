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

public class ReferIdMenu {

    AlertDialog alert;
    FirebaseFunctions functions;
    ProgressDialog dialog;
    String refer_id = null;
    public ReferIdMenu(Home_Activity activity){
        final RelativeLayout relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_refer_id,null);
        alert = new AlertDialog.Builder(activity).setView(relativeLayout).create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        functions = FirebaseFunctions.getInstance();
        dialog = ProgressPleaseWait.getDialogue(activity);
        functions.getHttpsCallable("getReferId").call().continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                return task.getResult().getData().toString();
            }
        }).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                dialog.dismiss();
                if(task.isSuccessful()) {
                    refer_id = task.getResult();
                    refer_id = refer_id.toLowerCase();
                    ((TextView) relativeLayout.findViewById(R.id.refer_id_view)).setText(refer_id);
                }
            }
        });

        relativeLayout.findViewById(R.id.share_id_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareButtonPressed(refer_id);
            }
        });
    }

    public void show(){
        if(refer_id == null)
            dialog.show();
        alert.show();
    }
    public void dismiss(){alert.dismiss();}

    public void onShareButtonPressed(String referid){ }

}
