package com.metagards.metagames._jhandhiMunda;

import static com.metagards.metagames.Utils.Functions.ANIMATION_SPEED;
import static com.metagards.metagames._AdharBahar.Fragments.GameFragment.MY_PREFS_NAME;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.metagards.metagames.BaseActivity;
import com.metagards.metagames.ChipsPicker;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Animations;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Sound;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._ColorPrediction.BotsAdapter;
import com.metagards.metagames._ColorPrediction.BotsAdapterRight;
import com.metagards.metagames._ColorPrediction.Bots_list;
import com.metagards.metagames._RedBlack.menu.DialogRedBlackHistory;
import com.metagards.metagames._RedBlack.menu.DialogRedBlacklastWinHistory;
import com.metagards.metagames._jhandhiMunda.menu.DialogRulesJhandiMunda;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class JhandhiMunda_A extends BaseActivity implements View.OnClickListener{

    RecyclerView recycle_bots, recycle_botsRight;
    ArrayList<Bots_list> bots_lists=new ArrayList<>();
    ArrayList<Bots_list> bots_listsRight=new ArrayList<>();
    BotsAdapter botsAdapter;
    BotsAdapterRight botsAdapterRight;

    LinearLayout chipanimation;
    Activity context = this;

    TextView txtName,txtBallence,txt_gameId,txtGameRunning,txtGameBets,tvWine,tvLose;
    Button btGameAmount;
    ImageView ivWine,ivLose;
    ImageView imgpl1circle;

    View ChipstoUser;


    private final String HEART = "HEART";
    private final String SPADE = "SPADE";
    private final String DIAMOND = "DIAMOND";
    private final String CLUB = "CLUB";
    private final String FACE = "FACE";
    private final String FLAG = "FLAG";

    private final int HEART_VALUE = 1;
    private final int SPADE_VALUE = 2;
    private final int DIAMOND_VALUE = 3;
    private final int CLUB_VALUE = 4;
    private final int FACE_VALUE = 5;
    private final int FLAG_VALUE = 6;

    private int HEART_WINCOUNT = 0;
    private int SPADE_WINCOUNT = 0;
    private int DIAMOND_WINCOUNT = 0;
    private int CLUB_WINCOUNT = 0;
    private int FACE_WINCOUNT = 0;
    private int FLAG_WINCOUNT = 0;

    private int minGameAmt = 50;
    private int maxGameAmt = 500;
    private int GameAmount = 50;
    private int StepGameAmount = 50;
    private int _30second = 30000;
    private int GameTimer = 30000;
    private int timer_interval = 1000;

    private String game_id = "";
    CountDownTimer pleasewaintCountDown;
    LinearLayout lnrfollow;


    int[] jackpotRuleStrip={
            R.drawable.ic_jackpot_strip_blue,
            R.drawable.ic_jackpot_strip_green,
            R.drawable.ic_jackpot_strip_orange,
            R.drawable.ic_jackpot_strip_purple,
            R.drawable.ic_jackpot_strip_shade,
            R.drawable.ic_jackpot_strip_green
    };

    private final String DEFAULT_CARD = "backside_card";

    View lnrOnlineUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jhandimunda);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Initialization();

        setDataonUser();

        recycle_bots = findViewById(R.id.recycle_bots);
        recycle_bots.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        recycle_bots.setHasFixedSize(true);
        recycle_botsRight = findViewById(R.id.recycle_bots_right);
        recycle_botsRight.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        recycle_botsRight.setHasFixedSize(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                getStatus_bots();
            }
        }, 500);

    }
    private void getStatus_bots() {
        HashMap params = new HashMap<String, String>();
        SharedPreferences prefs = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));
        params.put("room_id", "1");
        ApiRequest.Call_Api(context, Const.HeadTailStatus, params, new Callback() {
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


    private void Initialization() {
        lnrOnlineUser = findViewById(R.id.lnrOnlineUser);
        sound = new Sound();
        initSoundPool();
        rl_AnimationView = ((RelativeLayout)findViewById(R.id.sticker_animation_layout));
        ChipstoUser = findViewById(R.id.ChipstoUser);
        btGameAmount = findViewById(R.id.btGameAmount);
        lnrfollow = findViewById(R.id.lnrfollow);

        initRulesSelectAdapter();

        txtName = findViewById(R.id.txtName);
        imgpl1circle = findViewById(R.id.imgpl1circle);
        chipanimation = findViewById(R.id.chipanimation);
        txtBallence = findViewById(R.id.txtBallence);
        txt_gameId = findViewById(R.id.txt_gameId);
        txtGameRunning = findViewById(R.id.txtGameRunning);
        txtGameBets = findViewById(R.id.txtGameBets);

        ivWine = findViewById(R.id.ivWine);
        ivLose = findViewById(R.id.ivlose);
        tvWine = findViewById(R.id.tvWine);
        tvLose = findViewById(R.id.tvlose);

        rltwinnersymble1=findViewById(R.id.rltwinnersymble1);
        rtllosesymble1=findViewById(R.id.rtllosesymble1);


        findViewById(R.id.imgback).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1plus).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1minus).setOnClickListener(this::onClick);
        findViewById(R.id.iv_add).setOnClickListener(this::onClick);

        addChipsonView();


        pleaswaitTimer();
        RestartGame(true);

        setDataonUser();

        startService();

        initiAnimation();

    }

    private void removeRedBlackCards() {

    }

    public static final int SOUND_1 = 1;
    public static final int SOUND_2 = 2;

    SoundPool mSoundPool;
    HashMap<Integer, Integer> mSoundMap;
    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        mSoundMap = new HashMap<Integer, Integer>();

        if(mSoundPool != null){
            mSoundMap.put(SOUND_1, mSoundPool.load(this, R.raw.buttontouchsound, 1));
            mSoundMap.put(SOUND_2, mSoundPool.load(this, R.raw.teenpattichipstotable, 1));
        }
    }
    /*
     *Call this function from code with the sound you want e.g. playSound(SOUND_1);
     */
    public void playSound(int sound) {

        if(!SharePref.getInstance().isSoundEnable())
            return;

        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if(mSoundPool != null){
            mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, 0, 1.0f);
        }
    }


    JhandhiMundaAdapter jackpotRulesAdapter;
    int TOTAL_CELLS_PER_ROW = 3;
    RecyclerView rec_rules;
    private void initRulesSelectAdapter() {
        rec_rules= findViewById(R.id.rec_rules);
        final GridLayoutManager mng_layout = new GridLayoutManager(this, TOTAL_CELLS_PER_ROW/*In your case 4*/);

        mng_layout.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                Functions.LOGD("RedBlackPot","SpanSizeLookup : "+position);
//                int mod = position % 6;
//
//                if(position == 0 || position == 1)
//                    return 3;
//                else if(position == 4)
//                    return 2;
//                else if(position < 7)
//                    return 1;
//                else if(mod == 0 || mod == 1)
//                    return 2;
//                else
                return 1;
            }
        });

        rec_rules.setLayoutManager(mng_layout);
        jackpotRulesAdapter = new JhandhiMundaAdapter(context);
        jackpotRulesAdapter.onItemSelectListener(new OnItemClickListener() {
            @Override
            public void Response(View v, int position, Object object) {
                JhandhiMundaRulesModel jhandhiMundaRulesModel = (JhandhiMundaRulesModel) object;
                betplace = ""+ jhandhiMundaRulesModel.rule_value;
                if(Functions.isStringValid(betplace) && betValidation())
                {
                    putbet(""+betplace, jhandhiMundaRulesModel);

                }


            }
        });
        rec_rules.setAdapter(jackpotRulesAdapter);
        setSetRulesValue();
    }



    List<JhandhiMundaRulesModel> rulesModelList = new ArrayList<>();
    private void setSetRulesValue(){
        rulesModelList.clear();
        rulesModelList.add(new JhandhiMundaRulesModel(HEART,HEART_VALUE,0,0,"", JhandhiMundaRulesModel.TYPE_HIGH));
        rulesModelList.add(new JhandhiMundaRulesModel(SPADE,SPADE_VALUE,0,0,"", JhandhiMundaRulesModel.TYPE_HIGH));
        rulesModelList.add(new JhandhiMundaRulesModel(DIAMOND,DIAMOND_VALUE,0,0,"", JhandhiMundaRulesModel.TYPE_HIGH));
        rulesModelList.add(new JhandhiMundaRulesModel(CLUB,CLUB_VALUE,0,0,"", JhandhiMundaRulesModel.TYPE_HIGH));
        rulesModelList.add(new JhandhiMundaRulesModel(FACE,FACE_VALUE,0,0,"", JhandhiMundaRulesModel.TYPE_LOW));
        rulesModelList.add(new JhandhiMundaRulesModel(FLAG,FLAG_VALUE,0,0,"", JhandhiMundaRulesModel.TYPE_LOW));
        jackpotRulesAdapter.setArraylist(rulesModelList);
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



    private void initiAnimation() {
        blinksAnimation = AnimationUtils.loadAnimation(context,R.anim.blink);
        blinksAnimation.setDuration(1000);
        blinksAnimation.setRepeatCount(Animation.INFINITE);
        blinksAnimation.setStartOffset(700);
    }

    boolean isCardsDisribute = false;
    int timertime = 4000;
    Timer timerstatus;
    private void startService() {

        timerstatus = new Timer();
        timerstatus.scheduleAtFixedRate(new TimerTask() {

                                            @Override
                                            public void run() {

                                                // if (table_id.trim().length() > 0) {

                                                if (isCardsDisribute) {


                                                } else {

                                                    CALL_API_getGameStatus();

                                                }


                                                // }

                                            }

                                        },
//Set how long before to start calling the TimerTask (in milliseconds)
                200,
//Set the amount of time between each execution (in milliseconds)
                timertime);



    }

    CountDownTimer gameStartTime;
    boolean isGameTimerStarted = false;
    private void CardsDistruButeTimer(){

        if(isGameTimerStarted && getTextView(R.id.tvStartTimer).getVisibility() == View.VISIBLE)
            return;

        gameStartTime = new CountDownTimer((time_remaining * timer_interval),timer_interval) {
            @Override
            public void onTick(long millisUntilFinished) {

                // View from left user bots to andhar board
                View AndharFromView1 = findViewById(R.id.left_user1);
                View AndharFromView2 = findViewById(R.id.left_user2);
                View AndharFromView3 = findViewById(R.id.left_user3);

                // View from right user bots to bahar board
                View BaharFromView1 = findViewById(R.id.right_user1);
                View BaharFromView2 = findViewById(R.id.right_user2);
                View BaharFromView3 = findViewById(R.id.right_user3);

                isGameTimerStarted = true;
                float count = millisUntilFinished/timer_interval;

                getTextView(R.id.tvStartTimer).setVisibility(View.VISIBLE);
                getTextView(R.id.tvStartTimer).setText(""+count+"s");

//                PlaySaund(R.raw.teenpattitick);

                for (int i = 0; i < rec_rules.getChildCount(); i++) {
                    JhandhiMundaAdapter.holder holder = (JhandhiMundaAdapter.holder) rec_rules.getChildViewHolder(rec_rules.getChildAt(i));
                    View view = holder.itemView.findViewById(R.id.chipanimation);
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            if (view != null)
                            {
//                                DummyChipsAnimations(lnrOnlineUser, view, rl_AnimationView);
//                                DummyChipsAnimations(lnrOnlineUser, view, rl_AnimationView);
//                                DummyChipsAnimations(lnrOnlineUser, view, rl_AnimationView);
//                                DummyChipsAnimations(lnrOnlineUser, view, rl_AnimationView);

                                // Chip Animation From Left Users
                                DummyChipsAnimations(AndharFromView1,view,rl_AnimationView);
                                DummyChipsAnimations(AndharFromView2,view,rl_AnimationView);
                                DummyChipsAnimations(AndharFromView3,view,rl_AnimationView);

                                // Chip Animation From Right Users
                                DummyChipsAnimations(BaharFromView1,view,rl_AnimationView);
                                DummyChipsAnimations(BaharFromView2,view,rl_AnimationView);
                                DummyChipsAnimations(BaharFromView3,view,rl_AnimationView);


                            }
                        }
                    });

                }
            }

            @Override
            public void onFinish() {
                stopPlaying();
                isGameTimerStarted = false;

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
        super.onDestroy();
    }

    private void DestroyGames(){

        cancelStartGameTimmer();

        if (timerstatus !=null ){
            timerstatus.cancel();
        }

        stopPlaying();
    }

    public String status = "";
    public String winning;
    public int winning_rule ;
    private String added_date;
    private String user_id,name,wallet;
    private String profile_pic;
    ArrayList<String> aaraycards  = new ArrayList<>();
    boolean isGameBegning = false;
    boolean isConfirm = false;
    String bet_id = "";
    String betplace = "";
    boolean canbet = false;
    String betvalue = "";
    CountDownTimer counttimerforstartgame;
    CountDownTimer counttimerforcards;
    int time_remaining;
    boolean isCardDistribute = false;
    boolean isBetputes = false;
    int cardDistributeCount = 0;
    private void CALL_API_getGameStatus() {

        HashMap params = new HashMap();

        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));

        params.put("room_id", "1");


        params.put("total_bet_flag",""+ flag_amount);
        params.put("total_bet_face", ""+ face_amount);
        params.put("total_bet_club", ""+club_amount);
        params.put("total_bet_diamond", ""+diamond_amount);
        params.put("total_bet_spade", ""+spade_amount);
        params.put("total_bet_heart", ""+heart_amount);

        ApiRequest.Call_Api(context, Const.JhandiMundaStatus, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if (resp != null)
                {

                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equalsIgnoreCase("200")) {

                            JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");
                            JSONArray online_users = jsonObject.getJSONArray("online_users");
                            int online = jsonObject.getInt("online");
                            ((TextView) findViewById(R.id.tvonlineuser))
                                    .setText(""+online);
                            JSONArray last_winning = jsonObject.getJSONArray("last_winning");


                            for (int i = 0; i < arraygame_dataa.length() ; i++) {
                                JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                game_id  = welcome_bonusObject.getString("id");

                                status  = welcome_bonusObject.getString("status");
                                winning  = welcome_bonusObject.getString("winning");
                                winning_rule  = welcome_bonusObject.optInt("winning_rule");
                                String end_datetime  = welcome_bonusObject.getString("end_datetime");
                                added_date  = welcome_bonusObject.getString("added_date");
                                time_remaining  = welcome_bonusObject.optInt("time_remaining");

                            }

                            JSONArray arrayprofile = jsonObject.getJSONArray("profile");

                            for (int i = 0; i < arrayprofile.length() ; i++) {
                                JSONObject profileObject = arrayprofile.getJSONObject(i);

                                //  GameStatus model = new GameStatus();
                                user_id  = profileObject.getString("id");
                                user_id_player1 = user_id;
                                name  = profileObject.getString("name");
                                wallet  = profileObject.getString("wallet");

                                profile_pic  = profileObject.getString("profile_pic");

                                Picasso.get()
                                        .load(Const.IMGAE_PATH + profile_pic)
                                        .into(imgpl1circle);

                                //   txtBallence.setText(wallet);
                                txtName.setText(name);

                            }


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
//                                    parseGameUsersAmount(jsonObject);
                                    startBetAnim();
                                    CardsDistruButeTimer();
                                }
                                else {
                                    cancelStartGameTimmer();
                                }

                            }
                            else if (status.equals("0") && isGameBegning){
                                parseGameUsersAmount(jsonObject);

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

                                    counttimerforcards = new CountDownTimer(aaraycards.size()*400, 400) {

                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                            makeWinnertoPlayer("");
                                            makeLosstoPlayer("");

                                            if(aaraycards != null && aaraycards.size() >= 2)
                                            {
                                                int position = Integer.parseInt(aaraycards.get(cardDistributeCount)) - 1 ;

                                                JhandhiMundaAdapter.holder holder = (JhandhiMundaAdapter.holder) rec_rules.getChildViewHolder(rec_rules.getChildAt(position));
                                                ViewGroup viewRule = holder.itemView.findViewById(R.id.rltContainer);
                                                if (viewRule != null)
                                                {
                                                    addDiceonView(viewRule, rl_AnimationView,aaraycards.get(cardDistributeCount));
                                                }

                                                cardDistributeCount++;
                                                isCardDistribute = true;
                                            }
                                        }
                                        @Override
                                        public void onFinish() {

                                            VisiblePleasewaitforNextRound(true);
                                            cardDistributeCount = 0;
                                            isCardsDisribute = false;

                                            highlistWinRules(Integer.parseInt(winning),winning_rule);
                                            txtBallence.setText(wallet);

                                        }


                                    };
                                    stopBetAnim();


                                }


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

                            parseLastBetAmount(jsonObject);

                            if(!isConfirm)
                                VisiblePleaseBetsAmount(true);

                            makeWinnertoPlayer("");
                            makeLosstoPlayer("");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }


    private void highlistWinRules(int winning_rule_value,int ruleWin) {
        int winingMax = 1;
        try {
            for (JhandhiMundaRulesModel model: rulesModelList) {
                boolean isWin = false;
                switch (model.rule_value)
                {
                    case HEART_VALUE:
                        if(HEART_WINCOUNT > winingMax)
                            isWin =true;
                        break;
                    case SPADE_VALUE:
                        if(SPADE_WINCOUNT > winingMax)
                            isWin =true;
                        break;
                    case DIAMOND_VALUE:
                        if(DIAMOND_WINCOUNT > winingMax)
                            isWin =true;
                        break;
                    case CLUB_VALUE:
                        if(CLUB_WINCOUNT > winingMax)
                            isWin =true;

                        break;
                    case FACE_VALUE:
                        if(FACE_WINCOUNT > winingMax)
                            isWin =true;
                        break;
                    case FLAG_VALUE:
                        if(FLAG_WINCOUNT > winingMax)
                            isWin =true;
                        break;
                }

                model.setWine(isWin);
            }

            if(Functions.isStringValid(betplace) && (winning_rule_value == Integer.parseInt(betplace)))
            {
                PlaySaund(R.raw.tpb_battle_won);
            }
            else if(Functions.isStringValid(betplace)) {
            }

            jackpotRulesAdapter.notifyDataSetChanged();

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private String rulesName(int ruleVal){

        for (JhandhiMundaRulesModel jhandhiMundaRulesModel : rulesModelList) {
            int rule_value = jhandhiMundaRulesModel.rule_value;

            if(ruleVal == rule_value)
            {
                return jhandhiMundaRulesModel.getRule_type();
            }

        }

        return ""+ruleVal;
    }

    private void setHighlightedBackgroundforCard(int winning_rule_value) {
//        ivCardbg.setBackground(Functions.getDrawable(context,R.drawable.ic_jackpot_cardsbg_selected));
    }

    private void parseLastBetAmount(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("last_bet");
            if(jsonArray.length() > 0)
            {
                JSONObject lastbetObject = jsonArray.getJSONObject(0);
                int id = lastbetObject.getInt("id");
                int bet = lastbetObject.getInt("bet");
                int amount = lastbetObject.getInt("amount");

                animatedUsersPutAmount(id,bet,amount);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void removeTotalAddedAmount(){
        heart_amount = 0;
        flag_amount = 0;
        face_amount = 0;
        club_amount = 0;
        diamond_amount = 0;
        spade_amount = 0;

        for (JhandhiMundaRulesModel model: rulesModelList) {
            model.added_amount = 0;
        }

        jackpotRulesAdapter.notifyDataSetChanged();
    }

    private void animatedUsersPutAmount(int id,int bet, int amount) {
        for (JhandhiMundaRulesModel model: rulesModelList) {
            if(model.rule_value == bet)
            {
                model.setLast_added_id(id);
                model.setLast_added_rule_value(bet);
                model.setLast_added_amount(amount);
                model.setAnimatedAddedAmount(true);
                break;
            }
        }

        jackpotRulesAdapter.notifyDataSetChanged();

    }

    int heart_amount;
    int spade_amount;
    int diamond_amount;
    int club_amount;
    int face_amount;
    int flag_amount;

    private void parseGameUsersAmount(JSONObject jsonObject) throws JSONException {
        int total_jackpot_amount = 0;

        heart_amount = 0;
        flag_amount = 0;
        face_amount = 0;
        club_amount = 0;
        diamond_amount = 0;
        spade_amount = 0;

        for (JhandhiMundaRulesModel model: rulesModelList) {

            if(model.rule_type.equalsIgnoreCase(DIAMOND))
            {
                diamond_amount = jsonObject.optInt("diamond_amount");
                total_jackpot_amount = total_jackpot_amount + diamond_amount;
                model.added_amount = diamond_amount;


            }
            else if(model.rule_type.equalsIgnoreCase(CLUB))
            {
                club_amount = jsonObject.optInt("club_amount");
                total_jackpot_amount = total_jackpot_amount + club_amount;
                model.added_amount = club_amount;

            }else if(model.rule_type.equalsIgnoreCase(FACE))
            {
                face_amount = jsonObject.optInt("face_amount");
                total_jackpot_amount = total_jackpot_amount + face_amount;
                model.added_amount = face_amount;

            }else if(model.rule_type.equalsIgnoreCase(FLAG))
            {

                flag_amount = jsonObject.optInt("flag_amount");
                total_jackpot_amount = total_jackpot_amount + flag_amount;
                model.added_amount = flag_amount;

            }else if(model.rule_type.equalsIgnoreCase(HEART))
            {

                heart_amount = jsonObject.optInt("heart_amount");
                total_jackpot_amount = total_jackpot_amount + heart_amount;
                model.added_amount = heart_amount;
            }
            else if(model.rule_type.equalsIgnoreCase(SPADE))
            {

                spade_amount = jsonObject.optInt("spade_amount");
                total_jackpot_amount = total_jackpot_amount + spade_amount;
                model.added_amount = spade_amount;

            }


        }

        jackpotRulesAdapter.notifyDataSetChanged();
    }

    int coins_count = 10;

    private void VisiblePleaseBetsAmount(boolean visible){

//        findViewById(R.id.rltOngoinGame).setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    boolean isPleasewaitShowed = false;
    private void VisiblePleasewaitforNextRound(boolean visible){

        if(isPleasewaitShowed && visible)
            return;

        if(visible)
            isPleasewaitShowed = true;

        if(blinksAnimation != null)
        {
            isBlinkStart = false;
            txtGameRunning.clearAnimation();
            blinksAnimation.cancel();
        }

        findViewById(R.id.rltOngoinGame).setVisibility(visible ? View.VISIBLE : View.GONE);
//        txtGameRunning.setVisibility(visible ? View.VISIBLE : View.GONE);

        if(visible)
        {
            if(!Functions.checkisStringValid(((TextView) findViewById(R.id.txtcountdown)).getText().toString().trim()))
                pleasewaintCountDown.start();

//            BlinkAnimation(txtGameRunning);
        }
        else {
            pleasewaintCountDown.cancel();
            pleasewaintCountDown.onFinish();
        }


    }

    private void pleaswaitTimer(){
        pleasewaintCountDown = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long second = millisUntilFinished/1000;

//                ((TextView) findViewById(R.id.txtcountdown)).setText(second+"s");

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
    }

    private void TranslateLayout(ImageView imageView, int path){

        if(path > 0)
            cardflipSound();

        int distance = 8000;

        float scale = context.getResources().getDisplayMetrics().density;
        imageView.setCameraDistance(distance * scale);


        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context,
                R.animator.out_animation);
        set.setTarget(imageView);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

//                Glide.with(context)
//                        .load(path > 0 ? path : R.drawable.card_bg)
//                        .placeholder(R.drawable.card_bg)
//                        .into(imageView);

                imageView.setImageDrawable(path > 0 ? getDrawable(path) : getDrawable(R.drawable.card_bg));

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                Glide.with(context)
//                        .load(path > 0 ? path : R.drawable.card_bg)
//                        .placeholder(R.drawable.card_bg)
//                        .into(imageView);

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


    private void putbet(final String type, JhandhiMundaRulesModel jhandhiMundaRulesModel) {


        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("game_id", game_id);
        params.put("bet", ""+type);
        params.put("amount", ""+GameAmount);

        ApiRequest.Call_Api(context, Const.JhandiMundaPUTBET, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {

                    try {


                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");


                        if (code.equalsIgnoreCase("200")) {
                            playChipsSound();
                            bet_id = jsonObject.getString("bet_id");
                            wallet = jsonObject.getString("wallet");
                            txtBallence.setText(wallet);
//                            Functions.showToast(context, "Bet has been added successfully!");

                            betvalue = "";
//                            isConfirm = true;
                            isBetputes = true;

                            VisiblePleaseBetsAmount(false);

                            jhandhiMundaRulesModel.select_amount += GameAmount;
                            jackpotRulesAdapter.notifyDataSetChanged();

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.JhandiMundaCENCEL_BET,
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.JhandiMundaREPEAT_BET,
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
        txtBallence.setText(SharePref.getInstance().getString(SharePref.wallet));


        Glide.with(context)
                .load(Const.IMGAE_PATH + SharePref.getInstance().getString(SharePref.u_pic))
                .placeholder(R.drawable.avatar)
                .into(imgpl1circle);


    }

    String user_id_player1 = "";
    RelativeLayout rltwinnersymble1;
    View rtllosesymble1;
    public void makeWinnertoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);

        if (chaal_user_id.equals(user_id_player1)) {
            PlaySaund(R.raw.tpb_battle_won);
            rltwinnersymble1.setVisibility(View.VISIBLE);

        }

    }

    public void makeLosstoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);
        rtllosesymble1.setVisibility(View.GONE);

        if (chaal_user_id.equals(user_id_player1)) {
            PlaySaund(R.raw.game_loos_track);
            rtllosesymble1.setVisibility(View.VISIBLE);

        }

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

    Sound sound;
    public void cardflipSound(){

        if (!isInPauseState) {
            mp = MediaPlayer.create(this, R.raw.teenpatticardflip_android);
            mp.start();
        }
    }

    public void playChipsSound(){
        if (!isInPauseState) {
            playSound(SOUND_2);
        }
    }

    public void playButtonTouchSound() {
        if (!isInPauseState) {
            playSound(SOUND_1);
        }
    }

    public void playCountDownSound(){

//        if(!isInPauseState && !sound.isSoundPlaying())
//        {
//            sound.PlaySong(R.raw.count_down_timmer,true,context);
//        }

    }



    private void stopPlaying() {
        if (mp != null) {
            sound.StopSong();
            mp.stop();
            mp.release();
            mp = null;
        }
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

            case R.id.iv_add:{
                openBuyChipsListActivity();
            }
        }
    }

    private void openBuyChipsListActivity() {
        startActivity(new Intent(context, BuyChipsList.class));
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

    private void RestartGame(boolean isFromonCreate){

        HEART_WINCOUNT = 0;
        SPADE_WINCOUNT = 0;
        DIAMOND_WINCOUNT = 0;
        CLUB_WINCOUNT = 0;
        FACE_WINCOUNT = 0;
        FLAG_WINCOUNT = 0;

        removeRedBlackCards();
        removeTotalAddedAmount();


        rl_AnimationView.removeAllViews();
        RemoveChips();
        setSetRulesValue();

        VisiblePleasewaitforNextRound(false);

        cancelStartGameTimmer();

        isCardDistribute = false;

        txtBallence.setText(wallet);

        removeBet();
        aaraycards.clear();
        if(!isFromonCreate)
            isGameBegning = true;

    }

    private void removeBet(){
        canbet = true;
        isConfirm = false;
        bet_id = "";
        betplace="";
        betvalue = "";
    }

    private void RemoveChips(){
        addChipsonView();
    }

    @Override
    public void onBackPressed() {

        Functions.Dialog_CancelAppointment(context, "Confirmation", "Leave ?", new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp.equals("yes"))
                {
                    stopPlaying();
                    finish();
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

        PlaySaund(R.raw.teenpattichipstotable);


    }

    private void startBetAnim(){
        txtGameBets.setVisibility(View.VISIBLE);
        txtGameBets.setBackgroundResource(R.drawable.iv_bet_begin);
        txtGameBets.bringToFront();
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(1200);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        txtGameBets.startAnimation(fade_in);
        fade_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtGameBets.clearAnimation();
                txtGameBets.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                txtGameBets.setVisibility(View.GONE);
            }
        }, 1500);
    }
    private void stopBetAnim(){
        txtGameBets.setVisibility(View.VISIBLE);
        txtGameBets.setBackgroundResource(R.drawable.iv_bet_stops);

//        Animation sgAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_grow);
//        txtGameBets.startAnimation(sgAnimation);
//        txtGameBets.startAnimation(sgAnimation);

        txtGameBets.bringToFront();
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(200);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        txtGameBets.startAnimation(fade_in);
        fade_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtGameBets.clearAnimation();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cardDistributeCount = 0;
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
//                Toast.makeText(context, "nice", Toast.LENGTH_SHORT).show();
                txtGameBets.setVisibility(View.GONE);
            }
        }, 1250);
    }
    private void CardAnimationAnimations(View mfromView,View mtoView,boolean isTiger){
//        Toast.makeText(context, "stops", Toast.LENGTH_SHORT).show();

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

        int cards_size = (int) getResources().getDimension(R.dimen.jackpot_card_size);

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


//                addJackpotCard1(aaraycards.get(0),0);
//                addJackpotCard2(aaraycards.get(1),1);
//                addJakpotCard3(aaraycards.get(2),2);

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

//        if(Functions.isStringValid(betplace) && betValidation())
//            putbet(betplace);
    }


    public void cancelBooking(View view) {

        cancelbet();
        removeBet();
        RemoveChips();
    }

    public void openGameRules(View view) {
        DialogRulesJhandiMunda.getInstance(context).show();
    }

    public void openJackpotHistory(View view) {
        DialogRedBlackHistory.getInstance(context).show();
    }

    public void openJackpotLasrWinHistory(View view) {
        DialogRedBlacklastWinHistory.getInstance(context).setRoomid(game_id).show();
    }


    private void addCardonParentView(ViewGroup parentview,String image_card){
        View view  = LayoutInflater.from(context).inflate(R.layout.item_su_lastbet,null);
        TextView tvItems = view.findViewById(R.id.tvItems);
        tvItems.setText("");

        parentview.addView(view);
    }

    private void DummyChipsAnimations(View mfromView, View mtoView, ViewGroup rl_AnimationView) {

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

        rl_AnimationView.setVisibility(View.VISIBLE);

        ImageView image_chips = creatDynamicChips();

        rl_AnimationView.addView(image_chips);

        if (chips_width <= 0) {
            chips_width = 96;
        }

        int boardWidth = myViewRect.width(), boardHeight = myViewRect.height();


        int centreX = (int) (boardWidth / 2) - (chips_width / 2);
        int centreY = (int) (boardHeight / 2) - (chips_width / 2);

        if (chips_width > 0) {
            try {
                float ran_width = new Random().nextInt(boardWidth - chips_width);
                float ran_height = new Random().nextInt(boardHeight - chips_width);
                Log.e("TAG_ran_height", "DummyChipsAnimations: "+ran_width +"--"+ ran_height);
                if (ran_width > 0 && ran_height> 0) {
                    destX = destX + new Random().nextInt(boardWidth - chips_width);
                    destY = destY + new Random().nextInt(boardHeight - chips_width);
                }
            }catch (Exception e){
                Log.e("TAG_destY", "DummyChipsAnimations: "+e.toString() );
            }


        } else
        {
            destX += centreX;
            destY += centreY;
        }



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

        playSound(SOUND_2);


    }

    int chips_width = 0;
    int dice_width = 0;

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


    private void addDiceonView(View mtoView, ViewGroup rl_AnimationView, String diceValue){
        final View fromView, toView;

        fromView = findViewById(R.id.ivDiceCircle);
        toView = mtoView;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];


        Rect myViewRect = new Rect();
        toView.getGlobalVisibleRect(myViewRect);
        float destX = myViewRect.left;
        float destY = myViewRect.top;

        rl_AnimationView.setVisibility(View.VISIBLE);

        ImageView image_chips = getDiceType(diceValue);

        rl_AnimationView.addView(image_chips);

        if (dice_width <= 0) {
            dice_width = 96;
        }

        int boardWidth = myViewRect.width(), boardHeight = myViewRect.height();


        int centreX = (int) (boardWidth / 2) - (chips_width / 2);
        int centreY = (int) (boardHeight / 2) - (chips_width / 2);

        if (chips_width > 0) {
            if (new Random().nextInt(boardWidth - chips_width)>0) {
                destX = destX + new Random().nextInt(boardWidth - chips_width);
            }
        } else
        {
        }
        destY += centreY;



        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                animationon = false;


            }
        });
        image_chips.setAnimation(a);
        a.startNow();

    }

    private ImageView getDiceType(String diceValue) {
        ImageView dice = new ImageView(this);
        dice.setBackground(Functions.getDrawable(context,R.drawable.dice));
        int diceintValue = Integer.parseInt(diceValue);
        Log.e("jhgdhgjdchgc_", ""+diceintValue);

        switch (diceintValue) {
            case HEART_VALUE:
                HEART_WINCOUNT++;
                dice.setBackground((Functions.getDrawable(context, R.drawable.dic_heart)));
                break;
            case SPADE_VALUE:
                SPADE_WINCOUNT++;
                dice.setBackground((Functions.getDrawable(context, R.drawable.dic_spade)));
                break;
            case DIAMOND_VALUE:
                DIAMOND_WINCOUNT++;
                dice.setBackground((Functions.getDrawable(context, R.drawable.dic_diamond)));
                break;
            case CLUB_VALUE:
                CLUB_WINCOUNT++;
                dice.setBackground((Functions.getDrawable(context, R.drawable.dic_club)));
                break;
            case FACE_VALUE:
                FACE_WINCOUNT++;
                dice.setBackground((Functions.getDrawable(context, R.drawable.dic_face)));
                break;
            case FLAG_VALUE:
                FLAG_WINCOUNT++;
                dice.setBackground((Functions.getDrawable(context, R.drawable.dic_flag)));
                break;
        }

        int chips_size = 96;
        int resource = Functions.GetResourcePath("dots_"+0,context);
        if(resource > 0)
            dice.setImageDrawable(Functions.getDrawable(context,resource));

        dice.setLayoutParams(new ViewGroup.LayoutParams(chips_size, chips_size));

        dice.post(new Runnable() {
            @Override
            public void run() {
                dice_width = dice.getWidth();
            }
        });

        return dice;
    }
}