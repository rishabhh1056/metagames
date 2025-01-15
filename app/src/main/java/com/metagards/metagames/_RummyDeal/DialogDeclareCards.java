package com.metagards.metagames._RummyDeal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Adapter.DeclareAdapter;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.CardModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DialogDeclareCards {
    Context context;
    ArrayList<CardModel> game_users_cards_list = new ArrayList<>();
    int game_start_time;
    int _mint_mis = 5000;


    public DialogDeclareCards(Context context) {
        this.context = context;
        initDialog();
    }

    public DialogDeclareCards setGame_users_cards_list(ArrayList<CardModel> game_users_cards_list, String str_game_id) {

        boolean isUserLeaveTable = true;
        outer1:
        for (int i = 0; i < game_users_cards_list.size() ; i++) {
            CardModel previsous = game_users_cards_list.get(i);
            for (int k = 0; k < game_users_cards_list.size(); k++) {
                CardModel all_item = game_users_cards_list.get(k);
                if (previsous.score != all_item.score) {
                    //do stuff
                    isUserLeaveTable = false;
                    break outer1;
                }
            }
        }

        game_users_cards_list.get(0).setUserLeaveTable(isUserLeaveTable);


        this.game_users_cards_list = game_users_cards_list;
        ((TextView) dialog.findViewById(R.id.txt_title)).setText(context.getString(R.string.result_game_id)+str_game_id);

        int imageResource = Functions.GetResourcePath(game_users_cards_list.get(0).joker_card,context);

        if(imageResource > 0)
            Picasso.get().load(imageResource).into(((ImageView) dialog.findViewById(R.id.imgjokercard)));


        RecyclerView recyclerView = dialog.findViewById(R.id.rec_declareresult);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DeclareAdapter declareAdapter = new DeclareAdapter(context,game_users_cards_list);
        recyclerView.setAdapter(declareAdapter);
        for (CardModel model: game_users_cards_list) {
            if(model.total < 0)
            {
                dialog.findViewById(R.id.lnrTotal)
                        .setVisibility(View.GONE);
            }

//            if(model.won < 0)
//            {
//                dialog.findViewById(R.id.lnrWin)
//                        .setVisibility(View.GONE);
//            }
        }

        return this;
    }

    public DialogDeclareCards setGame_start_time(int game_start_time) {
        this.game_start_time = game_start_time;
        int intervel = 1000;
        _mint_mis = game_start_time * intervel;

        new CountDownTimer(_mint_mis, intervel) {
            @Override
            public void onTick(long millisUntilFinished) {

                ((TextView) dialog.findViewById(R.id.txt_timer)).setText(
                        context.getString(R.string.get_ready_next_game_start_in)+(millisUntilFinished/1000)+""+Functions.getString(context,R.string.seconds));

            }

            @Override
            public void onFinish() {
                if(dialog != null && !((Activity)context).isFinishing())
                    dialog.dismiss();
                startGame("startgame");

            }
        }.start();
        return this;
    }

    Dialog dialog;
    private DialogDeclareCards initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_declare_result);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Functions.setDialogParams(dialog);
        return this;
    }

    public void show(){
        if(!dialog.isShowing() && Functions.isActivityExist(context))
            dialog.show();
    }


    public void startGame(String resp){

    }
}


