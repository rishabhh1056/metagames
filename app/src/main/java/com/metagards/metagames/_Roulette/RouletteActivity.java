package com.metagards.metagames._Roulette;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.R;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouletteActivity extends AppCompatActivity {

    // sectors of our wheel (look at the image to see the sectors)
    private static final String[] sectors = {"32 red", "15 black",
            "19 red", "4 black", "21 red", "2 black", "25 red", "17 black", "34 red",
            "6 black", "27 red", "13 black", "36 red", "11 black", "30 red", "8 black",
            "23 red", "10 black", "5 red", "24 black", "16 red", "33 black",
            "1 red", "20 black", "14 red", "31 black", "9 red", "22 black",
            "18 red", "29 black", "7 red", "28 black", "12 red", "35 black",
            "3 red", "26 black", "zero"};

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
    private static final float HALF_SECTOR = 360f / 37f / 2f;

    Context context =this;
    LinearLayout lnrfollow, lnr_firstRow, lnr_secondRow, lnr_thirdRow;
    String tagamountselected = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_roulette);
        ButterKnife.bind(this);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        lnrfollow = findViewById(R.id.lnrfollow);
        lnr_firstRow = findViewById(R.id.lnr_firstRow);
        lnr_secondRow = findViewById(R.id.lnr_secondRow);

        addChipsonView();
        addViewFirstRow();
        addViewSecondRow();
    }
    @OnClick(R.id.spinBtn)
    public void spin(View v) {
        degreeOld = degree % 360;
        // we calculate random angle for rotation of our wheel
        degree = RANDOM.nextInt(360) + 720;
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
                resultTv.setText(getSector(360 - (degree % 360)));
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

    private void addChipsonView() {
        lnrfollow.removeAllViews();
        chipsDefaultSelection = false;
        addCategoryInView("10", R.drawable.coin_10);
        addCategoryInView("50", R.drawable.coin_50);
        addCategoryInView("100", R.drawable.coin_100);
//        addCategoryInView("500", R.drawable.coin_500);
        addCategoryInView("1000", R.drawable.coin_1000);
        addCategoryInView("5000", R.drawable.coin_5000);
//        addCategoryInView("7500");
    }

    private void addViewFirstRow() {
        lnr_firstRow.removeAllViews();
        chipsDefaultSelection = false;
        addViewfirstRow("3", R.color.red);
        addViewfirstRow("6", R.color.black);
        addViewfirstRow("9",  R.color.red);
        addViewfirstRow("12",  R.color.red);
        addViewfirstRow("15", R.color.black);
        addViewfirstRow("18",  R.color.red);
        addViewfirstRow("21",  R.color.red);
        addViewfirstRow("24", R.color.black);
        addViewfirstRow("27",  R.color.red);
        addViewfirstRow("30",  R.color.red);
        addViewfirstRow("33", R.color.black);
        addViewfirstRow("36",  R.color.red);
    }
    private void addViewSecondRow() {
        lnr_secondRow.removeAllViews();
        chipsDefaultSelection = false;
        addViewfirstRow("2", R.color.black);
        addViewfirstRow("5", R.color.red);
        addViewfirstRow("8",  R.color.black);
        addViewfirstRow("11",  R.color.black);
        addViewfirstRow("14", R.color.red);
        addViewfirstRow("17",  R.color.black);
        addViewfirstRow("20",  R.color.black);
        addViewfirstRow("23", R.color.red);
        addViewfirstRow("26",  R.color.black);
        addViewfirstRow("29",  R.color.black);
        addViewfirstRow("32", R.color.red);
        addViewfirstRow("35",  R.color.black);
    }

    boolean chipsDefaultSelection = false;
    private void addCategoryInView(String cat, int img) {
        View view = LayoutInflater.from(context).inflate(R.layout.cat_txtview_chip_bg, null);
        TextView txtview = view.findViewById(R.id.txt_cat);
//        txtview.setVisibility(View.INVISIBLE);
        txtview.setText(cat + "");
        txtview.setBackgroundResource(img);
        view.setTag(cat + "");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                tagamountselected = (String) view1.getTag();
                TextView txt = view1.findViewById(R.id.txt_cat);
//                txt.setTextColor(Color.parseColor("#ffffff"));
                SharedPreferences.Editor editor = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("tag", tagamountselected);
                editor.apply();
                setSelectedType(tagamountselected);
//                GameAmount = Integer.parseInt(tagamountselected);
            }
        });

        if(!chipsDefaultSelection)
        {
            chipsDefaultSelection = true;
            tagamountselected = (String) view.getTag();
            SharedPreferences.Editor editor = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("tag", tagamountselected);
            editor.apply();
            setSelectedType(tagamountselected);
        }
        lnrfollow.addView(view);
    }
    private void setSelectedType(String type) {
        LinearLayout lnrfollow = findViewById(R.id.lnrfollow);
        for (int i = 0; i < lnrfollow.getChildCount(); i++) {
            View view = lnrfollow.getChildAt(i);
            TextView txtview = view.findViewById(R.id.txt_cat);
            RelativeLayout relativeLayout = view.findViewById(R.id.relativeChip);
            if (txtview.getText().toString().equalsIgnoreCase(type)) {
                relativeLayout.setBackgroundResource(R.drawable.glow_circle_bg);
//                txtview.setTextColor(Color.parseColor("#ffffff"));
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) relativeLayout.getLayoutParams();
                int _20 = (int) getResources().getDimension(R.dimen.chip_up);
                mlp.setMargins(0, _20, 0, 0);
            } else {
                relativeLayout.setBackgroundResource(R.drawable.glow_circle_bg_transparent);
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) relativeLayout.getLayoutParams();
                mlp.setMargins(0, 0, 0, 0);
//                txtview.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            }
        }
    }


    //1st row
    private void addViewfirstRow(String cat, int color) {
        View view = LayoutInflater.from(context).inflate(R.layout.first_txtview_roulette_bg, null);
        TextView txtview = view.findViewById(R.id.txt_cat);
        CardView cardView = view.findViewById(R.id.cardView);
        cardView.setBackgroundResource(color);
//        txtview.setVisibility(View.INVISIBLE);
        txtview.setText(cat + "");
//        txtview.setBackgroundResource(color);
//        txtview.setBackgroundResource(img);
        view.setTag(cat + "");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                tagamountselected = (String) view1.getTag();
                TextView txt = view1.findViewById(R.id.txt_cat);
//                txt.setTextColor(Color.parseColor("#ffffff"));
                SharedPreferences.Editor editor = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("tag", tagamountselected);
                editor.apply();
                setSelectedType(tagamountselected);
//                GameAmount = Integer.parseInt(tagamountselected);
            }
        });

        if(!chipsDefaultSelection)
        {
            chipsDefaultSelection = true;
            tagamountselected = (String) view.getTag();
            SharedPreferences.Editor editor = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("tag", tagamountselected);
            editor.apply();
            setSelectedType(tagamountselected);
        }
        lnr_firstRow.addView(view);
    }
}