package com.metagards.metagames._Tournament;

import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;
import static com.metagards.metagames.Fragments.ActiveTables_BF.SEL_TABLE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.metagards.metagames.Fragments.ActiveTables_BF;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TourList extends AppCompatActivity {
    TextView txtheader;
    ImageView imgclosetop;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        progressDialog = new ProgressDialog(TourList.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        txtheader = findViewById(R.id.txtheader);
        txtheader.setText("Tournament List");

        imgclosetop = findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(view -> finish());

    }

    public void onResume() {
        super.onResume();
        getList();
    }

    double wallet = 0;
    long timeInMillis;
    Date date1 = null, date1_new = null;
    Date date2 = null, date2_new = null;
    int data_part_;
    LinearLayout lnrcancelist;
    String curr_date = "", curr_date_new;

    public void showList(JSONArray last_bet) throws JSONException {
        Log.e("json_list", String.valueOf(last_bet));

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat df_new = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        curr_date = df.format(c);
        curr_date_new = df_new.format(c);
        Log.d("curr_date", curr_date);

        lnrcancelist = findViewById(R.id.lnrlastView);
        lnrcancelist.removeAllViews();

//        for (int ii = 0; ii < last_bet.length(); ii++) {
//            JSONObject jsonObject1 = last_bet.getJSONObject(ii);
//            data_part = jsonObject1.getJSONArray("participants");
//            Log.d("data_part_", String.valueOf(data_part));
//            data_part_ = data_part.length();
//            Log.d("data_participant_no", String.valueOf(data_part_));
//        }

        for (int i = 0; i < last_bet.length(); i++) {
            String id = last_bet.getJSONObject(i).getString("id");
            String name = last_bet.getJSONObject(i).getString("name");
            String no_of_participant = last_bet.getJSONObject(i).getString("no_of_participant");
            String registration_fee = last_bet.getJSONObject(i).getString("registration_fee");
            String first_price = last_bet.getJSONObject(i).getString("first_price");
            String second_price = last_bet.getJSONObject(i).getString("second_price");
            String third_price = last_bet.getJSONObject(i).getString("third_price");
            String start_time = last_bet.getJSONObject(i).getString("start_time");
            String status = last_bet.getJSONObject(i).getString("status");
            String is_winner = last_bet.getJSONObject(i).getString("is_winner");
            String is_registered = last_bet.getJSONObject(i).getString("is_registered");//check if user is register itself

            JSONArray jsonArray = last_bet.getJSONObject(i).getJSONArray("participants");
            int part_int = jsonArray.length();
            Log.d("new_part_", String.valueOf(part_int));

//            for (int ii = 0; ii < last_bet.length(); ii++) {
//                JSONObject jsonObject1 = last_bet.getJSONObject(i);
//                data_part = jsonObject1.getJSONArray("participants");
//                Log.d("data_part_", String.valueOf(data_part));
//                data_part_ = data_part.length();
//                Log.d("data_participant_no", String.valueOf(data_part_));
//            }

            int price = (Integer.parseInt(first_price) + Integer.parseInt(second_price) + Integer.parseInt(third_price));

            String db_date = start_time;
            String db_date_new = start_time;
            Log.d("db_date_", db_date);
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dates_new = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date1 = dates.parse(curr_date);
                date1_new = dates_new.parse(curr_date_new);
                Log.d("date1_new", String.valueOf(date1_new));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date2 = dates.parse(db_date);
                date2_new = dates_new.parse(db_date_new);
                Log.d("date2_new", String.valueOf(date2_new));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            timeInMillis = (date2.getTime() - date1.getTime());
            Log.d("difference_", String.valueOf(timeInMillis));
            System.out.println("difference :: " + timeInMillis);

            addLastWinBet(id, name, no_of_participant, registration_fee, price, start_time, status, is_registered, timeInMillis, date1, date2, part_int, date1_new, date2_new, is_winner);
        }
    }

    int pos = 1;

    private void addLastWinBet(String id, String name, String no_participant, String reg_fee, int price, String start_time, String status, String is_reg, long timeinMS, Date date1, Date date2, int participants, Date date1New, Date date2New, String is_winner) {
        View view = LayoutInflater.from(TourList.this).inflate(R.layout.view_tour_list, null);
        TextView txt_name = view.findViewById(R.id.txt_name);
        TextView txt_details = view.findViewById(R.id.txt_details);
        TextView txt_price = view.findViewById(R.id.txt_price);
        TextView txt_entry_fee = view.findViewById(R.id.txt_entry_fee);
        TextView txt_join = view.findViewById(R.id.txt_join);
        TextView txt_no_participants = view.findViewById(R.id.txt_no_participants);
        TextView txt_start_time = view.findViewById(R.id.txt_start_time);
/*
status - 0 - pending
        1 - ongoing
                2. competed */
        txt_name.setText("" + name);
        txt_price.setText("₹" + price);
        txt_entry_fee.setText("Entry: ₹" + reg_fee);
        txt_no_participants.setText("" + participants + "/" + no_participant);
        txt_start_time.setText("" + start_time);


        if (status.equals("0")) {
            //  txt_join.setText("Running");

            if (is_reg.equals("0")) {
                txt_join.setText("Join");
                //  txt_join.setBackgroundResource(0);
                //   txt_join.setTextColor(Color.parseColor("#228C22"));
            }
            if (is_reg.equals("1")) {
                txt_join.setText("Registered");
                // txt_join.setBackgroundResource(0);
                // txt_join.setTextColor(Color.parseColor("#228C22"));
            }

            new CountDownTimer(timeinMS, 1000) {
                public void onTick(long millisUntilFinished) {
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

                    if (date1New.equals(date2New)) {
                        txt_start_time.setText("Today at\n" + yy);
                    }
                }

                public void onFinish() {
                    txt_start_time.setText("Timeout");
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
                        txt_join.setText("Game Already Started");
                        txt_join.setTextColor(Color.parseColor("#03DAC5"));
                    }
                }

            }.start();
        } else if (status.equals("1")) {
            Log.e("status", "addLastWinBet: " + status);
            Log.e("TAG_is_reg", "addLastWinBet: " + is_reg);
            Log.e("TAG_is_winner", "addLastWinBet: " + is_winner);
            if (is_reg.equals("1") && is_winner.equals("0")) {
                txt_join.setVisibility(View.VISIBLE);
                txt_join.setBackgroundResource(0);
                txt_join.setText("You Lose");
                txt_join.setTextColor(Color.parseColor("#FFDF00"));
            } else if (is_reg.equals("1") && is_winner.equals("1")) {
               // txt_join.setEnabled(false);
                txt_join.setVisibility(View.VISIBLE);
                txt_join.setBackgroundResource(0);
                txt_join.setText("Join next round");
                txt_join.setTextColor(Color.parseColor("#03DAC5"));
            } else  if (is_reg.equals("0") && is_winner.equals("0")) {
                txt_join.setEnabled(false);
                txt_join.setVisibility(View.VISIBLE);
                txt_join.setText("Game Already Started");
                txt_join.setBackgroundResource(0);
                txt_join.setTextColor(Color.parseColor("#03DAC5"));
            }
        } else if (status.equals("2")) {
            txt_join.setText("Completed");
            txt_join.setEnabled(false);
        }


        if (date2.before(date1)) {
            txt_start_time.setText("Expired");
            txt_start_time.setTextColor(Color.parseColor("#D50000"));
           // txt_join.setVisibility(View.GONE);
        }


        txt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG_TATAT", "onClick: "+txt_join.getText().toString() );
//                join_tournament(id);
                if (txt_join.getText().toString().equals("Take a Seat") || txt_join.getText().toString().equals("Join next round")) {
                    take_seat(id);
                } else {
                    join_tournament(id);
                }
            }
        });

//        String name, String no_participant, String reg_fee, int price, String start_time, String status, String is_reg, long timeinMS
        txt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TourListDetail.class);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("participant", no_participant);
                i.putExtra("reg_fee", reg_fee);
                i.putExtra("price", price);
                i.putExtra("time", start_time);
                i.putExtra("status", status);
                i.putExtra("is_reg", is_reg);
                i.putExtra("timeinMS", String.valueOf(timeinMS));
                i.putExtra("date1", String.valueOf(date1New));
                i.putExtra("date2", String.valueOf(date2New));
                startActivity(i);
            }
        });

        lnrcancelist.addView(view);
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
                                Functions.showToast(TourList.this, message);
                                getList();

                            } else {
                                if (jsonObject.has("message")) {
//                                    String message = jsonObject.getString("message");
                                    Functions.showToast(TourList.this, message);
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
                Functions.showToast(TourList.this, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = TourList.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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

        Volley.newRequestQueue(TourList.this).add(stringRequest);
    }

    public void take_seat(String id) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.take_seat,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("SEAT_TOUR", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equalsIgnoreCase("200")) {
                                progressDialog.dismiss();
                                Functions.showToast(TourList.this, message);
                                getList();
                                Intent intent = new Intent(getApplicationContext(), RummPoint_Tour.class);
                                intent.putExtra("player2", "");
                                intent.putExtra("min_entry", "min_try");
                                intent.putExtra("gametype", ActiveTables_BF.RUMMY_PRIVATE_TABLE);
                                intent.putExtra("bootvalue", "");
                                intent.putExtra("tournament_id", id);
                                intent.putExtra(SEL_TABLE, "");
                                startActivity(intent);


                            } else {
                                if (jsonObject.has("message")) {
//                                    String message = jsonObject.getString("message");
                                    Functions.showToast(TourList.this, message);
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
                Functions.showToast(TourList.this, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = TourList.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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

        Volley.newRequestQueue(TourList.this).add(stringRequest);
    }

    JSONArray data_part;

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
                                    Toast.makeText(TourList.this, "No Data found!", Toast.LENGTH_LONG).show();
                                }

//                                for (int i = 0; i < data.length(); i++) {
//                                    JSONObject jsonObject1 = data.getJSONObject(i);
//                                    data_part = jsonObject1.getJSONArray("participants");
//                                    Log.d("data_part_", String.valueOf(data_part));
//                                    data_part_ = data_part.length();
//                                    Log.d("data_participant_no", String.valueOf(data_part_));
//                                }
//                                Gson gson = new Gson();
//                                Participants participants = gson.fromJson(response, Participants.class);
//                                for (int i = 0; i < participants.getTableData().size(); i++) {
//                                    TableDatum tableDatum = participants.getTableData().get(i);
//                                    int abc_ = tableDatum.getParticipants().size();
//                                    Log.d("tableDara_", String.valueOf(abc_));
//                                }
                                showList(data);

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(TourList.this, message);
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
                Functions.showToast(TourList.this, "Something went wrong");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = TourList.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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

        Volley.newRequestQueue(TourList.this).add(stringRequest);

    }
}