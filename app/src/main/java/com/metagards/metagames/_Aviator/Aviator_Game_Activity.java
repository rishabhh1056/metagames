package com.metagards.metagames._Aviator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Aviator_Game_Activity extends AppCompatActivity {
    Activity context;
    ImageView img_gif, img_start_game;
    TextView txt_timer, txt, txt_exit, txt_exit_value, txt_inner_timer, txt_wallet_amt, txt_username;
    float gif_Count = 10;
    EditText txt_amount;
    LinearLayout lnr_pay;
    Button btn_start;
    int miliseconds;
    int period;
    boolean gif_run = false;
    String graphCall = "1.80";
    long startTime = 0;
    Timer timer;
    String timerStaus = "";
    double timervalue = 0;
    String redeem_value = "";
    String time = "1.00";
    String game_id = "";
    String get_socket_responce = "";
    String final_game_id = "1111";
    String redeem_gameid = "";
    //private static final String URL = "http://64.227.186.5:3002";
    private Socket mSocket;
    String wallet_amount = "",user_id;

    {
        try {
            mSocket = IO.socket(Const.AVIATOR_SOCKET_IP + "aviator");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aviator);
        context = this;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        mSocket.on("connect", onConnect);
        mSocket.on("connect_error", onConnectError);
        mSocket.connect();

        SharedPreferences prefs = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");
        Log.e("user_id",user_id);

        img_gif = findViewById(R.id.gif_roket);
        txt_inner_timer = findViewById(R.id.txt_inner_timer);
        img_start_game = findViewById(R.id.img_start_game);
        txt_amount = findViewById(R.id.txt_amount);
        txt_timer = findViewById(R.id.txt_timer);
        txt = findViewById(R.id.txt);
        txt_exit = findViewById(R.id.txt_exit);
        txt_exit_value = findViewById(R.id.txt_exit_value);
        lnr_pay = findViewById(R.id.lnr_pay);
        btn_start = findViewById(R.id.btn_start);
        txt_wallet_amt = findViewById(R.id.txt_wallet_amt);
        txt_username = findViewById(R.id.txt_username);

        txt_exit_value.setVisibility(View.GONE);
        txt_exit.setVisibility(View.GONE);

        mSocket.on("Game", onNewMessage);
        mSocket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("Socket connect_", "");
                        ShowGiftimer(15000, "one");
                    }
                });

            }
        });

        Profile_details();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Redeem /bet
                if (btn_start.getText().toString().equals("Redeem")) {
                    redeem_value = txt_inner_timer.getText().toString();
                    Redeem_amount(redeem_value);
                    btn_start.setText("Bet");

                } else if (btn_start.getText().toString().equals("Bet")) {
                    if (img_start_game.getVisibility() == View.VISIBLE) {
                        Betting_amount(txt_amount.getText().toString(), "timer on");
                        btn_start.setText("wait");
                    } else {
                        Betting_amount(txt_amount.getText().toString(), "timer on");
                        btn_start.setText("wait");
                    }
                } else {
                    Log.d("start ", " " + miliseconds + "/" + period);
                }
            }
        });

    }

    public void pluseValue(View view) {

        gif_Count = (float) (Float.parseFloat(txt_amount.getText().toString()) + 10);
        txt_amount.setText(new DecimalFormat("##.##").format(gif_Count));

    }

    public void minusValue(View view) {
        if (Integer.parseInt(txt_amount.getText().toString()) != 10) {
            gif_Count = (float) (Float.parseFloat(txt_amount.getText().toString()) - 10);
            txt_amount.setText(new DecimalFormat("##.##").format(gif_Count));
        }
    }

//    public void gif_change_by_range(String gif_Count_str) {
//
//
//        if (img_start_game.getVisibility() == View.GONE && btn_start.getText().equals("wait")) {
//            btn_start.setText("Redeem");
//        }
//
//        img_gif.setVisibility(View.VISIBLE);
//        float gif_Count = Float.parseFloat(time);
//        final_game_id = game_id;
//        Log.d("Socket a git start  ", time + "/   " + game_id);
//
//        if (gif_Count >= 1.00 && gif_Count <= 1.20) {
//            img_gif.setImageResource(R.drawable.rocket_a);
//            miliseconds = 2700;
//            period = 125;
//            game_timerStart(miliseconds, period);// timer backend start
//            reset(miliseconds, "1.20");
//
//        } else if (gif_Count > 1.20 && gif_Count <= 1.40) {
//            img_gif.setImageResource(R.drawable.rocket_b);
//            miliseconds = 2600;
//            period = 63;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "1.40");
//        } else if (gif_Count > 1.40 && gif_Count <= 1.60) {
//            img_gif.setImageResource(R.drawable.rocket_c);
//            miliseconds = 3750;
//            period = 61;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "1.60");
//        } else if (gif_Count > 1.60 && gif_Count <= 1.80) {
//            img_gif.setImageResource(R.drawable.rocket_d);
//            miliseconds = 3600;
//            period = 44;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "1.80");
//        } else if (gif_Count > 1.80 && gif_Count <= 2.00) {
//            img_gif.setImageResource(R.drawable.rocket_e);
//            miliseconds = 4550;//4600
//            period = 45;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "2.00");
//        } else if (gif_Count > 2.00 && gif_Count <= 2.20) {
//            img_gif.setImageResource(R.drawable.rocket_f);
//            miliseconds = 5450;
//            period = 45;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "2.20");
//        } else if (gif_Count > 2.20 && gif_Count <= 2.40) {
//            img_gif.setImageResource(R.drawable.rocket_g);
//            miliseconds = 6500;
//            period = 46;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "2.40");
//        } else if (gif_Count > 2.40 && gif_Count <= 2.60) {
//            img_gif.setImageResource(R.drawable.rocket_h);
//            miliseconds = 7250;//7200
//            period = 45;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "2.60");
//        } else if (gif_Count > 2.60 && gif_Count <= 2.80) {
//            img_gif.setImageResource(R.drawable.rocket_i);
//            miliseconds = 8180;//8200
//            period = 45;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "2.80");
//        } else if (gif_Count > 2.80 && gif_Count <= 3.00) {
//            img_gif.setImageResource(R.drawable.rocket_j);
//            miliseconds = 9090;
//            period = 45;
//            game_timerStart(miliseconds, period);
//            reset(miliseconds, "3.00");
//        }
//    }


    public void gif_change_by_range(String gif_Count_str) {


        if (img_start_game.getVisibility() == View.GONE && btn_start.getText().equals("wait")) {
            btn_start.setText("Redeem");
        }

        img_gif.setVisibility(View.VISIBLE);
        float gif_Count = Float.parseFloat(time);
        final_game_id = game_id;
        Log.d("Socket a git start  ", time + "/   " + game_id);

        if (gif_Count >= 1.00 && gif_Count <= 1.10) {
            img_gif.setImageResource(R.drawable.rocket_0);
            miliseconds = 700;
            period = 150;
            game_timerStart(miliseconds, period);// timer backend start
            reset(miliseconds, "1.10");
        } else if (gif_Count >= 1.11 && gif_Count <= 1.20) {
            img_gif.setImageResource(R.drawable.rocket_00);
            miliseconds = 900;
            period = 70;
            game_timerStart(miliseconds, period);// timer backend start
            reset(miliseconds, "1.20");
        }
//        if (gif_Count >= 1.00 && gif_Count <= 1.20) {
//            img_gif.setImageResource(R.drawable.rocket_a);
//            miliseconds = 2700;
//            period = 125;
//            game_timerStart(miliseconds, period);// timer backend start
//            reset(miliseconds, "1.20");
//
//        }
        else if (gif_Count > 1.20 && gif_Count <= 1.40) {
            img_gif.setImageResource(R.drawable.rocket_b);
            miliseconds = 2600;
            period = 63;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "1.40");
        } else if (gif_Count > 1.40 && gif_Count <= 1.60) {
            img_gif.setImageResource(R.drawable.rocket_c);
            miliseconds = 3750;
            period = 61;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "1.60");
        } else if (gif_Count > 1.60 && gif_Count <= 1.80) {
            img_gif.setImageResource(R.drawable.rocket_d);
            miliseconds = 3600;
            period = 44;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "1.80");
        } else if (gif_Count > 1.80 && gif_Count <= 2.00) {
            img_gif.setImageResource(R.drawable.rocket_e);
            miliseconds = 4550;//4600
            period = 45;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "2.00");
        } else if (gif_Count > 2.00 && gif_Count <= 2.20) {
            img_gif.setImageResource(R.drawable.rocket_f);
            miliseconds = 5450;
            period = 45;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "2.20");
        } else if (gif_Count > 2.20 && gif_Count <= 2.40) {
            img_gif.setImageResource(R.drawable.rocket_g);
            miliseconds = 6500;
            period = 46;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "2.40");
        } else if (gif_Count > 2.40 && gif_Count <= 2.60) {
            img_gif.setImageResource(R.drawable.rocket_h);
            miliseconds = 7250;//7200
            period = 45;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "2.60");
        } else if (gif_Count > 2.60 && gif_Count <= 2.80) {
            img_gif.setImageResource(R.drawable.rocket_i);
            miliseconds = 8180;//8200
            period = 45;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "2.80");
        } else if (gif_Count > 2.80 && gif_Count <= 3.00) {
            img_gif.setImageResource(R.drawable.rocket_j);
            miliseconds = 9090;
            period = 45;
            game_timerStart(miliseconds, period);
            reset(miliseconds, "3.00");
        }
    }

    public void reset(int YOUR_TIME_IN_MILISECONDS, String exit_value) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (timer != null) {// Stop timer
                    timer.cancel();
                    timer = null;
                    timervalue = 0;
                    miliseconds = 0;
                    period = 0;
//                  socket param clear
//                    time = "";
//                    game_id = "";

                    if (btn_start.getText().toString().equals("Redeem")) {
                        btn_start.setText("Bet");
                        txt_amount.setText("10");
                    }
                    if (btn_start.getText().toString().equals("Bet")) {
                        txt_amount.setText("10");
                    }
                    txt_inner_timer.setText("");
                    Log.d("start stop timer", " " + miliseconds + "/" + period);
                }

                if (YOUR_TIME_IN_MILISECONDS != 0) {
                    txt_exit.setVisibility(View.VISIBLE);
                    txt_exit.setText("FLEW AWAY !");
                    txt_exit_value.setVisibility(View.VISIBLE);
                    txt_exit_value.setText(exit_value);
                    img_gif.setVisibility(View.GONE);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ShowGiftimer(YOUR_TIME_IN_MILISECONDS, "");
                    }
                }, 2000);

            }
        }, YOUR_TIME_IN_MILISECONDS);


    }

    public void ShowGiftimer(int YOUR_TIME_IN_MILISECONDS, String type) {
//        call socket event
        int gifTimer;
        if (type.equals("one")) {
            gifTimer = 15000;
        } else {
            gifTimer = 12800 - YOUR_TIME_IN_MILISECONDS;
        }
        CountDownTimer counttimerforstartgame = new CountDownTimer(gifTimer, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                txt_timer.setText("" + millisUntilFinished / 1000 + " ");
                txt_timer.setVisibility(View.VISIBLE);
                img_start_game.setVisibility(View.VISIBLE);
                txt_exit.setVisibility(View.GONE);
                txt_exit_value.setVisibility(View.GONE);
                txt.setVisibility(View.VISIBLE);
                img_gif.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                txt_timer.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
                img_start_game.setVisibility(View.GONE);
                gif_change_by_range(time);
            }
        };
        counttimerforstartgame.start();
    }

    private void game_timerStart(int miliseconds, int period) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            double count = 1;
            double plus = 0.01;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timervalue = timervalue + period;
                        if (timervalue < miliseconds) {
                            txt_inner_timer.setText(String.format("%.2f", count));
                            count = count + plus;
                        }
                    }
                });
            }
        }, 100, period);
    }

    //     SOCKET CODE

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    get_socket_responce = "first_time";
                    JSONObject data = (JSONObject) args[0];
                    try {
                        time = data.getString("time");
                        game_id = data.getString("game_id");
                        Log.d("RES_Socketrunoutput", time + "/   " + game_id);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }
    };

//     ------------API CALL-------------------

    private void Betting_amount(String amount, String timerStatus) {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.showDialog(Aviator_Game_Activity.this);
        Log.d("RES_Socketbet", "API call: " + time + "/" + game_id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.AVIATOR_GAME_BETTING, new Response.Listener<String>() {
            private Object AdapterSlider;

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("RES_Bettingresponce", "" + jsonObject);
                    Profile_details();
                    Toast.makeText(context, "Bet success on Game id" + redeem_gameid, Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Something Went Wrong", "" + e);
                    loadingDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Something Went Wrong", "" + error);
                loadingDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                if (timerStatus.equals("timer off")) {
                    int a = Integer.parseInt(final_game_id) + 1;
                    params.put("game_id", String.valueOf(a));
                    redeem_gameid = String.valueOf(a);
                } else {
                    params.put("game_id", game_id);
                    redeem_gameid = game_id;
                }
                params.put("user_id", user_id);
                params.put("amount", amount);

                Log.e("Bettingparams", "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.AVIATOR_Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    private void Redeem_amount(String Redeem_amount) {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.showDialog(Aviator_Game_Activity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.AVIATOR_GAME_REDEEM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Res_Redeem", "" + response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String user = data.getString("id");
                        String wallet = data.getString("wallet");
                        String name = data.getString("name");
                        String mobile = data.getString("mobile");
                        Toast.makeText(context, "" + message + " " + Redeem_amount, Toast.LENGTH_SHORT).show();
                        Profile_details();
                    }

                    loadingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                loadingDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();

                params.put("game_id", redeem_gameid);
                params.put("user_id", user_id);
                params.put("amount", Redeem_amount);
                params.put("token", Const.AVIATOR_Token);
                params.put("bet_id", "10");

                Log.e("RES_RedeemParam", Const.AVIATOR_GAME_REDEEM + "" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.AVIATOR_Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    private void Profile_details() {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.showDialog(Aviator_Game_Activity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.AVIATOR_PROFILE_DETAILS, new Response.Listener<String>() {
            private Object AdapterSlider;

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("userdetails_resp", "" + response);
//                    String message = jsonObject.getString("message");
//                    String code = jsonObject.getString("code");

                    String user = jsonObject.getString("id");
                    wallet_amount = jsonObject.getString("wallet");
                    String name = jsonObject.getString("name");
                    String mobile = jsonObject.getString("mobile");

                    txt_wallet_amt.setText(wallet_amount);
                    txt_username.setText(name);
                    loadingDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(" Something Went Wrong", "" + error);
                loadingDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", user_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.AVIATOR_Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.v("RES_SOCKET ERROR ", " " + args);
                    Toast.makeText(context, "Socket Connection Error " + args, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("RES_CONNECT  ", " " + args);

                    //    Toast.makeText(context, "Socket Connect " + mSocket.id(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    //        mSocket.on("connect_error", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // String message = null;
//                        String user = String.valueOf(args[0]);
//
//                        Log.v("RES_connect_errorR" , user);
//
//                    }
//                });
//
//            }
//        });
//
//
//

}

