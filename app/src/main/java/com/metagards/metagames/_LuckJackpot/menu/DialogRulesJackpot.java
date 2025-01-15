package com.metagards.metagames._LuckJackpot.menu;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames._LuckJackpot.Model.JackpotLastWinHistory;
import com.google.gson.Gson;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DialogRulesJackpot {

    Context context;
    Callback callback;
    private static DialogRulesJackpot mInstance;

    private final String SET = "SET";
    private final String PURE_SEQ = "PURE SEQ";
    private final String SEQ = "SEQ";
    private final String COLOR = "COLOR";
    private final String PAIR = "PAIR";
    private final String HIGH_CARD = "HIGH CARD";

    public static DialogRulesJackpot getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogRulesJackpot.class) {
                if (null == mInstance) {
                    mInstance = new DialogRulesJackpot(context);
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
    public DialogRulesJackpot init(Context context) {
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
    ArrayList<JackpotLastWinHistory.Winner> rulesModelArrayList = new ArrayList<>();
    private DialogRulesJackpot initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_jackpot_lastwin_record);

        tvHeader = dialog.findViewById(R.id.txtheader);
        tvHeader.setText("How to Play");
        recJackpotHistory = dialog.findViewById(R.id.recJackpotHistory);
        recJackpotHistory.setLayoutManager(new GridLayoutManager(context,3));
        getJackpotHistoryList();

        return mInstance;
    }

    public DialogRulesJackpot(Context context) {
        this.context = context;
    }

    public DialogRulesJackpot() {
    }
    Dialog dialog;

    public DialogRulesJackpot show() {

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
        params.put("room_id","1");


        ApiRequest.Call_Api(context, Const.JackpotlastWinnerHistory, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp != null)
                {
                        Gson gson = new Gson();
                        Reader reader = new StringReader(resp);
                        final JackpotLastWinHistory jackpotWinHistory = gson.fromJson(reader, JackpotLastWinHistory.class);
                    ArrayList<JackpotLastWinHistory.Winner> ruleList = new ArrayList<>();
                    ruleList.addAll(getRuleValue());
                    if(jackpotWinHistory.getCode() == 200)
                        {
                            rulesModelArrayList.clear();
                            rulesModelArrayList.addAll(jackpotWinHistory.getWinners());
                            for (JackpotLastWinHistory.Winner model: ruleList) {
                                for (JackpotLastWinHistory.Winner winningModel: rulesModelArrayList) {
                                    int winning_value = Integer.parseInt(winningModel.getWinning());
                                    if(model.rule_value == winning_value)
                                    {
                                        model.wining_count++;
                                    }

                                }
                            }

                        }

                    recJackpotHistory.setAdapter(new jackpotHistoryAdapter(ruleList));
                }
            }
        });
    }

    private ArrayList<JackpotLastWinHistory.Winner> getRuleValue() {
        ArrayList<JackpotLastWinHistory.Winner> rulesModelList = new ArrayList<>();
        rulesModelList.add(new JackpotLastWinHistory.Winner(HIGH_CARD,1));
        rulesModelList.add(new JackpotLastWinHistory.Winner(PAIR,2));
        rulesModelList.add(new JackpotLastWinHistory.Winner(COLOR,3));
        rulesModelList.add(new JackpotLastWinHistory.Winner(SEQ,4));
        rulesModelList.add(new JackpotLastWinHistory.Winner(PURE_SEQ,5));
        rulesModelList.add(new JackpotLastWinHistory.Winner(SET,6));
        return rulesModelList;
    }

    String game_id;
    public DialogRulesJackpot setRoomid(String game_id) {
        this.game_id = game_id;
        return mInstance;
    }

    class jackpotHistoryAdapter extends RecyclerView.Adapter<jackpotHistoryAdapter.myHolder>{
        ArrayList<JackpotLastWinHistory.Winner> rulesModelArrayList;
        public jackpotHistoryAdapter(ArrayList<JackpotLastWinHistory.Winner> rulesModelArrayList) {
            this.rulesModelArrayList = rulesModelArrayList;
        }

        public void setDataList(ArrayList<JackpotLastWinHistory.Winner> rulesModelArrayList){
            this.rulesModelArrayList.addAll(rulesModelArrayList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jackpot_last_winhistory,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull myHolder holder, int position) {
            JackpotLastWinHistory.Winner winnermodel = rulesModelArrayList.get(position);
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

            public void bind(JackpotLastWinHistory.Winner winnermodel) {


                getTextView(R.id.tvTypes).setText(""+winnermodel.rule_type+": ");
                getTextView(R.id.tvValue).setText(""+winnermodel.wining_count);
            }

            TextView getTextView(int id){
               return  ((TextView) itemView.findViewById(id));
            }
        }


    }
}
