package com.first_player_games.Others;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressPleaseWait {

    public static ProgressDialog getDialogue(Activity activity){
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait");
        return dialog;
    }
}
