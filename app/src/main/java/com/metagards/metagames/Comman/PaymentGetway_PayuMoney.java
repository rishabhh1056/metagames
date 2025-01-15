package com.metagards.metagames.Comman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;
import com.payu.base.models.ErrorResponse;
import com.payu.base.models.PayUPaymentParams;
import com.payu.checkoutpro.PayUCheckoutPro;
import com.payu.checkoutpro.utils.PayUCheckoutProConstants;
import com.payu.ui.model.listeners.PayUCheckoutProListener;
import com.payu.ui.model.listeners.PayUHashGenerationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;

public class PaymentGetway_PayuMoney {

    Activity context;
    Callback callback;

    public PaymentGetway_PayuMoney(Activity context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void startPayment(String plan_id){

        mMerchantKey = SharePref.getInstance().getString(SharePref.PAYU_MERCENT_KEY);
//        mSalt = SharePref.getInstance().getString(SharePref.PAYTM_MERCHANT_SALT);

        Functions.LOGE("PaymentGetWay","mMerchantKey :"+mMerchantKey);
        Functions.LOGE("PaymentGetWay","mSalt :"+mSalt);

        initiatepayment(plan_id);
    }

    private void initiatepayment(String plan_id){

        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("plan_id", plan_id);

        ApiRequest.Call_Api(context, Const.payu_token, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    String code=jsonObject.getString("code");
                    String message=jsonObject.getString("message");

                    if (code.equals("200")){


                        orderId=jsonObject.getString("order_id");
                        orderAmount =jsonObject.getString("Total_Amount");

                        JSONObject paymentbody = jsonObject.getJSONObject("payumoney_body");
                        customerPlan_id = paymentbody.getString("plan_id");
//                        orderId = paymentbody.getString("orderId");
                        customerName = paymentbody.getString("name");
                        customerPhone = paymentbody.getString("mobile");
                        customerEmail = paymentbody.getString("email");
                        orderAmount = paymentbody.getString("amount");

                        StartPayuPaymentGateWay();
                    }
                    else  if (code.equals("404")) {
                        Functions.showToast(context, ""+message);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

//    private String mMerchantKey = "";
//    private String mSalt = "";

    //Test Credential
    private String mMerchantKey = "gtKFFx";
    private String mSalt = "wia56q6O";

    private String mBaseURL = "https://test.payu.in";
//    private String mBaseURL = "https://secure.payu.in";


    private String mAction = ""; // For Final URL
    private String mTXNId; // This will create below randomly
    private String mHash; // This will create below randomly
    private String mProductInfo = "Food Items"; //Passing String only
    private String mFirstName; // From Previous Activity
    private String mEmailId; // From Previous Activity
    private double mAmount; // From Previous Activity
    private String mPhone; // From Previous Activity
    private String mServiceProvider = "payu_paisa";
//    private String mSuccessUrl = "https://payuresponse.firebaseapp.com/success";
    private String mSuccessUrl = Const.payu_callback+"";
//    private String mFailedUrl = "https://payuresponse.firebaseapp.com/failure";
    private String mFailedUrl = Const.payu_callback+"";


    private void StartPayuPaymentGateWay(){

        mTXNId = orderId;
        mAmount = Double.parseDouble(orderAmount);
        mProductInfo = customerPlan_id;
        mFirstName = customerName;
        mEmailId = customerEmail;
        mPhone = customerPhone;

        String cashfree_stage = SharePref.getInstance().getString(SharePref.CASHFREE_STAGE);
        boolean isProduction = false;
        if(cashfree_stage.equalsIgnoreCase("PROD"))
            isProduction = true;

        String user_credential = mMerchantKey+":"+System.currentTimeMillis()+customerEmail;

        if(isProduction)
            user_credential = "";

        PayUPaymentParams payUPaymentParams = new PayUPaymentParams.Builder().setAmount(orderAmount)
                .setIsProduction(isProduction)
                .setProductInfo(customerPlan_id)
                .setKey(""+mMerchantKey)
                .setPhone(customerPhone)
                .setTransactionId(""+orderId)
                .setFirstName(customerName)
                .setEmail(customerEmail)
                .setUserCredential(user_credential)
                .setSurl(mSuccessUrl)
                .setFurl(mFailedUrl)
                .build();

        PayUCheckoutPro.open(context,
                payUPaymentParams,
                new PayUCheckoutProListener() {

                    @Override
                    public void onPaymentSuccess(Object response) {
                        //Cast response object to HashMap
                        HashMap<String,Object> result = (HashMap<String, Object>) response;
                        String payuResponse = (String)result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                        String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);

                        Functions.LOGE("PayuMoney",""+result);
                        callback.Responce(Variables.SUCCESS,"",null);

                    }

                    @Override
                    public void onPaymentFailure(@NonNull Object response) {
                        //Cast response object to HashMap
                        HashMap<String,Object> result = (HashMap<String, Object>) response;
                        String payuResponse = (String)result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                        String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);

                        Functions.LOGE("PayuMoney",""+result);
                        callback.Responce(Variables.FAILED,"",null);

                    }

                    @Override
                    public void onPaymentCancel(boolean isTxnInitiated) {
                    }

                    @Override
                    public void onError(ErrorResponse errorResponse) {
                        String errorMessage = errorResponse.getErrorMessage();

                        Functions.LOGE("PayuMoney",""+errorMessage);

                    }

                    @Override
                    public void setWebViewProperties(@Nullable WebView webView, @Nullable Object o) {
                        //For setting webview properties, if any. Check Customized Integration section for more details on this
                    }

                    @Override
                    public void generateHash(HashMap<String, String> valueMap, PayUHashGenerationListener hashGenerationListener) {

                        String hashName = valueMap.get(PayUCheckoutProConstants.CP_HASH_NAME);
                        String hashData = valueMap.get(PayUCheckoutProConstants.CP_HASH_STRING);
                        if (!TextUtils.isEmpty(hashName) && !TextUtils.isEmpty(hashData)) {
                            //Generate Hash from your backend here
                            String hash = null;
//                             else {

                                //Calculate SHA-512 Hash here
                                hash = calculateHash(hashData + mSalt);

                                Functions.LOGE("PaymentGetWay","HashData :"+hashData);
                                Functions.LOGE("PaymentGetWay","hash :"+hash);

//                            }

                          CALL_API_getHashString(hashData, new Callback() {
                              @Override
                              public void Responce(String resp, String type, Bundle bundle) {



                                  HashMap<String, String> dataMap = new HashMap<>();
                                  dataMap.put(hashName, resp);
                                  hashGenerationListener.onHashGenerated(dataMap);

                              }
                          });


                        }
                    }
                }
        );
    }

    private void CALL_API_getHashString(String hashData,Callback callback)
    {

        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("hash_data", hashData);

        ApiRequest.Call_Api(context, Const.payu_salt, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    String code=jsonObject.getString("code");
                    String message=jsonObject.getString("message");

                    if (code.equals("200")){

                        token=jsonObject.getString("payumoney_hash");
                        callback.Responce(token,null,null);
                    }
                    else  if (code.equals("404")) {
                        Functions.showToast(context, ""+message);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

    private String calculateHash(String hashString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            return getHexString(mdbytes);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private String getHexString(byte[] array){
        StringBuilder hash = new StringBuilder();
        for (byte hashByte : array) {
            hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }
        return hash.toString();
    }



    String appId = SharePref.getInstance().getString(SharePref.CASHFREE_CLIENT_ID);
    String orderId = "Order0001"+System.currentTimeMillis();
    String orderAmount = "1";
    String orderNote = "Buy Chips";
    String payumoney_string = "Buy Chips";
    String customerPlan_id = ""+System.currentTimeMillis();
    String customerName = SharePref.getInstance().getString("name", "");
    String customerPhone = SharePref.getInstance().getString("mobile", "");
    String customerEmail = "support@androappstech.com";
    String token = "";

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d("m", "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
            {
                String txStatus = bundle.getString("txStatus");
                if(!txStatus.equalsIgnoreCase("CANCELLED"))
                    CashFreeVerifyPayment(bundle);

                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d("resp", key + " : " + bundle.getString(key));
                    }
                }
            }

        }
    }

    private void CashFreeVerifyPayment(Bundle bundle){

        HashMap params = new HashMap();
        params.put("user_id",SharePref.getU_id());
        params.put("appId",appId);
        params.put("order_id",bundle.getString("orderId"));
        params.put("orderAmount",bundle.getString("orderAmount"));
        params.put("referenceId",bundle.getString("referenceId"));
        params.put("paymentMode",bundle.getString("paymentMode"));
        params.put("txMsg",bundle.getString("txMsg"));
        params.put("txTime",bundle.getString("txTime"));
        params.put("signature",bundle.getString("signature"));
        params.put("txStatus",bundle.getString("txStatus"));
        params.put("type",bundle.getString("type"));
        params.put("token", SharePref.getInstance().getString("token", ""));

        ApiRequest.Call_Api(context, Const.payu_verify, params, new Callback() {
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

