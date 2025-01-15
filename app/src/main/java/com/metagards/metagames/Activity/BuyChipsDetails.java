package com.metagards.metagames.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.Comman.CommonAPI;
import com.metagards.metagames.Comman.DialogSelectPaymentType;

import com.metagards.metagames.Comman.PaymentGetway_CashFree;
import com.metagards.metagames.Comman.PaymentGetway_Paymt;

import com.metagards.metagames.Comman.PaymentGetway_PayuMoney;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames.data.ChipsRepository;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.metagards.metagames.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;


public class BuyChipsDetails extends BaseActivity implements PaymentResultListener, PaymentStatusListener {


    Activity context = this;
    private static final String MY_PREFS_NAME = "Login_data";
    Button btnPaynow;
    TextView txtChipsdetails;
    String plan_id = "";
    String chips_details = "";
    String amount = "";
    String RazorPay_ID = "", orderIdString = "";
    private String order_id;
    ImageView imgback, imgPaynow;
    String county_code = "+91 ";
    String whats_no = "";
    ProgressDialog progressDialog;
    Calendar calendar = Calendar.getInstance();
    public static String btn_clicked="";
    RelativeLayout rl_extra;
    private static final String[] sectors = {"1",
            "2", "3", "4", "5", "6", "7", "8",
            "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20"};
    @BindView(R.id.spinBtn)
    Button spinBtn;
    @BindView(R.id.resultTv)
    TextView resultTv;
    @BindView(R.id.wheel)
    ImageView wheel;

    @BindView(R.id.btnDummyPay)
        TextView btnDummyPay;

    // We create a Random instance to make our wheel spin randomly
    private static final Random RANDOM = new Random();
    private int degree = 0, degreeOld = 0;
    // We have 37 sectors on the wheel, we divide 360 by this value to have angle for each sector
    // we divide by 2 to have a half sector
    private static final float HALF_SECTOR = 360f / 20f / 2f;

    private EasyUpiPayment easyUpiPayment;
    private RadioGroup radioAppChoice;
    RadioButton paymentAppChoice;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    String selected_payment = "", str_extraVal="";
    public static String str_diff="";
    static Date date1 = null, date2 = null;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    String currentDateandTimeOld = sdf.format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chips_details);
        ButterKnife.bind(this);


        initialize();
        radioAppChoice = findViewById(R.id.radioAppChoice);

        progressDialog = new ProgressDialog(this);

        rl_extra = findViewById(R.id.rl_extra);
        String str_extra = String.valueOf(SharePref.getInstance().getInt(SharePref.isExtra));     //0= visible
        Log.d("visible_extra", str_extra);
        if (str_extra.equals("0")){
           // rl_extra.setVisibility(View.VISIBLE);
        }
        if (str_extra.equals("1")){
            rl_extra.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        chips_details = intent.getStringExtra("chips_details");
        amount = intent.getStringExtra("amount");

        imgPaynow = findViewById(R.id.imgPaynow);
        txtChipsdetails = findViewById(R.id.txtChipsdetails);
        txtChipsdetails.setText("Buy " + chips_details + " Pay now " + Variables.CURRENCY_SYMBOL + amount);

        PaymentGateWayInit();
        selected_payment = SharePref.getInstance().getString(SharePref.PaymentType);
        if (selected_payment.equals("custom_pay")){
            findViewById(R.id.pay_load).setVisibility(View.VISIBLE);
        } else if (selected_payment.equals("neokard_pay")) {
            findViewById(R.id.pay_load).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.pay_load).setVisibility(View.GONE);
        }

        imgPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                if (SharePref.getInstance().getBoolean(SharePref.isPaymentGateShow, true)) {


                    DialogSelectPaymentType dialogSelectPaymentType = new DialogSelectPaymentType(context, new Callback() {
                        @Override
                        public void Responce(String resp, String type, Bundle bundle) {


                        }
                    });


                    selected_payment = SharePref.getInstance().getString(SharePref.PaymentType);
                    Log.e("TAG_selected_payment", "onClick: " + selected_payment);
                    if (selected_payment.equalsIgnoreCase(Variables.RAZOR_PAY)) {
                        place_order();
                    } else if (selected_payment.equalsIgnoreCase(Variables.PAYTM)) {
//                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                _paymentGetway_paymt.startPayment(plan_id, amount);
                            }
                        }, 1000);
                    } else if (selected_payment.equalsIgnoreCase(Variables.PAYUMONEY)) {
                        paymentGetway_payuMoney.startPayment(plan_id);
                    } else if (selected_payment.equalsIgnoreCase(Variables.UPI_FREE_WAY)) {
                       // PlaceOrderUPI();
                        Toast.makeText(context, "UPI Free ways not available at this time ", Toast.LENGTH_SHORT).show();
                    } else if (selected_payment.equalsIgnoreCase(Variables.ATOM_PAY)) {
                        String url = Const.BSE_URL + "atompay/index?user_id=" + prefs.getString("user_id", "") +
                                "&token=" + prefs.getString("token", "") + "&plan_id=" + plan_id;
                        Log.e("payment_url", "onClick: " + url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (selected_payment.equalsIgnoreCase(Variables.CUSTOM_PAY)) {
                      //  startCustomPayment();
                        startCustomPayment();
                        Log.e("UPI_IDD", "onClick: " + SharePref.getInstance().getString(SharePref.UPI_ID));
                    } else if (selected_payment.equalsIgnoreCase(Variables.NEOKARD_PAY)) {
                        //  startCustomPayment();
                        startNeokardPayment();
                        Log.e("UPI_IDD_neo", "onClick: " + SharePref.getInstance().getString(SharePref.UPI_ID));
                    }else {
                        _paymentGetwayCashFree.startPayment(plan_id);
                    }


                } else {
                    whats_no = prefs.getString("whats_no", "");

                    Functions.showToast(BuyChipsDetails.this, "" + whats_no);

                    if (!whats_no.equals(""))
                        Functions.openWhatsappContact(BuyChipsDetails.this, county_code + whats_no);
                }


//
            }
        });
        imgback = findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (btn_clicked.equals("")){
            spinBtn.setEnabled(true);
        }

        try {
            date2 = sdf.parse(currentDateandTimeOld);
            Log.d("date_one", String.valueOf(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(date1!=null){
            long difference = date2.getTime() - date1.getTime();
            int days  = (int) (difference / (1000*60*60*24));
            Log.d("days_diff", String.valueOf(days));
            int hours =(int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            Log.d("hours_diff", String.valueOf(hours));
            int minute = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            Log.d("mins_diff", String.valueOf(minute));
//            SharePref.getInstance().putInt(SharePref.isEnable, Integer.parseInt(String.valueOf(minute)));
            Log.d("difference_", String.valueOf(minute));
            if(date1!=null && minute>=10){
                spinBtn.setEnabled(true);
            }else{
                spinBtn.setEnabled(false);
            }

//            String str_enable = String.valueOf(SharePref.getInstance().getInt(SharePref.isEnable));
//            Log.d("str_enable", str_enable);
//            if (Integer.parseInt(str_enable)<=10){
//                spinBtn.setEnabled(false);
//            }
        }


    }

    private void initialize() {
        initPaymentStage();
    }

    private void initPaymentStage() {
        ChipsRepository chipsRepository = new ChipsRepository(getApplicationContext());
//        btnDummyPay.setVisibility(SharePref.getInstance().isApplicationStage() ? View.VISIBLE : View.GONE);

        btnDummyPay.setOnClickListener(view ->{
            progressDialog.show();
            chipsRepository.call_api_dummyOrder(plan_id,amount).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    progressDialog.dismiss();
                    if(s.equalsIgnoreCase("success"))
                        SuccessBox();
                    else
                        Functions.showToast(context,s);
                }
            });
        });
    }

    @OnClick(R.id.spinBtn)
    public void spin(View v) {
        try {
            date1 = sdf.parse(currentDateandTimeOld);
            Log.d("date_two", String.valueOf(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        degreeOld = degree % 1920;
        // we calculate random angle for rotation of our wheel
        degree = RANDOM.nextInt(360) + 1920;
//        degree = 270+360;                             //custom win
        Log.d("degree_new", String.valueOf(degree));
        Log.d("degree_old", String.valueOf(degreeOld));
        // rotation effect on the center of the wheel
        RotateAnimation rotateAnim = new RotateAnimation(degreeOld, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(3600);
        rotateAnim.setFillAfter(true);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // we empty the result text view when the animation start
                resultTv.setText("");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // we display the correct sector pointed by the triangle at the end of the rotate animation
                spinBtn.setEnabled(false);
                resultTv.setVisibility(View.VISIBLE);
                resultTv.setText("You will get "+getSector(360 - (degree % 360))+" % extra coins!");
                str_extraVal = getSector(360 - (degree % 360));
                Log.d("getSector_val", str_extraVal);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // we start the animation
        wheel.startAnimation(rotateAnim);
    }
    private String getSector(int degrees) {
        int i = 0;
        String text = null;

        do {
            // start and end of each sector on the wheel
            float start = HALF_SECTOR * (i * 2 + 1);
            float end   = HALF_SECTOR * (i * 2 + 3);
            Log.d("_start", String.valueOf(start));
            Log.d("_end", String.valueOf(end));
//            float start = 45;
//            float end   = 50;

            if (degrees >= start && degrees < end) {
                // degrees is in [start;end[
                // so text is equals to sectors[i];
                text = sectors[i];
            }

            i++;
            // now we can test our Android Roulette Game :)
            // That's all !
            // In the second part, you will learn how to add some bets on the table to play to the Roulette Game :)
            // Subscribe and stay tuned !

        } while (text == null && i < sectors.length);

        return text;
    }


    @Override
    protected void onResume() {
        super.onResume();

        CommonAPI.CALL_API_UserDetails(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

            }
        });

    }

    private void PaymentGateWayInit() {
        _paymentGetway_paymt = new PaymentGetway_Paymt(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if (resp.equalsIgnoreCase(Variables.SUCCESS)) {
                    dialog_payment_success();
                } else {

                }

            }
        });

        _paymentGetwayCashFree = new PaymentGetway_CashFree(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if (resp.equalsIgnoreCase(Variables.SUCCESS)) {
                    dialog_payment_success();
                } else {

                }

            }
        });

        paymentGetway_payuMoney = new PaymentGetway_PayuMoney(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if (resp.equalsIgnoreCase(Variables.SUCCESS)) {
                    dialog_payment_success();
                } else {

                }

            }
        });
    }


    public void place_order() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PLCE_ORDER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {

                                order_id = jsonObject.getString("order_id");
                                String Total_Amount = jsonObject.getString("Total_Amount");
                                RazorPay_ID = jsonObject.getString("RazorPay_ID");
                                startPayment(order_id, Total_Amount, RazorPay_ID);
                            } else if (code.equals("404")) {
                                Functions.showToast(BuyChipsDetails.this, "" + message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("plan_id", plan_id);
                Functions.LOGE("BuyChipsDetails", Const.PLCE_ORDER + "\n" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    PaymentGetway_CashFree _paymentGetwayCashFree;
    PaymentGetway_PayuMoney paymentGetway_payuMoney;
    PaymentGetway_Paymt _paymentGetway_paymt;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAG_data", "resultcode " + resultCode);
        if (selected_payment.equalsIgnoreCase(Variables.CASH_FREE)) {
            if (_paymentGetwayCashFree != null)
                _paymentGetwayCashFree.onActivityResult(requestCode, resultCode, data);

        } else if (selected_payment.equalsIgnoreCase(Variables.PAYTM)) {
            if (_paymentGetway_paymt != null)
                _paymentGetway_paymt.onActivityResult(requestCode, resultCode, data);

        } else if (selected_payment.equalsIgnoreCase(Variables.PAYUMONEY)) {
            if (paymentGetway_payuMoney != null)
                paymentGetway_payuMoney.onActivityResult(requestCode, resultCode, data);
        } else if (selected_payment.equalsIgnoreCase(Variables.CUSTOM_PAY)) {
            if (requestCode == 123) {
                if ((RESULT_OK == resultCode) || (resultCode == 123)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
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
            } else {
                Toast.makeText(BuyChipsDetails.this, "payment failed", Toast.LENGTH_SHORT).show();
            }
        }
        else if (selected_payment.equalsIgnoreCase(Variables.NEOKARD_PAY)) {
            if (requestCode == 1234) {
                if ((RESULT_OK == resultCode) || (resultCode == 1234)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperationNeo(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperationNeo(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperationNeo(dataList);
                }
            } else {
                Toast.makeText(BuyChipsDetails.this, "payment failed", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void startPayment(String ticket_id, String total_Amount, String razorPay_ID) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        String key = SharePref.getInstance().getString(SharePref.RAZOR_PAY_KEY);

        if (Functions.checkisStringValid(key)) {
            co.setKeyID(key);
        }

        try {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            JSONObject options = new JSONObject();
            options.put("name", prefs.getString("name", ""));
            options.put("description", "chips payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", total_Amount);
            options.put("order_id", razorPay_ID);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "support@androappstech.com");
            preFill.put("contact", prefs.getString("mobile", ""));
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Functions.showToast(activity, "Error in payment: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            payNow(razorpayPaymentID);

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


    public void payNow(final String payment_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PY_NOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            ParseSuccessPayment(jsonObject);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("order_id", order_id);
                params.put("payment_id", payment_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    private void ParseSuccessPayment(JSONObject jsonObject) throws JSONException {


        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");

        if (code.equals("200")) {
            Functions.showToast(BuyChipsDetails.this, "" + message);
            dialog_payment_success();
        } else if (code.equals("404")) {
            Functions.showToast(BuyChipsDetails.this, "" + message);
        }

    }

    private void dialog_payment_success() {

        new SmartDialogBuilder(BuyChipsDetails.this)
                .setTitle("Your Payment has been done Successfully!")
                .setSubTitle("Your Payment has been done Successfully!")
                .setCancalable(false)

                //.setTitleFont("Do you want to Logout?") //set title font
                // .setSubTitleFont(subTitleFont) //set sub title font
                .setNegativeButtonHide(true) //hide cancel button
                .setPositiveButton("Ok", new SmartDialogClickListener() {
                    @Override
                    public void onClick(SmartDialog smartDialog) {
                        smartDialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("Cancel", new SmartDialogClickListener() {
            @Override
            public void onClick(SmartDialog smartDialog) {
                // Funtions.showToast(context,"Cancel button Click");
                smartDialog.dismiss();

            }
        }).build().show();

    }

    private void PlaceOrderUPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.callback_place,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("callback_place", "onResponse: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                Log.e("SharePref_tag_", "onClick: " + SharePref.getInstance().getString(SharePref.MerchantID));
//                                Intent intent1 = new Intent(getApplicationContext(), InitiatePayment.class);
//                                intent1.putExtra("amt", amount);
//                                intent1.putExtra("upi", "");
//                                intent1.putExtra("merchant_id", SharePref.getInstance().getString(SharePref.MerchantID));
//                                intent1.putExtra("merchant_secret", SharePref.getInstance().getString(SharePref.MerchantSecret));
//                                intent1.putExtra("user_id", prefs.getString("user_id", ""));
//                                intent1.putExtra("name", prefs.getString("name", ""));
//                                intent1.putExtra("param1", jsonObject.getString("order_id"));
//                                startActivity(intent1);
                            } else if (code.equals("404")) {
                                Functions.showToast(BuyChipsDetails.this, "" + message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("plan_id", plan_id);
                Functions.LOGE("BuyChipsDetails", Const.PLCE_ORDER + "\n" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
    /*Custom UPI Payment Code */
    private void startCustomPayment() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PLACE_UPI_ORDER,
                response -> {
                    Log.e("TAG_onResponse", "onResponse: " + response);
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equals("200")) {
                            progressDialog.dismiss();
                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            String name = prefs.getString("name", "");
                            order_id = jsonObject.getString("order_id");
                          //  payUsingUpi(amount, SharePref.getInstance().getString(SharePref.UPI_ID), name, "Chips");
                            pay();
                        } else if (code.equals("404")) {
                            progressDialog.dismiss();
                            Functions.showToast(BuyChipsDetails.this, "" + message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("plan_id", plan_id);
                params.put("extra", str_extraVal);
                Functions.LOGE("BuyChipsDetails", Const.PLACE_UPI_ORDER + "\n" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void startNeokardPayment() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PLACE_UPI_ORDER_Neokred,
                response -> {
                    Log.e("TAG_onResponse_neo", "onResponse: " + response);
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equals("200")) {
                            progressDialog.dismiss();
                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            String name = prefs.getString("name", "");
                            order_id = jsonObject.getString("order_id");
                            String Neokred_upi_string = jsonObject.getString("Neokred_upi_string");     //new
//                            payUsingUpi(amount, SharePref.getInstance().getString(SharePref.UPI_ID), name, "Chips");      //old
                            payUsingUpiNeo(amount, SharePref.getInstance().getString(SharePref.UPI_ID), name, "Chips", Neokred_upi_string);

                        } else if (code.equals("404")) {
                            progressDialog.dismiss();
                            Functions.showToast(BuyChipsDetails.this, "" + message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("plan_id", plan_id);
                params.put("extra", str_extraVal);
                Functions.LOGE("BuyChipsDetails", Const.PLACE_UPI_ORDER_Neokred + "\n" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void payUsingUpi(String amount, String string, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", string)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("tr", String.valueOf(System.currentTimeMillis()))
                .appendQueryParameter("cu", "INR")
                .build();


        //  only google pay
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        Log.d("pay_uri", String.valueOf(uri));

        if (null != intent.resolveActivity(BuyChipsDetails.this.getPackageManager())) {

            BuyChipsDetails.this.startActivityForResult(intent, 123);
        } else {
            Toast.makeText(BuyChipsDetails.this, "BHIM app not found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    private void payUsingUpiNeo(String amount, String string, String name, String note, String str_uri) {
        Uri uri = Uri.parse(str_uri);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        Log.d("pay_uri", String.valueOf(uri));

        if (null != intent.resolveActivity(BuyChipsDetails.this.getPackageManager())) {

            BuyChipsDetails.this.startActivityForResult(intent, 1234);
        } else {
            Toast.makeText(BuyChipsDetails.this, "BHIM app not found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        String str = data.get(0);
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
        String paymentCancel = "";
        if (str == null) str = "discard";
        String status = "";

        String response[] = str.split("&");
        for (int i = 0; i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    orderIdString = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user.";
            }
        }
        if (status.equals("success")) {
            //Code to handle successful transaction here.
               /* Toast.makeText(BuyChipsDetails.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + orderIdString);*/
            finalCallback();
        } else if ("Payment cancelled by user.".equals(paymentCancel)) {
            Toast.makeText(BuyChipsDetails.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(BuyChipsDetails.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
        }

    }
    private void upiPaymentDataOperationNeo(ArrayList<String> data) {
        String str = data.get(0);
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
        String paymentCancel = "";
        if (str == null) str = "discard";
        String status = "";

        String response[] = str.split("&");
        for (int i = 0; i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    orderIdString = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user.";
            }
        }
        if (status.equals("success")) {
            //Code to handle successful transaction here.
               /* Toast.makeText(BuyChipsDetails.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + orderIdString);*/
           finalCallbackNeo();
        } else if ("Payment cancelled by user.".equals(paymentCancel)) {
            Toast.makeText(BuyChipsDetails.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(BuyChipsDetails.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void finalCallback() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CHECK_UPI_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("callback_place", "onResponse: " + Const.CHECK_UPI_STATUS);
                        Log.e("callback_place", "onResponse: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {
                                progressDialog.dismiss();
                                SuccessBox();
//                                finalCallbackNeo();
                            } else {
                                progressDialog.dismiss();
                                Functions.showToast(BuyChipsDetails.this, "" + message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("param1", order_id);
                params.put("status", "1");
                params.put("token", prefs.getString("token", ""));
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("amount", amount);
                Log.d("data", "getParams1_check " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void finalCallbackNeo() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CHECK_UPI_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("callback_place", "onResponse: " + Const.CHECK_UPI_STATUS);
                        Log.e("callback_place", "onResponse: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {
//                                progressDialog.dismiss();
//                                SuccessBox();
                                finalCallbackNeoSuccess();
                            } else {
                                progressDialog.dismiss();
                                Functions.showToast(BuyChipsDetails.this, "" + message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("param1", order_id);
                params.put("status", "1");
                params.put("token", prefs.getString("token", ""));
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("amount", amount);
                Log.d("data", "getParams1_check " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void finalCallbackNeoSuccess() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CHECK_UPI_STATUS_NEO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("callback_placeNeo", "onResponse: " + Const.CHECK_UPI_STATUS_NEO);
                        Log.e("callback_place_resNeo", "onResponse: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {
                                progressDialog.dismiss();
                                SuccessBox();
                            } else {
                                progressDialog.dismiss();
                                Functions.showToast(BuyChipsDetails.this, "" + message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("order_id", order_id);
                params.put("token", prefs.getString("token", ""));
                params.put("user_id", prefs.getString("user_id", ""));
                Log.d("data", "getParams1_checkNeo" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void SuccessBox() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Thank You")
                .setMessage("Your Payment has been done Successfully!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(BuyChipsDetails.this, Homepage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }).create().show();

    }

    /*EASYUPI*/

//    private void pay() {
//
//       // amount = amountEt.getText().toString().trim();
//        int int_amount = Integer.parseInt(amount);
//        float str_amt = (float) int_amount ;
//        String transactionId = "TID" + System.currentTimeMillis();
//        String payeeVpa = SharePref.getInstance().getString(SharePref.UPI_ID);
//        String payeeName = "PaySwiff";
//        String payeeMerchantCode = "200";
//        String transactionRefId = "transactionId";
//        String description = "RechargeWallet";
//        PaymentApp paymentApp;
//        paymentAppChoice = findViewById(radioAppChoice.getCheckedRadioButtonId());
//
//        switch (paymentAppChoice.getId()) {
//            case R.id.app_default:
//                paymentApp = PaymentApp.ALL;
//                break;
//            case R.id.app_amazonpay:
//                paymentApp = PaymentApp.AMAZON_PAY;
//                break;
//            case R.id.app_bhim_upi:
//                paymentApp = PaymentApp.BHIM_UPI;
//                break;
//            case R.id.app_google_pay:
//                paymentApp = PaymentApp.GOOGLE_PAY;
//                break;
//            case R.id.app_phonepe:
//                paymentApp = PaymentApp.PHONE_PE;
//                break;
//            case R.id.app_paytm:
//                paymentApp = PaymentApp.PAYTM;
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + paymentAppChoice.getId());
//        }
//
//
//        // START PAYMENT INITIALIZATION
//        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
//                .with(paymentApp)
//                .setPayeeVpa(payeeVpa)
//                .setPayeeName(payeeName)
//                .setTransactionId(transactionId)
//                .setTransactionRefId(transactionRefId)
//                .setPayeeMerchantCode(payeeMerchantCode)
//                .setDescription(description)
//                .setAmount(String.valueOf(str_amt));
//        Log.e("TAG_amount", "pay: "+amount );
//        // END INITIALIZATION
//
//        try {
//            // Build instance
//            easyUpiPayment = builder.build();
//
//            // Register Listener for Events
//            easyUpiPayment.setPaymentStatusListener(this);
//
//            // Start payment / transaction
//            easyUpiPayment.startPayment();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            toast("Error: " + exception.getMessage());
//        }
//    }

    private void pay() {

        // amount = amountEt.getText().toString().trim();
        int int_amount = Integer.parseInt(amount);
        float str_amt = (float) int_amount ;
        String transactionId = "TID" + System.currentTimeMillis();
        String payeeVpa = SharePref.getInstance().getString(SharePref.UPI_ID);
        String payeeName = "WinnerBaji";
        String payeeMerchantCode = "200";
        Random r = new Random();
        String output = r.nextInt((2000000 - 200000) +1) + "20000";
        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.MILLISECOND)*60*60*60;

        String transactionRefId = "TID" + time+generateID();;
        // String transactionRefId = "transactionId";
        String description = "RechargeWallet";
        PaymentApp paymentApp;
        paymentAppChoice = findViewById(radioAppChoice.getCheckedRadioButtonId());

        switch (paymentAppChoice.getId()) {
            case R.id.app_default:
                paymentApp = PaymentApp.ALL;
                break;
            case R.id.app_amazonpay:
                paymentApp = PaymentApp.AMAZON_PAY;
                break;
            case R.id.app_bhim_upi:
                paymentApp = PaymentApp.BHIM_UPI;
                break;
            case R.id.app_google_pay:
                paymentApp = PaymentApp.GOOGLE_PAY;
                break;
            case R.id.app_phonepe:
                paymentApp = PaymentApp.PHONE_PE;
                break;
            case R.id.app_paytm:
                paymentApp = PaymentApp.PAYTM;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + paymentAppChoice.getId());
        }


        // START PAYMENT INITIALIZATION
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .with(paymentApp)
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setPayeeMerchantCode("")
                .setDescription(description)
                .setAmount(String.valueOf(str_amt));
        Log.e("TAG_amount", "pay: "+paymentApp );
        // END INITIALIZATION

        try {
            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            toast("Error: " + exception.getMessage());
        }
    }

    public long generateID() {
        Random rnd = new Random();
        char [] digits = new char[11];
        digits[0] = (char) (rnd.nextInt(9) + '1');
        for(int i=1; i<digits.length; i++) {
            digits[i] = (char) (rnd.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        //  statusView.setText(transactionDetails.toString());

        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled by user");

    }

    private void onTransactionSuccess() {
        // Payment Success
        toast("Success");
        finalCallback();
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted");

    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Payment Failed");

    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
