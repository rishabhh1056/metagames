package com.metagards.metagames.Menu;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.appcompat.app.ActionBar;

import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames._RummyPull.Fragments.RummyActiveTables_BF;

public class DialogSelectPlayer {
    
    Activity context;
    OnItemClickListener onClick;

    public DialogSelectPlayer(Activity context) {
        this.context = context;
        initDialog();
    }

    public void setOnClick(OnItemClickListener onClick) {
        this.onClick = onClick;
    }

    Dialog dialog;
    LinearLayout lnr_2player, lnr_5player;
    TextView tv_2player, tv_5player;
    int selected_type = 5;
    
    private void initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_select_palyer);
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lnr_2player = dialog.findViewById(R.id.lnr_2player);
        lnr_5player = dialog.findViewById(R.id.lnr_5player);
        tv_2player = (TextView) dialog.findViewById(R.id.tv_2player);
        tv_5player = (TextView) dialog.findViewById(R.id.tv_5player);

        Button btn_player = dialog.findViewById(R.id.btn_play);

        lnr_2player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(2);
            }
        });

        lnr_5player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(5);
            }
        });

        btn_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (selected_type == 2) {
                    onClick.Response(v,2, RummyActiveTables_BF.RUMMY2);
                } else {
                    onClick.Response(v,5,RummyActiveTables_BF.RUMMY5);
                }


            }
        });


        ChangeTextviewColorChange(5);
    }

    public void show(){
        dialog.setCancelable(true);
        dialog.show();
    }

    private void ChangeTextviewColorChange(int colortype) {

        selected_type = colortype;

        if (colortype == 2) {
            tv_2player.setTextColor(getColor(R.color.white));
            lnr_2player.setBackgroundColor(getColor(R.color.new_colorPrimary));

            tv_5player.setTextColor(getColor(R.color.black));
            lnr_5player.setBackgroundColor(getColor(R.color.white));

        } else {
            tv_2player.setTextColor(getColor(R.color.black));
            lnr_2player.setBackgroundColor(getColor(R.color.white));

            tv_5player.setTextColor(getColor(R.color.white));
            lnr_5player.setBackgroundColor(getColor(R.color.new_colorPrimary));

        }


    }

    public void setMaxPlayerText(@IntRange(from = 3,to = 6) int maxPlayer)
    {
        tv_5player.setText(maxPlayer+"\nPlayer");
    }
    
    private int getColor(int color){
        return context.getResources().getColor(color);
    }

}
