package com.metagards.metagames.RedeemCoins;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.model.ChipsBuyModel;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RedeemActivity extends BaseActivity {
    private static final String MY_PREFS_NAME = "Login_data";
    //ImageView img_back;
    ArrayList<ChipsBuyModel> historyModelArrayList;
    RedeemAdapter historyAdapter;
    RecyclerView rec_history;
    ProgressDialog progressDialog;
    LinearLayout linear_no_history;
    ImageView imgback,imaprofile;
    Context context;
    TextView tvRedeemWallet,txtwallet,txtproname,txtbonus,txtwin,txtunuti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        // img_back=findViewById(R.id.img_back);
        context = this;
        rec_history = findViewById(R.id.rec_history);
        linear_no_history = findViewById(R.id.linear_no_history);
        imgback = findViewById(R.id.imgback);

        txtproname= findViewById(R.id.txtproname);
        txtwallet= findViewById(R.id.txtwallet);
        txtbonus= findViewById(R.id.txtbonus);
        txtwin= findViewById(R.id.txtwin);
        txtunuti=findViewById(R.id.txtunuti);
        imaprofile= findViewById(R.id.imaprofile);
//        tvRedeemWallet= findViewById(R.id.tvRedeemWallet);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Picasso.get().load(Const.IMGAE_PATH + prefs.getString("profile_pic","")).into(imaprofile);
//        txtproname.setText(prefs.getString("name",""));
        txtwallet.setText("" + prefs.getString("wallet",""));
        Log.e("walletnew","wallet"+txtwallet);
        txtbonus.setText("" +prefs.getString("bonus_wallet",""));
        Log.e("bonusnew","bonus"+txtbonus);
        txtwin.setText("" +prefs.getString("winning_wallet",""));
        txtunuti.setText("" +prefs.getString("unutilized_wallet",""));
//        tvRedeemWallet.setText("Amount can be Redeem "+ Variables.CURRENCY_SYMBOL + prefs.getString("winning_wallet",""));

        getChipsList();

        rec_history.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void getChipsList() {
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GET_Redeem_List,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RedeemActivity",""+Const.GET_Redeem_List+" \n"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equals("200")) {
                                progressDialog.dismiss();
                                JSONArray jsonArray = jsonObject.getJSONArray("List");
                                historyModelArrayList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ChipsBuyModel model = new ChipsBuyModel();
                                    model.setId(jsonObject1.getString("id"));
                                    model.title = jsonObject1.getString("title");
                                    model.Image = jsonObject1.getString("img");
                                    model.setProname(jsonObject1.getString("coin"));
                                    model.setAmount(jsonObject1.getString("amount"));
                                    // model.setTicket_id(jsonObject1.getString("desc"));

                                    historyModelArrayList.add(model);
                                }

                                historyAdapter = new RedeemAdapter(RedeemActivity.this, historyModelArrayList);
                                rec_history.setAdapter(historyAdapter);
                            } else {
                                linear_no_history.setVisibility(View.VISIBLE);
//                                 Funtions.showToast(context, ""+message);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                HashMap<String, String> params = new HashMap<>();
                params.put("token", prefs.getString("token", ""));
                params.put("user_id", prefs.getString("user_id", ""));
                //params.put("user_id", SharedPref.getVal(HistoryActivity.this,SharedPref.id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RedeemActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}