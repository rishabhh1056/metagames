package com.metagards.metagames.Details.Menu;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Details.Adapter.ReferalUserAdapter;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogReferralUser {

    Dialog alert;
    Context context;
    TextView nofound;
    ProgressBar progressBar;
    RecyclerView rec_winning;
    ReferalUserAdapter myWinningAdapte;
    SharedPreferences prefs;
    final ArrayList<com.metagards.metagames.Details.Menu.ReferralModel> referralModels;
    public DialogReferralUser(Context context) {
        this.context = context;
        alert = Functions.DialogInstance(context);
        alert.setContentView(R.layout.dialog_referral);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTitle = alert.findViewById(R.id.txtheader);
        tvTitle.setText("Referral History");
        nofound = alert.findViewById(R.id.txtnotfound);
        progressBar = alert.findViewById(R.id.progressBar);
        rec_winning = alert.findViewById(R.id.rec_winning);
        rec_winning.setLayoutManager(new LinearLayoutManager(context));


        prefs  = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        referralModels = new ArrayList();
        myWinningAdapte = new ReferalUserAdapter(context,referralModels);

        myWinningAdapte.onItemSelectListener(new OnItemClickListener() {
            @Override
            public void Response(View v, int position, Object object) {
                Log.e("TestDialog","working");
                String useid =  referralModels.get(position).referred_user_id;
                CALL_API_GET_LIST(useid);


            }
        });

       rec_winning.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alert.setContentView(R.layout.dialog_referral);

           }
       });

//        myWinningAdapte.onI


        alert.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        SwipeRefreshLayout swiperefresh = alert.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String userid= prefs.getString("user_id", "");
                CALL_API_GET_LIST(userid);
                swiperefresh.setRefreshing(false);
            }
        });

        String userid= prefs.getString("user_id", "");
        CALL_API_GET_LIST(userid);
    }

    public void show(){
        alert.show();
        Functions.setDialogParams(alert);
    }
    public void dismiss(){alert.dismiss();}

    private void CALL_API_GET_LIST(String user_id){

        NoDataVisible(false);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);

        ApiRequest.Call_Api(context, Const.USER_REFER, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    referralModels.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    Log.e("ref","ref");
                    if (code.equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("refferearnlog");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            com.metagards.metagames.Details.Menu.ReferralModel model = new com.metagards.metagames.Details.Menu.ReferralModel();
                            model.setId(jsonObject1.optString("id"));
                            model.setCoin(jsonObject1.optString("coin"));
                            model.setCount(jsonObject1.optString("refer_count"));

                            model.setName(jsonObject1.optString("name"));
                            model.setReferred_user_id(jsonObject1.optString("referral_code"));
                            model.setUpdated_date(jsonObject1.optString("added_date"));
                            model.setEmail(jsonObject1.optString("email"));


                            referralModels.add(model);
                        }

                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(referralModels.size() <= 0)
                    NoDataVisible(true);

                rec_winning.setAdapter(myWinningAdapte);

                HideProgressBar(true);
            }
        });
    }

    private void NoDataVisible(boolean visible){

        nofound.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void HideProgressBar(boolean gone){
        progressBar.setVisibility(!gone ? View.VISIBLE : View.GONE);
    }

}
