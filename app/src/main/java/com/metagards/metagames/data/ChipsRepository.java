package com.metagards.metagames.data;

import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.SharePref;

import org.json.JSONObject;

import java.util.HashMap;

public class ChipsRepository {

    Context context;

    public ChipsRepository(Context context) {
        this.context = context;
    }

    public LiveData<String> call_api_dummyOrder(String requestPlaneid,String requstAmount){
        MutableLiveData<String> _dummyPainResponse = new MutableLiveData<>();
        HashMap params = new HashMap();
        params.put("token", SharePref.getAuthorization());
        params.put("user_id", SharePref.getU_id());
        params.put("plan_id", requestPlaneid);
        params.put("extra", "0");
        ApiRequest.Call_Api(context, Const.PLACE_UPI_ORDER, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equals("200")) {
                          String order_id = jsonObject.getString("order_id");
                          call_api_dummypay(order_id, requstAmount, new Callback() {
                                @Override
                                public void Responce(String resp, String type, Bundle bundle) {
                                        _dummyPainResponse.postValue(message);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        _dummyPainResponse.postValue(e.getLocalizedMessage());
                    }
                }

            }
        });

        return _dummyPainResponse;
    }


    private LiveData<String> call_api_dummypay(String requestOrderId,String requstAmount,Callback callback){
        MutableLiveData<String> _dummyPainResponse = new MutableLiveData<>();
        HashMap params = new HashMap();
        params.put("param1", requestOrderId);
        params.put("status", "1");
        params.put("token", SharePref.getAuthorization());
        params.put("user_id", SharePref.getU_id());
        params.put("amount", requstAmount);
        ApiRequest.Call_Api(context, Const.CHECK_UPI_STATUS, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equals("200")) {
                            callback.Responce(message,message,null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.Responce(e.getLocalizedMessage(),e.getLocalizedMessage(),null);
                    }
                }

            }
        });

        return _dummyPainResponse;
    }
}
