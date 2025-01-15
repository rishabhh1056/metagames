package com.metagards.metagames.Details.Menu;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.MyAccountDetails.MyWinningAdapte;
import com.metagards.metagames.MyAccountDetails.UserRedeemHistoryAdapter;
import com.metagards.metagames.R;
import com.metagards.metagames.RedeemCoins.RedeemModel;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DialogRedeemHistory {

    Dialog alert;
    Context context;
    TextView nofound;
    ProgressBar progressBar;
    RecyclerView rec_winning;
    MyWinningAdapte myWinningAdapte;

    public DialogRedeemHistory(Context context) {
        this.context = context;
        alert = Functions.DialogInstance(context);
        alert.setContentView(R.layout.dialog_historyredeem);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nofound = alert.findViewById(R.id.txtnotfound);
        progressBar = alert.findViewById(R.id.progressBar);
        rec_winning = alert.findViewById(R.id.rec_winning);
        rec_winning.setLayoutManager(new LinearLayoutManager(context));

        TextView txtHeader = alert.findViewById(R.id.txtheader);
        txtHeader.setText("Redeem History");

        View lnrStatus = alert.findViewById(R.id.lnrStatus);
        lnrStatus.setVisibility(View.VISIBLE);

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
                CALL_API_GET_LIST();
                swiperefresh.setRefreshing(false);
            }
        });


        CALL_API_GET_LIST();
    }

    public void show(){
        alert.show();
        Functions.setDialogParams(alert);
    }
    public void dismiss(){alert.dismiss();}

    private void CALL_API_GET_LIST(){

        NoDataVisible(false);
        final ArrayList<RedeemModel> redeemModelArrayList = new ArrayList();

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",prefs.getString("user_id", ""));

        ApiRequest.Call_Api(context, Const.USER_Redeem_History_LIST, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                // progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("List");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            RedeemModel model = new RedeemModel();
                            model.setId(jsonObject1.getString("id"));
                            model.setCoin(jsonObject1.getString("coin"));
                            model.setMobile(jsonObject1.getString("mobile"));
                            model.setUser_name(jsonObject1.getString("user_name"));
                            model.setUser_mobile(jsonObject1.getString("user_mobile"));
                            model.setStatus(jsonObject1.getString("status"));
                            model.setUpdated_date(jsonObject1.getString("updated_date"));
                            model.ViewType = RedeemModel.REDEEM_LIST;


                            redeemModelArrayList.add(model);
                        }

                        Collections.reverse(redeemModelArrayList);


                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                }

                if(redeemModelArrayList.size() <= 0)
                    NoDataVisible(true);

                UserRedeemHistoryAdapter userWinnerAdapter = new UserRedeemHistoryAdapter(context, redeemModelArrayList);
                rec_winning.setAdapter(userWinnerAdapter);


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
