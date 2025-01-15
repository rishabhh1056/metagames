package com.metagards.metagames._LuckJackpot;

import static com.metagards.metagames.Utils.Functions.ANIMATION_SPEED;
import static com.metagards.metagames._AdharBahar.Fragments.GameFragment.MY_PREFS_NAME;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Animations;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Sound;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._LuckJackpot.menu.DialogJackpotHistory;
import com.metagards.metagames._LuckJackpot.menu.DialogJackpotlastWinHistory;
import com.metagards.metagames._LuckJackpot.menu.DialogRulesJackpot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class LuckJackPot_A_Socket extends BaseActivity implements View.OnClickListener {

    Activity context = this;
    TextView txtName, txtBallence, txt_gameId, txtGameRunning, txtGameBets, tvWine, tvLose;
    Button btGameAmount;
    ImageView ivWinnerImage, ivJackpotCard1, ivJackpotCard2, ivJackpotCard3, ivWine, ivLose;
    View ChipstoUser;

    private final String TIGER = "tiger";
    private final String DRAGON = "dragon";
    private final String TIE = "tie";
    private final String SET = "SET";
    private final String PURE_SEQ = "PURE SEQ";
    private final String SEQ = "SEQ";
    private final String COLOR = "COLOR";
    private final String PAIR = "PAIR";
    private final String HIGH_CARD = "HIGH";
    private String BET_ON = "";
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
    ImageView ivCardbg;

    String[] jackpotRules = {
            HIGH_CARD,
            PAIR,
            COLOR,
            SEQ,
            PURE_SEQ,
            SET
    };

    int[] jackpotWinBox = {
            R.drawable.ic_jackpot_winbox_blue,
            R.drawable.ic_jackpot_winbox_green,
            R.drawable.ic_jackpot_winbox_orange,
            R.drawable.ic_jackpot_winbox_purple,
            R.drawable.ic_jackpot_winbox_shade,
            R.drawable.ic_jackpot_winbox_green
    };

    int[] jackpotRuleStrip = {
            R.drawable.ic_jackpot_strip_blue,
            R.drawable.ic_jackpot_strip_green,
            R.drawable.ic_jackpot_strip_orange,
            R.drawable.ic_jackpot_strip_purple,
            R.drawable.ic_jackpot_strip_shade,
            R.drawable.ic_jackpot_strip_green
    };

    private static final String URL = "http://64.227.186.5:3002/jackpot";
    Socket mSocket;

    public String token = "";
    int version = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_jack_pot);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket(URL, opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.connect();
        mSocket.emit("jackpot_timer", "jackpot_timer");


        Initialization();
//      setDataonUser();

        mSocket.on("jackpot_timer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String resp = String.valueOf(args[0]);
                        Log.e("RES_Timer_Response", resp);
                        //     restartGame();

                        //    aaraycards.clear();
                        Integer intger = (Integer) args[0];
                        time_remaining = intger;

                        if (intger == 1){

                            cancelStartGameTimmer();
                            getTextView(R.id.tvStartTimer).setText("0");
                            txtGameBets.setVisibility(View.GONE);

                        }else {

                            if (intger >= 15) {
                                startBetAnim();
                            }

                            getTextView(R.id.tvStartTimer).setText(intger+"");
                            txtGameBets.setVisibility(View.VISIBLE);

                        }

                    }
                });

            }
        });


        mSocket.on("jackpot_status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String resp = String.valueOf(args[0]);
                        Log.e("RES_Socket_Response", resp);

//                        CardsDistruButeTimer();
                        try {

                            JSONObject jsonObject = new JSONObject(resp);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equalsIgnoreCase("200")) {

                                JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");
                                JSONArray online_users = jsonObject.getJSONArray("online_users");

                                int online = jsonObject.getInt("online");
                                ((TextView) findViewById(R.id.tvonlineuser)).setText("" + online);

                                JSONArray last_winning = jsonObject.getJSONArray("last_winning");
                                JSONArray big_winner = jsonObject.getJSONArray("big_winner");

                                if (big_winner != null && big_winner.length() > 0) {

                                    try {
                                        JSONObject user_data = big_winner.getJSONObject(0).getJSONArray("user_data").getJSONObject(0);
                                        if (Functions.isActivityExist(context))
                                            Glide.with(context).load(Const.IMGAE_PATH + user_data.getString("profile_pic")).into(ivWinnerImage);
                                        getTextView(R.id.txtName).setText("" + user_data.getString("name"));
                                        getTextView(R.id.txt_gameId).setText("" + Variables.CURRENCY_SYMBOL + user_data.getString("winning_amount"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (last_winning.length() > 0) {
                                    addLastWinBetonView(last_winning);
                                }

                                for (int i = 0; i < arraygame_dataa.length(); i++) {
                                    JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                    //  GameStatus model = new GameStatus();
                                    game_id = welcome_bonusObject.getString("id");
//                                txt_gameId.setText("GAME ID "+game_id);


//                                main_card  = welcome_bonusObject.getString("main_card");
                                    // txt_min_max.setText("Min-Max: "+main_card);
                                    status = welcome_bonusObject.getString("status");
                                    winning = welcome_bonusObject.getString("winning");
                                    String end_datetime = welcome_bonusObject.getString("end_datetime");
                                    added_date = welcome_bonusObject.getString("added_date");
//                                    time_remaining = welcome_bonusObject.optInt("time_remaining");

                                    //  updated_date  = welcome_bonusObject.getString("updated_date");

                                }
                                String onlineuSer = jsonObject.getString("online");
//                               txt_online.setText("Online User "+onlineuSer);
//
//                                JSONArray arrayprofile = jsonObject.getJSONArray("profile");
//
//                                for (int i = 0; i < arrayprofile.length(); i++) {
//                                    JSONObject profileObject = arrayprofile.getJSONObject(i);
//
//                                    //  GameStatus model = new GameStatus();
//                                    user_id = profileObject.getString("id");
//                                    user_id_player1 = user_id;
//                                    name = profileObject.getString("name");
//                                    wallet = profileObject.getString("wallet");
//
//                                    profile_pic = profileObject.getString("profile_pic");
//
////                                Picasso.with(context).load(Const.IMGAE_PATH + profile_pic).into(ivWinnerImage);
//
//                                    txtBallence.setText(wallet);
////                                txtName.setText(name);
//
//                                }

                                SharedPreferences prefs = getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE);
                                user_id_player1 = prefs.getString("user_id", "");

                                JSONArray arraypgame_cards = jsonObject.getJSONArray("game_cards");

                                for (int i = 0; i < arraypgame_cards.length(); i++) {
                                    JSONObject cardsObject = arraypgame_cards.getJSONObject(i);

                                    //  GameStatus model = new GameStatus();
                                    String card = cardsObject.getString("card");
                                    aaraycards.add(card);

                                }
//New Game Started here ------------------------------------------------------------------------

                                if (status.equals("0") && !isGameBegning) {

                                    RestartGame(false);

//                                    if (time_remaining > 0) {
////                                    parseGameUsersAmount(jsonObject);
//                                        startBetAnim();
////                                        CardsDistruButeTimer();
//                                    } else {
//                                        cancelStartGameTimmer();
//                                    }

                                } else if (status.equals("0") && isGameBegning) {
                                    parseGameUsersAmount(jsonObject);

                                }

//Game Started here
                                if (status.equals("1") && !isGameBegning) {
                                    VisiblePleasewaitforNextRound(true);

                                }

                                if (status.equals("1") && isGameBegning) {

                                    isGameBegning = false;
                                    Log.v("game", "stoped");
                                    if (aaraycards.size() > 0) {

                                        cancelStartGameTimmer();
                                        if (counttimerforcards != null) {
                                            counttimerforcards.cancel();
                                        }

                                        counttimerforcards = new CountDownTimer(aaraycards.size() * 1000, 1000) {

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                isCardsDisribute = true;

                                                makeWinnertoPlayer("");
                                                makeLosstoPlayer("");
                                                setDefultBackgroundforCard();


                                                if (aaraycards != null && aaraycards.size() >= 2 && !isCardDistribute) {
                                                    CardAnimationUtils();
                                                    isCardDistribute = true;
                                                }


                                            }

                                            @Override
                                            public void onFinish() {

//                                                getStatus();
                                                //secondtimestart(18);
                                                VisiblePleasewaitforNextRound(true);

                                                isCardsDisribute = false;

                                                highlistWinRules(Integer.parseInt(winning));
//                                            if(betplace != null && !betplace.equalsIgnoreCase("") && betplace.equalsIgnoreCase(winning))
//                                            {
//                                                AnimationUtils(true);
//                                            }
//                                            else {
//
//                                                if(betplace != null && !betplace.equalsIgnoreCase("") && !betplace.equalsIgnoreCase(winning))
//                                                {
//                                                    AnimationUtils(false);
////                                                    makeLosstoPlayer(SharePref.getU_id());
//                                                }
//
//                                            }

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

                                if (!isConfirm)
                                    VisiblePleaseBetsAmount(true);

                                setDefultBackgroundforCard();
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

    }

    private void Initialization() {

        sound = new Sound();
        initSoundPool();

        rl_AnimationView = ((RelativeLayout) findViewById(R.id.sticker_animation_layout));
        ChipstoUser = findViewById(R.id.ChipstoUser);
        ivCardbg = findViewById(R.id.ivCardbg);
        btGameAmount = findViewById(R.id.btGameAmount);
        lnrfollow = findViewById(R.id.lnrfollow);

        initRulesSelectAdapter();

        txtName = findViewById(R.id.txtName);
        ivWinnerImage = findViewById(R.id.ivWinnerImage);

        ivJackpotCard1 = findViewById(R.id.ivJackpotCard1);
        ivJackpotCard2 = findViewById(R.id.ivJackpotCard2);
        ivJackpotCard3 = findViewById(R.id.ivJackpotCard3);

        txtBallence = findViewById(R.id.txtBallence);
        txt_gameId = findViewById(R.id.txt_gameId);
        txtGameRunning = findViewById(R.id.txtGameRunning);
        txtGameBets = findViewById(R.id.txtGameBets);

        ivWine = findViewById(R.id.ivWine);
        ivLose = findViewById(R.id.ivlose);
        tvWine = findViewById(R.id.tvWine);
        tvLose = findViewById(R.id.tvlose);

        rltwinnersymble1 = findViewById(R.id.rltwinnersymble1);
        rtllosesymble1 = findViewById(R.id.rtllosesymble1);


        findViewById(R.id.imgback).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1plus).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1minus).setOnClickListener(this::onClick);
        findViewById(R.id.iv_add).setOnClickListener(this::onClick);

        addChipsonView();

        pleaswaitTimer();

        RestartGame(true);

        setDataonUser();

        initiAnimation();

    }

    public static final int SOUND_1 = 1;
    public static final int SOUND_2 = 2;

    SoundPool mSoundPool;
    HashMap<Integer, Integer> mSoundMap;

    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        mSoundMap = new HashMap<Integer, Integer>();

        if (mSoundPool != null) {
            mSoundMap.put(SOUND_1, mSoundPool.load(this, R.raw.buttontouchsound, 1));
            mSoundMap.put(SOUND_2, mSoundPool.load(this, R.raw.teenpattichipstotable, 1));
        }
    }

    /*
     *Call this function from code with the sound you want e.g. playSound(SOUND_1);
     */

    public void playSound(int sound) {

        if (!SharePref.getInstance().isSoundEnable())
            return;

        AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if (mSoundPool != null) {
            mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, 0, 1.0f);
        }
    }

    JackpotRulesAdapter jackpotRulesAdapter;

    private void initRulesSelectAdapter() {
        RecyclerView rec_rules = findViewById(R.id.rec_rules);
        rec_rules.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        jackpotRulesAdapter = new JackpotRulesAdapter(context);
        jackpotRulesAdapter.onItemSelectListener(new OnItemClickListener() {
            @Override
            public void Response(View v, int position, Object object) {
                JackpotRulesModel jackpotRulesModel = (JackpotRulesModel) object;
                betplace = "" + jackpotRulesModel.rule_value;
                if (Functions.isStringValid(betplace) && betValidation()) {
                    putbet("" + betplace, jackpotRulesModel);

                }


            }
        });
        rec_rules.setAdapter(jackpotRulesAdapter);
        setSetRulesValue();
    }

    List<JackpotRulesModel> rulesModelList = new ArrayList<>();

    private void setSetRulesValue() {
        rulesModelList.clear();
        rulesModelList.add(new JackpotRulesModel(SET, 6, 0, 0, "20%"));
        rulesModelList.add(new JackpotRulesModel(PURE_SEQ, 5, 0, 0, "10X"));
        rulesModelList.add(new JackpotRulesModel(SEQ, 4, 0, 0, "6X"));
        rulesModelList.add(new JackpotRulesModel(COLOR, 3, 0, 0, "5X"));
        rulesModelList.add(new JackpotRulesModel(PAIR, 2, 0, 0, "4X"));
        rulesModelList.add(new JackpotRulesModel(HIGH_CARD, 1, 0, 0, "3X"));
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

    private void initiAnimation() {
        blinksAnimation = AnimationUtils.loadAnimation(context, R.anim.blink);
        blinksAnimation.setDuration(1000);
        blinksAnimation.setRepeatCount(Animation.INFINITE);
        blinksAnimation.setStartOffset(700);
    }

    boolean isCardsDisribute = false;
    int timertime = 4000;
    Timer timerstatus;

    CountDownTimer gameStartTime;
    boolean isGameTimerStarted = false;

    private void CardsDistruButeTimer() {

        if (isGameTimerStarted && getTextView(R.id.tvStartTimer).getVisibility() == View.VISIBLE)
            return;

        gameStartTime = new CountDownTimer((time_remaining * timer_interval), timer_interval) {
            @Override
            public void onTick(long millisUntilFinished) {

                isGameTimerStarted = true;
                float count = millisUntilFinished / timer_interval;

                getTextView(R.id.tvStartTimer).setVisibility(View.VISIBLE);
                getTextView(R.id.tvStartTimer).setText("Betting " + count + "s");

//                PlaySaund(R.raw.teenpattitick);
            }

            @Override
            public void onFinish() {
                stopPlaying();
                isGameTimerStarted = false;

            }
        };


        gameStartTime.start();

    }

    private void cancelStartGameTimmer() {
        if (gameStartTime != null) {
            gameStartTime.cancel();
            gameStartTime.onFinish();
        }
    }

    private TextView getTextView(int id) {

        return ((TextView) findViewById(id));
    }

    @Override
    protected void onDestroy() {
        DestroyGames();
        super.onDestroy();
    }

    private void DestroyGames() {

        cancelStartGameTimmer();

        if (timerstatus != null) {
            timerstatus.cancel();
        }

        stopPlaying();
        mSocket.disconnect();
    }

    public String status = "";
    public String winning;
    private String added_date;
    private String user_id, name, wallet;
    private String profile_pic;
    ArrayList<String> aaraycards = new ArrayList<>();
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

    LinearLayout lnrcancelist;

    private void addLastWinBetonView(JSONArray last_bet) throws JSONException {
        lnrcancelist = findViewById(R.id.lnrcancelist);
        lnrcancelist.removeAllViews();
        for (int i = 0; i < last_bet.length(); i++) {

            String lastbet = last_bet.getJSONObject(i).getString("winning");

            addLastWinBet(lastbet, i);
        }

    }

    private void addLastWinBet(String items, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_last_bet, null);
        TextView tvItems = view.findViewById(R.id.tvItems);
        ImageView ivlastwinbg = view.findViewById(R.id.ivlastwinbg);
        int itemValue = Integer.parseInt(items);
        try {
            ivlastwinbg.setBackground(Functions.getDrawable(context, jackpotRuleStrip[itemValue]));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvItems.setText("" + rulesName(itemValue));
        if (Functions.checkStringisValid(Functions.getStringFromTextView(tvItems)))
            lnrcancelist.addView(view);
    }

    private void setDefultBackgroundforCard() {
        ivCardbg.setBackground(Functions.getDrawable(context, R.drawable.ic_jackpot_cardsbg));
    }

    private void highlistWinRules(int winning_rule_value) {
        try {
            for (JackpotRulesModel model : rulesModelList) {

                if (model.rule_value == winning_rule_value) {
                    model.setWine(true);
                    break;
                }

            }

            if (Functions.isStringValid(betplace) && (winning_rule_value == Integer.parseInt(betplace))) {
                PlaySaund(R.raw.tpb_battle_won);
            } else if (Functions.isStringValid(betplace)) {
//                PlaySaund(R.raw.game_loos_track);
            }

            jackpotRulesAdapter.notifyDataSetChanged();
            setHighlightedBackgroundforCard(winning_rule_value);
            getTextView(R.id.tvStartTimer).setText(rulesName(winning_rule_value));
            getTextView(R.id.tvStartTimer).setVisibility(Functions.checkisStringValid(rulesName(winning_rule_value))
                    ? View.VISIBLE : View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String rulesName(int ruleVal) {
        switch (ruleVal) {
            case 1:
                return HIGH_CARD;
            case 2:
                return PAIR;
            case 3:
                return COLOR;
            case 4:
                return SEQ;
            case 5:
                return PURE_SEQ;
            case 6:
                return SET;

            default:
                return "";
        }
    }

    private void setHighlightedBackgroundforCard(int winning_rule_value) {
//        ivCardbg.setBackground(Functions.getDrawable(context,R.drawable.ic_jackpot_cardsbg_selected));
        try {
            ivCardbg.setBackground(Functions.getDrawable(context, jackpotWinBox[winning_rule_value]));
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
        }

    }

    private void parseLastBetAmount(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("last_bet");
            if (jsonArray.length() > 0) {
                JSONObject lastbetObject = jsonArray.getJSONObject(0);
                int id = lastbetObject.getInt("id");
                int bet = lastbetObject.getInt("bet");
                int amount = lastbetObject.getInt("amount");

                animatedUsersPutAmount(id, bet, amount);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void removeTotalAddedAmount() {
        high_card_amount = 0;
        pair_amount = 0;
        color_amount = 0;
        sequence_amount = 0;
        pure_sequence_amount = 0;

        for (JackpotRulesModel model : rulesModelList) {
            model.added_amount = 0;
        }

        jackpotRulesAdapter.notifyDataSetChanged();
    }

    private void animatedUsersPutAmount(int id, int bet, int amount) {
        for (int i = 0; i < rulesModelList.size(); i++) {
            JackpotRulesModel model = rulesModelList.get(i);
            if (model.rule_value == bet) {
                putAmountonRules(id, bet, amount, i, model);
//                jackpotRulesAdapter.notifyItemChanged(i);
                break;
            }
        }


    }

    private void putAmountonRules(int id, int bet, int amount, int i, JackpotRulesModel model) {
        model.setLast_added_id(id);
        model.setLast_added_rule_value(bet);
        model.setLast_added_amount(amount);
        model.setAnimatedAddedAmount(true);
        jackpotRulesAdapter.notifyItemChanged(i);
    }

    int set_amount;
    int pure_sequence_amount;
    int sequence_amount;
    int color_amount;
    int pair_amount;
    int high_card_amount;

    private void parseGameUsersAmount(JSONObject jsonObject) throws JSONException {
        int total_jackpot_amount = 0;

//        high_card_amount = 0;
//        pair_amount = 0;
//        color_amount = 0;
//        sequence_amount = 0;
//        pure_sequence_amount = 0;
        Handler handler1 = new Handler();
        for (int i = 0; i < rulesModelList.size(); i++) {
            JackpotRulesModel model = rulesModelList.get(i);
            int betRuleTypeOn = model.rule_value;
            int putbetAmount = 0;

            if (model.rule_type.equalsIgnoreCase(SET)) {
                betRuleTypeOn = model.rule_value;
                int mSetAmount = jsonObject.getInt("set_amount");
                putbetAmount = mSetAmount - set_amount;
                set_amount = mSetAmount;
                total_jackpot_amount = total_jackpot_amount + set_amount;
                model.added_amount = set_amount;
            } else if (model.rule_type.equalsIgnoreCase(PURE_SEQ)) {
                betRuleTypeOn = model.rule_value;
                int mPuresequenceamount = jsonObject.getInt("pure_sequence_amount");
                putbetAmount = mPuresequenceamount - pure_sequence_amount;
                pure_sequence_amount = mPuresequenceamount;
                total_jackpot_amount = total_jackpot_amount + pure_sequence_amount;
                model.added_amount = pure_sequence_amount;
            } else if (model.rule_type.equalsIgnoreCase(SEQ)) {
                betRuleTypeOn = model.rule_value;
                int mSequenceamount = jsonObject.getInt("sequence_amount");
                putbetAmount = mSequenceamount - sequence_amount;
                sequence_amount = mSequenceamount;
                total_jackpot_amount = total_jackpot_amount + sequence_amount;
                model.added_amount = sequence_amount;
            } else if (model.rule_type.equalsIgnoreCase(COLOR)) {
                betRuleTypeOn = model.rule_value;
                int mColorAmount = jsonObject.getInt("color_amount");
                putbetAmount = mColorAmount - color_amount;
                color_amount = mColorAmount;
                total_jackpot_amount = total_jackpot_amount + color_amount;
                model.added_amount = color_amount;
            } else if (model.rule_type.equalsIgnoreCase(PAIR)) {
                betRuleTypeOn = model.rule_value;
                int mPaitAmount = jsonObject.getInt("pair_amount");
                putbetAmount = mPaitAmount - pair_amount;
                pair_amount = mPaitAmount;
                total_jackpot_amount = total_jackpot_amount + pair_amount;
                model.added_amount = pair_amount;
            } else if (model.rule_type.equalsIgnoreCase(HIGH_CARD)) {
                betRuleTypeOn = model.rule_value;
                int mHighcardamount = jsonObject.getInt("high_card_amount");
                putbetAmount = mHighcardamount - high_card_amount;
                high_card_amount = mHighcardamount;
                total_jackpot_amount = total_jackpot_amount + high_card_amount;
                model.added_amount = high_card_amount;
            }
            int finalPutbetAmount = putbetAmount;
            int finalBetRuleTypeOn = betRuleTypeOn;
            int finalI = i;
            handler1.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Random random = new Random();
// Generates random integers 0 to 49
                    int x = random.nextInt(50);

                    if (finalPutbetAmount > 0)
                        putAmountonRules(x, finalBetRuleTypeOn, finalPutbetAmount, finalI, model);
                }
            }, 1000 * i);
        }

        total_jackpot_amount = jsonObject.getInt("jackpot_amount");


        ((TextView) findViewById(R.id.tvJackpottotalamount)).setText(Variables.CURRENCY_SYMBOL + "" + total_jackpot_amount);
//        jackpotRulesAdapter.notifyDataSetChanged();
    }

    int count = 0;

    private void CardAnimationUtils() {
        final View[] fromView = {null};
        final View[] toView = {null};

        fromView[0] = ivJackpotCard3;
        toView[0] = ivJackpotCard1;
        count = 0;

        new CountDownTimer(600, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                count++;

                if (count == 1) {
                    addJackpotCard1(aaraycards.get(0), 0);
                } else if (count == 2) {
                    addJackpotCard2(aaraycards.get(1), 1);
                } else if (count == 3) {
                    addJakpotCard3(aaraycards.get(2), 2);
                }

            }

            @Override
            public void onFinish() {
            }
        }.start();

//        CardAnimationAnimations(fromView[0], toView[0],false);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fromView[0] = ivJackpotCard3;
//                toView[0] = ivJackpotCard2;
////                CardAnimationAnimations(fromView[0], toView[0],true);
//            }
//        },500);

        fromView[0] = ivJackpotCard3;
        toView[0] = ivJackpotCard3;

//        CardAnimationAnimations(fromView[0], toView[0],true);

    }

    int coins_count = 10;
    int cards_count = 2;

    private void AnimationUtils(boolean iswin) {
        coins_count = 10;

        if (!iswin)
            makeLosstoPlayer(SharePref.getU_id());
        else
            makeWinnertoPlayer(SharePref.getU_id());
    }

    private void VisiblePleaseBetsAmount(boolean visible) {

//        findViewById(R.id.rltOngoinGame).setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    boolean isPleasewaitShowed = false;

    private void VisiblePleasewaitforNextRound(boolean visible) {

        if (isPleasewaitShowed && visible)
            return;

        if (visible)
            isPleasewaitShowed = true;

        if (blinksAnimation != null) {
            isBlinkStart = false;
            txtGameRunning.clearAnimation();
            blinksAnimation.cancel();
        }

        findViewById(R.id.rltOngoinGame).setVisibility(visible ? View.VISIBLE : View.GONE);
//      txtGameRunning.setVisibility(visible ? View.VISIBLE : View.GONE);

        if (visible) {
            if (!Functions.checkisStringValid(((TextView) findViewById(R.id.txtcountdown)).getText().toString().trim()))
                pleasewaintCountDown.start();
//              BlinkAnimation(txtGameRunning);
        } else {
            pleasewaintCountDown.cancel();
            pleasewaintCountDown.onFinish();
        }


    }

    private void pleaswaitTimer() {
        pleasewaintCountDown = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long second = millisUntilFinished / 1000;

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

        if (isBlinkStart)
            return;

        isBlinkStart = true;
        view.startAnimation(blinksAnimation);
    }

    private void addJackpotCard1(String image_name, int countvaue) {
        int path = 0;
        if (Functions.checkisStringValid(image_name))
            path = Functions.GetResourcePath("" + image_name, context);

        TranslateLayout(ivJackpotCard1, path);

//        Glide.with(context)
//                .load(path > 0 ? path : R.drawable.card_bg)
//                .placeholder(R.drawable.card_bg)
//                .into(ivJackpotCard1);

    }

    private void TranslateLayout(ImageView imageView, int path) {

        if (path > 0)
            cardflipSound();

        int distance = 8000;

        float scale = context.getResources().getDisplayMetrics().density;
        imageView.setCameraDistance(distance * scale);

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.out_animation);
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

    private void addJakpotCard3(String image_name, int countvaue) {

        int path = 0;
        if (Functions.checkisStringValid(image_name))
            path = Functions.GetResourcePath("" + image_name, context);

//        Glide.with(context)
//                .load(path > 0 ? path : R.drawable.card_bg)
//                .placeholder(R.drawable.card_bg)
//                .into(ivJackpotCard3);

        TranslateLayout(ivJackpotCard3, path);

    }

    private void addJackpotCard2(String image_name, int countvaue) {
        int path = 0;
        if (Functions.checkisStringValid(image_name))
            path = Functions.GetResourcePath("" + image_name, context);

//        Glide.with(context)
//                .load(path > 0 ? path : R.drawable.card_bg)
//                .placeholder(R.drawable.card_bg)
//                .into(ivJackpotCard2);
        TranslateLayout(ivJackpotCard2, path);

    }

    private void putbet(final String type, JackpotRulesModel jackpotRulesModel) {

        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("game_id", game_id);
        params.put("bet", "" + type);
        params.put("amount", "" + GameAmount);

        ApiRequest.Call_Api(context, Const.JackpotPUTBET, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if (resp != null) {

                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");


                        if (code.equalsIgnoreCase("200")) {
                            playChipsSound();
                            bet_id = jsonObject.getString("bet_id");
                            wallet = jsonObject.getString("wallet");
                            Log.v("RES_CHECK_PUTBET", "wallet : " + wallet);

                            txtBallence.setText(wallet);
//                            Functions.showToast(context, "Bet has been added successfully!");

                            betvalue = "";
//                            isConfirm = true;
                            isBetputes = true;

                            VisiblePleaseBetsAmount(false);

                            jackpotRulesModel.select_amount += GameAmount;
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.JackpotCENCEL_BET,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")) {

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

//    private void repeatBet() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.JackpotREPEAT_BET,
//                new Response.Listener<String>() {
//
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.v("Repeat Responce", response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String code = jsonObject.getString("code");
//                            String message = jsonObject.getString("message");
//
//                            if (code.equals("200")) {
//
//                                wallet = jsonObject.getString("wallet");
//                                String bet = jsonObject.getString("bet");
//                                // bet_id = jsonObject.getString("bet_id");
//                                String amount = jsonObject.getString("amount");
//                                txtBallence.setText(wallet);
//                                betvalue = amount;
//                                betplace = bet;
//                                if (bet.equals("0")) {
//
//
//                                } else {
//
//                                }
//
//                            }
//                            Functions.showToast(context, message);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //  progressDialog.dismiss();
//                Functions.showToast(context, "Something went wrong");
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                params.put("user_id", prefs.getString("user_id", ""));
//                params.put("token", prefs.getString("token", ""));
//
//                params.put("game_id", game_id);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("token", Const.TOKEN);
//                return headers;
//            }
//        };
//
//        Volley.newRequestQueue(this).add(stringRequest);
//
//    }

    private void setDataonUser() {

//        txtName.setText(""+ SharePref.getInstance().getString(SharePref.u_name));
//        txtBallence.setText(Variables.CURRENCY_SYMBOL+""+ SharePref.getInstance().getString(SharePref.wallet));
        txtBallence.setText(SharePref.getInstance().getString(SharePref.wallet));

//        Glide.with(context)
//                .load(Const.IMGAE_PATH + SharePref.getInstance().getString(SharePref.u_pic))
//                .placeholder(R.drawable.avatar)
//                .into(imgpl1circle);


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
//            rtllosesymble1.setVisibility(View.VISIBLE);

        }

    }

    private MediaPlayer mp;
    boolean isInPauseState = false;

    public void PlaySaund(int saund) {

        if (!SharePref.getInstance().isSoundEnable())
            return;

        if (!isInPauseState) {
            stopPlaying();
            mp = MediaPlayer.create(this, saund);
            mp.start();

        }

    }

    Sound sound;

    public void cardflipSound() {
        if (!isInPauseState) {
            mp = MediaPlayer.create(this, R.raw.teenpatticardflip_android);
            mp.start();
        }
    }

    public void playChipsSound() {
        if (!isInPauseState) {
            playSound(SOUND_2);
        }
    }

    public void playButtonTouchSound() {
        if (!isInPauseState) {
            playSound(SOUND_1);
        }
    }

    public void playCountDownSound() {

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

        switch (v.getId()) {
            case R.id.imgback: {
                onBackPressed();
            }
            break;

            case R.id.imgpl1plus: {

                ChangeGameAmount(true);
            }
            break;

            case R.id.imgpl1minus: {
                ChangeGameAmount(false);
            }
            break;

            case R.id.iv_add: {
                openBuyChipsListActivity();
            }
        }
    }

    private void openBuyChipsListActivity() {
        startActivity(new Intent(context, BuyChipsList.class));
    }

    private void ChangeGameAmount(boolean isPlus) {


        if (isConfirm) {
            return;

        }

        if (isPlus && GameAmount < maxGameAmt) {
            GameAmount = GameAmount + StepGameAmount;
        } else if (!isPlus && GameAmount > minGameAmt) {
            GameAmount = GameAmount - StepGameAmount;
        }

        btGameAmount.setText(Variables.CURRENCY_SYMBOL + "" + GameAmount);
    }

    private void RestartGame(boolean isFromonCreate) {

        SharePref.getInstance().putInt(SharePref.lastAmountAddedID, -1);
        removeTotalAddedAmount();

        ivCardbg.setBackground(Functions.getDrawable(context, R.drawable.ic_jackpot_cardsbg));
        RemoveChips();
        setSetRulesValue();

        addJackpotCard1("0", 0);
        addJackpotCard2("0", 1);
        addJakpotCard3("0", 1);

        VisiblePleasewaitforNextRound(false);

//        cancelStartGameTimmer();

        isCardDistribute = false;

        txtBallence.setText(wallet);

        removeBet();
        aaraycards.clear();
        if (!isFromonCreate)
            isGameBegning = true;

        UserProfile();

    }

    private void removeBet() {
        canbet = true;
        isConfirm = false;
        bet_id = "";
        betplace = "";
        betvalue = "";
    }

    private void RemoveChips() {
        BET_ON = "";
        addChipsonView();
    }

    @Override
    public void onBackPressed() {

        Functions.Dialog_CancelAppointment(context, "Confirmation", "Leave ?", new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if (resp.equals("yes")) {
                    stopPlaying();
                    finish();
                    mSocket.disconnect();
                }
            }
        });
    }

    boolean animationon = false;
    RelativeLayout rl_AnimationView;

    private void ChipsAnimations(View mfromView, View mtoView, boolean iswin) {

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

        int stickerId = Functions.GetResourcePath("ic_dt_chips", context);

        int chips_size = (int) getResources().getDimension(R.dimen.chips_size);

        if (stickerId > 0)
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
                if (coins_count <= 0) {
                    RemoveChips();
                    rl_AnimationView.removeAllViews();
                    if (!iswin)
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

        Log.e("MainActivity", "FromView : " + fromRect);
        Log.e("MainActivity", "toView : " + toRect);

        PlaySaund(R.raw.teenpattichipstotable);


    }

    private void startBetAnim() {
        txtGameBets.setVisibility(View.VISIBLE);
        txtGameBets.setBackgroundResource(R.drawable.iv_bet_begin);
        txtGameBets.bringToFront();
        ScaleAnimation fade_in = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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

    private void stopBetAnim() {
        txtGameBets.setVisibility(View.VISIBLE);
        txtGameBets.setBackgroundResource(R.drawable.iv_bet_stops);

//        Animation sgAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_grow);
//        txtGameBets.startAnimation(sgAnimation);
//        txtGameBets.startAnimation(sgAnimation);

        txtGameBets.bringToFront();
        ScaleAnimation fade_in = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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

    private void CardAnimationAnimations(View mfromView, View mtoView, boolean isTiger) {
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

        int stickerId = Functions.GetResourcePath("backside_card", context);

        int cards_size = (int) getResources().getDimension(R.dimen.jackpot_card_size);

        if (stickerId > 0)
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


                addJackpotCard1(aaraycards.get(0), 0);
                addJackpotCard2(aaraycards.get(1), 1);
                addJakpotCard3(aaraycards.get(2), 2);

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

    private RequestManager LoadImage() {
        return Glide.with(context);
    }

    private boolean betValidation() {

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
        DialogRulesJackpot.getInstance(context).show();
    }

    public void openJackpotHistory(View view) {
        DialogJackpotHistory.getInstance(context).show();
    }

    public void openJackpotLasrWinHistory(View view) {
        DialogJackpotlastWinHistory.getInstance(context).setRoomid(game_id).show();
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