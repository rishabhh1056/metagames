package com.first_player_games.Menu;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.first_player_games.Home_Activity;
import com.first_player_games.Others.CommonUtils;
import com.first_player_games.Others.ProgressPleaseWait;
import com.first_player_games.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyDiamondDice implements PaymentResultListener {

    AlertDialog alert;
    FirebaseFunctions functions;
    private Checkout checkout;
    private String key;
    ProgressDialog dialog;
    public BuyDiamondDice(final Home_Activity activity){

        dialog = ProgressPleaseWait.getDialogue(activity);
        RelativeLayout relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.alert_add_diamonds,null);
        alert = new AlertDialog.Builder(activity).setView(relativeLayout).create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        functions = FirebaseFunctions.getInstance();


        relativeLayout.findViewById(R.id.small_diamonds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                functions.getHttpsCallable("getKeyId").call().continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (String) task.getResult().getData().toString();
                    }
                }).addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(task.isSuccessful()){

                            checkout = new Checkout();
                            checkout.setKeyID(task.getResult());
                            checkout.setImage(R.drawable.yonoj_written);
                            functions.getHttpsCallable("createOrder").call().continueWith(new Continuation<HttpsCallableResult, String>() {
                                @Override
                                public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                    return task.getResult().getData().toString();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    dialog.dismiss();
                                    if(task.isSuccessful()) {
                                        try {
                                            JSONObject json = new JSONObject(task.getResult());
                                            json.put("name", CommonUtils.getName(activity.getApplicationContext()));
                                            json.put("description", "Buy Small Diamonds");
                                            json.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                                            json.put("theme.color", "#3399cc");
                                            checkout.open(activity, json);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        System.out.println(task.getException().getMessage());
                                    }
                                }
                            });

                        }
                        else {
                            dialog.dismiss();
                            System.out.println(task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }

    public void show(){
        alert.show();
    }

    public void dismiss(){
        alert.dismiss();
    }

    @Override
    public void onPaymentSuccess(String s) {
        for(int i=0;i<6;i++) {
            for (int j = i; j < 6; j++)
                System.out.print("*");
            System.out.println("");
        }
        System.out.println("Sucesss       "+s);
    }

    @Override
    public void onPaymentError(int e, String s) {
        for(int i=0;i<6;i++) {
            for (int j = i; j < 6; j++)
                System.out.print("*");
            System.out.println("");
        }
        System.out.println("ERROR   : "+e+"      "+s);
    }
}
