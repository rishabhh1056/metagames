package com.metagards.metagames._TeenPatti;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.metagards.metagames.Activity.BuyChipsList;
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.ApiClasses.Dealer;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._TeenPatti.menu.DialogRulesTeenpatti;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.metagards.metagames.Fragments.ActiveTables_BF.SEL_TABLE;
import static com.metagards.metagames.Utils.Variables.TEENPATTIPUBLIC;

import androidx.core.content.ContextCompat;


public class PublicTable_New extends BaseActivity {
    public static final String CUSTOME_TABLE = "custome_table";
    public static final String PRIVATE_TABLE = "private_table";
    String TAG = "PublicTable_New";

    Dealer dealer = new Dealer();
    private String game_amount;
    Animation animBounce, animBlink, animMove1_2, animMove2_2, animMove3_2, animMove4_2, animMove5_2;
    public static final String MY_PREFS_NAME = "Login_data";
    Button btnCreateGame, btnStartGame,
            btnpl1number;
    ImageView imgsee1, imgseen1,  imgsee2, imgsee3, imgsee4, imgsee5, imgpack1, imgpack2, imgpack3, imgpack4,txtGameFinish,
            imgpack5, imgshow1, imgshow2, imgshow3, imgshow4, imgshow5;
    TextView txtPlay1, txtPlay2, txtPlay3, txtPlay4, txtPlay5, txtSlidshow, txt_coin_to_girl_player1, txt_coin_to_girl_player2, txt_coin_to_girl_player3,
            txt_coin_to_girl_player4, txt_coin_to_girl_player5, txtTotalCoin, txtrefery_com,
    //txt_coin_to_girl_player_winner,
    txtPlay1wallet, txtBallence,txtPlay2wallet, txtCounttimer, txtCounttimer1, txtCounttimer2, txtCounttimer3,
            txtCounttimer4, txtCounttimer5, txtPlay3wallet, txtPlay4wallet, txtPlay5wallet, txtTableid,
            txtpl1packdis, txtpl2packdis, txtpl3packdis, txtpl4packdis, txtpl5packdis, txtWaitforOther;

    TextView imgblind1;
    String CHAAL = "Chaal";
    String BLIND = "Blind";

    Context context = this;
    LinearLayout lnrcards1, lnrcards2, lnrcardsplayerplayermain2, lnrcardsplayerplayermain3,
            lnrcardsplayerplayermain4, lnrcardsplayerplayermain5, lnrPlay2wallet, lnrPlay3wallet,
            lnrPlay4wallet, lnrPlay5wallet, lnrcardsmainplayer1, lnrShowButtoncardspl2, lnrShowButtoncardspl3, lnrShowButtoncardspl4,
            lnrShowButtoncardspl5,
            lnrSeeButtoncardspl1, lnrGameButton;
    TextView txtnotfound;
    RelativeLayout rltGameButton, rltSeeButtoncardspl1;
    private ImageView imgpl1hidencard1, imgampire, imgpl1minus, imgpl1plus, imginvite2, imginvite3, imginvite4, imginvite5, imgpl1show, imgpl1pack, imgpl1chaal,
            imgback, imginfo, imgsetting, imgpl1circle, imgpl2circle, imgpl3circle, imgpl4circle,
            imgpl5circle, imgpl1winner, imgpl1winnerstar, imgpl2winnerstar, imgpl3winnerstar,
            imgpl4winnerstar, imgpl5winnerstar, imgpl2winner, imgpl3winner, imgpl4winner,
            imgpl1winnerpatti, imgpl2winnerpatti, imgpl3winnerpatti, imgpl4winnerpatti,
            imgpl5winnerpatti, imgchat, imgchat1,
            imgpl1glow, imgpl2glow, imgpl3glow, imgpl4glow, imgpl5glow,
            imgpl5winner, imgchipuser1, imgchipuser2, imgchipuser3, imgchipuser4, imgchipuser5, imgTip,
            imgwaiting2, imgwaiting3, imgwaiting4, imgwaiting5,
            imgpl2showcard1, imgpl2showcard2,
            imgpl2showcard3, imgpl3showcard1, imgpl3showcard2, imgpl3showcard3, imgpl4showcard1,
            imgpl4showcard2, imgpl4showcard3, imgpl5showcard1, imgpl5showcard2, imgpl5showcard3,
            imgpl1hidencard2, imgpl1hidencard3;

    private View imgpl1Frame,imgpl2Frame,imgpl3Frame,imgpl4Frame,imgpl5Frame;


    View  imgclose, imgaccespt;
    ImageView imggift1,imggift2, imggift3, imggift4, imggift5;

    private ImageView imgplayer2first, imgplayer2second, imgplayer2third, imgplayermain1,
            imgplayer2mainfirst, imgplayer3mainfirst, imgplayer4mainfirst, imgplayer5mainfirst;
    private String play2id, play3id, play4id, play5id;
    Animation animMove1, animMove2, animMove3, animMove4, animMove5;
    Thread thread;
    int pStatus = 100;
    int pStatusprogress = 0;
    Timer timerstatus;
    private Handler handler = new Handler();
    String chaak_user_id;
    String game_id = "";
    boolean isSeenUser = false;
    boolean isGamenotStarted = true;
    private Animation animMoveCardsPlayer1, animMoveCardsPlayer2, animMoveCardsPlayer3,
            animMoveCardsPlayer4, animMoveCardsPlayer5, animAmpireblink, animMoveCardsPlayerwinner1, animMoveCardsPlayerwinner2, animMoveCardsreferycomi,
            animMoveCardsPlayerwinner3, animMoveCardsPlayerwinner4, animMoveCardsPlayerwinner5;
    private String table_amount = "";
    private String walletplayer1, walletplayer2, walletplayer3, walletplayer4, walletplayer5;
    String table_id = "";
    String user_id_player1 = "";
    String user_id_player2 = "";
    String user_id_player3 = "";
    String user_id_player4 = "";
    String user_id_player5 = "";

    String user_pack1 = "1";
    String user_pack2 = "1";
    String user_pack3 = "1";
    String user_pack4 = "1";
    String user_pack5 = "1";


    String guser_id_player1 = "";
    String guser_id_player2 = "";
    String guser_id_player3 = "";
    String guser_id_player4 = "";
    String guser_id_player5 = "";
    int tips = 100;
    Typeface helvatikaboldround, helvatikabold, helvatikanormal, quitestylish;
    private String boot_value = "0", maximum_blind = "0", chaal_limit = "0", pot_limit = "0";
    boolean isGameStartforseeebtn = false;
    public String action = "";


    private RelativeLayout rltplayer1, rltSee1, rltSee2, rltSee3, rltSee4, rltSee5, rltplayer2,
            rltplayer1growing, rltGameFinish,
            rltplayer2growing,
            rltplayer3growing, rltplayer4growing, rltplayer5growing, rltslidshow;
    private AnimatorSet mAnimationSet;
    int timertime = 4000;
    boolean isPlayerPlayedChaal = true;
    CountDownTimer mCountDownTimer1, mCountDownTimer2, mCountDownTimer3, mCountDownTimer4,
            mCountDownTimer5, slidshowcounter;
    CountDownTimer counttimerforstartgame;
    ProgressBar mProgress1, mProgress2, mProgress3, mProgress4, mProgress5;
    boolean isProgressrun1 = true;
    boolean isProgressrun2 = true;
    boolean isProgressrun3 = true;
    boolean isProgressrun4 = true;
    boolean isProgressrun5 = true;

    String prev_id = "";

    int count = 8;
    String sentamounttype = "0";
    String typeUrl;
    String chaal_user_played_id = "";
    float updatedamount = 0;
    int timmersectlarge = 38000;
    int timmersectsmall = 1000;
    int playercount = 0;
    String slidshow_id = "";
    String shareMessage = "";

    private final String TOKEN = Const.TOKEN;
    private final String IMGAE_PATH = Const.IMGAE_PATH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int value = display.getWidth();
        int densityDpi = (int) (metrics.density * 160f);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);

        String size = getScreenSize();
        double doublesize = Double.parseDouble(size);
        if (doublesize > 6) {

            setContentView(R.layout.public_tablev3);

        } else {

           setContentView(R.layout.public_tablev3);
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        clickTask();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String img = prefs.getString("img_name", "");
        String wallet = prefs.getString("wallet", "");
        imgchipuser1.setVisibility(View.VISIBLE);
        float numberwallet = Float.parseFloat(wallet);
        txtPlay1wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberwallet));
        txtBallence.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberwallet));
        Picasso.get().load(Const.IMGAE_PATH + img).into(imgpl1circle);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            table_id = uri.getQueryParameter("table_id");
            System.out.println("scs");
            getGame(Const.GAME_TABLE_JOIN);
        } else {
            table_id = "";

            if(getIntent().hasExtra(SEL_TABLE))
            {
                getGame(Const.GAME_TABLE);
            }
            else {
                String gameType = getIntent().getStringExtra("gametype");
                String bootvalue = getIntent().getStringExtra("bootvalue");
                table_id = "";
                if(gameType.equals(CUSTOME_TABLE))
                {
                    getGame(Const.CUSTOMISED_GAME_TABLE,bootvalue);
                }
                else if(gameType.equals(PRIVATE_TABLE))
                {
                    getGame(Const.PRI_GAME_TABLE_CREAT,bootvalue);
                }
            }

        }

        animationtask();

        //Progress -
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        mProgress1 = (ProgressBar) findViewById(R.id.circularProgressbar1);
        mProgress1.setProgressDrawable(drawable);
        mProgress1.setProgress(pStatusprogress);
        mProgress1.setMax(timmersectlarge/1000);


        mCountDownTimer1 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public   void onTick(long millisUntilFinished) {
                imgpl1glow.setVisibility(View.VISIBLE);
                isProgressrun1 = false;
//                pStatus--;
                pStatus -= 3;
                pStatusprogress++;
                mProgress1.setProgress((int) ((int) pStatusprogress * 2.5));

                if(pStatus == 52){
                    pStatusprogress = 0;
                    mProgress1.setProgress((int) pStatusprogress * 2);
                    mProgress1.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN );
                }

                if (pStatus < 25) {
                    PlaySaund(R.raw.teenpattitick);
                }

            }

            @Override
            public void onFinish() {
                pStatusprogress = 0;
                mProgress1.setProgress(100);
                mProgress1.setProgress(0);
                imgpl1glow.setVisibility(View.GONE);
                isProgressrun1 = true;
                //GamePack("1");

            }

        };

        // mCountDownTimer1.start();

        Resources res2 = getResources();
        Drawable drawable2 = res2.getDrawable(R.drawable.circular);
        mProgress2 = (ProgressBar) findViewById(R.id.circularProgressbar2);
        mProgress2.setProgressDrawable(drawable2);
        mProgress2.setProgress(pStatusprogress);
        mProgress2.setMax(timmersectlarge/1000);


        mCountDownTimer2 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
//                pStatus++;
//                mProgress.setProgress((int)pStatus*1);
                imgpl2glow.setVisibility(View.VISIBLE);
                isProgressrun2 = false;
                pStatusprogress++;
                mProgress2.setProgress((int) pStatusprogress * 1);
                pStatus--;

            }

            @Override
            public void onFinish() {
                isProgressrun2 = true;
                pStatusprogress = 0;
//                pStatus++;
                mProgress2.setProgress(100);
                mProgress2.setProgress(0);
                imgpl2glow.setVisibility(View.GONE);
                //GamePack("1");

                //  }
            }
        };


        Resources res3 = getResources();
        Drawable drawable3 = res3.getDrawable(R.drawable.circular);
        mProgress3 = (ProgressBar) findViewById(R.id.circularProgressbar3);
        mProgress3.setProgressDrawable(drawable3);
        mProgress3.setProgress(pStatusprogress);
        mProgress3.setMax(timmersectlarge/1000);


        mCountDownTimer3 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public void onTick(long millisUntilFinished) {

                imgpl3glow.setVisibility(View.VISIBLE);
                isProgressrun3 = false;
                pStatusprogress++;
                mProgress3.setProgress((int) pStatusprogress * 1);

                pStatus--;

            }

            @Override
            public void onFinish() {

                isProgressrun3 = true;
                pStatusprogress = 0;
                mProgress3.setProgress(100);
                mProgress3.setProgress(0);
                imgpl3glow.setVisibility(View.GONE);

            }


        };


        Resources res4 = getResources();
        Drawable drawable4 = res4.getDrawable(R.drawable.circular);
        mProgress4 = (ProgressBar) findViewById(R.id.circularProgressbar4);
        mProgress4.setProgressDrawable(drawable4);
        mProgress4.setProgress(pStatusprogress);
        mProgress4.setMax(timmersectlarge/1000);


        mCountDownTimer4 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
//                pStatus++;
//                mProgress.setProgress((int)pStatus*1);
                imgpl4glow.setVisibility(View.VISIBLE);
                isProgressrun4 = false;
                pStatusprogress++;
                mProgress4.setProgress((int) pStatusprogress * 1);
                pStatus--;

            }

            @Override
            public void onFinish() {
//                pStatus++;
//                mProgress.setProgress(100);
                imgpl4glow.setVisibility(View.GONE);
                //if (chaak_user_id.equals(user_id_player1)){
                isProgressrun4 = true;
                pStatusprogress = 0;
//                pStatus++;
                mProgress4.setProgress(100);
                mProgress4.setProgress(0);
                // GamePack("1");

                //  }

            }
        };

        Resources res5 = getResources();
        Drawable drawable5 = res5.getDrawable(R.drawable.circular);
        mProgress5 = (ProgressBar) findViewById(R.id.circularProgressbar5);
        mProgress5.setProgressDrawable(drawable5);
        mProgress5.setProgress(pStatusprogress);
        mProgress5.setMax(timmersectlarge/1000);


        mCountDownTimer5 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
//                pStatus++;
//                mProgress.setProgress((int)pStatus*1);
                imgpl5glow.setVisibility(View.VISIBLE);
                isProgressrun5 = false;
                pStatusprogress++;
                mProgress5.setProgress((int) pStatusprogress * 1);
                pStatus--;
                //mProgress.setProgress((int)pStatus*1);
                //txtCounttimer5.setVisibility(View.VISIBLE);
                // txtCounttimer5.setText(pStatus+"");

//                if (pStatus < 25){
//                    PlaySaund(R.raw.teenpattitick);
//                }


            }

            @Override
            public void onFinish() {
//                pStatus++;
//                mProgress.setProgress(100);
                imgpl5glow.setVisibility(View.GONE);
                //if (chaak_user_id.equals(user_id_player1)){
                isProgressrun5 = true;
                pStatusprogress = 0;
//                pStatus++;
                mProgress5.setProgress(100);
                mProgress5.setProgress(0);
                // GamePack("1");

                //  }

            }


        };

        counttimerforstartgame = new CountDownTimer(8000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                count--;
                txtGameFinish.setVisibility(View.VISIBLE);
                rltGameFinish.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFinish() {
                txtGameFinish.setVisibility(View.GONE);
                rltGameFinish.setVisibility(View.GONE);
                GameStart();

            }


        };
        count = 8;
        counttimerforstartgame.start();

        timerstatus = new Timer();
        timerstatus.scheduleAtFixedRate(new TimerTask() {

                                            @Override
                                            public void run() {

                                                if (table_id.trim().length() > 0 && rltslidshow.getVisibility() == View.GONE) {

                                                    GameStatus();

                                                }

                                            }

                                        },
//Set how long before to start calling the TimerTask (in milliseconds)
                timertime,
//Set the amount of time between each execution (in milliseconds)
                timertime);


        slidshowcounter = new CountDownTimer(20000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                GameSideAction(slidshow_id, "2");
            }

        };
    }

    public void init() {

        RelativeLayout rltParentLayout = findViewById(R.id.rltParentLayout);
        ImageView imgTable = findViewById(R.id.imgTable);

//        SetBackgroundImageAsDisplaySize(this,rltParentLayout,R.drawable.bgnew1);
//        Glide.with(this).load(R.drawable.table_v5).into(imgTable);

        helvatikaboldround = Typeface.createFromAsset(getAssets(), "fonts/helvetica-rounded-bold-5871d05ead8de.otf");
        helvatikabold = Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Bold.ttf");
        helvatikanormal = Typeface.createFromAsset(getAssets(), "fonts/Helvetica.ttf");
        quitestylish = Typeface.createFromAsset(getAssets(), "fonts/QuiteMagicalRegular-8VA2.ttf");


        txtTotalCoin = (TextView) findViewById(R.id.txtTotalCoin);
        txtTotalCoin.setTypeface(helvatikaboldround);

        txtrefery_com = (TextView) findViewById(R.id.txtrefery_com);
        txtrefery_com.setTypeface(helvatikaboldround);

        btnCreateGame = findViewById(R.id.btnCreateGame);
        btnStartGame = findViewById(R.id.btnStartGame);
        txtPlay1 = findViewById(R.id.txtPlay1);
        txtPlay1.setTypeface(helvatikaboldround);
        txtPlay2 = findViewById(R.id.txtPlay2);
        txtPlay2.setTypeface(helvatikaboldround);
        txtPlay3 = findViewById(R.id.txtPlay3);
        txtPlay3.setTypeface(helvatikaboldround);
        txtPlay4 = findViewById(R.id.txtPlay4);
        txtPlay4.setTypeface(helvatikaboldround);
        txtPlay5 = findViewById(R.id.txtPlay5);


        txtSlidshow = findViewById(R.id.txtSlidshow);

        txtPlay5.setTypeface(helvatikaboldround);


        txtSlidshow.setTypeface(helvatikaboldround);


        imgsee1 = findViewById(R.id.imgsee1);
        imgseen1 = findViewById(R.id.imgseen1);
        imgblind1 = findViewById(R.id.imgblind1);

        ChangeTexttoChaal(false);

        // btnswtichtable = findViewById(R.id.btnswtichtable);


        btnpl1number = findViewById(R.id.btnpl1number);
        btnpl1number.setTypeface(helvatikanormal);

        imgpl1show = findViewById(R.id.imgpl1show);
        imgpl1chaal = findViewById(R.id.imgpl1chaal);
        imgpl1pack = findViewById(R.id.imgpl1pack);
        imgpl1minus = findViewById(R.id.imgpl1minus);
        imgpl1plus = findViewById(R.id.imgpl1plus);

        imginvite2 = findViewById(R.id.imginvite2);
        imginvite3 = findViewById(R.id.imginvite3);
        imginvite4 = findViewById(R.id.imginvite4);
        imginvite5 = findViewById(R.id.imginvite5);


        imggift1 = findViewById(R.id.imggift1);
        imggift2 = findViewById(R.id.imggift2);
        imggift3 = findViewById(R.id.imggift3);
        imggift4 = findViewById(R.id.imggift4);
        imggift5 = findViewById(R.id.imggift5);

        imgpl1glow = findViewById(R.id.imgpl1glow);
        imgpl2glow = findViewById(R.id.imgpl2glow);
        imgpl3glow = findViewById(R.id.imgpl3glow);
        imgpl4glow = findViewById(R.id.imgpl4glow);
        imgpl5glow = findViewById(R.id.imgpl5glow);


        imgchipuser1 = findViewById(R.id.imgchipuser1);
        imgchipuser2 = findViewById(R.id.imgchipuser2);
        imgchipuser3 = findViewById(R.id.imgchipuser3);
        imgchipuser4 = findViewById(R.id.imgchipuser4);
        imgchipuser5 = findViewById(R.id.imgchipuser5);

        imgTip = findViewById(R.id.imgTip);


        imgwaiting2 = findViewById(R.id.imgwaiting2);
        imgwaiting3 = findViewById(R.id.imgwaiting3);
        imgwaiting4 = findViewById(R.id.imgwaiting4);
        imgwaiting5 = findViewById(R.id.imgwaiting5);


//    imgplayer1first = findViewById(R.id.imgplayer1first);
//    imgplayer1second = findViewById(R.id.imgplayer1second);
//    imgplayer1third = findViewById(R.id.imgplayer1third);

        imgampire = findViewById(R.id.imgampire);

        Glide.with(context) .asGif()
                .load(R.drawable.girle_character_teen).into(imgampire);
        imgback = findViewById(R.id.imgback);

        imgsetting = findViewById(R.id.imgsetting);
        imginfo = findViewById(R.id.imginfo);

        imgpl1circle = findViewById(R.id.imgpl1circle);
        imgpl2circle = findViewById(R.id.imgpl2circle);
        imgpl3circle = findViewById(R.id.imgpl3circle);
        imgpl4circle = findViewById(R.id.imgpl4circle);
        imgpl5circle = findViewById(R.id.imgpl5circle);

        imgpl1winner = findViewById(R.id.imgpl1winner);
        imgpl1winnerstar = findViewById(R.id.imgpl1winnerstar);
        imgpl2winner = findViewById(R.id.imgpl2winner);

        Glide.with(context) .asGif()
                .load(R.drawable.giphy).into(imgpl2winner);
        imgpl2winnerstar = findViewById(R.id.imgpl2winnerstar);
        Glide.with(context) .asGif()
                .load(R.drawable.star).into(imgpl2winnerstar);

        imgpl3winner = findViewById(R.id.imgpl3winner);
        Glide.with(context) .asGif()
                .load(R.drawable.giphy).into(imgpl3winner);

        imgpl3winnerstar = findViewById(R.id.imgpl3winnerstar);
        Glide.with(context) .asGif()
                .load(R.drawable.star).into(imgpl3winnerstar);

        imgpl4winner = findViewById(R.id.imgpl4winner);
        Glide.with(context) .asGif()
                .load(R.drawable.giphy).into(imgpl4winner);

        imgpl4winnerstar = findViewById(R.id.imgpl4winnerstar);
        Glide.with(context) .asGif()
                .load(R.drawable.star).into(imgpl4winnerstar);

        imgpl5winner = findViewById(R.id.imgpl5winner);
        Glide.with(context) .asGif()
                .load(R.drawable.giphy).into(imgpl5winner);

        imgpl5winnerstar = findViewById(R.id.imgpl5winnerstar);
        Glide.with(context) .asGif()
                .load(R.drawable.star).into(imgpl5winnerstar);

        imgpl1winnerpatti = findViewById(R.id.imgpl1winnerpatti);
        imgpl2winnerpatti = findViewById(R.id.imgpl2winnerpatti);

        Glide.with(context) .asGif()
                .load(R.drawable.winner_patti).into(imgpl2winnerpatti);
        imgpl3winnerpatti = findViewById(R.id.imgpl3winnerpatti);
        imgpl4winnerpatti = findViewById(R.id.imgpl4winnerpatti);

        Glide.with(context) .asGif()
                .load(R.drawable.winner_patti).into(imgpl4winnerpatti);

        imgpl5winnerpatti = findViewById(R.id.imgpl5winnerpatti);
        Glide.with(context) .asGif()
                .load(R.drawable.winner_patti).into(imgpl5winnerpatti);

        imgpl1Frame = findViewById(R.id.imgpl1Frame);
        imgpl2Frame = findViewById(R.id.imgpl2Frame);
        imgpl3Frame = findViewById(R.id.imgpl3Frame);
        imgpl4Frame = findViewById(R.id.imgpl4Frame);
        imgpl5Frame = findViewById(R.id.imgpl5Frame);

//        imgpl1Frame.setVisibility(View.GONE);
//        imgpl2Frame.setVisibility(View.GONE);
//        imgpl3Frame.setVisibility(View.GONE);
//        imgpl4Frame.setVisibility(View.GONE);
//        imgpl5Frame.setVisibility(View.GONE);

        imgsee1 = findViewById(R.id.imgsee1);
        imgsee2 = findViewById(R.id.imgsee2);
        imgsee3 = findViewById(R.id.imgsee3);
        imgsee4 = findViewById(R.id.imgsee4);
        imgsee5 = findViewById(R.id.imgsee5);

        imgpack1 = findViewById(R.id.imgpack1);
        imgpack2 = findViewById(R.id.imgpack2);
        imgpack3 = findViewById(R.id.imgpack3);
        imgpack4 = findViewById(R.id.imgpack4);
        imgpack5 = findViewById(R.id.imgpack5);

        imgshow1 = findViewById(R.id.imgshow1);
        imgshow2 = findViewById(R.id.imgshow2);
        imgshow3 = findViewById(R.id.imgshow3);
        imgshow4 = findViewById(R.id.imgshow4);
        imgshow5 = findViewById(R.id.imgshow5);

        rltplayer1 = findViewById(R.id.rltplayer1);
        rltplayer2 = findViewById(R.id.rltplayer2);

        rltSee1 = findViewById(R.id.rltSee1);
        rltSee2 = findViewById(R.id.rltSee2);
        rltSee3 = findViewById(R.id.rltSee3);
        rltSee4 = findViewById(R.id.rltSee4);
        rltSee5 = findViewById(R.id.rltSee5);

        txtBallence = findViewById(R.id.txtBallence);
        txtPlay1wallet = findViewById(R.id.txtPlay1wallet);
        txtPlay1wallet.setTypeface(helvatikaboldround);
        txtPlay2wallet = findViewById(R.id.txtPlay2wallet);
        txtPlay2wallet.setTypeface(helvatikaboldround);
        txtCounttimer = findViewById(R.id.txtCounttimer);
        txtCounttimer1 = findViewById(R.id.txtCounttimer1);
        txtCounttimer2 = findViewById(R.id.txtCounttimer2);
        txtCounttimer3 = findViewById(R.id.txtCounttimer3);
        txtCounttimer4 = findViewById(R.id.txtCounttimer4);
        txtCounttimer5 = findViewById(R.id.txtCounttimer5);

        txtPlay3wallet = findViewById(R.id.txtPlay3wallet);
        txtPlay3wallet.setTypeface(helvatikaboldround);
        txtPlay4wallet = findViewById(R.id.txtPlay4wallet);
        txtPlay4wallet.setTypeface(helvatikaboldround);

        txtPlay5wallet = findViewById(R.id.txtPlay5wallet);
        txtPlay5wallet.setTypeface(helvatikaboldround);

        txtTableid = findViewById(R.id.txtTableid);

        txtpl1packdis = findViewById(R.id.txtpl1packdis);
        txtpl2packdis = findViewById(R.id.txtpl2packdis);
        txtpl3packdis = findViewById(R.id.txtpl3packdis);
        txtpl4packdis = findViewById(R.id.txtpl4packdis);
        txtpl5packdis = findViewById(R.id.txtpl5packdis);


        txtGameFinish = findViewById(R.id.txtGameFinish);
        Glide.with(context) .asGif()
                .load(R.drawable.loading_new).into(txtGameFinish);
        rltGameFinish = findViewById(R.id.rltGameFinish);

        txtWaitforOther = findViewById(R.id.txtWaitforOther);


        imgpl1hidencard1 = findViewById(R.id.imgpl1hidencard1);
        imgpl1hidencard2 = findViewById(R.id.imgpl1hidencard2);
        imgpl1hidencard3 = findViewById(R.id.imgpl1hidencard3);

        imgaccespt = findViewById(R.id.imgaccespt);
        imgclose = findViewById(R.id.imgclose);



        rltplayer1growing = findViewById(R.id.rltplayer1growing);
        rltplayer2growing = findViewById(R.id.rltplayer2growing);
        rltplayer3growing = findViewById(R.id.rltplayer3growing);
        rltplayer4growing = findViewById(R.id.rltplayer4growing);
        rltplayer5growing = findViewById(R.id.rltplayer5growing);

        rltslidshow = findViewById(R.id.rltslidshow);


        //rltwinnerpl2ani = findViewById(R.id.rltwinnerpl2ani);

//    imgplayer2first = findViewById(R.id.imgplayer2first);
//    imgplayer2second = findViewById(R.id.imgplayer2second);
//    imgplayer2third = findViewById(R.id.imgplayer2third);


        lnrcardsmainplayer1 = findViewById(R.id.lnrcardsmainplayer1);
        lnrcardsplayerplayermain2 = findViewById(R.id.lnrcardsplayerplayermain2);
        lnrcardsplayerplayermain3 = findViewById(R.id.lnrcardsplayerplayermain3);
        lnrcardsplayerplayermain4 = findViewById(R.id.lnrcardsplayerplayermain4);
        lnrcardsplayerplayermain5 = findViewById(R.id.lnrcardsplayerplayermain5);


        lnrPlay2wallet = findViewById(R.id.lnrPlay2wallet);
        lnrPlay3wallet = findViewById(R.id.lnrPlay3wallet);
        lnrPlay4wallet = findViewById(R.id.lnrPlay4wallet);
        lnrPlay5wallet = findViewById(R.id.lnrPlay5wallet);


        lnrShowButtoncardspl2 = findViewById(R.id.lnrShowButtoncardspl2);
        lnrShowButtoncardspl3 = findViewById(R.id.lnrShowButtoncardspl3);
        lnrShowButtoncardspl4 = findViewById(R.id.lnrShowButtoncardspl4);
        lnrShowButtoncardspl5 = findViewById(R.id.lnrShowButtoncardspl5);

        imgpl2showcard1 = findViewById(R.id.imgpl2showcard1);
        imgpl2showcard2 = findViewById(R.id.imgpl2showcard2);
        imgpl2showcard3 = findViewById(R.id.imgpl2showcard3);

        imgpl3showcard1 = findViewById(R.id.imgpl3showcard1);
        imgpl3showcard2 = findViewById(R.id.imgpl3showcard2);
        imgpl3showcard3 = findViewById(R.id.imgpl3showcard3);

        imgpl4showcard1 = findViewById(R.id.imgpl4showcard1);
        imgpl4showcard2 = findViewById(R.id.imgpl4showcard2);
        imgpl4showcard3 = findViewById(R.id.imgpl4showcard3);

        imgpl5showcard1 = findViewById(R.id.imgpl5showcard1);
        imgpl5showcard2 = findViewById(R.id.imgpl5showcard2);
        imgpl5showcard3 = findViewById(R.id.imgpl5showcard3);


        lnrSeeButtoncardspl1 = findViewById(R.id.lnrSeeButtoncardspl1);
        rltSeeButtoncardspl1 = findViewById(R.id.rltSeeButtoncardspl1);


        lnrGameButton = findViewById(R.id.lnrGameButton);
        rltGameButton = findViewById(R.id.rltGameButton);
        ManageBottomActionButton(false);
        //SlideUP(lnrGameButton,context);


        txt_coin_to_girl_player1 = findViewById(R.id.txt_coin_to_girl_player1);

        txt_coin_to_girl_player2 = findViewById(R.id.txt_coin_to_girl_player2);
        txt_coin_to_girl_player3 = findViewById(R.id.txt_coin_to_girl_player3);
        txt_coin_to_girl_player4 = findViewById(R.id.txt_coin_to_girl_player4);
        txt_coin_to_girl_player5 = findViewById(R.id.txt_coin_to_girl_player5);

        txt_coin_to_girl_player1.setTypeface(helvatikaboldround);
        txt_coin_to_girl_player2.setTypeface(helvatikaboldround);
        txt_coin_to_girl_player3.setTypeface(helvatikaboldround);
        txt_coin_to_girl_player4.setTypeface(helvatikaboldround);
        txt_coin_to_girl_player5.setTypeface(helvatikaboldround);

        //txt_coin_to_girl_player_winner = findViewById(R.id.txt_coin_to_girl_player_winner);


        imgplayermain1 = findViewById(R.id.imgplayermain1);
        imgplayer2mainfirst = findViewById(R.id.imgplayer2mainfirst);
        imgplayer3mainfirst = findViewById(R.id.imgplayer3mainfirst);
        imgplayer4mainfirst = findViewById(R.id.imgplayer4mainfirst);
        imgplayer5mainfirst = findViewById(R.id.imgplayer5mainfirst);

        imgchat = findViewById(R.id.imgchat);
        imgchat1 = findViewById(R.id.imgchat1);

    }

    private void ChangeTexttoChaal(boolean isChaal){

        imgblind1.setText(!isChaal ? ""+BLIND : CHAAL);
//        imgblind1.setBackground(isChaal
//                ? getDrawable(R.drawable.ic_chal_btn)
//                : getDrawable(R.drawable.btn_blind));

    }

    public void clickTask() {

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GameSideAction(slidshow_id, "2");

            }
        });


        imgaccespt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSlideShow = true;
                GameSideAction(slidshow_id, "1");


            }
        });


        imgTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Functions.showToast(context, "Coming Soon",
//                        Toast.LENGTH_LONG).show();

                Functions.showTipsDialog(context, dealer, imgampire, new Callback() {
                    @Override
                    public void Responce(String resp,String type,Bundle bundle) {

                        int tips = Integer.parseInt(resp);

                        dealer.tips = dealer.tips + tips;

                    }
                });

            }
        });
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String img_name = prefs.getString("img_name", "");
        final Bottom_GameChating_F bottom_gameChating_f = Bottom_GameChating_F.newInstence(game_id,img_name);


        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bottom_gameChating_f.show(getSupportFragmentManager(),bottom_gameChating_f.getTag());

            }
        });

        imgchat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottom_gameChating_f.show(getSupportFragmentManager(),bottom_gameChating_f.getTag());

            }
        });

        imggift2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Functions.showToast(context, "Coming Soon",
//                        Toast.LENGTH_LONG).show();
                Functions.GiftSendto_User = user_id_player2;

                showGiftDialog("2");
            }
        });

        imggift3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Functions.showToast(context, "Coming Soon",
//                        Toast.LENGTH_LONG).show();
                Functions.GiftSendto_User = user_id_player3;

                showGiftDialog("3");
            }
        });

        imggift4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Functions.showToast(context, "Coming Soon",
//                        Toast.LENGTH_LONG).show();
                Functions.GiftSendto_User = user_id_player4;

                showGiftDialog("4");
            }
        });

        imggift5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Functions.showToast(context, "Coming Soon",
//                        Toast.LENGTH_LONG).show();

                Functions.GiftSendto_User = user_id_player5;

                showGiftDialog("5");
            }
        });


        imgsee1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SeeCards1();

            }
        });

        imgseen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SeeCards1();

            }
        });


        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameStart();
            }
        });

        imgpl1show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playercount > 2) {

                    //Functions.showToast(context, "Side Show Coming Soon.");

                    if (user_pack5.equals("0")) {

                        GameSideShow(user_id_player5);
                    } else if (user_pack4.equals("0")) {
                        GameSideShow(user_id_player4);

                    } else if (user_pack3.equals("0")) {
                        GameSideShow(user_id_player3);

                    } else {

                        Functions.showToast(context, "Side Show Coming Soon.");
                    }


                } else {

                    GameShow();


                }

            }
        });

        imgpl2circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();


                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String referal_code = prefs.getString("referal_code", "");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, referal_code);
                shareMessage = Functions.inviteTableLink(context,table_id,TEENPATTIPUBLIC);

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });

        imgpl3circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String referal_code = prefs.getString("referal_code", "");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                        referal_code);
                shareMessage = Functions.inviteTableLink(context,table_id,TEENPATTIPUBLIC);


                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });


        imgpl4circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String referal_code = prefs.getString("referal_code", "");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                        referal_code);
                shareMessage = Functions.inviteTableLink(context,table_id,TEENPATTIPUBLIC);


                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });


        imgpl5circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String referal_code = prefs.getString("referal_code", "");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                        referal_code);
                shareMessage = Functions.inviteTableLink(context,table_id,TEENPATTIPUBLIC);


                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });

        imgpl1minus.setEnabled(true);
        imgpl1minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentamounttype = "0";

                float value = Float.parseFloat(table_amount);
                updatedamount = value;

                String next = "<font color='#FFFFFF'>CHAAL   </font>";
                btnpl1number.setText(Html.fromHtml(next + "   " + updatedamount));

            }
        });
        imgpl1plus.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //GameShow();
                imgpl1minus.setEnabled(true);
                sentamounttype = "1";
                float value = Float.parseFloat(table_amount);
                updatedamount = value * 2;

                String next = "<font color='#FFFFFF'>CHAAL   </font>";
                btnpl1number.setText(Html.fromHtml(next + "   " + updatedamount));



            }
        });

        btnpl1number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isChalClick)
                    return;

                isChalClick = true;

                GameChhal();
            }
        });

        imgblind1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isChalClick)
                    return;

                isChalClick = true;

                GameChhal();
            }
        });

        imgpl1pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GamePack("0");
            }
        });

//    btnswtichtable.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            GameTableChange();
//        }
//    });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialoagonBack();
            }
        });

        findViewById(R.id.ChipstoUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyChipsList.class);
                startActivity(intent);
            }
        });

        imgsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showDialogSetting();

                Functions.showDialogSetting(context, new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {

                    }
                });

            }
        });


        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialoagoninfo();
            }
        });

    }
    boolean isChalClick = false;
    int total_cards = 0;
    int card1_count = 0;
    int card2_count = 0;
    int card3_count = 0;
    int card4_count = 0;
    int card5_count = 0;

    boolean isPlayer1 = false;
    boolean isPlayer2 = false;
    boolean isPlayer3 = false;
    boolean isPlayer4 = false;
    boolean isPlayer5 = false;
    int animation_speed = 250;
    public void animationtask() {

        animMove1 = AnimationUtils.loadAnimation(context,
                R.anim.movetodowntoup);

        animMove1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isPlayer1 = true;
                //int fina_total = Integer.parseInt(walletplayer1) -  Integer.parseInt
                // (table_amount);
                //  txtPlay1wallet.setText(fina_total+"");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                PlaySaund(R.raw.teenpattiwingamesound);
                isGamenotStarted = false;
//                lnrcardsmainplayer1.setVisibility(View.VISIBLE);
//                imgplayermain1.startAnimation(animMoveCardsPlayer1);

                lnrcardsmainplayer1.setVisibility(View.VISIBLE);
                imgplayermain1.setVisibility(View.VISIBLE);
                card1_count = 0;
                imgplayermain1.startAnimation(animMoveCardsPlayer1);
                txt_coin_to_girl_player1.clearAnimation();
                txt_coin_to_girl_player1.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove2 = AnimationUtils.loadAnimation(context,
                R.anim.movetodowntoup_corner);

        animMove2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isPlayer2 = true;

//                int fina_total = Integer.parseInt(walletplayer2) -  Integer.parseInt(table_amount);
//                txtPlay2wallet.setText(fina_total+"");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lnrcardsplayerplayermain2.setVisibility(View.VISIBLE);
                txtTotalCoin.setVisibility(View.VISIBLE);
                txt_coin_to_girl_player2.clearAnimation();
                txt_coin_to_girl_player2.setVisibility(View.GONE);
                card2_count = 0;
//                imgplayer2mainfirst.setVisibility(View.VISIBLE);
//                imgplayer2mainfirst.startAnimation(animMoveCardsPlayer2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove3 = AnimationUtils.loadAnimation(context,
                R.anim.movetolefttoright);


        animMove3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isPlayer3 = true;

//                int fina_total = Integer.parseInt(walletplayer3) -  Integer.parseInt(table_amount);
//                txtPlay3wallet.setText(fina_total+"");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lnrcardsplayerplayermain3.setVisibility(View.VISIBLE);
                txtTotalCoin.setVisibility(View.VISIBLE);
                txt_coin_to_girl_player3.setVisibility(View.GONE);
                card3_count = 0;
//                imgplayer3mainfirst.setVisibility(View.VISIBLE);
//                imgplayer3mainfirst.startAnimation(animMoveCardsPlayer3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove4 = AnimationUtils.loadAnimation(context,
                R.anim.movetorighttoleft);

        animMove4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                isPlayer4 = true;


//                int fina_total = Integer.parseInt(walletplayer4) -  Integer.parseInt(table_amount);
//                txtPlay4wallet.setText(fina_total+"");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                txtTotalCoin.setVisibility(View.VISIBLE);
                txt_coin_to_girl_player4.setVisibility(View.GONE);
                lnrcardsplayerplayermain4.setVisibility(View.VISIBLE);
                card4_count = 0;
//                imgplayer4mainfirst.setVisibility(View.VISIBLE);
//                imgplayer4mainfirst.startAnimation(animMoveCardsPlayer4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove5 = AnimationUtils.loadAnimation(context,
                R.anim.movetodowntoup_corner_right_left);

        // txt_coin_to_girl_player5.setAnimation(animMove5);

        animMove5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                isPlayer5 = true;


//                int fina_total = Integer.parseInt(walletplayer5) -  Integer.parseInt(table_amount);
//                txtPlay5wallet.setText(fina_total+"");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lnrcardsplayerplayermain5.setVisibility(View.VISIBLE);
                txtTotalCoin.setVisibility(View.VISIBLE);
                txt_coin_to_girl_player5.setVisibility(View.GONE);
                card5_count = 0;
//                imgplayer5mainfirst.setVisibility(View.VISIBLE);
//                imgplayer5mainfirst.startAnimation(animMoveCardsPlayer5);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove1_2 = AnimationUtils.loadAnimation(context,
                R.anim.movetodowntoup);

        animMove1_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_coin_to_girl_player1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove2_2 = AnimationUtils.loadAnimation(context,
                R.anim.movetodowntoup_corner);

        animMove2_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_coin_to_girl_player2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove3_2 = AnimationUtils.loadAnimation(context,
                R.anim.movetolefttoright);

        animMove3_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_coin_to_girl_player3.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove4_2 = AnimationUtils.loadAnimation(context,
                R.anim.movetorighttoleft);

        animMove4_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_coin_to_girl_player4.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMove5_2 = AnimationUtils.loadAnimation(context,
                R.anim.movetodowntoup_corner_right_left);

        animMove5_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txt_coin_to_girl_player5.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMoveCardsPlayer1 = AnimationUtils.loadAnimation(context,
                R.anim.movetoanother);
        animMoveCardsPlayer1.setDuration(animation_speed);
        animMoveCardsPlayer1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                PlaySaund(R.raw.teenpatticardflip_android);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                card1_count++;
                total_cards--;

                rltSeeButtoncardspl1.setVisibility(View.VISIBLE);
                lnrSeeButtoncardspl1.setVisibility(View.VISIBLE);

                if(card1_count == 1)
                {
                    imgpl1hidencard1.setVisibility(View.VISIBLE);
                    imgpl1hidencard2.setVisibility(View.GONE);
                    imgpl1hidencard3.setVisibility(View.GONE);
                }
                else if(card1_count == 2)
                {
                    imgpl1hidencard2.setVisibility(View.VISIBLE);
                }
                else if(card1_count == 3)
                {
                    imgpl1hidencard3.setVisibility(View.VISIBLE);
                }


                if(card1_count >= 3)
                {
                    imgpl1hidencard1.setVisibility(View.VISIBLE);
                    imgpl1hidencard2.setVisibility(View.VISIBLE);
                    imgpl1hidencard3.setVisibility(View.VISIBLE);
                    lnrSeeButtoncardspl1.setVisibility(View.VISIBLE);
                    rltSeeButtoncardspl1.setVisibility(View.VISIBLE);
                    // rltSee1.setVisibility(View.VISIBLE);
                    // imgsee1.setVisibility(View.VISIBLE);
                    imgseen1.setVisibility(View.VISIBLE);
                    imgblind1.setVisibility(View.VISIBLE);
                    imgblind1.setText(""+BLIND);
                    lnrcardsmainplayer1.setVisibility(View.GONE);
                }
                else {

                }

                if(isPlayer2)
                    AnimateCardstoPlayer(PLAYER2);
                else if(isPlayer3)
                    AnimateCardstoPlayer(PLAYER3);
                else if(isPlayer4)
                    AnimateCardstoPlayer(PLAYER4);
                else if(isPlayer5)
                    AnimateCardstoPlayer(PLAYER5);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMoveCardsPlayer2 = AnimationUtils.loadAnimation(context,
                R.anim.movetoanotherleftcorner);
        animMoveCardsPlayer2.setDuration(animation_speed);
        animMoveCardsPlayer2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                PlaySaund(R.raw.teenpatticardflip_android);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                card2_count++;
                total_cards--;




                if(card2_count >= 3 || total_cards <= 0)
                {
                    rltSee2.setVisibility(View.VISIBLE);
                    lnrcardsplayerplayermain2.setVisibility(View.GONE);
                }
                else {

                }

                if(table_users > 2)
                {
                    if(isPlayer3)
                        AnimateCardstoPlayer(PLAYER3);
                    else if(isPlayer4)
                        AnimateCardstoPlayer(PLAYER4);
                    else if(isPlayer5)
                        AnimateCardstoPlayer(PLAYER5);
                }
                else {
                    AnimateCardstoPlayer(PLAYER1);
                }



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animMoveCardsPlayer3 = AnimationUtils.loadAnimation(context,
                R.anim.movetorighttoleftcards);
        animMoveCardsPlayer3.setDuration(animation_speed);
        animMoveCardsPlayer3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                PlaySaund(R.raw.teenpatticardflip_android);

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                card3_count++;
                total_cards--;



                if(card3_count >= 3 || total_cards <= 0)
                {
                    rltSee3.setVisibility(View.VISIBLE);
                    imgplayer3mainfirst.setVisibility(View.GONE);
                    lnrcardsplayerplayermain3.setVisibility(View.GONE);
                }
                else {

                }

                if(table_users > 2)
                {
                    if(isPlayer4)
                        AnimateCardstoPlayer(PLAYER4);
                    else if(isPlayer5)
                        AnimateCardstoPlayer(PLAYER5);
                    else if(isPlayer1)
                        AnimateCardstoPlayer(PLAYER1);
                    else if (isPlayer2)
                        AnimateCardstoPlayer(PLAYER2);

                }
                else {
                    AnimateCardstoPlayer(PLAYER1);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animMoveCardsPlayer4 = AnimationUtils.loadAnimation(context,
                R.anim.movetolefttorightcard);
        animMoveCardsPlayer4.setDuration(animation_speed);
        animMoveCardsPlayer4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                PlaySaund(R.raw.teenpatticardflip_android);

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                card4_count++;
                total_cards--;



                if(card4_count >= 3 || total_cards <= 0)
                {
                    rltSee4.setVisibility(View.VISIBLE);
                    lnrcardsplayerplayermain4.setVisibility(View.GONE);
                    imgplayer4mainfirst.setVisibility(View.GONE);
                }
                else {



                }

                if(table_users > 2)
                {
                    if(isPlayer5)
                        AnimateCardstoPlayer(PLAYER5);
                    else if(isPlayer1)
                        AnimateCardstoPlayer(PLAYER1);
                    else if (isPlayer2)
                        AnimateCardstoPlayer(PLAYER2);
                    else if (isPlayer3)
                        AnimateCardstoPlayer(PLAYER3);
                    else if (isPlayer4)
                        AnimateCardstoPlayer(PLAYER4);


                }
                else {
                    AnimateCardstoPlayer(PLAYER1);
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animMoveCardsPlayer5 = AnimationUtils.loadAnimation(context,
                R.anim.movetotoptodown_corner_right_left);
        animMoveCardsPlayer5.setDuration(animation_speed);
        animMoveCardsPlayer5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                PlaySaund(R.raw.teenpatticardflip_android);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                card5_count++;
                total_cards--;


                if(card5_count >= 3 || total_cards <= 0)
                {
                    rltSee5.setVisibility(View.VISIBLE);
                    lnrcardsplayerplayermain5.setVisibility(View.GONE);
                    imgplayer5mainfirst.setVisibility(View.GONE);
                }
                else {


                }

                if(total_cards > 0)
                {
                    AnimateCardstoPlayer(PLAYER1);
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animAmpireblink = AnimationUtils.loadAnimation(context,
                R.anim.blink);


        animMoveCardsPlayerwinner1 = AnimationUtils.loadAnimation(context,
                R.anim.movetoanotherwinner);
        animMoveCardsPlayerwinner2 = AnimationUtils.loadAnimation(context,
                R.anim.movetoanotherleftcornerwinner);

        animMoveCardsreferycomi = AnimationUtils.loadAnimation(context,
                R.anim.movetoanotherreferycom);



        animMoveCardsPlayerwinner2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtTotalCoin.clearAnimation();
                txtTotalCoin.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animMoveCardsPlayerwinner3 = AnimationUtils.loadAnimation(context,
                R.anim.movetorighttoleftcardswinner);

        animMoveCardsPlayerwinner4 = AnimationUtils.loadAnimation(context,
                R.anim.movetolefttorightcardnewwinner);

        animMoveCardsPlayerwinner5 = AnimationUtils.loadAnimation(context,
                R.anim.movetolefttorightcardswinner);

    }

    String PLAYER1 = "Player1";
    String PLAYER2 = "Player2";
    String PLAYER3 = "Player3";
    String PLAYER4 = "Player4";
    String PLAYER5 = "Player5";
    private void AnimateCardstoPlayer(String players) {

        if(players.equals(PLAYER1))
        {
            imgplayermain1.setVisibility(View.VISIBLE);
            imgplayermain1.startAnimation(animMoveCardsPlayer1);
        }
        else
        if(players.equals(PLAYER2))
        {
            imgplayer2mainfirst.setVisibility(View.VISIBLE);
            imgplayer2mainfirst.startAnimation(animMoveCardsPlayer2);
        }
        else
        if(players.equals(PLAYER3))
        {
            imgplayer3mainfirst.setVisibility(View.VISIBLE);
            imgplayer3mainfirst.startAnimation(animMoveCardsPlayer3);
        }
        else
        if(players.equals(PLAYER4))
        {
            imgplayer4mainfirst.setVisibility(View.VISIBLE);
            imgplayer4mainfirst.startAnimation(animMoveCardsPlayer4);
        }
        else
        if(players.equals(PLAYER5))
        {
            imgplayer5mainfirst.setVisibility(View.VISIBLE);
            imgplayer5mainfirst.startAnimation(animMoveCardsPlayer5);
        }


    }

    private void UpdateWalletBallance(String user_id, String wallet) {
        if (user_id.equals(user_id_player1)) {

            // txtPlay1wallet.setText(""+Variables.CURRENCY_SYMBOL+wallet);

            float numberamount = Float.parseFloat(wallet);

            txtPlay1wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));


        } else if (user_id.equals(user_id_player2)) {

            float numberamount = Float.parseFloat(wallet);

            txtPlay2wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

            //  txtPlay2wallet.setText(""+Variables.CURRENCY_SYMBOL+wallet);

        } else if (user_id.equals(user_id_player3)) {

            //txtPlay3wallet.setText(""+Variables.CURRENCY_SYMBOL+wallet);
            float numberamount = Float.parseFloat(wallet);

            txtPlay3wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

        } else if (user_id.equals(user_id_player4)) {
            float numberamount = Float.parseFloat(wallet);

            txtPlay4wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

            // txtPlay4wallet.setText(""+Variables.CURRENCY_SYMBOL+wallet);

        } else if (user_id.equals(user_id_player5)) {

            float numberamount = Float.parseFloat(wallet);
            txtPlay5wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
            // txtPlay5wallet.setText(""+Variables.CURRENCY_SYMBOL+wallet);

        } else {


        }

    }

    public void makeHightLightForChaal(String chaal_user_id) {

        if (chaal_user_id.equals(user_id_player1)) {

            if(!isSlideShow)
            {
                ManageBottomActionButton(true);
            }

            //SlideUP(lnrGameButton,context);
            if (isProgressrun1) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer1.start();
                isProgressrun1 = false;
            }
            isProgressrun2 = true;
            isProgressrun3 = true;
            isProgressrun4 = true;
            isProgressrun5 = true;

            mCountDownTimer2.cancel();
            mCountDownTimer3.cancel();
            mCountDownTimer4.cancel();
            mCountDownTimer5.cancel();

            mProgress2.setProgress(0);
            mProgress3.setProgress(0);
            mProgress4.setProgress(0);
            mProgress5.setProgress(0);

            imgpl2glow.setVisibility(View.GONE);
            imgpl3glow.setVisibility(View.GONE);
            imgpl4glow.setVisibility(View.GONE);
            imgpl5glow.setVisibility(View.GONE);

            txtCounttimer2.setVisibility(View.GONE);
            txtCounttimer3.setVisibility(View.GONE);
            txtCounttimer4.setVisibility(View.GONE);
            txtCounttimer5.setVisibility(View.GONE);


        } else if (chaal_user_id.equals(user_id_player2)) {

            ManageBottomActionButton(false);
            //SlideDown(lnrGameButton,context);
            if (isProgressrun2) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer2.start();
                isProgressrun2 = false;
            }


            isProgressrun1 = true;
            isProgressrun3 = true;
            isProgressrun4 = true;
            isProgressrun5 = true;

            mProgress1.setProgress(0);
            mProgress3.setProgress(0);
            mProgress4.setProgress(0);
            mProgress5.setProgress(0);

            imgpl1glow.setVisibility(View.GONE);
            imgpl3glow.setVisibility(View.GONE);
            imgpl4glow.setVisibility(View.GONE);
            imgpl5glow.setVisibility(View.GONE);

            mCountDownTimer1.cancel();
            mCountDownTimer3.cancel();
            mCountDownTimer4.cancel();
            mCountDownTimer5.cancel();

            txtCounttimer1.setVisibility(View.GONE);
            txtCounttimer3.setVisibility(View.GONE);
            txtCounttimer4.setVisibility(View.GONE);
            txtCounttimer5.setVisibility(View.GONE);

        } else if (chaal_user_id.equals(user_id_player3)) {
            if (isProgressrun3) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer3.start();
                isProgressrun3 = false;
            }

            ManageBottomActionButton(false);
            // SlideDown(lnrGameButton,context);
            isProgressrun2 = true;
            isProgressrun1 = true;
            isProgressrun4 = true;
            isProgressrun5 = true;

            mProgress1.setProgress(0);
            mProgress2.setProgress(0);
            mProgress4.setProgress(0);
            mProgress5.setProgress(0);

            imgpl1glow.setVisibility(View.GONE);
            imgpl2glow.setVisibility(View.GONE);
            imgpl4glow.setVisibility(View.GONE);
            imgpl5glow.setVisibility(View.GONE);

            mCountDownTimer1.cancel();
            mCountDownTimer2.cancel();
            mCountDownTimer4.cancel();
            mCountDownTimer5.cancel();

            txtCounttimer1.setVisibility(View.GONE);
            txtCounttimer2.setVisibility(View.GONE);
            txtCounttimer4.setVisibility(View.GONE);
            txtCounttimer5.setVisibility(View.GONE);

        } else if (chaal_user_id.equals(user_id_player4)) {
            if (isProgressrun4) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer4.start();
                isProgressrun4 = false;
            }

            ManageBottomActionButton(false);

            //SlideDown(lnrGameButton,context);
            isProgressrun2 = true;
            isProgressrun3 = true;
            isProgressrun1 = true;
            isProgressrun5 = true;

            mProgress1.setProgress(0);
            mProgress2.setProgress(0);
            mProgress3.setProgress(0);
            mProgress5.setProgress(0);

            imgpl1glow.setVisibility(View.GONE);
            imgpl2glow.setVisibility(View.GONE);
            imgpl3glow.setVisibility(View.GONE);
            imgpl5glow.setVisibility(View.GONE);

            mCountDownTimer1.cancel();
            mCountDownTimer2.cancel();
            mCountDownTimer3.cancel();
            mCountDownTimer5.cancel();


            txtCounttimer1.setVisibility(View.GONE);
            txtCounttimer2.setVisibility(View.GONE);
            txtCounttimer3.setVisibility(View.GONE);
            txtCounttimer5.setVisibility(View.GONE);

        } else if (chaal_user_id.equals(user_id_player5)) {
            if (isProgressrun5) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer5.start();
                isProgressrun5 = false;
            }


            ManageBottomActionButton(false);

            //SlideDown(lnrGameButton,context);

            isProgressrun2 = true;
            isProgressrun3 = true;
            isProgressrun4 = true;
            isProgressrun1 = true;


            mProgress1.setProgress(0);
            mProgress2.setProgress(0);
            mProgress3.setProgress(0);
            mProgress4.setProgress(0);

            imgpl1glow.setVisibility(View.GONE);
            imgpl2glow.setVisibility(View.GONE);
            imgpl3glow.setVisibility(View.GONE);
            imgpl4glow.setVisibility(View.GONE);

            mCountDownTimer1.cancel();
            mCountDownTimer2.cancel();
            mCountDownTimer3.cancel();
            mCountDownTimer4.cancel();

            txtCounttimer1.setVisibility(View.GONE);
            txtCounttimer2.setVisibility(View.GONE);
            txtCounttimer3.setVisibility(View.GONE);
            txtCounttimer4.setVisibility(View.GONE);

        } else {


        }

    }

    public void makeWinnertoPlayer(String chaal_user_id) {

        PlaySaund(R.raw.tpb_battle_won);
        isProgressrun1 = true;
        isProgressrun2 = true;
        isProgressrun3 = true;
        isProgressrun4 = true;
        isProgressrun5 = true;

        mCountDownTimer1.cancel();
        mCountDownTimer2.cancel();
        mCountDownTimer3.cancel();
        mCountDownTimer4.cancel();
        mCountDownTimer5.cancel();

        txtCounttimer1.setVisibility(View.GONE);
        txtCounttimer2.setVisibility(View.GONE);
        txtCounttimer3.setVisibility(View.GONE);
        txtCounttimer4.setVisibility(View.GONE);
        txtCounttimer5.setVisibility(View.GONE);

        total_cards = 0;
        card1_count = 0;
        card2_count = 0;
        card3_count = 0;
        card4_count = 0;
        card5_count = 0;

        isPlayer1 = false;
        isPlayer2 = false;
        isPlayer3 = false;
        isPlayer4 = false;
        isPlayer5 = false;

        float valueforrefery = Float.parseFloat(game_amount);
        int admin_commision = SharePref.getInstance().getInt(SharePref.ADMIN_COMMISSION);
        float referycom = valueforrefery * admin_commision / 100;

        if (chaal_user_id.equals(user_id_player1)) {
            txtTotalCoin.setVisibility(View.VISIBLE);
            txtTotalCoin.startAnimation(animMoveCardsPlayerwinner1);
            txtrefery_com.setText(referycom + "");
            txtrefery_com.setVisibility(View.VISIBLE);
            txtrefery_com.startAnimation(animMoveCardsreferycomi);
            imgpl1winner.setVisibility(View.VISIBLE);
            imgpl1winnerstar.setVisibility(View.VISIBLE);
            imgpl1winnerpatti.setVisibility(View.VISIBLE);
            // imgpl1winner.setAnimation(animAmpireblink);

        }
        else if (chaal_user_id.equals(user_id_player2)) {

            txtTotalCoin.setVisibility(View.VISIBLE);
            txtTotalCoin.startAnimation(animMoveCardsPlayerwinner2);
            //imgpl2winner.setAnimation(animAmpireblink);
            imgpl2winner.setVisibility(View.VISIBLE);
            imgpl2winnerstar.setVisibility(View.VISIBLE);
            imgpl2winnerpatti.setVisibility(View.VISIBLE);
            txtrefery_com.setText(referycom + "");
            txtrefery_com.setVisibility(View.VISIBLE);
            txtrefery_com.startAnimation(animMoveCardsreferycomi);

        }
        else if (chaal_user_id.equals(user_id_player3)) {
            txtTotalCoin.setVisibility(View.VISIBLE);
            txtTotalCoin.startAnimation(animMoveCardsPlayerwinner3);
            imgpl3winner.setAnimation(animAmpireblink);
            imgpl3winner.setVisibility(View.VISIBLE);
            imgpl3winnerstar.setVisibility(View.VISIBLE);
            imgpl3winnerpatti.setVisibility(View.VISIBLE);
            txtrefery_com.setText(referycom + "");
            txtrefery_com.setVisibility(View.VISIBLE);
            txtrefery_com.startAnimation(animMoveCardsreferycomi);


        }
        else if (chaal_user_id.equals(user_id_player4)) {
            txtTotalCoin.setVisibility(View.VISIBLE);
            txtTotalCoin.startAnimation(animMoveCardsPlayerwinner4);
            imgpl4winner.setAnimation(animAmpireblink);
            imgpl4winner.setVisibility(View.VISIBLE);
            imgpl4winnerstar.setVisibility(View.VISIBLE);
            imgpl4winnerpatti.setVisibility(View.VISIBLE);
            txtrefery_com.setText(referycom + "");
            txtrefery_com.setVisibility(View.VISIBLE);
            txtrefery_com.startAnimation(animMoveCardsreferycomi);

        }
        else if (chaal_user_id.equals(user_id_player5)) {

            txtTotalCoin.setVisibility(View.VISIBLE);
            txtTotalCoin.startAnimation(animMoveCardsPlayerwinner5);
            imgpl5winner.setAnimation(animAmpireblink);
            imgpl5winner.setVisibility(View.VISIBLE);
            imgpl5winnerstar.setVisibility(View.VISIBLE);
            imgpl5winnerpatti.setVisibility(View.VISIBLE);
            txtrefery_com.setText(referycom + "");
            txtrefery_com.setVisibility(View.VISIBLE);
            txtrefery_com.startAnimation(animMoveCardsreferycomi);

        }


        txtGameFinish.setVisibility(View.VISIBLE);
        rltGameFinish.setVisibility(View.VISIBLE);
        count = 8;
        counttimerforstartgame.start();

    }

    public void showGifttoPayer(String to_user_id,String gift_id)
    {


        if (to_user_id.equals(user_id_player1)) {
            visibleGiftofUser(imggift1,gift_id);
        }
        else if (to_user_id.equals(user_id_player2)) {
            visibleGiftofUser(imggift2,gift_id);
        }
        else if (to_user_id.equals(user_id_player3)) {
            visibleGiftofUser(imggift3,gift_id);
        }
        else if (to_user_id.equals(user_id_player4)) {
            visibleGiftofUser(imggift4,gift_id);
        }
        else if (to_user_id.equals(user_id_player5)) {
            visibleGiftofUser(imggift5,gift_id);
        }


    }

    private void visibleGiftofUser(ImageView imguser,String gifturl){

        imguser.setVisibility(View.VISIBLE);
        Glide.with(context)
//
                .load(Const.IMGAE_PATH + gifturl)
//                .skipMemoryCache(true)
                .into(imguser);

    }

    private void BacktoVisibleGift(ImageView imguser){

        imguser.setImageDrawable(getDrawable(R.drawable.gift));

    }

    public void makeLastChaaldisplay(String action, String chaal_user_id, String amount) {

        if (action.equals("0")) {

        } else if (action.equals("1")) {

            if (chaal_user_id.equals(user_id_player1)) {
                imgpack1.setVisibility(View.VISIBLE);
                imgpack1.startAnimation(animAmpireblink);
                rltSee1.setVisibility(View.GONE);
                lnrSeeButtoncardspl1.setVisibility(View.GONE);
                rltSeeButtoncardspl1.setVisibility(View.GONE);
                user_pack1 = "1";

            } else if (chaal_user_id.equals(user_id_player2)) {
                imgpack2.setVisibility(View.VISIBLE);
                imgpack2.startAnimation(animAmpireblink);
                rltSee2.setVisibility(View.GONE);
                user_pack2 = "1";

            } else if (chaal_user_id.equals(user_id_player3)) {
                imgpack3.setVisibility(View.VISIBLE);
                imgpack3.startAnimation(animAmpireblink);
                rltSee3.setVisibility(View.GONE);
                user_pack3 = "1";

            } else if (chaal_user_id.equals(user_id_player4)) {
                imgpack4.setVisibility(View.VISIBLE);
                imgpack4.startAnimation(animAmpireblink);
                rltSee4.setVisibility(View.GONE);
                user_pack4 = "1";

            } else if (chaal_user_id.equals(user_id_player5)) {
                imgpack5.setVisibility(View.VISIBLE);
                imgpack5.startAnimation(animAmpireblink);
                rltSee5.setVisibility(View.GONE);
                user_pack5 = "1";
            } else {


            }


        } else if (action.equals("2")) {

            if (chaal_user_played_id.equals(chaal_user_id)) {


            } else {

                chaal_user_played_id = chaal_user_id;

                makeshowchaalamountfloat(chaal_user_played_id, amount);


            }

        } else {

            System.out.println("" + action);


            if (chaal_user_id.equals(user_id_player1)) {
                imgshow1.setVisibility(View.VISIBLE);
                imgshow1.startAnimation(animAmpireblink);

            } else if (chaal_user_id.equals(user_id_player2)) {
                imgshow2.setVisibility(View.VISIBLE);
                imgshow2.startAnimation(animAmpireblink);

            } else if (chaal_user_id.equals(user_id_player3)) {
                imgshow3.setVisibility(View.VISIBLE);
                imgshow3.startAnimation(animAmpireblink);

            } else if (chaal_user_id.equals(user_id_player4)) {
                imgshow4.setVisibility(View.VISIBLE);
                imgshow4.startAnimation(animAmpireblink);

            } else if (chaal_user_id.equals(user_id_player5)) {
                imgshow5.setVisibility(View.VISIBLE);
                imgshow5.startAnimation(animAmpireblink);

            } else {


            }

        }


    }

    public void showSlideshowCardsafterPack(JSONArray slide_show_from_cards,JSONArray slide_show_to_cards){
        try {
            JSONObject slide_show_from = slide_show_from_cards.getJSONObject(0);
            JSONObject slide_show_to = slide_show_to_cards.getJSONObject(0);


            String from_user_id = slide_show_from.getString("user_id");
            String to_user_id = slide_show_to.getString("user_id");

            if(!user_id_player1.equalsIgnoreCase(from_user_id) && !user_id_player1.equalsIgnoreCase(to_user_id))
                return;


            if(from_user_id.equalsIgnoreCase(user_id_player1))
            {
                rltSee1.setVisibility(View.VISIBLE);
                lnrSeeButtoncardspl1.setVisibility(View.VISIBLE);
                rltSeeButtoncardspl1.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl1hidencard1,imgpl1hidencard2,imgpl1hidencard3,slide_show_from);
            }
            else if(from_user_id.equalsIgnoreCase(user_id_player2))
            {
                rltSee2.setVisibility(View.GONE);
                lnrShowButtoncardspl2.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl2showcard1,imgpl2showcard2,imgpl2showcard3,slide_show_from);
            }
            else if(from_user_id.equalsIgnoreCase(user_id_player3))
            {
                rltSee3.setVisibility(View.GONE);
                lnrShowButtoncardspl3.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl3showcard1,imgpl3showcard2,imgpl3showcard3,slide_show_from);
            }
            else if(from_user_id.equalsIgnoreCase(user_id_player4))
            {
                rltSee4.setVisibility(View.GONE);
                lnrShowButtoncardspl4.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl4showcard1,imgpl4showcard2,imgpl4showcard3,slide_show_from);
            }
            else if(from_user_id.equalsIgnoreCase(user_id_player5))
            {
                rltSee5.setVisibility(View.GONE);
                lnrShowButtoncardspl5.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl5showcard1,imgpl5showcard2,imgpl5showcard3,slide_show_from);
            }


            if(to_user_id.equalsIgnoreCase(user_id_player1))
            {
                visibleCardsofUsers(imgpl1hidencard1,imgpl1hidencard2,imgpl1hidencard3,slide_show_to);
            }
            else if(to_user_id.equalsIgnoreCase(user_id_player2))
            {
                rltSee2.setVisibility(View.GONE);
                lnrShowButtoncardspl2.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl2showcard1,imgpl2showcard2,imgpl2showcard3,slide_show_to);
            }
            else if(to_user_id.equalsIgnoreCase(user_id_player3))
            {
                rltSee3.setVisibility(View.GONE);
                lnrShowButtoncardspl3.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl3showcard1,imgpl3showcard2,imgpl3showcard3,slide_show_to);
            }
            else if(to_user_id.equalsIgnoreCase(user_id_player4))
            {
                rltSee4.setVisibility(View.GONE);
                lnrShowButtoncardspl4.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl4showcard1,imgpl4showcard2,imgpl4showcard3,slide_show_to);
            }
            else if(to_user_id.equalsIgnoreCase(user_id_player5))
            {
                rltSee5.setVisibility(View.GONE);
                lnrShowButtoncardspl5.setVisibility(View.VISIBLE);

                visibleCardsofUsers(imgpl5showcard1,imgpl5showcard2,imgpl5showcard3,slide_show_to);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void visibleCardsofUsers(ImageView ivCards1,ImageView ivCards2,ImageView ivCards3,JSONObject cardsobject) throws JSONException {

        String card1 = cardsobject.getString("card1");
        String card2 = cardsobject.getString("card2");
        String card3 = cardsobject.getString("card3");

        int imageResource1 = getResourcePath(card1.toLowerCase());

        int imageResource2 = getResourcePath(card2.toLowerCase());

        int imageResource3 = getResourcePath(card3.toLowerCase());

        Picasso.get().load(imageResource1).into(ivCards1);
        Picasso.get().load(imageResource2).into(ivCards2);
        Picasso.get().load(imageResource3).into(ivCards3);

    }


    private int getResourcePath(String drawablename){

        return Functions.GetResourcePath(drawablename,context);
    }

    public void makecardsdisplay(String user_id_temp) {

        if (user_id_temp.equals(user_id_player1)) {

            // rltSee1.setVisibility(View.VISIBLE);

        } else if (user_id_temp.equals(user_id_player2)) {

            if(!isViewVisible(lnrShowButtoncardspl2))
                rltSee2.setVisibility(View.VISIBLE);

        } else if (user_id_temp.equals(user_id_player3)) {

            if(!isViewVisible(lnrShowButtoncardspl3))
                rltSee3.setVisibility(View.VISIBLE);

        } else if (user_id_temp.equals(user_id_player4)) {

            if(!isViewVisible(lnrShowButtoncardspl4))
                rltSee4.setVisibility(View.VISIBLE);

        } else if (user_id_temp.equals(user_id_player5)) {

            if(!isViewVisible(lnrShowButtoncardspl5))
                rltSee5.setVisibility(View.VISIBLE);

        } else {


        }

    }

    private boolean isViewVisible(View view){
        boolean visible = false;

        if (view.getVisibility() == View.VISIBLE)
            visible = true;

        return visible;
    }


    public void makeslidshow(String user_id_temp, String name) {

        if (user_id_temp.equals(user_id_player1)) {

            ManageSlideShowAction(true);
            txtSlidshow.setText(name + " is Asking for Side Show");
//            txtSlidshow.setText("Someone has Asking for Side Show");

        } else {

            ManageSlideShowAction(false);

        }
    }

    public void showSlideshowonUsers(String from_user,String to_user){
        txtCounttimer1.setVisibility(View.GONE);
        txtCounttimer2.setVisibility(View.GONE);
        txtCounttimer3.setVisibility(View.GONE);
        txtCounttimer4.setVisibility(View.GONE);
        txtCounttimer5.setVisibility(View.GONE);

        txtCounttimer1.clearAnimation();
        txtCounttimer2.clearAnimation();
        txtCounttimer3.clearAnimation();
        txtCounttimer4.clearAnimation();
        txtCounttimer5.clearAnimation();

        String side_show = "Side Show";
        String show = "Show";

        if(from_user != null)
        {

            if(from_user.equalsIgnoreCase(user_id_player1))
            {
                isSlideShow = true;
                txtCounttimer1.setVisibility(View.VISIBLE);
                txtCounttimer1.setText(side_show);

            }
            else if(from_user.equalsIgnoreCase(user_id_player2))
            {
                txtCounttimer2.setVisibility(View.VISIBLE);
                txtCounttimer2.setText(side_show);

            }
            else if(from_user.equalsIgnoreCase(user_id_player3))
            {
                txtCounttimer3.setVisibility(View.VISIBLE);
                txtCounttimer3.setText(side_show);

            }
            else if(from_user.equalsIgnoreCase(user_id_player4))
            {
                txtCounttimer4.setVisibility(View.VISIBLE);
                txtCounttimer4.setText(side_show);

            }
            else if(from_user.equalsIgnoreCase(user_id_player5))
            {
                txtCounttimer5.setVisibility(View.VISIBLE);
                txtCounttimer5.setText(side_show);
            }


        }
        else {
            txtCounttimer1.setVisibility(View.GONE);
            txtCounttimer2.setVisibility(View.GONE);
            txtCounttimer3.setVisibility(View.GONE);
            txtCounttimer4.setVisibility(View.GONE);
            txtCounttimer5.setVisibility(View.GONE);
        }

        if(to_user != null)
        {
            if(to_user.equalsIgnoreCase(user_id_player1))
            {
                isSlideShow = true;
                txtCounttimer1.setVisibility(View.VISIBLE);
                txtCounttimer1.setText(show);

            }
            else if(to_user.equalsIgnoreCase(user_id_player2))
            {
                txtCounttimer2.setVisibility(View.VISIBLE);
                txtCounttimer2.setText(show);

            }
            else if(to_user.equalsIgnoreCase(user_id_player3))
            {
                txtCounttimer3.setVisibility(View.VISIBLE);
                txtCounttimer3.setText(show);

            }
            else if(to_user.equalsIgnoreCase(user_id_player4))
            {
                txtCounttimer4.setVisibility(View.VISIBLE);
                txtCounttimer4.setText(show);

            }
            else if(to_user.equalsIgnoreCase(user_id_player5))
            {
                txtCounttimer5.setVisibility(View.VISIBLE);
                txtCounttimer5.setText(show);
            }

        }
        else {
            txtCounttimer1.setVisibility(View.GONE);
            txtCounttimer2.setVisibility(View.GONE);
            txtCounttimer3.setVisibility(View.GONE);
            txtCounttimer4.setVisibility(View.GONE);
            txtCounttimer5.setVisibility(View.GONE);
        }

    }


    public void makeshowchaalamountfloat(String user_id_temp, String amount) {

        if (user_id_temp.equals(user_id_player1)) {
            //txt_coin_to_girl_player1.setText(""+Variables.CURRENCY_SYMBOL+table_amount);
            //  txt_coin_to_girl_player1.setVisibility(View.VISIBLE);
            //   txt_coin_to_girl_player1.startAnimation(animMove1_2);

        } else if (user_id_temp.equals(user_id_player2)) {
            txt_coin_to_girl_player2.setText(""+ Variables.CURRENCY_SYMBOL + amount);
            txt_coin_to_girl_player2.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player2.startAnimation(animMove2_2);
        } else if (user_id_temp.equals(user_id_player3)) {
            txt_coin_to_girl_player3.setText(""+Variables.CURRENCY_SYMBOL + amount);
            txt_coin_to_girl_player3.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player3.startAnimation(animMove3_2);

        } else if (user_id_temp.equals(user_id_player4)) {
            txt_coin_to_girl_player4.setText(""+Variables.CURRENCY_SYMBOL + amount);
            txt_coin_to_girl_player4.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player4.startAnimation(animMove4_2);

        } else if (user_id_temp.equals(user_id_player5)) {
            txt_coin_to_girl_player5.setText(""+Variables.CURRENCY_SYMBOL + amount);
            txt_coin_to_girl_player5.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player5.startAnimation(animMove5_2);

        } else {


        }

//        if (chaak_user_id.equals(user_id_player2)){
//            isPlayerPlayedChaal = false;
//        }

    }

    public void makegone() {

        imgpack1.setVisibility(View.GONE);
        imgpack2.setVisibility(View.GONE);
        imgpack3.setVisibility(View.GONE);
        imgpack4.setVisibility(View.GONE);
        imgpack5.setVisibility(View.GONE);

        imgshow1.setVisibility(View.GONE);
        imgshow2.setVisibility(View.GONE);
        imgshow3.setVisibility(View.GONE);
        imgshow4.setVisibility(View.GONE);
        imgshow5.setVisibility(View.GONE);


        imgpack1.clearAnimation();
        imgpack2.clearAnimation();
        imgpack3.clearAnimation();
        imgpack4.clearAnimation();
        imgpack5.clearAnimation();

        imgshow1.clearAnimation();
        imgshow2.clearAnimation();
        imgshow3.clearAnimation();
        imgshow4.clearAnimation();
        imgshow5.clearAnimation();


        rltplayer1growing.clearAnimation();
        rltplayer2growing.clearAnimation();
        rltplayer3growing.clearAnimation();
        rltplayer4growing.clearAnimation();
        rltplayer5growing.clearAnimation();


        imgpl1winner.setVisibility(View.GONE);
        imgpl2winner.setVisibility(View.GONE);
        imgpl3winner.setVisibility(View.GONE);
        imgpl4winner.setVisibility(View.GONE);
        imgpl5winner.setVisibility(View.GONE);

        imgpl1winnerstar.setVisibility(View.GONE);
        imgpl2winnerstar.setVisibility(View.GONE);
        imgpl3winnerstar.setVisibility(View.GONE);
        imgpl4winnerstar.setVisibility(View.GONE);
        imgpl5winnerstar.setVisibility(View.GONE);

        imgpl1winnerpatti.setVisibility(View.GONE);
        imgpl2winnerpatti.setVisibility(View.GONE);
        imgpl3winnerpatti.setVisibility(View.GONE);
        imgpl4winnerpatti.setVisibility(View.GONE);
        imgpl5winnerpatti.setVisibility(View.GONE);


        //imgpl1winner.clearAnimation();
        imgpl2winner.clearAnimation();
        imgpl3winner.clearAnimation();
        imgpl4winner.clearAnimation();
        imgpl5winner.clearAnimation();


        lnrSeeButtoncardspl1.setVisibility(View.GONE);
        rltSeeButtoncardspl1.setVisibility(View.GONE);


        lnrShowButtoncardspl2.setVisibility(View.GONE);
        lnrShowButtoncardspl3.setVisibility(View.GONE);
        lnrShowButtoncardspl4.setVisibility(View.GONE);
        lnrShowButtoncardspl5.setVisibility(View.GONE);

        int imageResource1 = R.drawable.backside_card;



        Picasso.get().load(imageResource1).into(imgpl1hidencard1);
        Picasso.get().load(imageResource1).into(imgpl1hidencard2);
        Picasso.get().load(imageResource1).into(imgpl1hidencard3);


    }

    public void startGrowingAnimation(RelativeLayout relativelayout) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(relativelayout, "alpha", .5f, .1f);
        fadeOut.setDuration(300);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(relativelayout, "alpha", .1f, .5f);
        fadeIn.setDuration(300);

        mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });

        mAnimationSet.start();
    }

    public void showDialoagonBack() {
        // custom dialog
        final Dialog dialog = Functions.DialogInstance(context);

        dialog.setContentView(R.layout.custom_dialog_close);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout lnr_switch = (LinearLayout) dialog.findViewById(R.id.lnr_switch);
        lnr_switch.setVisibility(View.VISIBLE);

        ImageView btnclose = (ImageView) dialog.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ImageView btnexitgame = (ImageView) dialog.findViewById(R.id.btnexitgame);
        ImageView btnexitloby = (ImageView) dialog.findViewById(R.id.btnexitloby);
        btnexitgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                GameLeave("1");

            }
        });

        btnexitloby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                GameLeave("0");

            }
        });

        ImageView btnswitchtabel = (ImageView) dialog.findViewById(R.id.btnswitchtabel);
        btnswitchtabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                table_id = "";
                GameTableChange();
            }
        });


        dialog.show();
        Functions.setDialogParams(dialog);
    }

    public void showDialogSetting() {
        // custom dialog
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.custom_dialog_setting);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclosetop);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        Switch switchd = (Switch) dialog.findViewById(R.id.switch1);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "0");

        if (value.equals("0")) {

            switchd.setChecked(true);

        } else {

            switchd.setChecked(false);
        }

        switchd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("issoundon", "0");
                    editor.apply();


                    // Functions.showToast(context, "On");

                } else {
                    // Functions.showToast(context, "Off");
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("issoundon", "1");
                    editor.apply();

                }
            }
        });

        dialog.show();
        Functions.setDialogParams(dialog);
    }

    public void showDialoagoninfo() {
        // custom dialog
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.custom_dialog_info);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclosetop);
        TextView txtboot = (TextView) dialog.findViewById(R.id.txtboot);
        txtboot.setTypeface(helvatikabold);
        TextView txtbootvalue = (TextView) dialog.findViewById(R.id.txtbootvalue);
        txtbootvalue.setTypeface(helvatikaboldround);
        txtbootvalue.setText(boot_value);
        TextView txtmaxblind = (TextView) dialog.findViewById(R.id.txtmaxblind);
        txtmaxblind.setTypeface(helvatikabold);
        TextView txtmaxblindvalue = (TextView) dialog.findViewById(R.id.txtmaxblindvalue);
        txtmaxblindvalue.setTypeface(helvatikaboldround);
        txtmaxblindvalue.setText(maximum_blind);
        TextView txtchallimt = (TextView) dialog.findViewById(R.id.txtchallimt);
        txtchallimt.setTypeface(helvatikabold);
        TextView txtchallimtvalue = (TextView) dialog.findViewById(R.id.txtchallimtvalue);
        txtchallimtvalue.setTypeface(helvatikaboldround);
        txtchallimtvalue.setText(chaal_limit);
        TextView txtpotlimit = (TextView) dialog.findViewById(R.id.txtpotlimit);
        txtpotlimit.setTypeface(helvatikabold);
        TextView txtpotlimitvalue = (TextView) dialog.findViewById(R.id.txtpotlimitvalue);
        txtpotlimitvalue.setTypeface(helvatikaboldround);
        txtpotlimitvalue.setText(pot_limit);
        TextView txtgudluck = (TextView) dialog.findViewById(R.id.txtgudluck);
        txtgudluck.setTypeface(helvatikaboldround);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
        Functions.setDialogParams(dialog);
    }

    private void getGame(final String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Functions.LOGE("PublicTable_New",""+url+"\n"+response);

                        // progressDialog.dismiss();
                        handleResponse(response);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Functions.LOGE("PublicTable_New",""+url+"\n error : "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("table_id", table_id);
                params.put("token", prefs.getString("token", ""));

                if(getIntent().hasExtra(SEL_TABLE))
                {
                    String boot_value = getIntent().getStringExtra(SEL_TABLE);
                    params.put("boot_value", boot_value);
                }

                Functions.LOGE("PublicTable_New",""+url+"\n"+params);

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

    private void getGame(final String url, final String boot_value) {

        HashMap params = new HashMap();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("table_id", table_id);
        params.put("boot_value",boot_value);
        params.put("token", prefs.getString("token", ""));


        ApiRequest.Call_Api(context, url, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    Functions.LOGE("CustomsiedTablev3",""+url+"\n"+resp);

                    // progressDialog.dismiss();
                    handleResponse(resp);

                }

            }
        });
    }


    private void handleResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            if (code.equalsIgnoreCase("200")) {

                if (jsonObject.has("table_data")) {
                    JSONObject jsonObject0 = jsonObject.getJSONArray("table_data").getJSONObject(0);
                    table_id = jsonObject0.getString("table_id");

                    txtWaitforOther.setAnimation(animAmpireblink);
                    txtWaitforOther.setVisibility(View.GONE);

                } else {

                    //if (jsonObject.has("message")) {
                    //  String message = jsonObject.getString("message");
//                        Functions.showToast(context, message,
//                                Toast.LENGTH_LONG).show();
                    //  }

                }

            } else if (code.equalsIgnoreCase("406")) {
                Functions.showToast(context, message);
                GameLeave("0");

            } else {
                // if (jsonObject.has("message")) {
                // String message = jsonObject.getString("message");
                Functions.showToast(context, message);
                // }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void GameLeave(final String value) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_TABLE_LEAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (value.equals("0")) {

                            finish();

                        } else {

                            finishAffinity();

                        }

                        finish();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                // .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
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

    boolean isMoreThen2palyer = false;
    JSONArray jsonArrayall_slide_show;
    boolean isSlideShow = false;

    private void GameStatus() {

        Functions.LOGE(TAG,"admin_commission : "+SharePref.getInstance().getInt(SharePref.ADMIN_COMMISSION));

        sentamounttype = "0";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_STATUS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Functions.LOGE("Public_Table",""+Const.GAME_STATUS+"\n"+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equals("407")) {
                                finish();

                            } else {

                            }

                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            JSONArray jsonArrayuser = jsonObject.getJSONArray("table_users");
                            makeleavetable();
                            int pp1 = 0;

                            for (int i = 0; i < jsonArrayuser.length(); i++) {
                                String use_temp = prefs.getString("user_id", "");

                                if (jsonArrayuser.getJSONObject(i).getString("user_id").equals(use_temp)) {
                                    pp1 = i;
                                }
                            }

                            for (int i = 0; i < pp1; i++) {

                                JSONObject temp = jsonArrayuser.getJSONObject(0);

                                for (int j = 0; j < jsonArrayuser.length() - 1; j++) {

                                    jsonArrayuser.put(j, jsonArrayuser.get(j + 1));//=jsonArrayuser

                                }
                                jsonArrayuser.put(jsonArrayuser.length() - 1,
                                        temp);
                            }


                            if (jsonArrayuser.length() > 1) {

                                txtGameFinish.setVisibility(View.VISIBLE);
                                rltGameFinish.setVisibility(View.VISIBLE);

                            } else {

                                txtGameFinish.setVisibility(View.GONE);
                                rltGameFinish.setVisibility(View.GONE);
                            }

                            user_id_player1 = "";
                            user_id_player2 = "";
                            user_id_player3 = "";
                            user_id_player4 = "";
                            user_id_player5 = "";
                            isMoreThen2palyer = false;
                            for (int k = 0; k < jsonArrayuser.length(); k++) {
                                if (k == 0) {
                                    isMoreThen2palyer = false;
                                    String name = jsonArrayuser.getJSONObject(0).getString("name");
                                    user_id_player1 = jsonArrayuser.getJSONObject(0).getString(
                                            "user_id");
                                    String profile_pic = jsonArrayuser.getJSONObject(0).getString("profile_pic");
                                    walletplayer1 = jsonArrayuser.getJSONObject(0).getString("wallet");
                                    txtPlay1.setText(name);
                                    float numberamount = Float.parseFloat(walletplayer1);
                                    txtPlay1wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
                                    //txtPlay1wallet.setText(""+Variables.CURRENCY_SYMBOL + walletplayer1);
                                    Picasso.get().load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);

                                    if (user_id_player1.equals(prefs.getString("user_id", ""))) {


                                    } else {

                                        Functions.showToast(context, "Your are timeout from " +
                                                "this table Join again.");


                                        finish();
                                    }
                                    imgchipuser1.setVisibility(View.VISIBLE);
//first player
                                } else if (k == 1) {

                                    play2id = jsonArrayuser.getJSONObject(1).getString("user_id");
                                    String table_id1 = jsonArrayuser.getJSONObject(1).getString(
                                            "table_id");
                                    final String name1 = jsonArrayuser.getJSONObject(1).getString(
                                            "name");
                                    user_id_player2 = jsonArrayuser.getJSONObject(1).getString(
                                            "user_id");
                                    String profile_pic1 =
                                            jsonArrayuser.getJSONObject(1).getString("profile_pic");
                                    walletplayer2 = jsonArrayuser.getJSONObject(1).getString(
                                            "wallet");

                                    if (user_id_player2.equals("0")) {

                                        txtPlay2.setText("");
                                        txtPlay2wallet.setVisibility(View.INVISIBLE);
                                        lnrPlay2wallet.setVisibility(View.INVISIBLE);

                                        int imageResource2 = R.drawable.avatar;

                                        Picasso.get().load(imageResource2).into(imgpl2circle);
                                        rltSee2.setVisibility(View.GONE);
                                        imgpack2.clearAnimation();
                                        imgpack2.setVisibility(View.GONE);
                                        mProgress2.setProgress(0);
                                        mCountDownTimer2.cancel();
                                        imgpl2glow.setVisibility(View.GONE);
                                     //   imginvite2.setVisibility(View.VISIBLE);
                                        imggift2.setVisibility(View.GONE);
                                        imgchipuser2.setVisibility(View.GONE);
                                        imgwaiting2.setVisibility(View.GONE);

                                    } else {
                                        isMoreThen2palyer = false;

                                        txtPlay2.setText(name1);
                                        txtPlay2wallet.setVisibility(View.VISIBLE);
                                        lnrPlay2wallet.setVisibility(View.VISIBLE);
                                        float numberamount = Float.parseFloat(walletplayer2);
                                        txtPlay2wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

                                        //txtPlay2wallet.setText("" + walletplayer2);
                                        Picasso.get().load(Const.IMGAE_PATH + profile_pic1).into(imgpl2circle);
                                    //    imginvite2.setVisibility(View.GONE);
                                        imggift2.setVisibility(View.VISIBLE);
                                        imgchipuser2.setVisibility(View.VISIBLE);
                                        imgwaiting2.setVisibility(View.VISIBLE);


                                    }

//second player
                                } else if (k == 2) {

                                    play3id = jsonArrayuser.getJSONObject(2).getString("user_id");
                                    String table_id3 = jsonArrayuser.getJSONObject(2).getString(
                                            "table_id");
                                    final String name3 = jsonArrayuser.getJSONObject(2).getString(
                                            "name");
                                    user_id_player3 = jsonArrayuser.getJSONObject(2).getString(
                                            "user_id");
                                    String profile_pic3 =
                                            jsonArrayuser.getJSONObject(2).getString("profile_pic");
                                    walletplayer3 = jsonArrayuser.getJSONObject(2).getString(
                                            "wallet");


                                    if (user_id_player3.equals("0")) {

                                        txtPlay3.setText("");
                                        txtPlay3wallet.setVisibility(View.INVISIBLE);
                                        lnrPlay3wallet.setVisibility(View.INVISIBLE);

                                        int imageResource3 = R.drawable.avatar;

                                        Picasso.get().load(imageResource3).into(imgpl3circle);
                                        rltSee3.setVisibility(View.GONE);
                                        imgpack3.clearAnimation();
                                        imgpack3.setVisibility(View.GONE);
                                        mProgress3.setProgress(0);
                                        mCountDownTimer3.cancel();
                                        imgpl3glow.setVisibility(View.GONE);
                                    //    imginvite3.setVisibility(View.VISIBLE);
                                        imggift3.setVisibility(View.GONE);
                                        imgchipuser3.setVisibility(View.GONE);
                                        imgwaiting3.setVisibility(View.GONE);

                                    } else {

                                        isMoreThen2palyer = true;

                                        txtPlay3wallet.setVisibility(View.VISIBLE);
                                        lnrPlay3wallet.setVisibility(View.VISIBLE);
                                        txtPlay3.setText(name3);
                                        float numberamount = Float.parseFloat(walletplayer3);
                                        txtPlay3wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

                                        //txtPlay3wallet.setText("" + walletplayer3);
                                        Picasso.get().load(Const.IMGAE_PATH + profile_pic3).into(imgpl3circle);
                                    //    imginvite3.setVisibility(View.GONE);
                                        imggift3.setVisibility(View.VISIBLE);
                                        imgchipuser3.setVisibility(View.VISIBLE);
                                        imgwaiting3.setVisibility(View.VISIBLE);
                                    }


//third player
                                } else if (k == 3) {

                                    play4id = jsonArrayuser.getJSONObject(3).getString("user_id");
                                    String table_id4 = jsonArrayuser.getJSONObject(3).getString(
                                            "table_id");
                                    final String name4 = jsonArrayuser.getJSONObject(3).getString(
                                            "name");
                                    user_id_player4 = jsonArrayuser.getJSONObject(3).getString(
                                            "user_id");
                                    String profile_pic4 =
                                            jsonArrayuser.getJSONObject(3).getString("profile_pic");
                                    walletplayer4 = jsonArrayuser.getJSONObject(3).getString(
                                            "wallet");

                                    if (user_id_player4.equals("0")) {

                                        txtPlay4.setText("");
                                        txtPlay4wallet.setVisibility(View.INVISIBLE);
                                        lnrPlay4wallet.setVisibility(View.INVISIBLE);

                                        int imageResource4 = R.drawable.avatar;

                                        Picasso.get().load(imageResource4).into(imgpl4circle);
                                        rltSee4.setVisibility(View.GONE);
                                        imgpack4.clearAnimation();
                                        imgpack4.setVisibility(View.GONE);
                                        mProgress4.setProgress(0);
                                        mCountDownTimer4.cancel();
                                        imgpl4glow.setVisibility(View.GONE);
                                     //   imginvite4.setVisibility(View.VISIBLE);
                                        imggift4.setVisibility(View.GONE);
                                        imgchipuser4.setVisibility(View.GONE);
                                        imgwaiting4.setVisibility(View.GONE);
                                    } else {

                                        isMoreThen2palyer = true;

                                        txtPlay4wallet.setVisibility(View.VISIBLE);
                                        lnrPlay4wallet.setVisibility(View.VISIBLE);
                                        txtPlay4.setText(name4);

                                        float numberamount = Float.parseFloat(walletplayer4);
                                        txtPlay4wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

                                        //  txtPlay4wallet.setText("" + walletplayer4);
                                        Picasso.get().load(Const.IMGAE_PATH + profile_pic4).into(imgpl4circle);
                                    //    imginvite4.setVisibility(View.GONE);
                                        imggift4.setVisibility(View.VISIBLE);
                                        imgchipuser4.setVisibility(View.VISIBLE);
                                        imgwaiting4.setVisibility(View.VISIBLE);
                                    }


                                } else {

                                    play5id = jsonArrayuser.getJSONObject(4).getString("user_id");
                                    String table_id5 = jsonArrayuser.getJSONObject(4).getString(
                                            "table_id");
                                    final String name5 = jsonArrayuser.getJSONObject(4).getString(
                                            "name");
                                    user_id_player5 = jsonArrayuser.getJSONObject(4).getString(
                                            "user_id");
                                    String profile_pic5 =
                                            jsonArrayuser.getJSONObject(4).getString("profile_pic");
                                    walletplayer5 = jsonArrayuser.getJSONObject(4).getString(
                                            "wallet");
                                    txtPlay5.setText(name5);

                                    if (walletplayer5.equals("0")) {

                                        txtPlay5.setText("");
                                        txtPlay5wallet.setVisibility(View.INVISIBLE);
                                        lnrPlay5wallet.setVisibility(View.INVISIBLE);

                                        int imageResource5 = R.drawable.avatar;

                                        Picasso.get().load(imageResource5).into(imgpl5circle);
                                        rltSee5.setVisibility(View.GONE);
                                        imgpack5.clearAnimation();
                                        imgpack5.setVisibility(View.GONE);
                                        mProgress5.setProgress(0);
                                        mCountDownTimer5.cancel();
                                        imgpl5glow.setVisibility(View.GONE);
                                    //    imginvite5.setVisibility(View.VISIBLE);
                                        imggift5.setVisibility(View.GONE);
                                        imgchipuser5.setVisibility(View.GONE);
                                        imgwaiting5.setVisibility(View.GONE);


                                    } else {
                                        isMoreThen2palyer = true;

                                        txtPlay5wallet.setVisibility(View.VISIBLE);
                                        lnrPlay5wallet.setVisibility(View.VISIBLE);
                                        txtPlay5.setText(name5);

                                        float numberamount = Float.parseFloat(walletplayer5);
                                        txtPlay5wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

                                        // txtPlay5wallet.setText("" + walletplayer5);
                                        Picasso.get().load(Const.IMGAE_PATH + profile_pic5).into(imgpl5circle);
                                    //    imginvite5.setVisibility(View.GONE);
                                        imggift5.setVisibility(View.VISIBLE);
                                        imgchipuser5.setVisibility(View.VISIBLE);
                                        imgwaiting5.setVisibility(View.VISIBLE);
                                    }


                                }

                            }

                            if(isMoreThen2palyer)
                            {
                                imgpl1show.setImageDrawable(getDrawable(R.drawable.shownew));
                            }
                            else {
                                imgpl1show.setImageDrawable(getDrawable(R.drawable.shownew));
                            }

                            txtWaitforOther.clearAnimation();
                            txtWaitforOther.setVisibility(View.GONE);

                            if (jsonObject.has("table_detail")) {

                                String table_detaisljson = jsonObject.getString("table_detail");

                                JSONObject table_detaisljsonobject =
                                        new JSONObject(table_detaisljson);

                                boot_value = table_detaisljsonobject.getString("boot_value");
                                maximum_blind = table_detaisljsonobject.getString("maximum_blind");
                                chaal_limit = table_detaisljsonobject.getString("chaal_limit");
                                pot_limit = table_detaisljsonobject.getString("pot_limit");

                            }

                            imggift1.setVisibility(View.GONE);
                            BacktoVisibleGift(imggift2);
                            BacktoVisibleGift(imggift3);
                            BacktoVisibleGift(imggift4);
                            BacktoVisibleGift(imggift5);
                            if(jsonObject.has("game_gifts"))
                            {

                                JSONArray giftsArray = jsonObject.getJSONArray("game_gifts");



                                for (int i = 0; i < giftsArray.length() ; i++) {
                                    JSONObject giftObject = giftsArray.getJSONObject(i);

                                    String gift_user_id = giftObject.optString("to_user_id","");
                                    String gift_id = giftObject.optString("image","");

                                    if(gift_id != null && !gift_id.equals(""))
                                    {
                                        showGifttoPayer(gift_user_id,gift_id);
                                    }

                                }

                            }


                            if (jsonObject.has("game_log")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("game_log");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String user_id = jsonObject1.getString("user_id");
                                action = jsonObject1.getString("action");
                                String amount = jsonObject1.getString("amount");
                                makeLastChaaldisplay(action, user_id, amount);
                            }


                            if (jsonObject.has("active_game_id")) {


                                game_id = jsonObject.getString("active_game_id");
                                if (game_id.equals("0")) {
                                    game_id = "";
                                }

                                String game_status = jsonObject.getString("game_status");

                                if (game_status.equals("0")) {

                                    makegone();

                                    ManageBottomActionButton(false);

                                    // SlideDown(lnrGameButton,context);
                                    timertime = 7000;
                                    imgwaiting2.setVisibility(View.GONE);
                                    imgwaiting3.setVisibility(View.GONE);
                                    imgwaiting4.setVisibility(View.GONE);
                                    imgwaiting5.setVisibility(View.GONE);

                                } else if (game_status.equals("1")) {


                                    isGameStartforseeebtn = true;

                                    table_amount = jsonObject.getString("table_amount");


                                    if (sentamounttype.equals("1")) {


                                    } else {


                                        String next = "<font color='#FFFFFF'>CHAAL   </font>";
                                        btnpl1number.setText(Html.fromHtml(next + "   " + table_amount));

                                    }

                                    txtGameFinish.setVisibility(View.GONE);
                                    rltGameFinish.setVisibility(View.GONE);

                                    JSONArray jsonArrayall_gameuserswating =
                                            jsonObject.getJSONArray(
                                                    "game_users");

                                    for (int i = 0; i < jsonArrayall_gameuserswating.length(); i++) {

                                        JSONObject jsonObjectall_usersgametemp =
                                                jsonArrayall_gameuserswating.getJSONObject(i);
                                        String user_idwaiting =
                                                jsonObjectall_usersgametemp.getString("user_id");
                                        makewaitingon(user_idwaiting);
                                    }

                                    if (action.equals("0")) {


                                        if (isGamenotStarted) {
                                            imgpl1plus.setEnabled(true);
                                            txtTotalCoin.clearAnimation();
                                            txtTotalCoin.setVisibility(View.VISIBLE);
                                            //isGamenotStarted = false;
                                            btnStartGame.setVisibility(View.GONE);
                                            makegone();
                                            PlaySaund(R.raw.teenpattichipstotable);
                                            // game_id = jsonObject.getString("game_id");

                                            JSONArray jsonArrayall_gameusers = jsonObject.getJSONArray(
                                                    "game_users");
                                            table_users = 0;
                                            for (int i = 0; i < jsonArrayall_gameusers.length(); i++) {

                                                JSONObject jsonObjectall_usersgametemp =
                                                        jsonArrayall_gameusers.getJSONObject(i);
                                                String user_id = jsonObjectall_usersgametemp.getString("user_id");
                                                distributecards(user_id);
                                            }


                                        }
                                    }

                                    //Game started
                                } else if (game_status.equals("2")) {
                                    // Game complted
                                    isGamenotStarted = true;
                                    timertime = 20000;
                                    isGameStartforseeebtn = false;
                                    //txtTotalCoin.clearAnimation();
                                    txtTotalCoin.setVisibility(View.GONE);

                                } else {


                                }

                            }

                            if (jsonObject.has("game_amount")) {
                                game_amount = jsonObject.getString("game_amount");
                                txtTotalCoin.setVisibility(View.VISIBLE);
                                txtTotalCoin.setText(""+Variables.CURRENCY_SYMBOL + game_amount);
                            } else {

                                txtTotalCoin.setText(Variables.CURRENCY_SYMBOL+"0");
                            }

                            String chaal = "";
                            String winner_user_id = "";

                            if (code.equals("200")) {
                                if (jsonObject.has("chaal")) {

                                    chaal = jsonObject.getString("chaal");
                                    chaal = jsonObject.getString("chaal");

                                    if (chaal.equals("0")) {

                                        if (jsonObject.has("winner_user_id")) {

                                            winner_user_id = jsonObject.getString("winner_user_id");
                                            makeWinnertoPlayer(winner_user_id);
                                            btnStartGame.setVisibility(View.GONE);


                                            ManageBottomActionButton(false);
                                            //SlideDown(lnrGameButton,context);
                                            isGamenotStarted = true;
                                            isGameStartforseeebtn = false;
                                            isSeenUser = false;
                                            imgpl1plus.setEnabled(true);

                                        }

                                    } else {

                                        chaak_user_id = chaal;
                                        makeHightLightForChaal(chaal);

                                    }

                                }
                            }

                            JSONArray jsonArrayall_users = jsonObject.optJSONArray("all_users");
                            if(jsonArrayall_users != null)
                            {
                                int lenghtjson = jsonArrayall_users.length();

                                if (lenghtjson > 0) {
                                    JSONObject jsonObjectall_users1 = jsonArrayall_users.getJSONObject(0);
                                    String user_id1 = jsonObjectall_users1.getString("user_id");
                                    String wallet1 = jsonObjectall_users1.getString("wallet");
                                    UpdateWalletBallance(user_id1, wallet1);

                                }

                                if (lenghtjson > 1) {

                                    JSONObject jsonObjectall_users2 = jsonArrayall_users.getJSONObject(1);
                                    String user_id2 = jsonObjectall_users2.getString("user_id");
                                    String wallet2 = jsonObjectall_users2.getString("wallet");
                                    UpdateWalletBallance(user_id2, wallet2);

                                }
                                if (lenghtjson > 2) {

                                    JSONObject jsonObjectall_users3 = jsonArrayall_users.getJSONObject(2);
                                    String user_id3 = jsonObjectall_users3.getString("user_id");
                                    String wallet3 = jsonObjectall_users3.getString("wallet");
                                    UpdateWalletBallance(user_id3, wallet3);
                                }

                                if (lenghtjson > 3) {

                                    JSONObject jsonObjectall_users4 = jsonArrayall_users.getJSONObject(3);
                                    String user_id4 = jsonObjectall_users4.getString("user_id");
                                    String wallet4 = jsonObjectall_users4.getString("wallet");
                                    UpdateWalletBallance(user_id4, wallet4);

                                }

                                if (user_id_player1.length() > 0) {


                                } else {

                                    rltSee1.setVisibility(View.GONE);

                                }

                                if (user_id_player2.length() > 0) {


                                } else {
                                    rltSee2.setVisibility(View.GONE);

                                }


                                if (user_id_player3.length() > 0) {


                                } else {
                                    rltSee3.setVisibility(View.GONE);

                                }


                                if (user_id_player4.length() > 0) {


                                } else {
                                    rltSee4.setVisibility(View.GONE);

                                }

                                if (user_id_player5.length() > 0) {


                                } else {
                                    rltSee5.setVisibility(View.GONE);

                                }


                                if (lenghtjson > 4) {
                                    JSONObject jsonObjectall_users5 = jsonArrayall_users.getJSONObject(4);
                                    String user_id5 = jsonObjectall_users5.getString("user_id");
                                    String wallet5 = jsonObjectall_users5.getString("wallet");
                                    UpdateWalletBallance(user_id5, wallet5);
                                }

                            }

                            /// slide_status = 0 request slide
                            /// slide_status = 1 accept slide
                            /// slide_status = 2 reject slide

                            JSONArray slideShowJSON = jsonObject.optJSONArray(
                                    "slide_show");
                            int slide_status = -1;

                            jsonArrayall_slide_show= null;
                            isSlideShow = false;

                            if (slideShowJSON != null && slideShowJSON.length() > 0) {

                                JSONObject json_obj_slid = slideShowJSON.getJSONObject(0);

                                // String packed = jsonObjectall_usersgametemp.getString("packed");
                                prev_id = json_obj_slid.getString("prev_id");
                                slidshow_id = json_obj_slid.getString("id");
                                slide_status = json_obj_slid.optInt("status");
                                String user_id_slid = json_obj_slid.getString("user_id");
                                String name = json_obj_slid.getString("name");



                                if(slide_status == 0)
                                {
                                    if(user_id_slid.equalsIgnoreCase(user_id_player1))
                                        isSlideShow = true;
                                    else
                                        isSlideShow = false;

                                    jsonArrayall_slide_show= slideShowJSON;
                                    makeslidshow(prev_id, name);
                                    showSlideshowonUsers(user_id_slid, prev_id);
                                }
                                else {
                                    jsonArrayall_slide_show= null;
                                    isSlideShow = false;
                                    showSlideshowonUsers(null, null);
                                }


                            } else {

                                ManageSlideShowAction(false);
                                showSlideshowonUsers(null, null);
                            }


                            JSONArray jsonArrayall_gameusers = jsonObject.getJSONArray(
                                    "game_users");

                            int lenghtjsongameuser = jsonArrayall_gameusers.length();


                            playercount = 0;
                            for (int i = 0; i < jsonArrayall_gameusers.length(); i++) {
                                JSONObject jsonObjectall_usersgametemp =
                                        jsonArrayall_gameusers.getJSONObject(i);
                                String packed = jsonObjectall_usersgametemp.getString("packed");
                                String seen = jsonObjectall_usersgametemp.getString("seen");
                                String user_id = jsonObjectall_usersgametemp.getString("user_id");

                                makeSeencard(user_id, seen);

                                if (packed.equals("0")) {
                                    playercount = playercount + 1;
                                }
                            }

                            if (playercount > 2) {

                                imgpl1show.setImageDrawable(getDrawable(R.drawable.shownew));
                                imgpl1show.setVisibility(View.VISIBLE);

                            } else {
                                imgpl1show.setImageDrawable(getDrawable(R.drawable.shownew));
                                imgpl1show.setVisibility(View.VISIBLE);
                            }

                            JSONArray slide_show_from_cards = jsonObject.optJSONArray("slide_show_from_cards");
                            JSONArray slide_show_to_cards = jsonObject.optJSONArray("slide_show_to_cards");
                            if (lenghtjsongameuser > 0) {

                                JSONObject jsonObjectall_usersgame1 =
                                        jsonArrayall_gameusers.getJSONObject(0);

                                String user_id_game1 = jsonObjectall_usersgame1.getString(
                                        "user_id");
                                String packed1 = jsonObjectall_usersgame1.getString("packed");
                                makecardsdisplay(user_id_game1);

                                if (packed1.equals("1")) {



                                    makeLastChaaldisplay("1", user_id_game1, "0");
                                    if(slide_status == 1)
                                    {
                                        showSlideshowCardsafterPack(slide_show_from_cards,slide_show_to_cards);
                                    }

                                } else {
                                    if (action.equals("3")) {

                                        String card1 = jsonObjectall_usersgame1.getString("card1");
                                        String card2 = jsonObjectall_usersgame1.getString("card2");
                                        String card3 = jsonObjectall_usersgame1.getString("card3");
                                        makeShowallcards(user_id_game1, card1, card2, card3);
                                    }

                                }

                            }

                            if (lenghtjsongameuser > 1) {

                                JSONObject jsonObjectall_usersgame2 =
                                        jsonArrayall_gameusers.getJSONObject(1);

                                String user_id_game2 = jsonObjectall_usersgame2.getString(
                                        "user_id");
                                String packed2 = jsonObjectall_usersgame2.getString("packed");
                                makecardsdisplay(user_id_game2);
                                if (packed2.equals("1")) {
                                    makeLastChaaldisplay("1", user_id_game2, "0");

                                    if(slide_status == 1)
                                    {
                                        showSlideshowCardsafterPack(slide_show_from_cards,slide_show_to_cards);
                                    }

                                } else {
                                    if (action.equals("3")) {

                                        String card1 = jsonObjectall_usersgame2.getString("card1");
                                        String card2 = jsonObjectall_usersgame2.getString("card2");
                                        String card3 = jsonObjectall_usersgame2.getString("card3");
                                        makeShowallcards(user_id_game2, card1, card2, card3);
                                    }

                                }

                            }

                            if (lenghtjsongameuser > 2) {

                                JSONObject jsonObjectall_usersgame3 =
                                        jsonArrayall_gameusers.getJSONObject(2);

                                String user_id_game3 = jsonObjectall_usersgame3.getString(
                                        "user_id");
                                String packed3 = jsonObjectall_usersgame3.getString("packed");
                                makecardsdisplay(user_id_game3);
                                if (packed3.equals("1")) {
                                    makeLastChaaldisplay("1", user_id_game3, "0");

                                    if(slide_status == 1)
                                    {
                                        showSlideshowCardsafterPack(slide_show_from_cards,slide_show_to_cards);
                                    }

                                } else {
                                    if (action.equals("3")) {

                                        String card1 = jsonObjectall_usersgame3.getString("card1");
                                        String card2 = jsonObjectall_usersgame3.getString("card2");
                                        String card3 = jsonObjectall_usersgame3.getString("card3");
                                        makeShowallcards(user_id_game3, card1, card2, card3);
                                    }

                                }

                            }

                            if (lenghtjsongameuser > 3) {

                                JSONObject jsonObjectall_usersgame4 =
                                        jsonArrayall_gameusers.getJSONObject(3);

                                String user_id_game4 = jsonObjectall_usersgame4.getString(
                                        "user_id");
                                String packed4 = jsonObjectall_usersgame4.getString("packed");
                                makecardsdisplay(user_id_game4);
                                if (packed4.equals("1")) {

                                    makeLastChaaldisplay("1", user_id_game4, "0");

                                    if(slide_status == 1)
                                    {
                                        showSlideshowCardsafterPack(slide_show_from_cards,slide_show_to_cards);
                                    }

                                } else {
                                    if (action.equals("3")) {

                                        String card1 = jsonObjectall_usersgame4.getString("card1");
                                        String card2 = jsonObjectall_usersgame4.getString("card2");
                                        String card3 = jsonObjectall_usersgame4.getString("card3");
                                        makeShowallcards(user_id_game4, card1, card2, card3);
                                    }

                                }

                            }

                            if (lenghtjsongameuser > 4) {

                                JSONObject jsonObjectall_usersgame5 =
                                        jsonArrayall_gameusers.getJSONObject(4);

                                String user_id_game5 = jsonObjectall_usersgame5.getString(
                                        "user_id");
                                String packed5 = jsonObjectall_usersgame5.getString("packed");
                                makecardsdisplay(user_id_game5);
                                if (packed5.equals("1")) {
                                    makeLastChaaldisplay("1", user_id_game5, "0");

                                    if(slide_status == 1)
                                    {
                                        showSlideshowCardsafterPack(slide_show_from_cards,slide_show_to_cards);
                                    }

                                } else {
                                    if (action.equals("3")) {

                                        String card1 = jsonObjectall_usersgame5.getString("card1");
                                        String card2 = jsonObjectall_usersgame5.getString("card2");
                                        String card3 = jsonObjectall_usersgame5.getString("card3");
                                        makeShowallcards(user_id_game5, card1, card2, card3);
                                    }

                                }

                            }


                            JSONArray jsonArraycardsblind = jsonObject.getJSONArray("cards");
                            int cardslenght = jsonArraycardsblind.length();

                            if (cardslenght > 0) {
                                rltSee1.setVisibility(View.GONE);
                                imgsee1.setVisibility(View.GONE);
                                imgseen1.setVisibility(View.GONE);
                                imgblind1.setVisibility(View.VISIBLE);
                                ChangeTexttoChaal(true);


                                JSONObject jsonObjectcardsblind =
                                        jsonArraycardsblind.getJSONObject(0);

                                String card1 = jsonObjectcardsblind.getString("card1");
                                String card2 = jsonObjectcardsblind.getString("card2");
                                String card3 = jsonObjectcardsblind.getString("card3");

                                int imageResource1 = getResourcePath(card1.toLowerCase());

                                int imageResource2 = getResourcePath(card2.toLowerCase());

                                int imageResource3 = getResourcePath(card3.toLowerCase());

                                Picasso.get().load(imageResource1).into(imgpl1hidencard1);
                                Picasso.get().load(imageResource2).into(imgpl1hidencard2);
                                Picasso.get().load(imageResource3).into(imgpl1hidencard3);

                            } else {


                            }

                            if (table_amount.length() > 0 && chaal_limit.length() > 0) {

                                float inttableamount = Float.parseFloat(table_amount);
                                float intchaal_limit = Float.parseFloat(chaal_limit);


                                if (isSeenUser) {

                                    System.out.println("scs");
                                    if (inttableamount >= intchaal_limit) {

                                        imgpl1plus.setEnabled(false);

                                    } else {
                                        //imgpl1plus.setEnabled(true);

                                    }

                                } else {

                                    System.out.println("scs");
                                    if (inttableamount >= intchaal_limit / 2) {

                                        imgpl1plus.setEnabled(false);

                                    } else {
                                        // imgpl1plus.setEnabled(true);

                                    }



                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                //  .show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("game_id", game_id);
                params.put("token", prefs.getString("token", ""));
                params.put("table_id", table_id);

                Functions.LOGE(TAG,""+"\n"+params);

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

    int table_users = 0;
    public void distributecards(String user_id) {

//        Functions.showToast(context, "Distribute Cards");

        isPlayer1 = false;
        isPlayer2 = false;
        isPlayer3 = false;
        isPlayer4 = false;
        isPlayer5 = false;
        total_cards = 0;

        card1_count = 0;
        card2_count = 0;
        card3_count = 0;
        card4_count = 0;
        card5_count = 0;

        if (user_id.equals(user_id_player1)) {

            table_users++;

            txt_coin_to_girl_player1.setText(table_amount);
            txt_coin_to_girl_player1.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player1.startAnimation(animMove1);
            pStatusprogress = 0;
            mProgress1.setProgress(0);
            imgpl1glow.setVisibility(View.GONE);

        }
        else if (user_id.equals(user_id_player2)) {

            table_users++;

            txt_coin_to_girl_player2.setText(table_amount);
            txt_coin_to_girl_player2.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player2.startAnimation(animMove2);
            pStatusprogress = 0;
            mProgress2.setProgress(0);
            imgpl2glow.setVisibility(View.GONE);


        } else if (user_id.equals(user_id_player3)) {

            table_users++;

            txt_coin_to_girl_player3.setText(table_amount);
            txt_coin_to_girl_player3.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player3.startAnimation(animMove3);
            pStatusprogress = 0;
            mProgress3.setProgress(0);
            imgpl3glow.setVisibility(View.GONE);


        } else if (user_id.equals(user_id_player4)) {

            table_users++;

            txt_coin_to_girl_player4.setText(table_amount);
            txt_coin_to_girl_player4.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player4.startAnimation(animMove4);
            pStatusprogress = 0;
            mProgress4.setProgress(0);
            imgpl4glow.setVisibility(View.GONE);


        } else if (user_id.equals(user_id_player5)) {

            table_users++;

            txt_coin_to_girl_player5.setText(table_amount);
            txt_coin_to_girl_player5.setVisibility(View.VISIBLE);
            txt_coin_to_girl_player5.startAnimation(animMove5);
            pStatusprogress = 0;
            mProgress5.setProgress(0);
            imgpl5glow.setVisibility(View.GONE);


        } else {


        }


        total_cards = table_users * 3;
//        Functions.showToast(context, ""+total_cards);
    }

    public void makewaitingon(String user_id) {

        if (user_id.equals(user_id_player1)) {


        } else if (user_id.equals(user_id_player2)) {

            imgwaiting2.setVisibility(View.GONE);

        } else if (user_id.equals(user_id_player3)) {
            imgwaiting3.setVisibility(View.GONE);

        } else if (user_id.equals(user_id_player4)) {
            imgwaiting4.setVisibility(View.GONE);

        } else if (user_id.equals(user_id_player5)) {
            imgwaiting5.setVisibility(View.GONE);
        } else {


        }

    }

    public void makeSeencard(String user_id, String seen) {

        if (user_id.equals(user_id_player1)) {
            user_pack1 = "0";

            if (seen.equals("0")) {

                isSeenUser = false;

            } else {

                isSeenUser = true;

            }


        } else if (user_id.equals(user_id_player2)) {
            user_pack2 = "0";
            int uri2 ;
            if (seen.equals("0")) {

                uri2 = R.drawable.blind;

            } else {

                uri2 = R.drawable.seen;

            }

            int imageResource2 = uri2;

            Picasso.get().load(imageResource2).into(imgsee2);


        } else if (user_id.equals(user_id_player3)) {
            user_pack3 = "0";
            int uri3 ;
            if (seen.equals("0")) {

                uri3 = R.drawable.blind;

            } else {

                uri3 = R.drawable.seen;

            }

            int imageResource3 = uri3;

            Picasso.get().load(imageResource3).into(imgsee3);

        } else if (user_id.equals(user_id_player4)) {
            user_pack4 = "0";
            int uri4;
            if (seen.equals("0")) {

                uri4 = R.drawable.blind;

            } else {

                uri4 = R.drawable.seen;

            }
            int imageResource4 = uri4;

            Picasso.get().load(imageResource4).into(imgsee4);

        } else if (user_id.equals(user_id_player5)) {

            user_pack5 = "0";

            int uri5;
            if (seen.equals("0")) {

                uri5 = R.drawable.blind;

            } else {

                uri5 = R.drawable.seen;

            }
            int imageResource5 = uri5;;

            Picasso.get().load(imageResource5).into(imgsee5);


        } else {


        }

    }

    public void makeShowallcards(String user_id, String cards1, String cards2, String cards3) {

        if (user_id.equals(user_id_player1)) {

            rltSee1.setVisibility(View.GONE);
            imgsee1.setVisibility(View.GONE);
            imgseen1.setVisibility(View.GONE);
            imgblind1.setVisibility(View.VISIBLE);
            ChangeTexttoChaal(true);


            int imageResource1 = getResourcePath(cards1.toLowerCase());

            int imageResource2 = getResourcePath(cards2.toLowerCase());

            int imageResource3 = getResourcePath(cards3.toLowerCase());

            Picasso.get().load(imageResource1).into(imgpl1hidencard1);
            Picasso.get().load(imageResource2).into(imgpl1hidencard2);
            Picasso.get().load(imageResource3).into(imgpl1hidencard3);


        }
        else if (user_id.equals(user_id_player2)) {
            rltSee2.setVisibility(View.GONE);
            lnrShowButtoncardspl2.setVisibility(View.VISIBLE);

            int imageResource1 = getResourcePath(cards1.toLowerCase());

            int imageResource2 = getResourcePath(cards2.toLowerCase());

            int imageResource3 = getResourcePath(cards3.toLowerCase());

            Picasso.get().load(imageResource1).into(imgpl2showcard1);
            Picasso.get().load(imageResource2).into(imgpl2showcard2);
            Picasso.get().load(imageResource3).into(imgpl2showcard3);

        }
        else if (user_id.equals(user_id_player3)) {
            rltSee3.setVisibility(View.GONE);
            lnrShowButtoncardspl3.setVisibility(View.VISIBLE);

            int imageResource1 = getResourcePath(cards1.toLowerCase());

            int imageResource2 = getResourcePath(cards2.toLowerCase());

            int imageResource3 = getResourcePath(cards3.toLowerCase());

            Picasso.get().load(imageResource1).into(imgpl3showcard1);
            Picasso.get().load(imageResource2).into(imgpl3showcard2);
            Picasso.get().load(imageResource3).into(imgpl3showcard3);


        }
        else if (user_id.equals(user_id_player4)) {
            rltSee4.setVisibility(View.GONE);
            lnrShowButtoncardspl4.setVisibility(View.VISIBLE);

            int imageResource1 = getResourcePath(cards1.toLowerCase());

            int imageResource2 = getResourcePath(cards2.toLowerCase());

            int imageResource3 = getResourcePath(cards3.toLowerCase());

            Picasso.get().load(imageResource1).into(imgpl4showcard1);
            Picasso.get().load(imageResource2).into(imgpl4showcard2);
            Picasso.get().load(imageResource3).into(imgpl4showcard3);


        }
        else if (user_id.equals(user_id_player5)) {
            rltSee5.setVisibility(View.GONE);
            lnrShowButtoncardspl5.setVisibility(View.VISIBLE);

            int imageResource1 = getResourcePath(cards1.toLowerCase());

            int imageResource2 = getResourcePath(cards2.toLowerCase());

            int imageResource3 = getResourcePath(cards3.toLowerCase());

            Picasso.get().load(imageResource1).into(imgpl5showcard1);
            Picasso.get().load(imageResource2).into(imgpl5showcard2);
            Picasso.get().load(imageResource3).into(imgpl5showcard3);

        }

    }

    public void makeleavetable() {

        txtPlay1.setText("Player 1");
        txtPlay1wallet.setText("");

        txtPlay2.setText("Player 2");
        txtPlay2wallet.setText("");
        lnrPlay2wallet.setVisibility(View.INVISIBLE);

        txtPlay3.setText("Player 3");
        txtPlay3wallet.setText("");
        lnrPlay3wallet.setVisibility(View.INVISIBLE);

        txtPlay4.setText("Player 4");
        txtPlay4wallet.setText("");
        lnrPlay4wallet.setVisibility(View.INVISIBLE);

        txtPlay5.setText("Player 5");
        txtPlay5wallet.setText("");
        lnrPlay5wallet.setVisibility(View.INVISIBLE);

        int imageResource = R.drawable.invite_u;


        Picasso.get().load(imageResource).into(imgpl1circle);
        Picasso.get().load(imageResource).into(imgpl2circle);
        Picasso.get().load(imageResource).into(imgpl3circle);
        Picasso.get().load(imageResource).into(imgpl4circle);
        Picasso.get().load(imageResource).into(imgpl5circle);

        rltplayer1growing.clearAnimation();
        rltplayer2growing.clearAnimation();
        rltplayer3growing.clearAnimation();
        rltplayer4growing.clearAnimation();
        rltplayer5growing.clearAnimation();


        rltplayer1growing.setVisibility(View.GONE);
        rltplayer2growing.setVisibility(View.GONE);
        rltplayer3growing.setVisibility(View.GONE);
        rltplayer4growing.setVisibility(View.GONE);
        rltplayer5growing.setVisibility(View.GONE);


        if (isGameStartforseeebtn) {


        } else {
            pStatusprogress = 0;
            mProgress1.setProgress(0);
            mProgress2.setProgress(0);
            mProgress3.setProgress(0);
            mProgress4.setProgress(0);
            mProgress5.setProgress(0);

            imgpl1glow.setVisibility(View.GONE);
            imgpl2glow.setVisibility(View.GONE);
            imgpl3glow.setVisibility(View.GONE);
            imgpl4glow.setVisibility(View.GONE);
            imgpl5glow.setVisibility(View.GONE);

            rltSee1.setVisibility(View.GONE);
            rltSee2.setVisibility(View.GONE);
            rltSee3.setVisibility(View.GONE);
            rltSee4.setVisibility(View.GONE);
            rltSee5.setVisibility(View.GONE);

            imgpack1.setVisibility(View.GONE);
            imgpack2.setVisibility(View.GONE);
            imgpack3.setVisibility(View.GONE);
            imgpack4.setVisibility(View.GONE);
            imgpack5.setVisibility(View.GONE);

        }

//        if (isGameStartforseeebtn)


//        if (user_id_player2.length() > 0){
//
//
//
//        }else{
//            rltSee2.setVisibility(View.GONE);
//
//        }
//
//        if (user_id_player3.length() > 0){
//
//
//
//        }else{
//            rltSee3.setVisibility(View.GONE);
//
//        }
//
//
//        if (user_id_player4.length() > 0){
//
//
//
//        }else{
//            rltSee4.setVisibility(View.GONE);
//
//        }
//
//
//        if (user_id_player.length() > 0){
//
//
//
//        }else{
//            rltSee2.setVisibility(View.GONE);
//
//        }


    }

    public void PlaySaund(int saund) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");

        if (value.equals("1")) {
            final MediaPlayer mp = MediaPlayer.create(this,
                    saund);
            mp.start();

        } else {


        }

    }

    private void GamePack(final String type) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_PACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            String chaal = "";
                            if (code.equals("200")) {
                                mCountDownTimer1.cancel();
                                mCountDownTimer2.cancel();
                                mCountDownTimer3.cancel();
                                mCountDownTimer4.cancel();
                                mCountDownTimer5.cancel();
                                mProgress1.setProgress(0);

                                isProgressrun1 = true;
                                isProgressrun2 = true;
                                isProgressrun3 = true;
                                isProgressrun4 = true;
                                isProgressrun5 = true;


                            } else {

                                // Functions.showToast(context, message, Toast.LENGTH_LONG)
                                // .show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                // Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                // .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                //params.put("user_id",prefs.getString("user_id", ""));
                params.put("user_id", chaak_user_id);
                //if (type.equals("1")){
                params.put("token", prefs.getString("token", ""));
                params.put("timeout", type);

                //  }
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

    private void GameChhal() {

        if(!Const.isNetworkAvailable(context))
        {
            isChalClick = false;
            Functions.showToast(context,getString(R.string.please_check_internet_connection));
            return;
        }

        imgpl1minus.setEnabled(false);
        mCountDownTimer1.cancel();
        mCountDownTimer2.cancel();
        mCountDownTimer3.cancel();
        mCountDownTimer4.cancel();
        mCountDownTimer5.cancel();
        mProgress1.setProgress(0);

        isProgressrun1 = true;
        isProgressrun2 = true;
        isProgressrun3 = true;
        isProgressrun4 = true;
        isProgressrun5 = true;

        // isPlayerPlayedChaal = true;

        //SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
        // MODE_PRIVATE);
        // params.put("user_id", prefs.getString("user_id", ""));
        //makeshowchaalamountfloat(prefs.getString("user_id", ""));


        if (sentamounttype.equals("1")) {

            txt_coin_to_girl_player1.setText(""+Variables.CURRENCY_SYMBOL + updatedamount);

        } else {

            txt_coin_to_girl_player1.setText(""+Variables.CURRENCY_SYMBOL + table_amount);


        }
        PlaySaund(R.raw.teenpattichipstotable);
        txt_coin_to_girl_player1.setVisibility(View.VISIBLE);
        txt_coin_to_girl_player1.startAnimation(animMove1_2);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_CHAAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Functions.LOGE("Public_Table",""+Const.GAME_CHAAL+"\n"+response);

                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            //  if (code.equals("200")) {

                            //   thread.interrupt();
//                                imgpl1minus.setEnabled(false);
//                                mCountDownTimer1.cancel();
//                                mCountDownTimer2.cancel();
//                                mCountDownTimer3.cancel();
//                                mCountDownTimer4.cancel();
//                                mCountDownTimer5.cancel();
//                                mProgress1.setProgress(0);
//
//                                isProgressrun1 = true;
//                                isProgressrun2 = true;
//                                isProgressrun3 = true;
//                                isProgressrun4 = true;
//                                isProgressrun5 = true;
//
//                                // isPlayerPlayedChaal = true;
//
//                                //SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
//                                // MODE_PRIVATE);
//                                // params.put("user_id", prefs.getString("user_id", ""));
//                                //makeshowchaalamountfloat(prefs.getString("user_id", ""));
//

                            if (sentamounttype.equals("1")) {

                                txt_coin_to_girl_player1.setText(""+Variables.CURRENCY_SYMBOL + updatedamount);

                            } else {

                                txt_coin_to_girl_player1.setText(""+Variables.CURRENCY_SYMBOL + table_amount);


                            }
//                                PlaySaund(R.raw.teenpattichipstotable);
//                                txt_coin_to_girl_player1.setVisibility(View.VISIBLE);
//                                txt_coin_to_girl_player1.startAnimation(animMove1_2);
                            sentamounttype = "0";
//                            } else {
//
//                                // Functions.showToast(context, message, Toast.LENGTH_LONG)
//                                // .show();
//
//                            }
                            isChalClick = false;


                        } catch (JSONException e) {
                            e.printStackTrace();
                            isChalClick = false;

                        }

                        isChalClick = false;
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                // Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                // .show();
                isChalClick = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("plus", sentamounttype);
                params.put("token", prefs.getString("token", ""));
                Functions.LOGE("Public_Table_params",""+Const.GAME_CHAAL+"\n"+params);

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

    private void GameShow() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_SHOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {
                                mCountDownTimer1.cancel();
                                mCountDownTimer2.cancel();
                                mCountDownTimer3.cancel();
                                mCountDownTimer4.cancel();
                                mCountDownTimer5.cancel();
                                mProgress1.setProgress(0);

                                isProgressrun1 = true;
                                isProgressrun2 = true;
                                isProgressrun3 = true;
                                isProgressrun4 = true;
                                isProgressrun5 = true;
                                // isPlayerPlayedChaal = true;

                                // SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
                                //   MODE_PRIVATE);
                                // params.put("user_id", prefs.getString("user_id", ""));
                                //makeshowchaalamountfloat(prefs.getString("user_id", ""));
                                PlaySaund(R.raw.teenpattichipstotable);
                                txt_coin_to_girl_player1.setText(""+Variables.CURRENCY_SYMBOL + table_amount);
                                txt_coin_to_girl_player1.setVisibility(View.VISIBLE);
                                txt_coin_to_girl_player1.startAnimation(animMove1_2);
                            } else {

                                //Functions.showToast(context, message, Toast.LENGTH_LONG)
                                // .show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ManageBottomActionButton(false);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                // Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                // .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
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

    private void ManageBottomActionButton(boolean isVisible){
        lnrGameButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        rltGameButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    CountDownTimer slideshowTimer;
    private void ManageSlideShowAction(boolean isVisible){
        rltslidshow.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        if(isVisible)
        {
            slideshowTimer = new CountDownTimer(7000,1000)
            {

                @Override
                public void onTick(long millisUntilFinished) {
//                    getTextView(R.id.tvSlideCountDown).setText((millisUntilFinished/1000)+" s");
                }

                @Override
                public void onFinish() {
                    GameSideAction(slidshow_id, "2");
                    rltslidshow.setVisibility(View.GONE);
                    isSlideShow = false;

                }
            }.start();

        }
        else {
            if(slideshowTimer != null)
            {
                isSlideShow = false;
                slideshowTimer.cancel();
            }
        }

    }

    private TextView getTextView(int id){
        return  ((TextView)findViewById(id));
    }

    private void GameSideShow(final String prev_user_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_SIDE_SHOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            // String message = jsonObject.getString("message");

                            if (code.equals("200")) {

                                slidshow_id = jsonObject.getString("slide_id");

                            }

                            slidshowcounter.start();

//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ManageBottomActionButton(false);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("prev_user_id", prev_user_id);
                params.put("token", prefs.getString("token", ""));
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

    private void GameSideAction(final String slidshowid, final String slide_type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_SIDE_SHOW_CANCEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            // String message = jsonObject.getString("message");

//                            if (code.equals("200")){
//
//                                slidshow_id= jsonObject.getString("slide_id");
//
//                            }
                            //  slidshowcounter.start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        GameStatus();
                        ManageSlideShowAction(false);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("slide_id", slidshowid);
                params.put("token", prefs.getString("token", ""));
                params.put("type", slide_type);
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

    private void GameStart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_START,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {

                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    //Functions.showToast(context, message, Toast
                                    // .LENGTH_LONG).show();
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
                //  Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                //  .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
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

    private void SeeCards1() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SEE_CARDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                PlaySaund(R.raw.teenpatticardflip_android);
                                PlaySaund(R.raw.teenpatticardflip_android);
                                PlaySaund(R.raw.teenpatticardflip_android);
                                rltSee1.setVisibility(View.GONE);

                                if (jsonObject.has("cards")) {
                                    imgsee1.setVisibility(View.GONE);
                                    imgseen1.setVisibility(View.GONE);
                                    imgblind1.setVisibility(View.VISIBLE);
                                    ChangeTexttoChaal(true);


                                    JSONObject jsonObject0 = jsonObject.getJSONArray("cards").getJSONObject(0);
                                    String card1 = jsonObject0.getString("card1");
                                    String card2 = jsonObject0.getString("card2");
                                    String card3 = jsonObject0.getString("card3");

                                    int imageResource1 = getResourcePath(card1.toLowerCase());

                                    int imageResource2 = getResourcePath(card2.toLowerCase());

                                    int imageResource3 = getResourcePath(card3.toLowerCase());

                                    Picasso.get().load(imageResource1).into(imgpl1hidencard1);
                                    Picasso.get().load(imageResource2).into(imgpl1hidencard2);
                                    Picasso.get().load(imageResource3).into(imgpl1hidencard3);


                                } else {

                                    if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
//                                        Functions.showToast(context, "Wrong mobile number or password",
//                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    // Functions.showToast(context, message, Toast
                                    // .LENGTH_LONG).show();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   progressDialog.dismiss();
                // Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                // .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
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

    private void SeeCards2() {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SEE_CARDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {

                                // lnrcards2.setVisibility(View.VISIBLE);
                                // lnrSeeButtoncardspl2.setVisibility(View.GONE);
                                if (jsonObject.has("cards")) {
                                    JSONObject jsonObject0 = jsonObject.getJSONArray("cards").getJSONObject(0);
                                    String card1 = jsonObject0.getString("card1");
                                    String card2 = jsonObject0.getString("card2");
                                    String card3 = jsonObject0.getString("card3");


                                    int imageResource1 = getResourcePath(card1.toLowerCase());

                                    int imageResource2 = getResourcePath(card2.toLowerCase());

                                    int imageResource3 = getResourcePath(card3.toLowerCase());



                                } else {

                                    if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
//                                        Functions.showToast(context, "Wrong mobile number or password",
//                                                Toast.LENGTH_LONG).show();
                                    }

                                }

                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    // Functions.showToast(context, message, Toast
                                    // .LENGTH_LONG).show();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                // .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", play2id);
                params.put("token", prefs.getString("token", ""));
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

    private void GameTableChange() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_SWITCH_TABLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {
                                JSONObject jsonObject0 = jsonObject.getJSONArray("table_data").getJSONObject(0);
                                //  game_id= jsonObject0.getString("id");
                                table_id = jsonObject0.getString("table_id");

                                mProgress1.setProgress(0);
                                mCountDownTimer1.cancel();
                                count = 8;
                                counttimerforstartgame.start();
                                //  txtTableid.setText(table_id);
                            } else {

                                //  Functions.showToast(context, message, Toast.LENGTH_LONG)
                                //  .show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                // Functions.showToast(context, "Something went wrong", Toast.LENGTH_LONG)
                // .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
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

    @Override
    public void onBackPressed() {

        showDialoagonBack();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCountDownTimer1.cancel();
        mCountDownTimer2.cancel();
        mCountDownTimer3.cancel();
        mCountDownTimer4.cancel();
        mCountDownTimer5.cancel();

        if (timerstatus != null) {
            timerstatus.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (table_id.length() > 0){
//
//            GameLeave();
//
//        }
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private String getScreenSize() {
        Point point = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealSize(point);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = point.x;
        int height = point.y;
        double wi = (double) width / (double) displayMetrics.xdpi;
        double hi = (double) height / (double) displayMetrics.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        return String.valueOf(Math.round((Math.sqrt(x + y)) * 10.0) / 10.0);
    }


    public void SlideUP(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_up));
    }

    public void SlideDown(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_down));
    }


    private void showGiftDialog(String players){

        Functions.showGiftDialog(context, players, new Callback() {
            @Override
            public void Responce(String resp,String playerno, Bundle bundle) {

//                try {

                String girfurl = ""+bundle.getString("gifturl");

//                    Parse_Response(resp,playerno,girfurl);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });

    }

    private void Parse_Response(String resp,String playerno,String gifturl) throws JSONException {

        JSONObject jsonObject = new JSONObject(resp);

        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");

        if (code.equalsIgnoreCase("200")) {

            if (playerno.equals("2")) {
                imggift2.setVisibility(View.VISIBLE);
                Glide.with(context)
//
                        .load(Const.IMGAE_PATH + gifturl)
//                        .skipMemoryCache(true)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                .placeholder(context.getResources().getDrawable(R.drawable.app_icon)).centerCrop())
                        .into(imggift2);

                imggift2.setVisibility(View.GONE);
            }
            else if (playerno.equals("3")) {
                imggift3.setVisibility(View.VISIBLE);
                Glide.with(context)
//
                        .load(Const.IMGAE_PATH + gifturl)
//                        .skipMemoryCache(true)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                .placeholder(context.getResources().getDrawable(R.drawable.app_icon)).centerCrop())
                        .into(imggift3);
                imggift3.setVisibility(View.GONE);

            }
            else if (playerno.equals("4")) {
                imggift4.setVisibility(View.VISIBLE);
                Glide.with(context)
//
                        .load(Const.IMGAE_PATH + gifturl)
//                        .skipMemoryCache(true)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                .placeholder(context.getResources().getDrawable(R.drawable.app_icon)).centerCrop())
                        .into(imggift4);
                imggift4.setVisibility(View.GONE);
            }
            else if (playerno.equals("5")) {
                imggift5.setVisibility(View.VISIBLE);
                Glide.with(context)
//
                        .load(Const.IMGAE_PATH + gifturl)
//                        .skipMemoryCache(true)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                .placeholder(context.getResources().getDrawable(R.drawable.app_icon)).centerCrop())
                        .into(imggift5);
                imggift5.setVisibility(View.GONE);
            }
            else {
                imggift5.setVisibility(View.VISIBLE);
                Glide.with(context)
//
                        .load(Const.IMGAE_PATH + gifturl)
//                        .skipMemoryCache(true)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                .placeholder(context.getResources().getDrawable(R.drawable.app_icon)).centerCrop())
                        .into(imggift5);

            }


            // Functions.showToast(context, ""+message);

        } else {
            if (jsonObject.has("message")) {

//                                    Functions.showToast(context, message,
//                                            Toast.LENGTH_LONG).show();
            }


        }


    }

    public void openGameRules(View view) {
        DialogRulesTeenpatti.getInstance(context).show();
    }

    public void openBuyChipsDetails(View view)
    {
        Intent intent = new Intent(PublicTable_New.this, BuyChipsList.class);
        startActivity(intent);
    }

}
