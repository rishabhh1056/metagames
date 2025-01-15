package com.example.sms_readpayment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    TextView txt_rs,txt_upi,txt_ref;

    String sample_txt = "Rs.5.00 is credited in your Kotak Bank a/c XXXX8503 by UPI ID mauryaarun50@okhdfcbank on 07-07-22 (UPI Ref no 218824882270). New balance: Rs. 794.77";
    String sms_text="";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT < 23){
            //your code here
            requestContactPermission();
        }else {
            requestContactPermission();
        }

        txt_rs  = findViewById(R.id.txt_rs);
        txt_upi = findViewById(R.id.txt_upi);
        txt_ref = findViewById(R.id.txt_ref);

        Intent ii=getIntent();
        if(ii.hasExtra("sms_text")){
        sms_text=ii.getStringExtra("sms_text");
// Split path into segments
            String segments[]       = sms_text.split("Rs.");
            String segment_upi[]    = sms_text.split("UPI Ref no");
            String segment_upi_id[] = sms_text.split("by UPI ID");
// Grab the last segment
            String splitData[] = sms_text.split("\\s", 2);
            String first_rs = splitData[0];
            String last_rs = segments[segments.length - 1];
            String str_amt= first_rs.replace("Rs.","").replace(".00","");

            String upi_id = segment_upi_id[segment_upi.length - 1];
            String[] splited_upi_id = upi_id.split("\\s+");
            String upiId = splited_upi_id[1];

            String upi_ref = segment_upi[segment_upi.length - 1];
            String[] splited_upi = upi_ref.split("\\s+");
            String upi = splited_upi[1].replace(")","").replace(".","");

            Log.d("first_rs_", first_rs);
//            Log.d("last_rs_", "Rs."+last_rs);
            Log.d("upi_id_", upiId);
            Log.d("upi_ref_", upi);

            txt_rs.setText(str_amt);
            txt_upi.setText(upiId);
            txt_ref.setText(upi);

            sendId(str_amt, upiId, upi);
        }

// Split path into segments
//        String segments[] = sample_txt.split("Rs.");
//        String segment_upi[] = sample_txt.split("UPI Ref no");
//        String segment_upi_id[] = sample_txt.split("by UPI ID");
//// Grab the last segment
//        String splitData[] = sample_txt.split("\\s", 2);
//        String first_rs = splitData[0];
//        String last_rs = segments[segments.length - 1];
//
//        String upi_id = segment_upi_id[segment_upi.length - 1];
//        String[] splited_upi_id = upi_id.split("\\s+");
//        String upiId = splited_upi_id[1];
//
//        String upi_ref = segment_upi[segment_upi.length - 1];
//        String[] splited_upi = upi_ref.split("\\s+");
//        String upi = splited_upi[1].replace(")","").replace(".","");
//
//        Log.d("first_rs_", first_rs);
////            Log.d("last_rs_", "Rs."+last_rs);
//        Log.d("upi_id_", upiId);
//        Log.d("upi_ref_", upi);
//
//        txt_rs.setText(first_rs);
//        txt_upi.setText(upiId);
//        txt_ref.setText(upi);
    }

    public void sendId(String rs, String upiId, String upi){
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Checking...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        if (ApiLinks.isNetworkAvailable(MainActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.update_payment,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("nik",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                AppHelper.LogCat(response);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                if (code.equals("200")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "" + ""+message, Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
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
                    params.put("amount",rs);
                    params.put("upi_id",upiId);
                    params.put("utr_no",upi);       //ref no
                    params.put("sms",sms_text);
                    Log.d("data", "getParams1_ststus " + params);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(MainActivity.this).add(stringRequest);

        } else {
            errMsg("check your internet connection...");
        }
    }

    private boolean errMsg(String msg) {
        Toast.makeText(MainActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }

    private void requestContactPermission() {
        int hasContactPermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS);
        if(hasContactPermission != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]   {Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
        }else {
            //Toast.makeText(AddContactsActivity.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Contact permission has now been granted. Showing result.");
                    Toast.makeText(this, "Contact Permission is Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Permission", "Contact permission was NOT granted.");
                }
                break;
        }
    }

}