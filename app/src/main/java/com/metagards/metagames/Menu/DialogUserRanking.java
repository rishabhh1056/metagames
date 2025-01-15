package com.metagards.metagames.Menu;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.domain.UserCategoryResponse;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DialogUserRanking {

    Context context;
    Callback callback;
    private static DialogUserRanking mInstance;


    public static DialogUserRanking getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogUserRanking.class) {
                if (null == mInstance) {
                    mInstance = new DialogUserRanking(context);
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
    public DialogUserRanking init(Context context) {
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
    ArrayList<UserCategoryResponse.UserCategory> userDetailsList = new ArrayList();
    private DialogUserRanking initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_user_ranking);

        tvHeader = dialog.findViewById(R.id.txtheader);
        tvHeader.setText("User Ranking Category");
        recJackpotHistory = dialog.findViewById(R.id.recJackpotHistory);
        recJackpotHistory.setLayoutManager(new LinearLayoutManager(context));
        getUserRankingCategory();

        dialog.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return mInstance;
    }

    public DialogUserRanking(Context context) {
        this.context = context;
    }

    public DialogUserRanking() {
    }
    Dialog dialog;

    public DialogUserRanking show() {

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

    public DialogUserRanking setCallback(Callback callback){
        this.callback = callback;
        return this;
    }

    void getUserRankingCategory(){
        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",prefs.getString("user_id", ""));
        params.put("token",""+ SharePref.getAuthorization());


        ApiRequest.Call_Api(context, Const.USER_CATEGORY_LIST, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp != null)
                {
                    Gson gson = new Gson();
                    Reader reader = new StringReader(resp);
                    final UserCategoryResponse jackpotWinHistory = gson.fromJson(reader, UserCategoryResponse.class);

                    if(jackpotWinHistory.getCode() == 200)
                    {
                        userDetailsList.clear();
                        userDetailsList.addAll(jackpotWinHistory.getUserCategory());
                    }

                    recJackpotHistory.setAdapter(new jackpotHistoryAdapter(userDetailsList));
                }
            }
        });
    }

    class jackpotHistoryAdapter extends RecyclerView.Adapter<jackpotHistoryAdapter.myHolder>{
        ArrayList<UserCategoryResponse.UserCategory> rulesModelArrayList;
        public jackpotHistoryAdapter(ArrayList<UserCategoryResponse.UserCategory> rulesModelArrayList) {
            this.rulesModelArrayList = rulesModelArrayList;
        }

        public void setDataList(ArrayList<UserCategoryResponse.UserCategory> rulesModelArrayList){
            this.rulesModelArrayList.addAll(rulesModelArrayList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_ranking,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull myHolder holder, int position) {
            UserCategoryResponse.UserCategory UserCategoryResponse = rulesModelArrayList.get(position);
            if(UserCategoryResponse != null)
                holder.bind(UserCategoryResponse);
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
            public void bind(UserCategoryResponse.UserCategory UserCategoryResponse) {
                getTextView(R.id.tvSerial)
                        .setText(Functions.decimal_to_Roman(getAdapterPosition()+1));

                getTextView(R.id.tvRanking)
                        .setText(UserCategoryResponse.getName());

                getTextView(R.id.tvPrice)
                        .setText(UserCategoryResponse.getAmount());

//                manageView();
            }

            private void manageView() {
                View lastItemView = itemView;
                lastItemView.post(new Runnable() {
                    @Override
                    public void run() {
                        int min_height = (int) getAbsoluteAdapterPosition() * 50;
                        Functions.LOGD("DialogUserRank","min_height : +"+min_height);
                        Functions.setMargins(lastItemView,0,0,0,min_height);
                    }
                });
            }

            TextView getTextView(int id){
                return  ((TextView) itemView.findViewById(id));
            }
        }


    }
}
