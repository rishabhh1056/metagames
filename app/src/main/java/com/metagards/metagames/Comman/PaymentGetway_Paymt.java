package com.metagards.metagames.Comman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class PaymentGetway_Paymt {

    Activity context;
    Callback callback;

    final static String MERCHANT_ID = SharePref.getInstance().getString(SharePref.PAYTM_MERCENT_ID);
    final static String WEBSITE = "WEBSTAGING";
    //    final static String HOST = "https://securegw-stage.paytm.in/theia/"; // Firebase functions url
    final static String HOST = "http://206.189.135.239/AllInOneDemo/api"; // Firebase functions url
    final static String CALLBACK = HOST + "/paytmCallback";
    final static String CHECKSUM = HOST + "/checksum";
    private String TAG = "PaymentGetway_Paymt";
    final int requestCode = 2;


    public PaymentGetway_Paymt(Activity context, Callback callback) {
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

        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("plan_id", plan_id);

        ApiRequest.Call_Api(context, Const.paytm_token_api, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    String code=jsonObject.getString("code");
                    String message=jsonObject.getString("message");

                    String  paytm_checksum,order_id,order_amount;
                    try {
                        paytm_checksum = jsonObject.getString("paytm_token");
                        order_id = jsonObject.getString("order_id");
                        order_amount = jsonObject.getString("Total_Amount");
                        ORDER_ID = order_id;
                        amount = order_amount;
                        startPaytmPayment(paytm_checksum);

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

    HashMap getHashPamPaytmParams() {
        HashMap body = new HashMap();
        try {
            body.put("requestType", "Payment");
            body.put("mid", MERCHANT_ID);
            body.put("websiteName", WEBSITE);
            body.put("orderId", ORDER_ID);
            body.put("currency", "INR");
            body.put("custId", SharePref.getU_id());
            body.put("callbackUrl", CALLBACK);

            float value = 0;
            try {
                value = Float.parseFloat(amount);
            } catch (Exception e) {
                value = 0f;
            }

            body.put("amount", String.format(Locale.getDefault(), "%.2f", value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("HashMapparams", "getPaytmParams: " + body);
        return body;
    }

    public void startPaytmPayment (String token){
        String txnTokenString = token;
        String stage = SharePref.getInstance().getString(SharePref.CASHFREE_STAGE);


        // for test mode use it
        String host = "https://securegw-stage.paytm.in/";
        // for production mode use it
        if(stage.equals("PROD"))
            host = "https://securegw.paytm.in/";

        String callBackUrl = host + "theia/paytmCallback?ORDER_ID="+ORDER_ID;
        Log.e(TAG, " callback URL "+callBackUrl);
        PaytmOrder paytmOrder = new PaytmOrder(ORDER_ID, MERCHANT_ID, txnTokenString, ""+amount, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback(){
            @Override
            public void onTransactionResponse(Bundle bundle) {
                VerifyPayment();
                Log.e(TAG, "Response (onTransactionResponse) : "+bundle.toString());
            }
            @Override
            public void networkNotAvailable() {
                Log.e(TAG, "network not available ");
            }
            @Override
            public void onErrorProceed(String s) {
                Log.e(TAG, " onErrorProcess "+s.toString());
            }
            @Override
            public void clientAuthenticationFailed(String s) {
                Log.e(TAG, "Clientauth "+s);
            }
            @Override
            public void someUIErrorOccurred(String s) {
                Log.e(TAG, " UI error "+s);
            }
            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.e(TAG, " error loading web "+s+"--"+s1);
            }
            @Override
            public void onBackPressedCancelTransaction() {
                Log.e(TAG, "backPress ");
            }
            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.e(TAG, " transaction cancel "+s);
            }
        });
//        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(context, requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == this.requestCode && data != null) {
            String nsdk = data.getStringExtra("nativeSdkForMerchantMessage");
            String response = data.getStringExtra("response");
//            Funtions.showToast(context, nsdk + response);

            Log.e(TAG+ " onActivity",""+nsdk + response);

            VerifyPayment();
        }
    }

    private void VerifyPayment(){

        HashMap params = new HashMap();
        params.put("user_id",SharePref.getU_id());
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("order_id", ORDER_ID);

        ApiRequest.Call_Api(context, Const.paytm_verify_checksum, params, new Callback() {
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






}

