package com.metagards.metagames.PaymentScreen;

import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.metagards.metagames.BaseActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Payment_A extends BaseActivity implements View.OnClickListener {
    LinearLayout lyPhonePay, lyGpay, lyPaytm, lyPaypal, lyjio, lyamazon;
    //String payWith = "";
    String plan_id = "";
    String chips_details = "";
    String amount = "";
    String id = "";
    String mobile = "";
    String transaction_id_ = "";
    LayoutInflater inflater;
    private RecyclerView recyclerView;
    private ArrayList<Payment_model> payment_modelArrayList = new ArrayList<>();
    private SingleAdapter adapter;
    public static int checkedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        InitView();


    }

    private void InitView() {
        checkedPosition = -1;
        ((TextView) findViewById(R.id.toolbr_lbl)).setText("Payment Process");

        findViewById(R.id.bt_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        chips_details = intent.getStringExtra("chips_details");
        amount = intent.getStringExtra("amount");
        mobile = intent.getStringExtra("mobile");
        id = intent.getStringExtra("id");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new SingleAdapter(this, payment_modelArrayList);
        recyclerView.setAdapter(adapter);

        createList();

        ((TextView) findViewById(R.id.txtPayableAmount)).setText(" " + Variables.CURRENCY_SYMBOL + amount);


        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment_A.this, Payment_Upload_A.class);
                intent.putExtra("type", "home_");
                intent.putExtra("plan_id", plan_id);
                intent.putExtra("chips_details", chips_details);
                intent.putExtra("amount", amount);
                intent.putExtra("mobile", mobile);
                intent.putExtra("id", id);
                intent.putExtra("transaction_id", transaction_id_);
                startActivity(intent);
            }
        });

        lyGpay = findViewById(R.id.lyGpay);
        lyPaytm = findViewById(R.id.lyPaytm);
        lyPhonePay = findViewById(R.id.lyPhonePay);
        lyPaypal = findViewById(R.id.lyPaypal);
        lyPaypal = findViewById(R.id.lyPaypal);
        lyjio = findViewById(R.id.lyjio);
        lyamazon = findViewById(R.id.lyamazon);

        lyGpay.setOnClickListener(this);
        lyPaytm.setOnClickListener(this);
        lyPhonePay.setOnClickListener(this);
        lyPaypal.setOnClickListener(this);
        lyamazon.setOnClickListener(this);
    }

    private void createList() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("pay_method");

            for (int i = 0; i < m_jArry.length(); i++) {
                Payment_model payment_model = new Payment_model();
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("image"));
                String formula_value = jo_inside.getString("pay_with");
                String url_value = jo_inside.getString("image");
                payment_model.name = formula_value;
                payment_model.image = url_value;
                payment_model.pay_with = jo_inside.getString("pay_with");
                payment_model.mobile = mobile;
                payment_modelArrayList.add(payment_model);

            }
            adapter.setEmployees(payment_modelArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyGpay:
                //payWith = "gpay";
                lyGpay.setBackground(getResources().getDrawable(R.drawable.round_bor_colored));
                lyPhonePay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPaytm.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyjio.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPaypal.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyamazon.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                ((TextView) findViewById(R.id.tvMobile_3)).setText(mobile);
                ((TextView) findViewById(R.id.tvMobile_2)).setText("");
                ((TextView) findViewById(R.id.tvMobile)).setText("");
                ((TextView) findViewById(R.id.tvMobile_5)).setText("");
                ((TextView) findViewById(R.id.tvMobile_4)).setText("");
               /* commentDialog(payWith);
                copyText(Payment_A.this, mobile);*/
                break;

            case R.id.lyPaytm:
               // payWith = "paytm";
                lyPaytm.setBackground(getResources().getDrawable(R.drawable.round_bor_colored));
                lyPhonePay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyGpay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyjio.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPaypal.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyamazon.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                ((TextView) findViewById(R.id.tvMobile)).setText("");
                ((TextView) findViewById(R.id.tvMobile_2)).setText(mobile);
                ((TextView) findViewById(R.id.tvMobile_3)).setText("");
                ((TextView) findViewById(R.id.tvMobile_5)).setText("");
                ((TextView) findViewById(R.id.tvMobile_4)).setText("");
              //  commentDialog(payWith);
                copyText(Payment_A.this, mobile);
                break;

            case R.id.lyPhonePay:
                lyPhonePay.setBackground(getResources().getDrawable(R.drawable.round_bor_colored));
                lyPaytm.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyGpay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyjio.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPaypal.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyamazon.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                ((TextView) findViewById(R.id.tvMobile_4)).setText("");
                ((TextView) findViewById(R.id.tvMobile_3)).setText("");
                ((TextView) findViewById(R.id.tvMobile_2)).setText("");
                ((TextView) findViewById(R.id.tvMobile)).setText(mobile);
                ((TextView) findViewById(R.id.tvMobile_5)).setText("");
                ((TextView) findViewById(R.id.tvMobile_4)).setText("");
                copyText(Payment_A.this, mobile);
                break;
            case R.id.lyPaypal:
                lyPaypal.setBackground(getResources().getDrawable(R.drawable.round_bor_colored));
                lyPaytm.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyGpay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyjio.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPhonePay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyamazon.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                ((TextView) findViewById(R.id.tvMobile_4)).setText(mobile);
                ((TextView) findViewById(R.id.tvMobile_3)).setText("");
                ((TextView) findViewById(R.id.tvMobile_2)).setText("");
                ((TextView) findViewById(R.id.tvMobile)).setText("");
                ((TextView) findViewById(R.id.tvMobile_5)).setText("");
                copyText(Payment_A.this, mobile);
                break;
            case R.id.lyamazon:
                lyamazon.setBackground(getResources().getDrawable(R.drawable.round_bor_colored));
                lyPaytm.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyGpay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyjio.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPhonePay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPaypal.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                ((TextView) findViewById(R.id.tvMobile_5)).setText(mobile);
                ((TextView) findViewById(R.id.tvMobile_3)).setText("");
                ((TextView) findViewById(R.id.tvMobile_2)).setText("");
                ((TextView) findViewById(R.id.tvMobile)).setText("");
                ((TextView) findViewById(R.id.tvMobile_4)).setText("");
                copyText(Payment_A.this, mobile);
                break;
            case R.id.lyjio:
                lyjio.setBackground(getResources().getDrawable(R.drawable.round_bor_colored));
                lyPaytm.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyGpay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyamazon.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPhonePay.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                lyPaypal.setBackground(getResources().getDrawable(R.drawable.d_background_border_gray));
                ((TextView) findViewById(R.id.tvMobile_6)).setText(mobile);
                ((TextView) findViewById(R.id.tvMobile_3)).setText("");
                ((TextView) findViewById(R.id.tvMobile_2)).setText("");
                ((TextView) findViewById(R.id.tvMobile)).setText("");
                ((TextView) findViewById(R.id.tvMobile_4)).setText("");
                ((TextView) findViewById(R.id.tvMobile_5)).setText("");
                copyText(Payment_A.this, mobile);
                break;


            default:
                Toast.makeText(getApplicationContext(), "Select Payment Method", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void copyText(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("CopiedText", text);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getApplicationContext(), "Mobile Number Copied", Toast.LENGTH_SHORT).show();
    }

    private void commentDialog(final String payWith) {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.comment_dialog, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setAnimationStyle(R.style.SheetDialog);
        popupWindow.setElevation(100);
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        final ImageView image = popupView.findViewById(R.id.image);

        if (payWith.equals("paytm")) {
            Glide.with(this).load(R.drawable.ic_payment_paytm).into(image);
        } else if (payWith.equals("gpay")) {
            Glide.with(this).load(R.drawable.gpay).into(image);
        } else if (payWith.equals("paypal")) {
            Glide.with(this).load(R.drawable.ic_payment_paypal_logo).into(image);
        } else if (payWith.equals("amazon")) {
            Glide.with(this).load(R.drawable.ic_payment_amzon_pay).into(image);
        } else if (payWith.equals("jio")) {
            Glide.with(this).load(R.drawable.ic_payment_jio_money).into(image);
        } else {
            Glide.with(this).load(R.drawable.ic_payment_new_phonepe).into(image);
        }

        final LinearLayout ly_submit = popupView.findViewById(R.id.ly_submit);
        final TextView pay_to = popupView.findViewById(R.id.pay_to);
        final TextView price = popupView.findViewById(R.id.price);
        final LinearLayout ly_close_dialog = popupView.findViewById(R.id.ly_close_dialog);
        price.setText(" " + Variables.CURRENCY_SYMBOL + amount + " ");
        pay_to.setText(" " + mobile);
        ly_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                create_Transaction(payWith);
                popupWindow.dismiss();
            }
        });
        ly_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }

    private void create_Transaction(final String payWith) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.create_transaction,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String transaction_id = jsonObject.getString("transaction_id");
                            if (code.equalsIgnoreCase("200")) {
                                transaction_id_ = transaction_id;
                                if (payWith.equals("paytm")) {
                                    OpenApp("net.one97.paytm");
                                } else if (payWith.equals("gpay")) {
                                    OpenApp("com.google.android.apps.nbu.paisa.user");
                                } else {
                                    OpenApp("com.phonepe.app");
                                }
                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(Payment_A.this, message,
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(Payment_A.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("agent_id", id);
                params.put("amount", "" + amount);
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java", "getParams: " + params);
                Log.e("paremter_java", "getParams: " + Const.addPaymentProof);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(Payment_A.this).add(stringRequest);

    }

    private void OpenApp(String pkgName) {
        try {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(pkgName);//
            startActivity(LaunchIntent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "App is not Installed", Toast.LENGTH_SHORT).show();

        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void onClickCalled(Payment_model employee, int i, String s) {
        if (adapter.getSelected() != null) {
            String payWith = adapter.getSelected().getName();

            commentDialog(payWith);
            copyText(Payment_A.this, mobile);
        } else {
            showToast("No Selection");
        }
    }
}