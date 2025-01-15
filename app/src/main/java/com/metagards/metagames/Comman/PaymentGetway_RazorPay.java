package com.metagards.metagames.Comman;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentGetway_RazorPay implements PaymentResultListener {

    Activity context;
    Callback callback;


    final int requestCode = 2;


    public PaymentGetway_RazorPay(Activity context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    String ORDER_ID = "";
    String amount = "1";
    public void startPayment(String plan_id, String amount){
        this.amount = amount;
        initiatepayment(plan_id);

    }

    private void initiatepayment(String plan_id){

        ORDER_ID = plan_id;
        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("plan_id", plan_id);
        params.put("amount",amount);



        ApiRequest.Call_Api(context, Const.PLCE_ORDER, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    String code=jsonObject.getString("code");
                    String message=jsonObject.getString("message");

                    String  paytm_checksum,order_id,order_amount;
                    try {
                        paytm_checksum = jsonObject.getString("RazorPay_ID");
                        order_id = jsonObject.getString("order_id");
                        order_amount = jsonObject.getString("Total_Amount");
                        ORDER_ID = order_id;
                        amount = order_amount;
                        startPayment(paytm_checksum);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

    public void startPayment(String razorPay_ID) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = context;

        final Checkout co = new Checkout();

        String key = SharePref.getInstance().getString(SharePref.RAZOR_PAY_KEY);

        if(Functions.checkisStringValid(key))
        {
            co.setKeyID(key);
        }

        try {
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            JSONObject options = new JSONObject();
            options.put("name",  prefs.getString("name", ""));
            options.put("description", "chips payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount);
            options.put("order_id", razorPay_ID);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "support@androappstech.com");
            preFill.put("contact",  prefs.getString("mobile", ""));
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Functions.showToast(activity, "Error in payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == this.requestCode && data != null) {
            String nsdk = data.getStringExtra("nativeSdkForMerchantMessage");
            String response = data.getStringExtra("response");
            Functions.showToast(context, nsdk + response);

            Log.e("onActivity",""+nsdk + response);

//            VerifyPayment(null);
        }
    }

    private void VerifyPayment(String payment_id){

        HashMap params = new HashMap();
        params.put("user_id",SharePref.getU_id());
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("order_id", ORDER_ID);
        params.put("payment_id", payment_id);

        ApiRequest.Call_Api(context, Const.PY_NOW, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    ParseSuccessPayment(jsonObject);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void ParseSuccessPayment(JSONObject jsonObject) throws JSONException {


        String code=jsonObject.getString("code");
        String message=jsonObject.getString("message");

        if (code.equals("200")){
            Functions.showToast(context, ""+message);
            callback.Responce(Variables.SUCCESS,"",null);

        }
        else  if (code.equals("404")) {
            Functions.showToast(context, ""+message);
            callback.Responce(Variables.FAILED,"",null);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            VerifyPayment(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            //Funtions.showToast(this, "Payment failed: " + code + " " + response, Toast
            // .LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}

