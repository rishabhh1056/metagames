package com.metagards.metagames;

import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.metagards.metagames.RedeemCoins.RedeemActivity;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Statement extends AppCompatActivity {

    TextView txtheader;
    ImageView imgclosetop;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        progressDialog = new ProgressDialog(Statement.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        txtheader = findViewById(R.id.txtheader);
        txtheader.setText("Statement");

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
                startActivity(new Intent(Statement.this, RedeemActivity.class));
            }
        });

//        getList();
    }

    public void onResume(){
        super.onResume();
        getList();
    }

    double wallet=0;
    LinearLayout lnrcancelist;
    public void showList(JSONArray last_bet) throws JSONException {
        Log.d("json_list", String.valueOf(last_bet));
        lnrcancelist = findViewById(R.id.lnrlastView);
        lnrcancelist.removeAllViews();

        JSONObject jsonObject = last_bet.getJSONObject(0);
        wallet = Double.parseDouble(jsonObject.getString("user_wallet"));
        Log.d("user_wallet_", String.valueOf(wallet));

        for (int i = 0; i < last_bet.length() ; i++) {

            String game = last_bet.getJSONObject(i).getString("game");
            String reff_id = last_bet.getJSONObject(i).getString("reff_id");
            double user_wallet = Double.parseDouble(last_bet.getJSONObject(i).getString("user_wallet"));
            double user_amt = Double.parseDouble(last_bet.getJSONObject(i).getString("user_amount"));
            double amt = Double.parseDouble(last_bet.getJSONObject(i).getString("amount"));
            String added_date = last_bet.getJSONObject(i).getString("added_date");
            String total = last_bet.getJSONObject(i).getString("total");
            String bracket_amount = last_bet.getJSONObject(i).getString("bracket_amount");

//            wallet =  Double.parseDouble(last_bet.getJSONObject(i).getString("user_wallet"));

//            double brack_val = (user_amt - amt);
//            Log.d("brack_val", String.valueOf(brack_val));
//            double first_val = wallet - brack_val;
//            Log.d("first_val_", String.valueOf(first_val));

            addLastWinBet("", game, reff_id, String.valueOf(total),bracket_amount, added_date);
        }
    }

    int pos = 1;
    private void addLastWinBet(String id, String game, String ref_id, String total, String brack_val, String date) {
        View view = LayoutInflater.from(Statement.this).inflate(R.layout.view_color_statement_list,null);
        TextView tvFiled1 = view.findViewById(R.id.tvFiled1);
        TextView tvFiled2 = view.findViewById(R.id.tvFiled2);
        TextView tvFiled3 = view.findViewById(R.id.tvFiled3);
        TextView tvFiled4 = view.findViewById(R.id.tvFiled4);
        TextView tvFiled5 = view.findViewById(R.id.tvFiled5);
        TextView tvFiled6 = view.findViewById(R.id.tvFiled6);

        tvFiled1.setText(""+pos++);
        tvFiled2.setText(""+String.valueOf(game));
        tvFiled3.setText("#"+String.valueOf(ref_id));
        tvFiled4.setText("₹ "+wallet);

//        double final_val = (wallet) - (Double.parseDouble(brack_val));
        if (brack_val.contains("-")){
            tvFiled6.setTextColor(Color.parseColor("#D50000"));
            tvFiled6.setText("("+brack_val+")");
        }else{
            tvFiled6.setText("("+"+"+brack_val+")");
        }
        tvFiled4.setText("₹ "+total);
        tvFiled5.setText(""+date);

        lnrcancelist.addView(view);
    }

    private void getList() {      //list
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_WALLET_HISTORY,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK_LIST_REPORT", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                progressDialog.dismiss();
                                JSONArray data = jsonObject.getJSONArray("GameLog");
                                int data_ = data.length();
                                if (data_==0){
                                    Toast.makeText(Statement.this, "No Logs found!", Toast.LENGTH_LONG).show();
                                }
                                showList(data);

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(Statement.this, message);
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
                Functions.showToast(Statement.this, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = Statement.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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

        Volley.newRequestQueue(Statement.this).add(stringRequest);

    }
}