package com.metagards.metagames.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.first_player_games.Api.LudoApiRepository;
import com.first_player_games.Home_Activity;
import com.first_player_games.LocalGame.LocalGame;
import com.first_player_games.LocalGame.VsComputer;
import com.first_player_games.Menu.ComputerGameMenu;
import com.first_player_games.Menu.LocalGameMenu;
import com.first_player_games.OnlineGame.Lobby.RoomCreationActivity;
import com.first_player_games.OnlineGame.Lobby.RoomJoinActivity;
import com.first_player_games.ludoApi.TableMaster;
import com.first_player_games.ludoApi.bottomFragment.LudoActiveTables_BF;
import com.metagards.metagames.Adapter.HomegameListAdapter;
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.Comman.CommonAPI;
import com.metagards.metagames.Comman.DialogRestrictUser;
import com.metagards.metagames.Comman.DialogSettingOption;
import com.metagards.metagames.Details.GameDetails_A;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.LocationManager.GetLocationlatlong;
import com.metagards.metagames.LocationManager.GpsUtils;
import com.metagards.metagames.Menu.DialogDailyBonus;
import com.metagards.metagames.Menu.DialogReferandEarn;
import com.metagards.metagames.Menu.DialogUserRanking;
import com.metagards.metagames.MyFlowLayout;
import com.metagards.metagames.RedeemCoins.WithdrawalList;
import com.metagards.metagames.Statement;
import com.metagards.metagames.Utils.Sound;
import com.metagards.metagames._AdharBahar.Andhar_Bahar_NewUI;
import com.metagards.metagames._AdharBahar.Andhar_Bahar_Socket;
import com.metagards.metagames._AnimalRoulate.AnimalRoulette_A;
import com.metagards.metagames._AnimalRoulate.AnimalRoulette_Socket;
import com.metagards.metagames._Aviator.Aviator_Game_Activity;
import com.metagards.metagames._CarRoullete.CarRoullete_A;
import com.metagards.metagames._CarRoullete.CarRoullete_Socket;
import com.metagards.metagames._CoinFlip.HeadTail_A;
import com.metagards.metagames._CoinFlip.HeadTail_Socket;
import com.metagards.metagames._ColorPrediction.ColorPrediction;
import com.metagards.metagames._ColorPrediction.ColorPrediction1_Socket;
import com.metagards.metagames._ColorPrediction.ColorPrediction3_Socket;
import com.metagards.metagames._ColorPrediction.ColorPrediction_Socket;
import com.metagards.metagames._DragonTiger.DragonTigerSocket;
import com.metagards.metagames.Fragments.ActiveTables_BF;
import com.metagards.metagames.Fragments.UserInformation_BT;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Animations;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames._DragonTiger.DragonTiger_A;
import com.metagards.metagames._LuckJackpot.LuckJackPot_A;
import com.metagards.metagames._LuckJackpot.LuckJackPot_A_Socket;
import com.metagards.metagames._Poker.Fragment.PokerActiveTables_BF;
import com.metagards.metagames._RedBlack.RedBlackPot_A;
import com.metagards.metagames._RedBlack.RedBlackPot_Socket;
import com.metagards.metagames._Rummy.RummPrivate;
import com.metagards.metagames._RummyDeal.DialogDealType;
import com.metagards.metagames._RummyDeal.Fragments.RummyDealActiveTables_BF;
import com.metagards.metagames._RummyDeal.RummyDealGame;
import com.metagards.metagames._RummyPull.Fragments.RummyActiveTables_BF;
import com.metagards.metagames._SevenUpGames.SevenUp_A;
import com.metagards.metagames._SevenUpGames.SevenUp_Socket;
import com.metagards.metagames._TeenPatti.PublicTable_New;
import com.metagards.metagames._TeenPatti.TeenPattiSocket;
import com.metagards.metagames._Tournament.TourList;
import com.metagards.metagames._baccarat.Baccarat_A;
import com.metagards.metagames._baccarat.Baccarat_Socket;
import com.metagards.metagames._jhandhiMunda.JhandhiMunda_A;
import com.metagards.metagames._jhandhiMunda.JhandhiMunda_Socket;
import com.metagards.metagames._rouleteGame.RouleteGame_A;
import com.metagards.metagames._rouleteGame.RouleteGame_Socket;
import com.metagards.metagames.account.LoginScreen;
import com.metagards.metagames.model.BannerModel;
import com.metagards.metagames.model.HomescreenModel;
//import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static com.metagards.metagames.GAMES.ANDHAR_BAHAR;
import static com.metagards.metagames.GAMES.ANIMAL_ROULETTE;
import static com.metagards.metagames.GAMES.AVIATOR;
import static com.metagards.metagames.GAMES.BACCARAT;
import static com.metagards.metagames.GAMES.CAR_ROULETTE;
import static com.metagards.metagames.GAMES.COLOUR_PREDICTION;
import static com.metagards.metagames.GAMES.COLOUR_PREDICTION1;
import static com.metagards.metagames.GAMES.CUSTOM_TABLE;
import static com.metagards.metagames.GAMES.DEAL_RUMMY;
import static com.metagards.metagames.GAMES.DRAGON_TIGER;
import static com.metagards.metagames.GAMES.HEAD_TAIL;
import static com.metagards.metagames.GAMES.JACKPOT_3PATTI;
import static com.metagards.metagames.GAMES.JHANDHI_MUNDA;
import static com.metagards.metagames.GAMES.LUDO_GAME_COMPUTER;
import static com.metagards.metagames.GAMES.LUDO_GAME_ONLINE;
import static com.metagards.metagames.GAMES.LUDO_GAME_PLAY_LOCAL;
import static com.metagards.metagames.GAMES.POINT_RUMMY;
import static com.metagards.metagames.GAMES.POKER_GAME;
import static com.metagards.metagames.GAMES.POOL_RUMMY;
import static com.metagards.metagames.GAMES.PRIVATE_RUMMY;
import static com.metagards.metagames.GAMES.PRIVATE_TABLE;
import static com.metagards.metagames.GAMES.RED_BLACK;
import static com.metagards.metagames.GAMES.SEVEN_UP_DOWN;
import static com.metagards.metagames.GAMES.TEENPATTI;
import static com.metagards.metagames.GAMES.ROULETTE;
import static com.metagards.metagames.GAMES.TOURNAMENT;
import static com.metagards.metagames.GAMES.COLOUR_PREDICTION3;
import static com.metagards.metagames.LocationManager.GpsUtils.ENABLE_LOCATION_CODE;
import static com.metagards.metagames.Utils.Functions.convertDpToPixel;

import static easypay.appinvoke.manager.PaytmAssist.getContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class Homepage extends BaseActivity {
    LocalGameMenu localGameMenu;
    ComputerGameMenu computergamemenu;
    List<String> ban_states = new ArrayList<>();
    Animation animBounce, animBlink;
    public static final String MY_PREFS_NAME = "Login_data";
    ImageView imgLogout, imgshare, imaprofile;
    ImageView imgpublicGame, imgPrivategame, ImgCustomePage, ImgVariationGane, iv_andher, ivIcon;
    private String user_id, name, mobile, profile_pic, referral_code, wallet, game_played, bank_detail, adhar_card, upi;
    private TextView txtName, txtwallet, txtproname;
    LinearLayout lnrbuychips, lnrinvite, lnrmail, lnrsetting, lnrvideo;
    Typeface helvatikaboldround, helvatikanormal;
    public String token = "";
    private String game_for_private, app_version;
    SeekBar sBar;
    SeekBar sBarpri;
    ImageView imgCreatetable, imgCreatetablecustom, imgclosetoppri, imgclosetop,homespin;
    int pval = 1;
    int pvalpri = 1;
    Button btnCreatetable;
    Button btnCreatetablepri;
    TextView txtStart, txtLimit, txtwalletchips,
            txtwalletchipspri, txtBootamount, txtPotLimit, txtNumberofBlind,
            txtMaximumbetvalue;
    TextView txtStartpri, txtLimitpri, txtBootamountpri, txtPotLimitpri, txtNumberofBlindpri, txtMaximumbetvaluepri;
    RelativeLayout rltimageptofile;
    int version = 0;
    public static String profile_img="";

    RelativeLayout rootView;

    public static String str_colr_pred = "";
    String base_64 = "", str_filter_val="";
    ProgressDialog progressDialog;
    Activity context = this;

    String REFERRAL_AMOUNT = "referral_amount";
    String JOINING_AMOUNT = "joining_amount";

    AutoScrollViewPager headerViewPager;
    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
//    HorizontalInfiniteCycleViewPager viewPager;
    Timer timer;
    ViewPager viewpager;
    private String str_user_id;

    Random random = new Random();

    private void emitBubbles() {
        // It will create a thread and attach it to
        // the main thread
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                int size = random.nextInt(250);
//                bubbleEmitter.emitBubble(size);
//                bubbleEmitter.setColors(android.R.color.transparent,
//                        android.R.color.holo_blue_light,
//                        android.R.color.holo_blue_bright);
//                emitBubbles();
            }
        }, random.nextInt(500));
    }


//    BubbleEmitterView bubbleEmitter;

    @BindView(R.id.tvUserCategory)
    TextView tvUserCategory;

//new
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);


        LudoApiRepository.getInstance().init(this);

        soundMedia = new Sound();
        initGamesTabs();

        SharePref.getInstance().init(context);

        findViewById(R.id.rltDragonTiger).setVisibility(SharePref.getIsDragonTigerVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltTeenpatti).setVisibility(SharePref.getIsTeenpattiVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltPrivate).setVisibility(SharePref.getIsPrivateVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltCustom).setVisibility(SharePref.getIsCustomVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltAndharbhar).setVisibility(SharePref.getIsAndharBaharVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltRummy).setVisibility(SharePref.getIsRummyVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltJackpot).setVisibility(Variables.JACKPOTGAME_SHOW ? View.VISIBLE : View.GONE);
        findViewById(R.id.rltRummyDeal).setVisibility(Variables.RUMMYDEAL_SHOW ? View.VISIBLE : View.GONE);
        findViewById(R.id.rltRummyPool).setVisibility(Variables.RUMMPOOL_SHOW ? View.VISIBLE : View.GONE);
        findViewById(R.id.rltSeveUp).setVisibility(Variables.SEVENUPSDOWN_SHOW ? View.VISIBLE : View.GONE);
        findViewById(R.id.rltCarRoullete).setVisibility(Variables.CARROULETE_SHOW ? View.VISIBLE : View.GONE);
        findViewById(R.id.rltAnimalRoullete).setVisibility(Variables.ANIMALROULETE_SHOW ? View.VISIBLE : View.GONE);

//        viewPager = findViewById(R.id.view_pager);
//        headerViewPager = (AutoScrollViewPager) findViewById(R.id.pager);
        viewpager = findViewById(R.id.viewpager);

        imgLogout = findViewById(R.id.imgLogout);
        initHomeScreenModel();
        ivIcon = findViewById(R.id.ivIcon);
        Glide.with(context) .asGif()
                .load(R.drawable.home_lady).into(ivIcon);
        lnrbuychips = findViewById(R.id.lnrbuychips);
        lnrmail = findViewById(R.id.lnrmail);
        lnrinvite = findViewById(R.id.lnrinvite);
        lnrsetting = findViewById(R.id.lnrsetting);


        imaprofile = findViewById(R.id.imaprofile);


        emitBubbles();

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        FrameLayout home_container = findViewById(R.id.home_container);
        home_container.setVisibility(View.VISIBLE);


        rootView = findViewById(R.id.rlt_animation_layout);
        RelativeLayout relativeLayout = findViewById(R.id.rlt_parent);
//        SetBackgroundImageAsDisplaySize(context,relativeLayout,R.drawable.splash);


//        BonusView();


        // Set fullscreen
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        helvatikaboldround = Typeface.createFromAsset(getAssets(),
                "fonts/helvetica-rounded-bold-5871d05ead8de.otf");

        helvatikanormal = Typeface.createFromAsset(getAssets(),
                "fonts/Helvetica.ttf");

        rltimageptofile = findViewById(R.id.rltimageptofile);

        rltimageptofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserInformation_BT userInformation_bt = new UserInformation_BT(new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {
                        UserProfile();
                    }
                });
                userInformation_bt.setCancelable(false);
                userInformation_bt.show(getSupportFragmentManager(), userInformation_bt.getTag());
            }
        });
        homespin = (ImageView) findViewById(R.id.homespin);
        Glide.with(context) .asGif()
                .load(R.drawable.home_spin_win).into(homespin);
        imgpublicGame = (ImageView) findViewById(R.id.imgpublicGame);
        Glide.with(context) .asGif()
                .load(R.drawable.home_public_img).into(imgpublicGame);
        imgPrivategame = (ImageView) findViewById(R.id.imgPrivategame);
        ImgCustomePage = (ImageView) findViewById(R.id.ImgCustomePage);
        ImgVariationGane = (ImageView) findViewById(R.id.ImgVariationGane);
        Glide.with(context) .asGif()
                .load(R.drawable.home_rummy_point).into(ImgVariationGane);


        iv_andher = (ImageView) findViewById(R.id.iv_andher);
        Glide.with(context) .asGif()
                .load(R.drawable.home_andherbahar).into(iv_andher);

        txtName = findViewById(R.id.txtName);
        txtName.setTypeface(helvatikaboldround);
        txtwallet = findViewById(R.id.txtwallet);
        txtwallet.setTypeface(helvatikanormal);
        txtproname = findViewById(R.id.txtproname);
        txtproname.setTypeface(helvatikaboldround);
        TextView txtMail = findViewById(R.id.txtMail);

        // load the animation
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        imgpublicGame.startAnimation(animBlink);
        imgpublicGame.startAnimation(animBounce);


        imgPrivategame.startAnimation(animBlink);
        imgPrivategame.startAnimation(animBounce);


        ImgCustomePage.startAnimation(animBlink);
        ImgCustomePage.startAnimation(animBounce);
        ImgCustomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomeTeenpatti(view);
            }
        });


        ImgVariationGane.startAnimation(animBlink);
        ImgVariationGane.startAnimation(animBounce);
        clickTask();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        // Funtions.showToast(context, token);
                        UserProfile();

                    }
                });


      //  rotation_animation(((ImageView) findViewById(R.id.imgsetting)), R.anim.rotationback_animation);


        findViewById(R.id.lnr_redeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, WithdrawalList.class));

//                LoadFragment(new WalletFragment());
            }
        });

        findViewById(R.id.lnr_statement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Statement.class));
            }
        });

        findViewById(R.id.lnr_spinner).setOnClickListener(v -> startActivity(new Intent(context, Spinner_Wheels.class)));
        findViewById(R.id.lnr_spin_win).setOnClickListener(v -> startActivity(new Intent(context, Spinner_Wheels_Reward.class)));

        findViewById(R.id.lnrhistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(context,MyWinningAcitivity.class));
                startActivity(new Intent(context, GameDetails_A.class));

//                showRedeemDailog();
            }
        });
        findViewById(R.id.lnr_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyWinCoins();
            }
        }); findViewById(R.id.lnr_first_reacharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyWinCoins();
            }
        });


        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuyChipsList.class);
                intent.putExtra("homepage", "homepage");
                startActivity(intent);
//                rotation_animation(findViewById(R.id.iv_add), R.anim.rotationback_animation);
//                UserProfile();
            }
        });

        TextView btn_all,btn_multiplayer,btn_skills,btn_sports,btn_slots;
        LinearLayout lnr_all,lnr_multiplayer,lnr_skills,lnr_sports,lnr_slots;

        lnr_all = findViewById(R.id.lnr_all);
        lnr_multiplayer = findViewById(R.id.lnr_multiplayer);
        lnr_skills = findViewById(R.id.lnr_skills);
        lnr_sports = findViewById(R.id.lnr_sports);
        lnr_slots = findViewById(R.id.lnr_slots);

        btn_all = findViewById(R.id.btn_all);
        btn_multiplayer = findViewById(R.id.btn_multiplayer);
        btn_skills = findViewById(R.id.btn_skills);
        btn_sports = findViewById(R.id.btn_sports);
        btn_slots = findViewById(R.id.btn_slots);

        lnr_all.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                str_filter_val = "";
                filter(str_filter_val.toString());
                btn_all.setTextColor(getColor(R.color.red));
                btn_multiplayer.setTextColor(getColor(R.color.black));
                btn_skills.setTextColor(getColor(R.color.black));
                btn_slots.setTextColor(getColor(R.color.black));
            }
        });

        lnr_multiplayer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                str_filter_val = "multi";
                filter(str_filter_val.toString());
                btn_all.setTextColor(getColor(R.color.black));
                btn_multiplayer.setTextColor(getColor(R.color.red));
                btn_skills.setTextColor(getColor(R.color.black));
                btn_slots.setTextColor(getColor(R.color.black));
            }
        });

        lnr_skills.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                str_filter_val = "skills";
                filter(str_filter_val.toString());
                btn_all.setTextColor(getColor(R.color.black));
                btn_multiplayer.setTextColor(getColor(R.color.black));
                btn_skills.setTextColor(getColor(R.color.red));
                btn_slots.setTextColor(getColor(R.color.black));
            }
        });

        lnr_slots.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                str_filter_val = "slots";
                filter(str_filter_val.toString());
                btn_all.setTextColor(getColor(R.color.black));
                btn_multiplayer.setTextColor(getColor(R.color.black));
                btn_skills.setTextColor(getColor(R.color.black));
                btn_slots.setTextColor(getColor(R.color.red));
            }
        });

        findViewById(R.id.btn_sports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


    }

    String[] gamelist = {
            "All",
            "Skills",
            "Sports",
    };

    MyFlowLayout lnrGamesTabs;
    int tabsCount = 0;

    private void initGamesTabs() {
        tabsCount = 0;
        lnrGamesTabs = findViewById(R.id.lnrGamesTabs);
        for (String tabs : gamelist) {
            CreateTabsLayout(tabsCount, tabs);
            tabsCount++;
        }
    }

    private void CreateTabsLayout(final int position, String name) {
        final View view = Functions.CreateDynamicViews(R.layout.item_gamehistory_tabs, lnrGamesTabs, context);
        String strtitle = name;
        view.setTag("" + strtitle);

        TextView title = view.findViewById(R.id.tvGameRecord);

        title.setText(strtitle);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPagerPostion(strtitle);
            }
        });

        if (position == 0)
            setPagerPostion(strtitle);
    }

    String selectedTab = "";

    private void setPagerPostion(String name) {
        for (int i = 0; i < lnrGamesTabs.getChildCount(); i++) {

            View view = lnrGamesTabs.getChildAt(i);
            TextView title = view.findViewById(R.id.tvGameRecord);

            if (Functions.getStringFromTextView(title).equalsIgnoreCase(name)) {
                if (homegameListAdapter != null)
                    homegameListAdapter.getFilter().filter(name);
                title.setTextColor(context.getResources().getColor(R.color.black));
                title.setBackground(context.getResources().getDrawable(R.drawable.btn_yellow_discard));
            } else {
                title.setTextColor(context.getResources().getColor(R.color.white));
                title.setBackground(context.getResources().getDrawable(R.drawable.d_white_corner));
            }

        }
    }

    HomegameListAdapter homegameListAdapter;

    private void initHomeScreenModel() {
        RecyclerView recGamesList = findViewById(R.id.recGamesList);
        EditText edit_searchview = findViewById(R.id.edit_searchview);

        homegameListAdapter = new HomegameListAdapter(context);
        homegameListAdapter.setArrayList(getGameList());
        recGamesList.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false));
        homegameListAdapter.setCallback(new OnItemClickListener() {
            @Override
            public void Response(View v, int position, Object object) {
                Enum gametype = (Enum) object;
                if (TEENPATTI.equals(gametype)) {
                    openPublicTeenpatti(null);
                } else if (DRAGON_TIGER.equals(gametype)) {
                    openDragonTiger(null);
                } else if (ANDHAR_BAHAR.equals(gametype)) {
                    openAndharbahar(null);
                } else if (POINT_RUMMY.equals(gametype)) {
                    openRummyGame(null);
                } else if (PRIVATE_RUMMY.equals(gametype)) {
                    openPrivateRummyTable();
//                    DialogRummyCreateTable.getInstance(context).show();
                } else if (POOL_RUMMY.equals(gametype)) {
                    openRummyPullGame(null);
                } else if (DEAL_RUMMY.equals(gametype)) {
                    openRummyDealGame(null);
                } else if (PRIVATE_TABLE.equals(gametype)) {
                    openPrivateTeenpatti(null);
                } else if (CUSTOM_TABLE.equals(gametype)) {
                    openCustomeTeenpatti(null);
                } else if (SEVEN_UP_DOWN.equals(gametype)) {
                    openSevenup(null);
                } else if (CAR_ROULETTE.equals(gametype)) {
                    openCarRoulette(null);
                } else if (JACKPOT_3PATTI.equals(gametype)) {
                    openLuckJackpotActivity(null);
                } else if (ANIMAL_ROULETTE.equals(gametype)) {
                    openAnimalRoullete(null);
                } else if (COLOUR_PREDICTION.equals(gametype)) {
                     openColorPred(null);
                } else if (POKER_GAME.equals(gametype)) {
                    openPokerGame(null);
                } else if (HEAD_TAIL.equals(gametype)) {
                    openHeadTailGame(null);
                } else if (RED_BLACK.equals(gametype)) {
                    openRedBlackGame(null);
                } else if (LUDO_GAME_PLAY_LOCAL.equals(gametype)) {
                    openLudoGames_LOCAL(null);
                } else if (LUDO_GAME_COMPUTER.equals(gametype)) {
                    openLudoGames_COMPUTER(null);
                } else if (LUDO_GAME_ONLINE.equals(gametype)) {
                    openLudoGames_ONLINE(null);
                } else if (BACCARAT.equals(gametype)) {
                    openBaccarat(null);
                } else if (JHANDHI_MUNDA.equals(gametype)) {
                    openJhandhiMunda(null);
                }else if (ROULETTE.equals(gametype)) {
                    openRoulette(null);
                }else if (TOURNAMENT.equals(gametype)) {
                    openTournament();
                }else if (COLOUR_PREDICTION1.equals(gametype)) {
                    openColorPred1(null);
                } else if (COLOUR_PREDICTION3.equals(gametype)) {
                    openColorPred3(null);
                }
                else if (AVIATOR.equals(gametype)) {
                    openAviator();
                }
                else {
                    Functions.showToast(context, "Coming soon!");
                }
            }
        });
        recGamesList.setAdapter(homegameListAdapter);

        edit_searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }

    public void filter(String text){
        List<HomescreenModel> temp = new ArrayList();
        for(HomescreenModel d: getGameList()){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getItemType().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        homegameListAdapter.updateList(temp);
    }

    private void openJhandhiMunda(Object o) {
        startActivity(new Intent(getApplicationContext(), JhandhiMunda_A.class));
//        startActivity(new Intent(getApplicationContext(), JhandhiMunda_Socket.class));
    }

    private void openBaccarat(Object o) {
        startActivity(new Intent(getApplicationContext(), Baccarat_A.class));
//        startActivity(new Intent(getApplicationContext(), Baccarat_Socket.class));
    }

    private void openLudoGames(View view) {
        startActivity(new Intent(getApplicationContext(), Home_Activity.class));
    }

    private void openLudoGames_LOCAL(View view) {
        LocalGameMenu localgamemenu = new LocalGameMenu(this) {
            public void onGameStartAction(boolean[] players) {
                Intent intent = new Intent(Homepage.this, LocalGame.class);
                intent.putExtra("players", players);
                startActivity(intent);
            }
        };
        localgamemenu.showMenu();

    }

    private void openLudoGames_ONLINE(View view) {
      StartOnlineLudo();
    }

    private void StartOnlineLudo() {
        LudoActiveTables_BF ludoActiveTables_bf = new LudoActiveTables_BF();
        ludoActiveTables_bf.setCallback(new com.first_player_games.ClassCallback() {
            @Override
            public void Response(View v, int position, Object object) {
                if (object instanceof TableMaster.TableDatum) {
                    TableMaster.TableDatum tableDatum = (TableMaster.TableDatum) object;

                    if (!com.first_player_games.Others.Functions.checkStringisValid(tableDatum.getInvite_code()) && !tableDatum.getOnlineMembers().equals("1")) {
                        // Create Free Room
                        startActivity(new Intent(Homepage.this, RoomCreationActivity.class)
                                .putExtra("diamonds", 0).putExtra("boot_value", tableDatum.getBootValue()));
                    } else {
                        String roomid = tableDatum.getInvite_code();
                        Log.e("roomid", "Response: "+roomid );
                        Intent intent = new Intent(Homepage.this, RoomJoinActivity.class);
                        intent.putExtra("roomid", roomid);
                        startActivity(intent);
                    }

                }

            }
        });
        ludoActiveTables_bf.show(getSupportFragmentManager(), "lus");

    }

    private void openLudoGames_COMPUTER(View view) {
        computergamemenu = new ComputerGameMenu(Homepage.this) {
            public void onGameStartAction(int[] players) {
                Intent intent = new Intent(Homepage.this, VsComputer.class);
                intent.putExtra("players", players);
                startActivity(intent);
            }
        };
        computergamemenu.showMenu();
    }

    private void openRedBlackGame(View view) {
        startActivity(new Intent(getApplicationContext(), RedBlackPot_A.class));
//        startActivity(new Intent(getApplicationContext(), RedBlackPot_Socket.class));
    }

    private void openHeadTailGame(View view) {
        startActivity(new Intent(getApplicationContext(), HeadTail_A.class));
//        startActivity(new Intent(getApplicationContext(), HeadTail_Socket.class));
    }

    private void openPrivateRummyTable() {
        LoadTableFragments(ActiveTables_BF.RUMMY_PRIVATE_TABLE);
    }

    private List<HomescreenModel> getGameList() {
        List<HomescreenModel> arraylist = new ArrayList<>();

        //arraylist.add(new HomescreenModel(27, AVIATOR, R.drawable.aviator_icon, "slots"));
        if (SharePref.getIsTeenpattiVisible())
            arraylist.add(new HomescreenModel(1, TEENPATTI, R.drawable.home_teenpatti, "Multi"));

        if (SharePref.getIsPrivateVisible())
            arraylist.add(new HomescreenModel(2, PRIVATE_TABLE, R.drawable.home_privatetable, "Multi"));

        if (SharePref.getIsCustomVisible())
            arraylist.add(new HomescreenModel(3, CUSTOM_TABLE, R.drawable.home_customnboot, "Multi"));

        if (SharePref.getIsHomeJackpotVisible())
            arraylist.add(new HomescreenModel(4, JACKPOT_3PATTI, R.drawable.home_jackpot, "All"));

        if (SharePref.getIsPointRummyVisible())
            arraylist.add(new HomescreenModel(5, POINT_RUMMY, R.drawable.home_pointrummy, "Skills"));

        if (SharePref.getIsPrivateRummyVisible())
            arraylist.add(new HomescreenModel(6, PRIVATE_RUMMY, R.drawable.home_pvtrummy, "Skills"));

        if (SharePref.getIsPoolRummyVisible())
            arraylist.add(new HomescreenModel(7, POOL_RUMMY, R.drawable.home_poolrummy, "Skills"));

        if (SharePref.getIsDealRummyVisible())
            arraylist.add(new HomescreenModel(8, DEAL_RUMMY, R.drawable.home_rummydeal, "Skills"));

        if (SharePref.getIsDragonTigerVisible())
            arraylist.add(new HomescreenModel(9, DRAGON_TIGER, R.drawable.home_tgdr, "slots"));

        if (SharePref.getIsAndharBaharVisible())
            arraylist.add(new HomescreenModel(10, ANDHAR_BAHAR, R.drawable.home_andhar_bhar, "slots"));

        if (SharePref.getIsSevenUpVisible())
            arraylist.add(new HomescreenModel(11, SEVEN_UP_DOWN, R.drawable.home_seven, "slots"));

        if (SharePref.getIsCarRouletteVisible())
            arraylist.add(new HomescreenModel(12, CAR_ROULETTE, R.drawable.home_car, "slots"));

        if (SharePref.getIsAnimalRouletteVisible())
            arraylist.add(new HomescreenModel(13, ANIMAL_ROULETTE, R.drawable.home_animal, "slots"));

        if (SharePref.getIsColorPredictionVisible())
            arraylist.add(new HomescreenModel(14, COLOUR_PREDICTION, R.drawable.home_color, "slots"));

        if (SharePref.getIsPokerVisible())
            arraylist.add(new HomescreenModel(15, POKER_GAME, R.drawable.home_poker, "slots"));

        if (SharePref.getIsHeadTailVisible())
            arraylist.add(new HomescreenModel(16, HEAD_TAIL, R.drawable.home_headtales, "slots"));

        if (SharePref.getIsRedBlackVisible())
            arraylist.add(new HomescreenModel(17, RED_BLACK, R.drawable.home_redblack, "slots"));

        if (SharePref.getIsLudoVisible())
            arraylist.add(new HomescreenModel(18, LUDO_GAME_PLAY_LOCAL, R.drawable.home_localludo, "slots"));

        if (SharePref.getIsLudoOnlineVisible())
            arraylist.add(new HomescreenModel(19, LUDO_GAME_ONLINE, R.drawable.home_onlineludo, "multi"));

        if (SharePref.getIsLudoComputerVisible())
            arraylist.add(new HomescreenModel(20, LUDO_GAME_COMPUTER, R.drawable.home_ludocomputer, "Skills"));

        if (SharePref.getBacarateVisible())
            arraylist.add(new HomescreenModel(21, BACCARAT, R.drawable.home_baccarat, "slots"));

        if (SharePref.getJhandi_MundaVisible())
            arraylist.add(new HomescreenModel(22, JHANDHI_MUNDA, R.drawable.home_jundimundi, "slots"));

        if (SharePref.getRouletteVisible())
            arraylist.add(new HomescreenModel(23, ROULETTE, R.drawable.home_rol, "slots"));

        if (SharePref.getTournamentVisible())
            arraylist.add(new HomescreenModel(24, TOURNAMENT, R.drawable.home_tournament, "Multi"));

        if (SharePref.getIsColorPrediction1Visible())
            arraylist.add(new HomescreenModel(25, COLOUR_PREDICTION1, R.drawable.color_pred_1min, "slots"));


        if (SharePref.getIsColorPrediction3Visible())
            arraylist.add(new HomescreenModel(26, COLOUR_PREDICTION3, R.drawable.color_pred_3min, "slots"));

//        if (SharePref.getIsAviatorVisible())
//            arraylist.add(new HomescreenModel(27, AVIATOR, R.drawable.aviator_icon, "slots"));
//        arraylist.add(new HomescreenModel(15,CRICKET_GAME,R.drawable.home_cricket,"Sports"));
//        arraylist.add(new HomescreenModel(16,BEST_OF_5,R.drawable.home_best_of_5,"All"));
        return arraylist;
    }

    private void BonusView() {

        if (SharePref.getInstance().getBoolean(SharePref.isBonusShow))
            lnrmail.setVisibility(View.VISIBLE);
        else
            lnrmail.setVisibility(View.GONE);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String datevalue = prefs.getString("cur_date4", "12/06/2020");


        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate1 = df1.format(c);
        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), datevalue, formattedDate1);


        if (dateDifference > 0) {
            // catalog_outdated = 1;

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("cur_date4", formattedDate);
            editor.apply();

            if (SharePref.getInstance().getBoolean(SharePref.isBonusShow))
                showDailyWinCoins();

        } else {

            System.out.println("");


        }

        lnrmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getApplicationContext(), MaiUserListingActivity.class);
//                startActivity(intent);
//                Funtions.showToast(context, "Coming Soon",
//                        Toast.LENGTH_LONG).show();
                showDailyWinCoins();

            }
        });
    }

    private void showDailyWinCoins() {
        DialogDailyBonus.getInstance(context).returnCallback(new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                UserProfile();
            }
        }).show();
    }

    private void LoadFragment(Fragment fragment) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.home_container, fragment).
                addToBackStack(null).
                commit();
    }

    private void BlinkAnimation(final View view) {
        view.setVisibility(View.VISIBLE);
        final Animation animationUtils = AnimationUtils.loadAnimation(context, R.anim.blink);
        animationUtils.setDuration(1000);
        animationUtils.setRepeatCount(1);
        animationUtils.setStartOffset(700);
        view.startAnimation(animationUtils);

        animationUtils.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void shineAnimation(final View view) {
        final Animation animationUtils = AnimationUtils.loadAnimation(context, R.anim.left_to_righ);
        animationUtils.setDuration(1500);
        view.startAnimation(animationUtils);

        animationUtils.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(animationUtils);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    LinearLayout lnr_2player, lnr_5player, lnr_private;
    TextView tv_2player, tv_5player;
    int selected_type = 5;
    Button btn_join_private;
    public void Dialog_SelectPlayer() {
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_select_palyer);
        lnr_2player = dialog.findViewById(R.id.lnr_2player);
        lnr_5player = dialog.findViewById(R.id.lnr_5player);
        lnr_private = dialog.findViewById(R.id.lnr_private);
        tv_2player = (TextView) dialog.findViewById(R.id.tv_2player);
        tv_5player = (TextView) dialog.findViewById(R.id.tv_5player);
        btn_join_private = (Button) dialog.findViewById(R.id.btn_join_private);
        tv_5player.setText("6 \nPlayer");
//        lnr_private.setVisibility(View.VISIBLE);

        Button btn_player = dialog.findViewById(R.id.btn_play);
        ImageView img_close = dialog.findViewById(R.id.img_close);


        btn_join_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);

                // builder.setTitle("Choose Type Of Material");
                View rowList = getLayoutInflater().inflate(R.layout.join_private, null);
                EditText et_code =rowList.findViewById(R.id.et_code);
                Button btn_join = rowList.findViewById(R.id.btn_join_private);


                builder.setView(rowList);
                AlertDialog dialog = builder.create();
                dialog.show();

                btn_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_code = et_code.getText().toString().trim();
                        if (str_code.equals("")){
                            Toast.makeText(Homepage.this, "Please enter valid code to join!", Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.dismiss();
                            join_privateTable(str_code);
                        }
                    }
                });

            }
        });

        ChangeTextviewColorChange(5);

        lnr_2player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(2);
            }
        });

        lnr_5player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(5);
            }
        });

        btn_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (selected_type == 2) {
                    LoadTableFragments(ActiveTables_BF.RUMMY2);

                } else {
                    LoadTableFragments(ActiveTables_BF.RUMMY5);


                }


            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


    }

    private void join_privateTable(String code){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.join_private,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK_PRIVATE", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                String message = jsonObject.getString("message");
                                Functions.showToast(context, message);
                                Intent i = new Intent(Homepage.this, RummPrivate.class);
                                i.putExtra("player5","player5");
                                i.putExtra("call_status", "1");
//                                i.putExtra(SEL_TABLE,pool_boot);
                                i.putExtra("min_entry","0");
                                i.putExtra("hide_hint","1");
                                startActivity(i);
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

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("code",""+code);
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java_join", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void Dialog_SelectPlayerPool() {
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_select_palyer);
        lnr_2player = dialog.findViewById(R.id.lnr_2player);
        lnr_5player = dialog.findViewById(R.id.lnr_5player);
        tv_2player = (TextView) dialog.findViewById(R.id.tv_2player);
        tv_5player = (TextView) dialog.findViewById(R.id.tv_5player);
        tv_5player.setText("6 \nPlayer");

        Button btn_player = dialog.findViewById(R.id.btn_play);
        ImageView img_close = dialog.findViewById(R.id.img_close);

        ChangeTextviewColorChange(5);

        lnr_2player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(2);
            }
        });

        lnr_5player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(5);
            }
        });

        btn_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (selected_type == 2) {
                    LoadPullRummyActiveTable(ActiveTables_BF.RUMMY2);

                } else {
                    LoadPullRummyActiveTable(ActiveTables_BF.RUMMY5);
                }


            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


    private void LoadTableFragments(String TAG) {
        ActiveTables_BF activeTables_bf = new ActiveTables_BF(TAG);
        activeTables_bf.show(getSupportFragmentManager(), activeTables_bf.getTag());
    }

    private void LoadPointRummyActiveTable(String TAG) {
        RummyActiveTables_BF rummyActiveTables_bf = new RummyActiveTables_BF(TAG);
        rummyActiveTables_bf.show(getSupportFragmentManager(), rummyActiveTables_bf.getTag());
    }

    private void ChangeTextviewColorChange(int colortype) {

        selected_type = colortype;

        if (colortype == 2) {
            tv_2player.setTextColor(getResources().getColor(R.color.white));
            lnr_2player.setBackgroundColor(getResources().getColor(R.color.new_colorPrimary));

            tv_5player.setTextColor(getResources().getColor(R.color.black));
            lnr_5player.setBackgroundColor(getResources().getColor(R.color.white));

        } else {
            tv_2player.setTextColor(getResources().getColor(R.color.black));
            lnr_2player.setBackgroundColor(getResources().getColor(R.color.white));

            tv_5player.setTextColor(getResources().getColor(R.color.white));
            lnr_5player.setBackgroundColor(getResources().getColor(R.color.new_colorPrimary));

        }


    }


    private void rotation_animation(View view, int animation) {

        Animation circle = Functions.AnimationListner(context, animation, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

            }
        });
        view.setAnimation(circle);
        circle.startNow();

    }

    int Counts = 100;
    int postion = -100;

    private void CenterAnimationView(String imagename, View imgcards, int Home_Page_Animation) {


        int AnimationSpeed = Counts + Home_Page_Animation;
        Counts += 300;

        final View fromView, toView;
        rootView.setVisibility(View.VISIBLE);
//        rootView.removeAllViews();
//        View ivpickcard = findViewById(R.id.view_center);

        fromView = rootView;
        toView = imgcards;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        final ImageView sticker = new ImageView(context);

        int stickerId = Functions.GetResourcePath(imagename, context);

        int card_width = (int) getResources().getDimension(R.dimen.home_card_width);
        int card_hieght = (int) getResources().getDimension(R.dimen.home_card_height);

        Picasso.get().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_width, card_hieght));
        rootView.addView(sticker);


        Animations anim = new Animations();
        Animation a = anim.fromAtoB(0, 0, convertDpToPixel(postion, context), 0, null, AnimationSpeed, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                fromView.setVisibility(View.VISIBLE);
                toView.setVisibility(View.VISIBLE);
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();

        postion += 100;

    }

    @Override
    protected void onResume() {
        super.onResume();
        str_colr_pred = "";
//        EnableGPS();
        UserProfile();
        GameLeave();
        UserLudoLeaveTable();
    }

    private void UserLudoLeaveTable() {
        LudoApiRepository.getInstance().call_api_leaveTable();
    }



    public void clickTask() {

        imgPrivategame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialoag();
                openPrivateTeenpatti(null);

            }
        });

        lnrsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogSettingOption dialogSettingOption = new DialogSettingOption(context) {
                    public void playstopSound() {

                        playSound(R.raw.game_soundtrack, true);

                    }
                };

                dialogSettingOption.showDialogSetting();
            }
        });



        imgLogout.setOnClickListener(new View.OnClickListener() {       //contact us
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(Homepage.this);
//                builder.setTitle("Location")
                builder.setMessage("Please connect us on +91 9999999999")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                android.app.AlertDialog alert = builder.create();
                alert.setTitle("Contact Us");
//                alert.show();

                Functions.Dialog_CancelAppointmentNew(context, "Contact Us", "", new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {
                        if (resp.equals("yes")) {
                            stopPlaying();
                            finish();
                        }
                    }
                });

            }
        });

        lnrinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogReferandEarn.getInstance(context).show();
            }
        });
        findViewById(R.id.ivIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogReferandEarn.getInstance(context).show();
            }
        });
    }

    public void openBuyChipsActivity(View view) {
        Intent intent = new Intent(context, BuyChipsList.class);
        intent.putExtra("homepage", "homepage");
        startActivity(intent);
    }


    private void UserProfile() {
        CommonAPI.CALL_API_UserDetails(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if (resp != null) {
                    ParseResponse(resp);
                }
            }
        },token);

    }

    private void ParseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            if (code.equalsIgnoreCase("200")) {
                JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
                user_id = jsonObject0.getString("id");
                name = jsonObject0.optString("name");
                mobile = jsonObject0.optString("mobile");
                profile_pic = jsonObject0.optString("profile_pic");
                profile_img = jsonObject0.optString("profile_pic");
                referral_code = jsonObject0.optString("referral_code");
                wallet = jsonObject0.optString("wallet");
                Log.d("wallet_home", wallet);
                String winning_wallet = jsonObject0.optString("winning_wallet");
                String spin_remaining = jsonObject0.getString("spin_remaining");
                Log.d("extra_spinner_", spin_remaining);
                SharePref.getInstance().setSpin_remaining(spin_remaining);
                game_played = jsonObject0.optString("game_played");
                bank_detail = jsonObject0.optString("bank_detail");
                upi = jsonObject0.optString("upi");
                adhar_card = jsonObject0.optString("adhar_card");

                /*------------------------- Banner Source code ------------------------- */
                bannerModelArrayList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("app_banner");

                Log.d("jsonArray","jsonArray_"+jsonArray);

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    BannerModel bannerModel = new BannerModel();
                    bannerModel.setId(jsonObject1.getString("id"));
                    bannerModel.setImg(jsonObject1.getString("banner"));
                    bannerModelArrayList.add(bannerModel);
                }

//                SlidingImageAdapter adapter = new SlidingImageAdapter();
//                viewPager.setFocusableInTouchMode(true);
//                viewPager.setAdapter(adapter);
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        viewpager.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                viewPager.setCurrentItem((viewPager.getCurrentItem()) % bannerModelArrayList.size());
//                            }
//                        });
//                    }
//                };
//                timer = new Timer();
//                timer.schedule(timerTask, 3000, 3000);
//                headerViewPager.setAdapter(adapter);
                CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.titles);
//                indicator.setViewPager(headerViewPager);
//                headerViewPager.startAutoScroll();
//                headerViewPager.setInterval(3000);
//                headerViewPager.setScrollDurationFactor(5);

                //------banner ends here -----

                // txtName.setText("Welcome Back "+name);

                if (Functions.isStringValid(wallet)) {
                    float f_wallet = Float.parseFloat(wallet);
                    Log.d("float_home", ""+f_wallet);
//                    long numberamount =  Float.parseFloat(f_wallet);
//                    long numberamount = (long) f_wallet;
                    txtwallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(f_wallet));

//                    if (wallet.length() >=4) {
//                        f_wallet = f_wallet/1000;
//                        txtwallet.setText(String.format("%.1f", f_wallet)+"k");
//                    }
//                        else{
//                        txtwallet.setText("" + f_wallet);
//                        }

                }

                ((TextView)findViewById(R.id.txt_user_id)).setText("ID:"+user_id);
                txtproname.setText(name);
                Picasso.get().load(Const.IMGAE_PATH + profile_pic).into(imaprofile);

                String setting = jsonObject.optString("setting");
                JSONObject jsonObjectSetting = new JSONObject(setting);
                JSONArray avatar = jsonObject.getJSONArray("avatar");

                game_for_private = jsonObjectSetting.optString("game_for_private");
                app_version = jsonObjectSetting.optString("app_version");
                String referral_amount = jsonObjectSetting.optString("referral_amount");
                String referral_link = jsonObjectSetting.optString("referral_link");
                String joining_amount = jsonObjectSetting.optString("joining_amount");
                String whats_no = jsonObjectSetting.optString("whats_no");
                String mBan_states = jsonObjectSetting.optString("ban_states");
                String extra_spinner = jsonObjectSetting.optString("extra_spinner");

                int admin_commission = jsonObjectSetting.optInt("admin_commission", 2);

                ban_states = Arrays.asList(mBan_states.split(","));
//                checkForBanState();


                BonusView();

                ((ImageView) findViewById(R.id.imgicon)).setImageDrawable(
                        Variables.CURRENCY_SYMBOL.equalsIgnoreCase(Variables.RUPEES) ? Functions.getDrawable(context, R.drawable.ic_currency_rupee) :
                                Variables.CURRENCY_SYMBOL.equalsIgnoreCase(Variables.DOLLAR) ? Functions.getDrawable(context, R.drawable.ic_currency_dollar) :
                                        Functions.getDrawable(context, R.drawable.ic_currency_chip));

                tvUserCategory.setText(SharePref.getInstance().getUSER_CATEGORY());

            } else if (code.equals("411")) {

                Intent intent = new Intent(context, LoginScreen.class);
                startActivity(intent);
                finishAffinity();
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", "");
                editor.putString("referal_code", "");
                editor.putString("img_name", "");
                editor.putString("game_for_private", "");
                editor.putString("app_version", "");
                editor.apply();

                Functions.showToast(context, "You are Logged in from another " +
                        "device.");


            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Functions.showToast(context, message);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        findViewById(R.id.iv_add).clearAnimation();
        Functions.dismissLoader();

    }

    @OnClick(R.id.tvUserCategory)
    void openUserRanking(){
        DialogUserRanking.getInstance(context).setCallback(new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
            }
        }).show();
    }

    public void PlaySaund(int sound) {

        if (!SharePref.getInstance().isSoundEnable())
            return;

        final MediaPlayer mp = MediaPlayer.create(context,
                sound);
        mp.start();
    }

    public void showDialoagPrivettable() {
        // custom dialog
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.custom_dialog_custon_boot);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sBarpri = (SeekBar) dialog.findViewById(R.id.seekBar1);
        sBarpri.setProgress(0);
        sBarpri.incrementProgressBy(10);
        sBarpri.setMax(100);
        txtStartpri = (TextView) dialog.findViewById(R.id.txtStart);
        txtLimitpri = (TextView) dialog.findViewById(R.id.txtLimit);
        txtwalletchipspri = (TextView) dialog.findViewById(R.id.txtwalletchips);
        float numberamount = Float.parseFloat(wallet);
        txtwalletchipspri.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
        txtheader = (TextView) dialog.findViewById(R.id.txtheader);
        txtheader.setText("Private Table");
        // txtwalletchipspri.setText(wallet);
        txtBootamountpri = (TextView) dialog.findViewById(R.id.txtBootamount);
        txtPotLimitpri = (TextView) dialog.findViewById(R.id.txtPotLimit);
        txtNumberofBlindpri = (TextView) dialog.findViewById(R.id.txtNumberofBlind);
        txtMaximumbetvaluepri = (TextView) dialog.findViewById(R.id.txtMaximumbetvalue);
        imgclosetoppri = (ImageView) dialog.findViewById(R.id.imgclosetop);
        imgclosetoppri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgCreatetable = (ImageView) dialog.findViewById(R.id.imgCreatetable);

        imgCreatetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, TeenPattiSocket.class);
                Intent intent = new Intent(context, PublicTable_New.class);
                intent.putExtra("gametype", PublicTable_New.PRIVATE_TABLE);
                intent.putExtra("bootvalue", pvalpri + "");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        // tView.setText(sBar.getProgress() + "/" + sBar.getMax());
        sBarpri.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                if (progress == 1) {

                    pvalpri = 50;

                } else if (progress == 2) {
                    pvalpri = 100;
                } else if (progress == 3) {

                    pvalpri = 500;
                } else if (progress == 4) {

                    pvalpri = 1000;

                } else if (progress == 5) {

                    pvalpri = 5000;

                } else if (progress == 6) {

                    pvalpri = 10000;
                } else if (progress == 7) {

                    pvalpri = 25000;
                } else if (progress == 8) {

                    pvalpri = 50000;
                } else if (progress == 9) {

                    pvalpri = 100000;
                } else if (progress == 10) {

                    pvalpri = 250000;
                }

                //progress = progress * 10;
                // pvalpri = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtBootamountpri.setText("Boot amount : " + kvalue(pvalpri) + "");

                int valueforpot = pvalpri * 1024;
                int valueformaxi = pvalpri * 128;

                //long valueforpotlong= valueforpot;

                txtPotLimitpri.setText("Pot limit : " + kvalue(valueforpot) + "");
                txtMaximumbetvaluepri.setText("Maximumbet balue : " + kvalue(valueformaxi) + "");
                txtNumberofBlindpri.setText("Number of Blinds : 4");
                //tView.setText(pval + "/" + seekBar.getMax());
            }
        });


        dialog.show();
        Functions.setDialogParams(dialog);
    }

    TextView txtheader;
    public void showDialoagonBack() {
        // custom dialog
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.custom_dialog_custon_boot);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sBar = (SeekBar) dialog.findViewById(R.id.seekBar1);
        sBar.setProgress(0);
        sBar.incrementProgressBy(10);
        sBar.setMax(100);
        txtStart = (TextView) dialog.findViewById(R.id.txtStart);
        txtLimit = (TextView) dialog.findViewById(R.id.txtLimit);
        txtwalletchips = (TextView) dialog.findViewById(R.id.txtwalletchips);
        float numberamount = Float.parseFloat(wallet);
        txtwalletchips.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
        txtBootamount = (TextView) dialog.findViewById(R.id.txtBootamount);
        txtPotLimit = (TextView) dialog.findViewById(R.id.txtPotLimit);
        txtNumberofBlind = (TextView) dialog.findViewById(R.id.txtNumberofBlind);
        txtMaximumbetvalue = (TextView) dialog.findViewById(R.id.txtMaximumbetvalue);
        txtheader = (TextView) dialog.findViewById(R.id.txtheader);
        txtheader.setText("Custom Boot");
        imgclosetop = (ImageView) dialog.findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgCreatetablecustom = (ImageView) dialog.findViewById(R.id.imgCreatetable);
        imgCreatetablecustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, TeenPattiSocket.class);
                Intent intent = new Intent(context, PublicTable_New.class);
                intent.putExtra("gametype", PublicTable_New.CUSTOME_TABLE);
                intent.putExtra("bootvalue", pval + "");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        // tView.setText(sBar.getProgress() + "/" + sBar.getMax());
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                progress = progress / 10;
                if (progress == 1) {

                    pval = 50;

                } else if (progress == 2) {
                    pval = 100;
                } else if (progress == 3) {

                    pval = 500;
                } else if (progress == 4) {

                    pval = 1000;

                } else if (progress == 5) {

                    pval = 5000;

                } else if (progress == 6) {

                    pval = 10000;
                } else if (progress == 7) {

                    pval = 25000;
                } else if (progress == 8) {

                    pval = 50000;
                } else if (progress == 9) {

                    pval = 100000;
                } else if (progress == 10) {

                    pval = 250000;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtBootamount.setText("Boot amount : " + kvalue(pval) + "");

                int valueforpot = pval * 1024;
                int valueformaxi = pval * 128;
                txtPotLimit.setText("Pot limit : " + kvalue(valueforpot) + "");
                txtMaximumbetvalue.setText("Maximumbet balue : " + kvalue(valueformaxi) + "");
                txtNumberofBlind.setText("Number of Blinds : 4");
                //tView.setText(pval + "/" + seekBar.getMax());
            }
        });


        dialog.show();
        Functions.setDialogParams(dialog);
    }

    private void GameLeave() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_TABLE_LEAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        System.out.println("" + response);
                        // finish();

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

        Volley.newRequestQueue(context).add(stringRequest);

    }

    public void openLuckJackpotActivity(View view) {
//        startActivity(new Intent(context, LuckJackPot_A_Socket.class));
        startActivity(new Intent(context, LuckJackPot_A.class));
    }

    public void openSevenup(View view) {

        startActivity(new Intent(context, SevenUp_A.class));
        //startActivity(new Intent(context, SevenUp_Socket.class));
    }

    public void openPublicTeenpatti(View view) {
        PlaySaund(R.raw.buttontouchsound);
        LoadTableFragments(ActiveTables_BF.TEENPATTI);
    }

    public void openPrivateTeenpatti(View view) {
        PlaySaund(R.raw.buttontouchsound);
        float gamecount = 0;
        if (game_played != null && !game_played.equals("")) {
            gamecount = Float.parseFloat(game_played);
        }
        float game_for_privatetemp = Float.parseFloat(game_for_private);
        if (gamecount > game_for_privatetemp) {

            showDialoagPrivettable();

        } else {

            Functions.showToast(context, "To Unblock Private Table you have to Play at least " + game_for_privatetemp +
                    " Games.");

        }
    }

    public void openCustomeTeenpatti(View view) {
        PlaySaund(R.raw.buttontouchsound);
        showDialoagonBack();
    }

    public void openRummyGame(View view) {
        Dialog_SelectPlayer();
//        LoadPointRummyActiveTable(ActiveTables_BF.RUMMY5);
    }

    public void openRummyPullGame(View view) {
        Dialog_SelectPlayerPool();
    }

    public void openPokerGame(View view) {
        LoadPokerGameActiveTable(ActiveTables_BF.RUMMY5);
    }


    public void openRummyDealGame(View view) {
//        LoadDealRummyActiveTable(ActiveTables_BF.RUMMY2);
        DialogDealType dialogDealType = new DialogDealType(this) {
            @Override
            protected void startGame(Bundle bundle) {
                openRummyGames(bundle);
            }
        };
        dialogDealType.show();
    }

    private void openRummyGames(Bundle bundle) {
        Intent intent = new Intent(context, RummyDealGame.class);
        if (bundle != null)
            intent.putExtras(bundle);

        if (context != null && !context.isFinishing())
            context.startActivity(intent);
    }


    public void openAndharbahar(View view) {
     startActivity(new Intent(context, Andhar_Bahar_NewUI.class).putExtra("room_id", "1"));
      //  startActivity(new Intent(context, Andhar_Bahar_Socket.class).putExtra("room_id", "1"));
    }

    public void openRoulette(View view) {
       startActivity(new Intent(context, RouleteGame_A.class).putExtra("room_id", "1"));
        //startActivity(new Intent(context, RouleteGame_Socket.class).putExtra("room_id", "1"));
    }

    public void openTournament() {
        Intent intent = new Intent(context, TourList.class);
        context.startActivity(intent);
    }
   public void openAviator(){
       Intent intent = new Intent(context, Aviator_Game_Activity.class);
       context.startActivity(intent);


   }

    public void openColorPred(View view) {
   Intent intent = new Intent(context, ColorPrediction.class);
        //Intent intent = new Intent(context, ColorPrediction_Socket.class);
        context.startActivity(intent);
    }

    public void openColorPred1(View view) {
    //    Intent intent = new Intent(context, ColorPrediction1.class);
        Intent intent = new Intent(context, ColorPrediction1_Socket.class);
        context.startActivity(intent);
    }

    public void openColorPred3(View view) {
    //    Intent intent = new Intent(context, ColorPrediction3.class);
        Intent intent = new Intent(context, ColorPrediction3_Socket.class);
        context.startActivity(intent);
    }


    public void openDragonTiger(View view) {
        startActivity(new Intent(context, DragonTiger_A.class));
      //  startActivity(new Intent(context, DragonTigerSocket.class));
    }

    public void openCarRoulette(View view) {
//        startActivity(new Intent(context, CarRoullete_Socket.class));
        startActivity(new Intent(context, CarRoullete_A.class));


    }

    public void openAnimalRoullete(View view) {
        startActivity(new Intent(context, AnimalRoulette_A.class));
       // startActivity(new Intent(context, AnimalRoulette_Socket.class));
    }

    public interface itemClick {
        void OnDailyClick(String id);
    }

    TextView txtnotfound;

    private void LoadPullRummyActiveTable(String TAG) {
        RummyActiveTables_BF rummyActiveTables_bf = new RummyActiveTables_BF(TAG);
        rummyActiveTables_bf.show(getSupportFragmentManager(), rummyActiveTables_bf.getTag());
    }

    private void LoadPokerGameActiveTable(String TAG) {
        PokerActiveTables_BF pokerActiveTables_bf = new PokerActiveTables_BF(TAG);
        pokerActiveTables_bf.show(getSupportFragmentManager(), pokerActiveTables_bf.getTag());
    }

    private void LoadDealRummyActiveTable(String TAG) {
        RummyDealActiveTables_BF rummyDealActiveTables_bf = new RummyDealActiveTables_BF(TAG);
        rummyDealActiveTables_bf.show(getSupportFragmentManager(), rummyDealActiveTables_bf.getTag());
    }


    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public String kvalue(int number) {

        String numberString = "";
        if (Math.abs(number / 1000000) > 1) {
            numberString = (number / 1000000) + "m";

        } else if (Math.abs(number / 1000) > 1) {
            numberString = (number / 1000) + "k";

        } else {
            numberString = number + "";

        }
        return numberString;
    }

    @Override
    protected void onStart() {
        super.onStart();

        playSound(R.raw.game_soundtrack, true);
    }

    @Override
    protected void onDestroy() {
        stopPlaying();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlaying();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlaying();
    }

    Sound soundMedia;

    public void playSound(int sound, boolean isloop) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");

        if (value.equals("1")) {
            soundMedia.PlaySong(sound, isloop, context);
        } else {
            stopPlaying();
        }


    }

    private void stopPlaying() {
        soundMedia.StopSong();
    }

    private void checkForBanState() {
        String user_state = "";
        if (Variables.latitude > 0 && Variables.longitude > 0) {
            Address address = getAddressFromLatLong(Variables.latitude, Variables.longitude, context);
            if (address != null)
                user_state = address.getAdminArea();
        }

        for (String state : ban_states) {
            if (state.trim().equalsIgnoreCase(user_state)) {
                DialogRestrictUser.getInstance(context).show();
                break;
            }
        }
    }

    public static Address getAddressFromLatLong(double lat, double long_temp, Context context) {

        Address address = null;

        if (lat <= 0 && long_temp <= 0)
            return address;

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, long_temp, 1);
            address = addresses.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            // new GetClass().execute();
        }

        return address;
    }

    private boolean beforeClickPermissionRat;
    private boolean afterClickPermissionRat;

    public void requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            beforeClickPermissionRat = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
            requestPermissions(Functions.LOCATION_PERMISSIONS, Variables.REQUESTCODE_LOCATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ENABLE_LOCATION_CODE: {
                if (resultCode == RESULT_OK) {

                    storeLatlong();

                } else {
                    finishAffinity();
                }
            }
            break;
        }
    }

    // before after
// FALSE  FALSE  =  Was denied permanently, still denied permanently --> App Settings
// FALSE  TRUE   =  First time deny, not denied permanently yet --> Nothing
// TRUE   FALSE  =  Just been permanently denied --> Changing my caption to "Go to app settings to edit permissions"
// TRUE   TRUE   =  Wasn't denied permanently, still not denied permanently --> Nothing
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Variables.REQUESTCODE_LOCATION:

                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // user rejected the permission

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            afterClickPermissionRat = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                        if ((!afterClickPermissionRat && !beforeClickPermissionRat)) {
                            // user also CHECKED "never ask again"
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting

//                            showUserClearAppDataDialog();

//                            openMapActivity();
                            finishAffinity();
                            break;
                        } else if ((afterClickPermissionRat && !beforeClickPermissionRat)) {

//                            if(!Functions.isAndroid11())
//                            {
//                                openMapActivity();
//                                break;
//                            }

                        } else {
                            showRationale(permission, R.string.permission_denied_contacts);
                            // user did NOT check "never ask again"
                            // context is a good place to explain the user
                            // why you need the permission and ask if he wants
                            // to accept it (the rationale)
                        }
                    }
                }

                try {

                    if (isPermissionGranted(grantResults)) {
                        enable_permission();
                    } else {

                        if ((afterClickPermissionRat && !beforeClickPermissionRat)
                                || (afterClickPermissionRat && beforeClickPermissionRat)) {
                            EnableGPS();
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    public void EnableGPS() {

        if (Functions.check_location_permissions(context)) {
            if (!GpsUtils.isGPSENABLE(context)) {
                requestLocationAccess();
//                showFragment();
            } else {
                enable_permission();
            }
        } else {
//            showFragment();
            requestLocationAccess();
        }
    }

    public void requestLocationAccess() {

        if (Functions.check_location_permissions(context)) {
            enable_permission();
        } else {
            requestLocationPermissions();
        }
    }


    private void showRationale(String permission, int permission_denied_contacts) {
    }


    private boolean isPermissionGranted(int[] grantResults) {
        boolean isGranted = true;

        for (int result : grantResults) {

            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }

        }

        return isGranted;
    }


    private void storeLatlong() {
        LatLng latLng = getLatLong();
        Variables.latitude = latLng.latitude;
        Variables.longitude = latLng.longitude;

        checkForBanState();
    }

    public LatLng getLatLong() {
        if (getLocationlatlong != null)
            return getLocationlatlong.getLatlong();
        else {
            getLocationlatlong = new GetLocationlatlong(context);
        }

        return getLocationlatlong.getLatlong();
    }

    private void enable_permission() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GpsStatus) {

            new GpsUtils(context).turnGPSOn(isGPSEnable -> {

                if (isGPSEnable)
                    enable_permission();

            });
        } else if (Functions.check_location_permissions(context)) {
//            dismissFragment();
            storeLatlong();
        }
    }

    GetLocationlatlong getLocationlatlong;

    private void initilizeLocation() {
        getLocationlatlong = new GetLocationlatlong(context);
    }

    private class SlidingImageAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;

        public SlidingImageAdapter() {
             layoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.slider_images, view, false);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            Glide.with(getApplicationContext()).load(Const.banner_img + bannerModelArrayList.get(position).getImg()).into(imageView);
            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public int getCount() {
            return bannerModelArrayList.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }
    }
}



