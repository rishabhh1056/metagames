package com.metagards.metagames._CarRoullete;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
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
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Animations;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Sound;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._CarRoullete.menu.DialogRulesCarRoulette;
import com.metagards.metagames._LuckJackpot.menu.DialogJackpotHistory;
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
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class CarRoullete_Socket extends BaseActivity implements View.OnClickListener {
    Activity context = this;
    TextView txtName, txtBallence, txt_gameId, txtGameRunning, txtGameBets, tvWine, tvLose;
    Button btGameAmount;
    ImageView imgpl1circle, ivWine, ivLose;
    View ChipstoUser;

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

    int[] gameroulate_icon = {
            R.drawable.ic_car_circle_tata, R.drawable.ic_car_circle_mahindra, R.drawable.ic_car_circle_audi, R.drawable.ic_car_circle_bmw, R.drawable.ic_car_circle_mercedez, R.drawable.ic_car_circle_porche, R.drawable.ic_car_lamborghiani, R.drawable.ic_car_circle_ferrari,
    };

    String[] gameInto = {
            "5X", "5X", "5X", "5X", "10X", "15X", "25X", "40X",
    };
    String[] gameroulate_String =
            {
                    "tata",
                    "mahindra",
                    "audi",
                    "BMW",
                    "mercedez",
                    "Porche",
                    "Lamborghini",
                    "Ferrari"
            };

    int[] carSelection_IDS =
            {
                    R.id.ivSelection1,
                    R.id.ivSelection2,
                    R.id.ivSelection3,
                    R.id.ivSelection4,
                    R.id.ivSelection5,
                    R.id.ivSelection6,
                    R.id.ivSelection7,
                    R.id.ivSelection8,
                    R.id.ivSelection9,
                    R.id.ivSelection10,
                    R.id.ivSelection11,
                    R.id.ivSelection12,
                    R.id.ivSelection13,
                    R.id.ivSelection14,
                    R.id.ivSelection15,
                    R.id.ivSelection16,
                    R.id.ivSelection17,
                    R.id.ivSelection18,
                    R.id.ivSelection19,
                    R.id.ivSelection20,
                    R.id.ivSelection21,
                    R.id.ivSelection22,
            };

    LinearLayout lnrOnlineUser;

    int drawableSelectionbg = R.drawable.ic_carroulate_selectedbg;

    private static final String URL = "http://64.227.186.5:3002/car_roulette";

    Socket mSocket;

    public String token = "";
    int version = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carroullete);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket(URL, opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.connect();
        mSocket.emit("car_roulette_timer" , "car_roulette_timer");

        Initialization();
        setDataonUser();

        mSocket.on("car_roulette_timer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //     restartGame();
                        //    aaraycards.clear();

                        String resp = String.valueOf(args[0]);
                        Log.v("RES_Timer_Response", resp);

                        Integer intger = (Integer) args[0];
                        time_remaining = intger;

                        if (intger == 1) {

                            getTextView(R.id.tvStartTimerNew).setText("0");//

                            cancelStartGameTimmer();
                            stopPlaying();
                            isGameTimerStarted = false;
                            isRounlateStarted = false;
                            removeallSelection();

                        }
                        else {
                            getTextView(R.id.tvStartTimerNew).setText(intger + "");
                            txtGameBets.setVisibility(View.VISIBLE);

                        }

                    }
                });

            }
        });

        mSocket.on("car_roulette_status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String resp = String.valueOf(args[0]);
                        Log.v("RES_Socket_Response", resp);
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equalsIgnoreCase("200")) {
                                JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");
                                JSONArray online_users = jsonObject.getJSONArray("online_users");
                                int online = jsonObject.getInt("online");
                                ((TextView) findViewById(R.id.tvonlineuser))
                                        .setText("" + online_users.length());
                                JSONArray last_winning = jsonObject.getJSONArray("last_winning");

                                for (int i = 0; i < arraygame_dataa.length(); i++) {
                                    JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                    if (last_winning.length() > 0 && !game_id.equals(welcome_bonusObject.getString("id"))) {
                                        addLastWinBetonView(last_winning);
                                    }

                                    game_id = welcome_bonusObject.getString("id");
                                    status = welcome_bonusObject.getString("status");
                                    winning = welcome_bonusObject.getString("winning");
                                    String end_datetime = welcome_bonusObject.getString("end_datetime");
                                    added_date = welcome_bonusObject.getString("added_date");

                                    //    time_remaining = welcome_bonusObject.optInt("time_remaining");
                                    //  updated_date  = welcome_bonusObject.getString("updated_date");
                                }
                                String onlineuSer = jsonObject.getString("online");
//                            txt_online.setText("Online User "+onlineuSer);
//                            JSONArray arrayprofile = jsonObject.getJSONArray("profile");
//                            for (int i = 0; i < arrayprofile.length(); i++) {
//                                JSONObject profileObject = arrayprofile.getJSONObject(i);
//                                //  GameStatus model = new GameStatus();
//                                user_id = profileObject.getString("id");
//                                user_id_player1 = user_id;
//                                name = profileObject.getString("name");
//                                wallet = profileObject.getString("wallet");
//                                profile_pic = profileObject.getString("profile_pic");
//                                Picasso.get().load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);
////                                txtBallence.setText(wallet);
//                                txtName.setText(name);
//                            }

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
                                Functions.LOGE("GameActivity","status : "+status+" isGameBegning : "+isGameBegning);
                                if (status.equals("0") && !isGameBegning) {

                                    RestartGame(false);
//                                    if (time_remaining > 0) {
//                                        startBetAnim();
//                                    } else {
//                                        cancelStartGameTimmer();
//                                    }
                                    startBetAnim();
                                } else if (status.equals("0") && isGameBegning) {

                                    PlaySaund(R.raw.car_engine_start);
                                    for (int i = 0; i < rec_rules.getChildCount(); i++) {
                                        CarRollouteAdapter.holder holder = (CarRollouteAdapter.holder) rec_rules.getChildViewHolder(rec_rules.getChildAt(i));
                                        View view = holder.itemView.findViewById(R.id.rltContainer);
                                        if (view != null)
                                            DummyChipsAnimations(lnrOnlineUser, view, rl_AnimationView);
                                    }
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
                                parseGameUsersAmount(jsonObject);
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



        /*will check erro*/
    }

    private void Initialization() {
        sound = new Sound();

        initSoundPool();

        lnrOnlineUser = findViewById(R.id.lnrOnlineUser);
        rl_AnimationView = ((RelativeLayout) findViewById(R.id.sticker_animation_layout));
        ChipstoUser = findViewById(R.id.ChipstoUser);
        btGameAmount = findViewById(R.id.btGameAmount);
        lnrfollow = findViewById(R.id.lnrfollow);
        initRulesSelectAdapter();
        txtName = findViewById(R.id.txtName);
        imgpl1circle = findViewById(R.id.imgpl1circle);

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
//      startService();
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

    CarRollouteAdapter carRollouteAdapter;
    RecyclerView rec_rules;

    private void initRulesSelectAdapter() {
        rec_rules = findViewById(R.id.rec_rules);
        rec_rules.setLayoutManager(new GridLayoutManager(this, 4));
        carRollouteAdapter = new CarRollouteAdapter(context);
        carRollouteAdapter.onItemSelectListener(new OnItemClickListener() {
            @Override
            public void Response(View v, int position, Object object) {
                CarRollouteModel carRollouteModel = (CarRollouteModel) object;
                betplace = "" + carRollouteModel.rule_value;  //get value from json set it into inner box
                //betplace = tagamountselected;
                if (Functions.isStringValid(betplace) && betValidation()) {
                    // putbet("" + betplace, carRollouteModel);

                    putbet("" + betplace, carRollouteModel);
                }
            }
        });
        rec_rules.setAdapter(carRollouteAdapter);
        setSetRulesValue();
        rec_rules.post(new Runnable() {
            @Override
            public void run() {
                initDisplayMetrics();
            }
        });
    }

    List<CarRollouteModel> rulesModelList = new ArrayList<>();
    View betingViews[];

    private void setSetRulesValue() {
        rulesModelList.clear();
        for (int i = 0; i < gameroulate_String.length; i++) {
            String item = gameroulate_String[i];
            int item_icon = gameroulate_icon[i];
            rulesModelList.add(new CarRollouteModel(item, item_icon, i + 1, 0, 0, gameInto[i]));
        }

        carRollouteAdapter.setArraylist(rulesModelList);

        betingViews = new View[gameroulate_String.length];

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
            //chip selection coin
            @Override
            public void onClick(View view1) {
                tagamountselected = (String) view1.getTag();
                Log.d("inner_tag", "click" + tagamountselected);
                betplace = tagamountselected;
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
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

    private void startService() {
        timerstatus = new Timer();
        timerstatus.scheduleAtFixedRate(new TimerTask() {
                                            @Override
                                            public void run() {
                                                // if (table_id.trim().length() > 0) {
                                                if (isCardsDisribute) {
                                                } else {
//                                                    CALL_API_getGameStatus();
                                                }
                                                // }
                                            }
                                        },
//Set how long before to start calling the TimerTask (in milliseconds)
                200,
//Set the amount of time between each execution (in milliseconds)
                timertime);
    }

    CountDownTimer gameStartTime, gameRoulateTimer;
    boolean isGameTimerStarted = false;
    int selectionCountDown = -1;
    int roulateInterver = 300;

    private void CardsDistruButeTimer() {

        if (isGameTimerStarted && getTextView(R.id.tvStartTimerNew).getVisibility() == View.VISIBLE)
            return;
        count = 0;
        gameStartTime = new CountDownTimer((time_remaining * timer_interval), timer_interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                isGameTimerStarted = true;
                float count = millisUntilFinished / timer_interval;
//                getTextView(R.id.tvStartTimer).setVisibility(View.VISIBLE);
                getTextView(R.id.tvStartTimerNew).setText("" + count + "");
                PlaySaund(R.raw.car_engine_start);
                //   manageRoullete();
                for (int i = 0; i < rec_rules.getChildCount(); i++) {
                    CarRollouteAdapter.holder holder = (CarRollouteAdapter.holder) rec_rules.getChildViewHolder(rec_rules.getChildAt(i));
                    View view = holder.itemView.findViewById(R.id.rltContainer);
                    if (view != null)
                        DummyChipsAnimations(lnrOnlineUser, view, rl_AnimationView);
                }
            }

            @Override
            public void onFinish() {
                stopPlaying();
                isGameTimerStarted = false;
                isRounlateStarted = false;
                removeallSelection();
            }
        };
        gameStartTime.start();


    }

    int dragonWidth = 0, dragonHeight = 0;

    private void initDisplayMetrics() {
        View view = getBettingView(0);
        view.post(new Runnable() {
            @Override
            public void run() {
                dragonWidth = view.getWidth();
                dragonHeight = view.getHeight();
            }
        });
    }


    private void DummyChipsAnimations(View mfromView, View mtoView, ViewGroup rl_AnimationView) {

//        if (mfromView == null || mtoView == null || rl_AnimationView == null) {
//            // Handle null references appropriately
//            return;
//        }

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

        if (chips_width <= 0) {
            chips_width = 96;
        }

        int boardWidth = dragonWidth, boardHeight = dragonHeight;

//        if(toView.getId() == R.id.rltTie)
//        {
//            boardWidth = tieWidth;
//            boardHeight = tieHeight;
//            boardX = tieX;
//            boardY = tieY;
//        }

        int centreX = (int) (boardWidth / 2) - (chips_width / 2);
        int centreY = (int) (boardHeight / 2) - (chips_width / 2);

        if (chips_width > 0) {
            destX = destX + new Random().nextInt(boardWidth - chips_width);
        } else
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

//      PlaySaund(R.raw.teenpattichipstotable);
        playSound(SOUND_2);
    }

    int chips_width = 0;

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


    private View getBettingView(int position) {
        View rltContainer = null;
        CarRollouteAdapter.holder holder = (CarRollouteAdapter.holder)
                rec_rules.findViewHolderForAdapterPosition(position);
        if (null != holder) {
            rltContainer = findViewById(R.id.ivContainerbg);
        }
        return rltContainer;
    }

    CountDownTimer wheelCountDown;
    Handler wheelCount = new Handler();
    Runnable wheelRunnable;

    private void wheelTimer() {
        count = 0;
        wheelRunnable = new Runnable() {
            @Override
            public void run() {
                if (wheelSelectionCount < 0) {
                    count = 0;
                    selectionCountDown = -1;

                    txtGameBets.clearAnimation();
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            counttimerforcards.start();
                        }
                    }, 1500);
                } else {
                    if (count >= 22)
                        count = 0;
                    manageRoullete();
                    wheelCount.postDelayed(wheelRunnable, 200);
                }
            }
        };
        wheelCount.post(wheelRunnable);
    }

    private void cancelWheelCountDown() {
        if (wheelCountDown != null) {
            wheelCountDown.cancel();
            wheelCountDown.onFinish();
        }
        if (wheelRunnable != null)
            wheelCount.removeCallbacks(wheelRunnable);
    }

    private int getWinnerCarsPosition() {
        int win = Integer.parseInt(winning);
        switch (win) {
            case 1:
                return 5;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 1;
            case 5:
                return 4;
            case 6:
                return 11;
            case 7:
                return 9;
            case 8:
                return 10;
            default:
                return 0;
        }
    }

    private void manageRoullete() {
        isRounlateStarted = true;
        int position = count;
        if (selectionCountDown != -1) {
            ImageView selectionView = findViewById(carSelection_IDS[selectionCountDown]);
            selectionView.setVisibility(View.GONE);
        }

        ImageView selectionView = findViewById(carSelection_IDS[position]);
        selectionView.setVisibility(View.VISIBLE);
        selectionCountDown = position;
        Functions.LOGE("AnimalRoulette", "selectioncount: " + selectionCountDown);
        count++;
        wheelSelectionCount--;

        if (wheelSelectionCount < 0) {
            selectionView.setBackground(Functions.getDrawable(context, R.drawable.ic_circle));
//            int circleSize = (int) context.getResources().getDimension(R.dimen.dp70);
//            selectionView.getLayoutParams().height = circleSize;
//            selectionView.getLayoutParams().width = circleSize;
//            selectionView.requestLayout();
            BlinkAnimation(selectionView);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(50); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            selectionView.startAnimation(anim);
        }
    }

    private void resetCarsBlinkAnimation() {
        for (int carSelectionids : carSelection_IDS) {
            View carSelection = findViewById(carSelectionids);
            carSelection.setBackground(Functions.getDrawable(context, drawableSelectionbg));
            carSelection.clearAnimation();
        }
    }

    private void removeallSelection() {
        for (int id : carSelection_IDS) {
            ImageView roulateSelection = findViewById(id);
            roulateSelection.setVisibility(View.GONE);
        }
    }

    private void cancelStartGameTimmer() {
        if (gameStartTime != null) {
            gameStartTime.cancel();
            gameStartTime.onFinish();
        }

        if (gameRoulateTimer != null) {
            gameRoulateTimer.cancel();
            gameRoulateTimer.onFinish();
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
        cancelWheelCountDown();
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

//    private void CALL_API_getGameStatus() {
//        HashMap params = new HashMap();
//        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
//        params.put("token", SharePref.getInstance().getString("token", ""));
//        params.put("room_id", "1");
//        ApiRequest.Call_Api(context, Const.CarRouletteStatus, params, new Callback() {
//            @Override
//            public void Responce(String resp, String type, Bundle bundle) {
//                if (resp != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(resp);
//                        String code = jsonObject.getString("code");
//                        String message = jsonObject.getString("message");
//
//                        if (code.equalsIgnoreCase("200")) {
//                            JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");
//                            JSONArray online_users = jsonObject.getJSONArray("online_users");
//                            int online = jsonObject.getInt("online");
//                            ((TextView) findViewById(R.id.tvonlineuser)).setText("" + online_users.length());
//
//                            JSONArray last_winning = jsonObject.getJSONArray("last_winning");
//
//                            for (int i = 0; i < arraygame_dataa.length(); i++) {
//                                JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);
//
//                                if (last_winning.length() > 0 && !game_id.equals(welcome_bonusObject.getString("id"))) {
//                                    addLastWinBetonView(last_winning);
//                                }
//
//                                game_id = welcome_bonusObject.getString("id");
//                                status = welcome_bonusObject.getString("status");
//                                winning = welcome_bonusObject.getString("winning");
//                                String end_datetime = welcome_bonusObject.getString("end_datetime");
//                                added_date = welcome_bonusObject.getString("added_date");
//                                time_remaining = welcome_bonusObject.optInt("time_remaining");
//                                //  updated_date  = welcome_bonusObject.getString("updated_date");
//                            }
//                            String onlineuSer = jsonObject.getString("online");
////                          txt_online.setText("Online User "+onlineuSer);
//                            JSONArray arrayprofile = jsonObject.getJSONArray("profile");
//
//                            for (int i = 0; i < arrayprofile.length(); i++) {
//                                JSONObject profileObject = arrayprofile.getJSONObject(i);
//                                //  GameStatus model = new GameStatus();
//                                user_id = profileObject.getString("id");
//                                user_id_player1 = user_id;
//                                name = profileObject.getString("name");
//                                wallet = profileObject.getString("wallet");
//                                profile_pic = profileObject.getString("profile_pic");
//                                Picasso.get().load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);
////                                txtBallence.setText(wallet);
//                                txtName.setText(name);
//                            }
//                            JSONArray arraypgame_cards = jsonObject.getJSONArray("game_cards");
//                            for (int i = 0; i < arraypgame_cards.length(); i++) {
//                                JSONObject cardsObject = arraypgame_cards.getJSONObject(i);
//                                //  GameStatus model = new GameStatus();
//                                String card = cardsObject.getString("card");
//                                aaraycards.add(card);
//                            }
////New Game Started here ------------------------------------------------------------------------
//                            if (status.equals("0") && !isGameBegning) {
//                                RestartGame(false);
//                                if (time_remaining > 0) {
//                                    startBetAnim();
////                                    Toast.makeText(context, "Bet Started", Toast.LENGTH_SHORT).show();
//                                    CardsDistruButeTimer();
//                                } else {
//                                    cancelStartGameTimmer();
//                                }
//                            } else if (status.equals("0") && isGameBegning) {
//                            }
////Game Started here
//                            if (status.equals("1") && !isGameBegning) {
//                                VisiblePleasewaitforNextRound(true);
//                            }
//
//                            if (status.equals("1") && isGameBegning) {
//                                isGameBegning = false;
//                                Log.v("game", "stoped");
//                                if (aaraycards.size() > 0) {
//                                    cancelStartGameTimmer();
//                                    if (counttimerforcards != null) {
//                                        counttimerforcards.cancel();
//                                    }
//                                    counttimerforcards = new CountDownTimer(aaraycards.size() * 1000, 1000) {
//                                        @Override
//                                        public void onTick(long millisUntilFinished) {
//                                            isCardsDisribute = true;
//                                            makeWinnertoPlayer("");
//                                            makeLosstoPlayer("");
//                                            setDefultBackgroundforCard();
//                                            if (aaraycards != null && aaraycards.size() >= 2 && !isCardDistribute) {
//                                                CardAnimationUtils();
//                                                isCardDistribute = true;
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFinish() {
////                                                getStatus();
//                                            //secondtimestart(18);
//                                            VisiblePleasewaitforNextRound(true);
//                                            isCardsDisribute = false;
//                                            highlistWinRules(Integer.parseInt(winning));
//
//                                        }
//                                    };
//                                    stopBetAnim();
//
//                                }
//                            }
//                        }
//                        else {
//                            if (jsonObject.has("message")) {
//                                Functions.showToast(context, message);
//                            }
//                        }
//                        if (status.equals("1")) {
////                            VisiblePleasewaitforNextRound(true);
//                            VisiblePleaseBetsAmount(false);
//                        }
//                        else {
//                            VisiblePleasewaitforNextRound(false);
//                            parseGameUsersAmount(jsonObject);
//                            parseLastBetAmount(jsonObject);
//                            if (!isConfirm)
//                                VisiblePleaseBetsAmount(true);
//                            setDefultBackgroundforCard();
//                            makeWinnertoPlayer("");
//                            makeLosstoPlayer("");
//                        }
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

    LinearLayout lnrcancelist;

    private void addLastWinBetonView(JSONArray last_bet) throws JSONException {
        lnrcancelist = findViewById(R.id.lnrcancelist);
        lnrcancelist.removeAllViews();
        for (int i = 0; i < last_bet.length(); i++) {
            String lastbet = last_bet.getJSONObject(i).getString("winning");
            addLastWinBet(lastbet);
        }
    }

    private void addLastWinBet(String items) {
        int winCar = Integer.parseInt(items);
        if (winCar != 0)
            --winCar;
        View view = LayoutInflater.from(context).inflate(R.layout.item_carroulate_lastwin, null);
        ImageView ivlastwin = view.findViewById(R.id.ivlastwin);
        try {
            Glide.with(context).load(gameroulate_icon[winCar]).into(ivlastwin);
        } catch (Exception e) {
        }
        lnrcancelist.addView(view);
    }

    private void setDefultBackgroundforCard() {
//        ivCardbg.setBackground(Functions.getDrawable(context, R.drawable.ic_jackpot_cardsbg));
    }

    private void highlistWinRules(int winning_rule_value) {
        try {
            for (CarRollouteModel model : rulesModelList) {
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
            carRollouteAdapter.notifyDataSetChanged();
            setHighlightedBackgroundforCard(); // background table winner
//            getTextView(R.id.tvStartTimer).setText(rulesName(winning_rule_value));
//            getTextView(R.id.tvStartTimer).setVisibility(Functions.checkisStringValid(rulesName(winning_rule_value))
//                    ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String rulesName(int ruleVal) {
        String ruleName = "";
        for (int i = 0; i < gameroulate_String.length; i++) {
            ruleName = gameroulate_String[i];
            if (ruleVal == i)
                break;
        }

        return ruleName;
    }

    private void setHighlightedBackgroundforCard() {
        removeallSelection();
        ImageView highlightImage = findViewById(carSelection_IDS[getWinnerCarsPosition()]);
        highlightImage.setVisibility(View.VISIBLE);
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

    private void animatedUsersPutAmount(int id, int bet, int amount) {
        for (CarRollouteModel model : rulesModelList) {
            if (model.rule_value == bet) {
                model.setLast_added_id(id);
                model.setLast_added_rule_value(bet);
                model.setLast_added_amount(amount);
                model.setAnimatedAddedAmount(true);
                break;
            }
        }
        carRollouteAdapter.notifyDataSetChanged();
    }


    private void parseGameUsersAmount(JSONObject jsonObject) throws JSONException {
        int total_jackpot_amount = 0;
        int lamborghini_amount = jsonObject.getInt("lamborghini_amount");
        int toyota_amount = jsonObject.getInt("toyota_amount");
        int mahindra_amount = jsonObject.getInt("mahindra_amount");
        int audi_amount = jsonObject.getInt("audi_amount");
        int mercedes_amount = jsonObject.getInt("mercedes_amount");
        int bmw_amount = jsonObject.getInt("bmw_amount");
        int porsche_amount = jsonObject.getInt("porsche_amount");
        int ferrari_amount = jsonObject.getInt("ferrari_amount");
        for (CarRollouteModel model : rulesModelList) {
            if (model.rule_type.equalsIgnoreCase(gameroulate_String[0])) {
                model.added_amount = toyota_amount;
            } else if (model.rule_type.equalsIgnoreCase(gameroulate_String[1])) {
                model.added_amount = mahindra_amount;

            } else if (model.rule_type.equalsIgnoreCase(gameroulate_String[2])) {
                model.added_amount = audi_amount;

            } else if (model.rule_type.equalsIgnoreCase(gameroulate_String[3])) {
                model.added_amount = bmw_amount;


            } else if (model.rule_type.equalsIgnoreCase(gameroulate_String[4])) {

                model.added_amount = mercedes_amount;

            } else if (model.rule_type.equalsIgnoreCase(gameroulate_String[5])) {

                model.added_amount = porsche_amount;

            } else if (model.rule_type.equalsIgnoreCase(gameroulate_String[6])) {
                model.added_amount = lamborghini_amount;

            } else if (model.rule_type.equalsIgnoreCase(gameroulate_String[7])) {
                model.added_amount = ferrari_amount;

            }
        }
        carRollouteAdapter.notifyDataSetChanged();
    }

    int count = 0;
    boolean isRounlateStarted = false;

    private void CardAnimationUtils() {

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
//        txtGameBets.setVisibility(visible ? View.VISIBLE : View.GONE);
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
//        txtGameRunning.setVisibility(visible ? View.VISIBLE : View.GONE);

        if (visible) {
            if (!Functions.checkisStringValid(((TextView) findViewById(R.id.txtcountdown)).getText().toString().trim()))
                pleasewaintCountDown.start();

//            BlinkAnimation(txtGameRunning);
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
                ((TextView) findViewById(R.id.txtcountdown)).setText(second + "s");
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

    }

    private void putbet(final String type, CarRollouteModel carRollouteModel) {
        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("game_id", game_id);
        params.put("bet", "" + type);
        params.put("amount", "" + GameAmount);
        ApiRequest.Call_Api(context, Const.CarRoulettePUTBET, params, new Callback() {
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
                            txtBallence.setText(wallet);
                            Functions.showToast(context, "Bet has been added successfully!");
                            betvalue = "";
//                            isConfirm = true;
                            isBetputes = true;
                            VisiblePleaseBetsAmount(false);
                            carRollouteModel.select_amount = GameAmount;
                            carRollouteAdapter.notifyDataSetChanged();
                        } else {
                            RemoveChips();
                            if (jsonObject.has("message")) {
//                                Functions.showToast(context, message);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CarRouletteCENCEL_BET,
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

    private void repeatBet() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CarRouletteREPEAT_BET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Repeat Responce", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equals("200")) {
                                wallet = jsonObject.getString("wallet");
                                String bet = jsonObject.getString("bet");
                                // bet_id = jsonObject.getString("bet_id");
                                String amount = jsonObject.getString("amount");
                                txtBallence.setText(wallet);
                                betvalue = amount;
                                betplace = bet;
                                if (bet.equals("0")) {
                                } else {
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
        txtName.setText("" + SharePref.getInstance().getString(SharePref.u_name));
        txtBallence.setText(Variables.CURRENCY_SYMBOL + "" + SharePref.getInstance().getString(SharePref.wallet));
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
//        startBetAnim();
//        ivCardbg.setBackground(Functions.getDrawable(context, R.drawable.ic_jackpot_cardsbg));
        resetCarsBlinkAnimation();
        RemoveChips();
        setSetRulesValue();
        rl_AnimationView.removeAllViews();
        VisiblePleasewaitforNextRound(false);
        cancelStartGameTimmer();
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

    int wheelSelectionCount = 22;

    private void stopBetAnim() {
//        Toast.makeText(context, "Bet Stops", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        wheelSelectionCount = 22;
        wheelSelectionCount += getWinnerCarsPosition();
        wheelTimer();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(context, "nice", Toast.LENGTH_SHORT).show();
                txtGameBets.setVisibility(View.GONE);
            }
        }, 1250);
    }

    private RequestManager LoadImage() {
        return Glide.with(context);
    }

    private boolean betValidation() {
        if (isConfirm) {
            Functions.showToast(context, "Bet Already Confirmed So Not Allowed to Put again");
            return false;
        } else if (!canbet) {
            Functions.showToast(context, "Game Already Started You can not put coins");
            return false;
        }
        return true;
    }

    public void confirmBooking(View view) {
//        if (Functions.isStringValid(betplace) && betValidation())
//            putbet(betplace, carRollouteModel);
    }

    public void cancelBooking(View view) {
        cancelbet();
        removeBet();
        RemoveChips();
    }

    public void openGameRules(View view) {
        // DialogJackpotlastWinHistory.getInstance(context).show();
        DialogRulesCarRoulette.getInstance(context).show();
    }

    public void openJackpotHistory(View view) {
        DialogJackpotHistory.getInstance(context).show();
    }

    public void openJackpotLasrWinHistory(View view) {
        DialogRulesJackpot.getInstance(context).setRoomid(game_id).show();
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