package com.metagards.metagames._SevenUpGames;

import static com.metagards.metagames.Utils.Functions.ANIMATION_SPEED;
import static com.metagards.metagames._AdharBahar.Fragments.GameFragment.MY_PREFS_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
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
import com.metagards.metagames.Activity.BuyChipsList;
import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.ChipsPicker;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Animations;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Sound;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._ColorPrediction.BotsAdapter;
import com.metagards.metagames._ColorPrediction.BotsAdapterRight;
import com.metagards.metagames._ColorPrediction.Bots_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class SevenUp_Socket extends BaseActivity implements View.OnClickListener {

    RecyclerView recycle_bots, recycle_botsRight;
    ArrayList<Bots_list> bots_lists=new ArrayList<>();
    ArrayList<Bots_list> bots_listsRight=new ArrayList<>();
    BotsAdapter botsAdapter;
    BotsAdapterRight botsAdapterRight;

    private final String TAG = "SevenUp_A";
    private final String TWO_SIX = "2-6";
    private final String SEVEN = "7";
    private final String EIGHT_TWELVE = "8-12";
    Activity context = this;

    TextView txtName,txtBallence,txt_gameId,txtGameRunning,txtGameBets,tvWine,tvLose;
    Button btGameAmount;
    ImageView imgpl1circle,ivWine,ivLose,ChipstoDealer;

    View rltTwoSixe,rltEightTwovelf,rltTie;
    View rltDown,rltUp,rltTieChips;

    View ChipstoUser;
    LinearLayout lnrOnlineUser;

    private final String UP = "UP";
    private final String DOWN = "DOWN";
    private final String TIE = "tie";

    private final int DOWN_VALUE = 0;
    private final int UP_VALUE = 1;
    private final int TIE_VALUE = 2;

    private String BET_ON = "";

    private int minGameAmt = 0;
    private int maxGameAmt = 500;
    private int GameAmount = 0;
    private int StepGameAmount = 0;
    private int _30second = 30000;
    private int GameTimer = 30000;
    private int timer_interval = 1000;

    private String game_id = "";
    CountDownTimer pleasewaintCountDown;

    Sound soundMedia;
    LinearLayout lnrfollow ;
    View rltDicerollparent;
    private boolean isWine;
    Random random;

    String[] cardsColors = {"rp","rs","bl","bp"};

    public String token = "";
    int version = 0 ;

    private static final  String URL = "http://64.227.186.5:3002/seven_up_down";
    Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_up);

        Initialization();
        initSoundPool();

        setDataonUser();

//        addCategoryInView("5", R.drawable.coin_5);
        addCategoryInView("10", R.drawable.coin_10);
        addCategoryInView("50", R.drawable.coin_50);
        addCategoryInView("100", R.drawable.coin_100);
//        addCategoryInView("500", R.drawable.coin_500);
        addCategoryInView("1000", R.drawable.coin_1000);
        addCategoryInView("5000", R.drawable.coin_5000);
//        addCategoryInView("7500");

        ImageView[] dices;

        dices = new ImageView[]{
                findViewById(R.id.dice1),
                findViewById(R.id.dice2),
        };

        random = new Random();

        recycle_bots = findViewById(R.id.recycle_bots);
        recycle_bots.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        recycle_bots.setHasFixedSize(true);
        recycle_botsRight = findViewById(R.id.recycle_bots_right);
        recycle_botsRight.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        recycle_botsRight.setHasFixedSize(true);


        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[] { WebSocket.NAME };
            mSocket = IO.socket(URL,opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();
        mSocket.emit("seven_up_down_timer", "seven_up_down_timer");

        mSocket.on("seven_up_down_timer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                     //   RestartGame(false);
                        Integer intger= (Integer)args[0];
                        time_remaining = intger;

                        if (intger == 1){

                            getTextView(R.id.tvStartTimer).setText("");
                            txtGameBets.setVisibility(View.GONE);
                            stopBetAnim();

                        }else {

                            getTextView(R.id.tvStartTimer).setText(intger+"");
                            txtGameBets.setVisibility(View.VISIBLE);

                        }

                    }
                });

            }
        });

        mSocket.on("seven_up_down_status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        CardsDistruButeTimer();

                        String resp = String.valueOf(args[0]);
                        Log.e("Socket_Response",resp);

                        try {

                            JSONObject jsonObject = new JSONObject(resp);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equalsIgnoreCase("200")) {

                                JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");
                                JSONArray last_winning = jsonObject.optJSONArray("last_winning");

                                JSONArray bot_user = jsonObject.getJSONArray("bot_user");
                                show_bots_user(bot_user);

                                int my_up_bet = jsonObject.optInt("my_up_bet");
                                int my_down_bet = jsonObject.optInt("my_down_bet");
                                int my_tie_bet = jsonObject.optInt("my_tie_bet");

                                all_up_bet = jsonObject.optInt("up_bet");
                                all_down_bet = jsonObject.optInt("down_bet");
                                all_tie_bet = jsonObject.optInt("tie_bet");

                                ((TextView) findViewById(R.id.tvDownMyBet)).setText("");
                                ((TextView) findViewById(R.id.tvUpMyBet)).setText("");
                                ((TextView) findViewById(R.id.tvTieAddedAmt)).setText("");

                                if(my_down_bet > 0)
                                    ((TextView) findViewById(R.id.tvDownMyBet)).setText(""+my_down_bet);
                                if(my_up_bet > 0)
                                    ((TextView) findViewById(R.id.tvUpMyBet)).setText(""+my_up_bet);
                                if(my_tie_bet > 0)
                                    ((TextView) findViewById(R.id.tvTieAddedAmt)).setText(""+my_tie_bet);

                                int online = jsonObject.optInt("online");
                                ((TextView) findViewById(R.id.tvonlineuser))
                                        .setText(""+online);

                                for (int i = 0; i < arraygame_dataa.length() ; i++) {
                                    JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                    if(last_winning.length() > 0 && !game_id.equalsIgnoreCase(welcome_bonusObject.getString("id")))
                                    {
                                        addLastWinBetonView(last_winning);
                                    }

                                    //  GameStatus model = new GameStatus();
                                    game_id  = welcome_bonusObject.getString("id");
                                    txt_gameId.setText("GAME ID "+game_id);

                                    main_card  = welcome_bonusObject.getString("main_card");
                                    // txt_min_max.setText("Min-Max: "+main_card);
                                    status  = welcome_bonusObject.getString("status");
                                    winning  = welcome_bonusObject.getString("winning");
                                    String end_datetime  = welcome_bonusObject.getString("end_datetime");
                                    added_date  = welcome_bonusObject.getString("added_date");
                                //    time_remaining  = welcome_bonusObject.optInt("time_remaining");
                                    time_remaining = time_remaining-7;
                                    //  updated_date  = welcome_bonusObject.getString("updated_date");


                                    String uri1 = "@drawable/" + main_card.toLowerCase();  // where myresource " +
                                    int imageResource1 = getResources().getIdentifier(uri1, null,
                                            getPackageName());


                                }
//                                String onlineuSer = jsonObject.getString("online");
//                            txt_online.setText("Online User "+onlineuSer);
//                                JSONArray arrayprofile = jsonObject.getJSONArray("profile");
//
//                                for (int i = 0; i < arrayprofile.length() ; i++) {
//                                    JSONObject profileObject = arrayprofile.getJSONObject(i);
//
//                                    //  GameStatus model = new GameStatus();
//                                    user_id  = profileObject.getString("id");
//                                    user_id_player1 = user_id;
//                                    name  = profileObject.getString("name");
//                                    wallet  = profileObject.getString("wallet");
//
//                                    profile_pic  = profileObject.getString("profile_pic");
//
//                                    Picasso.get().load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);
//
//                                    //  txtBallence.setText(wallet);
//                                    txtName.setText(name);
//
//                                }

                                SharedPreferences prefs = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE);
                                user_id_player1 = prefs.getString("user_id", "");

                                JSONArray arraypgame_cards = jsonObject.getJSONArray("game_cards");

                                for (int i = 0; i < arraypgame_cards.length() ; i++) {
                                    JSONObject cardsObject = arraypgame_cards.getJSONObject(i);

                                    //  GameStatus model = new GameStatus();
                                    String card  = cardsObject.getString("card");
                                    aaraycards.add(card);

                                }
//New Game Started here ------------------------------------------------------------------------

                                if (status.equals("0") && !isGameBegning){


                                    RestartGame(false);

                                    if(time_remaining > 0)
                                    {
                                        if(all_up_bet > 0)
                                            ((TextView) findViewById(R.id.tvUpTotalAmt)).setText(""+all_up_bet);
                                        if(all_down_bet > 0)
                                            ((TextView) findViewById(R.id.tvDownTotalAmt)).setText(""+all_down_bet);
                                        if(all_tie_bet > 0)
                                            ((TextView) findViewById(R.id.tvTieTotalAmt)).setText(""+all_tie_bet);

                                  //      CardsDistruButeTimer();
                                    }
                                    else {
                                        cancelStartGameTimmer();
                                    }

                                }else if (status.equals("0") && isGameBegning){
                                    if(all_up_bet > 0)
                                        ((TextView) findViewById(R.id.tvUpTotalAmt)).setText(""+all_up_bet);
                                    if(all_down_bet > 0)
                                        ((TextView) findViewById(R.id.tvDownTotalAmt)).setText(""+all_down_bet);
                                    if(all_tie_bet > 0)
                                        ((TextView) findViewById(R.id.tvTieTotalAmt)).setText(""+all_tie_bet);
                                }

//Game Started here
                                if (status.equals("1") && !isGameBegning){
                                    VisiblePleasewaitforNextRound(true);

                                }

                                if (status.equals("1") && isGameBegning){


                                    isGameBegning = false;
                                    Log.v("game" ,"stoped");
                                    if (aaraycards.size() > 0){

                                        cancelStartGameTimmer();

                                        if (counttimerforcards != null){
                                            counttimerforcards.cancel();
                                        }


                                        counttimerforcards = new CountDownTimer(aaraycards.size()*1000, 1000) {

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                isCardsDisribute = true;

                                                makeWinnertoPlayer("");
                                                makeLosstoPlayer("");


                                                if(aaraycards != null && aaraycards.size() >= 2 && !isCardDistribute)
                                                {
//                                                CardAnimationUtils();
                                                    dicehandler();
                                                    isCardDistribute = true;
                                                }


                                            }

                                            @Override
                                            public void onFinish() {

//                                                getStatus();
                                                //secondtimestart(18);
                                                VisiblePleasewaitforNextRound(true);

                                                isCardsDisribute = false;

                                                if(betplace != null && !betplace.equalsIgnoreCase("") && betplace.equalsIgnoreCase(winning))
                                                {
                                                    AnimationUtils(true);
                                                }
                                                else {

                                                    if(betplace != null && !betplace.equalsIgnoreCase("") && !betplace.equalsIgnoreCase(winning))
                                                    {
                                                        AnimationUtils(false);
//                                                    makeLosstoPlayer(SharePref.getU_id());
                                                    }

                                                }


                                            }


                                        };

                                        isWine = false;
                                        if(Functions.isStringValid(betplace) && betplace.equalsIgnoreCase(winning))
                                        {
                                            isWine = true;
                                        }
//                                    dicehandler();

//                                    counttimerforcards.start();


                                    }



                                }else {


                                }

                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(context, message);

                                }


                            }

                            if (status.equals("1")) {
//                            VisiblePleasewaitforNextRound(true);
                                VisiblePleaseBetsAmount(false);
                            } else {
                                VisiblePleasewaitforNextRound(false);

                                if(!isConfirm)
                                    VisiblePleaseBetsAmount(true);

                                makeWinnertoPlayer("");
                                makeLosstoPlayer("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });

        diceHandler = new DiceHandler(getApplicationContext(),dices,MediaPlayer.create(SevenUp_Socket.this,R.raw.dice_rolling_effect)){
            public void onDiceResule(){

                int dicevalue = Integer.parseInt(aaraycards.get(0));
//                int dicevalue = 2;

                float diceseqrate = dicevalue / 2;
                int dice1 = (int) roundUpWithDecimal(diceseqrate,0);

//                int dice1= dicevalue > 6 ? 6 :random.nextInt(dicevalue <=6 ? dicevalue : 6);

                if(dice1 == 0)
                    dice1 = 1;

                if(dicevalue == dice1)
                    --dice1;
                else if(dicevalue == 2)
                {
                    dice1 = 1;
                }

                int dice2= dicevalue - dice1;
                dice2 = dice2 == 0 ? 1 : dice2;
                int dice1Resounse = Functions.GetResourcePath("dots_"+dice1,context);
                int dice2Resounse = Functions.GetResourcePath("dots_"+dice2,context);

                Functions.LOGE("SevenUp","dicevalue : "+dicevalue);
                Functions.LOGE("SevenUp","dice1 : "+dice1);
                Functions.LOGE("SevenUp","dice2 : "+dice2);

                dices[0].setImageDrawable(Functions.getDrawable(context,dice1Resounse));
                dices[1].setImageDrawable(Functions.getDrawable(context,dice2Resounse));

                dices[0].startAnimation(animBounce);
                dices[1].startAnimation(animBounce);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimationUtils(isWine);
                    }
                },6000);


            }
        };

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                // this code will be executed after 2 seconds
//                getStatus_bots();
//            }
//        }, 5000);

        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyChipsList.class);
                intent.putExtra("homepage", "homepage");
                startActivity(intent);
            }
        });
    }

    public void show_bots_user(JSONArray bot_user){
        bots_lists.clear();
        for (int i = 0; i < bot_user.length() ; i++) {
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = bot_user.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                assert jsonObject1 != null;
                bots_lists.add(new Bots_list(
                        jsonObject1.getString("id"),
                        jsonObject1.getString("name"),
                        jsonObject1.getString("coin"),
                        jsonObject1.getString("avatar")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        bots_listsRight.clear();
        botsAdapter = new BotsAdapter(this, bots_lists);
        recycle_bots.setAdapter(botsAdapter);

        for (int i = 3; i < bot_user.length() ; i++) {
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = bot_user.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                assert jsonObject1 != null;
                bots_listsRight.add(new Bots_list(
                        jsonObject1.getString("id"),
                        jsonObject1.getString("name"),
                        jsonObject1.getString("coin"),
                        jsonObject1.getString("avatar")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        botsAdapterRight = new BotsAdapterRight(this, bots_listsRight);
        recycle_botsRight.setAdapter(botsAdapterRight);

    }

    public static float roundUpWithDecimal(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public void openGameRules(View view) {
        DialogRulesSeven.getInstance(SevenUp_Socket.this).show();
    }

    String tagamountselected = "";
    private void addCategoryInView(String cat, int img) {

        View view = LayoutInflater.from(context).inflate(R.layout.cat_txtview_chip_bg,  null);
        TextView txtview = view.findViewById(R.id.txt_cat);
//        txtview.setVisibility(View.INVISIBLE);
        txtview.setText(cat+"");
        txtview.setBackgroundResource(img);
        view.setTag(cat+"");


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
                GameAmount = Integer.parseInt(tagamountselected);
            }
        });
        lnrfollow.addView(view);

    }

    private void setSelectedType(String type) {

        LinearLayout lnrfollow = findViewById(R.id.lnrfollow);

        for (int i = 0; i < lnrfollow.getChildCount(); i++) {

            View view = lnrfollow.getChildAt(i);
            TextView txtview = view.findViewById(R.id.txt_cat);
            RelativeLayout relativeLayout = view.findViewById(R.id.relativeChip);

            if(txtview.getText().toString().equalsIgnoreCase(type)){
                relativeLayout.setBackgroundResource(R.drawable.glow_circle_bg);
//                txtview.setTextColor(Color.parseColor("#ffffff"));
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) relativeLayout.getLayoutParams();
                int _20 = (int)getResources().getDimension(R.dimen.chip_up);
                mlp.setMargins(0, _20, 0, 0);
            }else{
                relativeLayout.setBackgroundResource(R.drawable.glow_circle_bg_transparent);
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) relativeLayout.getLayoutParams();
                mlp.setMargins(0, 0, 0, 0);
//                txtview.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            }

        }

    }

    private void addDummyChipsonAndharBahar() {


        leftaddView();
        rightaddView();

        View AndharToView = rltTwoSixe;
        View AndharFromView = dummyUserleft;

        View BaharToView = rltEightTwovelf;
        View BaharFromView = dummyUserright;

        View TieFromView = dummyUserleft;
        View TieToView = rltTie;

        // View from left user bots to andhar board
        View AndharFromView1 = findViewById(R.id.left_user1);
        View AndharFromView2 = findViewById(R.id.left_user3);

        // View from right user bots to bahar board
        View BaharFromView1 = findViewById(R.id.right_user1);
        View BaharFromView2 = findViewById(R.id.right_user3);

        // View from middle left and right user to tie board
        View TieFromView1 = findViewById(R.id.left_user2);
        View TieFromView2 = findViewById(R.id.right_user2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chip Animation From Left Users
                //    DummyChipsAnimations(AndharFromView,AndharToView,rl_AnimationView);
                DummyChipsAnimations(AndharFromView1,AndharToView,rl_AnimationView);
                DummyChipsAnimations(AndharFromView2,AndharToView,rl_AnimationView);

                // Chip Animation From Right Users
                //   DummyChipsAnimations(BaharFromView,BaharToView,rl_AnimationView);
                DummyChipsAnimations(BaharFromView1,BaharToView,rl_AnimationView);
                DummyChipsAnimations(BaharFromView2,BaharToView,rl_AnimationView);


                // Chip Animation from Middle Left and Right Users To Tie Board
                //     DummyChipsAnimations(TieFromView,TieToView,rl_AnimationView);
                DummyChipsAnimations(TieFromView1,TieToView,rl_AnimationView);
                DummyChipsAnimations(TieFromView2,TieToView,rl_AnimationView);
            }
        },500);

    }

    DisplayMetrics metrics;
    int twosixeWidth = 0,twosixHeight = 0;
    int tieWidth = 0,tieHeight = 0;
    float twosixX,twosixY;
    float tieX,tieY;
    private void initDisplayMetrics(){
        metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        rltTwoSixe.post(new Runnable() {
            @Override
            public void run() {
                twosixeWidth = rltTwoSixe.getWidth();
                twosixHeight = rltTwoSixe.getHeight();

                twosixX = rltTwoSixe.getX();
                twosixY = rltTwoSixe.getY();
            }
        });

        rltTie.post(new Runnable() {
            @Override
            public void run() {
                tieWidth = rltTie.getWidth();
                tieHeight = rltTie.getHeight();

                tieX = rltTie.getX();
                tieY = rltTie.getY();
            }
        });
    }


    private void DummyChipsAnimations(View mfromView,View mtoView,ViewGroup rl_AnimationView){

        animationon = true;


        final View fromView, toView;

        fromView = mfromView;
        toView = mtoView;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

//        int toLoc[] = new int[2];
//        toView.getLocationOnScreen(toLoc);
//        float destX = toLoc[0];
//        float destY = toLoc[1];

        Rect myViewRect = new Rect();
        toView.getGlobalVisibleRect(myViewRect);
        float destX = myViewRect.left;
        float destY = myViewRect.top;

        rl_AnimationView.setVisibility(View.VISIBLE);

        ImageView image_chips = creatDynamicChips();

        rl_AnimationView.addView(image_chips);

        if(chips_width <= 0)
        {
            int chips_size = (int) getResources().getDimension(R.dimen.chips_size);
            chips_width = chips_size;
        }

        int boardWidth = twosixeWidth,boardHeight = twosixHeight;
        float boardX = twosixX, boardY = twosixY;

        Functions.LOGD("boardHeight","twosixHeight : "+twosixHeight);
//        Functions.LOGD("DragonTiger","boardY : "+boardY);

        if(toView.getId() == R.id.rltseven)
        {
            boardWidth = tieWidth;
            boardHeight = tieHeight;
            boardX = tieX;
            boardY = tieY;
        }

        int centreX = (int) (boardWidth / 2) - (chips_width  / 2);
        int centreY = (int) (boardHeight / 2) - (chips_width  / 2);

        if(chips_width > 0)
        {
            destX  = destX + new Random().nextInt(boardWidth - chips_width);
        }
        else
            destX += centreX;

        destY += centreY;


        Animations anim = new Animations();
        float finalDestX = destX;
        float finalDestY = destY;
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                animationon = false;


            }
        });
        image_chips.setAnimation(a);
        a.startNow();

//        PlaySaund(R.raw.teenpattichipstotable);
        playSound(CHIPS_SOUND,false);


    }

    int chips_width  = 0;
    private ImageView creatDynamicChips() {
        ImageView chips = new ImageView(this);

        int chips_size = (int) getResources().getDimension(R.dimen.chips_size);

        chips.setImageDrawable(Functions.getDrawable(context, ChipsPicker.getInstance().getChip()));

        chips.setLayoutParams(new ViewGroup.LayoutParams(chips_size, chips_size));

        chips.post(new Runnable() {
            @Override
            public void run() {
                chips_width = chips.getWidth();
            }
        });

        return chips;
    }


    ImageView  dummyUserleft,dummyUserright;
    public View leftaddView() {
        dummyUserleft = new ImageView(this);
        int user_size = (int) getResources().getDimension(R.dimen.user_size);
        dummyUserleft.setLayoutParams(new ViewGroup.LayoutParams(user_size, user_size));
        rl_AnimationView.addView(dummyUserleft);


        int leftMargin = 0;
//        leftMargin = new Random().nextInt(metrics.widthPixels - dummyUser.getWidth());;
        int topMargin = new Random().nextInt(metrics.heightPixels - 2*dummyUserleft.getHeight());;

        Functions.setMargins(dummyUserleft, leftMargin, topMargin, 0, 0);

        return dummyUserleft;
    }
    public View rightaddView() {
        dummyUserright = new ImageView(this);
//        dummyUserright.setImageResource(R.drawable.ic_user_male);
        int user_size = (int) getResources().getDimension(R.dimen.user_size);
        dummyUserright.setLayoutParams(new RelativeLayout.LayoutParams(user_size, user_size));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)dummyUserright.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        dummyUserright.setLayoutParams(params);

        rl_AnimationView.addView(dummyUserright);


        int leftMargin = 0;
//        leftMargin = new Random().nextInt(metrics.widthPixels - dummyUser.getWidth());;
        int topMargin = new Random().nextInt(metrics.heightPixels - 2*dummyUserright.getHeight());;

        Functions.setMargins(dummyUserleft, leftMargin, topMargin, 0, 0);

        return dummyUserleft;
    }



    private void Initialization() {

        soundMedia = new Sound();
        rl_AnimationView = ((RelativeLayout)findViewById(R.id.sticker_animation_layout));
        ChipstoDealer = findViewById(R.id.ChipstoDealer);
        Glide.with(getApplicationContext()).load(Functions.getDrawable(context, R.drawable.girl_char_seven)).into(ChipstoDealer);

        ChipstoUser = findViewById(R.id.ChipstoUser);
        rltDicerollparent = findViewById(R.id.rltDicerollparent);
        btGameAmount = findViewById(R.id.btGameAmount);
        lnrfollow  = findViewById(R.id.lnrfollow);

        txtName = findViewById(R.id.txtName);
        imgpl1circle = findViewById(R.id.imgpl1circle);

        txtBallence = findViewById(R.id.txtBallence);
        txt_gameId = findViewById(R.id.txt_gameId);
        txtGameRunning = findViewById(R.id.txtGameRunning);
        txtGameBets = findViewById(R.id.txtGameBets);

        lnrOnlineUser = findViewById(R.id.lnrOnlineUser);

        ivWine = findViewById(R.id.ivWine);
        ivLose = findViewById(R.id.ivlose);
        tvWine = findViewById(R.id.tvWine);
        tvLose = findViewById(R.id.tvlose);

        rltwinnersymble1=findViewById(R.id.rltwinnersymble1);
        rtllosesymble1=findViewById(R.id.rtllosesymble1);

        rltTwoSixe=findViewById(R.id.rlttwosix);
        rltEightTwovelf=findViewById(R.id.rlteighttwelf);
        rltTie=findViewById(R.id.rltseven);

        rltDown =findViewById(R.id.rltDragonChips);
        rltUp =findViewById(R.id.rltTigerChips);
        rltTieChips=findViewById(R.id.rltTieChips);

        findViewById(R.id.imgback).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1plus).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1minus).setOnClickListener(this::onClick);

        initDisplayMetrics();


        pleaswaitTimer();
        RestartGame(true);

        setDataonUser();

     //   startService();

        initiAnimation();

    }

    private void visibleDiceview(){
        rltDicerollparent.setVisibility(View.VISIBLE);
        setHighlightedBackgroundforCard(Integer.parseInt(winning));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                rltDicerollparent.setVisibility(View.GONE);
//            }
//        },2*1000);
    }

    private void goneDiceview(){
        rltDicerollparent.setVisibility(View.INVISIBLE);
    }

    private void initiAnimation() {
        blinksAnimation = AnimationUtils.loadAnimation(context,R.anim.blink);
        blinksAnimation.setDuration(1000);
        blinksAnimation.setRepeatCount(Animation.INFINITE);
        blinksAnimation.setStartOffset(700);
    }

    boolean isCardsDisribute = false;
    int timertime = 4000;
    Timer timerstatus;

//    private void startService() {
//
//        timerstatus = new Timer();
//        timerstatus.scheduleAtFixedRate(new TimerTask() {
//
//                                            @Override
//                                            public void run() {
//
//                                                // if (table_id.trim().length() > 0) {
//
//                                                if (isCardsDisribute) {
//
//
//                                                } else {
//
//                                                    CALL_API_getGameStatus();
//
//                                                }
//
//
//                                                // }
//
//                                            }
//
//                                        },
////Set how long before to start calling the TimerTask (in milliseconds)
//                200,
////Set the amount of time between each execution (in milliseconds)
//                timertime);
//
//    }

    public void putBetonTwoSix(View view){
        AddBet(DOWN);

    }

    public void putBetonEightTwelve(View view)
    {
        AddBet(UP);
    }

    public void putBetonSeven(View view){
        AddBet(TIE);
    }

    CountDownTimer gameStartTime;
    boolean isGameTimerStarted = false;
    private void CardsDistruButeTimer(){

        if(isGameTimerStarted && getTextView(R.id.tvStartTimer).getVisibility() == View.VISIBLE)
            return;

        gameStartTime = new CountDownTimer((time_remaining * timer_interval),timer_interval) {
            @Override
            public void onTick(long millisUntilFinished) {

                isGameTimerStarted = true;
                float count = millisUntilFinished/timer_interval;

            //    getTextView(R.id.tvStartTimer).setVisibility(View.VISIBLE);
            //    getTextView(R.id.tvStartTimer).setText(""+count);

                addDummyChipsonAndharBahar();
//                addDummyChipsonAndharBahar();

                if(isTimerStar)
                    return;

                isTimerStar = true;
//                playSound(COUNTDOWN_SOUND,true);

            }

            @Override
            public void onFinish() {
                isTimerStar =false;
                stopSound(COUNTDOWN_SOUND);
                stopPlaying();
                isGameTimerStarted = false;
           //     getTextView(R.id.tvStartTimer).setVisibility(View.INVISIBLE);
            }
        };


        gameStartTime.start();

    }

    private void cancelStartGameTimmer(){
        if(gameStartTime != null)
        {
            gameStartTime.cancel();
            gameStartTime.onFinish();
        }
    }

    private TextView getTextView(int id){

        return ((TextView) findViewById(id));
    }

    @Override
    protected void onDestroy() {
        DestroyGames();
        releaseSoundpoll();
        super.onDestroy();
    }

    private void DestroyGames(){

        cancelStartGameTimmer();

        if (timerstatus !=null ){
            timerstatus.cancel();
        }

        stopPlaying();
        releaseSoundpoll();
        mSocket.disconnect();
    }

    public String main_card;
    public String status = "";
    public String winning;
    private String added_date;
    private String user_id,name,wallet;
    private String profile_pic;
    ArrayList<String> aaraycards  = new ArrayList<>();
    boolean isGameBegning = false;
    boolean isConfirm = false;
    String bet_id = "";
    String betplace = "";
    boolean canbet = false;
    CountDownTimer counttimerforstartgame;
    CountDownTimer counttimerforcards;
    int time_remaining;
    boolean isCardDistribute = false;
    int room_id = 1;

    int all_up_bet = 0;
    int all_down_bet = 0;
    int all_tie_bet = 0;

    DiceHandler diceHandler;
    private void dicehandler() {
        visibleDiceview();
        diceHandler.rollDice();
    }

    int coins_count = 10;
    int cards_count = 2;
    boolean isbetonDragon = false;
    boolean isbetonTiger = false;
    boolean isbetTie = false;

    private void AnimationUtils(boolean iswin) {
        coins_count =10;
        isbetonDragon = false;
        isbetonTiger = false;
        isbetTie = false;

        isbetonDragon = BET_ON.equals(UP) ? true : false;
        isbetonTiger = BET_ON.equals(DOWN) ? true : false;
        isbetTie = BET_ON.equals(TIE) ? true : false;

        View fromView = null;
        View toView = null;

        if(isbetonDragon)
        {
            fromView = rltDown;
        }
        else if(isbetonTiger)
        {
            fromView = rltUp;
        }
        else {
            fromView = rltTieChips;
        }

        if(iswin)
        {
            toView = ChipstoUser;
        }
        else {
            toView = lnrOnlineUser;
        }

        View finalFromView = fromView;
        View finalToView = toView;
        new CountDownTimer(2000,200) {
            @Override
            public void onTick(long millisUntilFinished) {
                coins_count--;
                ChipsAnimations(finalFromView, finalToView,iswin);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void VisiblePleaseBetsAmount(boolean visible){

        txtGameBets.setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    private void VisiblePleasewaitforNextRound(boolean visible){

        if(blinksAnimation != null)
        {
            isBlinkStart = false;
            txtGameRunning.clearAnimation();
            blinksAnimation.cancel();
        }

        txtGameRunning.setVisibility(visible ? View.VISIBLE : View.GONE);

        if(visible)
        {
            if(!Functions.checkisStringValid(((TextView) findViewById(R.id.txtcountdown)).getText().toString().trim()))
                pleasewaintCountDown.start();

            BlinkAnimation(txtGameRunning);
        }
        else {
            pleasewaintCountDown.cancel();
            pleasewaintCountDown.onFinish();
        }


    }

    private void pleaswaitTimer(){
        pleasewaintCountDown = new CountDownTimer(8000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long second = millisUntilFinished/1000;

                ((TextView) findViewById(R.id.txtcountdown)).setText(second+"s");

            }

            @Override
            public void onFinish() {
                ((TextView) findViewById(R.id.txtcountdown)).setText("");
            }
        };
    }

    Animation blinksAnimation;
    boolean isBlinkStart = false;
    private void BlinkAnimation(final View view) {

        if(isBlinkStart)
            return;

        isBlinkStart = true;
        view.startAnimation(blinksAnimation);

        UserProfile();
    }

    private void putbet(final String type) {


        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("game_id", game_id);
        params.put("bet", type);
        params.put("amount", ""+GameAmount);

        ApiRequest.Call_Api(context, Const.SevenupPUTBET, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {

                    try {


                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");


                        if (code.equalsIgnoreCase("200")) {
                            bet_id = jsonObject.getString("bet_id");
                            wallet = jsonObject.getString("wallet");
                            txtBallence.setText(wallet);
                            Functions.showToast(context, "Bet has been added successfully!");

//                            GameAmount = 50;
//                            isConfirm = true;

                            VisiblePleaseBetsAmount(false);
                       //     CALL_API_getGameStatus();

                        } else {
                            RemoveChips();

                            if (jsonObject.has("message")) {

                                Functions.showToast(context, message);


                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        RemoveChips();
                    }
                }


            }
        });
    }

    private void cancelbet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SevenupCENCEL_BET,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")){

                                wallet = jsonObject.getString("wallet");
                                txtBallence.setText(wallet);


                            }
                            Functions.showToast(context, message);



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
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));

                params.put("game_id", game_id);
                params.put("bet_id", bet_id);
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

    private void repeatBet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SevenupREPEAT_BET,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Log.v("Repeat Responce" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")){

                                wallet = jsonObject.getString("wallet");
                                String  bet = jsonObject.getString("bet");
                                // bet_id = jsonObject.getString("bet_id");
                                String amount = jsonObject.getString("amount");
                                txtBallence.setText(wallet);
                                GameAmount = Integer.parseInt(amount);
                                betplace = bet;
                                if (bet.equals("0")){


                                }else {

                                }

                            }
                            Functions.showToast(context, message);



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
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));

                params.put("game_id", game_id);
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

    private void setDataonUser() {

        txtName.setText(""+ SharePref.getInstance().getString(SharePref.u_name));
        txtBallence.setText(Variables.CURRENCY_SYMBOL+""+ SharePref.getInstance().getString(SharePref.wallet));

        Glide.with(context)
                .load(Const.IMGAE_PATH + SharePref.getInstance().getString(SharePref.u_pic))
                .placeholder(R.drawable.avatar)
                .into(imgpl1circle);


    }

    String user_id_player1 = "null";
    RelativeLayout rltwinnersymble1;
    View rtllosesymble1;
    public void makeWinnertoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);
        addWinLoseImageonView();

        if (chaal_user_id.equals(user_id_player1)) {

            int sound = R.raw.tpb_battle_won;

            PlaySaund(sound);
//            rltwinnersymble1.setVisibility(View.VISIBLE);
        }

    }

    public void makeLosstoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);
        rtllosesymble1.setVisibility(View.GONE);
        addWinLoseImageonView();

        int sound = R.raw.game_loos_track;


        if (chaal_user_id.equals(user_id_player1)) {
            PlaySaund(sound);
//            rtllosesymble1.setVisibility(View.VISIBLE);

        }

    }

    public void addWinLoseImageonView(){

        ivWine.setImageDrawable(isbetonDragon ?
                Functions.getDrawable(context,R.drawable.ic_dt_dragon_win)
                : isbetonTiger ? Functions.getDrawable(context,R.drawable.ic_dt_tiger_win)
                : Functions.getDrawable(context,R.drawable.ic_dt_tiegame));

        ivLose.setImageDrawable(isbetonDragon ?
                Functions.getDrawable(context,R.drawable.ic_dt_dragon_win)
                : isbetonTiger ? Functions.getDrawable(context,R.drawable.ic_dt_tiger_win)
                : Functions.getDrawable(context,R.drawable.ic_dt_tiegame));

        tvWine.setText(isbetonDragon ? "Dragon Win"
                : isbetonTiger ? "Tiger Win"
                : "Tie Win");

        tvLose.setText(isbetonDragon ? "Dragon Lose"
                : isbetonTiger ? "Tiger Lose"
                : "Tie Lose");

    }

    private MediaPlayer mp;
    boolean isInPauseState = false;
    public void PlaySaund(int saund) {

        if(!SharePref.getInstance().isSoundEnable())
            return;

        if (!isInPauseState) {
            stopPlaying();
            mp = MediaPlayer.create(this, saund);
            mp.start();

        }


    }

    public void PlaySaund(int sound,boolean isloop) {

        if(!SharePref.getInstance().isSoundEnable())
            return;

        if (!isInPauseState) {
            stopPlaying();

            soundMedia.PlaySong(sound,isloop,context);
        }


    }



    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }

        soundMedia.StopSong();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isInPauseState = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isInPauseState = true;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgback:
            {
                onBackPressed();
            }
            break;

            case R.id.imgpl1plus:
            {

                ChangeGameAmount(true);
            }
            break;

            case R.id.imgpl1minus:
            {
                ChangeGameAmount(false);
            }
            break;
        }
    }

    private void ChangeGameAmount(boolean isPlus){


        if (isConfirm) {
            return;

        }

        if(isPlus && GameAmount < maxGameAmt)
        {
            GameAmount = GameAmount + StepGameAmount ;
        }
        else if(!isPlus && GameAmount > minGameAmt)
        {
            GameAmount = GameAmount - StepGameAmount ;
        }

        btGameAmount.setText(Variables.CURRENCY_SYMBOL+""+GameAmount);
    }

    private void AddBet(String beton) {

        if(!betValidation())
            return;

        BET_ON = beton;

        betplace = beton.equals(UP) ? ""+UP_VALUE: beton.equals(DOWN) ? ""+DOWN_VALUE : ""+TIE_VALUE;

//        rltTigerChips.setVisibility(beton.equals(TIGER) ? View.VISIBLE : View.GONE);
//        rltDragonChips.setVisibility(beton.equals(DRAGON) ? View.VISIBLE : View.GONE);
//        rltTieChips.setVisibility(beton.equals(TIE) ? View.VISIBLE : View.GONE);
        if(beton.equals(UP))
            rltUp.setVisibility(View.VISIBLE);
        if(beton.equals(DOWN))
            rltDown.setVisibility(View.VISIBLE);
        if(beton.equals(TIE))
            rltTieChips.setVisibility(View.VISIBLE);

        if(GameAmount > 0 && betValidation())
            putbet(betplace);
        else
        {
            rltUp.setVisibility(View.GONE);
            rltDown.setVisibility(View.GONE);
            rltTieChips.setVisibility(View.GONE);
            Functions.showToast(context,"First Select Bet amount");
        }

    }

    private void RestartGame(boolean isFromonCreate){

        ((TextView) findViewById(R.id.tvUpTotalAmt)).setText("");
        ((TextView) findViewById(R.id.tvDownTotalAmt)).setText("");
        ((TextView) findViewById(R.id.tvTieTotalAmt)).setText("");

        restartBetPutView();
        RemoveChips();
        goneDiceview();
        VisiblePleasewaitforNextRound(false);

        cancelStartGameTimmer();

        isCardDistribute = false;

   //     txtBallence.setText(wallet);

        removeBet();
        aaraycards.clear();
        if(!isFromonCreate)
            isGameBegning = true;

        setSelectedType("");

    }

    private void addLastWinBet(String items) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_su_lastbet,null);
        TextView tvItems = view.findViewById(R.id.tvItems);
        tvItems.setText(""+rulesName(Integer.parseInt(items)));
        lnrcancelist.addView(view);
    }

    LinearLayout lnrcancelist;
    private void addLastWinBetonView(JSONArray last_bet) throws JSONException {
        lnrcancelist = findViewById(R.id.lnrcancelist);
        lnrcancelist.removeAllViews();
        for (int i = 0; i < last_bet.length() ; i++) {

            String lastbet = last_bet.getJSONObject(i).getString("winning");

            addLastWinBet(lastbet);
        }

    }
    private String rulesName(int ruleVal){
        switch (ruleVal){
            case 0:
                return TWO_SIX;
            case 1:
                return EIGHT_TWELVE;
            case 2:
                return SEVEN;

            default:
                return "";
        }
    }


    private void removeBet(){
        canbet = true;
        isConfirm = false;
        bet_id = "";
        betplace="";
        GameAmount =0;
    }

    private void RemoveChips(){
        BET_ON = "";
        rltUp.setVisibility(View.GONE);
        rltDown.setVisibility(View.GONE);
        rltTieChips.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        Functions.Dialog_CancelAppointment(context, "Confirmation", "Leave ?", new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp.equals("yes"))
                {
                    releaseSoundpoll();
                    stopPlaying();
                    finish();
                    mSocket.disconnect();
                }
            }
        });
    }

    boolean animationon = false;
    RelativeLayout rl_AnimationView;
    private void ChipsAnimations(View mfromView,View mtoView,boolean iswin){

        animationon = true;


        final View fromView, toView, shuttleView;

        fromView = mfromView;
        toView = mtoView;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl_AnimationView.setVisibility(View.VISIBLE);
//        rl_AnimationView.removeAllViews();
        final ImageView sticker = new ImageView(this);

        int stickerId = Functions.GetResourcePath("ic_dt_chips",context);

        int chips_size = (int) getResources().getDimension(R.dimen.chips_size);

        if(stickerId > 0)
            try {
                LoadImage().load(stickerId).into(sticker);
            }catch (Exception e){}

        sticker.setLayoutParams(new ViewGroup.LayoutParams(chips_size, chips_size));
        rl_AnimationView.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.setVisibility(View.GONE);
                if(coins_count <= 0)
                {
                    RemoveChips();
                    rl_AnimationView.removeAllViews();
                    if(!iswin)
                        makeLosstoPlayer(SharePref.getU_id());
                    else
                        makeWinnertoPlayer(SharePref.getU_id());
                }

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

        playSound(CHIPS_SOUND,false);


    }

    private void CardAnimationAnimations(View mfromView,View mtoView,boolean isTiger){

        animationon = true;


        final View fromView, toView, shuttleView;

        fromView = mfromView;
        toView = mtoView;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl_AnimationView.setVisibility(View.VISIBLE);
//        rl_AnimationView.removeAllViews();
        final ImageView sticker = new ImageView(this);

        int stickerId = Functions.GetResourcePath("backside_card",context);

        int cards_width = (int) getResources().getDimension(R.dimen.dt_card_width);
        int cards_size = (int) getResources().getDimension(R.dimen.dt_card_hight);

        if(stickerId > 0)
            LoadImage().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(cards_size, cards_size));
        rl_AnimationView.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.setVisibility(View.GONE);
                rl_AnimationView.removeAllViews();


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

    private RequestManager LoadImage()
    {
        return  Glide.with(context);
    }

    private boolean betValidation(){

        if (isConfirm) {

            Functions.showToast(context, "Bet Already Confirmed So Not Allowed to Put again");
            return false;

        } else if (!canbet) {
            Functions.showToast(context, "Game Already Started You can not Bet");
            return false;

        }

        return true;
    }

    public void confirmBooking(View view) {

        if(Functions.isStringValid(betplace) && betValidation())
            putbet(betplace);
    }


    public void cancelBooking(View view) {

        cancelbet();
        removeBet();
        RemoveChips();
    }

    public static final int COUNTDOWN_SOUND = 0;
    public static final int CHIPS_SOUND = 1;
    public static final int CARD_SOUND = 2;

    SoundPool mSoundPool;
    HashMap<Integer, Integer> mSoundMap;
    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        mSoundMap = new HashMap<Integer, Integer>();

        if(mSoundPool != null){
            mSoundMap.put(COUNTDOWN_SOUND, mSoundPool.load(this, R.raw.teenpattitick, 1));
            mSoundMap.put(CHIPS_SOUND, mSoundPool.load(this, R.raw.teenpattichipstotable, 1));
            mSoundMap.put(CARD_SOUND, mSoundPool.load(this, R.raw.teenpatticardflip_android, 1));
        }
    }
    /*
     *Call this function from code with the sound you want e.g. playSound(SOUND_1);
     */
    boolean isTimerStar ;
    public void playSound(int sound,boolean loop) {

        if(!SharePref.getInstance().isSoundEnable())
            return;

        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if(mSoundPool != null){
            mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, loop ? -1 : 0, 1.0f);
        }
    }

    public void stopSound(int sound){
        if(mSoundMap.get(sound) != null)
            mSoundPool.stop(mSoundMap.get(sound));
    }

    public void releaseSoundpoll(){
        stopSound(COUNTDOWN_SOUND);
        stopSound(CARD_SOUND);
        stopSound(CHIPS_SOUND);
        if(mSoundPool != null)
        {
            mSoundMap.clear();
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    ImageView ivBetStatus;

    private void stopBetAnim(){
        ivBetStatus = findViewById(R.id.ivBetStatus);
        findViewById(R.id.rltBetStatus).setVisibility(View.VISIBLE);
        ivBetStatus.setBackgroundResource(R.drawable.iv_bet_stops);

        ivBetStatus.bringToFront();
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(200);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        ivBetStatus.startAnimation(fade_in);
        fade_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivBetStatus.clearAnimation();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        winCardDistribution();
                        dicehandler();
                    }
                }, 1500);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.rltBetStatus).setVisibility(View.GONE);
            }
        }, 1250);
    }

    private void winCardDistribution() {
        animationon = true;


        int winingCard = getWiningCard();
        ((ImageView)findViewById(R.id.ivWinCard))
                .setImageDrawable(Functions.getDrawable(context,winingCard));

        final View fromView, toView, shuttleView;

        fromView = findViewById(R.id.ivCardGadhi);
        toView = findViewById(R.id.ivWinCard);


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl_AnimationView.setVisibility(View.VISIBLE);
        final ImageView sticker = new ImageView(this);

        int stickerId = Functions.GetResourcePath("backside_card",context);

        int cards_size = (int) getResources().getDimension(R.dimen.dt_card_hight);

        if(stickerId > 0)
            LoadImage().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(cards_size, cards_size));
        rl_AnimationView.addView(sticker);

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                rl_AnimationView.removeAllViews();
                visibleDiceview();
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

    private int getWiningCard() {
        int dicevalue = Integer.parseInt(aaraycards.get(0));

        int size = cardsColors.length;
        String cardColor = cardsColors[new Random().nextInt(size)];
        String cardNumber = ConvertNumbertoSpecial(""+dicevalue);
        Functions.LOGD(TAG,"cardColor: "+cardColor);
        Functions.LOGD(TAG,"cardNumber: "+cardNumber);
        return Functions.GetResourcePath(cardColor+cardNumber,context);
    }

    private String ConvertNumbertoSpecial(String $card1_num_set){

        $card1_num_set = $card1_num_set.equalsIgnoreCase("11") ? "J" : $card1_num_set;
        $card1_num_set = $card1_num_set.equalsIgnoreCase("12") ? "Q" : $card1_num_set;
        $card1_num_set = $card1_num_set.equalsIgnoreCase("13") ? "K" : $card1_num_set;
        $card1_num_set = $card1_num_set.equalsIgnoreCase("14") ? "A" : $card1_num_set;


        return $card1_num_set;
    }

    private void setHighlightedBackgroundforCard(int winning_rule_value) {
        int drawableHighlight = R.drawable.ic_seven_highlighted;
        View viewHighlight = null;
        switch (winning_rule_value)
        {
            case DOWN_VALUE:
                viewHighlight = rltTwoSixe;
                viewHighlight.setBackground(Functions.getDrawable(context,R.drawable.win_two));
                break;
            case TIE_VALUE:
                viewHighlight =rltTie;
                viewHighlight.setBackground(Functions.getDrawable(context,R.drawable.win_seven));
                break;
            case UP_VALUE:
                viewHighlight =rltEightTwovelf;
                viewHighlight.setBackground(Functions.getDrawable(context,R.drawable.win_eight));
                break;

        }
       // viewHighlight.setBackground(Functions.getDrawable(context,drawableHighlight));
        BlinkAnimation(viewHighlight);
    }

    private void restartBetPutView(){
        int drawableDefault = R.drawable.ic_seven_up_box_up_left;
        int drawableDefault_mid = R.drawable.ic_seven_up_box_up_mid;
        int drawableDefault_right = R.drawable.ic_seven_up_box_up_right;
        rltTwoSixe.clearAnimation();
        rltTie.clearAnimation();
        rltEightTwovelf.clearAnimation();
        rltTwoSixe.setBackground(Functions.getDrawable(context,drawableDefault));
        rltTie.setBackground(Functions.getDrawable(context,drawableDefault_mid));
        rltEightTwovelf.setBackground(Functions.getDrawable(context,drawableDefault_right));
    }

    private void UserProfile() {

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE);
        params.put("id", prefs.getString("user_id", ""));
        params.put("fcm", token);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        params.put("app_version", version + "");
        params.put("token", prefs.getString("token", ""));

        ApiRequest.Call_Api(context, Const.PROFILE, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        Log.d("profile_res", resp);
                        String code = jsonObject.getString("code");
                        if (code.equalsIgnoreCase("200")) {
                            JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
                            wallet = jsonObject0.optString("wallet");
                            txtBallence.setText(wallet);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

}