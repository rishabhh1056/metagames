package com.metagards.metagames._AdharBahar;

import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;
import static com.metagards.metagames.Utils.Functions.ANIMATION_SPEED;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
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
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
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
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._AdharBahar.menu.DialogRulesAndharBahar;
import com.metagards.metagames._ColorPrediction.BotsAdapter;
import com.metagards.metagames._ColorPrediction.BotsAdapterRight;
import com.metagards.metagames._ColorPrediction.Bots_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Andhar_Bahar_Socket extends BaseActivity {

    RecyclerView recycle_bots, recycle_botsRight;
    ArrayList<Bots_list> bots_lists=new ArrayList<>();
    ArrayList<Bots_list> bots_listsRight=new ArrayList<>();
    BotsAdapter botsAdapter;
    BotsAdapterRight botsAdapterRight;

    private final int ANDHAR = 0;
    private final int BAHAR = 1;
    LinearLayout lnrfollow ;
    LinearLayout lnrandarpatte,lnrbaharpatte;
    RelativeLayout rltline;
    Context context =this;
    CountDownTimer counttimerforstartgame;
    CountDownTimer counttimerforcards;
    CountDownTimer counttimerforcardsforAnimation;
    int count = 0;
    private MediaPlayer mp;
    String tagamountselected = "";
    int betcountandar = 1;
    int betcountbahar = 1;
    int countvaue = 0;
    int countvaueforani = 0;
    boolean isConfirm = false;
    boolean IsFirsttimeCall = true;
    String betvalue = "";
    int widthandar = -430;
    int countwidhtbahar = 41;
    int widthbahar = -430;
    int countwidhtanadar = 41;
    int firstcount = 0;
    TextView txtGameFinish,txtName,txtBallence,txt_catander,txt_catbahar,txt_room,txt_gameId,txt_online,txt_min_max,txt_many_cards;
    ImageView imgmaincard;
    Typeface helvatikaboldround;
ImageView txtGameRunning,txtGameBets;
    ArrayList<String> aaraycards;
    boolean isGameBegning = false;
    int timertime = 4000;
    int cardAnimationIntervel = 200;
    public String game_id;
    public String room_id;
    public String min_coin;
    public String max_coin;
    public String main_card;
    public String winning;
    public String status = "";
    String bet_id = "";
    private String added_date;
    int time_remaining;
    private int timer_interval = 1000;
    private String user_id,name,wallet;
    Timer timerstatus;
    ImageView imgCardsandar,imgCardsbahar;
    Animation setanimation;
    ImageView imgmaincardsvaluehiostory;
    String betplace = "";
    boolean canbet = false;
    boolean isInPauseState = false;
    boolean isCardsDisribute = false;
    Button btnCANCEL,btnRepeat,btnDouble,btnconfirm;
    RelativeLayout rltandarbet,rltbaharbet,rltmainviewander,rltmainviewbahar,rlt41more,rlt1to5,rlt6to10,rlt11to15,rlt16to25,rlt26to30,rlt31to35,rlt36to40;

    ImageView imgpl1circle;
    LinearLayout lnrOnlineUser;
    View ChipstoUser;
    private boolean isCountDownStart;

    private static final  String URL = "http://64.227.186.5:3002/ander_bahar";
    Socket mSocket;

    public String token = "";
    int version = 0 ;

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Functions.Dialog_CancelAppointment(Andhar_Bahar_Socket.this, "Confirmation", "Leave ?", new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp.equals("yes"))
                {
                    DestroyGames();
                    releaseSoundpoll();
                    finish();
                    mSocket.disconnect();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andhar_bahar_newui);

        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[] { WebSocket.NAME };
            mSocket = IO.socket(URL,opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.connect();
        mSocket.emit("ander_bahar_timer", "ander_bahar_timer");

        initSoundPool();
        initview();
        initiAnimation();
        initDisplayMetrics();
        setDataonUser();

        recycle_bots = findViewById(R.id.recycle_bots);
        recycle_bots.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        recycle_bots.setHasFixedSize(true);
        recycle_botsRight = findViewById(R.id.recycle_bots_right);
        recycle_botsRight.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        recycle_botsRight.setHasFixedSize(true);

        mSocket.on("ander_bahar_timer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String resp = String.valueOf(args[0]);
                        Log.e("RES_Timer_Response",resp);
                   //     restartGame();

                    //    aaraycards.clear();
                        Integer intger= (Integer)args[0];
                        time_remaining = intger;

                        if (intger == 1){

                            getTextView(R.id.tvStartTimer).setText("0");
                            txtGameBets.setVisibility(View.GONE);

                        }else {

                            getTextView(R.id.tvStartTimer).setText(intger+"");
                            txtGameBets.setVisibility(View.VISIBLE);

                        }

                    }
                });

            }
        });


        mSocket.on("ander_bahar_status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String resp = String.valueOf(args[0]);
                        Log.e("RES_Socket_Response",resp);

                        startGameCountDown();

                        try {

                            JSONObject jsonObject = new JSONObject(resp);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equalsIgnoreCase("200")) {

                                JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");
                                JSONArray online_users = jsonObject.getJSONArray("online_users");

                                JSONArray bot_user = jsonObject.getJSONArray("bot_user");
                                show_bots_user(bot_user);

                                int my_ander_bet = jsonObject.optInt("my_ander_bet");
                                int my_bahar_bet = jsonObject.optInt("my_bahar_bet");

                                txt_catander.setText(""+my_ander_bet);
                                txt_catbahar.setText(""+my_bahar_bet);

                                ander_bet = jsonObject.optInt("ander_bet",0);
                                bahar_bet = jsonObject.optInt("bahar_bet",0);


                                ((TextView) findViewById(R.id.tvAndharTotal)).setText("");
                                ((TextView) findViewById(R.id.tvBaharTotal)).setText("");


                                int online = jsonObject.getInt("online");
                                ((TextView) findViewById(R.id.tvonlineuser))
                                        .setText(""+online);

                                for (int i = 0; i < arraygame_dataa.length() ; i++) {
                                    JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

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
                                    //  updated_date  = welcome_bonusObject.getString("updated_date");


                                    String uri1 = "@drawable/" + main_card.toLowerCase();  // where myresource " +
                                    int imageResource1 = getResources().getIdentifier(uri1, null,
                                            getPackageName());
                                    try {
                                        Glide.with(context).load(imageResource1).into(imgmaincard);
                                        Glide.with(context).load(imageResource1).into(imgmaincardsvaluehiostory);
                                    }catch (Exception e){}

                                    Random r = new Random();
                                    int randomNumber = r.nextInt(100);

                                }
                                String onlineuSer = jsonObject.getString("online");
                                txt_online.setText("Online User "+onlineuSer);
                     //           JSONArray arrayprofile = jsonObject.getJSONArray("profile");

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
//                                    Glide.with(context).load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);
//
//                                    //  txtBallence.setText(wallet);
//                                    txtName.setText(name);
//
//                                }


                                JSONArray arraypgame_cards = jsonObject.getJSONArray("game_cards");

                                for (int i = 0; i < arraypgame_cards.length() ; i++) {
                                    JSONObject cardsObject = arraypgame_cards.getJSONObject(i);

                                    //  GameStatus model = new GameStatus();
                                    String card  = cardsObject.getString("card");
                                    aaraycards.add(card);

                                }
//New Game Started here ------------------------------------------------------------------------
                                Log.e("gamestatus",status+" "+isGameBegning);
                                if (status.equals("0") && !isGameBegning){

                                    VisiblePleasewaitforNextRound(false);
                                    if (counttimerforcards != null){
                                        counttimerforcards.cancel();
                                    }

                                    if (counttimerforcardsforAnimation != null){
                                        counttimerforcardsforAnimation.cancel();
                                    }

                                    restartGame();

                                    if(ander_bet > 0)
                                        ((TextView) findViewById(R.id.tvAndharTotal)).setText(""+ander_bet);
                                    if(bahar_bet > 0)
                                        ((TextView) findViewById(R.id.tvBaharTotal)).setText(""+bahar_bet);

                               //     startGameCountDown();

                                }
                                else if (status.equals("0") && isGameBegning){

                                    if(ander_bet > 0)
                                        ((TextView) findViewById(R.id.tvAndharTotal)).setText(""+ander_bet);
                                    if(bahar_bet > 0)
                                        ((TextView) findViewById(R.id.tvBaharTotal)).setText(""+bahar_bet);

                                }

                                //Game Started here
                                if (status.equals("1") && !isGameBegning){
                                    VisiblePleasewaitforNextRound(true);
                                }

                                if (status.equals("1") && isGameBegning){

                                    isGameBegning = false;
                                    Log.v("game" ,"stoped");
                                    if (aaraycards.size() > 0){
                                        if (counttimerforcardsforAnimation != null){
                                            counttimerforcardsforAnimation.cancel();
                                        }

                                        if (counttimerforcards != null){
                                            counttimerforcards.cancel();
                                        }


                                        counttimerforcards = new CountDownTimer(aaraycards.size()*500, cardAnimationIntervel) {

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                isCardsDisribute = true;
                                                int y = countvaue % 2;

//                                                makeWinnertoPlayer("");
//                                                makeLosstoPlayer("");

                                                try {
                                                    if (y == 0) {

                                                        int chieldcount =   lnrandarpatte.getChildCount();
                                                        View view = null;
                                                        if (chieldcount > 0){
                                                            view = lnrandarpatte.getChildAt(chieldcount-1);
                                                        }else {
                                                            view = lnrandarpatte.getChildAt(chieldcount);
                                                        }


                                                        int[] locationimgandar = new int[2];
                                                        int[] locationlnrandar = new int[2];
                                                        imgCardsandar.getLocationOnScreen(locationimgandar);
                                                        lnrandarpatte.getLocationOnScreen(locationlnrandar);

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

                                                        if (locationimgandarwidth[0] < 2){

                                                        }else {
                                                            finalwidht = locationlnrandarwidth[0]-locationimgandarwidth[0] ;
                                                            animation = new TranslateAnimation(0, finalwidht,0 , locationlnrandar[1] - locationimgandar[1]);
                                                            animation.setRepeatMode(0);
                                                            animation.setDuration(300);
                                                            animation.setFillAfter(false);
                                                            imgCardsandar.setVisibility(View.VISIBLE);
//
                                                            imgCardsandar.startAnimation(animation);

                                                            firstcount++;
                                                            //  Log.v("width-1" , locationlnrandarwidth[0] +" / "+locationimgandarwidth[0]);


                                                        }

                                                        //   Log.v("width-1" , locationlnrandarwidth[0] +" / "+locationimgandarwidth[0]);
                                                        addCardinLayout(aaraycards.get(countvaue),countvaue,lnrandarpatte);
                                                        Log.e("Andar_Patte",arraypgame_cards.toString());
                                                      //  Log.e("Andar_Patte_Count",aaraycards.get(countvaue));


                                                    }else {


                                                        int finalwidhtbahar = 0;
                                                        int chieldcount =   lnrbaharpatte.getChildCount();
                                                        View view1 = null;
                                                        if (chieldcount > 0){
                                                            view1 = lnrbaharpatte.getChildAt(chieldcount-1);
                                                        }else {
                                                            view1 = lnrbaharpatte.getChildAt(chieldcount);
                                                        }

                                                        int[] locationimgbaharwidth = new int[2];
                                                        int[] locationlnrbaharwidth = new int[2];
                                                        imgCardsbahar.getLocationOnScreen(locationimgbaharwidth);
                                                        if (view1 != null){
                                                            view1.getLocationOnScreen(locationlnrbaharwidth);
                                                        }else {

                                                        }

                                                        //----------------------Close ---------------------


                                                        int[] locationimgbahar = new int[2];
                                                        int[] locationlnrbahar = new int[2];
                                                        imgCardsbahar.getLocationOnScreen(locationimgbahar);
                                                        lnrbaharpatte.getLocationOnScreen(locationlnrbahar);


                                                        if (locationimgbaharwidth[0] < 2) {


                                                        }else {

                                                            finalwidhtbahar = locationlnrbaharwidth[0]-locationimgbaharwidth[0] ;
                                                            TranslateAnimation animation = new TranslateAnimation(0, finalwidhtbahar,0 , locationlnrbahar[1]-locationimgbahar[1]);
                                                            animation.setRepeatMode(0);
                                                            animation.setDuration(300);
                                                            animation.setFillAfter(false);
                                                            imgCardsbahar.startAnimation(animation);
                                                        }

                                                        //   Log.v("width" , locationimgbaharwidth +" / "+locationlnrbaharwidth[0]);
                                                        addCardinLayout(aaraycards.get(countvaue),countvaue,lnrbaharpatte);
                                                    }
                                                }
                                                catch (IndexOutOfBoundsException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }

                                                if (countvaue > 0 && countvaue < 6){
                                                    rlt1to5.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                if (countvaue > 5 && countvaue < 11){
                                                    rlt6to10.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                if (countvaue > 10 && countvaue < 16){
                                                    rlt11to15.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                if (countvaue > 15 && countvaue < 26){
                                                    rlt16to25.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                if (countvaue > 25 && countvaue < 31){
                                                    rlt26to30.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                if (countvaue > 30 && countvaue < 36){
                                                    rlt31to35.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                if (countvaue > 35 && countvaue < 41){
                                                    rlt36to40.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                if (countvaue > 41){
                                                    rlt41more.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border_withcolor));

                                                }

                                                countvaue++;
                                           //     Log.e("Andar_Patte_CardCount",""+countvaue);
                                                if (countvaue == aaraycards.size()-1) {

                                                    boolean iswin = Functions.isStringValid(betplace)
                                                            && betplace.equalsIgnoreCase(winning) ? true : false;

                                                    Functions.LOGD("AndharBahar_NewUI","iswin : "+iswin);

                                                    AnimationUtils(iswin);
                                                    makeWinnertoPlayer(user_id_player1);
                                                }

                                            }

                                            @Override
                                            public void onFinish() {

//                                                getStatus();
                                                //secondtimestart(18);
                                                VisiblePleasewaitforNextRound(true);
                                                isCardsDisribute = false;

//                                                if(betplace != null && !betplace.equalsIgnoreCase("") && betplace.equalsIgnoreCase(winning))
//                                                {
//                                                    makeWinnertoPlayer(user_id_player1);
//                                                }
//                                                else {
//
//                                                    if(betplace != null && !betplace.equalsIgnoreCase("") && !betplace.equalsIgnoreCase(winning))
//                                                    {
//                                                        makeLosstoPlayer(user_id_player1);
//                                                    }
//
//                                                }

                                                Functions.LOGD("AndharBahar_NewUI","betplace : "+betplace);
                                                Functions.LOGD("AndharBahar_NewUI","winning : "+winning);
//
//                                                boolean iswin = Functions.isStringValid(betplace)
//                                                        && betplace.equalsIgnoreCase(winning) ? true : false;
//
//                                                Functions.LOGD("AndharBahar_NewUI","iswin : "+iswin);
//
//                                                AnimationUtils(iswin);
//                                                makeWinnertoPlayer(user_id_player1);



                                            }


                                        };

                                        counttimerforcards.start();



                                    }



                                }else {


                                }

                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(context, message);


                                }


                            }

                            if (status.equals("1")) {
                                //  txtGameRunning.setVisibility(View.VISIBLE);
                                //    txtGameBets.setVisibility(View.GONE);
                            } else {
                                // txtGameRunning.setVisibility(View.GONE);
                                //    txtGameBets.setVisibility(View.VISIBLE);

                         //       makeWinnertoPlayer("");
                                makeLosstoPlayer("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                // this code will be executed after 2 seconds
//                getStatus_bots();
//            }
//        }, 5000);

    }
    private void getStatus_bots() {
        HashMap params = new HashMap<String, String>();
        SharedPreferences prefs = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));
        params.put("room_id", "1");
        ApiRequest.Call_Api(context, Const.AnderBahar, params, new Callback() {
            @Override
            public void Responce(String response, String type, Bundle bundle) {
                if(response != null)
                {
                    try {
                        bots_lists.clear();
                        bots_listsRight.clear();
                        Functions.LOGE("AndharBahar",""+response);
                        Log.v("responce" , response);

                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equalsIgnoreCase("200")) {

                            JSONArray bot_user = jsonObject.getJSONArray("bot_user");
                            JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");

                            show_bots_user(bot_user);


                        } else {
                            if (jsonObject.has("message")) {

                                Functions.showToast(context, message);

                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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

        botsAdapter = new BotsAdapter(this, bots_lists);
        recycle_bots.setAdapter(botsAdapter);

        bots_listsRight.clear();
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

    private void addChipsonView() {

        lnrfollow.removeAllViews();
        addCategoryInView("10", R.drawable.coin_10);
        addCategoryInView("50", R.drawable.coin_50);
        addCategoryInView("100", R.drawable.coin_100);
//        addCategoryInView("500", R.drawable.coin_500);
        addCategoryInView("1000", R.drawable.coin_1000);
        addCategoryInView("5000", R.drawable.coin_5000);
//        addCategoryInView("7500");
    }

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
//                GameAmount = Integer.parseInt(tagamountselected);
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

    Animation blinksAnimation;
    boolean isBlinkStart = false;
    private void BlinkAnimation(final View view) {

        if(isBlinkStart)
            return;

        isBlinkStart = true;
        view.startAnimation(blinksAnimation);
    }

    private void restartGame(){

        ander_bet = 0;
        bahar_bet = 0;
        ((TextView) findViewById(R.id.tvAndharTotal)).setText("");
        ((TextView) findViewById(R.id.tvBaharTotal)).setText("");
        canbet = true;
  //      txtBallence.setText(wallet);
        firstcount = 0;
        count = 0;
        countwidhtanadar = 41;
        widthandar = -430;
        countwidhtbahar = 41;
        widthbahar = -430;
        isConfirm = false;
        bet_id = "";
        betplace="";
        aaraycards.clear();
        countvaue = 0;
        betcountandar = 1;
        isGameBegning = true;
        betvalue = "";
        VisiblePleasewaitforNextRound(false);
        removeBetChips();
        Log.v("startgame" ,"start");
        removeChips();
        lnrandarpatte.removeAllViews();
        lnrbaharpatte.removeAllViews();
        andharPutBetVisiblity(false);
        txt_catander.setText("0");
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.remove("tag").commit();
        setSelectedType("");
        restartAndharBaharView();
        baharPutBetVisiblity(false);
        txt_catbahar.setText("0");
        rlt1to5.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        rlt6to10.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        rlt11to15.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        rlt16to25.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        rlt26to30.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        rlt31to35.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        rlt36to40.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        rlt41more.setBackground(ContextCompat.getDrawable(context, R.drawable.background_border));
        UserProfile();

    }

    private void removeBetChips() {
        andharPutBetVisiblity(false);
        baharPutBetVisiblity(false);
    }

    private void andharPutBetVisiblity(boolean visible){
        rltmainviewander.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void baharPutBetVisiblity(boolean visible){
        rltmainviewbahar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void initiAnimation() {
        blinksAnimation = AnimationUtils.loadAnimation(context,R.anim.blink);
        blinksAnimation.setDuration(1000);
        blinksAnimation.setRepeatCount(Animation.INFINITE);
        blinksAnimation.setStartOffset(700);
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
//            if(!Functions.checkisStringValid(((TextView) findViewById(R.id.txtcountdown)).getText().toString().trim()))
//                pleasewaintCountDown.start();

          //  BlinkAnimation(txtGameRunning);
        }
        else {
//            pleasewaintCountDown.cancel();
//            pleasewaintCountDown.onFinish();
        }


    }

    private String profile_pic;
    View rtllosesymble1;
    public void initview(){

        // setanimation= AnimationUtils.loadAnimation(this,R.anim.set);

        rl_AnimationView = ((RelativeLayout)findViewById(R.id.sticker_animation_layout));

        rltwinnersymble1=findViewById(R.id.rltwinnersymble1);
        rtllosesymble1=findViewById(R.id.rtllosesymble1);

        imgpl1circle = findViewById(R.id.imgpl1circle);
        lnrOnlineUser = findViewById(R.id.lnrOnlineUser);
        ChipstoUser = findViewById(R.id.ChipstoUser);
        profile_pic = SharePref.getInstance().getString(SharePref.u_pic);
        Glide.with(context).load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);

        imgmaincardsvaluehiostory = findViewById(R.id.imgmaincardsvaluehiostory);

        txtGameRunning = findViewById(R.id.txtGameRunning);
        Glide.with(context) .asGif()
                .load(R.drawable.waiting_for_next).into(txtGameRunning);

        txtGameBets =  findViewById(R.id.txtGameBets);
        Glide.with(context) .asGif()
                .load(R.drawable.place_your_bet).into(txtGameBets);

        imgCardsandar = findViewById(R.id.imgCardsandar);
        imgCardsbahar = findViewById(R.id.imgCardsbahar);
        lnrfollow = findViewById(R.id.lnrfollow);
        lnrandarpatte = findViewById(R.id.lnrandarpatte);
        // lnrpaate = findViewById(R.id.lnrpaate);
        rltline = findViewById(R.id.rltline);
        lnrbaharpatte = findViewById(R.id.lnrbaharpatte);
        // setBackground(context,findViewById(R.id.activity_main),R.mipmap.src_gameboard);
        txt_room = findViewById(R.id.txt_room);
        txt_gameId = findViewById(R.id.txt_gameId);
        txt_online = findViewById(R.id.txt_online);
        txt_min_max = findViewById(R.id.txt_min_max);
        txt_many_cards = findViewById(R.id.txt_many_cards);
        txtGameFinish = findViewById(R.id.tvStartTimer);
     //   txtGameBets = findViewById(R.id.txtGameBets);
        imgmaincard = findViewById(R.id.imgmaincard);
        rltandarbet = findViewById(R.id.rltandarbet);
        rltmainviewander = findViewById(R.id.rltmainviewander);
        rltmainviewbahar = findViewById(R.id.rltmainviewbahar);
        rlt1to5 = findViewById(R.id.rlt1to5);
        rlt6to10 = findViewById(R.id.rlt6to10);
        rlt11to15 = findViewById(R.id.rlt11to15);
        rlt16to25 = findViewById(R.id.rlt16to25);

        rlt26to30 = findViewById(R.id.rlt26to30);
        rlt31to35 = findViewById(R.id.rlt31to35);
        rlt36to40 = findViewById(R.id.rlt36to40);
        rlt41more = findViewById(R.id.rlt41more);
        btnRepeat = findViewById(R.id.btnRepeat);

        min_coin= getIntent().getStringExtra("min_coin");
        max_coin= getIntent().getStringExtra("max_coin");
        room_id= getIntent().getStringExtra("room_id");



        txt_room.setText("ROOM ID "+room_id);

        txt_min_max.setText("Min-Max: "+min_coin+" - "+max_coin);

//        imgCards.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imgCards.startAnimation(setanimation);
//            }
//        });


        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                //  Functions.showToast(context, "Need to Discuss");

                if (betplace.length() > 0 ){

                    Functions.showToast(context, "You Already Placed Bet");

                }else {

                    repeatBet();

                }




            }
        });

        btnDouble = findViewById(R.id.btnDouble);
        btnDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();
                // Functions.showToast(context, "Need to Discuss");

                if (betplace.length() > 0){
                    float valedou = Float.parseFloat(betvalue);
                    float dobff=valedou*2;
                    betvalue=dobff+"";
//                    txt_catander.setText(""+betvalue);
//                    txt_catbahar.setText(""+betvalue);
                }else {
                    Functions.showToast(context, "You have not Bet yet. ");

                }


            }
        });


        btnconfirm = findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  finish();
                //Functions.showToast(context, "Need to Discuss");

                putbetonView();
            }
        });

        btnCANCEL = findViewById(R.id.btnCANCEL);
        btnCANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                betvalue = "";
                betplace = "";
                if (bet_id.length() > 0){

                    cancelbet();

                }else {

                    Functions.showToast(context, "You have not Bet yet. ");
                    removeBetChips();


                }

            }
        });

        txt_catander = findViewById(R.id.txt_catander);
        txt_catbahar = findViewById(R.id.txt_catbahar);
        rltandarbet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConfirm) {

                    Functions.showToast(context, "Bet Already Confirmed So Not Allowed to Put again");


                } else {

                    if (canbet) {
                        if (tagamountselected.length() > 0) {

                            if (betplace.equals("1")){
                                betvalue = "";
                            }

                            betplace = "0";
                            float valedou = 0;
                            if (betvalue.length() > 0) {
                                valedou = Float.parseFloat(betvalue);
                            } else {

                                valedou = 0;
                            }
                            float valedoutagamountselected = 0;
                            if (tagamountselected.length() > 0) {
                                valedoutagamountselected = Float.parseFloat(tagamountselected);
                            } else {

                                valedoutagamountselected = 0;
                            }


                            float betvalueint = valedou + valedoutagamountselected;


                            betvalue = betvalueint + "";
                            andharPutBetVisiblity(true);

                            putbetonView();


                        } else {

                            Functions.showToast(context, "Please Select Bet amount First");

                        }
                    } else {

                        Functions.showToast(context, "Game Already Started You can not Bet");

                    }
                }



            }
        });
        rltbaharbet = findViewById(R.id.rltbaharbet);
        rltbaharbet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isConfirm) {

                    Functions.showToast(context, "Bet Already Confirmed So Not Allowed to Put again");



                } else {


                    if (canbet) {

                        if (tagamountselected.length() > 0) {


//                            andharPutBetVisiblity(false);
//                            txt_catander.setText("");

                            if (betplace.equals("0")){

                                betvalue = "";

                            }else {


                            }

                            float valedou = 0;
                            if (betvalue.length() > 0) {
                                valedou = Float.parseFloat(betvalue);
                            } else {

                                valedou = 0;
                            }
                            float valedoutagamountselected = 0;
                            if (tagamountselected.length() > 0) {
                                valedoutagamountselected = Float.parseFloat(tagamountselected);
                            } else {

                                valedoutagamountselected = 0;
                            }

                            float betvalueint = valedou + valedoutagamountselected;
                            betvalue = betvalueint + "";
                            baharPutBetVisiblity(true);
//                            txt_catbahar.setText("" + betvalue);
                            //putbet("1");
                            betplace = "1";

                            putbetonView();

                        } else {

                            Functions.showToast(context, "Please Select Bet amount First");

                        }

                    } else {

                        Functions.showToast(context, "Game Already Started You can not Bet");

                    }
                }

            }
        });
        txtName = findViewById(R.id.txtName);
        txtGameRunning = findViewById(R.id.txtGameRunning);
        txtBallence = findViewById(R.id.txtBallence);
        helvatikaboldround = Typeface.createFromAsset(getAssets(), "fonts/helvetica-rounded-bold-5871d05ead8de.otf");
        txtGameFinish.setTypeface(helvatikaboldround);
      /*  txtGameRunning.setTypeface(helvatikaboldround);
        txtGameBets.setTypeface(helvatikaboldround);*/
        aaraycards  = new ArrayList<>();

        addChipsonView();
//        timerstatus = new Timer();
//        timerstatus.scheduleAtFixedRate(new TimerTask() {
//
//                                            @Override
//                                            public void run() {
//
//                                                // if (table_id.trim().length() > 0) {
//
//                                                if (isCardsDisribute){
//
//
//
//                                                }else {
//
//                                                    getStatus("1");
//
//                                                }
//
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


        findViewById(R.id.imgback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void putbetonView() {

        if (betplace.length() > 0){

            if (betvalue.length() > 0){
                putbet(betplace);

//                isConfirm = true;

            }

        }else {
            Functions.showToast(context, "You have not Bet yet. ");

        }

    }

    private void addCardinLayout(String cat ,int countnumber ,ViewGroup lnrLayout){

        View view = LayoutInflater.from(context).inflate(R.layout.ab_cards_layout,  null);
        ImageView imgcards = view.findViewById(R.id.cards);
        view.setTag(cat+"");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
                Functions.showToast(context , tag);
            }
        });
        String uri1 = "@drawable/" + cat.toLowerCase();  // where myresource " +
        int imageResource1 = getResources().getIdentifier(uri1, null,
                getPackageName());
        Glide.with(context).load(imageResource1).into(imgcards);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0,
                0,
                -35, 0);

        lnrLayout.addView(view,layoutParams);
        playSound(CARD_SOUND,false);


    }

    boolean animationon = false;
    RelativeLayout rl_AnimationView;

    int coins_count = 10;
    private void ChipsAnimations(View mfromView,View mtoView,ViewGroup rl_AnimationView){

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
            chips_width = 96;
        }

        int centreX = (int) (andharX +  andharWidth  / 2) - (chips_width  / 2);
        int centreY = (int) (andharY +  andharHeight / 2) - (chips_width  / 2);

        if(chips_width > 0)
        {
            destX  = destX + new Random().nextInt(andharWidth - chips_width);
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
        anim.setAnimationView(image_chips);
        image_chips.setAnimation(a);
        a.startNow();

        playSound(CHIPS_SOUND,false);

    }

    private void ChipsAnimations(View mfromView,View mtoView){

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

        if(stickerId > 0 && Functions.isActivityExist(context))
            LoadImage().load(stickerId).into(sticker);

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
                    removeChips();
                    rl_AnimationView.removeAllViews();
//                    if(!iswin)
//                        makeLosstoPlayer(SharePref.getU_id());
//                    else
//                        makeWinnertoPlayer(SharePref.getU_id());
                }

            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        playSound(CHIPS_SOUND,false);


    }

    private RequestManager LoadImage()
    {
        return  Glide.with(context);
    }


    private void removeChips(){
        rl_AnimationView.removeAllViews();
    }


    public void PlaySaund(int saund) {

        if(!SharePref.getInstance().isSoundEnable())
            return;

        if (!isInPauseState) {
//            final MediaPlayer mp = MediaPlayer.create(this,
//                    saund);
//            mp.start();
//            try {
//                if (mp.isPlaying()) {
//                    mp.stop();
//                    mp.release();
//                    mp = MediaPlayer.create(context, saund);
//                }
//                mp.start();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            stopPlaying();
            mp = MediaPlayer.create(this, saund);
            mp.start();

        }


    }

    private void stopPlaying() {
        try {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    int ander_bet;
    int bahar_bet;

    private void startGameCountDown() {
        if (counttimerforstartgame != null) {
            counttimerforstartgame.cancel();
        }

        Functions.LOGE("GameActivity","time_remaining : "+time_remaining);
        counttimerforstartgame = new CountDownTimer((time_remaining * timer_interval),timer_interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                isCountDownStart = true;
                count--;
            //    txtGameFinish.setVisibility(View.VISIBLE);
            //    txtGameFinish.setText("" + millisUntilFinished/timer_interval);

                playSound(COUNTDOWN_SOUND,false);

                addDummyChipsonAndharBahar();
                addDummyChipsonAndharBahar();
                addDummyChipsonAndharBahar();
                addDummyChipsonAndharBahar();
                addDummyChipsonAndharBahar();
            }

            @Override
            public void onFinish() {
                isCountDownStart = false;
                stopSound(COUNTDOWN_SOUND);
                canbet = false;
                if (betplace.length() > 0) {

                    if (betvalue.length() > 0) {
                        putbet(betplace);
                    }

                } else {
                    Functions.showToast(context, "You have not Bet yet. ");


                }
             //   txtGameFinish.setVisibility(View.INVISIBLE);
              //  getStatus("0");
                counttimerforstartgame.cancel();
            }


        };

        counttimerforstartgame.start();
    }

    DisplayMetrics metrics;
    int andharWidth = 0,andharHeight = 0;
    View lnrAndharBoard,lnrBaharBoard;
    float andharX,andharY;

    private void initDisplayMetrics(){
        lnrAndharBoard = findViewById(R.id.lnrAndharBoard);
        lnrBaharBoard = findViewById(R.id.lnrBaharBoard);
        metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        lnrAndharBoard.post(new Runnable() {
            @Override
            public void run() {
                andharWidth = lnrAndharBoard.getWidth();
                andharHeight = lnrAndharBoard.getHeight();

                andharX = lnrAndharBoard.getX();
                andharY = lnrAndharBoard.getY();
            }
        });
    }

    int chips_width  = 0;
    private ImageView creatDynamicChips() {
        ImageView chips = new ImageView(this);

        int chips_size = (int) getResources().getDimension(R.dimen.chips_size);

        chips.setImageDrawable(Functions.getDrawable(context,ChipsPicker.getInstance().getChip()));

        chips.setLayoutParams(new ViewGroup.LayoutParams(chips_size, chips_size));

        chips.post(new Runnable() {
            @Override
            public void run() {
                chips_width = chips.getWidth();
            }
        });

        return chips;
    }

    public void addDummyChipsonAndharBahar() {

        if(!isCountDownStart)
            return;

        // View AndharFromView = lnrOnlineUser;
        View AndharFromView = recycle_bots;
        View AndharToView = lnrAndharBoard;

        //  View BaharFromView = lnrOnlineUser;
        View BaharFromView = recycle_botsRight;
        View BaharToView = lnrBaharBoard;

        // View from left user bots to andhar board
        View AndharFromView1 = findViewById(R.id.left_user1);
        View AndharFromView2 = findViewById(R.id.left_user2);
        View AndharFromView3 = findViewById(R.id.left_user3);

        // View from right user bots to bahar board
        View BaharFromView1 = findViewById(R.id.right_user1);
        View BaharFromView2 = findViewById(R.id.right_user2);
        View BaharFromView3 = findViewById(R.id.right_user3);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // ChipsAnimations(AndharFromView,AndharToView,rl_AnimationView);

                // Chip Animation From Left Users
                ChipsAnimations(AndharFromView1,AndharToView,rl_AnimationView);
                ChipsAnimations(AndharFromView2,AndharToView,rl_AnimationView);
                ChipsAnimations(AndharFromView3,AndharToView,rl_AnimationView);

                //  ChipsAnimations(BaharFromView,BaharToView,rl_AnimationView);

                // Chip Animation From Right Users
                ChipsAnimations(BaharFromView1,BaharToView,rl_AnimationView);
                ChipsAnimations(BaharFromView2,BaharToView,rl_AnimationView);
                ChipsAnimations(BaharFromView3,BaharToView,rl_AnimationView);
            }
        },500);

    }

    ImageView  dummyUserleft,dummyUserright;
    public View leftaddView() {
        dummyUserleft = new ImageView(this);
//        dummyUserleft.setImageResource(R.drawable.ic_user_male);
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

    private void putbet(final String type) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PUTBET,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Functions.LOGE("putbet",Const.PUTBET+"\n"+response);
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");


                            if (code.equalsIgnoreCase("200")) {
                                bet_id = jsonObject.getString("bet_id");
                                wallet = jsonObject.getString("wallet");
                                txtBallence.setText(wallet);
//                                Functions.showToast(context, ""+message);

                                if (type.equals("0")){
                                    andharPutBetVisiblity(true);
//                                    txt_catander.setText(""+betvalue);

                                }else {
                                    baharPutBetVisiblity(true);
//                                    txt_catbahar.setText(""+betvalue);
                                }

                                betvalue = "";


                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(context, message);


                                }


                            }

                        //    getStatus("0");

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
                params.put("bet", type);
                params.put("amount", betvalue);
                //  params.put("token", "9959cbfce148e91db099d4936b1a9b66");
                Functions.LOGE("putbet",Const.PUTBET+"\n"+params);

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

    private void cancelbet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CENCEL_BET,
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
                                removeBetChips();


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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.REPEAT_BET,
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
                                betvalue = amount;
                                betplace = bet;
                                if (bet.equals("0")){

                                    andharPutBetVisiblity(true);

//                                    txt_catander.setText(""+amount);
                                }else {
//                                    txt_catbahar.setText(""+amount);

                                    baharPutBetVisiblity(true);
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
    @Override
    protected void onDestroy() {
        DestroyGames();
        releaseSoundpoll();
        if(runnable != null)
            handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void DestroyGames(){

        if (counttimerforstartgame != null){
            counttimerforstartgame.cancel();
        }

        if (counttimerforcards != null){
            counttimerforcards.cancel();
        }

        if (timerstatus !=null ){
            timerstatus.cancel();
        }

        stopPlaying();
        releaseSoundpoll();
        mSocket.disconnect();
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
        if (mp !=null ){
            mp.stop();
            mp.release();
        }
        pauseSoundPool();
    }

    void pauseSoundPool(){
        if(mSoundPool != null)
        {
            mSoundPool.pause(COUNTDOWN_SOUND);
            mSoundPool.pause(CHIPS_SOUND);
            mSoundPool.pause(CARD_SOUND);
        }
    }

    String user_id_player1 = "";
    RelativeLayout rltwinnersymble1;
    public void makeWinnertoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);
     //   restartAndharBaharView();

        if (chaal_user_id.equals(user_id_player1)) {
            PlaySaund(R.raw.tpb_battle_won);
            highlightWinView();
        }

    }

    public void makeLosstoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);
        rtllosesymble1.setVisibility(View.GONE);

        if (chaal_user_id.equals(user_id_player1)) {
            PlaySaund(R.raw.tpb_battle_won);
//            rtllosesymble1.setVisibility(View.VISIBLE);
        }

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
        if(mSoundMap != null && mSoundMap.size() > 0 && mSoundPool != null)
            mSoundPool.stop(mSoundMap.get(sound));
    }

    public void releaseSoundpoll(){
        stopSound(COUNTDOWN_SOUND);
        stopSound(CARD_SOUND);
        stopSound(CHIPS_SOUND);


        if(mSoundPool != null)
        {
            mSoundPool.unload(COUNTDOWN_SOUND);
            mSoundPool.unload(CARD_SOUND);
            mSoundPool.unload(CHIPS_SOUND);
            mSoundMap.clear();
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    public void openBuyChipsActivity(View view) {
        openBuyChipsListActivity();
    }

    private void openBuyChipsListActivity() {
        startActivity(new Intent(context, BuyChipsList.class));
    }

    public void openGameRules(View view) {
        DialogRulesAndharBahar.getInstance(context).show();
    }

    private void highlightWinView(){
        int winnerview = Integer.parseInt(winning);
        View view = winnerview == 0 ? rltandarbet : rltbaharbet;
        view.clearAnimation();
        view.setBackground(Functions.getDrawable(context,R.drawable.background_border_highlight));
        view.startAnimation(blinksAnimation);
    }

    private void restartAndharBaharView(){
        rltandarbet.clearAnimation();
        rltbaharbet.clearAnimation();
        rltandarbet.setBackground(Functions.getDrawable(context,R.drawable.background_border));
        rltbaharbet.setBackground(Functions.getDrawable(context,R.drawable.background_border));
    }

    private void AnimationUtils(boolean iswin) {

        coins_count =10;

        boolean isbetonAndhar = betplace.equals(""+ANDHAR) ? true : false;
        boolean isbetonBahar = betplace.equals(""+BAHAR) ? true : false;

//        View fromView = null;
        View toView = null;

//        if(isbetonAndhar)
//        {
//            fromView = rltandarbet;
//        }
//        else if(isbetonBahar)
//        {
//            fromView = rltbaharbet;
//        }

        if(iswin)
        {
            toView = ChipstoUser;
        }
        else {
            toView = lnrOnlineUser;
        }

//        View finalFromView = fromView;
        View finalToView = toView;
        new CountDownTimer(2000,200) {
            @Override
            public void onTick(long millisUntilFinished) {
                coins_count--;
                ChipsAnimations(rltandarbet, finalToView);
                ChipsAnimations(rltbaharbet, finalToView);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    ImageView ivBetStatus;
    private void stopBetAnim(){
        ivBetStatus = findViewById(R.id.ivBetStatus);
        ivBetStatus.setVisibility(View.VISIBLE);
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
                        counttimerforcards.start();
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
                ivBetStatus.setVisibility(View.GONE);
            }
        }, 1250);
    }

    Handler handler = new Handler();
    Runnable runnable;

    private TextView getTextView(int id){

        return ((TextView) findViewById(id));
    }

    private void setDataonUser() {

        txtName.setText(""+ SharePref.getInstance().getString(SharePref.u_name));
        txtBallence.setText(Variables.CURRENCY_SYMBOL+""+ SharePref.getInstance().getString(SharePref.wallet));

        Glide.with(getApplicationContext())
                .load(Const.IMGAE_PATH + SharePref.getInstance().getString(SharePref.u_pic))
                .placeholder(R.drawable.avatar)
                .into(imgpl1circle);

    }

    public void UserProfile() {

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