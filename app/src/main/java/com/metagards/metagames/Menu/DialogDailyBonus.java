package com.metagards.metagames.Menu;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;
import static com.metagards.metagames.Utils.Functions.SetBackgroundImageAsDisplaySize;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.Adapter.WelcomeRewardAdapter;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.WelcomeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogDailyBonus {

    Context context;

    private volatile static DialogDailyBonus mInstance;
    Callback callback;

    public static DialogDailyBonus getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogDailyBonus.class) {
                if (null == mInstance) {
                    mInstance = new DialogDailyBonus();
                }
            }
        }

        if(mInstance != null)
            mInstance.init(context);

        return mInstance;
    }

    /**
     * initialization of context, use only first time later it will use context again and again
     *
     * @param context app context: first time
     */
    public DialogDailyBonus init(Context context) {
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

    Dialog dialog;

    TextView txtnotfound;
    ImageView imgclosetop;

    private DialogDailyBonus initDialog(){
        dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_dailyreward);
        dialog.setTitle("Title...");

        RecyclerView Reward_rec;

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txtHeader = dialog.findViewById(R.id.txtheader);
        txtHeader.setText("Daily Rewards");

        RelativeLayout relativeLayout = dialog.findViewById(R.id.rlt_parent);
        SetBackgroundImageAsDisplaySize((Activity) context,relativeLayout,R.drawable.home_bg);


        imgclosetop = dialog.findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((View)dialog.findViewById(R.id.btnCollect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectWelcomeBonus(dialog);

            }
        });


        Reward_rec = dialog.findViewById(R.id.reward_rec);

        Homepage.itemClick itemClick = new Homepage.itemClick() {
            @Override
            public void OnDailyClick(String id) {

//                CollectWelcomeBonus();
            }
        };

        ArrayList<Integer> CoinsList = new ArrayList<>();

        CoinsList.add(R.drawable.day1);
        CoinsList.add(R.drawable.day2);
        CoinsList.add(R.drawable.day3);
        CoinsList.add(R.drawable.day4);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);

        GetUserWelcomeBonus(Reward_rec,itemClick,dialog,CoinsList);

        Reward_rec.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));

        return mInstance;
    }

    public DialogDailyBonus show() {
        dialog.show();
        Functions.setDialogParams(dialog);

        return mInstance;
    }

    public DialogDailyBonus dismiss(){
        dialog.dismiss();
        return mInstance;
    }


    public DialogDailyBonus returnCallback(Callback callback){
        this.callback = callback;
        return mInstance;
    }

    private void GetUserWelcomeBonus(final RecyclerView reward_rec, final Homepage.itemClick itemClick, Dialog dialog, final ArrayList<Integer> coinsList) {

        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<WelcomeModel> welcomeList = new ArrayList();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.WELCOME_BONUS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {

                                txtnotfound.setVisibility(View.GONE);

                                JSONArray welcome_bonusArray = jsonObject.getJSONArray("welcome_bonus");

                                for (int i = 0; i < welcome_bonusArray.length() ; i++) {
                                    JSONObject welcome_bonusObject = welcome_bonusArray.getJSONObject(i);

                                    WelcomeModel model= new WelcomeModel();
                                    model.setCoins(welcome_bonusObject.getString("coin"));
                                    model.setId(welcome_bonusObject.getString("id"));
                                    model.setGame_played(welcome_bonusObject.getString("game_played"));
                                    model.setCollected_days(jsonObject.getString("collected_days"));
                                    model.setDay(welcome_bonusObject.getString("id"));
                                    model.setImgcoins(coinsList.get(i));

                                    welcomeList.add(model);
                                }

                                WelcomeRewardAdapter welcomeRewardAdapter = new WelcomeRewardAdapter(context,welcomeList,itemClick);
                                reward_rec.setAdapter(welcomeRewardAdapter);

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(context, message);
                                }

                                txtnotfound.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

    }

    private void CollectWelcomeBonus(Dialog dialog) {

        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.COLLECT_BONUS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            String coins = "0";
                            if (jsonObject.has("coin"))
                                coins  = jsonObject.getString("coin");

                            if (code.equalsIgnoreCase("200")) {
                                dialog.dismiss();
                                callback.Responce("","",null);
                                WelcomeRewardAdapter.showWinDialog(context,coins);

                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(context, message);

                                }
                            }

                            rlt_progress.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rlt_progress.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

    }

}