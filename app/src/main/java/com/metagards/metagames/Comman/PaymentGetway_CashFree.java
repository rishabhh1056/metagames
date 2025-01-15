package com.metagards.metagames.Comman;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cashfree.pg.CFPaymentService;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentGetway_CashFree {

    Activity context;
    Callback callback;

    public PaymentGetway_CashFree(Activity context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void startPayment(String plan_id){
        initiatepayment(plan_id);
    }

    private void initiatepayment(String plan_id){

        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("plan_id", plan_id);

        ApiRequest.Call_Api(context, Const.cashfree_token, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    String code=jsonObject.getString("code");
                    String message=jsonObject.getString("message");

                    if (code.equals("200")){

                        token=jsonObject.getString("cftoken");

                        orderId=jsonObject.getString("order_id");
                        orderAmount =jsonObject.getString("Total_Amount");
                        StartCashFreePaymentGateWay();
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

    private void StartCashFreePaymentGateWay(){

        String stage = SharePref.getInstance().getString(SharePref.CASHFREE_STAGE);
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);

        for(Map.Entry entry : getInputParams().entrySet()) {
            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
        }

        cfPaymentService.doPayment(context, getInputParams(), token, stage, "#F8A31A", "#FFFFFF", false);

    }


    String appId = SharePref.getInstance().getString(SharePref.CASHFREE_CLIENT_ID);
    String orderId = "Order0001";
    String orderAmount = "1";
    String orderNote = "Buy Chips";
    String customerName = SharePref.getInstance().getString("name", "");
    String customerPhone = SharePref.getInstance().getString("mobile", "");
    String customerEmail = "support@androappstech.com";
    String token = "";
    private Map<String, String> getInputParams() {


        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }

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

        ApiRequest.Call_Api(context, Const.cashfree_verify, params, new Callback() {
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
