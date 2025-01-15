package com.metagards.metagames.RedeemCoins;

import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WithdrawalList extends AppCompatActivity {

    TextView txtheader;
    ImageView imgclosetop;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_list);

        progressDialog = new ProgressDialog(WithdrawalList.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        txtheader = findViewById(R.id.txtheader);
        txtheader.setText("Withdrawal Logs");

        imgclosetop = findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WithdrawalList.this, RedeemActivity.class));
            }
        });

//        getList();
    }

    public void onResume(){
        super.onResume();
        getList();
    }

    LinearLayout lnrcancelist;
    public void showList(JSONArray last_bet) throws JSONException {
        Log.d("json_list", String.valueOf(last_bet));
        lnrcancelist = findViewById(R.id.lnrlastView);
        lnrcancelist.removeAllViews();
        for (int i = 0; i < last_bet.length() ; i++) {

            String id = last_bet.getJSONObject(i).getString("id");
            String coin = last_bet.getJSONObject(i).getString("coin");
            String status = last_bet.getJSONObject(i).getString("status");
            String created_date = last_bet.getJSONObject(i).getString("created_date");

            addLastWinBet(id, coin, status, created_date);
        }
    }

    int pos = 1;
    private void addLastWinBet(String id, String coin, String status, String date) {
        View view = LayoutInflater.from(WithdrawalList.this).inflate(R.layout.view_color_lastwin_list,null);
        TextView tvFiled1 = view.findViewById(R.id.tvFiled1);
        TextView tvFiled2 = view.findViewById(R.id.tvFiled2);
        TextView tvFiled3 = view.findViewById(R.id.tvFiled3);
        TextView tvFiled4 = view.findViewById(R.id.tvFiled4);

        tvFiled1.setText(""+pos++);
        tvFiled2.setText(""+String.valueOf(coin));
        tvFiled3.setText(""+String.valueOf(status));
        tvFiled4.setText(""+date);
        if (status.equals("0")){
            tvFiled3.setText("Pending");
        }
        if (status.equals("1")){
            tvFiled3.setText("Approved");
        }
        if (status.equals("2")){
            tvFiled3.setText("Rejected");
        }

        lnrcancelist.addView(view);
    }

    private void getList() {      //list
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_WITHDRAWAL_LOGS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK_LIST", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                progressDialog.dismiss();
                                JSONArray data = jsonObject.getJSONArray("data");
                                int data_ = data.length();
                                if (data_==0){
                                    Toast.makeText(WithdrawalList.this, "No Logs found!", Toast.LENGTH_LONG).show();
                                }
                                showList(data);

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(WithdrawalList.this, message);
                                    progressDialog.dismiss();
                                }

                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(WithdrawalList.this, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = WithdrawalList.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java_with_list", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(WithdrawalList.this).add(stringRequest);

    }
}