package com.metagards.metagames._CarRoullete.menu;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._LuckJackpot.Model.JackpotWinHistory;
import com.google.gson.Gson;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DialogCarRollouteHistory {

    Context context;
    Callback callback;
    private static DialogCarRollouteHistory mInstance;


    public static DialogCarRollouteHistory getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogCarRollouteHistory.class) {
                if (null == mInstance) {
                    mInstance = new DialogCarRollouteHistory(context);
                }
            }
        }

        if(mInstance != null)
            mInstance.init(context);

        return mInstance;
    }

    /**
     * initialization of context, use only first time later it will use this again and again
     *
     * @param context app context: first time
     */
    public DialogCarRollouteHistory init(Context context) {
        try {

            if (context != null) {
                this.context = context;
                initDialog();
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return mInstance;
    }

    RecyclerView recJackpotHistory ;
    TextView tvHeader;
    ArrayList<JackpotWinHistory.Winner> rulesModelArrayList = new ArrayList<JackpotWinHistory.Winner>();
    private DialogCarRollouteHistory initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_jackpot_history);

        tvHeader = dialog.findViewById(R.id.txtheader);
        recJackpotHistory = dialog.findViewById(R.id.recJackpotHistory);
        recJackpotHistory.setLayoutManager(new LinearLayoutManager(context));
        getJackpotHistoryList();

        return mInstance;
    }

    public DialogCarRollouteHistory(Context context) {
        this.context = context;
    }

    public DialogCarRollouteHistory() {
    }
    Dialog dialog;

    public DialogCarRollouteHistory show() {

        dialog.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return mInstance;
    }

    public void dismiss(){
        if(dialog != null)
            dialog.dismiss();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    void getJackpotHistoryList(){
        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",prefs.getString("user_id", ""));
        params.put("token",""+ SharePref.getAuthorization());


        ApiRequest.Call_Api(context, Const.JackpotWinnerHistory, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp != null)
                {
                        Gson gson = new Gson();
                        Reader reader = new StringReader(resp);
                        final JackpotWinHistory jackpotWinHistory = gson.fromJson(reader, JackpotWinHistory.class);

                        if(jackpotWinHistory.getCode() == 200)
                        {
                            rulesModelArrayList.clear();
                            rulesModelArrayList.addAll(jackpotWinHistory.getWinners());
                        }

                    recJackpotHistory.setAdapter(new jackpotHistoryAdapter(rulesModelArrayList));
                }
            }
        });
    }

    class jackpotHistoryAdapter extends RecyclerView.Adapter<jackpotHistoryAdapter.myHolder>{
        ArrayList<JackpotWinHistory.Winner> rulesModelArrayList;
        public jackpotHistoryAdapter(ArrayList<JackpotWinHistory.Winner> rulesModelArrayList) {
            this.rulesModelArrayList = rulesModelArrayList;
        }

        public void setDataList(ArrayList<JackpotWinHistory.Winner> rulesModelArrayList){
            this.rulesModelArrayList.addAll(rulesModelArrayList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jackpothistory,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull myHolder holder, int position) {
            JackpotWinHistory.Winner winnermodel = rulesModelArrayList.get(position);
            if(winnermodel != null)
                holder.bind(winnermodel);
        }

        @Override
        public int getItemCount() {
            return rulesModelArrayList.size();
        }

        public  class myHolder extends RecyclerView.ViewHolder{
            public myHolder(@NonNull View itemView) {
                super(itemView);
            }

            float total_amount = 0;
            public void bind(JackpotWinHistory.Winner winnermodel) {

                JackpotWinHistory.UserDatum userDataModel = winnermodel.getUserData().get(0);

                float winnig_amount = Float.parseFloat(userDataModel.getWinningAmount());

                total_amount += winnig_amount;

                tvHeader.setText("Jackpot : "+Variables.CURRENCY_SYMBOL+total_amount);

                getTextView(R.id.tvTime).setText(""+winnermodel.getTime());
                getTextView(R.id.tvWinnerId).setText(""+winnermodel.getWinners());
                getTextView(R.id.tvrewards).setText(""+winnermodel.getRewards());
                getTextView(R.id.txtName).setText(""+userDataModel.getName());
                getTextView(R.id.tvbetvalue).setText(
                        "BET "+ Variables.CURRENCY_SYMBOL +userDataModel.getAmount()+" "+
                        "GET "+ Variables.CURRENCY_SYMBOL +userDataModel.getWinningAmount());
                Glide.with(context).load(Const.IMGAE_PATH + userDataModel.getProfilePic()).into(((ImageView)itemView.findViewById(R.id.ivUserImage)));
                String[] typeCards = winnermodel.getType().split(",");
                int card1 = Functions.GetResourcePath(typeCards[0],context);
                int card2 = Functions.GetResourcePath(typeCards[1],context);
                int card3 = Functions.GetResourcePath(typeCards[2],context);
                Glide.with(context).load(Functions.getDrawable(context,card1)).into(((ImageView)itemView.findViewById(R.id.ivJackpotCard1)));
                Glide.with(context).load(Functions.getDrawable(context,card2)).into(((ImageView)itemView.findViewById(R.id.ivJackpotCard2)));
                Glide.with(context).load(Functions.getDrawable(context,card3)).into(((ImageView)itemView.findViewById(R.id.ivJackpotCard3)));
            }

            TextView getTextView(int id){
               return  ((TextView) itemView.findViewById(id));
            }
        }


    }
}
