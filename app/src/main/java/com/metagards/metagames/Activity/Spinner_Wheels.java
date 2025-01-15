package com.metagards.metagames.Activity;

import static com.metagards.metagames.Utils.Functions.SetBackgroundImageAsDisplaySize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.Adapter.ChipsBuy_SpinAdapter;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.model.ChipsBuyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Spinner_Wheels extends AppCompatActivity {
    RelativeLayout rl_extra;
    private static final String[] sectors = {"1",
            "2", "3", "4", "5", "6", "7", "8",
            "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18"};
    @BindView(R.id.spinBtn)
    Button spinBtn;
    @BindView(R.id.resultTv)
    TextView resultTv;
    @BindView(R.id.wheel)
    ImageView wheel;
    // We create a Random instance to make our wheel spin randomly
    private static final Random RANDOM = new Random();
    private int degree = 0, degreeOld = 0;
    // We have 37 sectors on the wheel, we divide 360 by this value to have angle for each sector
    // we divide by 2 to have a half sector
    private static final float HALF_SECTOR = 360f / 18f / 2f;
    String selected_payment = "", str_extraVal = "";
    public static String str_diff = "";
    static Date date1 = null, date2 = null;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    String currentDateandTimeOld = sdf.format(new Date());
    public static String btn_clicked = "";
    private static final String MY_PREFS_NAME = "Login_data";
    //ImageView img_back;
    ArrayList<ChipsBuyModel> historyModelArrayList;
    ChipsBuy_SpinAdapter historyAdapter;
    RecyclerView rec_history;
    ProgressDialog progressDialog;
    LinearLayout linear_no_history;
    ImageView imgback;
    int spinner_result = 0;
    public static int final_string_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_wheels);
        ButterKnife.bind(this);
        intWheels();
        intRecyclerview();

    }

    private void intRecyclerview() {
        RelativeLayout relativeLayout = findViewById(R.id.rlt_parent);
        SetBackgroundImageAsDisplaySize(this, relativeLayout, R.drawable.home_bg);


        ((TextView) findViewById(R.id.txtheader)).setText("Lucky Slot");


        // img_back=findViewById(R.id.img_back);
        rec_history = findViewById(R.id.rec_chips);
        linear_no_history = findViewById(R.id.linear_no_history);
        imgback = findViewById(R.id.imgclosetop);
        imgback.setOnClickListener(view -> finish());

        progressDialog = new ProgressDialog(this);

        getChipsList();

        rec_history.setLayoutManager(new GridLayoutManager(this, 2));
        // rec_history.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void intWheels() {
        if (btn_clicked.equals("")) {
            // spinBtn.setEnabled(true);
        }

        try {
            date2 = sdf.parse(currentDateandTimeOld);
            Log.d("date_one", String.valueOf(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date1 != null) {
            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            Log.d("days_diff", String.valueOf(days));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            Log.d("hours_diff", String.valueOf(hours));
            int minute = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            Log.d("mins_diff", String.valueOf(minute));
//            SharePref.getInstance().putInt(SharePref.isEnable, Integer.parseInt(String.valueOf(minute)));
            Log.d("difference_", String.valueOf(minute));
            if (date1 != null && minute >= 10) {
                spinBtn.setEnabled(true);
                SharePref.getInstance().putString(SharePref.spin_discount, "0");
            } else {
                spinBtn.setEnabled(false);
            }

        }

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
        // degree = RANDOM.nextInt(360) + 1920;
        // degree = 360+345;                             //custom win for 1 0
        // degree = 360 + 325;                        //custom win for 2 340

        /*this is random for 1 and 2 only */
        List<Integer> givenList = Arrays.asList(345, 325);
        Random rand = new Random();
        degree = givenList.get(rand.nextInt(givenList.size())) + 360;
        // Log.d("degree_new_", String.valueOf(givenList.get(rand.nextInt(givenList.size()))));
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
                spinner_result = Integer.parseInt(getSector(360 - (degree % 360)));
                resultTv.setText("You will get " + getSector(360 - (degree % 360)) + " % extra coins!");
                str_extraVal = getSector(360 - (degree % 360));
                Log.d("getSector_val", str_extraVal);
                getChipsList();
                SharePref.getInstance().putString(SharePref.spin_discount, str_extraVal);
                final_string_value = Integer.parseInt(str_extraVal);
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
            float end = HALF_SECTOR * (i * 2 + 3);
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

    public void getChipsList() {
        //  progressDialog.show();
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GET_CHIP_PLAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG_response", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equals("200")) {
                                progressDialog.dismiss();
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("PlanDetails");
                                historyModelArrayList = new ArrayList<>();
                                String str_extra = String.valueOf(SharePref.getInstance().getString(SharePref.spin_discount));     //0= visible

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ChipsBuyModel model = new ChipsBuyModel();
                                    model.setId(jsonObject1.getString("id"));
                                    model.setCoin(jsonObject1.getString("coin"));
                                    model.title = jsonObject1.getString("title");
                                    model.setAmount(jsonObject1.getString("price"));
                                    if (str_extra.equalsIgnoreCase("") || str_extra.equalsIgnoreCase("0"))
                                        model.setSpinner_result(String.valueOf("0"));
                                    else
                                        model.setSpinner_result(String.valueOf(str_extra));

                                    Log.d("spinner_result", "onResponse() returned: " + str_extra);

                                    historyModelArrayList.add(model);
                                }

                                historyAdapter = new ChipsBuy_SpinAdapter(Spinner_Wheels.this, historyModelArrayList);
                                rec_history.setAdapter(historyAdapter);
                            } else {
                                linear_no_history.setVisibility(View.VISIBLE);
                                // Funtions.showToast(HistoryActivity.this, ""+message);
                                progressDialog.dismiss();
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                findViewById(R.id.progressBar).setVisibility(View.GONE);

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
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                HashMap<String, String> params = new HashMap<>();
                params.put("token", prefs.getString("token", ""));
                params.put("user_id", prefs.getString("user_id", ""));
                //params.put("user_id", SharedPref.getVal(HistoryActivity.this,SharedPref.id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Spinner_Wheels.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}