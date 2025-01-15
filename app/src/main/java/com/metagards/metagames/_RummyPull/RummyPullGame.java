package com.metagards.metagames._RummyPull;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.metagards.metagames.BaseActivity;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.CommonFunctions;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Animations;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.MyDragShadowBuilder;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Sound;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._Rummy.DeclareRules;
import com.metagards.metagames._Rummy.GameLocalLogic;
import com.metagards.metagames._Rummy.menu.DialogRulesRummy;
import com.metagards.metagames._RummyDeal.DialogDeclareCards;
import com.metagards.metagames._RummyPull.Adapter.ShareWalletAdapter;
import com.metagards.metagames._RummyPull.Fragments.Bottom_listDialog;
import com.metagards.metagames._RummyPull.Fragments.GameUsersPoint_bt;
import com.metagards.metagames._TeenPatti.Bottom_GameChating_F;
import com.metagards.metagames.model.CardModel;
import com.metagards.metagames.model.TableModel;
import com.metagards.metagames.model.Usermodel;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.view.View.DRAG_FLAG_OPAQUE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;
import static com.metagards.metagames.Fragments.ActiveTables_BF.SEL_TABLE;
import static com.metagards.metagames.Utils.Functions.ANIMATION_SPEED;
import static com.metagards.metagames.Utils.Functions.SetBackgroundImageAsDisplaySize;
import static com.metagards.metagames.Utils.Functions.convertDpToPixel;
import static com.metagards.metagames.Utils.Variables.RUMMYGAMEPOOL;

public class RummyPullGame extends BaseActivity implements Animation.AnimationListener {

    private final int RUMMY_TOTALE_CARD = 13;
    private String TAG = "Rummy5Player";

    String INVALID = "Invalid";

    String IMPURE_SEQUENCE = "Impure sequence";
    String PURE_SEQUENCE = "Pure Sequence";
    String SET = "set";

    String DECLARE_BACK = "";
    String JOKER_CARD = "JK";
    String MAIN_JOKER_CARD = "JKR1";
    String MAIN_JOKER_CARD_2 = "JKR2";
    String DUMMY_CARD = "backside_card";

    int IMPURE_SEQUENCE_VALUE = 4;
    int PURE_SEQUENCE_VALUE = 5;
    int SET_VALUE = 6;
    String shareMessage = "", str_1st_life = "", str_2nd_life="", string_middle_card="", str_chk_joker="";

    LinearLayout rlt_addcardview;

    FrameLayout fl13;
    Animation animFadein,animMove;
    int height,width;
    ArrayList<CardModel> cardModelArrayList = new ArrayList<>();
    ArrayList<Usermodel> userModelArrayList = new ArrayList<>();
    //    ArrayList<CardModel> cardImageList = new ArrayList<>();

    Animation animMoveCardsPlayerwinner1, animMoveCardsPlayerwinner2
            ,animMoveCardsPlayerwinner3,animMoveCardsPlayerwinner4,animMoveCardsPlayerwinner5;

    RelativeLayout rltwinnersymble1,rltwinnersymble2,rltwinnersymble3,rltwinnersymble4,rltwinnersymble5
            ,rltwinnersymble6,rltwinnersymble7;

    ImageView imgpl1glow,imgpl2glow,imgpl3glow,imgpl4glow,imgpl5glow,imgpl6glow,imgpl7glow;

    ImageView imgpl1circle,imgpl2circle,imgpl3circle,imgpl4circle,imgpl5circle,imgpl6circle,imgpl7circle;

    TextView txtPlay1wallet,txtPlay2wallet,txtPlay3wallet,txtPlay4wallet,txtPlay5wallet,txtPlay6wallet,txtPlay7wallet;

    View /*lnrPlay1wallet,*/lnrPlay2wallet,lnrPlay3wallet,lnrPlay4wallet,lnrPlay5wallet,lnrPlay6wallet,lnrPlay7wallet;

    ImageView ivDrawCards;

    TextView txtPlay2,txtPlay3,txtPlay4,txtPlay5,txtPlay6,txtPlay7;

    FrameLayout flUserHighlight;

    int pStatus = 100;
    int pStatusprogress = 0;

    boolean isProgressrun1 = true;
    boolean isProgressrun2 = true;
    boolean isProgressrun3 = true;
    boolean isProgressrun4 = true;
    boolean isProgressrun5 = true;
    boolean isProgressrun6 = true;
    boolean isProgressrun7 = true;

    ProgressBar mProgress1, mProgress2, mProgress3, mProgress4, mProgress5,mProgress6,mProgress7;
    CountDownTimer counttimerforstartgame,declareCountDown;

    int SECOND_30 = 30 * 1000;
    int SECOND_60 = 60 * 1000;

    boolean isDeclareShow = false;
    CountDownTimer mCountDownTimer1,mCountDownTimer2,mCountDownTimer3,mCountDownTimer4,mCountDownTimer5
            ,mCountDownTimer6,mCountDownTimer7;


    Activity context ;
    String user_id_player1 = "";
    String user_id_player2 = "";
    String user_id_player3 = "";
    String user_id_player4 = "";
    String user_id_player5 = "";
    String user_id_player6 = "";
    String user_id_player7 = "";


    ArrayList<CardModel> rs_cardlist_group1 ;
    ArrayList<CardModel> rp_cardlist_group2 ;
    ArrayList<CardModel> bl_cardlist_group3 ;
    ArrayList<CardModel> bp_cardlist_group4 ;
    ArrayList<CardModel> joker_cardlist_group5 ;

    ArrayList<CardModel> ext_group1 ;
    ArrayList<CardModel> ext_group2 ;
    ArrayList<CardModel> ext_group3 ;
    ArrayList<CardModel> ext_group4 ;
    ArrayList<CardModel> ext_group5 ;

    ArrayList<CardModel> selectedcardvalue ;
    ArrayList<ArrayList<CardModel>> grouplist ;
    ImageView /*ivallcard,*/ivpickcard,iv_jokercard,ivFinishDesk;


    RelativeLayout ivallcard;
    float centreX , centreY;

    int timmersectlarge = 60000;
    int timmersectsmall = 1000;

    Button bt_creategroup,bt_sliptcard,bt_discard,bt_startgame,bt_drop,bt_declare;
    boolean isSplit = false;
    String selectedpatti = "";
    String selectedpatti_id = "";

    String joker_card = "" ;
    SharedPreferences cardPref;
    String Pref_cards = "cards_";

    ImageView ivDropCreate;

    View bt_sharewallet;

    TableModel tableModel;

    public static final String RUMMY_TABLE = "rummy_table";


    ToolTipRelativeLayout toolTipRelativeLayout;
    boolean isPracticeGame = false;
    private boolean isDeclareDialogShow = false;

    int [] UserstatusIDS = {R.id.tvUserStatus1,R.id.tvUserStatus2,R.id.tvUserStatus3
            ,R.id.tvUserStatus4,R.id.tvUserStatus5,R.id.tvUserStatus6,R.id.tvUserStatus7};

    final public String REQUEST_SEND = "Send";
    final public String REQUEST_PENDING = "Pending";
    final public String REQUEST_REJECT = "Rejected";
    final public String REQUEST_ACCEPTED = "Accepted";

    BroadcastReceiver updatechatBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String sender_id = intent.getStringExtra("sender_id");
            String message = intent.getStringExtra("message");

            showchatMessage = message;
            if(!sender_id.equals(SharePref.getU_id()))
                showChatPopUP(sender_id);

        }
    };

    ApiViewModel apiViewModel;
//        ActivityMainBind binding;
//        ActivityMainBinding binding;

    ImageView imgCardsandar;
    boolean isPlayer2 = false;
    String pool_game="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rummydeal);

        context = this;
        apiViewModel = ApiViewModel.getInstance().init(context);

        if(getIntent().hasExtra("player2"))
            isPlayer2 = true;

        if(getIntent().hasExtra("pool_game")){
            pool_game = getIntent().getStringExtra("pool_game");
//            Toast.makeText(context, ""+pool_game, Toast.LENGTH_SHORT).show();
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        DECLARE_BACK = getString(R.string.declare_back);

        Intent bundle = getIntent();

        if(soundMedia== null)
            soundMedia = new Sound();

        if(bundle.hasExtra("tableModel"))
            tableModel= (TableModel) bundle.getSerializableExtra("tableModel");

        if(tableModel != null && tableModel.getBootValue().equals("0"))
        {
            isPracticeGame = true;
        }

        if(getIntent().hasExtra("min_entry") && Functions.checkisStringValid(getIntent().getStringExtra("min_entry")))
        {
            if(getIntent().getStringExtra("min_entry").equals("0"))
                isPracticeGame = true;

        }

        buttonIntialization();

        startGameButtonVisible();
        Initialization();


        InitCoutDown();


        UserProgressCount();

        findViewById(R.id.rl_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String referal_code = prefs.getString("referal_code", "");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, referal_code);
                shareMessage = Functions.inviteTableLink(context,table_id,RUMMYGAMEPOOL);

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });

        findViewById(R.id.iv_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottom_listDialog != null)
                    bottom_listDialog.show(getSupportFragmentManager(), bottom_listDialog.getTag());

            }
        });

        if(getIntent().hasExtra("hide_hint")){
//            Toast.makeText(RummPoint.this, "hide", Toast.LENGTH_SHORT).show();
//            findViewById(R.id.lnr_first_second).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.tvPoints).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameUsersPoint_bt != null)
                {
                    if(pointUserlist != null && pointUserlist.size() > 0)
                    {
                        if(!gameUsersPoint_bt.isAdded())
                            gameUsersPoint_bt.show(getSupportFragmentManager(), gameUsersPoint_bt.getTag());
                    }
                    else {
                        Toast.makeText(context, "Please wait we are getting records.", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        findViewById(R.id.ivChatUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String img_name = prefs.getString("img_name", "");

                final Bottom_GameChating_F bottom_gameChating_f = Bottom_GameChating_F.newInstence(game_id,img_name);
//                bottom_gameChating_f.setCallBack(new Callback() {
//                    @Override
//                    public void Responce(String resp, String type, Bundle bundle) {
//                        API_CALL_status();
//                    }
//                });

                if(bottom_gameChating_f != null)
                {

                    if(!bottom_gameChating_f.isAdded())
                        bottom_gameChating_f.show(getSupportFragmentManager(),bottom_gameChating_f.getTag());

                }

            }
        });

        findViewById(R.id.ivShareUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName();
                String applink = "https://play.google.com/store/apps/details?id=" + appPackageName;

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String referal_code = prefs.getString("referal_code", "");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, referal_code);
                String shareMessage = Functions.inviteTableLink(context,table_id,RUMMY_TABLE);

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));

            }
        });

        Log.d("pool_table_", "true");
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            table_id = uri.getQueryParameter("table_id");
            System.out.println("scs");
            getGame(Const.Rummypooljoin_table);
        } else {
            Log.d("some_error_", "2");
        }
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
    private void handleResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            if (code.equalsIgnoreCase("200")) {

                if (jsonObject.has("table_data")) {
                    JSONObject jsonObject0 = jsonObject.getJSONArray("table_data").getJSONObject(0);
                    table_id = jsonObject0.getString("table_id");

//                    txtWaitforOther.setAnimation(animAmpireblink);    //need later
//                    txtWaitforOther.setVisibility(View.GONE);

                } else {

                    //if (jsonObject.has("message")) {
                    //  String message = jsonObject.getString("message");
//                        Functions.showToast(context, message,
//                                Toast.LENGTH_LONG).show();
                    //  }

                }

            } else if (code.equalsIgnoreCase("406")) {
                Functions.showToast(context, message);
                API_CALL_leave_table("0");

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

    public void openGameRules(View view) {
        DialogRulesRummy.getInstance(context).show();
    }

    private void buttonIntialization(){
        bt_drop=findViewById(R.id.bt_drop);
        bt_drop.setVisibility(View.GONE);
        bt_startgame=findViewById(R.id.bt_startgame);
        bt_discard=findViewById(R.id.iv_discard);
        bt_sliptcard=findViewById(R.id.iv_sliptcard);
        ivallcard=findViewById(R.id.ivallcard);
    }

    RelativeLayout animationLayout;
    private void Initialization() {


        imgCardsandar = findViewById(R.id.imgCardsandar);

        startGameButtonGone();

        registerReceiver(updatechatBroadcast, new IntentFilter("CHAT_UPDATE"));


        ImageView imgTable = findViewById(R.id.imgTable);
        RelativeLayout rltParentLayout = findViewById(R.id.rltParentLayout);

        SetBackgroundImageAsDisplaySize(this,rltParentLayout,R.drawable.home_bg);
        LoadImage().load(R.drawable.update_rummy_table).into(imgTable);

        InitDialog();

        cardPref = getSharedPreferences("cardPref",MODE_PRIVATE);

        if (cardPref.getLong("ExpiredDate", -1) > System.currentTimeMillis()) {
            // read email and password
        } else {
            SharedPreferences.Editor editor = cardPref.edit();
            editor.clear();
            editor.apply();
        }

        Functions.LOGE("Rummy5Player","ExpiredDate : "+cardPref.getLong("ExpiredDate", -1) +
                "\ncurrentTimeMillis : "+System.currentTimeMillis());

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


        toolTipRelativeLayout = (ToolTipRelativeLayout) findViewById(R.id.activity_main_tooltipRelativeLayout);

        findViewById(R.id.tvPassword).setVisibility(View.GONE);

        if(tableModel != null)
        {

            if(Functions.isStringValid(tableModel.getPassword()))
            {
                getTextView(R.id.tvPassword).setVisibility(View.VISIBLE);
                getTextView(R.id.tvPassword).setText("Private Table Code Password : "+tableModel.getPassword());
            }
        }


        findViewById(R.id.rlt_highlighted_pick).setVisibility(View.INVISIBLE);
        findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.INVISIBLE);

        if(isPlayer2)
        {
            findViewById(R.id.lnr_new2player).setVisibility(View.VISIBLE);

            findViewById(R.id.rltplayer2).setVisibility(View.GONE);
            findViewById(R.id.rltplayer3).setVisibility(View.GONE);
            findViewById(R.id.rltplayer4).setVisibility(View.GONE);
            findViewById(R.id.rltplayer5).setVisibility(View.GONE);
            findViewById(R.id.rltplayer6).setVisibility(View.GONE);
            findViewById(R.id.rltplayer7).setVisibility(View.GONE);

        }
        else {
            findViewById(R.id.lnr_new2player).setVisibility(View.GONE);

            findViewById(R.id.rltplayer2).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer3).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer4).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer5).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer6).setVisibility(View.VISIBLE);
//            findViewById(R.id.rltplayer7).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.tvTableamount).setVisibility(View.GONE);


        ivDropCreate =  findViewById(R.id.ivDropCreate);
        bt_sharewallet =  findViewById(R.id.bt_sharewallet);
        shareWalletButtonGone();
        ivDropCreate.setVisibility(View.GONE);

        findViewById(R.id.iv_rightarrow).setVisibility(View.GONE);
        findViewById(R.id.iv_leftarrow).setVisibility(View.GONE);


        mProgress1 = (ProgressBar) findViewById(R.id.circularProgressbar1);
        if(isPlayer2)
            mProgress2 = (ProgressBar) findViewById(R.id.circularProgressbar2_new);
        else
            mProgress2 = (ProgressBar) findViewById(R.id.circularProgressbar2);
        mProgress3 = (ProgressBar) findViewById(R.id.circularProgressbar3);
        mProgress4 = (ProgressBar) findViewById(R.id.circularProgressbar4);
        mProgress5 = (ProgressBar) findViewById(R.id.circularProgressbar5);
        mProgress6 = (ProgressBar) findViewById(R.id.circularProgressbar6);
        mProgress7 = (ProgressBar) findViewById(R.id.circularProgressbar7);

        initializeProgress(mProgress1);
        initializeProgress(mProgress2);
        initializeProgress(mProgress3);
        initializeProgress(mProgress4);
        initializeProgress(mProgress5);
        initializeProgress(mProgress6);
        initializeProgress(mProgress7);

        rs_cardlist_group1 = new ArrayList<>();
        rp_cardlist_group2 = new ArrayList<>();
        bl_cardlist_group3 = new ArrayList<>();
        bp_cardlist_group4 = new ArrayList<>();
        joker_cardlist_group5 = new ArrayList<>();
        selectedcardvalue = new ArrayList<>();

        animationLayout = ((RelativeLayout)findViewById(R.id.sticker_animation_layout));


        ext_group1 = new ArrayList<>();
        ext_group2 = new ArrayList<>();
        ext_group3 = new ArrayList<>();
        ext_group4 = new ArrayList<>();
        ext_group5 = new ArrayList<>();


        grouplist = new ArrayList<>();


        rlt_addcardview=findViewById(R.id.rlt_addcardview);

        rltwinnersymble1=findViewById(R.id.rltwinnersymble1);
        if(isPlayer2)
        {
            rltwinnersymble2=findViewById(R.id.rltwinnersymble2_new);
            findViewById(R.id.rltwinnersymble2).setVisibility(View.GONE);
        }
        else
            rltwinnersymble2=findViewById(R.id.rltwinnersymble2);
        rltwinnersymble3=findViewById(R.id.rltwinnersymble3);
        rltwinnersymble4=findViewById(R.id.rltwinnersymble4);
        rltwinnersymble5=findViewById(R.id.rltwinnersymble5);
        rltwinnersymble6=findViewById(R.id.rltwinnersymble6);
        rltwinnersymble7=findViewById(R.id.rltwinnersymble7);

        rltwinnersymble1.setVisibility(View.GONE);
        rltwinnersymble2.setVisibility(View.GONE);
        rltwinnersymble3.setVisibility(View.GONE);
        rltwinnersymble4.setVisibility(View.GONE);
        rltwinnersymble5.setVisibility(View.GONE);
        rltwinnersymble6.setVisibility(View.GONE);
        rltwinnersymble7.setVisibility(View.GONE);

        ScaleAnimation scaler = new ScaleAnimation((float) 0.7, (float) 1.0, (float) 0.7, (float) 1.0);
        scaler.setRepeatCount(Animation.INFINITE);
        scaler.setDuration(40);

        animMoveCardsPlayerwinner1 = AnimationUtils.loadAnimation(context,
                R.anim.movetoanotherwinner);
        animMoveCardsPlayerwinner2 = AnimationUtils.loadAnimation(context,
                R.anim.movetoanotherleftcornerwinner);


        bt_creategroup=findViewById(R.id.iv_creategroup);
        bt_declare=findViewById(R.id.bt_declare);

        setDeclareButton();




        imgpl1glow=findViewById(R.id.imgpl1glow);
        if(isPlayer2)
            imgpl2glow=findViewById(R.id.imgpl2_newglow);
        else
            imgpl2glow=findViewById(R.id.imgpl2glow);
        imgpl3glow=findViewById(R.id.imgpl3glow);
        imgpl4glow=findViewById(R.id.imgpl4glow);
        imgpl5glow=findViewById(R.id.imgpl5glow);
        imgpl6glow=findViewById(R.id.imgpl6glow);
        imgpl7glow=findViewById(R.id.imgpl7glow);


        ivpickcard=findViewById(R.id.ivpickcard);
        iv_jokercard=findViewById(R.id.iv_jokercard);
        ivFinishDesk=findViewById(R.id.ivFinishDesk);

        imgpl1circle = findViewById(R.id.imgpl1circle);
        if(isPlayer2)
            imgpl2circle = findViewById(R.id.imgpl2_newcircle);
        else
            imgpl2circle=findViewById(R.id.imgpl2circle);
        imgpl3circle = findViewById(R.id.imgpl3circle);
        imgpl4circle = findViewById(R.id.imgpl4circle);
        imgpl5circle = findViewById(R.id.imgpl5circle);
        imgpl6circle = findViewById(R.id.imgpl6circle);
        imgpl7circle = findViewById(R.id.imgpl7circle);

        txtPlay1wallet = findViewById(R.id.txtPlay1wallet);
        if(isPlayer2)
            txtPlay2wallet = findViewById(R.id.txtPlay2_newwallet);
        else
            txtPlay2wallet = findViewById(R.id.txtPlay2wallet);
        txtPlay3wallet = findViewById(R.id.txtPlay3wallet);
        txtPlay4wallet = findViewById(R.id.txtPlay4wallet);
        txtPlay5wallet = findViewById(R.id.txtPlay5wallet);
        txtPlay6wallet = findViewById(R.id.txtPlay6wallet);
        txtPlay7wallet = findViewById(R.id.txtPlay7wallet);

        if(isPlayer2)
            lnrPlay2wallet = findViewById(R.id.lnruserdetails2_new);
        else
            lnrPlay2wallet = findViewById(R.id.lnruserdetails2);
        lnrPlay3wallet = findViewById(R.id.lnruserdetails3);
        lnrPlay4wallet = findViewById(R.id.lnruserdetails4);
        lnrPlay5wallet = findViewById(R.id.lnruserdetails5);
        lnrPlay6wallet = findViewById(R.id.lnruserdetails6);
        lnrPlay7wallet = findViewById(R.id.lnruserdetails7);

        lnrPlay2wallet.setVisibility(View.INVISIBLE);
        lnrPlay3wallet.setVisibility(View.INVISIBLE);
        lnrPlay4wallet.setVisibility(View.INVISIBLE);
        lnrPlay5wallet.setVisibility(View.INVISIBLE);
        lnrPlay6wallet.setVisibility(View.INVISIBLE);
        lnrPlay7wallet.setVisibility(View.INVISIBLE);


        flUserHighlight = findViewById(R.id.flUserHighlight);
        flUserHighlight.setVisibility(View.GONE);
        if(isPlayer2)
            txtPlay2 = findViewById(R.id.txtPlay2_new);
        else
            txtPlay2 = findViewById(R.id.txtPlay2);
        txtPlay3 = findViewById(R.id.txtPlay3);
        txtPlay4 = findViewById(R.id.txtPlay4);
        txtPlay5 = findViewById(R.id.txtPlay5);
        txtPlay6 = findViewById(R.id.txtPlay6);
        txtPlay7 = findViewById(R.id.txtPlay7);

        txtPlay2.setText("");
        txtPlay3.setText("");
        txtPlay4.setText("");
        txtPlay5.setText("");
        txtPlay6.setText("");
        txtPlay7.setText("");

        iv_jokercard=findViewById(R.id.iv_jokercard);
        ivDrawCards=findViewById(R.id.ivDrawCards);

        txtGameFinish=findViewById(R.id.txtGameFinish);


        OnClickListener();
    }

    private void setDeclareButton() {
        bt_declare.setText(getString(R.string.declare));
        bt_declare.setTag(getString(R.string.declare));
    }

    private void setGameEndDeclareButton() {
        bt_declare.setText(getString(R.string.get_ready_to_end));
        bt_declare.setTag(getString(R.string.declare));
    }

    private void setDeclareBackButton(String declarestring){
        bt_declare.setText(declarestring);
        bt_declare.setTag(DECLARE_BACK);
    }

    private void declareButtonVisible(){
        bt_declare.setVisibility(View.VISIBLE);
    }

    private void declareButtonGone(){
        bt_declare.setVisibility(View.GONE);
    }

    private RequestManager LoadImage()
    {
        return  Glide.with(getApplicationContext());
    }

    CountDownTimer drawCardCountDown;
    ArrayList<CardModel> drawCardsList = new ArrayList<>();
    int draw_card_count = 0;
    private void DrawCards(){


        drawCardCountDown = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                SharePref.getInstance().putBoolean(SharePref.isCardDrawn+drawnUniqueid,true);
                findViewById(R.id.rltDrawCard).setVisibility(View.GONE);
                animationLayout.setVisibility(View.GONE);
                if(!isDeclareDialogShow)
                    API_CALL_start_game();
            }
        };

        drawCardCountDown.start();

        for (int i = 0; i < drawCardsList.size() ; i++) {
            String image_path = drawCardsList.get(i).Image;

            if(image_path.substring(image_path.length() - 1).equalsIgnoreCase("_"))
            {
                image_path = removeLastChars(image_path,1);
            }

            String user_id = drawCardsList.get(i).user_id;

            String finalImage_path = image_path;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showDrawnCardonPlayer(user_id, finalImage_path);
                }
            },700);

            if(drawCardsList.size() <= (i+1))
                SharePref.getInstance().putBoolean(SharePref.isCardDrawn+drawnUniqueid,true);
        }
    }


    public void showDrawnCardonPlayer(String chaal_user_id,String uriuser2) {

        animationLayout.setVisibility(View.VISIBLE);


        View  toView = null;
        final View fromView;
        View shuttleView = null;

        fromView = ivallcard;

        if (chaal_user_id.equals(user_id_player1)) {

            toView = imgpl1circle;

        }
        else if (chaal_user_id.equals(user_id_player2)) {


            toView = imgpl2circle;

        }
        else if (chaal_user_id.equals(user_id_player3)) {


            toView = imgpl3circle;

        }
        else if (chaal_user_id.equals(user_id_player4)) {


            toView = imgpl4circle;

        }
        else if (chaal_user_id.equals(user_id_player5)) {


            toView = imgpl5circle;

        }
        else if (chaal_user_id.equals(user_id_player6)) {


            toView = imgpl6circle;

        }
        else if (chaal_user_id.equals(user_id_player7)) {


            toView = imgpl7circle;

        }


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        if(chaal_user_id.equals(user_id_player1))
        {
            destX = destX-50;
            destY = destY-100;
        }

        final ImageView sticker = new ImageView(this);

        // " +
        int stickerId = Functions.GetResourcePath(uriuser2,context);

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        if(stickerId > 0)
            Picasso.get().load(stickerId).into(sticker);
        else {
            uriuser2 = "@drawable/backside_card";  // where myresource

            stickerId = getResources().getIdentifier(uriuser2,
                    null,
                    getPackageName());

            Picasso.get().load(stickerId).into(sticker);
        }

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        animationLayout.addView(sticker);

        Animations anim = new Animations();
        float finalDestX = destX;
        float finalDestY = destY;
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                animationon = false;
                animationLayout.setElevation(5f);
                animationLayout.bringToFront();
                sticker.setX(finalDestX);
                sticker.setY(finalDestY);
            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        PlaySaund(R.raw.teenpatticardflip_android);



    }


    private void initializeProgress(ProgressBar progressBar){

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        progressBar.setProgressDrawable(drawable);
        progressBar.setProgress(pStatusprogress);
        progressBar.setMax(timmersectlarge/1000);

    }

    int count = 8;
    TextView txtGameFinish;
    Timer timerstatus;
    int timertime = 6000;
    SharedPreferences prefs;
    int ongoing = 0;
    private void InitCoutDown() {


        if(getIntent().hasExtra("ongoing"))
            ongoing = getIntent().getIntExtra("ongoing",0);

        if(getIntent().hasExtra("table_id"))
            table_id = getIntent().getStringExtra("table_id");

        if(!isOngoing())
        {
            API_CALL_get_table();
        }
        else {
            startGameButtonGone();
        }


        timerstatus = new Timer();

        timerstatus.scheduleAtFixedRate(new TimerTask() {

                                            @Override
                                            public void run() {

                                                if (!isDeclareDialogShow)
                                                    API_CALL_status();


                                            }

                                        },
                //Set how long before to start calling the TimerTask (in milliseconds)
                timertime,
                //Set the amount of time between each execution (in milliseconds)
                timertime);



        counttimerforstartgame = new CountDownTimer(Variables.GameStart, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                centreX=ivallcard.getX() + ivallcard.getWidth()  / 2;
                centreY=ivallcard.getY() + ivallcard.getHeight() / 2;

                //                Funtions.LOGE("MainActivity","centreX : "+centreX+" / "+"centreY :"+centreY);

//                    count--;
                txtGameFinish.setVisibility(View.VISIBLE);
                txtGameFinish.setText("" + millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                txtGameFinish.setVisibility(View.GONE);
                API_CALL_start_game();

            }


        }.start();

        declareCountDown = new CountDownTimer(Variables.Declare_Back_Editing_Time, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {


                setDeclareBackButton(DECLARE_BACK + " "+(millisUntilFinished/1000));

                isDeclareShow = true;

            }

            @Override
            public void onFinish() {

                isDeclareShow = false;

            }


        };


    }
    SoundPool soundPool;
    private void UserProgressCount() {
        //        soundPool  = new SoundPool(context);

        //Progress -
        Log.d("countdown_timer_", String.valueOf(timmersectlarge));
        mCountDownTimer1 = new CountDownTimer(timmersectlarge, timmersectsmall) {


            @Override
            public void onTick(long millisUntilFinished) {
                imgpl1glow.setVisibility(View.VISIBLE);
                isProgressrun1 = false;
                //                pStatus--;
                pStatus -= 2;
                pStatusprogress++;
                mProgress1.setProgress((int) pStatusprogress * 1);
                // txtCounttimer1.setVisibility(View.VISIBLE);
                //txtCounttimer1.setText(pStatus+"");

                int half_time = timmersectlarge / 2;

                half_time = half_time / 1000;

                if (pStatusprogress >= half_time) {
                    //                    soundPool.PlaySound(R.raw.teenpattitick);
                    playCountDownSound();
                    isTimeoutSoundPlay = true;
                }


            }

            @Override
            public void onFinish() {

                cancelSound();

                pStatusprogress = 0;
                mProgress1.setProgress(100);
                mProgress1.setProgress(0);
                imgpl1glow.setVisibility(View.GONE);
                isProgressrun1 = true;
                Functions.LOGE("mCountDownTimer1","onFinish");
                checkMyCards();

            }

        };

        mCountDownTimer2 = Functions.onUserCountDownTimer(context, timmersectlarge,timmersectsmall, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equals("onTick"))
                {

                    imgpl2glow.setVisibility(View.VISIBLE);
                    isProgressrun2 = false;
                    pStatusprogress++;
                    mProgress2.setProgress((int) pStatusprogress * 1);
                    pStatus--;

                    //                    OnUserProgressStartorEnd("onstart",
                    //                            imgpl2glow,
                    //                            isProgressrun2,
                    //                            pStatusprogress,
                    //                            mProgress2,
                    //                            pStatus);

                    ScalePlayer(imgpl2glow);



                }
                else {
                    isProgressrun2 = true;
                    pStatusprogress = 0;
                    mProgress2.setProgress(100);
                    mProgress2.setProgress(0);
                    imgpl2glow.setVisibility(View.GONE);
                    //                    OnUserProgressStartorEnd("end",
                    //                            imgpl2glow,
                    //                            isProgressrun2,
                    //                            pStatusprogress,
                    //                            mProgress2,
                    //                            pStatus);
                }

            }
        });


        mCountDownTimer3 = Functions.onUserCountDownTimer(context, timmersectlarge,timmersectsmall, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equals("onTick"))
                {

                    imgpl3glow.setVisibility(View.VISIBLE);
                    ScalePlayer(imgpl3glow);

                    isProgressrun3 = false;
                    pStatusprogress++;
                    mProgress3.setProgress((int) pStatusprogress * 1);
                    pStatus--;



                }
                else {
                    isProgressrun3 = true;
                    pStatusprogress = 0;
                    mProgress3.setProgress(100);
                    mProgress3.setProgress(0);
                    imgpl2glow.setVisibility(View.GONE);


                }

            }
        });

        mCountDownTimer4 = Functions.onUserCountDownTimer(context, timmersectlarge,timmersectsmall, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equals("onTick"))
                {

                    imgpl4glow.setVisibility(View.VISIBLE);
                    ScalePlayer(imgpl4glow);
                    isProgressrun4 = false;
                    pStatusprogress++;
                    mProgress4.setProgress((int) pStatusprogress * 1);
                    pStatus--;

                }
                else {
                    isProgressrun4 = true;
                    pStatusprogress = 0;
                    mProgress4.setProgress(100);
                    mProgress4.setProgress(0);
                    imgpl4glow.setVisibility(View.GONE);




                }

            }
        });

        mCountDownTimer5 = Functions.onUserCountDownTimer(context, timmersectlarge,timmersectsmall, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equals("onTick"))
                {

                    imgpl5glow.setVisibility(View.VISIBLE);
                    ScalePlayer(imgpl5glow);

                    isProgressrun5 = false;
                    pStatusprogress++;
                    mProgress5.setProgress((int) pStatusprogress * 1);
                    pStatus--;


                }
                else {
                    isProgressrun5 = true;
                    pStatusprogress = 0;
                    mProgress5.setProgress(100);
                    mProgress5.setProgress(0);
                    imgpl5glow.setVisibility(View.GONE);


                }

            }
        });


        mCountDownTimer6 = Functions.onUserCountDownTimer(context, timmersectlarge,timmersectsmall, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equals("onTick"))
                {

                    imgpl6glow.setVisibility(View.VISIBLE);
                    ScalePlayer(imgpl6glow);

                    isProgressrun6 = false;
                    pStatusprogress++;
                    mProgress6.setProgress((int) pStatusprogress * 1);
                    pStatus--;


                }
                else {
                    isProgressrun6 = true;
                    pStatusprogress = 0;
                    mProgress6.setProgress(100);
                    mProgress6.setProgress(0);
                    imgpl6glow.setVisibility(View.GONE);


                }

            }
        });

        mCountDownTimer7 = Functions.onUserCountDownTimer(context, timmersectlarge,timmersectsmall, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equals("onTick"))
                {

                    imgpl7glow.setVisibility(View.VISIBLE);
                    ScalePlayer(imgpl7glow);

                    isProgressrun7 = false;
                    pStatusprogress++;
                    mProgress7.setProgress((int) pStatusprogress * 1);
                    pStatus--;


                }
                else {
                    isProgressrun7 = true;
                    pStatusprogress = 0;
                    mProgress7.setProgress(100);
                    mProgress7.setProgress(0);
                    imgpl7glow.setVisibility(View.GONE);


                }

            }
        });

    }

    private void showMessagePopup(String message,View msgView){

        ToolTip toolTip = new ToolTip()
                .withText(""+message)
                .withColor(Color.RED)
                .withTextColor(Color.WHITE)
                .withShadow();
//                            .withAnimationType(ToolTip.AnimationType);


        final ToolTipView myToolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, msgView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myToolTipView.remove();
            }
        },5000);

        myToolTipView.setOnToolTipViewClickedListener(new ToolTipView.OnToolTipViewClickedListener() {
            @Override
            public void onToolTipViewClicked(ToolTipView toolTipView) {

            }
        });

        SharePref.getInstance().putString(SharePref.storeChatID,showChatID);
    }


    private void ScalePlayer(View view){

        view.animate()
                .scaleX(1.2f).scaleY(1.2f)//scale to quarter(half x,half y)
//                                .translationY((rltPlayer2_Userbg.getHeight()/4)).translationX((rltPlayer2_Userbg.getWidth()/4))// move to bottom / right
//                                .alpha(0.5f) // make it less visible
                .rotation(360f) // one round turns
                .setDuration(1000) // all take 1 seconds
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //animation ended
                    }
                });

    }

    private void checkMyCards() {

        int my_count = 0;
        for (int i = 0; i < rlt_addcardview.getChildCount() ; i++) {

            View view = rlt_addcardview.getChildAt(i);
            LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);

            if(lnr_group_card == null)
                return;

            for (int j = 0; j < lnr_group_card.getChildCount() ; j++) {
                my_count++;
            }

        }

        if(my_count != RUMMY_TOTALE_CARD && !isCardPick)
        {

            int extra_cards_count = my_count - RUMMY_TOTALE_CARD;

            HashMap params = new HashMap();
            params.put("game_id",""+game_id);
            params.put("user_id",""+prefs.getString("user_id", ""));
            params.put("token",""+prefs.getString("token", ""));
            int finalMy_count = my_count;
            ApiRequest.Call_Api(this, Const.Rummypoolmy_card, params, new Callback() {
                @Override
                public void Responce(String resp, String type, Bundle bundle) {
                    try {
                        JSONObject jsonObject = new JSONObject(resp);


                        String code = jsonObject.optString("code");
                        String message = jsonObject.optString("message");

                        if(code.equalsIgnoreCase("200"))
                        {
                            JSONArray cardsArray = jsonObject.optJSONArray("cards");

                            if(cardsArray != null && cardsArray.length() > 0 && finalMy_count > RUMMY_TOTALE_CARD)
                            {

                                for (int k = 0; k < rlt_addcardview.getChildCount() ; k++) {

                                    View view = rlt_addcardview.getChildAt(k);
                                    LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);

                                    if(lnr_group_card == null)
                                        return;

                                    for (int j = 0; j < lnr_group_card.getChildCount() ; j++) {
                                        boolean isCardAvaialble = false;
                                        View view2 = lnr_group_card.getChildAt(j);
                                        String mycardid = view2.getTag().toString().trim();


                                        for (int i = 0; i < cardsArray.length() ; i++) {

                                            JSONObject cardObject = cardsArray.getJSONObject(i);
                                            CardModel model = new CardModel();

                                            model.card_id = cardObject.optString("card");

                                            if(model.card_id.equalsIgnoreCase(mycardid))
                                            {
                                                isCardAvaialble =true;
                                                break;
//
                                            }

                                        }

                                        if(!isCardAvaialble)
                                        {
//
//                                                    lnr_group_card.removeViewAt(j);
                                            RemoveCardFromArrayLists(mycardid);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    animation_type = "reset_card";
                                                    API_CALL_Sort_card_value();


                                                }
                                            },500);

                                        }

                                    }

                                }



                            }
                            else {
                                Parse_response(jsonObject);
                            }

                        }


                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


        }


    }

    private void OnClickListener(){

        findViewById(R.id.ivrefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMyCards();
                API_CALL_status();
            }
        });

        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivallcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isMyChaal)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(opponent_game_declare)
                    return;

                if(isSplit)
                {
                    animation_type = "pick";
                    API_CALL_get_card();
                }else {
                    Toast.makeText(context, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                }

            }
        });


        ivpickcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isMyChaal)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(opponent_game_declare)
                    return;

//                if(isSplit)
//                {
//                    animation_type = "drop_pick";
//                    API_CALL_get_drop_card();
//                }else {
//                    Toast.makeText(context, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
//                }
                if(isSplit)
                {
                    animation_type = "drop_pick";
                    if (string_middle_card.equalsIgnoreCase("JKR1") || string_middle_card.equalsIgnoreCase("JKR2")){
                        Toast.makeText(context, "Can't pick Joker card!", Toast.LENGTH_SHORT).show();
                    }else if (string_middle_card.equalsIgnoreCase(joker_card)){
                        Toast.makeText(context, "Can't pick Joker card!", Toast.LENGTH_SHORT).show();
                    }else if (str_chk_joker.equalsIgnoreCase("1")){
                        Toast.makeText(context, "Can't pick Joker card!", Toast.LENGTH_SHORT).show();
                    }else{
                        API_CALL_get_drop_card();
                    }
                    Log.d("middle_card_log",string_middle_card);
                    Log.d("joker_card_log",joker_card);     //left one
                }else {
                    Functions.showToast(context, ""+getString(R.string.sort_error_message));
                }
            }
        });

        bt_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                if(!isMyChaal)
                //                {
                //                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                //                    return;
                //                }

                //                if(isSplit)
                //                {
                //                    Toast.makeText(MainActivity.this, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                //                    return;
                //                }

                API_CALL_pack_game();

            }
        });

        bt_declare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isMyChaal && !opponent_game_declare)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isSplit)
                {
                    API_CALL_declare();
                    declareButtonGone();
                    bt_drop.setVisibility(View.GONE);
                }else {
                    Toast.makeText(context, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                }

                //                GetCardFromLayout();

            }
        });

        bt_startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPracticeGame)
                {
                    API_CALL_start_game();
                }
                else {
                    apiViewModel.CALL_API_ASK_START_GAME();
                }
                startGameButtonGone();

            }
        });

        bt_creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animation_type = "group";
                //                API_CALL_card_value();
                CreateGroupFromSelect(false);
                bt_sliptcard.setVisibility(View.GONE);

            }
        });

        bt_sliptcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animation_type = "normal";

                SplitCardtoGroup();

            }
        });

        bt_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isMyChaal)
                {
                    Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedcardvalue.size() == 1)
                {
                    selectedpatti = selectedcardvalue.get(0).Image;
                    selectedpatti_id = selectedcardvalue.get(0).card_id;
                }

                if (selectedpatti.length()>0){

                    if(isSplit)
                    {
                        animation_type = "drop";
                        API_CALL_drop_card(null,0);
                    }
                    else {
                        Toast.makeText(context, ""+getString(R.string.sort_error_message), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(context, ""+getString(R.string.select_card_error_message), Toast.LENGTH_SHORT).show();

                }



            }
        });

        bt_sharewallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWallet(v);
            }
        });
    }

    private void shareWallet(View view){
        Functions.showToast(context,"Share Wallet");

        API_CALL_share_wallet();
        shareWalletButtonGone();
    }

    Dialog shareWalletDialog;
    public void showShareWalletDialog(String view_type,ArrayList<CardModel> game_users_cards_list , final Callback callback) {

        ((ImageView) shareWalletDialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWalletDialog.dismiss();
            }
        });

        TextView txt_title = shareWalletDialog.findViewById(R.id.txtheader);

        if(view_type.equals("share_wallet"))
        {
            txt_title.setText(Functions.getString(context,R.string.share_wallet));
        }
        else {
            txt_title.setText(Functions.getString(context,R.string.game_start));
        }

        RecyclerView recyclerView = shareWalletDialog.findViewById(R.id.rec_declareresult);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ShareWalletAdapter declareAdapter = new ShareWalletAdapter(context,game_users_cards_list);
        recyclerView.setAdapter(declareAdapter);

        if(!shareWalletDialog.isShowing())
            shareWalletDialog.show();
        Functions.setDialogParams(shareWalletDialog);
    }

    private void InitDialog(){
        shareWalletDialog = Functions.DialogInstance(context);
        shareWalletDialog.setContentView(R.layout.dialog_sharewallet_user);
        shareWalletDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    CountDownTimer slideshowTimer,startgameCountDown;
    private void DialogShareWallet(String name ,String share_wallet_id){

        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_slide_show);

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(context != null && !((Activity )context).isFinishing() && dialog.isShowing())
            return;

        if(context != null && !((Activity )context).isFinishing())
            dialog.show();
        Functions.setDialogParams(dialog);

        ((TextView) dialog.findViewById(R.id.txtSlidshow)).setText(name +" has asking for share wallet");

        dialog.findViewById(R.id.imgaccespt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptorrejectShareWallet(share_wallet_id,1);
                slideshowTimer.cancel();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.imgclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptorrejectShareWallet(share_wallet_id,2);
                slideshowTimer.cancel();
                dialog.dismiss();
            }
        });

        slideshowTimer = new CountDownTimer(SECOND_30,1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {
                ((TextView) dialog.findViewById(R.id.tvSlideCountDown)).setText((millisUntilFinished/1000)+" s");

            }

            @Override
            public void onFinish() {
                acceptorrejectShareWallet(share_wallet_id,2);
                dialog.dismiss();
            }
        }.start();

    }

    private void acceptorrejectShareWallet(String share_wallet_id ,int type){

        // type accept = 1;
        //type reject = 2;

        HashMap params = new HashMap();
        params.put("game_id",""+game_id);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));
        params.put("share_wallet_id",""+share_wallet_id);
        params.put("type",""+type);

        ApiRequest.Call_Api(this, Const.Rummypooldo_share_wallet, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    API_CALL_status();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private TextView getTextView(int id){
        return  ((TextView)findViewById(id));
    }

    @Override
    public void onBackPressed() {
        Functions.showDialoagonBack(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(type.equals("exit"))
                {
                    StopSound();

                    API_CALL_leave_table("1");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finishAffinity();

                        }
                    },500);

                }
                else if(type.equals("next"))
                {
                    API_CALL_leave_table("0");
                    StopSound();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                        }
                    },500);
                }

            }
        });
    }

    private void TranslateLayout(ImageView imageView, int cardImage){

//        PlaySaund(R.raw.teenpatticardflip_android);

        int distance = 8000;

        float scale = context.getResources().getDisplayMetrics().density;
        imageView.setCameraDistance(distance * scale);


        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context,
                R.animator.out_animation);
        set.setTarget(imageView);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setImageDrawable(getDrawable(cardImage));

            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();


    }

    boolean animationon = false;
    private void DropTranslationAnimation(){

        animationon = true;

        final View fromView, toView, shuttleView;

        fromView = imgpl1circle;
        toView = ivpickcard;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        animationLayout.setVisibility(View.VISIBLE);
        animationLayout.removeAllViews();
        final ImageView sticker = new ImageView(this);


        String uriuser2 = "@drawable/backside_card";  // where myresource
        // " +
        int stickerId = getResources().getIdentifier(uriuser2,
                null,
                getPackageName());

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        if(stickerId > 0)
            Picasso.get().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        animationLayout.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.clearAnimation();
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        PlaySaund(R.raw.teenpatticardflip_android);



    }

    private void PickCardTranslationAnimation(){

        animationon = true;

        View animationview = findViewById(R.id.animationview);

        final View fromView, toView, shuttleView;

        fromView = ivallcard;
        toView = animationview;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        animationLayout.setVisibility(View.VISIBLE);
        animationLayout.removeAllViews();
        final ImageView sticker = new ImageView(this);


        String uriuser2 = "@drawable/backside_card";  // where myresource
        // " +
        int stickerId = getResources().getIdentifier(uriuser2,
                null,
                getPackageName());

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        if(stickerId > 0)
            Picasso.get().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        animationLayout.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX-100, destY-200, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle)
            {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.clearAnimation();
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        Log.e("MainActivity","FromView : "+fromRect);
        Log.e("MainActivity","toView : "+toRect);

        PlaySaund(R.raw.teenpatticardflip_android);


    }

    private void DropPickTranslationAnimation(){

        animationon = true;


        View rootView = findViewById(R.id.ivpickcard);
        final View fromView, toView, shuttleView;

        fromView = ivpickcard;
        toView = imgpl1circle;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        animationLayout.setVisibility(View.VISIBLE);
        animationLayout.removeAllViews();
        final ImageView sticker = new ImageView(this);


        String uriuser2 = "@drawable/backside_card";  // where myresource
        // " +
        int stickerId = getResources().getIdentifier(uriuser2,
                null,
                getPackageName());

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        if(stickerId > 0)
            Picasso.get().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        animationLayout.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.clearAnimation();
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        PlaySaund(R.raw.teenpatticardflip_android);

    }


    String table_id = "",game_id = "",Main_Game_ID = "";
//    private void API_CALL_get_table() {
//
//        apiViewModel.CALL_API_get_table(tableModel, new Callback() {
//            @Override
//            public void Responce(String resp, String type, Bundle bundle) {
//
//                table_id = resp;
//
//            }
//        });
//
//    }

    private void API_CALL_get_table() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        if(isPlayer2)
            params.put("no_of_players","2");
        else
            params.put("no_of_players","5");

        if(getIntent().hasExtra(SEL_TABLE))
        {
            String boot_value = getIntent().getStringExtra(SEL_TABLE);
            params.put("boot_value", boot_value);
        }
        else {
            return;
        }

        ApiRequest.Call_Api(this, Const.Rummypoolget_table, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    Log.v("get_table" , "working -   "+resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        table_id = jsonObject
                                .getJSONArray("table_data")
                                .getJSONObject(0)
                                .optString("table_id");

                        JSONArray table_users = jsonObject.optJSONArray("table_data");

                        if(table_users != null)
                        {
                            //                            UserResponse(table_users);
                        }

                    }
                    else {
                        Functions.showToast(context, ""+message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void API_CALL_share_wallet() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.Rummypoolshare_wallet, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {

                    Log.v("get_table" , "working -   "+resp);

                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {

                    }
                    else {
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }


    String drawnUniqueid;
    private void UserResponse(JSONArray table_users) {
        //---------------------------------------------User arrange ----------------

        try {
            int pp1 = 0;
            JSONObject Player1Data = null;
            for (int i = 0; i < table_users.length(); i++) {
                String use_temp = prefs.getString("user_id", "");


                if (table_users.getJSONObject(i).getString("user_id").equals(use_temp)) {
                    pp1 = i;
                    Player1Data = table_users.getJSONObject(pp1);
                    table_users.remove(pp1);
                }

            }

//            if(Player1Data == null)
//            {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("id","");
//                jsonObject.put("user_id",""+SharePref.getU_id());
//                jsonObject.put("name",""+SharePref.getInstance().getString(SharePref.u_name));
//                jsonObject.put("profile_pic",""+SharePref.getInstance().getString(SharePref.u_pic));
//                jsonObject.put("wallet",""+SharePref.getInstance().getString(SharePref.wallet));
//                jsonObject.put("invested","0");
//
//                Player1Data = jsonObject;
//            }

            JSONArray NewUsersList = new JSONArray();
            int count = 0;
            for (int i = 0; i < table_users.length() ; i++) {

                if(Player1Data != null && i==0)
                {
                    NewUsersList.put(0,Player1Data);
                }
                {
                    JSONObject UserObjects = table_users.getJSONObject(count);
                    NewUsersList.put(UserObjects);
                    count++;
                }

            }

            table_users = NewUsersList;


            Functions.LOGE("UserResponse","table_users : "+table_users);

            user_id_player1 = "";
            user_id_player2 = "";
            user_id_player3 = "";
            user_id_player4 = "";
            user_id_player5 = "";
            user_id_player6 = "";
            user_id_player7 = "";
            drawCardsList.clear();
            drawnUniqueid = table_id;
            if(isPlayer2)
            {
                for (int k = 0; k < table_users.length(); k++) {
                    if (k == 0) {

                        String name = table_users.getJSONObject(0).getString("name");
                        user_id_player1 = table_users.getJSONObject(0).getString(
                                "user_id");
                        String profile_pic = table_users.getJSONObject(0).getString("profile_pic");
                        String walletplayer1 = table_users.getJSONObject(0).getString("wallet");
                        String invested = table_users.getJSONObject(0).optString("invested","0");
                        long numberamount = Long.parseLong(invested);
                        getTextView(R.id.tvTableType).setText("Pool Rummy "
                                +numberamount);
                        txtPlay1wallet.setText("" +Functions.formateAmount(walletplayer1));

                        if(context != null && !context.isFinishing())
                            LoadImage().load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);

                        if (user_id_player1.equals(prefs.getString("user_id", ""))) {


                        } else {

                            Functions.showToast(context, "Your are timeout from " +
                                    "this table Join again.");


                            finish();
                        }
                        // imgchipuser1.setVisibility(View.VISIBLE);
                        // first player
                    }
                    else {

                        user_id_player2 = table_users.getJSONObject(k).getString("user_id");
                        InitializeUsers(user_id_player2,
                                table_users.getJSONObject(k),
                                txtPlay2,
                                txtPlay2wallet,
                                imgpl2circle,
                                lnrPlay2wallet);

                        if(!user_id_player2.equals("0") && !user_id_player2.equals(""))
                            break;

                    }
                }
            }
            else
                for (int k = 0; k < table_users.length(); k++) {
                    if (k == 0) {

                        String name = table_users.getJSONObject(0).getString("name");
                        user_id_player1 = table_users.getJSONObject(0).getString(
                                "user_id");
                        String profile_pic = table_users.getJSONObject(0).getString("profile_pic");
                        String walletplayer1 = table_users.getJSONObject(0).getString("wallet");
                        String invested = table_users.getJSONObject(0).optString("invested","0");
                        long numberamount = Long.parseLong(invested);
//                        txtPlay1wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
                        txtPlay1wallet.setText("" +Functions.formateAmount(walletplayer1));
                        Picasso.get()
                                .load(Const.IMGAE_PATH + profile_pic)
//                                .error(Functions.getDrawable(context,R.drawable.update_background_profile))
//                                .placeholder(Functions.getDrawable(context,R.drawable.update_background_profile))
                                .into(imgpl1circle);

                        if (user_id_player1.equals(prefs.getString("user_id", ""))) {


                        }
                        else {

                            if(!isOngoing())
                            {
                                Toast.makeText(context, "Your are timeout from " +
                                                "this table Join again.",
                                        Toast.LENGTH_LONG).show();


                                finish();
                            }
                        }
                        // imgchipuser1.setVisibility(View.VISIBLE);

                        //first player

                        String drawn_card = table_users.getJSONObject(0).getString("card");
                        CardModel drawCardModel = new CardModel();
                        drawCardModel.user_id = user_id_player1;
                        drawCardModel.Image = drawn_card;
                        drawnUniqueid = table_users.getJSONObject(0).getString("table_id");
                        if(Functions.checkisStringValid(drawn_card))
                            drawCardsList.add(drawCardModel);

                    }
                    else if (k == 1) {

                        user_id_player2 = table_users.getJSONObject(k).getString("user_id");
                        InitializeUsers(user_id_player2,
                                table_users.getJSONObject(k),
                                txtPlay2,
                                txtPlay2wallet,
                                imgpl2circle,
                                lnrPlay2wallet);
                    }
                    else if (k == 2) {

                        user_id_player3 = table_users.getJSONObject(k).getString("user_id");

                        InitializeUsers(user_id_player3,
                                table_users.getJSONObject(k),
                                txtPlay3,
                                txtPlay3wallet,
                                imgpl3circle,
                                lnrPlay3wallet);
                    }
                    else if (k == 3) {

                        user_id_player4 = table_users.getJSONObject(k).getString("user_id");

                        InitializeUsers(user_id_player4,
                                table_users.getJSONObject(k),
                                txtPlay4,
                                txtPlay4wallet,
                                imgpl4circle,
                                lnrPlay4wallet);
                    }
                    else if (k == 4) {

                        user_id_player5 = table_users.getJSONObject(k).getString("user_id");

                        InitializeUsers(user_id_player5,
                                table_users.getJSONObject(k),
                                txtPlay5,
                                txtPlay5wallet,
                                imgpl5circle,
                                lnrPlay5wallet);
                    }
                    else if (k == 5) {

                        user_id_player6 = table_users.getJSONObject(k).getString("user_id");

                        InitializeUsers(user_id_player6,
                                table_users.getJSONObject(k),
                                txtPlay6,
                                txtPlay6wallet,
                                imgpl6circle,
                                lnrPlay6wallet);
                    }
                    else if(k == 6){

                        user_id_player7 = table_users.getJSONObject(k).getString("user_id");

                        InitializeUsers(user_id_player7,
                                table_users.getJSONObject(k),
                                txtPlay7,
                                txtPlay7wallet,
                                imgpl7circle,
                                lnrPlay7wallet);
                    }
                }

            Functions.LOGE("Rummy5Player","isCardDrawn : " +isCardDrawn()+" : "+drawnUniqueid);
            if(!isgamestarted && drawCardsList.size() > 0 && !isCardDrawn())
            {
                DrawCards();
            }
            else {
                if(!isDeclareDialogShow && !isOngoing())
                    if(!isgamestarted && drawCardsList.size() > 0 &&  isCardDrawn())
                        API_CALL_start_game();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isOngoing(){
        return ongoing == 1 ? true : false;
    }

    private boolean isCardDrawn(){
        return SharePref.getInstance().getBoolean(SharePref.isCardDrawn+drawnUniqueid,false);
    }

    private void API_CALL_pack_game() {

        Submitcardslist = GetCardFromLayout();


        apiViewModel.CALL_API_pack_game(Submitcardslist, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        isFirstChall = false;
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        bt_drop.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void API_CALL_leave_table(final String value) {

        Submitcardslist = GetCardFromLayout();

        apiViewModel.CALL_API_leave_table(Submitcardslist);

    }

    private void API_CALL_rejoin_game_amount() {

        apiViewModel.CALL_API_rejoin_game_amount(new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.optString("code");
                    String message = jsonObject.optString("message");
                    String rejoin_amount = jsonObject.optString("rejoin_amount","");

                    CommonFunctions.showAlertDialog(context, "Do you want to continue ? "
                            , "Pay " + rejoin_amount + " Coins to continue the Game!",
                            false, "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    apiViewModel.CALL_API_renew_game();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isPayDialogShow = false;
                                        }
                                    },2000);

                                }
                            },
                            "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    isPayDialogShow = false;
                                    ExitFromGames();
                                }
                            });

                    isPayDialogShow = true;

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    String Submitcardslist = "";
    boolean isApiCalling = false;
    private void API_CALL_declare() {

        if(isApiCalling)
            return;

        isApiCalling = true;

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);

        String url = "";

        if(bt_declare.getTag().toString().trim().equalsIgnoreCase(DECLARE_BACK))
            url = Const.Rummypooldeclare_back;
        else
            url = Const.Rummypooldeclare;

        ApiRequest.Call_Api(this, url, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {


                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        declareButtonGone();
                        bt_drop.setVisibility(View.GONE);
                        game_declare = true;
                    }
                    else {
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    declareButtonGone();
                    bt_drop.setVisibility(View.GONE);
                }

                isApiCalling = false;
                isFinishDesk = false;
            }
        });



    }

    private String GetCardFromLayout(){

        JSONArray jsonArray = new JSONArray();

        try {


            if(!isSplit)
            {

                for (int k = 0; k < 1 ; k++) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("card_group",""+0);

                    JSONArray jsonArray1 = new JSONArray();

                    for (int i = 0; i < rlt_addcardview.getChildCount() ; i++) {

                        View view = rlt_addcardview.getChildAt(i);

                        jsonArray1.put(""+view.getTag());

                        jsonObject.put("cards",jsonArray1);
                    }

                    jsonArray.put(jsonObject);

                }



            }
            else {


                for (int i = 0; i < rlt_addcardview.getChildCount() ; i++) {

                    JSONObject jsonObject = new JSONObject();

                    JSONArray jsonArray1 = new JSONArray();

                    View view = rlt_addcardview.getChildAt(i);
                    LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);

                    if(lnr_group_card == null)
                        return "";

//                    jsonObject.put("card_group",""+lnr_group_card.getTag());
                    Log.e("TAG_lnr_group_card", "GetCardFromLayout: " + lnr_group_card.getTag().toString());

                    if (str_1st_life.equals("1") && str_2nd_life.equals("1")) {
                        jsonObject.put("card_group", "" + lnr_group_card.getTag());
                        Log.e("one_", "3");
                    }
                    else {
                        jsonObject.put("card_group", "0");
                    }
                    for (int j = 0; j < lnr_group_card.getChildCount() ; j++) {

                        View view2 = lnr_group_card.getChildAt(j);
                        jsonArray1.put(""+view2.getTag());

                        //                    Log.e("MainActivity","Layout Tags : "+view2.getTag());

                        jsonObject.put("cards",jsonArray1);
                    }


                    jsonArray.put(jsonObject);

                }


                //            Log.e("MainActivity","Layout Tags : "+jsonArray.toString());


            }

        } catch (JSONException e) {
            e.printStackTrace();

            Log.e("MainActivity","Layout Tags : "+e.getMessage());


        }


        cardPref.edit().putString(Pref_cards+game_id,""+jsonArray.toString()).apply();
        cardPref.edit().putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12)).apply();

        Functions.LOGE("Rummy5Player",""+jsonArray.toString());
        return jsonArray.toString();
    }

    private void API_CALL_start_game() {

        startGameButtonGone();
        RestartGameActivity();

        apiViewModel.CALL_API_start_game(new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                game_id = resp;
                Main_Game_ID = resp;


            }
        });


    }

    private void startGameButtonVisible(){
        if(!isgamestarted)
            bt_startgame.setVisibility(View.VISIBLE);
    }

    private void startGameButtonGone(){
        bt_startgame.setVisibility(View.GONE);
    }

    private void shareWalletButtonVisible(){
//        if(!isPracticeGame)
//            bt_sharewallet.setVisibility(View.VISIBLE);
    }

    private void shareWalletButtonGone(){
        bt_sharewallet.setVisibility(View.GONE);
    }

    boolean isStatusApiCalling = false;
    private void API_CALL_status() {

        if(isStatusApiCalling)
            return;

        isStatusApiCalling = true;

        userModelArrayList.clear();

        if(rlt_addcardview.getChildCount() > 0)
            Submitcardslist = GetCardFromLayout();
        else {
            if(isgamestarted)
                checkMyCards();
        }


        apiViewModel.CALL_API_status(game_id, table_id, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    Parse_responseStatus(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                isStatusApiCalling = false;
            }
        });

    }

    private void API_CALL_getCardList() {

        if(isOngoing())
            return;


        apiViewModel.CALL_API_getCardList(game_id, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    RestartGameActivity();

                    String previous_cards =  cardPref.getString(Pref_cards+game_id,"");

                    if(previous_cards != null && !previous_cards.equals("") && !previous_cards.equalsIgnoreCase("[{\"card_group\":\"0\"}]"))
                    {
                        Parse_PreviousCards(previous_cards,jsonObject);
                    }
                    else {
                        Parse_response(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }

    private void Parse_PreviousCards(String previous_cards, JSONObject jsonObject) throws JSONException{

        JSONArray jsonArray = new JSONArray(previous_cards);
        int groups_lenth = jsonArray.length();

        if(groups_lenth > 0)
        {

            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            JSONArray cardsArray1 = jsonObject1.getJSONArray("cards");

            if(cardsArray1.length() > 0)
            {
                String cardvalue = cardsArray1.get(0).toString();
                if(cardvalue != null && !cardvalue.equalsIgnoreCase("") && !cardvalue.equalsIgnoreCase("null"))
                {
                    for (int i = 0; i < groups_lenth ; i++) {

                        if(i == 0)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),rs_cardlist_group1,jsonObject);
                        else if(i == 1)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),rp_cardlist_group2,jsonObject);
                        else if(i == 2)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),bl_cardlist_group3,jsonObject);
                        else if(i == 3)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),bp_cardlist_group4,jsonObject);
                        else if(i == 4)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),joker_cardlist_group5,jsonObject);
                        else if(i == 5)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),ext_group1,jsonObject);
                        else if(i == 6)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),ext_group2,jsonObject);
                        else if(i == 7)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),ext_group3,jsonObject);
                        else if(i == 8)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),ext_group4,jsonObject);
                        else if(groups_lenth == 9)
                            addCardsonListFromJson(jsonArray.getJSONObject(i),ext_group5,jsonObject);


                    }

                    findViewById(R.id.rlt_dropgameview).setVisibility(View.GONE);
                    isgamestarted = true;
                    isFirstGameStart = true;
                    API_CALL_Sort_card_value();

                }
                else
                    Parse_response(jsonObject);

            }
            else
                Parse_response(jsonObject);


        }
        else
            Parse_response(jsonObject);


    }

    private void addCardsonListFromJson(JSONObject jsonObject,ArrayList<CardModel> arrayList,JSONObject apiCardsList) throws JSONException {

        JSONArray jsonArray = jsonObject.getJSONArray("cards");
        JSONArray cardsArray = apiCardsList.optJSONArray("cards");

        for (int i = 0; i < jsonArray.length() ; i++) {
            CardModel model = new CardModel();
            model.Image = ""+jsonArray.get(i);

            if(model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
            {
                model.Image = removeLastChars(model.Image,1);
            }

            model.card_id = ""+jsonArray.get(i);


            model.card_group = getColorCode(model.Image);


            boolean isCardAvaialble = false;

            for (int k = 0; k < cardsArray.length() ; k++) {

                JSONObject cardObject = cardsArray.getJSONObject(k);
                CardModel storeCardModel = new CardModel();

                storeCardModel.card_id = cardObject.optString("card");

                if(storeCardModel.card_id.equalsIgnoreCase(model.card_id))
                {
                    isCardAvaialble =true;
                    break;
                }

            }

            if (isCardAvaialble) {
                arrayList.add(model);
            }

        }

    }

    boolean isDeclareVisible = false;
    boolean isFirstChall = false;
    boolean isFirstGame = false;
    private void RestartGameActivity(){
        removeUserStatus();
        findViewById(R.id.rlt_dropgameview).setVisibility(View.GONE);
        isSplit = false;
        isTimeoutSoundPlay = false;
        rs_cardlist_group1.clear();
        rp_cardlist_group2.clear();
        bl_cardlist_group3.clear();
        bp_cardlist_group4.clear();
        joker_cardlist_group5.clear();
        ext_group1.clear();
        ext_group2.clear();
        ext_group3.clear();
        ext_group4.clear();
        ext_group5.clear();
        selectedcardvalue.clear();
        grouplist.clear();
        rlt_addcardview.removeAllViews();
        bt_sliptcard.setVisibility(View.GONE);
        bt_creategroup.setVisibility(View.GONE);
        declareButtonGone();
//        bt_drop.setVisibility(View.VISIBLE);
        DrobButtonVisible(false);
        setDeclareButton();
        game_declare = false;
        opponent_game_declare = false;
        isFinishDesk = false;
        isFirstChall = true;
        isgamestarted = false;
        isGamePacked = false;
        isCardPick = false;
        isResentCardDrop = false;

        cardModelArrayList.clear();
        userModelArrayList.clear();
        findViewById(R.id.iv_rightarrow).setVisibility(View.GONE);
        findViewById(R.id.iv_leftarrow).setVisibility(View.GONE);

        String uri1 = "@drawable/" + "finish_deck";  // where myresource " +

        int imageResource1 = getResources().getIdentifier(uri1, null,
                getPackageName());
        Picasso.get().load(imageResource1).into(ivFinishDesk);

        int first_check_ui = R.drawable.ic_uncheckbox;
        int second_check_ui = R.drawable.ic_uncheckbox;

        ((ImageView)findViewById(R.id.ivFirstlifecheck))
                .setImageDrawable(Functions.getDrawable(context,first_check_ui));

        ((ImageView)findViewById(R.id.ivSeconlifecheck))
                .setImageDrawable(Functions.getDrawable(context,second_check_ui));

        str_1st_life=""; str_2nd_life="";
        string_middle_card = "";


    }

    private void DeclareButtonVisible(boolean visible){
        bt_declare.setVisibility(visible ? View.VISIBLE : View.GONE);
//            bt_finish.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void DrobButtonVisible(boolean visible){
//        bt_declare.setVisibility(visible ? View.VISIBLE : View.GONE);
        bt_drop.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(updatechatBroadcast);
        StopSound();
        timerstatus.cancel();
        if(slideshowTimer  != null)
        {
            slideshowTimer.onFinish();
            slideshowTimer.cancel();
        }

        super.onDestroy();

    }

    boolean isgamestarted = false;
    boolean isFirstGameStart = false;
    boolean game_declare = false;
    boolean opponent_game_declare = false;
    boolean isGamePacked = false;
    ArrayList<Usermodel> pointUserlist ;
    ArrayList<Usermodel> startGameUserList ;
    String mUserChaal,str_game_id="";
    private void Parse_responseStatus(JSONObject jsonObject) throws JSONException {

        String code = jsonObject.optString("code");
        String message = jsonObject.optString("message");
        JSONArray table_users = jsonObject.optJSONArray("table_users");
        JSONArray chat_list = jsonObject.optJSONArray("chat_list");
        JSONObject table_detail = jsonObject.optJSONObject("table_detail");
        JSONArray start_game = jsonObject.optJSONArray("start_game");
        JSONArray game_users = jsonObject.optJSONArray("game_users");
        String table_amount = jsonObject.optString("table_amount","50");
        String total_table_amount  = jsonObject.optString("total_table_amount");
        int round  = jsonObject.optInt("round",1);


        getTextView(R.id.tv_gameid).setText("#"+game_id+"-"+round);
        getTextView(R.id.tvTableType).setText("Pool Rummy "+Functions.getStringFromTextView(txtPlay1wallet));

        if(Functions.checkisStringValid(total_table_amount))
        {
            getTextView(R.id.tvTableamount).setVisibility(View.VISIBLE);
            getTextView(R.id.tvTableamount).setText("Prize "+total_table_amount);

        }
        else {
            getTextView(R.id.tvTableamount).setVisibility(View.GONE);
        }

        if(start_game != null)
        {
            ParseStartGameRespones(start_game);
        }

        if(game_users != null)
        {
            for (int i = 0; i < game_users.length() ; i++) {
                JSONObject jsonObject1 = game_users.getJSONObject(i);
                String user_id = jsonObject1.getString("user_id");
                String packed = jsonObject1.optString("packed");

                if(user_id.equals(prefs.getString("user_id", "")))
                {
                    if(packed.equals("1"))
                    {
                        isgamestarted = false;
                        complategameUIChange();
                        isGamePacked = true;
                        ((TextView)findViewById(R.id.tv_gamemessage)).setText(""+getString(R.string.drop_message));
                        findViewById(R.id.rlt_dropgameview).setVisibility(View.VISIBLE);
                    }
                    else {
                        isGamePacked = false;
                    }
                }

            }

            if(game_users.length() >= 2 && !isOngoing())
                shareWalletButtonVisible();
            else
                shareWalletButtonGone();
        }
        else {

        }

        if(table_users != null)
        {
            UserResponse(table_users);
        }

        if(chat_list != null)
        {
            ParseChatingResponse(chat_list);
        }

        if(table_detail != null)
        {
            ParseTableDetails(table_detail);
        }


        if(code.equalsIgnoreCase("200"))
        {


            JSONArray drop_card = jsonObject.optJSONArray("drop_card");
            JSONArray game_users_cards = jsonObject.optJSONArray("game_users_cards");
            JSONArray game_log = jsonObject.optJSONArray("game_log");
            Log.e("TAG_game_log", "Parse_responseStatus: " + game_log);
            str_game_id = game_log.getJSONObject(0).getString("game_id");
            joker_card = jsonObject.optString("joker");

            if(joker_card.substring(joker_card.length() - 1).equalsIgnoreCase("_"))
            {
                joker_card = removeLastChars(joker_card,1);
            }


            String game_status = jsonObject.optString("game_status");
            String declare_user_id = jsonObject.optString("declare_user_id");
            boolean declare = jsonObject.optBoolean("declare");

            if(declare_user_id != null &&
                    !declare_user_id.equals(prefs.getString("user_id", "")) && declare && !game_declare)
            {
                opponent_game_declare = true;
                Toast.makeText(context, ""+getString(R.string.declare_game), Toast.LENGTH_SHORT).show();

                if(!isOngoing())
                {
                    declareButtonVisible();

                    if(!isDeclareShow)
                    {
                        setDeclareBackButton(DECLARE_BACK);
                        declareCountDown.start();
                    }
                }

            }
            else {

                if(!declare_user_id.equalsIgnoreCase("0"))
                {
                    if(declare)
                    {
                        game_declare = declare;
                        declareButtonGone();
                    }
                }

            }

            String winner_user_id = jsonObject.optString("winner_user_id");

            if(game_status.equalsIgnoreCase("2") || game_status.equalsIgnoreCase("0"))
            {
                isgamestarted = false;
                complategameUIChange();
                makeWinnertoPlayer(winner_user_id);
                game_id = jsonObject.optString("active_game_id");

                if(!isFirstGame)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            count = 8;
                            counttimerforstartgame.start();
                            isFirstGame = true;
                        }
                    },3000);
                }


            }
            else if(game_status.equalsIgnoreCase("1"))
            {
                startGameButtonGone();
                rltwinnersymble1.setVisibility(View.GONE);
                rltwinnersymble2.setVisibility(View.GONE);
                rltwinnersymble3.setVisibility(View.GONE);
                rltwinnersymble4.setVisibility(View.GONE);
                rltwinnersymble5.setVisibility(View.GONE);
                rltwinnersymble6.setVisibility(View.GONE);
                rltwinnersymble7.setVisibility(View.GONE);

                if(!isgamestarted && !isGamePacked)
                    API_CALL_getCardList();
                else  if(isgamestarted && rlt_addcardview.getChildCount() <= 0)
                    API_CALL_getCardList();


                String chaal = jsonObject.getString("chaal");
                String card_count = jsonObject.getString("card_count");
                getTextView(R.id.tvCardCounts).setText(""+card_count);
                mUserChaal = jsonObject.getString("chaal");
                parseLastCard(jsonObject);
                makeHightLightForChaal(mUserChaal);
            }

            String urijokar = "@drawable/" + joker_card.toLowerCase();  // where myresource " +
            //
            int imageResource2 = getResources().getIdentifier(urijokar, null,
                    getPackageName());
            if(imageResource2 > 0)
                Picasso.get().load(imageResource2).into(iv_jokercard);

            //---------------------------------------------User Aarange------------------



            for (int i = 0; i < table_users.length() ; i++) {

                JSONObject tables_Object = table_users.getJSONObject(i);
                Usermodel model = new Usermodel();
                model.userid = tables_Object.optString("user_id");


                model.seat_position = tables_Object.optString("seat_position");


                model.userName = tables_Object.optString("name");
                model.userMobile = tables_Object.optString("mobile");
                model.userImage = tables_Object.optString("profile_pic");
                model.userWallet = tables_Object.optString("wallet");

                userModelArrayList.add(model);
            }


            if(drop_card != null)
            {

                if(drop_card.length() > 0)
                {
                    for (int i = 0; i < drop_card.length() ; i++) {

                        JSONObject drop_Object = drop_card.getJSONObject(i);
                        CardModel model = new CardModel();
                        model.Image = drop_Object.optString("card");
                        string_middle_card = drop_Object.optString("card");
                        if(!model.Image.equals("") && !model.Image.equals("null") &&
                                model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
                        {
                            model.Image = removeLastChars(model.Image,1);
                        }
                        String uri1 = "@drawable/" + model.Image.toLowerCase();  // where myresource " +



                        int imageResource1 = getResources().getIdentifier(uri1, null,
                                getPackageName());

                        if(imageResource1 > 0)
                            ivpickcard.setImageDrawable(Functions.getDrawable(context,imageResource1));

                        String src_joker_cards = "";
                        src_joker_cards = joker_card.substring(joker_card.length() - 1);

                        if(src_joker_cards != null && !src_joker_cards.equals(""))
                        {
                            if(src_joker_cards.contains(model.Image.substring(model.Image.length() - 1)))
                            {

                                ((ImageView)findViewById(R.id.iv_jokercard2)).setVisibility(View.VISIBLE);
                                str_chk_joker="1";
                            }
                            else {
                                ((ImageView)findViewById(R.id.iv_jokercard2)).setVisibility(View.GONE);
                                str_chk_joker="";
                            }
                        }

                    }
                }
                else {
                    ivpickcard.setImageDrawable(getResources().getDrawable(R.drawable.backside_card));
                }


            }

            if(game_users_cards != null)
            {
                isDeclareDialogShow = true;
                ArrayList<CardModel> game_users_cards_list = new ArrayList<>();

                for (int i = 0; i < game_users_cards.length() ; i++) {


                    JSONObject game_object = game_users_cards.getJSONObject(i);
                    JSONObject json_user = game_object.getJSONObject("user");
                    CardModel model = new CardModel();
                    String user_id = json_user.optString("user_id");
                    model.user_id = user_id;

//                    for (Usermodel usermodel: userModelArrayList) {
//                        if(user_id.equals(usermodel.userid))
//                        {
//                            model.user_name = usermodel.userName;
//                        }
//                    }

                    model.user_name = json_user.optString("name");
                    model.score = json_user.optInt("score",0);
                    model.won = json_user.optInt("win",0);

                    // result = 1 user drop
                    // result = 0 user normal functional

                    model.result = json_user.optInt("result",0);
                    model.packed = json_user.optInt("packed",0);

                    model.winner_user_id = winner_user_id;
                    model.joker_card = joker_card;
                    model.game_id = Main_Game_ID;

                    ArrayList<CardModel> groups_cardlist = new ArrayList<>();
                    if(model.user_id.equals(prefs.getString("user_id", "")))
                    {

                        if(Submitcardslist == null || Submitcardslist.equals(""))
                            Submitcardslist = GetCardFromLayout();

                        String str_jsonArray = Submitcardslist;

                        JSONArray jsonArray = new JSONArray(str_jsonArray);

                        if(jsonArray.length() > 0)
                        {
                            for (int k = 0; k < jsonArray.length() ; k++) {
                                JSONObject json_cardlist = jsonArray.getJSONObject(k);
                                ArrayList<CardModel> user_cards = new ArrayList<>();
                                CardModel group_model = new CardModel();

                                JSONArray json_cards = json_cardlist.getJSONArray("cards");
                                for (int j = 0; j < json_cards.length() ; j++) {

                                    CardModel model_cards = new CardModel();
                                    model_cards.Image = json_cards.getString(j);
                                    user_cards.add(model_cards);

                                    group_model.groups_cards = user_cards;

                                }
                                groups_cardlist.add(group_model);
                                model.groups_cards = groups_cardlist;

                            }
                        }
                        else {
                            JSONArray group_array = json_user.optJSONArray("cards");

//                            if(group_array == null)
//                                return;

                            if(group_array == null)
                            {
                                for (int k = 0; k < 1 ; k++) {
                                    CardModel group_model = new CardModel();
                                    ArrayList<CardModel> user_cards = new ArrayList<>();

                                    for (int j = 0; j < 13 ; j++) {

                                        CardModel model_cards = new CardModel();
                                        model_cards.card_group = DUMMY_CARD;
                                        model_cards.Image = DUMMY_CARD;
                                        user_cards.add(model_cards);

                                        group_model.groups_cards = user_cards;
                                    }

                                    groups_cardlist.add(group_model);
                                    model.groups_cards = groups_cardlist;

                                }

                            }
                            else {
                                for (int k = 0; k < group_array.length() ; k++) {
                                    CardModel group_model = new CardModel();
                                    ArrayList<CardModel> user_cards = new ArrayList<>();
                                    JSONObject cards_object = group_array.getJSONObject(k);

                                    String card_group = cards_object.optString("card_group");

                                    JSONArray cards_array = cards_object.getJSONArray("cards");

                                    for (int j = 0; j < cards_array.length() ; j++) {
                                        //                                JSONObject card_object = cards_array.getJSONObject(j);

                                        CardModel model_cards = new CardModel();
                                        model_cards.card_group = card_group;
                                        model_cards.Image = cards_array.getString(j);
                                        user_cards.add(model_cards);

                                        group_model.groups_cards = user_cards;
                                    }

                                    groups_cardlist.add(group_model);
                                    model.groups_cards = groups_cardlist;

                                }
                            }
                        }

                    }
                    else {

                        JSONArray group_array = json_user.optJSONArray("cards");

//                        if(group_array == null)
//                            return;

                        if(group_array == null)
                        {
                            for (int k = 0; k < 1 ; k++) {
                                CardModel group_model = new CardModel();
                                ArrayList<CardModel> user_cards = new ArrayList<>();

                                for (int j = 0; j < 13 ; j++) {

                                    CardModel model_cards = new CardModel();
                                    model_cards.card_group = DUMMY_CARD;
                                    model_cards.Image = DUMMY_CARD;
                                    user_cards.add(model_cards);

                                    group_model.groups_cards = user_cards;
                                }

                                groups_cardlist.add(group_model);
                                model.groups_cards = groups_cardlist;

                            }

                        }
                        else {
                            for (int k = 0; k < group_array.length() ; k++) {
                                CardModel group_model = new CardModel();
                                ArrayList<CardModel> user_cards = new ArrayList<>();
                                JSONObject cards_object = group_array.getJSONObject(k);

                                String card_group = cards_object.optString("card_group");

                                JSONArray cards_array = cards_object.getJSONArray("cards");

                                for (int j = 0; j < cards_array.length() ; j++) {
                                    //                                JSONObject card_object = cards_array.getJSONObject(j);

                                    CardModel model_cards = new CardModel();
                                    model_cards.card_group = card_group;
                                    model_cards.Image = cards_array.getString(j);
                                    user_cards.add(model_cards);

                                    group_model.groups_cards = user_cards;
                                }

                                groups_cardlist.add(group_model);
                                model.groups_cards = groups_cardlist;

                            }
                        }

                    }

                    game_users_cards_list.add(model);

                }

                cancelSound();
                setDeclareButton();
                int game_start_time = jsonObject.optInt("game_start_time",30);
                DialogDeclareCards dialogDeclareCards = new DialogDeclareCards(context){
                    @Override
                    public void startGame(String resp) {
                        makeWinnertoPlayer("");
                        if(resp.equalsIgnoreCase("startgame"))
                        {
                            API_CALL_start_game();
                        }

                        isDeclareDialogShow = false;
                    }
                };
                dialogDeclareCards
                        .setGame_users_cards_list(game_users_cards_list, str_game_id)
                        .setGame_start_time(game_start_time)
                        .show();
                ivpickcard.setImageDrawable(getResources().getDrawable(R.drawable.backside_card));
            }

            JSONArray share_wallet = jsonObject.optJSONArray("share_wallet");
            // share_wallet_status = 0 pending
            // share_wallet_status = 1 accept
            // share_wallet_status = 2 reject
            if(share_wallet != null && share_wallet.length() > 0)
            {
                ArrayList <CardModel> shareWalletList = new ArrayList<>();
                shareWalletButtonGone();
                String from_user_id = "";
                for (int i = 0; i < share_wallet.length() ; i++) {
                    JSONObject shareObject = share_wallet.getJSONObject(i);

                    from_user_id = shareObject.optString("user_id");
                    String to_user_id = shareObject.optString("to_user_id");
                    String name = shareObject.optString("name");
                    String to_name = shareObject.optString("to_name");
                    String share_wallet_status = shareObject.optString("status");


                    CardModel shareWalletModel = new CardModel();
                    shareWalletModel.user_id = to_user_id;
                    if(to_user_id.equalsIgnoreCase(user_id_player1))
                    {
                        name = shareObject.optString("name");
                        String share_wallet_id = shareObject.optString("id");
                        share_wallet_status = shareObject.optString("status");

                        if(share_wallet_status.equalsIgnoreCase("0")
                                && !share_wallet_status.equalsIgnoreCase("2"))
                        {
                            DialogShareWallet(name,share_wallet_id);
                        }

                    }
                    else {

                        CardModel myshareWallet = new CardModel();
                        myshareWallet.user_name = name;
                        myshareWallet.user_id = from_user_id;
                        myshareWallet.status = "Send request";

                        boolean isAdded = false;

                        for (CardModel myuser: shareWalletList) {

                            if(myuser.user_id.equals(user_id_player1))
                            {
                                isAdded = true;
                                break;
                            }

                        }

                        if(!isAdded)
                            shareWalletList.add(0,myshareWallet);

                    }

                    shareWalletModel.user_id = to_user_id;
                    shareWalletModel.user_name = to_name;
                    shareWalletModel.status = share_wallet_status;
                    if(share_wallet_status.equalsIgnoreCase("0"))
                    {
                        shareWalletModel.status = "Pending";
                    }
                    else if (share_wallet_status.equalsIgnoreCase("1")) {
                        shareWalletModel.status = "Accepted";
                    }
                    else if (share_wallet_status.equalsIgnoreCase("2")) {
                        shareWalletModel.status = "Rejected";
                    }


                    boolean isAdded = false;

                    for (CardModel myuser: shareWalletList) {

                        if(myuser.user_id.equals(to_user_id))
                        {
                            isAdded = true;
                            break;
                        }

                    }

                    if(!isAdded)
                        shareWalletList.add(shareWalletModel);

                }

                if(from_user_id.equalsIgnoreCase(prefs.getString("user_id","")))
                {
                    showShareWalletDialog("share_wallet",shareWalletList, new Callback() {
                        @Override
                        public void Responce(String resp, String type, Bundle bundle) {

                        }
                    });
                }

            }
            else
            {
                if(shareWalletDialog != null)
                    shareWalletDialog.dismiss();

                shareWalletButtonVisible();
            }

        }
        else if(code.equalsIgnoreCase("403"))
        {
            ExitFromGames();
        }
        else {
            game_id = jsonObject.optString("active_game_id");
            //            Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
        }

        JSONArray pointsArray = jsonObject.optJSONArray("points");


        pointUserlist = new ArrayList<>();
        if(pointsArray != null)
        {
            for (int i = 0; i < pointsArray.length() ; i++) {
                JSONObject pointObject = pointsArray.getJSONObject(i);
                Usermodel usermodel = new Usermodel();
                usermodel.userid = pointObject.getString("user_id");
                usermodel.userName = pointObject.getString("name");
                boolean isUserAlreadyAdded = false;
                if(pointUserlist.size() > 0)
                {
                    for (Usermodel model: pointUserlist) {

                        if(model.userid.equals(usermodel.userid))
                            isUserAlreadyAdded = true;

                    }
                }

                if(!isUserAlreadyAdded)
                    pointUserlist.add(usermodel);
            }


            for (int i = 0; i <pointUserlist.size(); i++) {
                Usermodel usermodel = pointUserlist.get(i);
                String user_id = usermodel.userid;
                usermodel.pointlist = new ArrayList<>();
                for (int k = 0; k < pointsArray.length() ; k++){
                    JSONObject pointObject = pointsArray.getJSONObject(k);
                    String point_user_id = pointObject.getString("user_id");
                    Usermodel pointModel = new Usermodel();
                    if(point_user_id.equals(user_id))
                    {
//                                pointModel.user_points = pointObject.getString("points");
                        pointModel.user_points = pointObject.getString("total_points");
                        usermodel.pointlist.add(pointModel);
                    }

                }

            }



            gameUsersPoint_bt = new GameUsersPoint_bt(pointUserlist,game_id);

        }

        if(pointUserlist != null)
        {
            for (int i = 0; i < pointUserlist.size(); i++) {

                Usermodel model = pointUserlist.get(i);
                if(model.userid.equals(prefs.getString("user_id", "")))
                {

                    int point = 0;

                    String user_point = model.pointlist.get(model.pointlist.size()-1).user_points;

                    if(Functions.checkisStringValid(user_point));
                    point = Integer.parseInt(user_point);

                    if (pool_game.equals("101")){
                        if(point > 101)
                        {
                            if(!isPayDialogShow)
                            {
                                userLoose();
//                            API_CALL_rejoin_game_amount();
                            }
                        }
                    }
                    if (pool_game.equals("201")){
                        if(point > 201)
                        {
                            if(!isPayDialogShow)
                            {
                                userLoose();
//                            API_CALL_rejoin_game_amount();
                            }
                        }
                    }


                    break;
                }
            }
        }


    }

    private void userLoose(){
        isPayDialogShow = true;
        Functions.SmartAlertDialog(context, "You Loose?", "", "Okay", "", new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ExitFromGames();
            }
        },3000);
    }

    private void ParseStartGameRespones(JSONArray start_game) throws JSONException {

        if(start_game != null && start_game.length() > 0 && !isgamestarted)
        {
            boolean isAllUserRejected = true;
            ArrayList <CardModel> startGamesList = new ArrayList<>();



            String from_user_id = "";
            for (int i = 0; i < start_game.length() ; i++) {
                JSONObject shareObject = start_game.getJSONObject(i);

                from_user_id = shareObject.optString("user_id");
                String to_user_id = shareObject.optString("to_user_id");
                String name = shareObject.optString("name");
                String to_name = shareObject.optString("to_name");
                String share_wallet_status = shareObject.optString("status");


                CardModel shareWalletModel = new CardModel();
                shareWalletModel.user_id = to_user_id;
                if(to_user_id.equalsIgnoreCase(user_id_player1))
                {

                    name = shareObject.optString("name");
                    String start_game_id = shareObject.optString("id");
                    share_wallet_status = shareObject.optString("status");

//                        boolean isAccepted = false;
//
//                        if(!drawnUniqueid.equalsIgnoreCase("1"))
//                        {
//                            isAccepted = SharePref.getInstance().getBoolean(
//                                    SharePref.isStartGameAccespted+drawnUniqueid,false);
//                        }

                    if(share_wallet_status.equalsIgnoreCase("0")
                            && !share_wallet_status.equalsIgnoreCase("2")
                        /*&& !isStartGameTimerStart*/
                        /*&& !isAccepted*/)
                    {
                        OpenStartGameDialog(name,start_game_id);
                    }

                    CardModel userStartModel = new CardModel();
                    userStartModel.user_name = name;
                    userStartModel.user_id = from_user_id;
                    userStartModel.status = "Send request";
                    boolean isAdded = false;

                    for (CardModel myuser: startGamesList) {

                        if(myuser.user_id.equals(user_id_player1))
                        {
                            isAdded = true;
                            break;
                        }

                    }

                    if(!isAdded)
                        startGamesList.add(userStartModel);

                }
                else {
                    CardModel myshareWallet = new CardModel();
                    myshareWallet.user_name = name;
                    myshareWallet.user_id = from_user_id;
                    myshareWallet.status = "Send request";
                    boolean isAdded = false;

                    for (CardModel myuser: startGamesList) {

                        if(myuser.user_id.equals(user_id_player1))
                        {
                            isAdded = true;
                            break;
                        }

                    }

                    if(!isAdded)
                        startGamesList.add(0,myshareWallet);


                }

                shareWalletModel.user_id = to_user_id;
                shareWalletModel.user_name = to_name;
                shareWalletModel.status = share_wallet_status;
                if(share_wallet_status.equalsIgnoreCase("0"))
                {
                    shareWalletModel.status = "Pending";
                    isAllUserRejected = false;
//                        startGameButtonGone();
                }
                else if (share_wallet_status.equalsIgnoreCase("1")) {
                    shareWalletModel.status = "Accepted";
                    isAllUserRejected = false;
//                        startGameButtonGone();
                }
                else if (share_wallet_status.equalsIgnoreCase("2")) {
                    shareWalletModel.status = "Rejected";
                }
                else {
                    shareWalletModel.status = "Send request";
                }


                boolean isAdded = false;

                for (CardModel myuser: startGamesList) {

                    if(myuser.user_id.equals(to_user_id))
                    {
                        isAdded = true;
                        break;
                    }

                }

                if(!isAdded)
                    startGamesList.add(shareWalletModel);


            }


            manageSentStartGameRequest(startGamesList);

            if(!isStartGameTimerStart)
            {
                startgameCountDown = new CountDownTimer(SECOND_30,1000)
                {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        isStartGameTimerStart = true;
                    }

                    @Override
                    public void onFinish() {
                        isStartGameTimerStart = false;
                        if(!isCardDrawn() && !isOngoing() && !isgamestarted)
                        {
                            startGameButtonVisible();
                        }
                        else{
                            startGameButtonGone();
                        }
                    }
                }.start();
            }


        }
        else
        {

            if(shareWalletDialog != null)
                shareWalletDialog.dismiss();

            removeUserStatus();

            if(!isCardDrawn() && !isOngoing() && !isgamestarted)
                startGameButtonVisible();
            else{
                startGameButtonGone();
            }
        }

    }

    private void rejectAllUserRequest() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < userModelArrayList.size() ; i++) {

                    String user_id = userModelArrayList.get(i).userid;

                    apiViewModel.CALL_API_DO_START_GAME(user_id,2,null);

                }
            }
        },SECOND_30);


    }

    private void removeUserStatus(){
        for (int i = 0; i < UserstatusIDS.length ; i++) {

            TextView tvUserStatus = findViewById(UserstatusIDS[i]);
            tvUserStatus.setText("");
            tvUserStatus.setVisibility(View.GONE);

        }
    }

    private void manageSentStartGameRequest(ArrayList<CardModel> startGamesList) {

        for (int i = 0; i < UserstatusIDS.length ; i++) {

            TextView userStatus = findViewById(UserstatusIDS[i]);
            userStatus.setVisibility(View.GONE);

            for (int j = 0; j < startGamesList.size() ; j++) {

                CardModel startGameUserModel = startGamesList.get(j);

                String user_id = startGameUserModel.user_id;

                if(user_id.equalsIgnoreCase(user_id_player1) && i == 0)
                {
                    setValueonTextView(userStatus,startGameUserModel.status);
                    removeUserSetData(startGamesList,user_id);
                    break ;
                }
                else if(user_id.equalsIgnoreCase(user_id_player2) && i == 1)
                {
                    setValueonTextView(userStatus,startGameUserModel.status);
                    removeUserSetData(startGamesList,user_id);
                    break;

                }
                else if(user_id.equalsIgnoreCase(user_id_player3) && i == 2)
                {
                    setValueonTextView(userStatus,startGameUserModel.status);
                    removeUserSetData(startGamesList,user_id);

                    break;

                }
                else if(user_id.equalsIgnoreCase(user_id_player4) && i == 3)
                {
                    setValueonTextView(userStatus,startGameUserModel.status);
                    removeUserSetData(startGamesList,user_id);

                    break ;

                }
                else if(user_id.equalsIgnoreCase(user_id_player5) && i == 4)
                {
                    setValueonTextView(userStatus,startGameUserModel.status);
                    removeUserSetData(startGamesList,user_id);

                    break ;

                }
                else if(user_id.equalsIgnoreCase(user_id_player6) && i == 5)
                {
                    setValueonTextView(userStatus,startGameUserModel.status);
                    removeUserSetData(startGamesList,user_id);

                    break ;

                }
                else if(user_id.equalsIgnoreCase(user_id_player7) && i == 6)
                {
                    setValueonTextView(userStatus,startGameUserModel.status);
                    removeUserSetData(startGamesList,user_id);

                    break ;
                }


            }

        }

    }

    private void removeUserSetData(ArrayList<CardModel> startGamesList,String remove_id){

        for (int i = 0; i < startGamesList.size() ; i++) {

            CardModel usermodel = startGamesList.get(i);

            String user_id = usermodel.user_id;
            if(user_id.equalsIgnoreCase(remove_id))
                startGamesList.remove(usermodel);

        }

    }

    private void setValueonTextView(TextView textView,String status)
    {
        textView.setVisibility(View.VISIBLE);
        textView.setText(""+status);
    }


    boolean isStartGameTimerStart = false;
    private void OpenStartGameDialog(String name ,String share_wallet_id){

        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_slide_show);

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(dialog.isShowing())
            return;

        dialog.show();
        Functions.setDialogParams(dialog);

        ((TextView) dialog.findViewById(R.id.tvHeading)).setText("Do you want to start Game ? ");
        ((TextView) dialog.findViewById(R.id.txtSlidshow)).setText(name +" request you to start game!");

        dialog.findViewById(R.id.imgaccespt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePref.getInstance().putBoolean(
                        SharePref.isStartGameAccespted+table_id,true);
                apiViewModel.CALL_API_DO_START_GAME(share_wallet_id, 1, new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {
                        startGameButtonGone();
                        API_CALL_status();
                    }
                });
                slideshowTimer.cancel();
                dialog.dismiss();
                isStartGameTimerStart = false;
            }
        });

        dialog.findViewById(R.id.imgclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideshowTimer.cancel();
                slideshowTimer.onFinish();
                dialog.dismiss();
                isStartGameTimerStart = false;

            }
        });

        if(!isStartGameTimerStart)
        {
            slideshowTimer = new CountDownTimer(SECOND_30,1000)
            {

                @Override
                public void onTick(long millisUntilFinished) {
                    isStartGameTimerStart = true;
                    ((TextView) dialog.findViewById(R.id.tvSlideCountDown)).setText((millisUntilFinished/1000)+" s");

                }

                @Override
                public void onFinish() {
                    isStartGameTimerStart = false;
                    startGameButtonVisible();
                    apiViewModel.CALL_API_DO_START_GAME(share_wallet_id,2,null);
                    API_CALL_status();
                    dialog.dismiss();
                }
            }.start();
        }


    }

    String showChatID = "";
    String showchatMessage = "";
    private void ParseChatingResponse(JSONArray chat_list) throws JSONException {

        String storeChatId = SharePref.getInstance().getString(SharePref.storeChatID,"");

        if(chat_list != null && chat_list.length() > 0)
        {
            showChatID = "";
            showchatMessage = "";
            JSONObject jsonObject = chat_list.getJSONObject(0);

            showChatID = jsonObject.getString("id");
            String user_id = jsonObject.getString("user_id");
//            String rummy_table_id = jsonObject.getString("rummy_table_id");
            showchatMessage = jsonObject.getString("chat");
            String to_user_id = jsonObject.getString("to_user_id");

//                if(!storeChatId.equals(showChatID) && !user_id.equals(SharePref.getU_id()))
//                    showChatPopUP(user_id);
        }

    }

    private void showChatPopUP(String chat_user_id){

        View showMessageView = null;

        if (chat_user_id.equals(user_id_player1)) {

            showMessageView = findViewById(R.id.lnruserdetails1);

        } else if (chat_user_id.equals(user_id_player2)) {

            showMessageView = findViewById(R.id.lnruserdetails2);

        }
        else if (chat_user_id.equals(user_id_player3)) {
            showMessageView = findViewById(R.id.lnruserdetails3);

        }
        else if (chat_user_id.equals(user_id_player4)) {
            showMessageView = findViewById(R.id.lnruserdetails4);


        }
        else if (chat_user_id.equals(user_id_player5)) {


            showMessageView = findViewById(R.id.lnruserdetails5);

        }
        else if (chat_user_id.equals(user_id_player6)) {

            showMessageView = findViewById(R.id.lnruserdetails6);


        }
        else if (chat_user_id.equals(user_id_player7)) {

            showMessageView = findViewById(R.id.lnruserdetails7);


        }


        if(showMessageView!=null)
            showMessagePopup(showchatMessage,showMessageView);
    }

    private void ParseTableDetails(JSONObject tableObject) {

        if (tableModel==null)
            tableModel = new TableModel();

        tableModel.setId(tableObject.optString("id",""));
        tableModel.setBootValue(tableObject.optString("boot_value","0"));
        tableModel.setMaximumBlind(tableObject.optString("maximum_blind","0"));
        tableModel.setChaalLimit(tableObject.optString("chaal_limit","0"));
        tableModel.setPotLimit(tableObject.optString("pot_limit","0"));
        tableModel.setOnlineMembers(tableObject.optString("online_members","0"));
        tableModel.setPoint_value(tableObject.optString("point_value","0"));
        tableModel.setMax_player(tableObject.optString("max_player","0"));
        tableModel.setTable_name(tableObject.optString("name","0"));
        tableModel.setWinning_amount(tableObject.optString("winning_amount","0"));
        tableModel.setFounder_id(tableObject.optString("founder_id","0"));
        tableModel.setInvitation_code(tableObject.optString("invitation_code","0"));
        tableModel.setPassword(tableObject.optString("password","0"));
        tableModel.setViewer_status(tableObject.optString("viewer_status","0"));
        tableModel.setTableType(tableObject.optString("private","0"));


        if(Functions.isStringValid(tableModel.getPassword()))
        {
            getTextView(R.id.tvPassword).setVisibility(View.VISIBLE);
            getTextView(R.id.tvPassword).setText("Private Table Code Password : "+tableModel.getPassword());
        }

        if(tableModel.getBootValue().equals("0"))
        {
            isPracticeGame = true;
        }

    }

    private void ExitFromGames(){
        API_CALL_leave_table("0");
        StopSound();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        },500);
    }

    boolean isPayDialogShow = false;

    private void InitializeUsers
            (String user_id_player,JSONObject table_users,TextView txtPlay,TextView txtPlaywallet,ImageView imgplcircle,View lnrPlaywallet)
            throws JSONException {

        String table_id1 = table_users.getString(
                "table_id");
        final String name1 = table_users.getString(
                "name");
        user_id_player = table_users.getString(
                "user_id");
        String profile_pic1 = table_users.getString("profile_pic");
        String walletplayer2 = table_users.getString(
                "wallet");

        String invested = table_users.optString(
                "invested","0");


        if (user_id_player.equals("0")) {

            txtPlay.setText("");
            txtPlaywallet.setVisibility(View.INVISIBLE);
            lnrPlaywallet.setVisibility(View.INVISIBLE);

            String uriuser2 = "@drawable/avatar";  // where myresource
            // " +
            int imageResourceuser2 = getResources().getIdentifier(uriuser2,
                    null,
                    getPackageName());

            Picasso.get().load(imageResourceuser2).into(imgplcircle);
        }
        else {

            txtPlay.setText(name1);
            txtPlaywallet.setVisibility(View.VISIBLE);
            lnrPlaywallet.setVisibility(View.VISIBLE);

            txtPlaywallet.setText("" + Functions.formateAmount(walletplayer2));

            Picasso.get()
                    .load(Const.IMGAE_PATH + profile_pic1)
//                    .error(Functions.getDrawable(context,R.drawable.update_background_profile))
//                    .placeholder(Functions.getDrawable(context,R.drawable.update_background_profile))
                    .into(imgplcircle);

            String drawn_card = table_users.getString("card");
            CardModel drawCardModel = new CardModel();
            drawCardModel.user_id = user_id_player;
            drawCardModel.Image = drawn_card;
            if(Functions.checkisStringValid(drawn_card))
                drawCardsList.add(drawCardModel);

        }

    }

    int countvaue = 0;
    CountDownTimer cardDistrubutionCount;
    boolean isRunning = false;
    private void Parse_response(JSONObject jsonObject) throws JSONException {

        String code = jsonObject.optString("code");
        String message = jsonObject.optString("message");
        cardModelArrayList.clear();
        countvaue = 0;
        if(code.equalsIgnoreCase("200"))
        {
            findViewById(R.id.iv_rightarrow).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_leftarrow).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_rightarrow).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
            findViewById(R.id.iv_leftarrow).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));


            JSONArray cardsArray = jsonObject.optJSONArray("cards");

            if(cardsArray != null && cardsArray.length() > 0)
            {
                findViewById(R.id.rlt_dropgameview).setVisibility(View.GONE);
                isgamestarted = true;
                isFirstGameStart = true;

                for (int i = 0; i < cardsArray.length() ; i++) {

                    JSONObject cardObject = cardsArray.getJSONObject(i);
                    CardModel model = new CardModel();
                    //                    model.card_id = cardObject.optString("id");

                    model.Image = cardObject.optString("card");

                    if(model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
                    {
                        model.Image = removeLastChars(model.Image,1);
                    }
                    //                    Toast.makeText(context, ""+removeLastChars(model.Image,1), Toast.LENGTH_SHORT).show();

                    model.card_id = cardObject.optString("card");

//                        addCardsBahar(""+ model.Image,i);


                    model.card_group = cardObject.optString("card_group");

                    cardModelArrayList.add(model);
                }

                if(cardsArray.length() > RUMMY_TOTALE_CARD)
                    isCardPick = true;

                bt_sliptcard.setVisibility(View.GONE);
//                SplitCardtoGroup();

                if(!isRunning)
                {
                    rlt_addcardview.removeAllViews();
                    cardDistrubutionCount = new CountDownTimer(cardModelArrayList.size() * Variables.cardDistributionInterverl,
                            Variables.cardDistributionInterverl) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            isRunning = true;
                            startGameCardDrawnAnimation();

                        }

                        @Override
                        public void onFinish() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgCardsandar.setVisibility(View.GONE);
                                    isRunning = false;
                                    addCardsinMainList();
                                }
                            },500);
                        }
                    }.start();
                }

            }
            else {
                ((TextView)findViewById(R.id.tv_gamemessage)).setText(""+getString(R.string.cards_getting_error));
                findViewById(R.id.rlt_dropgameview).setVisibility(View.VISIBLE);
            }

        }

    }

    private void addCardsinMainList(){

        int nextCardFlipDelay = 300;
        countvaue = 0 ;
        new CountDownTimer(cardModelArrayList.size() * nextCardFlipDelay , nextCardFlipDelay){

            @Override
            public void onTick(long millisUntilFinished) {

                try {
                    AllCardFlipAnimation(cardModelArrayList.get(countvaue).Image,countvaue);
                    countvaue++;
                }
                catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SplitCardtoGroup();
                    }
                }, 1500);
            }
        }.start();


    }

    private void AllCardFlipAnimation(String image_card, int card_count){

        View view = rlt_addcardview.getChildAt(card_count);

        if(view == null)
            return;

        ImageView imgcards = view.findViewById(R.id.imgcard);

        String imagename = image_card;
        if(image_card.contains("id")) {
            imagename = image_card.substring(11);
        }

        int imageResource = Functions.GetResourcePath(imagename,this);

        view.setTag(image_card+"");


        TranslateLayout(imgcards, imageResource);

    }


    private void startGameCardDrawnAnimation(){
        imgCardsandar.setVisibility(View.VISIBLE);


        final View fromView;

        fromView = ivpickcard;

        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

//        imgCardsandar.setX(startX);
//        imgCardsandar.setY(startY);

        int chieldcount =   getCardListSize();
        View view = null;
        if (chieldcount > 0){
            view = getListLastCard();
        }
        else {
//            view = rlt_addcardview.getChildAt(chieldcount);
            view = findViewById(R.id.animationview);
        }




        int[] locationimgandar = new int[2];
        int[] locationlnrandar = new int[2];
        imgCardsandar.getLocationOnScreen(locationimgandar);
        rlt_addcardview.getLocationOnScreen(locationlnrandar);

        //----------------------For Width ----------------
        int[] locationimgandarwidth = new int[2];
        int[] locationlnrandarwidth = new int[2];
        imgCardsandar.getLocationOnScreen(locationimgandarwidth);
        if (view != null){
            view.getLocationOnScreen(locationlnrandarwidth);
        }else {

        }

        TranslateAnimation animation = null;
        int finalwidht = 0;
        int finalheight = 0;

        if (locationimgandarwidth[0] < 2){

        }else {

            finalwidht = locationlnrandarwidth[0]-locationimgandarwidth[0] ;
            finalheight = locationlnrandar[1] - locationimgandar[1] ;

            Functions.LOGE("Rummy5Player","finalwidht : "+finalwidht+" / toyDelta : "+(locationlnrandar[1] - locationimgandar[1]));

            if (finalwidht > 0) {
            }

            int animationDuration = 100;

//            if(countvaue > 1)
//                animationDuration += 100;

            animation = new TranslateAnimation(0, finalwidht,0 , finalheight+50);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setRepeatMode(0);
            animation.setDuration(animationDuration);
            animation.setFillAfter(true);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    PlaySaund(R.raw.teenpatticardflip_android);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    addCardsBahar(cardModelArrayList.get(countvaue).Image,countvaue);
//                    addCardtoList(cardModelArrayList.get(countvaue).Image,countvaue);
//                    countvaue++;
                    imgCardsandar.clearAnimation();
                    imgCardsandar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            imgCardsandar.startAnimation(animation);

            try {
                addCardtoList(cardModelArrayList.get(countvaue).Image,countvaue);
                countvaue++;
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }


        }


    }

    private void addCardtoList(String image_card , final int countnumber){

        View view = LayoutInflater.from(this).inflate(R.layout.item_card,  null);
        ImageView imgcards = view.findViewById(R.id.imgcard);
        view.setTag(image_card+"");

        final ImageView iv_jokercard = view.findViewById(R.id.iv_jokercard);
        iv_jokercard.setVisibility(View.GONE);


        int valueInPixels = (int) getResources().getDimension(R.dimen.margin_left);
        int left_margin = 0;
        if(countnumber == 0)
        {
            left_margin = (int) convertDpToPixel(valueInPixels,this);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(left_margin, (int) cardTopMargin,
                (int) right_margin, (int) convertDpToPixel(10,this));
        ViewGroup.LayoutParams params = rlt_addcardview.getLayoutParams();


        if (countnumber==0){
            params.width = (int) convertDpToPixel(140,this);
        }
        else if (countnumber == 1)
        {
            params.width = (int) convertDpToPixel(160,this);
        }
        else {
            params.width = (int) convertDpToPixel(90,this) * countnumber;
        }

        rlt_addcardview.setLayoutParams(params);
        rlt_addcardview.requestLayout();
        rlt_addcardview.addView(view,layoutParams);

    }


    private int getCardListSize() {
        return rlt_addcardview.getChildCount();
    }

    private View getListLastCard(){
        return rlt_addcardview.getChildAt(getCardListSize() - 1);
    }


    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }


    private void CreateGroupFromSelect(boolean isDrag){

        int count = 0;

        for (int i = 0; i < selectedcardvalue.size() ; i++) {
            Functions.LOGE("MainActvity","\n"+selectedcardvalue.get(i).toString());
            CardModel model = selectedcardvalue.get(i);
            if(model.isSelected)
            {
                count++;
            }

        }

        if(count < 3 && !isDrag)
        {
            Toast.makeText(this, ""+getString(R.string.minimum_grouping), Toast.LENGTH_SHORT).show();
            return;
        }


        for (int i = 0; i < selectedcardvalue.size() ; i++) {

            CardModel model = selectedcardvalue.get(i);

            RemoveCardFromArrayLists(model.card_id);

        }

        if(ext_group1.size() == 0){
            ext_group1.addAll(selectedcardvalue);
        }
        else if(ext_group2.size() == 0){
            ext_group2.addAll(selectedcardvalue);
        }
        else if(ext_group3.size() == 0){
            ext_group3.addAll(selectedcardvalue);
        }
        else if(ext_group4.size() == 0){
            ext_group4.addAll(selectedcardvalue);
        }
        else if(ext_group5.size() == 0){
            ext_group5.addAll(selectedcardvalue);
        }

        else if(rs_cardlist_group1.size() == 0)
        {
            rs_cardlist_group1.addAll(selectedcardvalue);
        }
        else if(rp_cardlist_group2.size() == 0)
        {
            rp_cardlist_group2.addAll(selectedcardvalue);
        }
        else if(bl_cardlist_group3.size() == 0)
        {
            bl_cardlist_group3.addAll(selectedcardvalue);
        }
        else if(bp_cardlist_group4.size() == 0)
        {
            bp_cardlist_group4.addAll(selectedcardvalue);
        }
        else if(joker_cardlist_group5.size() == 0)
        {
            joker_cardlist_group5.addAll(selectedcardvalue);
        }

        ArrayList<ArrayList<CardModel>> templist = new ArrayList<>();

        if(rs_cardlist_group1.size() > 0)
        {
            templist.add(rs_cardlist_group1);
        }

        if(rp_cardlist_group2.size() > 0)
        {
            templist.add(rp_cardlist_group2);
        }

        if(bl_cardlist_group3.size() > 0)
        {
            templist.add(bl_cardlist_group3);
        }

        if(bp_cardlist_group4.size() > 0)
        {
            templist.add(bp_cardlist_group4);
        }

        if(joker_cardlist_group5.size() > 0)
        {
            templist.add(joker_cardlist_group5);
        }

        if(ext_group1.size() > 0)
        {
            templist.add(ext_group1);
        }

        if(ext_group2.size() > 0)
        {
            templist.add(ext_group2);
        }

        if(ext_group3.size() > 0)
        {
            templist.add(ext_group3);
        }

        if(ext_group4.size() > 0)
        {
            templist.add(ext_group4);
        }

        if(ext_group5.size() > 0)
        {
            templist.add(ext_group5);
        }



        if(temp_grouplist != null)
            temp_grouplist.clear();

        temp_grouplist = templist;
        API_CALL_Sort_card_value();


    }

    private void API_CALL_Sort_card_value() {

        AddSplit_to_layout();

    }

    private String getColorCode(String card_name){
        return card_name.substring(0, 2);
    }

    private void API_CALL_drop_card(final ArrayList<CardModel> arrayList, final int countnumber) {

        if(!isMyChaal)
        {
            Toast.makeText(context, ""+getString(R.string.chaal_error_messsage), Toast.LENGTH_SHORT).show();

//                if(isViewonTouch)
//                {
            setTouchViewVisible(true);
            ResetCardtoPosition(arrayList,countnumber);
//                }

            return;
        }

        if(!isCardPick)
        {
            Functions.showToast(context,getString(R.string.please_pick_and_drop_card));

            setTouchViewVisible(true);
            ResetCardtoPosition(arrayList,countnumber);
            isFinishDesk = false;
            return;
        }

        cancelSound();
        isResentCardDrop = true;
        removeCardFromGroup(selectedpatti_id);
        isMyChaal = false;

        HashMap params = new HashMap();


        //        if(selectedpatti.substring(selectedpatti.length() - 1).equalsIgnoreCase("_"))
        //        {
        //            selectedpatti = removeLastChars(selectedpatti,1);
        //        }

        params.put("card",""+selectedpatti_id);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();
        params.put("json",""+Submitcardslist);

        //        RemoveCardFromArray();




        ApiRequest.Call_Api(this, Const.Rummypooldrop_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        isViewonTouch = false;
                        isCardPick = false;
                        bt_discard.setVisibility(View.GONE);
//                            removeCardFromGroup(selectedpatti_id);


                    }
                    else {



                        restoreRemoveCard();

//                            setTouchViewVisible(true);
//                            ResetCardtoPosition(arrayList, countnumber);

                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    if(isViewonTouch)
                    {
                        setTouchViewVisible(true);
                        isViewonTouch = false;

                        restoreRemoveCard();
                        ResetCardtoPosition(arrayList,countnumber);
                    }
                }

                API_CALL_status();

            }
        });


        //        cardListAdapter.notifyDataSetChanged();

    }

    private void setTouchViewVisible(boolean visible){

        if(_view != null)
            _view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

    }

    private void removeCardFromGroup(String selectedpatti_id) {

        if (ext_group1.size() > 0) {
            RemoveCard(selectedpatti_id, ext_group1);
        }

        if (ext_group2.size() > 0) {
            RemoveCard(selectedpatti_id, ext_group2);
        }

        if (ext_group3.size() > 0) {
            RemoveCard(selectedpatti_id, ext_group3);
        }

        if (ext_group4.size() > 0) {
            RemoveCard(selectedpatti_id, ext_group4);
        }

        if (ext_group5.size() > 0) {
            RemoveCard(selectedpatti_id, ext_group5);
        }


        if (rs_cardlist_group1.size() > 0) {
            RemoveCard(selectedpatti_id, rs_cardlist_group1);
        }

        if (rp_cardlist_group2.size() > 0) {
            RemoveCard(selectedpatti_id, rp_cardlist_group2);
        }

        if (bl_cardlist_group3.size() > 0) {
            RemoveCard(selectedpatti_id, bl_cardlist_group3);
        }

        if (bp_cardlist_group4.size() > 0) {
            RemoveCard(selectedpatti_id, bp_cardlist_group4);
        }

        if (joker_cardlist_group5.size() > 0) {
            RemoveCard(selectedpatti_id, joker_cardlist_group5);
        }

//                            AddSplit_to_layout();

        GetGroupValueFromTouch(animation_type);

    }

    private boolean getCardApiCalling = false;
    private void API_CALL_get_card() {

        if(getCardApiCalling)
            return;

        getCardApiCalling = true;



        CardModel dummy_card_model = new CardModel();
        dummy_card_model.card_id = DUMMY_CARD;
        dummy_card_model.Image = DUMMY_CARD;
        AddCardinEmptyList(dummy_card_model);

//                            AddSplit_to_layout();
        animation_type = "pick";
        GetGroupValueFromTouch(animation_type);


        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.Rummypoolget_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        isResentCardDrop = false;
                        isCardPick = true;

                        rlt_addcardview.removeAllViews();
                        JSONArray drop_card = jsonObject.optJSONArray("card");
                        JSONObject cardObject = drop_card.getJSONObject(0);
                        String card = cardObject.getString("cards");
                        String card_id = cardObject.optString("id");


                        CardModel model = new CardModel();
                        model.Image = card;

                        if(model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
                        {
                            model.Image = removeLastChars(model.Image,1);
                        }

                        model.card_id = card;

                        RemoveCardFromArrayLists(DUMMY_CARD);
                        AddCardinEmptyList(model);


//                            AddSplit_to_layout();


                    }
                    else {
                    }


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    getCardApiCalling = false;
                    RemoveCardFromArrayLists(DUMMY_CARD);
                }

                RemoveCardFromArrayLists(DUMMY_CARD);
                animation_type = "reset_card";
                GetGroupValueFromTouch(animation_type);
                getCardApiCalling = false;
                API_CALL_status();


            }
        });



        //        cardListAdapter.notifyDataSetChanged();

    }

    private void API_CALL_get_drop_card() {

        Submitcardslist = GetCardFromLayout();

        apiViewModel.CALL_API_get_drop_card(Submitcardslist, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        isCardPick = true;
                        rlt_addcardview.removeAllViews();
                        JSONArray drop_card = jsonObject.optJSONArray("card");
                        JSONObject cardObject = drop_card.getJSONObject(0);
                        String card = cardObject.getString("card");
                        String card_id = cardObject.optString("id");

                        CardModel model = new CardModel();
                        model.Image = card;

                        if(model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
                        {
                            model.Image = removeLastChars(model.Image,1);
                        }

                        model.card_id = card;

                        AddCardinEmptyList(model);

                        animation_type = "drop_pick";
                        GetGroupValueFromTouch(animation_type);
//                            AddSplit_to_layout();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void AddCardinEmptyList(CardModel model){
        if (ext_group1.size() == 0) {
            ext_group1.add(model);
        } else if (ext_group2.size() == 0) {
            ext_group2.add(model);
        } else if (ext_group3.size() == 0) {
            ext_group3.add(model);
        } else if (ext_group4.size() == 0) {
            ext_group4.add(model);
        } else if (ext_group5.size() == 0) {
            ext_group5.add(model);
        }

        else if (rs_cardlist_group1.size() == 0) {
            rs_cardlist_group1.add(model);
        } else if (rp_cardlist_group2.size() == 0) {
            rp_cardlist_group2.add(model);
        } else if (bl_cardlist_group3.size() == 0) {
            bl_cardlist_group3.add(model);
        } else if (bp_cardlist_group4.size() == 0) {
            bp_cardlist_group4.add(model);
        } else if (joker_cardlist_group5.size() == 0) {
            joker_cardlist_group5.add(model);
        }
    }

    private void RemoveCard(String card_value,ArrayList<CardModel> modelArray){

        for (int i = 0; i < modelArray.size() ; i++) {
            CardModel model = modelArray.get(i);
            if (model.card_id.equalsIgnoreCase(card_value))
            {
                removemodel = model;
                removeCardList = new ArrayList<>(modelArray);
                modelArray.remove(model);
                Functions.LOGE("RemoveCard","card_id : "+card_value);
            }

        }

    }

    private void restoreRemoveCard(){

        outer :
        for (int i = 0; i < removeCardList.size() ; i++) {

            CardModel removemodel = removeCardList.get(i);

            boolean found = false;
            for (CardModel model : ext_group1) {
                if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                    found = true;
                    break;
                }
            }

            if (found)
            {
                ext_group1.clear();
                ext_group1.addAll(removeCardList);
                break outer;
            }
            else
            {
                for (CardModel model : ext_group2) {
                    if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                        found = true;
                        break;
                    }
                }

                if (found)
                {
                    ext_group2.clear();
                    ext_group2.addAll(removeCardList);
                    break outer;
                }
                else
                {

                    for (CardModel model : ext_group3) {
                        if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                            found = true;
                            break;
                        }
                    }

                    if (found)
                    {
                        ext_group3.clear();
                        ext_group3.addAll(removeCardList);
                        break outer;
                    }
                    else
                    {

                        for (CardModel model : ext_group4) {
                            if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                                found = true;
                                break;
                            }
                        }


                        if (found)
                        {
                            ext_group4.clear();
                            ext_group4.addAll(removeCardList);
                            break outer;
                        }
                        else
                        {
                            for (CardModel model : ext_group5) {
                                if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                                    found = true;
                                    break ;
                                }
                            }


                            if (found)
                            {
                                ext_group5.clear();
                                ext_group5.addAll(removeCardList);
                                break outer;
                            }
                            else
                            {
                                for (CardModel model : rs_cardlist_group1) {
                                    if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                                        found = true;
                                        break;
                                    }
                                }


                                if (found)
                                {
                                    rs_cardlist_group1.clear();
                                    rs_cardlist_group1.addAll(removeCardList);
                                    break outer;
                                }
                                else
                                {
                                    for (CardModel model : rp_cardlist_group2) {
                                        if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                                            found = true;
                                            break;
                                        }
                                    }


                                    if (found)
                                    {
                                        rp_cardlist_group2.clear();
                                        rp_cardlist_group2.addAll(removeCardList);
                                        break outer;
                                    }
                                    else
                                    {
                                        for (CardModel model : bl_cardlist_group3) {
                                            if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                                                found = true;
                                                break;
                                            }
                                        }


                                        if (found)
                                        {
                                            bl_cardlist_group3.clear();
                                            bl_cardlist_group3.addAll(removeCardList);
                                            break outer;
                                        }
                                        else
                                        {
                                            for (CardModel model : bp_cardlist_group4) {
                                                if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                                                    found = true;
                                                    break;
                                                }
                                            }


                                            if (found)
                                            {
                                                bp_cardlist_group4.clear();
                                                bp_cardlist_group4.addAll(removeCardList);
                                                break outer;
                                            }
                                            else
                                            {
                                                for (CardModel model : joker_cardlist_group5) {
                                                    if (removemodel.card_id.equalsIgnoreCase(model.card_id)) {
                                                        found = true;
                                                        break;
                                                    }
                                                }


                                                if (found)
                                                {
                                                    joker_cardlist_group5.clear();
                                                    joker_cardlist_group5.addAll(removeCardList);
                                                    break outer;
                                                }
                                                else if(i == (removeCardList.size() - 1)) {

                                                    if(rs_cardlist_group1.size() == 0)
                                                    {
                                                        rs_cardlist_group1.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(rp_cardlist_group2.size() == 0)
                                                    {
                                                        rp_cardlist_group2.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(bl_cardlist_group3.size() == 0)
                                                    {
                                                        bl_cardlist_group3.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(bp_cardlist_group4.size() == 0)
                                                    {
                                                        bp_cardlist_group4.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(joker_cardlist_group5.size() == 0)
                                                    {
                                                        joker_cardlist_group5.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else
                                                    if(ext_group1.size() == 0){
                                                        ext_group1.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(ext_group2.size() == 0){
                                                        ext_group2.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(ext_group3.size() == 0){
                                                        ext_group3.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(ext_group4.size() == 0){
                                                        ext_group4.addAll(removeCardList);
                                                        break outer;
                                                    }
                                                    else if(ext_group5.size() == 0){
                                                        ext_group5.addAll(removeCardList);
                                                        break outer;
                                                    }

                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }

                }
            }


        }

        removeCardList.clear();
        animation_type = "restore_animation";
        GetGroupValueFromTouch(animation_type);
    }

    public void complategameUIChange(){

        Submitcardslist = GetCardFromLayout();

        rs_cardlist_group1.clear();
        rp_cardlist_group2.clear();
        bl_cardlist_group3.clear();
        bp_cardlist_group4.clear();
        joker_cardlist_group5.clear();
        ext_group1.clear();
        ext_group2.clear();
        ext_group3.clear();
        ext_group4.clear();
        ext_group5.clear();
        selectedcardvalue.clear();
        grouplist.clear();
        if(removeCardList != null)
            removeCardList.clear();
        rlt_addcardview.removeAllViews();
        bt_sliptcard.setVisibility(View.GONE);
        bt_drop.setVisibility(View.GONE);
        declareButtonGone();
        cardPref.edit().putString(Pref_cards+game_id,"").apply();
    }

    boolean isMyChaal = false;
    public void makeHightLightForChaal(String chaal_user_id) {

        if(chaal_user_id.equals(prefs.getString("user_id","")))
        {

            isMyChaal = true;

            if(!opponent_game_declare)
                setDeclareButton();

//                findViewById(R.id.rlt_highlighted_pick).setVisibility(View.VISIBLE);
            findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.VISIBLE);
            findViewById(R.id.flUserHighlight).setVisibility(View.VISIBLE);
            findViewById(R.id.flUserHighlight).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));


//                findViewById(R.id.rlt_highlighted_pick).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
            findViewById(R.id.rlt_highlighted_gadhi).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));

        }
        else {
            isMyChaal = false;
            cancelSound();
            findViewById(R.id.flUserHighlight).setVisibility(View.INVISIBLE);
            findViewById(R.id.rlt_highlighted_pick).setVisibility(View.INVISIBLE);
            findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.INVISIBLE);

            findViewById(R.id.flUserHighlight).clearAnimation();
            findViewById(R.id.rlt_highlighted_pick).clearAnimation();
            findViewById(R.id.rlt_highlighted_gadhi).clearAnimation();


        }

        if (chaal_user_id.equals(user_id_player1)) {

            if (isProgressrun1) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer1.start();
                isProgressrun1 = false;
                PlaySaund(R.raw.player_chal_sound);
            }

            highlight("1");

        }
        else if (chaal_user_id.equals(user_id_player2)) {

            if (isProgressrun2) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer2.start();
                isProgressrun2 = false;
                PlaySaund(R.raw.buttontouchsound);

            }

            highlight("2");
        }
        else if (chaal_user_id.equals(user_id_player3)) {

            if (isProgressrun3) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer3.start();
                isProgressrun3 = false;
                PlaySaund(R.raw.buttontouchsound);

            }

            highlight("3");
        }
        else if (chaal_user_id.equals(user_id_player4)) {

            if (isProgressrun4) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer4.start();
                isProgressrun4 = false;
                PlaySaund(R.raw.buttontouchsound);

            }

            highlight("4");

        }
        else if (chaal_user_id.equals(user_id_player5)) {

            if (isProgressrun5) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer5.start();
                isProgressrun5 = false;
                PlaySaund(R.raw.buttontouchsound);

            }


            highlight("5");

        }
        else if (chaal_user_id.equals(user_id_player6)) {

            if (isProgressrun6) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer6.start();
                isProgressrun6 = false;
                PlaySaund(R.raw.buttontouchsound);

            }

            highlight("6");

        }
        else if (chaal_user_id.equals(user_id_player7)) {

            if (isProgressrun7) {

                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer7.start();
                isProgressrun7 = false;
                PlaySaund(R.raw.buttontouchsound);

            }

            highlight("7");

        }

    }

    public void highlight(String player){

        if(!player.equals("1"))
        {
            isProgressrun1 = true;

            Player1CancelCountDown();
            mProgress1.setProgress(0);
            imgpl1glow.setVisibility(View.GONE);
        }


        if(!player.equals("2"))
        {
            isProgressrun2 = true;
            mCountDownTimer2.cancel();
            mProgress2.setProgress(0);
            imgpl2glow.setVisibility(View.GONE);
        }


        if(!player.equals("3"))
        {
            isProgressrun3 = true;
            mCountDownTimer3.cancel();
            mProgress3.setProgress(0);
            imgpl3glow.setVisibility(View.GONE);
        }


        if(!player.equals("4"))
        {
            isProgressrun4 = true;
            mCountDownTimer4.cancel();
            mProgress4.setProgress(0);
            imgpl4glow.setVisibility(View.GONE);
        }


        if(!player.equals("5"))
        {
            isProgressrun5 = true;
            mCountDownTimer5.cancel();
            mProgress5.setProgress(0);
            imgpl5glow.setVisibility(View.GONE);
        }


        if(!player.equals("6"))
        {
            isProgressrun6 = true;
            mCountDownTimer6.cancel();
            mProgress6.setProgress(0);
            imgpl6glow.setVisibility(View.GONE);
        }


        if (!player.equals("7"))
        {
            isProgressrun7 = true;
            mCountDownTimer7.cancel();
            mProgress7.setProgress(0);
            imgpl7glow.setVisibility(View.GONE);
        }


    }

    private void Player1CancelCountDown(){
//            mCountDownTimer1.onFinish();
        mCountDownTimer1.cancel();
        checkMyCards();
    }


    public void makeWinnertoPlayer(String chaal_user_id) {
        mProgress1.setProgress(0);
        mProgress2.setProgress(0);
        mProgress3.setProgress(0);
        mProgress4.setProgress(0);
        mProgress5.setProgress(0);
        mCountDownTimer1.cancel();
        mCountDownTimer2.cancel();
        mCountDownTimer3.cancel();
        mCountDownTimer4.cancel();
        mCountDownTimer5.cancel();
        mCountDownTimer6.cancel();
        mCountDownTimer7.cancel();

        isProgressrun1 = true;
        isProgressrun2 = true;
        isProgressrun3 = true;
        isProgressrun4 = true;
        isProgressrun5 = true;
        isProgressrun6 = true;
        isProgressrun7 = true;


        PlaySaund(R.raw.tpb_battle_won);

        rltwinnersymble1.setVisibility(View.GONE);
        rltwinnersymble2.setVisibility(View.GONE);
        rltwinnersymble3.setVisibility(View.GONE);
        rltwinnersymble4.setVisibility(View.GONE);
        rltwinnersymble5.setVisibility(View.GONE);
        rltwinnersymble6.setVisibility(View.GONE);
        rltwinnersymble7.setVisibility(View.GONE);


        if (chaal_user_id.equals(user_id_player1)) {

            rltwinnersymble1.setVisibility(View.VISIBLE);

        } else if (chaal_user_id.equals(user_id_player2)) {


            rltwinnersymble2.setVisibility(View.VISIBLE);
        }
        else if (chaal_user_id.equals(user_id_player3)) {


            rltwinnersymble3.setVisibility(View.VISIBLE);
        }
        else if (chaal_user_id.equals(user_id_player4)) {


            rltwinnersymble4.setVisibility(View.VISIBLE);
        }
        else if (chaal_user_id.equals(user_id_player5)) {


            rltwinnersymble5.setVisibility(View.VISIBLE);
        }
        else if (chaal_user_id.equals(user_id_player6)) {


            rltwinnersymble6.setVisibility(View.VISIBLE);
        }
        else if (chaal_user_id.equals(user_id_player7)) {


            rltwinnersymble7.setVisibility(View.VISIBLE);
        }

    }


    public void cancelSound()
    {
        isTimeoutSoundPlay = false;
        soundMedia.StopSong();
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }


    public void StopSound(){

        cancelSound();

        if(mCountDownTimer1 != null)
        {
            Player1CancelCountDown();
        }

        if(mCountDownTimer2 != null)
        {
            mCountDownTimer2.cancel();
        }

        if(mCountDownTimer3 != null)
        {
            mCountDownTimer3.cancel();
        }

        if(mCountDownTimer4 != null)
        {
            mCountDownTimer4.cancel();
        }

        if(mCountDownTimer5 != null)
        {
            mCountDownTimer5.cancel();
        }

        if(mCountDownTimer6 != null)
        {
            mCountDownTimer6.cancel();
        }

        if(mCountDownTimer7 != null)
        {
            mCountDownTimer7.cancel();
        }

    }

    MediaPlayer mediaPlayer ;
    boolean isTimeoutSoundPlay = false;
    Sound soundMedia = null;
    public void PlaySaund(int saund) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");

        if(isTimeoutSoundPlay)
            return;

        if (value.equals("1")) {
            soundMedia.PlaySong(saund,false,context);

        } else {


        }

    }

    public void playCountDownSound(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");

        if(isTimeoutSoundPlay)
            return;

        if (value.equals("1")) {
            soundMedia.PlaySong(R.raw.teenpattitick,true,context);

        } else {


        }
    }

    ArrayList<ArrayList<CardModel>> temp_grouplist;
    private void SplitCardtoGroup(){
        rs_cardlist_group1.clear();
        rp_cardlist_group2.clear();
        bl_cardlist_group3.clear();
        bp_cardlist_group4.clear();
        joker_cardlist_group5.clear();
        ext_group1.clear();
        ext_group2.clear();
        ext_group3.clear();
        ext_group4.clear();
        ext_group5.clear();
        selectedcardvalue.clear();
        grouplist.clear();
        rlt_addcardview.removeAllViews();
        bt_sliptcard.setVisibility(View.GONE);
        isSplit = true;


        for (int i = 0; i < cardModelArrayList.size() ; i++) {

            String card_tag = cardModelArrayList.get(i).card_group;

            if(card_tag.equalsIgnoreCase("rs"))
            {
                rs_cardlist_group1.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("rp"))
            {
                rp_cardlist_group2.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("bl"))
            {
                bl_cardlist_group3.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("bp"))
            {
                bp_cardlist_group4.add(cardModelArrayList.get(i));
            }
            else if(card_tag.equalsIgnoreCase("jk"))
            {
                joker_cardlist_group5.add(cardModelArrayList.get(i));
            }

        }

        temp_grouplist = new ArrayList<>() ;


        if(rs_cardlist_group1.size() > 0)
        {
            temp_grouplist.add(rs_cardlist_group1);
        }

        if(rp_cardlist_group2.size() > 0)
        {
            temp_grouplist.add(rp_cardlist_group2);
        }

        if(bl_cardlist_group3.size() > 0)
        {
            temp_grouplist.add(bl_cardlist_group3);
        }

        if(bp_cardlist_group4.size() > 0)
        {
            temp_grouplist.add(bp_cardlist_group4);
        }

        if(joker_cardlist_group5.size() > 0)
        {
            temp_grouplist.add(joker_cardlist_group5);
        }


        API_CALL_Sort_card_value();


    }


    int grouplist_size  = 0;
    Bottom_listDialog bottom_listDialog;
    GameUsersPoint_bt gameUsersPoint_bt;
    private void AddSplit_to_layout(){
        isSplit = true;
        rlt_addcardview.removeAllViews();
        selectedcardvalue.clear();
        grouplist.clear();

//            if(!opponent_game_declare)
        isDeclareVisible = true;

        if(rs_cardlist_group1.size() > 0)
        {
            grouplist.add(rs_cardlist_group1);

            //            CreateGroups(rs_cardlist_group1);
        }

        if(rp_cardlist_group2.size() > 0)
        {
            grouplist.add(rp_cardlist_group2);

            //            CreateGroups(rp_cardlist_group2);
        }

        if(bl_cardlist_group3.size() > 0)
        {
            grouplist.add(bl_cardlist_group3);

            //            CreateGroups(bl_cardlist_group3);
        }

        if(bp_cardlist_group4.size() > 0)
        {
            grouplist.add(bp_cardlist_group4);

            //            CreateGroups(bp_cardlist_group4);
        }

        if(joker_cardlist_group5.size() > 0)
        {
            grouplist.add(joker_cardlist_group5);

            //            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group1.size() > 0)
        {
            grouplist.add(ext_group1);

            //            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group2.size() > 0)
        {
            grouplist.add(ext_group2);

            //            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group3.size() > 0)
        {
            grouplist.add(ext_group3);

            //            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group4.size() > 0)
        {
            grouplist.add(ext_group4);

            //            CreateGroups(bp_cardlist_group4);
        }

        if(ext_group5.size() > 0)
        {
            grouplist.add(ext_group5);

            //            CreateGroups(bp_cardlist_group4);
        }


        grouplist_size = grouplist.size();

        bottom_listDialog = new Bottom_listDialog(grouplist);

        gameUsersPoint_bt = new GameUsersPoint_bt();

        groupvalueList.clear();
        fistlife_position = -1;

        for (int i = 0; i < grouplist.size() ; i++) {
            card_count = 0;
            CreateGroups(grouplist.get(i),i);

        }

    }

    int card_count = 0;
    List<Integer> groupvalueList = new ArrayList<>();
    int fistlife_position = -1;
    private void CreateGroups(ArrayList<CardModel> cardImageList, int count) {

        final View view = LayoutInflater.from(this).inflate(R.layout.item_grouplayout,  null);
        view.setId(count);
        TextView tv_status = view.findViewById(R.id.tv_status);
        ImageView iv_status = view.findViewById(R.id.iv_status);
        RelativeLayout relativeLayout = view.findViewById(R.id.rlt_icons);
        LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);
        //        lnr_group_card.setId(count);
        RelativeLayout rltCardView = view.findViewById(R.id.rltCardView);
//        RelativeLayout rlt_icons = view.findViewById(R.id.rlt_icons);


        com.metagards.metagames._Rummy.GameLocalLogic gameLocalLogic = GameLocalLogic.getInstance(context);
        gameLocalLogic.setJokerCard(joker_card);

        cardImageList = gameLocalLogic.CardValue(cardImageList);

        if(getIntent().hasExtra("hide_hint")){
            relativeLayout.setVisibility(View.INVISIBLE);
        }else{
            relativeLayout.setVisibility(View.VISIBLE);
        }

        if(cardImageList.get(0).group_value.equalsIgnoreCase(INVALID))
            tv_status.setText(""+cardImageList.get(0).group_value+"("+cardImageList.get(0).group_points+")");
        else
            tv_status.setText(cardImageList.get(0).group_points);


        lnr_group_card.setTag(""+cardImageList.get(0).value_grp);
        groupvalueList.add(cardImageList.get(0).value_grp);

        if((grouplist.size()-1) == count)
        {
            str_1st_life="";
            str_2nd_life="";
            int first_check_ui = R.drawable.ic_uncheckbox;
            int second_check_ui = R.drawable.ic_uncheckbox;

            for (int i = 0; i < groupvalueList.size() ; i++) {
                int position = i;
                int value = groupvalueList.get(i);

                if(value == PURE_SEQUENCE_VALUE && fistlife_position == -1)
                {
                    first_check_ui = R.drawable.ic_checkbox;
                    fistlife_position = position;
                    str_1st_life = "1";
                }

                if(value == IMPURE_SEQUENCE_VALUE || (fistlife_position != -1 && fistlife_position != position && value == PURE_SEQUENCE_VALUE)){
                    second_check_ui = R.drawable.ic_checkbox;
                    str_2nd_life = "1";
                }

                ((ImageView)findViewById(R.id.ivFirstlifecheck))
                        .setImageDrawable(Functions.getDrawable(context,first_check_ui));

                ((ImageView)findViewById(R.id.ivSeconlifecheck))
                        .setImageDrawable(Functions.getDrawable(context,second_check_ui));

            }

        }

        String grou_json = ""+getGroupJson(cardImageList.get(0));
        Functions.LOGE("group_json",""+grou_json);

        if(count == 1)
        {
            view.findViewById(R.id.iv_arrow_left).setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_arrow_right).setVisibility(View.VISIBLE);
        }
        else
        {
            view.findViewById(R.id.iv_arrow_left).setVisibility(View.GONE);
            view.findViewById(R.id.iv_arrow_right).setVisibility(View.GONE);
        }

        int Imageresours = 0;
        if(cardImageList.get(0).group_value.equalsIgnoreCase(INVALID))
        {
            isDeclareVisible = false;
            Imageresours = Functions.GetResourcePath("invalid_circlebg",this);
        }
        else {
            Imageresours = Functions.GetResourcePath("valid_circlebg",this);
//                isDeclareVisible = true;
        }

        if(opponent_game_declare)
            isDeclareVisible = true;

        if(isDeclareVisible && (grouplist.size()-1) == count)
        {

            /* rules = atlease have one pure-seq and impure-seq or 2 pure-seq*/

            boolean isRulesMatch = false;
            DeclareRules declareRules = new DeclareRules(groupvalueList);

            isRulesMatch = declareRules.isRuleMatch();

            if(!isRulesMatch)
                return;

            if(isMyChaal)
            {
                if(!game_declare)
                    setDeclareButton();
            }
            else
                setGameEndDeclareButton();

            declareButtonVisible();

            if(isFinishDesk)
            {
                isFinishDesk = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        API_CALL_declare();
                    }
                },500);

            }
            else {
                if(isResentCardDrop)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            API_CALL_declare();
                        }
                    },500);
                }
            }

        }
        else
        {

            if(isFinishDesk && (grouplist.size()-1) == count)
            {
                isFinishDesk = false;
                Toast.makeText(context, "Finish Desk not allowed", Toast.LENGTH_SHORT).show();
            }

            declareButtonGone();
        }


        iv_status.setImageResource(Imageresours);


        for (int i = 0; i < cardImageList.size() ; i++) {

            AddCardtoGroup(cardImageList.get(i).Image,i,lnr_group_card,cardImageList);

        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
                //                Toast.makeText(MainActivity.this , tag, Toast.LENGTH_LONG).show();

            }
        });

        ArrayList<CardModel> finalCardImageList = cardImageList;
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                int action = event.getAction();
                View view1 = (View) event.getLocalState();

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        }
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.invalidate();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        v.invalidate();
                        break;

                    case DragEvent.ACTION_DROP:

                        //                        Toast.makeText(context, ""+view.getId(), Toast.LENGTH_SHORT).show();

                        if(group_id == v.getId())
                        {

                            Toast.makeText(context, ""+view.getId()+" / "+group_id, Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        CardModel model ;
                        selectedpatti = String.valueOf(_view.getTag());
                        selectedpatti_id = String.valueOf(_view.getTag());
                        Functions.LOGE("RummyGame","selectedpatti : "+selectedpatti+
                                "\n selectedpatti_id : "+selectedpatti_id);
                        RemoveCardFromArrayLists(selectedpatti_id);

                        if(removemodel != null)
                        {
                            model = removemodel;
                        }
                        else {
                            model = new CardModel();
                            model.Image = selectedpatti;
                            model.Image = selectedpatti_id;
                        }


                        finalCardImageList.add(model);

                        GetGroupValueFromTouch("Touch");

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                }
                return true;
            }
        });



        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int valueInPixels = (int) getResources().getDimension(R.dimen.margin_left_group);

        if(grouplist_size >= 5)
        {
            valueInPixels = 10 /** grouplist_size*/;
        }

        int leftmargin = 0;
        if(count == 0)
        {
            leftmargin = (int) convertDpToPixel(valueInPixels,this);
        }



        layoutParams.setMargins((int) leftmargin, (int) convertDpToPixel(0,this), 0, 0);

        ViewGroup.LayoutParams params = rlt_addcardview.getLayoutParams();


        rlt_addcardview.setLayoutParams(params);

        rlt_addcardview.addView(view,layoutParams);

    }

    private JSONObject getGroupJson(CardModel cardModel){
        JSONObject group_json = null;

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("card_value",""+cardModel.Image);
            jsonObject.put("card_id",""+cardModel.card_id);

            jsonObject.put("group_value_params",""+cardModel.group_value_params);
            jsonObject.put("group_value_response",""+cardModel.group_value_response);
            jsonObject.put("group_value",""+cardModel.group_value);
            jsonObject.put("group_points",""+cardModel.group_points);

            group_json = jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return group_json;
    }

    private void GetGroupValueFromTouch(String animationType){

        if(temp_grouplist != null)
            temp_grouplist.clear();
        else {
            temp_grouplist = new ArrayList<>();
        }


        if(rs_cardlist_group1.size() > 0)
        {
            temp_grouplist.add(rs_cardlist_group1);

        }

        if(rp_cardlist_group2.size() > 0)
        {
            temp_grouplist.add(rp_cardlist_group2);

        }

        if(bl_cardlist_group3.size() > 0)
        {
            temp_grouplist.add(bl_cardlist_group3);

        }

        if(bp_cardlist_group4.size() > 0)
        {
            temp_grouplist.add(bp_cardlist_group4);

        }

        if(joker_cardlist_group5.size() > 0)
        {
            temp_grouplist.add(joker_cardlist_group5);

        }

        if(ext_group1.size() > 0)
        {
            temp_grouplist.add(ext_group1);

        }

        if(ext_group2.size() > 0)
        {
            temp_grouplist.add(ext_group2);

        }

        if(ext_group3.size() > 0)
        {
            temp_grouplist.add(ext_group3);

        }

        if(ext_group4.size() > 0)
        {
            temp_grouplist.add(ext_group4);

        }

        if(ext_group5.size() > 0)
        {
            temp_grouplist.add(ext_group5);
        }


        animation_type = animationType;
        API_CALL_Sort_card_value();

    }

    CardModel removemodel;
    ArrayList<CardModel> removeCardList;
    private void RemoveCardFromArrayLists(String selectedpatti_id){

        if(ext_group1.size() > 0)
        {
            RemoveCard(selectedpatti_id,ext_group1);
        }

        if(ext_group2.size() > 0)
        {
            RemoveCard(selectedpatti_id,ext_group2);
        }

        if(ext_group3.size() > 0)
        {
            RemoveCard(selectedpatti_id,ext_group3);
        }

        if(ext_group4.size() > 0)
        {
            RemoveCard(selectedpatti_id,ext_group4);
        }

        if(ext_group5.size() > 0)
        {
            RemoveCard(selectedpatti_id,ext_group5);
        }


        if(rs_cardlist_group1.size() > 0)
        {
            RemoveCard(selectedpatti_id,rs_cardlist_group1);
        }

        if(rp_cardlist_group2.size() > 0)
        {
            RemoveCard(selectedpatti_id,rp_cardlist_group2);
        }

        if(bl_cardlist_group3.size() > 0)
        {
            RemoveCard(selectedpatti_id,bl_cardlist_group3);
        }

        if(bp_cardlist_group4.size() > 0)
        {
            RemoveCard(selectedpatti_id,bp_cardlist_group4);
        }

        if(joker_cardlist_group5.size() > 0)
        {
            RemoveCard(selectedpatti_id,joker_cardlist_group5);
        }


    }

    private final int right_margin = Functions.getDimens(R.dimen.card_right_margin);
    private final int cardTopMargin = Functions.getDimens(R.dimen.card_top_margin);

    String animation_type = "normal";
    private void AddCardtoGroup(String image_card , final int countnumber, final LinearLayout addlayout,
                                final ArrayList<CardModel> arrayList) {

        View view = LayoutInflater.from(this).inflate(R.layout.item_card,  null);
        ImageView imgcards = view.findViewById(R.id.imgcard);
        //        view.setTag(image_card+"");
        view.setTag (arrayList.get(countnumber).card_id+"");


        final ImageView iv_jokercard = view.findViewById(R.id.iv_jokercard);

        String src_joker_cards = "";
        src_joker_cards = joker_card.substring(joker_card.length() - 1);

        if(src_joker_cards != null && !src_joker_cards.equals(""))
        {
            if(src_joker_cards.contains(image_card.substring(image_card.length() - 1)))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_jokercard.setVisibility(View.VISIBLE);
                    }
                },4000);

            }
            else {
                iv_jokercard.setVisibility(View.GONE);
            }
        }

        if(selectedcardvalue.size() == 1 && !opponent_game_declare)
            bt_discard.setVisibility(View.VISIBLE);
        else
            bt_discard.setVisibility(View.GONE);

        if(selectedcardvalue.size() >= 2)
            bt_creategroup.setVisibility(View.VISIBLE);
        else
            bt_creategroup.setVisibility(View.GONE);


        cardActionHandler(view,addlayout,arrayList,countnumber);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onGroupsCardClick(view,arrayList,countnumber);

            }
        });


        String imagename = image_card;
        if(image_card.contains("id")) {
            imagename = image_card.substring(11);
        }

        int imageResource = Functions.GetResourcePath(imagename,this);

        if(imageResource > 0)
            Picasso.get().load(imageResource).into(imgcards);


        if(animation_type.equals("normal"))
        {
//            TranslateLayout(imgcards, countnumber*200);
        }
        else if(animation_type.equals("drop") && !animationon)
            DropTranslationAnimation();
        else if(animation_type.equals("pick") && !animationon)
            PickCardTranslationAnimation();
        else if(animation_type.equals("drop_pick") && !animationon)
            DropPickTranslationAnimation();


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        boolean isLastCard = countnumber == (arrayList.size() - 1);

        layoutParams.setMargins(0,
                cardTopMargin,
                !isLastCard ? right_margin : 0, 0);

        setMargins(ivDropCreate,(int) convertDpToPixel(80,this),(int) convertDpToPixel(20,this)
                ,0,0);

        addlayout.addView(view,layoutParams);



        if(isLastCard)
        {
            ViewGroup.LayoutParams params = addlayout.getLayoutParams();
            int groupSize = arrayList.size();
            float half_card_size = Math.abs(right_margin);
            float card_width = Functions.getDimens(R.dimen.card_width) * groupSize;

//            params.width += half_card_size;

//            addlayout.setLayoutParams(params);
//            addlayout.requestLayout();
        }
    }

    //Define these globally e.g in your MainActivity class
    private short touchMoveFactor = 10;
    private short touchTimeFactor = 200;
    private PointF actionDownPoint = new PointF(0f, 0f);
    private long touchDownTime = 0L;

    private void cardActionHandler(View cardview,LinearLayout addlayout,final ArrayList<CardModel> arrayList, final int countnumber){
        isViewonTouch = false;
        isCardDrop = false;

        cardview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();

//                    if(!touchmode.isChecked())
//                    {
//                        return false;
//                    }

                isResentCardDrop = false;
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        actionDownPoint.x = event.getX();
                        actionDownPoint.y = event.getY();
                        touchDownTime = System.currentTimeMillis();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        return onTouchMainCard(v,event,arrayList,countnumber);
                    }
                    case MotionEvent.ACTION_UP: {
                        //on touch released, check if the finger was still close to the point he/she clicked
                        boolean isTouchLength = (Math.abs(event.getX() - actionDownPoint.x)+ Math.abs(event.getY() - actionDownPoint.y)) < touchMoveFactor;
                        boolean isClickTime = System.currentTimeMillis() - touchDownTime < touchTimeFactor;

                        //if it's been more than 200ms since user first touched and, the finger was close to the same place when released, consider it a click event
                        //Please note that this is a workaround :D
                        if (isTouchLength && isClickTime){
                            //call this method on the view, e.g ivPic.performClick();
                            onGroupsCardClick(v,arrayList,countnumber);
                            return true;
                        }
                        else {
                            // your code for move and drag
                            if(addlayout != null)
                                group_id = addlayout.getId();
                            return onTouchMainCard(v,event,arrayList,countnumber);
                        }
                    }
                }
                return false;
            }
        });

//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });

    }


    private void onGroupsCardClick(View view, ArrayList<CardModel> arrayList, int countnumber) {

        View imgalphacard = view.findViewById(R.id.imgalphacard);

        if(game_declare || arrayList.size() == 0)
        {
            return;
        }


        String tag = (String) view.getTag();
        //                Toast.makeText(MainActivity.this , tag, Toast.LENGTH_LONG).show();

        float CardMargin = cardTopMargin;
        CardModel model = arrayList.get(countnumber);
        Functions.LOGE("MainActivity","Card Position : "+countnumber);

        model.isSelected = !model.isSelected;

        if (model.isSelected)
        {
            CardMargin = 0;
            if(selectedcardvalue.size() == 0)
            {
                selectedpatti = model.Image;
                selectedpatti_id = model.card_id;
                selectedcardvalue.add(model);
            }

            for (int i = 0; i < selectedcardvalue.size() ; i++) {

                CardModel selectmodel = selectedcardvalue.get(i);

                if(!selectmodel.card_id.equals(model.card_id))
                {
                    selectedcardvalue.add(model);

                    selectedpatti = model.Image;
                    selectedpatti_id = model.card_id;

                    break;
                }

            }

            if(imgalphacard != null)
                imgalphacard.setVisibility(View.VISIBLE);

        }
        else {

            for (int i = 0; i < selectedcardvalue.size() ; i++) {

                //                        if(selectedcardvalue.get(i).Image.contains(model.Image))
                //                        {
                //                            selectedcardvalue.remove(model);
                selectedpatti = "";
                //                        }
                RemoveCard(model.card_id,selectedcardvalue);

            }

            if(imgalphacard != null)
                imgalphacard.setVisibility(View.GONE);

        }


        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        setMargins(view,
                0,
                (int) CardMargin,
                lp.rightMargin,
                0);


        if(selectedcardvalue.size() == 1 && !opponent_game_declare)
            bt_discard.setVisibility(View.VISIBLE);
        else
            bt_discard.setVisibility(View.GONE);

        if(selectedcardvalue.size() >= 2)
            bt_creategroup.setVisibility(View.VISIBLE);
        else
            bt_creategroup.setVisibility(View.GONE);

    }

    private GestureDetector gestureDetector;

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }


    View _view;
    ViewGroup _root,remove_viewgroup;
    int group_id = -1;
    LinearLayout.LayoutParams view_linearparams ;
    private int _xDelta;
    private int _yDelta;
    boolean isCardPick = false;
    boolean isCardDrop = false;
    boolean isResentCardDrop = false;
    boolean isFinishDesk = false;
    public void InitilizeRootView(final ArrayList<CardModel> arrayList, final int countnumber){

        _root = (ViewGroup)findViewById(R.id.root);
        _root.setVisibility(View.VISIBLE);

        ivpickcard.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        setTouchViewVisible(false);
                        CreateGroupViewVisible(true);
                        isCardDrop = false;

                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        }

                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
//                            view.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
//                            if(isCardDrop)
//                                return false;
//
//                                                setTouchViewVisible(true);
//                            isViewonTouch = false;
//
//
//                            ResetCardtoPosition(arrayList,countnumber);

                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        selectedpatti = String.valueOf(_view.getTag());
                        selectedpatti_id = String.valueOf(_view.getTag());

                        Functions.LOGE("RummyGame","selectedpatti : "+selectedpatti+
                                "\n selectedpatti_id : "+selectedpatti_id);

                        animation_type = "swap_animation";

                        API_CALL_drop_card(arrayList,countnumber);
                        //                        Toast.makeText(context, "Card Drop"+selectedpatti, Toast.LENGTH_SHORT).show();
                        isCardDrop = true;

                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:

                        if(isCardDrop)
                            return false;

                        setTouchViewVisible(true);
                        CreateGroupViewVisible(false);
                        isViewonTouch = false;


                        ResetCardtoPosition(arrayList,countnumber);

                        return true;
                }
                return false;
            }
        });

        ivDropCreate.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        CreateGroupViewVisible(true);
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        }

                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        view.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:


                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        CardModel model ;
                        selectedpatti = String.valueOf(_view.getTag());
                        selectedpatti_id = String.valueOf(_view.getTag());

                        Functions.LOGE("RummyGame","selectedpatti : "+selectedpatti+
                                "\n selectedpatti_id : "+selectedpatti_id);
                        RemoveCardFromArrayLists(selectedpatti_id);

                        if(removemodel != null)
                        {
                            model = removemodel;
                        }
                        else {
                            model = new CardModel();
                            model.Image = selectedpatti;
                            model.Image = selectedpatti_id;
                        }


                        selectedcardvalue.add(model);
                        animation_type = "swap_animation";
                        CreateGroupFromSelect(true);
                        CreateGroupViewVisible(false);
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:

                        CreateGroupViewVisible(false);
                        isViewonTouch = false;

                        return true;
                }
                return false;
            }
        });

        ivFinishDesk.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:

                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        }

                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
//                            view.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:

                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        selectedpatti = String.valueOf(_view.getTag());
                        selectedpatti_id = String.valueOf(_view.getTag());
                        animation_type = "finish_desk";
                        isFinishDesk = true;
                        API_CALL_drop_card(arrayList,countnumber);
                        isCardDrop = true;

                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:

                        ResetCardtoPosition(arrayList,countnumber);

                        return true;
                }
                return false;
            }
        });

    }

    private void CreateGroupViewVisible(boolean isVisible){

//        ivDropCreate.setVisibility(isVisible ?  View.VISIBLE : View.GONE);
        ivDropCreate.setVisibility(isVisible ?  View.GONE : View.GONE);

    }

    boolean isViewonTouch = false;
    private boolean onTouchMainCard(View cardview, MotionEvent event, final ArrayList<CardModel> arrayList, final int countnumber){

        InitilizeRootView(arrayList,countnumber);

        isCardDrop = false;

        if(!isSplit)
        {
            return false;
        }

        if(isViewonTouch)
            return false;


        _view = cardview;

        isViewonTouch = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isViewonTouch)
                {
                    ResetCardtoPosition(arrayList,countnumber);
                }

            }
        },5000);

        ClipData data = ClipData.newPlainText("","");

        MyDragShadowBuilder shadowBuilder = new MyDragShadowBuilder(_view);
        _view.startDrag(data, shadowBuilder, _view, DRAG_FLAG_OPAQUE);



        return true;

    }

    private void ResetCardtoPosition(final ArrayList<CardModel> arrayList, final int countnumber){

        isViewonTouch = false;
        isCardDrop = false;


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        animationon = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();

            //            Funtions.LOGE("MainAcitvity","Left : "+left);

        }

    }

    int mPickCardId = -1;
    private void parseLastCard(JSONObject mainJson) {
        try {
            JSONObject lastcardJson = mainJson.getJSONObject("last_card");
            int id = lastcardJson.getInt("id");
            if(mPickCardId != id)
            {
                boolean is_drop_card = lastcardJson.getInt("is_drop_card") == 1;
                mPickCardId = id;
                animateUserCardPick(!is_drop_card ? ivallcard : ivpickcard);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void animateUserCardPick(View fromView){
        if(!Functions.checkStringisValid(mUserChaal))
            return;

        Functions.LOGE("RummyPoint","mUserChaal : "+mUserChaal);

        View toView = null;

        if(mUserChaal.equalsIgnoreCase(user_id_player2))
        {
            if(isPlayer2)
                toView = findViewById(R.id.rltplayer2_new);
            else
                toView = findViewById(R.id.rltplayer2);

        }
        else if(mUserChaal.equalsIgnoreCase(user_id_player3))
        {
            toView = findViewById(R.id.rltplayer3);
        }
        else if(mUserChaal.equalsIgnoreCase(user_id_player3))
        {
            toView = findViewById(R.id.rltplayer4);
        }
        else if(mUserChaal.equalsIgnoreCase(user_id_player3))
        {
            toView = findViewById(R.id.rltplayer5);
        }

        if(toView != null)
        {
            View finalToView = toView;
            toView.post(new Runnable() {
                @Override
                public void run() {
                    cardmoveAnimation(fromView, finalToView);
                }
            });
        }
    }

    int card_width = Functions.getDimens(R.dimen.table_joker_card_width);
    private void cardmoveAnimation(View mfromView, View mtoView) {

        animationon = true;


        final View fromView, toView;

        fromView = mfromView;
        toView = mtoView;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];


        Rect myViewRect = new Rect();
        toView.getGlobalVisibleRect(myViewRect);
        float destX = myViewRect.left;
        float destY = myViewRect.top;

        animationLayout.setVisibility(View.VISIBLE);

        ImageView image_chips = creatDynamicard();

        animationLayout.addView(image_chips);

        if (card_width <= 0) {
            card_width = Functions.getDimens(R.dimen.table_joker_card_width);
        }

        int boardWidth = myViewRect.width(), boardHeight = myViewRect.height();


        int centreX = (int) (boardWidth / 2) - (card_width / 2);
        int centreY = (int) (boardHeight / 2) - (card_width / 2);

//        if (card_width > 0) {
//            destX = destX + boardWidth - card_width;
//            destY = destY + boardHeight - card_width;
//        } else
//        {
        destX += centreX;
        destY += centreY;
//        }



        Animations anim = new Animations();
        float finalDestX = destX;
        float finalDestY = destY;
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                animationon = false;
                animationLayout.removeAllViews();
            }
        });
        image_chips.setAnimation(a);
        a.startNow();

        PlaySaund(R.raw.teenpatticardflip_android);
    }

    private ImageView creatDynamicard() {
        ImageView chips = new ImageView(this);

        int chips_size = (int) getResources().getDimension(R.dimen.card_width);

        chips.setImageDrawable(Functions.getDrawable(context,R.drawable.backside_card));

        chips.setLayoutParams(new ViewGroup.LayoutParams(chips_size, chips_size));

        chips.post(new Runnable() {
            @Override
            public void run() {
                card_width = chips.getWidth();
            }
        });

        return chips;
    }

}