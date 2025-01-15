package com.example.sms_readpayment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InitiatePayment extends AppCompatActivity {

    EditText et_amt, et_upi_id, et_user_id, et_upi_id_rec, et_name, et_note;
    TextView btn_submit;
    final int UPI_PAYMENT = 0;
    final int UPI_PAYMENT_via = 2;
    final int BACK_PRESS = 8;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10 * 1000; //Delay for 15 seconds.  One second = 1000 milliseconds.
    TextView txt_title;
    TranslateAnimation animate;

    String str_user_id = "", str_amt = "", str_upi = "", str_merchant_id = "", str_merchant_secret = "", str_user_name = "", param1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_payment);

        et_amt = findViewById(R.id.et_amt);
        et_upi_id = findViewById(R.id.et_upi_id);
        et_user_id = findViewById(R.id.et_user_id);
        et_upi_id_rec = findViewById(R.id.et_upi_id_rec);
        et_name = findViewById(R.id.et_name);
        et_note = findViewById(R.id.et_note);
        txt_title = findViewById(R.id.txt_title);
        btn_submit = findViewById(R.id.btn_submit);

        animate = new TranslateAnimation(11, -11, 0, 0);
        animate.setDuration(1000);
        animate.setFillAfter(true);
        animate.setRepeatMode(2);
        animate.setRepeatCount(Animation.INFINITE);
        txt_title.startAnimation(animate);

        if (getIntent().hasExtra("amt") && getIntent().hasExtra("upi") && getIntent().hasExtra("merchant_id") &&
                getIntent().hasExtra("merchant_secret") && getIntent().hasExtra("user_id") && getIntent().hasExtra("name") && getIntent().hasExtra("param1")) {
            str_amt = getIntent().getStringExtra("amt");
            str_upi = getIntent().getStringExtra("upi");
            str_merchant_id = getIntent().getStringExtra("merchant_id");
            str_merchant_secret = getIntent().getStringExtra("merchant_secret");
            str_user_id = getIntent().getStringExtra("user_id");
            str_user_name = getIntent().getStringExtra("name");
            btn_submit.setEnabled(true);
        } else {
            Toast.makeText(this, "Parameter missing..", Toast.LENGTH_SHORT).show();
            btn_submit.setEnabled(false);
        }


        (findViewById(R.id.pay_via_upi)).setOnClickListener(view -> {
            Uri uri = Uri.parse("upi://pay?pa=" + et_upi_id.getText().toString() + "&pn=Info nix Data Private Limited&mc=5816&tid=AXIFRCO120720221js3b5g8ft&tr=AXIFRCO120720221js3b5g8ft&am=+" + et_amt.getText().toString() + "+&cu=INR\"");
            Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
            upiPayIntent.setData(uri);
            Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
            if (null != chooser.resolveActivity(getPackageManager())) {
                startActivityForResult(chooser, UPI_PAYMENT_via);
            } else {
                Toast.makeText(getApplicationContext(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
            }

        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (et_amt.getText().toString().equals("")) {
//                    Toast.makeText(InitiatePayment.this, "Please enter valid Amount.", Toast.LENGTH_SHORT).show();
//                }
//                else
                if (et_upi_id.getText().toString().equals("")) {
                    Toast.makeText(InitiatePayment.this, "Please enter valid UPI Id", Toast.LENGTH_SHORT).show();
                }
//                else
//                if (et_user_id.getText().toString().equals("")) {
//                    Toast.makeText(InitiatePayment.this, "Please enter User Id.", Toast.LENGTH_SHORT).show();
//                } else if (et_upi_id_rec.getText().toString().equals("")) {
//                    Toast.makeText(InitiatePayment.this, "Please enter valid Receiver UPI Id", Toast.LENGTH_SHORT).show();
//                }
//                else if (et_name.getText().toString().equals("")) {
//                    Toast.makeText(InitiatePayment.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
//                } else if (et_note.getText().toString().equals("")) {
//                    Toast.makeText(InitiatePayment.this, "Please enter you Note!", Toast.LENGTH_SHORT).show();
//                }
                else {
                    initiate_payment();
                }

            }
        });

    }

    String payment_id = "", payment_upi = "", payment_amt = "", payment_url = "";

    public void initiate_payment() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(InitiatePayment.this);
        progressDialog.setMessage("Checking...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        if (ApiLinks.isNetworkAvailable(InitiatePayment.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.initiate_payment,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                AppHelper.LogCat(response);
                                Log.d("initiate_payment_res", response);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                payment_id = jsonObject.getString("payment_id");
                                payment_upi = jsonObject.getString("pa");
                                payment_amt = jsonObject.getString("am");

                                payment_url = jsonObject.getString("payment_url");
                                Log.d("payment_url_", payment_url);

                                if (code.equals("200")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(InitiatePayment.this, "" + message, Toast.LENGTH_SHORT).show();
                                    call_success();
//                                    payUsingUpi(et_amt.getText().toString(),"9820522335@paytm","Rajesh","testing");
//                                    payUsingUpi(et_amt.getText().toString(), et_upi_id_rec.getText().toString(), et_name.getText().toString(), et_note.getText().toString());

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(InitiatePayment.this, "" + message, Toast.LENGTH_SHORT).show();
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
                    return header;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_id", str_user_id);
                    params.put("upi_id", et_upi_id.getText().toString());
                    params.put("amount", str_amt);       //ref no
//                    params.put("sms",sms_text);
                    Log.d("data", "getParams1_init: " + params);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(InitiatePayment.this).add(stringRequest);

        } else {
            errMsg("check your internet connection...");
        }
    }

    void payUsingUpi(String amount, String upiId, String name, String note) {
        Log.e("payUsingUpi", "payUsingUpi: " + upiId);
        Log.e("payUsingUpi", "payUsingUpi: " + amount);
        Log.e("payUsingUpi", "payUsingUpi: " + name);
        Log.e("payUsingUpi", "payUsingUpi: " + note);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
//                .appendQueryParameter("mc", "5816")
//                .appendQueryParameter("tid", "AXIFRCO120720221js3b5g8ft")
//                .appendQueryParameter("tr", "AXIFRCO120720221js3b5g8ft")
                .appendQueryParameter("cu", "INR")
                .build();

        Log.e("uri_upi", "payUsingUpi: " + uri);
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(InitiatePayment.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    public void call_success() {
        Intent i = new Intent(InitiatePayment.this, CheckPaymentStatus.class);
        i.putExtra("payment_id", payment_id);
        i.putExtra("amount", payment_amt);
        i.putExtra("upi_id", payment_upi);
        i.putExtra("upi_id_entered", et_upi_id.getText().toString());
        i.putExtra("payment_url", payment_url);
        i.putExtra("merchant_id", str_merchant_id);
        i.putExtra("merchant_secret", str_merchant_secret);
        i.putExtra("user_id", str_user_id);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivity", "" + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == 123) {
                Toast.makeText(this, "back again", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == UPI_PAYMENT) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.d("UPI", "onActivityResult: " + trxt);
                    Log.d("_data_", String.valueOf(data));
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        } else if (requestCode == UPI_PAYMENT_via) {
            if ((RESULT_OK == resultCode)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.d("UPI", "onActivityResult: " + trxt);
                    Log.d("_data_", String.valueOf(data));
                }
            }
        }


    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(InitiatePayment.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            Log.d("status_", status);
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(InitiatePayment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI_payment", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(InitiatePayment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.d("payment_cancelled", "cancel");
            } else {
                Toast.makeText(InitiatePayment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(InitiatePayment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
        if (!payment_id.equals("")) {
//            Intent i = new Intent(InitiatePayment.this, CheckPaymentStatus.class);
//            i.putExtra("payment_id", payment_id);
//            i.putExtra("amount", et_amt.getText().toString());
//            i.putExtra("upi_id", et_upi_id.getText().toString());
//            startActivity(i);
            call_success();
        } else {
            Toast.makeText(this, "Payment id is empty!", Toast.LENGTH_SHORT).show();
        }

//        check();

    }

    private void check_status() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(InitiatePayment.this);
        progressDialog.setMessage("Checking...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        if (ApiLinks.isNetworkAvailable(InitiatePayment.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                AppHelper.LogCat(response);
                                Log.d("payment_status", response);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
//                                payment_id = jsonObject.getString("payment_id");

                                if (code.equals("200") && message.equals("Success")) {
                                    progressDialog.dismiss();
                                    handler.removeCallbacks(runnable);
                                    Toast.makeText(InitiatePayment.this, "Thank you for your Payment!", Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(InitiatePayment.this, "" + message, Toast.LENGTH_SHORT).show();
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
                    header.put("token", ApiLinks.Token);
                    return header;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("payment_id", payment_id);
//                    params.put("sms",sms_text);
                    Log.d("data", "getParams1: " + params);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(InitiatePayment.this).add(stringRequest);

        } else {
            errMsg("check your internet connection...");
        }
    }

    public void check() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                //do something
                check_status();
                handler.postDelayed(runnable, delay);
            }
        }, delay);
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private boolean errMsg(String msg) {
        Toast.makeText(InitiatePayment.this, "" + msg, Toast.LENGTH_SHORT).show();
        return false;
    }

}