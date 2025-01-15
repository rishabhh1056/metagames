package com.metagards.metagames._Rummy;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.metagards.metagames.Activity.BuyChipsList;
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Animations;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.MyDragShadowBuilder;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Sound;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._Rummy.menu.DialogRulesRummy;
import com.metagards.metagames._RummyDeal.DialogDeclareCards;
import com.metagards.metagames._RummyGodMode.DialogRummyAllUserCard;
import com.metagards.metagames._RummyGodMode.DialogRummyGadhiCardsList;
import com.metagards.metagames.model.CardModel;
import com.metagards.metagames.model.Usermodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.view.View.DRAG_FLAG_OPAQUE;
import static com.metagards.metagames.Fragments.ActiveTables_BF.SEL_TABLE;
import static com.metagards.metagames.Utils.Functions.SetBackgroundImageAsDisplaySize;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;;
import static com.metagards.metagames.Utils.Functions.ANIMATION_SPEED;
import static com.metagards.metagames.Utils.Functions.convertDpToPixel;

public class RummPoint extends BaseActivity implements Animation.AnimationListener {
    private final int RUMMY_TOTALE_CARD = 13;
    String INVALID = "Invalid";
    public static final String CUSTOME_TABLE = "custome_table";
    public static final String PRIVATE_TABLE = "private_table";
    String IMPURE_SEQUENCE = "Impure sequence";
    String PURE_SEQUENCE = "Pure Sequence";
    String SET = "set";

    String DECLARE_BACK = "";
    String JOKER_CARD = "JK";
    String MAIN_JOKER_CARD = "JKR1";
    String MAIN_JOKER_CARD_2 = "JKR2";

    String DUMMY_CARD = "backside_card";
    int FINISH_DESK_CARD = R.drawable.finish_deck;
    int  teenpatti_backcard = R.drawable.backside_teenpatti;

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

    ImageView imgpl1circle,imgpl2circle,imgpl3circle,imgpl4circle,imgpl5circle,imgpl6circle,imgpl7circle,imgpl1winner,imgpl1winnerstar;

    TextView txtPlay1wallet,txtPlay2wallet,txtPlay3wallet,txtPlay4wallet,txtPlay5wallet,txtPlay6wallet,txtPlay7wallet, txt_30;

    View /*lnrPlay1wallet,*/lnrPlay2wallet,lnrPlay3wallet,lnrPlay4wallet,lnrPlay5wallet,lnrPlay6wallet,lnrPlay7wallet;


    TextView txtPlay2,txtPlay3,txtPlay4,txtPlay5,txtPlay6,txtPlay7;

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
    CountDownTimer counttimerforstartgame;
    CountDownTimer mCountDownTimer1,mCountDownTimer2,mCountDownTimer3,mCountDownTimer4,mCountDownTimer5
            ,mCountDownTimer6,mCountDownTimer7;


    Activity context ;
    String user_id_player1 = "-1";
    String user_id_player2 = "-1";
    String user_id_player3 = "-1";
    String user_id_player4 = "-1";
    String user_id_player5 = "-1";
    String user_id_player6 = "-1";
    String user_id_player7 = "-1";


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
    ImageView /*ivallcard,*/ivpickcard,iv_jokercard;
    ImageView ivFinishDesk;


    RelativeLayout ivallcard;
    float centreX , centreY;

    int timmersectlarge = 60000;
    int timmersectsmall = 1000;

    Button bt_creategroup,bt_sliptcard,bt_discard,bt_changecard,bt_startgame,bt_drop,bt_declare,bt_finish;

    boolean isSplit = false;
    String selectedpatti = "";
    String selectedpatti_id = "";

    String joker_card = "" ;
    SharedPreferences cardPref;
    String Pref_cards = "cards_";
    TextView tv_gameid,tv_tableid;
    int min_entry = 0;

    ImageView ivDropCreate;
    boolean isCardPick = false;
    boolean isPlayer2 = false;

    int loos_point = 20;
    boolean isFirstChall = true;
    Switch touchmode;

    FrameLayout flUserHighlight;

    ImageView imgCardsandar;

    CountDownTimer declareCountDown;
    boolean isDeclareShow = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rummypoint);

        if(getIntent().hasExtra("player2"))
            isPlayer2 = true;


        if(getIntent().hasExtra("hide_hint")){
//            Toast.makeText(RummPoint.this, "hide", Toast.LENGTH_SHORT).show();
//            findViewById(R.id.lnr_first_life).setVisibility(View.GONE);
//            findViewById(R.id.lnr_sec_life).setVisibility(View.GONE);
        }

        context = this;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        DECLARE_BACK = getString(R.string.declare_back);

        Initialization();

//        API_CALL_middlecard();

        InitCoutDown();


        UserProgressCount();

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.d("went_here_rummy", "1");
            Uri uri = intent.getData();
            table_id = uri.getQueryParameter("table_id");
            System.out.println("scs");
            getGame(Const.rummy_join_table);
        } else {
            Log.d("some_error_", "2");
        }
//        findViewById(R.id.rl_invite).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                String referal_code = prefs.getString("referal_code", "");
//
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, referal_code);
//                shareMessage = Functions.inviteTableLink(context,table_id,TEENPATTIPRIVATE);
//
//                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                startActivity(Intent.createChooser(shareIntent, "choose one"));
//            }
//        });

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

    RelativeLayout animationLayout;
    private void Initialization() {

        if(!SharePref.getInstance().isGodmodeEnable())
            findViewById(R.id.ivUserCardsGuid).setVisibility(View.GONE);

        bt_changecard = findViewById(R.id.bt_changecard);

        findViewById(R.id.ivUserCardsGuid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserCardList(v);
            }
        });

        bt_changecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGadhiCardList(v);
            }
        });

        if(getIntent().hasExtra("min_entry"))
            min_entry = getIntent().getIntExtra("min_entry",0);

        flUserHighlight = findViewById(R.id.flUserHighlight);
        imgCardsandar = findViewById(R.id.imgCardsandar);
        flUserHighlight.setVisibility(View.GONE);


        RelativeLayout rltParentLayout = findViewById(R.id.rltParentLayout);
        ImageView imgTable = findViewById(R.id.imgTable);

        SetBackgroundImageAsDisplaySize(this,rltParentLayout,R.drawable.home_bg);
        if(context != null && !context.isFinishing())
            LoadImage().load(R.drawable.update_rummy_table).into(imgTable);

        touchmode = findViewById(R.id.touchmode);
        touchmode.setVisibility(View.GONE);
        tv_gameid = findViewById(R.id.tv_gameid);
        tv_tableid = findViewById(R.id.tv_tableid);

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

        String textToCache = "Some text";
//            boolean success = GetCacheDirExample.writeAllCachedText(this, "myCacheFile.txt", textToCache);
//            String readText = GetCacheDirExample.readAllCachedText(this, "myCacheFile.txt");

//            for (File filepath:
//                    GetCacheDirExample.getCashFiles(context)) {
//
//                Functions.LOGE("getCashFiles",""+filepath.toPath());
//            }

//            Functions.LOGE("myCacheFile",""+readText);

        findViewById(R.id.rlt_highlighted_pick).setVisibility(View.INVISIBLE);
        findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.INVISIBLE);

        if(isPlayer2)
        {
            findViewById(R.id.lnr_new2player).setVisibility(View.VISIBLE);

//            findViewById(R.id.rtlAmpire).setVisibility(View.GONE);
            findViewById(R.id.rltplayer2).setVisibility(View.GONE);
            findViewById(R.id.rltplayer3).setVisibility(View.GONE);
            findViewById(R.id.rltplayer4).setVisibility(View.GONE);
            findViewById(R.id.rltplayer5).setVisibility(View.GONE);
            findViewById(R.id.rltplayer6).setVisibility(View.GONE);
            findViewById(R.id.rltplayer7).setVisibility(View.GONE);

        }
        else {
            findViewById(R.id.lnr_new2player).setVisibility(View.GONE);

            findViewById(R.id.rtlAmpire).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer2).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer3).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer4).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer5).setVisibility(View.VISIBLE);
            findViewById(R.id.rltplayer6).setVisibility(View.VISIBLE);
//            findViewById(R.id.rltplayer7).setVisibility(View.VISIBLE);
        }

        ivDropCreate =  findViewById(R.id.ivDropCreate);
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

        bt_drop=findViewById(R.id.bt_drop);
        bt_finish=findViewById(R.id.bt_finish);
        DrobButtonVisible(false);
        FinishButtonVisible(false);
        bt_startgame=findViewById(R.id.bt_startgame);
        bt_discard=findViewById(R.id.iv_discard);
        bt_sliptcard=findViewById(R.id.iv_sliptcard);
        ivallcard=findViewById(R.id.ivallcard);


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
        ivFinishDesk=findViewById(R.id.ivfindeck);

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
        txt_30 = findViewById(R.id.txt_30);

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

        txtGameFinish=findViewById(R.id.txtGameFinish);

        imgpl1winner = findViewById(R.id.imgpl1winner);
        imgpl1winnerstar = findViewById(R.id.imgpl1winnerstar);
        Glide.with(getApplicationContext()).load(Functions.getDrawable(context, R.drawable.giphy)).into(imgpl1winner);
        Glide.with(getApplicationContext()).load(Functions.getDrawable(context, R.drawable.star)).into(imgpl1winnerstar);


        OnClickListener();


    }

    private void visibleChangeCardButton(boolean visible){
        if(SharePref.getInstance().isGodmodeEnable())
            bt_changecard.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void openUserCardList(View view) {
        DialogRummyAllUserCard.getInstance(context).show();
    }

    public void openGadhiCardList(View view) {
        DialogRummyGadhiCardsList.getInstance(context)
                .setMyChangingCard(selectedcardvalue.get(0).card_id)
                .returnCallback(new OnItemClickListener() {
                    @Override
                    public void Response(View v, int position, Object object) {

                        if(selectedcardvalue.size() == 1)
                        {
                            selectedpatti = selectedcardvalue.get(0).Image;
                            selectedpatti_id = selectedcardvalue.get(0).card_id;
                        }

                        RemoveCardFromArrayLists(selectedpatti_id);

                        String card = (String) object;
                        CardModel model = new CardModel();
                        model.Image = card;

                        if(model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
                        {
                            model.Image = removeLastChars(model.Image,1);
                        }

                        model.card_id = card;
                        animation_type = "";
                        AddCardinEmptyList(model);
                        GetGroupValueFromTouch(animation_type);

                    }
                }).show();
    }

    private void setDeclareButton() {
        bt_declare.setText(getString(R.string.declare));
        bt_declare.setTag(getString(R.string.declare));
    }

    private void setDeclareBackButton(String declarestring){
        bt_declare.setText(declarestring);
        bt_declare.setTag(DECLARE_BACK);
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
    int timertime = 500;
    SharedPreferences prefs;
    private void InitCoutDown() {

        API_CALL_get_table();


        timerstatus = new Timer();

        timerstatus.scheduleAtFixedRate(new TimerTask() {

                                            @Override
                                            public void run() {

                                                API_CALL_status();



                                            }

                                        },
                //Set how long before to start calling the TimerTask (in milliseconds)
                timertime,
                //Set the amount of time between each execution (in milliseconds)
                timertime);



        counttimerforstartgame = new CountDownTimer(8000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                isFirstGame = true;
                centreX=ivallcard.getX() + ivallcard.getWidth()  / 2;
                centreY=ivallcard.getY() + ivallcard.getHeight() / 2;

                //                Functions.LOGE("MainActivity","centreX : "+centreX+" / "+"centreY :"+centreY);

                count--;
                txtGameFinish.setVisibility(View.VISIBLE);
                txtGameFinish.setText("Round will start in " + (millisUntilFinished/1000)+" second");


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

    private void show_halfTime(){
        txt_30.setVisibility(View.VISIBLE);
        new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_30.setText("" + millisUntilFinished / 1000+" sec");
                // logic to set the EditText could go here
            }
            public void onFinish() {
                txt_30.setText("");
            }
        }.start();
    }
    private void UserProgressCount() {
        //        soundPool  = new SoundPool(context);

        //Progress -
        mCountDownTimer1 = new CountDownTimer(timmersectlarge, timmersectsmall) {

            @Override
            public void onTick(long millisUntilFinished) {
                txt_30.setVisibility(View.INVISIBLE);
                imgpl1glow.setVisibility(View.VISIBLE);
                isProgressrun1 = false;

                pStatus -= 2;
                pStatusprogress++;
                mProgress1.setProgress((int) pStatusprogress * 1);

                int half_time = timmersectlarge / 2;

                half_time = half_time / 1000;

                if (pStatusprogress >= half_time) {
                    PlaySoundFx(R.raw.teenpattitick,true);
//                    Toast.makeText(context, "half time", Toast.LENGTH_SHORT).show();
//                    show_halfTime();
                    txt_30.setVisibility(View.VISIBLE);
                    txt_30.setText("" + millisUntilFinished / 1000+" sec");
                }

            }

            @Override
            public void onFinish() {
                txt_30.setVisibility(View.INVISIBLE);
                cancelSound();
                pStatusprogress = 0;
                mProgress1.setProgress(100);
                mProgress1.setProgress(0);
                imgpl1glow.setVisibility(View.GONE);
                isProgressrun1 = true;

//                ExitFromGames();      //need later

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

    private void Player1CancelCountDown(){
//            mCountDownTimer1.onFinish();
        mCountDownTimer1.cancel();
        checkMyCards();
    }

    private void OnClickListener(){

//        findViewById(R.id.bt_sharewallet).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                API_CALL_share_wallet();
//            }
//        });

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
                    Functions.showToast(context, ""+getString(R.string.chaal_error_messsage));
                    return;
                }

                if(opponent_game_declare)
                    return;

                if(isSplit)
                {
                    animation_type = "pick";
                    API_CALL_get_card();
                }else {
                    Functions.showToast(context, ""+getString(R.string.sort_error_message));
                }

            }
        });


        ivpickcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isMyChaal)
                {
                    Functions.showToast(context, ""+getString(R.string.chaal_error_messsage));
                    return;
                }

                if(opponent_game_declare)
                    return;

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
//
            }
        });

        bt_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                if(!isMyChaal)
                //                {
                //                    Functions.showToast(context, ""+getString(R.string.chaal_error_messsage));
                //                    return;
                //                }

                //                if(isSplit)
                //                {
                //                    Functions.showToast(MainActivity.this, ""+getString(R.string.sort_error_message));
                //                    return;
                //                }

                if(isFirstChall)
                    loos_point = 20;
                else
                    loos_point = 40;

                Functions.Dialog_CancelAppointment(context, "Drop", "drop ? You will lose this game by "+loos_point+" points.", new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {

                        if(resp.equalsIgnoreCase("yes"))
                            API_CALL_pack_game();
                    }
                });


            }
        });

        bt_declare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    if(!isMyChaal && !opponent_game_declare)
//                    {
//                        Functions.showToast(context, ""+getString(R.string.chaal_error_messsage));
//                        return;
//                    }

                if(isSplit)
                {
                    API_CALL_declare();
                    declareButtonGone();
                    DrobButtonVisible(false);

                }else {
                    Functions.showToast(context, ""+getString(R.string.sort_error_message));
                }

                //                GetCardFromLayout();

            }
        });

        bt_startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                API_CALL_start_game();

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
                    Functions.showToast(context, ""+getString(R.string.chaal_error_messsage));
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
                        Functions.showToast(context, ""+getString(R.string.sort_error_message));
                    }

                }
                else {

                    Functions.showToast(context, ""+getString(R.string.select_card_error_message));

                }



            }
        });

        findViewById(R.id.bt_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isMyChaal)
                {
                    Functions.showToast(context, ""+getString(R.string.chaal_error_messsage));
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
                        loos_point = 40;
                        Functions.Dialog_CancelAppointment(context, "Finish", "finish ? You will lose this game by "+loos_point+" points if you wrong declare.", new Callback() {
                            @Override
                            public void Responce(String resp, String type, Bundle bundle) {

                                if(resp.equalsIgnoreCase("yes"))
                                {
                                    animation_type = "finish_desk";
                                    isFinishDesk = true;

                                    API_CALL_drop_card(null,0);
                                }


                            }
                        });
                    }
                    else {
                        Functions.showToast(context, ""+getString(R.string.sort_error_message));
                    }

                }
                else {

                    Functions.showToast(context, ""+getString(R.string.select_card_error_message));

                }


            }
        });

    }

    private void FinishButtonVisible(boolean visible){
        bt_finish.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void declareButtonVisible(){
        bt_declare.setVisibility(View.VISIBLE);
    }

    private void declareButtonGone(){
        bt_declare.setVisibility(View.GONE);
    }

    private void DrobButtonVisible(boolean visible){
        bt_drop.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        Functions.Dialog_CancelAppointment(context, "Confirmation", "Leave ?", new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp.equals("yes"))
                {
                    StopSound();

                    API_CALL_leave_table("1");

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

            addCardtoList(cardModelArrayList.get(countvaue).Image,countvaue);
            countvaue++;

        }


    }

    private int getCardListSize() {
        return rlt_addcardview.getChildCount();
    }

    private View getListLastCard(){
        return rlt_addcardview.getChildAt(getCardListSize() - 1);
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


        int stickerId = teenpatti_backcard;

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        if(stickerId > 0)
            if(context != null && !context.isFinishing())
                LoadImage().load(stickerId).into(sticker);

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


        //        AnimatorSet animatorSet =
        //                getViewToViewScalingAnimator(rootView, shuttleView, fromRect, toRect, ANIMATION_SPEED, 0);
        //
        //        animatorSet.addListener(new Animator.AnimatorListener() {
        //            @Override
        //            public void onAnimationStart(Animator animation) {
        //                shuttleView.setVisibility(View.VISIBLE);
        ////                fromView.setVisibility(View.INVISIBLE);
        //            }
        //
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                shuttleView.setVisibility(View.GONE);
        //                fromView.setVisibility(View.VISIBLE);
        //                animationon = false;
        //            }
        //
        //            @Override
        //            public void onAnimationCancel(Animator animation) {
        //
        //            }
        //
        //            @Override
        //            public void onAnimationRepeat(Animator animation) {
        //
        //            }
        //        });
        //        animatorSet.start();

    }

    private void PickCardTranslationAnimation(){

        animationon = true;

        View animationview = findViewById(R.id.animationview);

        final View fromView, toView, shuttleView;

        fromView = ivallcard;

        if(getCardListSize() > 0)
            toView = getListLastCard();
        else
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


        int stickerId = teenpatti_backcard;

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        if(stickerId > 0)
            if(context != null && !context.isFinishing())
                LoadImage().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_hieght, card_hieght));
        animationLayout.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX-100, destY-250, null, ANIMATION_SPEED, new Callback() {
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

        // " +
        int stickerId = teenpatti_backcard;

        int card_hieght = (int) getResources().getDimension(R.dimen.card_hieght);

        if(stickerId > 0)
            if(context != null && !context.isFinishing())
                LoadImage().load(stickerId).into(sticker);

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


        //        AnimatorSet animatorSet =
        //                getViewToViewScalingAnimator(rootView, shuttleView, fromRect, toRect, ANIMATION_SPEED, 0);
        //
        //        animatorSet.addListener(new Animator.AnimatorListener() {
        //            @Override
        //            public void onAnimationStart(Animator animation) {
        //                shuttleView.setVisibility(View.VISIBLE);
        ////                fromView.setVisibility(View.INVISIBLE);
        //            }
        //
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                shuttleView.setVisibility(View.GONE);
        //                fromView.setVisibility(View.VISIBLE);
        //                animationon = false;
        //            }
        //
        //            @Override
        //            public void onAnimationCancel(Animator animation) {
        //
        //            }
        //
        //            @Override
        //            public void onAnimationRepeat(Animator animation) {
        //
        //            }
        //        });
        //        animatorSet.start();

    }


    String table_id = "1",game_id = "",Main_Game_ID = "";
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

        ApiRequest.Call_Api(this, Const.get_table, params, new Callback() {
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


    float user_wallet_1 = -1;
    private void UserResponse(JSONArray table_users) throws JSONException {
        //---------------------------------------------User arrange ----------------

        int pp1 = 0;

        for (int i = 0; i < table_users.length(); i++) {
            String use_temp = prefs.getString("user_id", "");

            if (table_users.getJSONObject(i).getString("user_id").equals(use_temp)) {
                pp1 = i;
            }
        }

        for (int i = 0; i < pp1; i++) {

            JSONObject temp = table_users.getJSONObject(0);

            for (int j = 0; j < table_users.length() - 1; j++) {

                table_users.put(j, table_users.get(j + 1));//=jsonArrayuser

            }

            table_users.put(table_users.length() - 1,
                    temp);
        }

        user_id_player1 = "";
        user_id_player2 = "";
        user_id_player3 = "";
        user_id_player4 = "";
        user_id_player5 = "";
        user_id_player6 = "";
        user_id_player7 = "";

        if(isPlayer2)
        {
            for (int k = 0; k < table_users.length(); k++) {
                if (k == 0) {

                    String name = table_users.getJSONObject(0).getString("name");
                    user_id_player1 = table_users.getJSONObject(0).getString(
                            "user_id");
                    String profile_pic = table_users.getJSONObject(0).getString("profile_pic");
                    String walletplayer1 = table_users.getJSONObject(0).getString("wallet");
//                long numberamount = Float.parseFloat(walletplayer1);
//                txtPlay1wallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
                    txtPlay1wallet.setText("" + walletplayer1);
//                    LoadImage().load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);
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
                    user_wallet_1 = Float.parseFloat(walletplayer1);
                    txtPlay1wallet.setText("" + Functions.formateAmount(walletplayer1));
                    if(context != null && !context.isFinishing())
                        LoadImage().load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);

                    if (user_id_player1.equals(prefs.getString("user_id", ""))) {


                    } else {

                        Functions.showToast(context, "Your are timeout from " +
                                "this table Join again.");


                        finish();
                    }
                    // imgchipuser1.setVisibility(View.VISIBLE);
                    //first player
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

    }

    private RequestManager LoadImage()
    {
        return  Glide.with(getApplicationContext());
    }

    private void API_CALL_pack_game() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);


        //        Log.v("userinfo","user_id "+prefs.getString("user_id", "")+"game_id" +game_id);

        ApiRequest.Call_Api(this, Const.pack_game, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                Log.v("pack_game",resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        Functions.showToast(context, ""+message);
                        DrobButtonVisible(false);

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

    private void API_CALL_leave_table(final String value) {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);


        //        Log.v("userinfo","user_id "+prefs.getString("user_id", "")+"game_id" +game_id);

        ApiRequest.Call_Api(this, Const.leave_table, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                Log.v("pack_game",resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {

                        //                        Functions.showToast(MainActivity.this, ""+message);
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
            url = Const.declare_back;
        else
            url = Const.declare;

        ApiRequest.Call_Api(this, url, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {


                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        Functions.showToast(context, ""+message);
                        declareButtonGone();
                        DrobButtonVisible(false);

                        game_declare = true;
                        cancelSound();
                    }
                    else {
                        Functions.showToast(context, ""+message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    declareButtonGone();
                    DrobButtonVisible(false);

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

//                    jsonObject.put("card_group",""+lnr_group_card.getTag());      //new cmt

                    Log.e("TAG_lnr_group_card", "GetCardFromLayout: " + lnr_group_card.getTag().toString());

                    if (str_1st_life.equals("1") && str_2nd_life.equalsIgnoreCase("")){
                        jsonObject.put("card_group", "0");
                        Log.e("one_", "1");
                    }

                    if (str_2nd_life.equals("1") && str_1st_life.equalsIgnoreCase("")){
                        jsonObject.put("card_group", "0");
                        Log.e("one_", "2");
                    }

                    if (str_1st_life.equals("1") && str_2nd_life.equals("1")) {
                        jsonObject.put("card_group", "" + lnr_group_card.getTag());
                        Log.e("one_", "3");
                    }
                    if (str_1st_life.equals("") && str_2nd_life.equals("")) {
                        jsonObject.put("card_group", "0");
                        Log.e("one_", "4");
                    }
//                    else {
//                        jsonObject.put("card_group", "0");
//                    }

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

//    private String GetCardFromLayout() {
//
//        JSONArray jsonArray = new JSONArray();
//
//        try {
//
//
//            if (!isSplit) {
//
//                for (int k = 0; k < 1; k++) {
//
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("card_group", "" + 0);
//
//                    JSONArray jsonArray1 = new JSONArray();
//
//                    for (int i = 0; i < rlt_addcardview.getChildCount(); i++) {
//
//                        View view = rlt_addcardview.getChildAt(i);
//
//                        jsonArray1.put("" + view.getTag());
//
//                        jsonObject.put("cards", jsonArray1);
//                    }
//
//                    jsonArray.put(jsonObject);
//
//                }
//
//
//            } else {
//
//
//                for (int i = 0; i < rlt_addcardview.getChildCount(); i++) {
//
//                    JSONObject jsonObject = new JSONObject();
//
//                    JSONArray jsonArray1 = new JSONArray();
//
//                    View view = rlt_addcardview.getChildAt(i);
//                    LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);
//
//                    if (lnr_group_card == null)
//                        return "";
//
//                    Log.e("TAG_lnr_group_card", "GetCardFromLayout: " + lnr_group_card.getTag().toString());
//                   /* if (PURE_SEQUENCE_VALUE == Integer.parseInt(lnr_group_card.getTag().toString()) || IMPURE_SEQUENCE_VALUE == Integer.parseInt(lnr_group_card.getTag().toString()) || SET_VALUE == Integer.parseInt(lnr_group_card.getTag().toString())) {
//                        jsonObject.put("card_group", "" + lnr_group_card.getTag());
//                    } else {
//                        jsonObject.put("card_group", "0");
//                    }*/
//                    if (groupvalueList.contains(PURE_SEQUENCE_VALUE)) {
//                        jsonObject.put("card_group", "" + lnr_group_card.getTag());
//                    } else {
//                        jsonObject.put("card_group", "0");
//                    }
//
//                    for (int j = 0; j < lnr_group_card.getChildCount(); j++) {
//
//                        View view2 = lnr_group_card.getChildAt(j);
//                        jsonArray1.put("" + view2.getTag());
//
//                        //                    Log.e("MainActivity","Layout Tags : "+view2.getTag());
//
//                        jsonObject.put("cards", jsonArray1);
//                    }
//
//
//                    jsonArray.put(jsonObject);
//
//                }
//
//
//                //            Log.e("MainActivity","Layout Tags : "+jsonArray.toString());
//
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//            Log.e("MainActivity", "Layout Tags : " + e.getMessage());
//
//
//        }
//
//
//        cardPref.edit().putString(Pref_cards + game_id, "" + jsonArray.toString()).apply();
//        cardPref.edit().putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12)).apply();
//
//        Functions.LOGE("Rummy5Player", "" + jsonArray.toString());
//        return jsonArray.toString();
//    }

    private void API_CALL_start_game() {

        RestartGameActivity();

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.start_game, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                Log.v("start_game",resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if(code.equalsIgnoreCase("200"))
                    {
                        game_id = jsonObject.optString("game_id");
                        Main_Game_ID = jsonObject.optString("game_id");
                        bt_startgame.setVisibility(View.GONE);
                    }
                    else if(code.equalsIgnoreCase("406"))
                    {

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

    boolean isStatusApiCalling = false;

    private void API_CALL_middlecard() {

        HashMap params = new HashMap();
        params.put("game_id",""+game_id);
        params.put("table_id",""+table_id);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));
        //        Log.v("userinfo","user_id "+prefs.getString("user_id", "")+"game_id" +game_id);

        ApiRequest.Call_Api(this, Const.status, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    Parse_responseMiddle(jsonObject);
                    //                    Log.v("status" , "working -   "+jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

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


        HashMap params = new HashMap();
        params.put("game_id",""+game_id);
        params.put("table_id",""+table_id);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));
        //        Log.v("userinfo","user_id "+prefs.getString("user_id", "")+"game_id" +game_id);

        ApiRequest.Call_Api(this, Const.status, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    Parse_responseStatus(jsonObject);
                    //                    Log.v("status" , "working -   "+jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                    isStatusApiCalling = false;
                }

                isStatusApiCalling = false;


            }
        });



    }

    private void API_CALL_getCardList() {

        HashMap params = new HashMap();
        params.put("game_id",""+game_id);
        //        params.put("table_id","1");
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.my_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                Log.v("my_card" , resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    RestartGameActivity();
                    DrobButtonVisible(true);


                    String previous_cards =  cardPref.getString(Pref_cards+game_id,"");

                    if(previous_cards != null && !previous_cards.equals("") && !previous_cards.equalsIgnoreCase("[{\"card_group\":\"0\"}]"))
                    {
                        Parse_response(jsonObject);
//                        Parse_PreviousCards(previous_cards,jsonObject);
                    }
                    else {
                        Parse_response(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        //        cardListAdapter.notifyDataSetChanged();

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
                    API_CALL_Sort_card_value(null,0,0);

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                checkMyCards();
//                            }
//                        },1000);

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

        if(my_count != 13 && !isCardPick)
        {

            int extra_cards_count = my_count - 10;

            HashMap params = new HashMap();
            params.put("game_id",""+game_id);
            params.put("user_id",""+prefs.getString("user_id", ""));
            params.put("token",""+prefs.getString("token", ""));

            ApiRequest.Call_Api(this, Const.my_card, params, new Callback() {
                @Override
                public void Responce(String resp, String type, Bundle bundle) {
                    try {
                        JSONObject jsonObject = new JSONObject(resp);


                        String code = jsonObject.optString("code");
                        String message = jsonObject.optString("message");

                        if(code.equalsIgnoreCase("200"))
                        {
                            JSONArray cardsArray = jsonObject.optJSONArray("cards");

                            if(cardsArray != null && cardsArray.length() > 0)
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
                                                    API_CALL_Sort_card_value(null,0,0);


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
    String mUserChaal;
    private void RestartGameActivity(){
        findViewById(R.id.rlt_dropgameview).setVisibility(View.GONE);
        ivpickcard.setImageDrawable(getResources().getDrawable(teenpatti_backcard));
        isSplit = false;
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
        DrobButtonVisible(false);
        setDeclareButton();
        game_declare = false;
        opponent_game_declare = false;
        isFinishDesk = false;
        isFirstChall = true;
        isgamestarted = false;
        isGamePacked = false;
        cardModelArrayList.clear();
        userModelArrayList.clear();
        isCardPick = false;
        isResentCardDrop = false;
        findViewById(R.id.iv_rightarrow).setVisibility(View.GONE);
        findViewById(R.id.iv_leftarrow).setVisibility(View.GONE);
        cancelSound();
        int first_check_ui = R.drawable.ic_uncheckbox;
        int second_check_ui = R.drawable.ic_uncheckbox;

        ((ImageView)findViewById(R.id.ivFirstlifecheck))
                .setImageDrawable(Functions.getDrawable(context,first_check_ui));

        ((ImageView)findViewById(R.id.ivSeconlifecheck))
                .setImageDrawable(Functions.getDrawable(context,second_check_ui));

        str_1st_life=""; str_2nd_life="";
        string_middle_card = "";

        ;  // where myresource " +

        int imageResource1 = FINISH_DESK_CARD;


        if(context != null && !context.isFinishing())
            LoadImage().load(imageResource1).into(ivFinishDesk);

    }

    @Override
    protected void onDestroy() {
        StopSound();
        timerstatus.cancel();
        super.onDestroy();

    }

    boolean isgamestarted = false;
    boolean game_declare = false;
    boolean opponent_game_declare = false;
    boolean isGamePacked = false;
    boolean isFirstGame = false;
    ArrayList<Usermodel> pointUserlist ;
    String str_game_id = "";

    private void Parse_responseMiddle(JSONObject jsonObject) throws JSONException {
        String code = jsonObject.optString("code");

        if(code.equalsIgnoreCase("200"))
        {
            JSONArray drop_card = jsonObject.optJSONArray("drop_card");
            if(drop_card != null)
            {
                if(drop_card.length() > 0)
                {
                    for (int i = 0; i < drop_card.length() ; i++) {

                        JSONObject drop_Object = drop_card.getJSONObject(i);
                        CardModel model = new CardModel();
                        model.Image = drop_Object.optString("card");

                        int imageResource1 = Functions.GetResourcePath(model.Image.toLowerCase(),context);
                        if(imageResource1 > 0 && isSplit)
                        {
                            if(context != null && !context.isFinishing())
                                LoadImage().load(imageResource1).into(ivpickcard);
                            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();

                            if(opponent_game_declare || game_declare)
                                if(context != null && !context.isFinishing())
                                    LoadImage().load(imageResource1).into(ivFinishDesk);
                        }

                    }
                }
                else {
                    ivpickcard.setImageDrawable(getResources().getDrawable(teenpatti_backcard));
                }

            }


        }
        else if(code.equalsIgnoreCase("403"))
        {
            ExitFromGames();
        }
        else {
            //            Functions.showToast(this, ""+message);
        }


    }
    private void Parse_responseStatus(JSONObject jsonObject) throws JSONException {

        String code = jsonObject.optString("code");
        String message = jsonObject.optString("message");
        JSONArray table_users = jsonObject.optJSONArray("table_users");
        JSONArray game_users = jsonObject.optJSONArray("game_users");
        JSONObject table_detail = jsonObject.optJSONObject("table_detail");
        String total_table_amount  = jsonObject.optString("table_amount");
        String table_amount = jsonObject.optString("table_amount","80");

        if(table_detail != null)
        {
            int boot_value = table_detail.optInt("boot_value",0);
            int point_value = boot_value/80;
            min_entry = point_value * 100;

        }


        getTextView(R.id.tv_gameid).setText("#"+game_id);
        getTextView(R.id.tvTableType).setText("Rummy Point "+Functions.getStringFromTextView(txtPlay1wallet));

        if(Functions.checkisStringValid(total_table_amount))
        {
            getTextView(R.id.tvTableamount).setVisibility(View.VISIBLE);
            getTextView(R.id.tvTableamount).setText("Prize "+total_table_amount);

        }
        else {
            getTextView(R.id.tvTableamount).setVisibility(View.GONE);
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
        }

        if(table_users != null)
        {
            UserResponse(table_users);
        }


        if(code.equalsIgnoreCase("200"))
        {


            JSONArray drop_card = jsonObject.optJSONArray("drop_card");
            JSONArray game_users_cards = jsonObject.optJSONArray("game_users_cards");
            JSONArray game_log = jsonObject.optJSONArray("game_log");
            Log.e("TAG_game_log", "Parse_responseStatus: " + game_log);
            str_game_id = game_log.getJSONObject(0).getString("game_id");
            joker_card = jsonObject.optString("joker");
            Log.d("model_joker_card", joker_card);
            if(joker_card.substring(joker_card.length() - 1).equalsIgnoreCase("_"))
            {
                joker_card = removeLastChars(joker_card,1);
            }


            String game_status = jsonObject.optString("game_status");
            String declare_user_id = jsonObject.optString("declare_user_id");
            boolean declare = jsonObject.optBoolean("declare");

            if(declare_user_id != null &&
                    !declare_user_id.equals(prefs.getString("user_id", "")) && declare)
            {
                opponent_game_declare = true;
                Functions.showToast(context, ""+getString(R.string.declare_game));
                DrobButtonVisible(false);
                declareButtonVisible();

                if(!isDeclareShow)
                {
                    setDeclareBackButton(DECLARE_BACK);
                    declareCountDown.start();
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
                //                bt_startgame.setVisibility(View.VISIBLE);
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
                bt_startgame.setVisibility(View.GONE);
                rltwinnersymble1.setVisibility(View.GONE);
                rltwinnersymble2.setVisibility(View.GONE);
                rltwinnersymble3.setVisibility(View.GONE);
                rltwinnersymble4.setVisibility(View.GONE);
                rltwinnersymble5.setVisibility(View.GONE);
                rltwinnersymble6.setVisibility(View.GONE);
                rltwinnersymble7.setVisibility(View.GONE);

                if(!isgamestarted && !isGamePacked)
                    API_CALL_getCardList();
                else  if(isgamestarted && rlt_addcardview.getChildCount() <= 0 && !isRunning)
                    API_CALL_getCardList();


                mUserChaal = jsonObject.getString("chaal");
                parseLastCard(jsonObject);
                makeHightLightForChaal(mUserChaal);
            }


            int imageResource2 = Functions.GetResourcePath(joker_card.toLowerCase(),context);
            Log.d("imageResource2_", ""+imageResource2);
            if(imageResource2 > 0)
                if(imageResource2 > 0)
                    if(context != null && !context.isFinishing())
                        LoadImage().load(imageResource2).into(iv_jokercard);

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
                        Log.d("middle_pick_card", drop_Object.optString("card"));
                        if(!model.Image.equals("") && !model.Image.equals("null") &&
                                model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
                        {
                            model.Image = removeLastChars(model.Image,1);
                        }

                        int imageResource1 = Functions.GetResourcePath(model.Image.toLowerCase(),context);

                        if(imageResource1 > 0 && isSplit)
                        {
                            if(context != null && !context.isFinishing())
                                LoadImage().load(imageResource1).into(ivpickcard);

                            if(opponent_game_declare || game_declare)
                                if(context != null && !context.isFinishing())
                                    LoadImage().load(imageResource1).into(ivFinishDesk);
                        }


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
                    ivpickcard.setImageDrawable(getResources().getDrawable(teenpatti_backcard));
                }



            }

            if(game_users_cards != null)
            {

                ArrayList<CardModel> game_users_cards_list = new ArrayList<>();

                for (int i = 0; i < game_users_cards.length() ; i++) {


                    JSONObject game_object = game_users_cards.getJSONObject(i);
                    JSONObject json_user = game_object.getJSONObject("user");
                    CardModel model = new CardModel();
                    String user_id = json_user.optString("user_id");
                    model.user_id = user_id;
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

//                        isDeclareDialogShow = false;
                    }
                };
                dialogDeclareCards
                        .setGame_users_cards_list(game_users_cards_list, str_game_id)
                        .setGame_start_time(game_start_time)
                        .show();
                ivpickcard.setImageDrawable(getResources().getDrawable(teenpatti_backcard));

            }

        }
        else if(code.equalsIgnoreCase("403"))
        {
            ExitFromGames();
        }
        else {
            game_id = jsonObject.optString("active_game_id");
            //            Functions.showToast(this, ""+message);
        }


    }

    private TextView getTextView(int id){
        return  ((TextView)findViewById(id));
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


        if (user_id_player.equals("0")) {

            txtPlay.setText("");
            txtPlaywallet.setVisibility(View.INVISIBLE);
            lnrPlaywallet.setVisibility(View.INVISIBLE);


            int imageResourceuser2 = R.drawable.avatar;

            if(context != null && !context.isFinishing())
                LoadImage().load(imageResourceuser2).into(imgplcircle);
        }
        else {

            txtPlay.setText(name1);
            txtPlaywallet.setVisibility(View.VISIBLE);
            lnrPlaywallet.setVisibility(View.VISIBLE);

            float numberamount = Float.parseFloat(walletplayer2);
            txtPlaywallet.setText("" + Functions.formateAmount(walletplayer2));

            if(context != null && !context.isFinishing())
                LoadImage().load(Const.IMGAE_PATH + profile_pic1).into(imgplcircle);

        }

    }

    int countvaue = 0;
    CountDownTimer cardDistrubutionCount;
    boolean isRunning = false;
    private void Parse_response(JSONObject jsonObject) throws JSONException {

        String code = jsonObject.optString("code");
        String message = jsonObject.optString("message");
        countvaue = 0;
        if(code.equalsIgnoreCase("200"))
        {
//            findViewById(R.id.iv_rightarrow).setVisibility(View.VISIBLE);
//            findViewById(R.id.iv_leftarrow).setVisibility(View.VISIBLE);
//            findViewById(R.id.iv_rightarrow).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
//            findViewById(R.id.iv_leftarrow).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));


            JSONArray cardsArray = jsonObject.optJSONArray("cards");

            if(cardsArray != null && cardsArray.length() > 0)
            {
                findViewById(R.id.rlt_dropgameview).setVisibility(View.GONE);
                isgamestarted = true;

                for (int i = 0; i < cardsArray.length() ; i++) {

                    JSONObject cardObject = cardsArray.getJSONObject(i);
                    CardModel model = new CardModel();
                    //                    model.card_id = cardObject.optString("id");

                    model.Image = cardObject.optString("card");

                    if(model.Image.substring(model.Image.length() - 1).equalsIgnoreCase("_"))
                    {
                        model.Image = removeLastChars(model.Image,1);
                    }
                    //                    Functions.showToast(context, ""+removeLastChars(model.Image,1));

                    model.card_id = cardObject.optString("card");

//                        addCardsBahar(""+ model.Image,i);


                    model.card_group = cardObject.optString("card_group");

                    cardModelArrayList.add(model);
                }

                if(cardsArray.length() > 13)
                    isCardPick = true;

                bt_sliptcard.setVisibility(View.GONE);


                if(!isRunning)
                {
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
                ((TextView)findViewById(R.id.tv_gamemessage)).setText(""+getString(R.string.please_wait_game_is_going_on));
                findViewById(R.id.rlt_dropgameview).setVisibility(View.VISIBLE);
            }

        }
        else {
            Functions.showToast(this, ""+message);
        }

    }

    private void addCardsinMainList(){

        int nextCardFlipDelay = 100;
        countvaue = 0 ;
        new CountDownTimer(cardModelArrayList.size() * nextCardFlipDelay , nextCardFlipDelay){

            @Override
            public void onTick(long millisUntilFinished) {

                AllCardFlipAnimation(cardModelArrayList.get(countvaue).Image,countvaue);
                countvaue++;
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
            Functions.showToast(this, ""+getString(R.string.minimum_grouping));
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


        //        for (int i = 0; i < templist.size() ; i++) {
        //
        //
        //            API_CALL_Sort_card_value(templist.get(i),templist.size(),i);
        //
        //        }

        loopgroupsize = 0;
        if(temp_grouplist != null)
            temp_grouplist.clear();

        temp_grouplist = templist;
        API_CALL_Sort_card_value(temp_grouplist.get(loopgroupsize),temp_grouplist.size(),loopgroupsize);


    }

    String group_params = "";

    private void API_CALL_Sort_card_value(final ArrayList<CardModel> arrayList, final int size, final int position) {

        AddSplit_to_layout();

//            HashMap params = new HashMap();
//            params.put("user_id",""+prefs.getString("user_id", ""));
//            params.put("token",""+prefs.getString("token", ""));
//
//            Submitcardslist = GetCardFromLayout();
//            params.put("json",""+Submitcardslist);
//
//            int count = 0;
//
//            group_params = "";
//
//            for (int i = 0; i < arrayList.size(); i++) {
//
//                CardModel model = arrayList.get(i);
//                count++;
//                String card_params = "card_" + count;
//
//                if(!group_params.equals(""))
//                    group_params = group_params + " , " +card_params +" : "+model.Image;
//                else
//                    group_params = card_params +" : "+model.Image;
//
//                params.put(card_params, model.Image);
//
//            }
//
////            Functions.LOGE("Rummy5Player","CardValue : Group_"+CardValue(arrayList));
//
//
//            ApiRequest.Call_Api(this, Const.card_value, params, new Callback() {
//                @Override
//                public void Responce(String resp, String type, Bundle bundle) {
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(resp);
//                        String code,message;
//                        code = jsonObject.getString("code");
//                        message = jsonObject.getString("message");
//
//                        if (code.equalsIgnoreCase("200"))
//                        {
//
//                            for (int i = 0; i < arrayList.size() ; i++) {
//
//                                CardModel model = arrayList.get(i);
//
//                                int card_value = (int) jsonObject.getJSONArray("card_value").get(0);
//                                model.group_value_params = group_params;
//                                model.group_value_response = ""+card_value;
//
//                                if(card_value == IMPURE_SEQUENCE_VALUE)
//                                    model.group_value = IMPURE_SEQUENCE;
//                                else
//                                if(card_value == PURE_SEQUENCE_VALUE)
//                                    model.group_value = PURE_SEQUENCE;
//                                else if(card_value == SET_VALUE)
//                                    model.group_value = SET;
//                                else
//                                    model.group_value = INVALID;
//
//                                model.value_grp = card_value;
//
//                            }
//
//
//                        }
//                        else if(code.equals("406"))
//                        {
//
//                            InvalidGroup(arrayList);
//                        }
//                        else {
//
//                            InvalidGroup(arrayList);
//
//                            Functions.showToast(context, ""+message);
//                        }
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                        InvalidGroup(arrayList);
//
//                    }
//
//                    isGroupNameSet = false;
//
//                    try {
//                        if(loopgroupsize != size)
//                        {
//                            API_CALL_Sort_card_value(temp_grouplist.get(loopgroupsize),size,loopgroupsize);
//                            loopgroupsize++;
//                        }
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//
//                    if(position == (size - 1))
//                        AddSplit_to_layout();
//
//
//                }
//            });

    }


    private String getColorCode(String card_name){
        return card_name.substring(0, 2);
    }


    private void API_CALL_drop_card(final ArrayList<CardModel> arrayList, final int countnumber) {

        if(!isMyChaal)
        {
            Functions.showToast(context, ""+getString(R.string.chaal_error_messsage));

            setTouchViewVisible(true);
            ResetCardtoPosition(arrayList,countnumber);
            isFinishDesk = false;
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




        ApiRequest.Call_Api(this, Const.drop_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        removeCardList.clear();
                        isCardPick = false;
                        isFirstChall = false;
                        DrobButtonVisible(true);
                        isViewonTouch = false;
                        bt_discard.setVisibility(View.GONE);
                        visibleChangeCardButton(false);
                        FinishButtonVisible(false);
//                        removeCardFromGroup(selectedpatti_id);


                    }
                    else {


                        isFinishDesk = false;
                        restoreRemoveCard();

//                            setTouchViewVisible(true);
//                            ResetCardtoPosition(arrayList, countnumber);

                        Functions.showToast(context, "" + message);
                    }


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    if(isViewonTouch)
                    {
                        setTouchViewVisible(true);
                        isViewonTouch = false;

                        isFinishDesk = false;
                        restoreRemoveCard();
                        ResetCardtoPosition(arrayList,countnumber);
                    }
                }

                API_CALL_status();

            }
        });

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

        GetGroupValueFromTouch(animation_type);

    }

    private boolean getCardApiCalling = false;
    private void API_CALL_get_card() {

        if(getCardApiCalling)
            return;

        getCardApiCalling = true;


        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(this, Const.get_card, params, new Callback() {
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
                        DrobButtonVisible(false);

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
                        animation_type = "pick";

                    }
                    else {
                        animation_type = "erro_pick";
                    }
                    Functions.showToast(context, ""+message);


                }
                catch (JSONException e) {
                    e.printStackTrace();
                    animation_type = "erro_pick";
                    getCardApiCalling = false;
                    RemoveCardFromArrayLists(DUMMY_CARD);
                }

                RemoveCardFromArrayLists(DUMMY_CARD);
                GetGroupValueFromTouch(animation_type);
                getCardApiCalling = false;
                API_CALL_status();


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

    private void API_CALL_get_drop_card() {

        HashMap params = new HashMap();
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        Submitcardslist = GetCardFromLayout();

        params.put("json",""+Submitcardslist);


        ApiRequest.Call_Api(this, Const.get_drop_card, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                Log.v("get_card" , " "+resp);

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code,message;
                    code = jsonObject.getString("code");
                    message = jsonObject.getString("message");

                    if (code.equalsIgnoreCase("200"))
                    {
                        JSONArray drop_cardNew = jsonObject.optJSONArray("card");
                        JSONObject cardObjectNew = drop_cardNew.getJSONObject(0);
                        String card_jk = cardObjectNew.getString("card");

//                        if (card_jk.equalsIgnoreCase("JKR1") || card_jk.equalsIgnoreCase("JKR2")){
//                            Toast.makeText(context, "Can't pick Joker card!", Toast.LENGTH_SHORT).show();
//                        }
//                        if (card_jk.equalsIgnoreCase(joker_card)){
//                            Toast.makeText(context, "Can't pick Joker card!", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
                        isCardPick = true;
                        DrobButtonVisible(false);

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
                        Functions.showToast(context, ""+message);
//                        }

                    }
                    else {
                    }
//                    Functions.showToast(context, ""+message);


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                API_CALL_status();


            }
        });


        //        cardListAdapter.notifyDataSetChanged();

    }

    private void RemoveCardFromArray() {

        if(isSplit)
        {

            if(rs_cardlist_group1.size() > 0)
            {

                for (Iterator<CardModel> it = rs_cardlist_group1.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(rp_cardlist_group2.size() > 0)
            {
                for (Iterator<CardModel> it = rp_cardlist_group2.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(bl_cardlist_group3.size() > 0)
            {
                for (Iterator<CardModel> it = bl_cardlist_group3.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(bp_cardlist_group4.size() > 0)
            {
                for (Iterator<CardModel> it = bp_cardlist_group4.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

            if(joker_cardlist_group5.size() > 0)
            {
                for (Iterator<CardModel> it = joker_cardlist_group5.iterator(); it.hasNext();) {
                    if (it.next().isSelected) {
                        it.remove();
                    }
                }
            }

        }
        else {


            for (Iterator<CardModel> it = cardModelArrayList.iterator(); it.hasNext();) {
                if (it.next().isSelected) {
                    it.remove();
                }
            }

            //            for (int i = 0; i < arraysize ; i++) {
            //                CardModel model = cardModelArrayList.get(i);
            //                if(model.isSelected)
            //                {
            //                    cardModelArrayList.remove(model);
            //                    removearray = removearray - 1;
            ////                    count++;
            //                    String card_params = "card_"+count;
            ////                    params.put(card_params,model.Image);
            //                }
            //
            //            }
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
        DrobButtonVisible(false);
        declareButtonGone();
        cardPref.edit().putString(Pref_cards+game_id,"").apply();
    }

    boolean isMyChaal = false;
    public void makeHightLightForChaal(String chaal_user_id) {

        if(chaal_user_id.equals(prefs.getString("user_id","")))
        {
            isMyChaal = true;

            findViewById(R.id.rlt_highlighted_pick).setVisibility(View.VISIBLE);
            findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.VISIBLE);
            findViewById(R.id.flUserHighlight).setVisibility(View.VISIBLE);
            findViewById(R.id.flUserHighlight).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));



            findViewById(R.id.rlt_highlighted_pick).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
            findViewById(R.id.rlt_highlighted_gadhi).startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));

        }
        else {
            isMyChaal = false;
            cancelSound();

            findViewById(R.id.rlt_highlighted_pick).setVisibility(View.INVISIBLE);
            findViewById(R.id.rlt_highlighted_gadhi).setVisibility(View.INVISIBLE);

            findViewById(R.id.rlt_highlighted_pick).clearAnimation();
            findViewById(R.id.rlt_highlighted_gadhi).clearAnimation();

            findViewById(R.id.flUserHighlight).setVisibility(View.INVISIBLE);
            findViewById(R.id.flUserHighlight).clearAnimation();



        }

        if (chaal_user_id.equals(user_id_player1)) {

            if (isProgressrun1) {
                game_declare = false;
                pStatus = 100;
                pStatusprogress = 0;
                mCountDownTimer1.start();
                isProgressrun1 = false;
                PlaySaund(R.raw.buttontouchsound);
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

        if(!Functions.checkStringisValid(chaal_user_id))
            return;


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


    Sound soundMedia = null;

    public void PlaySaund(int saund) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");

        if (value.equals("1")) {

            soundMedia.PlaySong(saund,false,context);

        } else {


        }

    }

    public void PlaySaund(int saund,boolean islooping) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");

        if (value.equals("1")) {

            soundMedia.PlaySong(saund,islooping,context);

        }

    }

    public void PlaySoundFx(int saund,boolean islooping) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");


        if (value.equals("1") && !soundMedia.isSoundPlayingFX()) {

            soundMedia.PlayFX(saund,islooping,context);

        }

    }


    public void cancelSound()
    {
        soundMedia.StopSong();
        soundMedia.StopFX();
        txt_30.setVisibility(View.INVISIBLE);
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

    boolean isGroupNameSet = false;
    int loopgroupsize = 0;
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
        animation_type = "normal";


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


        int temp_size = temp_grouplist.size();
        //        for (int i = 0; i < temp_size ; i++) {
        //
        ////            if(!isGroupNameSet)
        ////            {
        ////                isGroupNameSet = true;
        //                API_CALL_Sort_card_value(temp_grouplist.get(i),temp_grouplist.size(),i);
        ////            }
        //
        //        }

        loopgroupsize = 0;
        API_CALL_Sort_card_value(temp_grouplist.get(loopgroupsize),temp_grouplist.size(),loopgroupsize);


    }


    int grouplist_size  = 0;

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

        GameLocalLogic gameLocalLogic = GameLocalLogic.getInstance(context);
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
                Log.e("value_sequence", ""+value);

                if(value == PURE_SEQUENCE_VALUE && fistlife_position == -1)
                {
                    first_check_ui = R.drawable.ic_checkbox;
                    fistlife_position = position;
                    str_1st_life = "1";
                }

                if(value == IMPURE_SEQUENCE_VALUE || (fistlife_position != -1 && fistlife_position != position && value == PURE_SEQUENCE_VALUE)){
                    second_check_ui = R.drawable.ic_checkbox;       //check_here
                    str_2nd_life = "1";
                }

                ((ImageView)findViewById(R.id.ivFirstlifecheck)).setImageDrawable(Functions.getDrawable(context,first_check_ui));
//                str_1st_life = "";
//                Toast.makeText(context, "1st reset", Toast.LENGTH_SHORT).show();

                ((ImageView)findViewById(R.id.ivSeconlifecheck)).setImageDrawable(Functions.getDrawable(context,second_check_ui));
//                str_2nd_life = "";
//                Toast.makeText(context, "2nd reset", Toast.LENGTH_SHORT).show();

            }


        }



        if(count == 1)
        {
//            view.findViewById(R.id.iv_arrow_left).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.iv_arrow_right).setVisibility(View.VISIBLE);
        }
        else
        {
            view.findViewById(R.id.iv_arrow_left).setVisibility(View.GONE);
            view.findViewById(R.id.iv_arrow_right).setVisibility(View.GONE);
        }

        int Imageresours = 0;
        Log.e("cardlist_", String.valueOf(cardImageList));
        if(cardImageList.get(0).group_value.equalsIgnoreCase(INVALID))
        {
            isDeclareVisible = false;
            Imageresours = R.drawable.invalid_circlebg;
        }
        else {
            Imageresours = R.drawable.valid_circlebg;
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

            if(isRulesMatch)
            {
                if(!game_declare)
                    setDeclareButton();

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

            if(isFinishDesk)
            {
                Functions.SmartAlertDialog(context, "Sequence and Impure Sequence needed.", "", "Okay", "", new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {

                    }
                });
                isFinishDesk = false;
            }

        }
        else
        {

            if(isFinishDesk && (grouplist.size()-1) == count)
            {
                isFinishDesk = false;
                API_CALL_pack_game();
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
                //                Functions.showToast(MainActivity.this , tag);

            }
        });

        final ArrayList<CardModel> finalCardImageList = cardImageList;
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

                        //                        Functions.showToast(context, ""+view.getId());

                        if(group_id == v.getId())
                        {

                            Functions.showToast(context, ""+view.getId()+" / "+group_id);
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

        loopgroupsize = 0;

        animation_type = animationType;
        API_CALL_Sort_card_value(temp_grouplist.get(loopgroupsize),temp_grouplist.size(),loopgroupsize);

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

        layoutParams.setMargins(left_margin, cardTopMargin,
                right_margin, (int) convertDpToPixel(10,this));
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

    @Override
    protected void onStart() {
        super.onStart();

        if(soundMedia== null)
            soundMedia = new Sound();

    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        cancelSound();
//    }

    String animation_type = "normal";
    private static final int MAX_CLICK_DURATION = 500;
    private long startClickTime;
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

        if(selectedcardvalue.size() == 1 && !opponent_game_declare && isCardPick)
        {
            bt_discard.setVisibility(View.VISIBLE);
            FinishButtonVisible(true);
        }
        else
        {
            bt_discard.setVisibility(View.GONE);
            FinishButtonVisible(false);
        }

        if(selectedcardvalue.size() == 1 && !opponent_game_declare)
        {
            visibleChangeCardButton(true);
        }
        else
        {
            visibleChangeCardButton(false);
        }



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
            if(context != null && !context.isFinishing())
                LoadImage().load(imageResource).into(imgcards);


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

    private void onGroupsCardClick(View view, ArrayList<CardModel> arrayList, int countnumber) {

        View imgalphacard = view.findViewById(R.id.imgalphacard);

        if(game_declare || arrayList.size() == 0)
        {
            return;
        }

        float CardMargin = cardTopMargin;
        CardModel model = arrayList.get(countnumber);

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

                selectedpatti = "";
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


        if(selectedcardvalue.size() == 1 && !opponent_game_declare && isCardPick)
        {
            bt_discard.setVisibility(View.VISIBLE);
            FinishButtonVisible(true);
        }
        else
        {
            bt_discard.setVisibility(View.GONE);
            FinishButtonVisible(false);
        }

        if(selectedcardvalue.size() == 1 && !opponent_game_declare)
        {
            visibleChangeCardButton(true);
        }
        else
        {
            visibleChangeCardButton(false);
        }


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
                        //                        Functions.showToast(context, "Card Drop"+selectedpatti);
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
            //            Functions.showToast(MainActivity.this, ""+getString(R.string.sort_error_message));
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
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(_view);
        _view.startDrag(data, shadowBuilder, _view, DRAG_FLAG_OPAQUE);


        return true;

    }



    private void ResetCardtoPosition(final ArrayList<CardModel> arrayList, final int countnumber){

        isViewonTouch = false;
        isCardDrop = false;


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

                if(!touchmode.isChecked())
                {
                    return false;
                }

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

            //            Functions.LOGE("MainAcitvity","Left : "+left);

        }

    }


    public void openBuyChipsActivity(View view) {
        openBuyChipsListActivity();
    }

    private void openBuyChipsListActivity() {
        startActivity(new Intent(context, BuyChipsList.class));
    }

    public void openGameRules(View view) {
        DialogRulesRummy.getInstance(context).show();
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