package com.metagards.metagames._Tournament;

import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TourListDetail extends AppCompatActivity {

    TextView txtheader, txt_detail;
    ImageView imgclosetop;
    ProgressDialog progressDialog;
    String id = "", name, no_participant, reg_fee, price, start_time, status, is_reg, timeinMS, date1, date2;
    TextView txt_name, txt_price, txt_entry_fee, txt_join, txt_no_participants, txt_start_time, txt_gold, txt_silver, txt_bronze, txt_1st_win, txt_2nd_win, txt_3rd_win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tour_list);

        progressDialog = new ProgressDialog(TourListDetail.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        txtheader = findViewById(R.id.txtheader);
        txtheader.setText("Details");
        txt_detail = findViewById(R.id.txt_details);
        txt_detail.setVisibility(View.INVISIBLE);

        txt_name = findViewById(R.id.txt_name);
        txt_price = findViewById(R.id.txt_price);
        txt_entry_fee = findViewById(R.id.txt_entry_fee);
        txt_join = findViewById(R.id.txt_join);
        txt_no_participants = findViewById(R.id.txt_no_participants);
        txt_start_time = findViewById(R.id.txt_start_time);

        txt_gold = findViewById(R.id.txt_gold);
        txt_silver = findViewById(R.id.txt_silver);
        txt_bronze = findViewById(R.id.txt_bronze);

        txt_1st_win = findViewById(R.id.txt_1st_win);
        txt_2nd_win = findViewById(R.id.txt_2nd_win);
        txt_3rd_win = findViewById(R.id.txt_3rd_win);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        no_participant = getIntent().getStringExtra("participant");
        reg_fee = getIntent().getStringExtra("reg_fee");
        price = String.valueOf(Integer.parseInt(String.valueOf(getIntent().getIntExtra("price", 0))));
        start_time = getIntent().getStringExtra("time");
        status = getIntent().getStringExtra("status");
        is_reg = getIntent().getStringExtra("is_reg");
        timeinMS = getIntent().getStringExtra("timeinMS");
        date1 = getIntent().getStringExtra("date1");
        date2 = getIntent().getStringExtra("date2");
//        Toast.makeText(this, ""+price, Toast.LENGTH_SHORT).show();

        imgclosetop = findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (is_reg.equals("0")) {
            txt_join.setText("Join");
        }
        if (is_reg.equals("1")) {
            txt_join.setText("Registered");
        }

        new CountDownTimer(Long.parseLong(timeinMS), 1000) {
            public void onTick(long millisUntilFinished) {
//                tv_time.setText("" + (millisUntilFinished / 1000)  / 60+":"+(int)((millisUntilFinished / 1000) % 60));
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);

                txt_start_time.setText(yy);
                if (date1.equals(date2)) {
                    txt_start_time.setText("Today at\n" + yy);
                }
            }

            public void onFinish() {
                txt_start_time.setText("Expired");
                txt_start_time.setTextColor(Color.parseColor("#D50000"));
                if (is_reg.equals("1")) {
                    txt_join.setVisibility(View.VISIBLE);
                    txt_join.setBackgroundResource(0);
                    txt_join.setText("Take a Seat");
                    txt_join.setTextColor(Color.parseColor("#FFDF00"));
                }
                if (is_reg.equals("0")) {
                    txt_join.setEnabled(false);
                    txt_join.setVisibility(View.VISIBLE);
                    txt_join.setBackgroundResource(0);
                    txt_join.setText("Completed");
                    txt_join.setTextColor(Color.parseColor("#03DAC5"));
                }
            }

        }.start();

        txt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_tournament(id);
            }
        });

        if (status.equals("2")) {
            txt_join.setText("Completed");
            txt_join.setEnabled(false);
            txt_join.setTextColor(Color.parseColor("#ffffff"));
            txt_join.setBackgroundResource(R.drawable.btn_green_bg);
        }
        getList();
    }

    public void join_tournament(String id) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.join_tour,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("JOIN_TOUR", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equalsIgnoreCase("200")) {
                                progressDialog.dismiss();
                                Functions.showToast(TourListDetail.this, message);

                            } else {
                                if (jsonObject.has("message")) {
//                                    String message = jsonObject.getString("message");
                                    Functions.showToast(TourListDetail.this, message);
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
                Functions.showToast(TourListDetail.this, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = TourListDetail.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("tournament_id", id);
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

        Volley.newRequestQueue(TourListDetail.this).add(stringRequest);
    }

    public void onResume() {
        super.onResume();
    }

    LinearLayout lnrParticipants;
    String curr_date = "", curr_date_new;

    public void showList(JSONArray last_bet) throws JSONException {
        Log.d("json_list", String.valueOf(last_bet));


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat df_new = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        curr_date = df.format(c);
        curr_date_new = df_new.format(c);
        Log.d("curr_date", curr_date);

        lnrParticipants = findViewById(R.id.lnrParticipants);
        lnrParticipants.removeAllViews();
        for (int i = 0; i < last_bet.length(); i++) {
            if (id.equals(last_bet.getJSONObject(i).getString("id"))) {
                txt_no_participants.setText("" + last_bet.length() + "/" + no_participant);
                String main_id = last_bet.getJSONObject(i).getString("id");
                String first_price = last_bet.getJSONObject(i).getString("first_price");
                String second_price = last_bet.getJSONObject(i).getString("second_price");
                String third_price = last_bet.getJSONObject(i).getString("third_price");
                String first_winner_name = last_bet.getJSONObject(i).getString("first_winner_name");
                String second_winner_name = last_bet.getJSONObject(i).getString("second_winner_name");
                String third_winner_name = last_bet.getJSONObject(i).getString("third_winner_name");
                String no_of_participant = last_bet.getJSONObject(i).getString("no_of_participant");
                int price = (Integer.parseInt(first_price) + Integer.parseInt(second_price) + Integer.parseInt(third_price));
                txt_gold.setText(" 1st ₹:" + first_price);
                txt_silver.setText(" 2nd ₹:" + second_price);
                txt_bronze.setText(" 3rd ₹:" + third_price);
                ((TextView) findViewById(R.id.txt_price)).setText("" + price);

                txt_1st_win.setText(""+first_winner_name);
                txt_2nd_win.setText(""+second_winner_name);
                if (!third_winner_name.equals("null"))
                txt_3rd_win.setText(""+third_winner_name);
            }
            JSONArray jsonArray = last_bet.getJSONObject(i).getJSONArray("participants");
            int part_int = jsonArray.length();
            Log.d("new_part_", String.valueOf(part_int));

            for (int ii = 0; ii < jsonArray.length(); ii++) {
                if (id.equals(jsonArray.getJSONObject(ii).getString("tournament_id"))) {
                    String participants = jsonArray.getJSONObject(ii).optString("name");
                    Log.d("participants_name", participants);
                    addLastWinBet(participants);
                }

            }


        }
    }

    int pos = 1;

    private void addLastWinBet(String name) {
        View view = LayoutInflater.from(TourListDetail.this).inflate(R.layout.view_participant_list, null);
        TextView txt_no = view.findViewById(R.id.txt_no);
        TextView txt_name = view.findViewById(R.id.txt_name);

        txt_no.setText("" + pos++ + ".".trim());
        txt_name.setText("" + name.trim());

        lnrParticipants.addView(view);

    }

    private void getList() {      //list
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.tour_list,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK_LIST_TOUR", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                progressDialog.dismiss();
                                JSONArray data = jsonObject.getJSONArray("table_data");
                                int data_ = data.length();
                                if (data_ == 0) {
                                    Toast.makeText(TourListDetail.this, "No Data found!", Toast.LENGTH_LONG).show();
                                }
                                showList(data);

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(TourListDetail.this, message);
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
                Functions.showToast(TourListDetail.this, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = TourListDetail.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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

        Volley.newRequestQueue(TourListDetail.this).add(stringRequest);

    }
}