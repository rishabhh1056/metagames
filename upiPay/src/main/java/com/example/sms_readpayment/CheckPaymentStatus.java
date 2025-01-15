package com.example.sms_readpayment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sms_readpayment.ApiClasses.ApiLinks;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.view.WindowManager;

public class CheckPaymentStatus extends AppCompatActivity {
    CountDownTimer cTimer = null;
    ProgressDialog progressDialog;
    String pay_id = "", payment_url = "";
    final Handler ha = new Handler();
    Runnable runnable;
    int delay = 10000;
    String check_timer = "";
    final int UPI_PAYMENT_via = 2;
    LinearLayout lnr_utr;
    Button btn_submitUTR;
    EditText edt_utr, edt_amt;
    String str_merchant_id = "", str_merchant_secret = "", user_id = "";
    TextView txt_pp_pay, txt_gp_pay, txt_pt_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment_status);
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        check_timer = "";
        progressDialog = new ProgressDialog(CheckPaymentStatus.this);
        progressDialog.setMessage("Checking Payment Status..");
        progressDialog.setCancelable(true);

        pay_id = getIntent().getStringExtra("payment_id");
        payment_url = getIntent().getStringExtra("payment_url");
        str_merchant_id = getIntent().getStringExtra("merchant_id");
        str_merchant_secret = getIntent().getStringExtra("merchant_secret");
        user_id = getIntent().getStringExtra("user_id");

        findViewById(R.id.bt_menu).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.txt_amount)).setText("Amount: Rs." + getIntent().getStringExtra("amount"));
        ((TextView) findViewById(R.id.txn_id)).setText("Entered UPI ID:" + getIntent().getStringExtra("upi_id_entered"));
        ((TextView) findViewById(R.id.txt_phonepe)).setText("PHONEPE: " + getIntent().getStringExtra("upi_id"));
        ((TextView) findViewById(R.id.txt_gpay)).setText("GOOGLE PAY: " + getIntent().getStringExtra("upi_id"));
        ((TextView) findViewById(R.id.txt_paytm)).setText("PAYTM: " + getIntent().getStringExtra("upi_id"));
        ((TextView) findViewById(R.id.txt_pp_amt)).setText("Amount Rs: " + getIntent().getStringExtra("amount"));
        ((TextView) findViewById(R.id.txt_gp_amt)).setText("Amount Rs: " + getIntent().getStringExtra("amount"));
        ((TextView) findViewById(R.id.txt_pt_amt)).setText("Amount Rs: " + getIntent().getStringExtra("amount"));

        txt_pp_pay = findViewById(R.id.txt_pp_pay);
        txt_gp_pay = findViewById(R.id.txt_gp_pay);
        txt_pt_pay = findViewById(R.id.txt_pt_pay);

        edt_utr = findViewById(R.id.edt_utr);
        edt_amt = findViewById(R.id.edt_amt);

        lnr_utr = findViewById(R.id.lnr_utr);
        btn_submitUTR = findViewById(R.id.btn_submitUTR);


        txt_pp_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = getPackageManager().getLaunchIntentForPackage("com.phonepe.app");
                    startActivity(i);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(CheckPaymentStatus.this, "No PhonePe app installed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_gp_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user");
                    startActivity(i);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(CheckPaymentStatus.this, "No GPay app installed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_pt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = getPackageManager().getLaunchIntentForPackage("net.one97.paytm");
                    startActivity(i);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(CheckPaymentStatus.this, "No PayTM app installed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_submitUTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call back api
                if (edt_utr.getText().toString().equals("")) {
                    Toast.makeText(CheckPaymentStatus.this, "Please enter valid UTR No.", Toast.LENGTH_SHORT).show();
                } else if (edt_amt.getText().toString().equals("")) {
                    Toast.makeText(CheckPaymentStatus.this, "Please enter valid Amount!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    update_utr();
                }
            }
        });

        //start checking
        progressDialog.show();
        StartPaymentTimer();
        check();

    }

    public void check() {
        runnable = new Runnable() {
            @Override
            public void run() {
                check_status();
                ha.postDelayed(this, delay);
            }
        };
        ha.postDelayed(runnable, delay);

    }

    private void check_status() {
        if (ApiLinks.isNetworkAvailable(CheckPaymentStatus.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.check_status,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("payment_status", response);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                if (code.equals("200")) {
                                    progressDialog.dismiss();
                                    ha.removeCallbacks(runnable);
                                    cTimer.cancel();
                                    SuccesBox();

                                } else if (code.equals("404") && message.equals("Failed")) {
                                    progressDialog.dismiss();
                                    ha.removeCallbacks(runnable);
                                    Toast.makeText(CheckPaymentStatus.this, "Payment failed!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(CheckPaymentStatus.this, InitiatePayment.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(CheckPaymentStatus.this, "" + message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                progressDialog.dismiss();
                                ha.removeCallbacks(runnable);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    ha.removeCallbacks(runnable);
                    error.printStackTrace();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> header = new HashMap<>();
                    header.put("token", ApiLinks.Token);
                    header.put("merchantid", str_merchant_id);
                    header.put("merchantsecret", str_merchant_secret);
                    Log.e("TAG_header", "getHeaders: " + header);
                    return header;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("payment_id", pay_id);
                    Log.d("data", "getParams1_check " + params);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(CheckPaymentStatus.this).add(stringRequest);

        } else {
            errMsg("check your internet connection...");
        }
    }

    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ha.removeCallbacks(runnable); //stop handler when activity not visible
        check_timer = "1";
    }

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CheckPaymentStatus.this);
        builder.setTitle("Warning");
        builder.setMessage("Are you Sure you want to close this page?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        check_timer = "";
                        Log.d("check_timer_", check_timer);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("ALERT!");
        alert.show();
    }

    private boolean errMsg(String msg) {
        Toast.makeText(CheckPaymentStatus.this, "" + msg, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void SuccesBox() {
        new AlertDialog.Builder(this)
                .setTitle("Thank You")
                .setMessage("Your Payment has been done Successfully!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        ha.removeCallbacks(runnable);
                    }
                }).create().show();

    }

    private void StartPaymentTimer() {
        cTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.pay_time)).setText("" + millisUntilFinished / 1000);
                ((TextView) findViewById(R.id.pay_time)).setText("" + String.format("%d min : %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                ((TextView) findViewById(R.id.pay_time)).setText("Session Expired");

                //start session expire here
                cTimer.cancel();
                ha.removeCallbacks(runnable);
                progressDialog.dismiss();
                lnr_utr.setVisibility(View.VISIBLE);
            }
        };
        cTimer.start();
    }

    public void update_utr() {

        if (ApiLinks.isNetworkAvailable(CheckPaymentStatus.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.update_utr,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("utr_update_msg", response);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                if (code.equals("200")) {
                                    lnr_utr.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Toast.makeText(CheckPaymentStatus.this, "" + message, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (code.equals("201")) {
                                    Toast.makeText(CheckPaymentStatus.this, message, Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CheckPaymentStatus.this, "" + message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    error.printStackTrace();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> header = new HashMap<>();
                    header.put("merchantid", str_merchant_id);
                    header.put("merchantsecret", str_merchant_secret);
                    header.put("token", ApiLinks.Token);
                    android.util.Log.e("header_check", "getHeaders: " + header);
                    return header;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("utr_no", edt_utr.getText().toString());
                    params.put("amount", edt_amt.getText().toString());
                    params.put("user_id", user_id);
                    Log.d("data", "getParams1_init: " + params);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(CheckPaymentStatus.this).add(stringRequest);

        } else {
            errMsg("check your internet connection...");
        }
    }

}