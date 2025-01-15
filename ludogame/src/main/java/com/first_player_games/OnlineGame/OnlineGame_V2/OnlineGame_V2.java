package com.first_player_games.OnlineGame.OnlineGame_V2;

import static com.first_player_games.Api.EndPoints.SERVER_PATH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first_player_games.Api.LudoApiRepository;
import com.first_player_games.Api.Resource;
import com.first_player_games.BaseActivity;
import com.first_player_games.ModelResponse.UserdataModelResponse;
import com.first_player_games.Others.CommonUtils;
import com.first_player_games.Others.Functions;
import com.first_player_games.Others.ProgressPleaseWait;
import com.first_player_games.Others.SharePref;
import com.first_player_games.R;
import com.first_player_games.ludoApi.model.LudoViewModel;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class OnlineGame_V2 extends BaseActivity {

    private WebSocket webSocket;
    //private String SERVER_PATH = "ws://SERVER-IP-HERE:PORT-NUMBER-HERE";

    DotsView dotview;
    Gamestate state;

    LinearLayout dicecontainer;
    LinearLayout playernamecontainer;
    TextView turnnameview;
    ImageView dice;
    DiceHandler diceHandler;

    EditText cheatview;

    boolean[] players;
    int playernumber = -1;
    String roomid;
    String table_id;

    //    DatabaseReference room,action;
    FirebaseFunctions functions;
    MediaPlayer diceSound;

    int[] playenameview = new int[]{
            R.id.online_game_playername_red,
            R.id.online_game_playername_yellow,
            R.id.online_game_playername_blue,
            R.id.online_game_playername_green
    };

    ImageView[] dices;
    ImageView arrow;

    String[] names;
    AlertDialog dialogue;

    int duration = 50;
    int count = 50;
    int tempVar = 0;
    CountDownTimer timer;
    ProgressDialog dialog;
    int[] turnsSkipped;
    int diamonds = -1;
    boolean gotReward = false;
    int game_id;
    LudoViewModel ludoViewModel;
    LudoApiRepository ludoApiRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game__v2);

        initiateSocketConnection();
        ludoApiRepository = LudoApiRepository.getInstance().init(getApplicationContext());
        ludoViewModel = LudoViewModel.getInstance().init(getApplicationContext());

        diamonds = getIntent().getIntExtra("diamonds",-1);
        System.out.println("DIAMONDS IN GAME ARE    "+diamonds);
        dotview = findViewById(R.id.ludodotview);
        dicecontainer = findViewById(R.id.dicecontainer);
        cheatview = findViewById(R.id.cheatview);
        diceSound = MediaPlayer.create(OnlineGame_V2.this,R.raw.dice_rolling_effect);

        playernamecontainer = findViewById(R.id.playernameCotainer);
        turnnameview = findViewById(R.id.playernameview);
        arrow = findViewById(R.id.arrow_view);

        dialog = ProgressPleaseWait.getDialogue(OnlineGame_V2.this);

        dices = new ImageView[]{
                findViewById(R.id.dice_red),
                findViewById(R.id.dice_yellow),
                findViewById(R.id.dice_blue),
                findViewById(R.id.dice_green)
        };

        timer = new CountDownTimer(duration*1000,1000) {
            @Override
            public void onTick(long l) {
                ((TextView) findViewById(R.id.timeview)).setText(String.valueOf(count));
                count--;
            }

            @Override
            public void onFinish() {
                webSocketNextTurn();
            }
        };
        turnsSkipped = new int[4];


        players = getIntent().getExtras().getBooleanArray("players");
        names = getIntent().getStringArrayExtra("names");
        for(int i=0;i<4;i++)
            if(players[i]) ((TextView) findViewById(playenameview[i])).setText(names[i]);
            else findViewById(playenameview[i]).setVisibility(View.INVISIBLE);

        playernumber = getIntent().getIntExtra("playernumber",-1);

        Log.v("playernumber " , "playernumber  ="+playernumber);

        roomid = getIntent().getStringExtra("roomid");
        table_id = getIntent().getStringExtra("table_id");
//        room = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomid);
//        action = room.child("action");
        functions = FirebaseFunctions.getInstance();
        dice = findViewById(R.id.dice);

        if(playernumber != 0)
            dice.setVisibility(View.INVISIBLE);
        if(playernumber == 0)
            arrow.setVisibility(View.VISIBLE);
        showDice(0);

        dotview.post(new Runnable() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void run() {
                state = new Gamestate(dotview.positionHandler,getApplicationContext(),players){
                    public void onNextTurn() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDice(state.turn);
                                setPlayerName(state.turn);
                                if(state.turn == playernumber) dice.setVisibility(View.VISIBLE);
                                if(state.turn == playernumber) arrow.setVisibility(View.VISIBLE);
                                else dice.setVisibility(View.INVISIBLE);
                            }
                        },400);

                    }

                    public void onGameWon(int playerwon){
                        state.won = true;
//                        HashMap<String,String> map = new HashMap<>();
//                        map.put("playernumber","0");
//                        map.put("action_name","game_won");
//                        map.put("action_value",String.valueOf(playerwon));
//                        map.put("action_salt","0");
//                        action.setValue(map);
                        webSocketGameWin(playerwon);
                    }
                    public void resetTimer(){
                        OnlineGame_V2.this.restartTimer(tempVar);
                    }
                    public void onMovementFinished(){
                        dotview.setEnabled(false);

                        if(state.turn == playernumber){
                            arrow.setVisibility(View.VISIBLE);
                        }
                    }
                };

                restartTimer(tempVar);
                dicecontainer.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(),state.player[playernumber].colorResId));
                setPlayerName(0);
                state.initTurns();
                diceHandler = new DiceHandler(getApplicationContext(),dice,dices, diceSound){
                    public void onDiceResule(){
                        state.diceRolled();
                        restartTimer(tempVar);
                        dotview.setEnabled(true);

                    }
                };
                dotview.setGamestate(state);
                dotview.setOnTouchListener(new DotTouchListner(state.player,players,state){
                    public void touchDetected(int playernumber, int piecenumber){
                        if(diceHandler.rolled && state.turn == playernumber && count > 1) {

                            state.pieceClicked(playernumber, piecenumber);
//                            firebaseMove(piecenumber);
                         //   dotview.setEnabled(false);

                            webSocketMove(piecenumber);
                        }
                    }
                });
                state.setDiceHandler(diceHandler);
                // addListnersToFirebase();
            }
        });

        dicecontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!diceHandler.rolled && state.turn == playernumber && count > 1) {
                    resetTimerReduest();
                    diceHandler.diceAnimationInitial.start();
//                    firebaeRollDice();
                    webSocketRollDice();
                }
            }
        });
        ludoApiRepository.call_api_getStartGame().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {

                switch (stringResource.status)
                {
                    case SUCCESS:{
                        try {
                            JSONObject jsonObject = new JSONObject(stringResource.data);
                            int code = jsonObject.getInt("code");
                            if(code == 200)
                                game_id = jsonObject.getInt("game_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        gameStatus();
    }

    private void webSocketNextTurn() {
        Log.v("WebsocketCall" , "webSocketNextTurn");

        JSONObject map = new JSONObject();
        try {
            map.put("roomid",roomid);
            map.put("playernumber","-1");
            map.put("action_name","next_turn");
            map.put("action_value","-1");
            map.put("action_salt","-1");
            webSocket.send(String.valueOf(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void webSocketGameWin(int playerwon) {
        Log.v("WebsocketCall" , "webSocketGameWin");

        JSONObject map = new JSONObject();
        try {
            map.put("roomid",roomid);
            map.put("playernumber","0");
            map.put("action_name","game_won");
            map.put("action_value",String.valueOf(playerwon));
            map.put("action_salt","0");
            webSocket.send(String.valueOf(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }


    Timer timerstatus;
    int timertime = 6000;

    private void gameStatus() {
        timerstatus = new Timer();

        timerstatus.scheduleAtFixedRate(new TimerTask() {

                                            @Override
                                            public void run() {

                                                getGameStatusData();

                                            }

                                        },
                //Set how long before to start calling the TimerTask (in milliseconds)
                timertime,
                //Set the amount of time between each execution (in milliseconds)
                timertime);
    }

    @Override
    protected void onDestroy() {
        if(timer != null)
            timer.cancel();
        timerstatus.cancel();
        super.onDestroy();
    }

    String apponent_id;
    private void getGameStatusData() {
        Log.v("ApiCall " , "getGameStatusData");

        Functions.LOGD("OnlineGame","table_id : "+table_id);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ludoApiRepository.call_api_gameStatus(""+table_id,game_id).observe(OnlineGame_V2.this, new Observer<Resource<UserdataModelResponse>>() {
                    @Override
                    public void onChanged(Resource<UserdataModelResponse> userdataModelResponseResource) {
                        switch (userdataModelResponseResource.status)
                        {
                            case SUCCESS:
                            {
                                UserdataModelResponse userDatum = userdataModelResponseResource.data;
                                if(userDatum.getCode() == 200)
                                {
                                    if(userDatum.getTable_users().size() <= 1 && !gotReward)
                                    {
                                        Log.e("TAG_UserdataModelRes", "onResponse: "+userdataModelResponseResource.data );
                                        Log.e("TAG_UserdataModel", "onResponse: "+userdataModelResponseResource );
                                        gameWon(playernumber);
                                    }
                                }
                                else if(userDatum.getCode() == 407)
                                {
                                    if(!gotReward)
                                    {
                                        gameWon(playernumber);
                                    }
                                }
                                if(userDatum.getTable_users() != null)
                                {
                                    for (UserdataModelResponse.UserDatum userdata :userDatum.getTable_users()) {

                                        if(Functions.checkStringisValid(userdata.getId()) && !userdata.getId().equals(SharePref.getU_id()))
                                        {
                                            apponent_id = userdata.getId();
                                        }

                                    }
                                }

                            }
                        }
                    }
                });
            }
        });
    }

//    public void addListnersToFirebase(){ }

//    public void firebaeRollDice(){
//        arrow.setVisibility(View.INVISIBLE);
//        if(!diceSound.isPlaying())
//            diceSound.start();
//        HashMap<String,String> map = new HashMap<>();
//        map.put("playernumber",String.valueOf(playernumber));
//        map.put("roomid",roomid);
//        map.put("steps",String.valueOf(diceHandler.steps));
////        action.setValue(map);
//
//        functions.getHttpsCallable("rollDice").call(map).continueWith(new Continuation<HttpsCallableResult, String>() {
//            @Override public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
//                return (String) task.getResult().getData().toString();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override public void onComplete(@NonNull Task<String> task) {
//                int res = Integer.parseInt(task.getResult());
//                diceHandler.rollDice(res);
//                diceHandler.rolled = true;
//            }
//        });
//
//    }

    public void webSocketRollDice(){
        Log.v("WebsocketCall" , "webSocketRollDice");

        arrow.setVisibility(View.INVISIBLE);
        if(!diceSound.isPlaying())
            diceSound.start();
        JSONObject map = new JSONObject();
        try {
            map.put("roomid",roomid);
            map.put("playernumber",String.valueOf(playernumber));
            map.put("steps",String.valueOf(diceHandler.steps));

            webSocket.send(String.valueOf(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        int res = diceHandler.getRan();
        diceHandler.rollDice(res);
        diceHandler.rolled = true;

        JSONObject mapparams = new JSONObject();
        try {
            mapparams.put("roomid",roomid);
            mapparams.put("playernumber",String.valueOf(playernumber));
            mapparams.put("steps",String.valueOf(res));
            mapparams.put("action_name","roll_dice");
            mapparams.put("action_salt",String.valueOf(generateSalt()));

            webSocket.send(String.valueOf(mapparams));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        functions.getHttpsCallable("rollDice").call(map).continueWith(new Continuation<HttpsCallableResult, String>() {
//            @Override public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
//                return (String) task.getResult().getData().toString();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override public void onComplete(@NonNull Task<String> task) {
//
//
//            }
//        });
    }

//    public void firebaseMove(int piece){
//        HashMap<String,String> map = new HashMap<>();
//        map.put("playernumber",String.valueOf(playernumber));
//        map.put("action_name","move");
//        map.put("action_value",String.valueOf(piece));
//        map.put("action_salt",String.valueOf(generateSalt()));
////        action.setValue(map);
//    }

    public void webSocketMove(int piece){
        Log.v("WebsocketCall" , "webSocketMove");

        JSONObject map = new JSONObject();
        try {
            map.put("roomid",roomid);
            map.put("playernumber",String.valueOf(playernumber));
            map.put("action_name","move");
            map.put("action_value",String.valueOf(piece));
            map.put("action_salt",String.valueOf(generateSalt()));
            webSocket.send(String.valueOf(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public void firebaseGameRestartMove(){
//        HashMap<String,String> map = new HashMap<>();
//        map.put("playernumber",String.valueOf(playernumber));
//        map.put("action_name","restart");
//        map.put("action_value","restart");
//        map.put("action_salt",String.valueOf(generateSalt()));
////        action.setValue(map);
//    }

    public int generateSalt(){
        return diceHandler.random.nextInt(100000);
    }

    public void showDice(int num){
        if(num != playernumber)
            dices[num].setVisibility(View.VISIBLE);
        else
            dices[num].setVisibility(View.INVISIBLE);
        for(int i=0;i<dices.length;i++)
            if(i!=num)
                dices[i].setVisibility(View.INVISIBLE);
    }

    public void setPlayerName(int turn){
        turnnameview.setText(names[turn]+"'s Turn");
        playernamecontainer
                .setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(),state.player[turn].colorResId));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.alert_exit_game,null);
        final AlertDialog alert = new AlertDialog.Builder(OnlineGame_V2.this).setView(relativeLayout).create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        relativeLayout.findViewById(R.id.alert_exit_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ludoApiRepository.call_api_leaveTable();
                finish();
            }
        });
        relativeLayout.findViewById(R.id.alert_exit_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        if(Functions.isActivityExist(OnlineGame_V2.this))
            alert.show();
    }


    public void restartTimer(final int tempVar){
        dialog.dismiss();
        count = duration;
        try {
            timer.cancel();
        }catch (Exception e){}
        timer = new CountDownTimer(duration*2000,1000) {
            @Override
            public void onTick(long l) {
                ((TextView) findViewById(R.id.timeview)).setText(String.valueOf(count));
                if (count < 1) timerFinished(tempVar);
                else count--;
            }
            @Override public void onFinish() {

            }
        };
        if(!state.won)
            timer.start();
    }

    public void timerFinished(final int temp){

        Log.v("timerFinished " , "timerFinished");

        if(Functions.isActivityExist(this))
            //dialog.show();

            if(state.turn == playernumber) {
                dialog.dismiss();
                ludoApiRepository.call_api_leaveTable();
                finish();
            }
//        else
//            gameWon(state.turn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  dialog.dismiss();
                if(temp == tempVar) {
                    if(turnsSkipped[state.turn] >= 2){
                        players[state.turn] = false;
                    }

                    turnsSkipped[state.turn]++;
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("playernumber", "-1");
//                    map.put("action_name", "next_turn");
//                    map.put("action_value", String.valueOf(state.turn));
//                    map.put("action_salt", "-1");
//                    action.setValue(map);

                    webSocketTimeFinished();

                    try {
                        timer.cancel();
                    } catch (Exception e) {
                    }
                    if(state.checkWon() != -1 )
                        state.onGameWon(state.checkWon());
                    else if(state.singlePLayerLeft() != -1){
                        state.onGameWon(state.singlePLayerLeft());
                    }
                }

            }
        },2000);

    }

    private void webSocketTimeFinished() {
        Log.v("WebsocketCall" , "webSocketTimeFinished");

        JSONObject map = new JSONObject();
        try {
            map.put("roomid",roomid);
            map.put("playernumber", "-1");
            map.put("action_name", "next_turn");
            map.put("action_value", String.valueOf(state.turn));
            map.put("action_salt", "-1");
            webSocket.send(String.valueOf(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void gameWon(final int player){
        Log.v("FirebaseFunction " , "FirebaseFunctionOWN"+player);

        state.won = true;
        if(Functions.isActivityExist(OnlineGame_V2.this))
            // dialog.show();
            if(!gotReward) {
                gotReward = true;
                ludoApiRepository.call_api_getMakeWinner(SharePref.getU_id(),table_id);

                if (player == playernumber) {
//This is for creation table win
                    Log.v("FirebaseFunction " , "FirebaseFunctionOWN");

                    gotReward = true;

                    LinearLayout linerlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_online_game_won, null);
                    final AlertDialog alert = new AlertDialog.Builder(OnlineGame_V2.this).setView(linerlayout).create();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ((TextView)linerlayout.findViewById(R.id.gamewon_playername)).setText(names[player]+" Won");
                    linerlayout.findViewById(R.id.game_won_trophy)
                            .setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(),state.player[player].colorResId));
                    linerlayout.findViewById(R.id.claim_diamonds_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                            finish();

//                        LinearLayout linerlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_for_shop, null);
//                        AlertDialog wondiag = new AlertDialog.Builder(OnlineGame_V2.this).setView(linerlayout).create();
//                        wondiag.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        int num = state.numberOfPLayers;
////                        int reward = (int) ((float) diamonds * (num - 1) * 0.6);
//                        ((TextView) linerlayout.findViewById(R.id.alert_for_shop_message_view))
//                                .setText("You won " /*+ reward + " Diamonds"*/);
//                        linerlayout.findViewById(R.id.shop_alert_okay_button).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                finish();
//                            }
//                        });
//                        // if(Functions.isActivityExist(OnlineGame_V2.this))
//                        //wondiag.show();
                        }
                    });
                    alert.setCancelable(true);
                    alert.setCanceledOnTouchOutside(true);
                    if(Functions.isActivityExist(OnlineGame_V2.this))
                        alert.show();


//                HashMap<String, String> map = new HashMap<>();
//                map.put("roomid", roomid);
//                map.put("diamonds", String.valueOf(diamonds));
//                map.put("nop", String.valueOf(state.numberOfPLayers));
//                map.put("name", CommonUtils.getName(getApplicationContext()));
//                FirebaseFunctions.getInstance().getHttpsCallable("claimDiamonds").call(map).continueWith(new Continuation<HttpsCallableResult, String>() {
//                    @Override
//                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
//                        return task.getResult().getData().toString();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//
//                        dialog.dismiss();
//                        LinearLayout linerlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_online_game_won, null);
//                        final AlertDialog alert = new AlertDialog.Builder(OnlineGame_V2.this).setView(linerlayout).create();
//                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        ((TextView)linerlayout.findViewById(R.id.gamewon_playername)).setText(names[player]+" Won");
//                        linerlayout.findViewById(R.id.game_won_trophy)
//                                .setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(),state.player[player].colorResId));
//                        linerlayout.findViewById(R.id.claim_diamonds_button).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                LinearLayout linerlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_for_shop, null);
//                                AlertDialog wondiag = new AlertDialog.Builder(OnlineGame_V2.this).setView(linerlayout).create();
//                                wondiag.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                int num = state.numberOfPLayers;
//                                int reward = (int) ((float) diamonds * (num - 1) * 0.6);
//                                ((TextView) linerlayout.findViewById(R.id.alert_for_shop_message_view))
//                                        .setText("You won " /*+ reward + " Diamonds"*/);
//                                linerlayout.findViewById(R.id.shop_alert_okay_button).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        finish();
//                                    }
//                                });
//                               // if(Functions.isActivityExist(OnlineGame_V2.this))
//                                    //wondiag.show();
//                            }
//                        });
//                        alert.setCancelable(false);
//                        alert.setCanceledOnTouchOutside(false);
//                        if(Functions.isActivityExist(OnlineGame_V2.this))
//                            alert.show();
//                    }
//                });

                } else {
                    Log.v("FirebaseFunction " , "FirebaseFunctionOther");

                    LinearLayout linerlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_online_game_lost, null);
                    AlertDialog alert = new AlertDialog.Builder(OnlineGame_V2.this).setView(linerlayout).create();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ((TextView)linerlayout.findViewById(R.id.gamewon_playername)).setText(names[player]+" Won");
                    linerlayout.findViewById(R.id.game_won_trophy)
                            .setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(),state.player[player].colorResId));
                    linerlayout.findViewById(R.id.exitview).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                            finish();
                        }
                    });
                    alert.setCanceledOnTouchOutside(false);
                    alert.setCancelable(false);
                    if(Functions.isActivityExist(OnlineGame_V2.this))
                        alert.show();
                    // finish();

                }
            }
    }

    public void resetTimerReduest(){
//        room.child("resettimer").setValue(String.valueOf(generateSalt()));
        webSocketResetTimer();
    }

    private void webSocketResetTimer() {
        Log.v("WebsocketCall" , "webSocketResetTimer");

        JSONObject map = new JSONObject();
        try {
            map.put("roomid",roomid);
            map.put("resettimer", "resettimer");
            map.put("resettimerSalt", String.valueOf(generateSalt()));
            webSocket.send(String.valueOf(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                Toast.makeText(OnlineGame_V2.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();
//                initializeView();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    Log.v("SocketListener " , "SocketListener"+text.toString());

                    JSONObject jsonObject = new JSONObject(text);
                    Functions.LOGD("WebSocket","response : "+jsonObject);
                    String gameroomid = jsonObject.optString("roomid","");
                    String gameplayernumber = jsonObject.optString("playernumber","");
                    String steps = jsonObject.optString("steps");
                    String action_name = jsonObject.optString("action_name","");
                    String action_value = jsonObject.optString("action_value","");
                    String resettimer = jsonObject.optString("resettimer","");

                    if(gameroomid.equalsIgnoreCase(roomid))
                    {
                        if(Functions.checkStringisValid(action_name))
                        {
                            if (!gameplayernumber.equals(String.valueOf(playernumber)))
                            {
                                if (action_name.equalsIgnoreCase("roll_dice") && !state.won) {
                                    int val = Integer.parseInt(steps);
                                    if (val >= 1 && val <= 6) {
                                        diceHandler.rollDice(val);
                                    }
                                    turnsSkipped[state.turn] = 0;
                                }
                                else if (action_name.equals("move") && !state.won) {
                                    int piece = Integer.parseInt(action_value);
                                    state.player[state.turn].move(piece, diceHandler.steps);
                                    turnsSkipped[state.turn] = 0;
                                }
                                else if (action_name.equals("restart")) {
                                    state.startNewGame();
                                    try {
                                        dialogue.dismiss();
                                    }catch (Exception e){}
                                }
                                else if(action_name.equals("next_turn") && !state.won){
                                    state.nextTurn();
                                }
                            }

                            if(action_name.equals("game_won")){
                                int player = Integer.parseInt(action_value);
                                gameWon(player);
                            }
                            tempVar++;
                            if(!state.won)
                                restartTimer(tempVar);
                        }
                        else if(Functions.checkisStringValid(resettimer) && resettimer.equals("resettimer"))
                        {
                            restartTimer(tempVar);
                        }
                    }

                } catch (JSONException e) {
                    Functions.LOGD("OnlineGame",""+e.getLocalizedMessage());
                    e.printStackTrace();
                }

            });

        }
    }

}