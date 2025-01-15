package com.metagards.metagames.account;

import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.BaseActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.ApiClasses.CommonFunctions;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register_Activity extends BaseActivity {
    private static final String MY_PREFS_NAME = "Login_data";
    EditText edtPhone, edtname, edtReferalCode;
    EditText edtPassword, edit_phone_edit;
    TextView tv, tv_edit_no;
    int pStatus = 0;
    private Handler handler = new Handler();
    TextView imglogin;
    AlertDialog dialog;
    EditText edit_OTP;
    String verificationID, str_chk="";

    RadioGroup radioGroup;
    boolean isSelected = true;
    RadioButton genderradioButton;
    ImageView imgBackground, imgBackgroundlogin;
    Activity context = this;
    public BottomSheetBehavior sheetBehavior;
    public View bottom_sheet;


    private final String LOGIN = "login";
    private final String REGISTER = "register";
    private Bitmap panBitmap;
    LinearLayout lnrThirdScreen;
    LinearLayout lnrSeconScreen;
    LinearLayout lnrFirstScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_register);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtname = findViewById(R.id.edtname);
        edtReferalCode = findViewById(R.id.edtReferalCode);
        imglogin = findViewById(R.id.imglogin);

        lnrFirstScreen = findViewById(R.id.lnrFirstScreen);
        lnrSeconScreen = findViewById(R.id.lnrSeconScreen);
        lnrThirdScreen = findViewById(R.id.lnrThirdScreen);


        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CommonFunctions.isNetworkConnected(context)) {

                    if (edtname.getText().toString().equals("")) {
                        Toast.makeText(context, "Please enter your Name", Toast.LENGTH_SHORT).show();
                    } else if (edtPhone.getText().toString().equals("")) {
                        Toast.makeText(context, "Please enter your Phone", Toast.LENGTH_SHORT).show();
                    } else if (edtPassword.getText().toString().equals("")) {
                        Toast.makeText(context, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    }else {
                        if (isSelected) {
                            RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                            if (rb != null) {
                                login(rb.getText() + "");
                            } else {
                                login("male");
                            }

                        } else {
                            Functions.showToast(context, "Please select Gender first ?");

                        }


                    }
//                    else{
//                        CommonFunctions.showNoInternetDialog(context);
//                    }
                }


            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    isSelected = true;
                }

            }
        });

    }

    private void login(final String value) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();
        Functions.setDialogParams(dialog);


        HashMap params = new HashMap<String, String>();
        params.put("mobile", edtPhone.getText().toString());
        params.put("type", REGISTER);
        ApiRequest.Call_Api(context, Const.SEND_OTP, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                progressDialog.dismiss();
                handleResponse(resp, value);
            }

        });

    }

    private void handleResponse(String response, String value) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            String code = jsonObject.getString("code");


            if (code.equalsIgnoreCase("200")) {

                String otp_id = jsonObject.getString("otp_id");
                phoneLogin(otp_id, value);

            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Functions.showToast(context, message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void phoneLogin(final String otp_id, final String value) {
        // String phoneNumber= "+91"+edtPhone.getText().toString().trim();
        //SendVerificationCode(phoneNumber);
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_subimt);
        dialog.setTitle("Title...");
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        edit_OTP = (EditText) dialog.findViewById(R.id.edit_OTP);
        edit_phone_edit = (EditText) dialog.findViewById(R.id.edit_phone_edit);
        tv_edit_no = (TextView) dialog.findViewById(R.id.tv_edit_no);

        tv_edit_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_chk="1";
                edit_phone_edit.setVisibility(View.VISIBLE);
            }
        });

        dialog.findViewById(R.id.imgclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_chk="";
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.imglogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_OTP.getText().toString().length() > 0) {
                    String verify_code = edit_OTP.getText().toString();
                    VerifyCode(verify_code, otp_id, value);
                } else {
                    Functions.showToast(getApplicationContext(), "Please Enter OTP");

                }

            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        Functions.setDialogParams(dialog);

    }


    private void VerifyCode(final String code, final String otp_id, final String value) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();
        Functions.setDialogParams(dialog);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");


                            if (code.equalsIgnoreCase("201")) {

                                String token = jsonObject.getString("token");

                                if (jsonObject.has("user")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONArray("user").getJSONObject(0);
                                    String id = jsonObject1.getString("id");
                                    String name = jsonObject1.getString("name");
                                    String mobile = jsonObject1.getString("mobile");


                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("user_id", id);
                                    editor.putString("name", name);
                                    editor.putString("mobile", mobile);
                                    editor.putString("token", token);
                                    editor.apply();

                                    Intent i = new Intent(context, Homepage.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Functions.showToast(context, "Login Successful");
                                } else {

                                    if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
                                        Functions.showToast(context, "Wrong mobile number or password");
                                    }

                                }


                            } else if (code.equalsIgnoreCase("200")) {
                                String token = jsonObject.getString("token");
                                String user_id = jsonObject.getString("user_id");

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("user_id", user_id);
                                editor.putString("name", edtname.getText().toString());
                                editor.putString("mobile", edtPhone.getText().toString());
                                editor.putString("token", token);

                                editor.apply();

                                Intent i = new Intent(context, Homepage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Functions.showToast(context, "Login Successful");
//
                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(context, message);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //  handleResponse(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", edit_OTP.getText().toString());
                params.put("otp_id", otp_id.trim());
                    if (str_chk.equals("1")){
                params.put("mobile", edit_phone_edit.getText().toString());
                    }else{
                params.put("mobile", edtPhone.getText().toString());
                    }
                params.put("name", edtname.getText().toString());
                params.put("pan_card_no", "");
                params.put("dob", "");
                params.put("state", "");
                params.put("password", edtPassword.getText().toString());
                params.put("gender", value.trim());
                params.put("referral_code", edtReferalCode.getText().toString().trim());
                if (panBitmap != null)
                    params.put("pan_card", Functions.Bitmap_to_base64(context, panBitmap));

                params.put("type", REGISTER);

                Log.e("LoginScreen_otp", "Register : " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void LoginBtnClick(View view) {
        startActivity(new Intent(getApplicationContext(), LoginWithPasswordScreen_A.class));
    }
}