package com.metagards.metagames._Poker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.ActionBar;

import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames._Poker.OnItemSelect;

public class DialogRaisePrice {
    Context context;
    OnItemSelect callback;

    public DialogRaisePrice(Context context) {
        this.context = context;
        initDialog();
    }
    Dialog dialog;
    public DialogRaisePrice initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_poker_raise);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        dialog.findViewById(R.id.btnAllin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callback != null)
                    callback.onItemSelect(view,0,"");
            }
        });
        dialog.findViewById(R.id.btnHalfpot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callback != null)
                    callback.onItemSelect(view,0,"");
            }
        });
        dialog.findViewById(R.id.btnFullPot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callback != null)
                    callback.onItemSelect(view,0,"");
            }
        });

        return this;
    }

    public void show(){
        dialog.setCancelable(true);
        dialog.show();
//        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void dismiss(){
        if(dialog != null)
            dialog.dismiss();
    }

    public void setCallback(OnItemSelect callback){
        this.callback = callback;
    }

}
