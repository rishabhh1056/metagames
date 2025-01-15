package com.metagards.metagames.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.account.LoginScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splashscreen extends BaseActivity{
    private static final String MY_PREFS_NAME = "Login_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


//        Mint.initAndStartSession(this.getApplication(), "cc552ad8");
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        RelativeLayout relativeLayout = findViewById(R.id.rlt_parent);
//        SetBackgroundImageAsDisplaySize(this,relativeLayout,R.drawable.splash);
        SharePref.getInstance().init(getApplicationContext());
//        SharePref.getInstance().putBoolean(SharePref.isDragonTigerVisible, true);
//        SharePref.getInstance().putBoolean(SharePref.isTeenpattiVisible, true);
//        SharePref.getInstance().putBoolean(SharePref.isPrivateVisible, true);
//        SharePref.getInstance().putBoolean(SharePref.isCustomVisible, true);
//        SharePref.getInstance().putBoolean(SharePref.isRummyVisible, true);
//        SharePref.getInstance().putBoolean(SharePref.isAndharBaharVisible, true);
        SharePref.getInstance().putBoolean(SharePref.isLoginWithPassword, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String user_id = prefs.getString("user_id", "");
                    if (user_id.trim().length() > 0) {
                        startActivity(new Intent(Splashscreen.this, Homepage.class));
                    } else {
                        startActivity(new Intent(Splashscreen.this, LoginScreen.class));
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Check_game_on_off();
    }

    public void Check_game_on_off() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.game_on_off,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("game_response_", "onResponse: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equalsIgnoreCase("200")) {
//                                Functions.showToast(Splashscreen.this, message);
                                JSONObject game_setting = jsonObject.getJSONObject("game_setting");
                                String teen_patti = game_setting.getString("teen_patti");
                                if (teen_patti.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isTeenpattiVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isTeenpattiVisible, false);
                                }
                                String dragon_tiger = game_setting.getString("dragon_tiger");
                                if (dragon_tiger.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isDragonTigerVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isDragonTigerVisible, false);
                                }
                                String andar_bahar = game_setting.getString("andar_bahar");
                                if (andar_bahar.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isAndharBaharVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isAndharBaharVisible, false);
                                }
                                String point_rummy = game_setting.getString("point_rummy");
                                if (point_rummy.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isPointRummyVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isPointRummyVisible, false);
                                }
                                String private_rummy = game_setting.getString("private_rummy");
                                if (private_rummy.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isPrivateRummyVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isPrivateRummyVisible, false);
                                }
                                String pool_rummy = game_setting.getString("pool_rummy");
                                if (pool_rummy.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isPoolRummyVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isPoolRummyVisible, false);
                                }
                                String deal_rummy = game_setting.getString("deal_rummy");
                                if (deal_rummy.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isDealRummyVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isDealRummyVisible, false);
                                }
                                String private_table = game_setting.getString("private_table");
                                if (private_table.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isPrivateVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isPrivateVisible, false);
                                }
                                String custom_boot = game_setting.getString("custom_boot");
                                if (custom_boot.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isCustomVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isCustomVisible, false);
                                }
                                String seven_up_down = game_setting.getString("seven_up_down");
                                if (seven_up_down.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isSevenUpVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isSevenUpVisible, false);
                                }
                                String car_roulette = game_setting.getString("car_roulette");
                                if (car_roulette.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isCarRouletteVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isCarRouletteVisible, false);
                                }
                                String jackpot_teen_patti = game_setting.getString("jackpot_teen_patti");
                                if (jackpot_teen_patti.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isHomeJackpotVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isHomeJackpotVisible, false);
                                }
                                String animal_roulette = game_setting.getString("animal_roulette");
                                if (animal_roulette.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isAnimalRouletteVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isAnimalRouletteVisible, false);
                                }
                                String color_prediction = game_setting.getString("color_prediction");
                                if (color_prediction.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isColorPredictionVisible, true);
                                    SharePref.getInstance().putBoolean(SharePref.isColorPrediction1Visible, false);
                                    SharePref.getInstance().putBoolean(SharePref.isColorPrediction3Visible, false);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isColorPredictionVisible, false);
                                    SharePref.getInstance().putBoolean(SharePref.isColorPrediction1Visible, false);
                                    SharePref.getInstance().putBoolean(SharePref.isColorPrediction3Visible, false);
                                }
                                String poker = game_setting.getString("poker");
                                if (poker.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isPokerVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isPokerVisible, false);
                                }
                                String head_tails = game_setting.getString("head_tails");
                                if (head_tails.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isHeadTailVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isHeadTailVisible, false);
                                }
                                String red_vs_black = game_setting.getString("red_vs_black");
                                if (red_vs_black.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isRedBlackVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isRedBlackVisible, false);
                                }
                                String ludo = game_setting.getString("ludo_local");
                                if (ludo.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isLudoVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isLudoVisible, false);
                                }
                                String ludo_online = game_setting.getString("ludo_online");
                                if (ludo_online.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isLudoOnlineVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isLudoOnlineVisible, false);
                                }
                                String ludo_computer = game_setting.getString("ludo_computer");
                                if (ludo_computer.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isLudoComputerVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isLudoComputerVisible, false);
                                }
                                String bacarate = game_setting.getString("bacarate");
                                if (bacarate.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isBacarateVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isBacarateVisible, false);
                                }
                                String jhandi_munda = game_setting.getString("jhandi_munda");
                                if (jhandi_munda.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isJhandi_MundaVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isJhandi_MundaVisible, false);
                                }
                                String roulette = game_setting.getString("roulette");
                                if (roulette.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isRouletteVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isRouletteVisible, false);
                                }
                                String tour = game_setting.getString("tournament_rummy");
                                if (tour.equals("1")){
                                    SharePref.getInstance().putBoolean(SharePref.isTournamentVisible, true);
                                }else{
                                    SharePref.getInstance().putBoolean(SharePref.isTournamentVisible, false);
                                }
                            } else {
                                Functions.showToast(Splashscreen.this, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(Splashscreen.this, "Something went wrong");
            }
        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                params.put("user_id", prefs.getString("user_id", ""));
//                params.put("token", prefs.getString("token", ""));
//                Log.d("paremter_java_kyc", "getParams: " + params);
//                return params;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(Splashscreen.this).add(stringRequest);
    }

}
