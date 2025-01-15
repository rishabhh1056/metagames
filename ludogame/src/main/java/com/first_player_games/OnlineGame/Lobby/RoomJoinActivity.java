package com.first_player_games.OnlineGame.Lobby;

import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first_player_games.Api.LudoApiRepository;
import com.first_player_games.Api.Resource;
import com.first_player_games.BaseActivity;
import com.first_player_games.Callback;
import com.first_player_games.ModelResponse.UserdataModelResponse;
import com.first_player_games.OnlineGame.OnlineGame_V2.OnlineGame_V2;
import com.first_player_games.Others.CommonUtils;
import com.first_player_games.Others.Functions;
import com.first_player_games.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RoomJoinActivity extends BaseActivity {

    String roomid,table_id;
    RoomFunctions roomFunctions;
    String name;

    private List<String[]> playersList;
    private int[][] views = new int[][]{
            {R.id.roomcreation_player_1,R.id.roomcreation_player_name_1},
            {R.id.roomcreation_player_2,R.id.roomcreation_player_name_2},
            {R.id.roomcreation_player_3,R.id.roomcreation_player_name_3},
            {R.id.roomcreation_player_4,R.id.roomcreation_player_name_4}
    };

    int playernumber = 2;
    private int diamonds = -1;
    LudoApiRepository ludoApiRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_join);

        ludoApiRepository = LudoApiRepository.getInstance().init(getApplicationContext());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        rotation_animation(((ImageView) findViewById(R.id.img_dice)),R.anim.rotationback_animation);

        roomid = getIntent().getStringExtra("roomid");

        LudoApiRepository.getInstance().call_api_getJoinTable(roomid).observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                switch (stringResource.status)
                {
                    case SUCCESS:
                    {
                        table_id = stringResource.data;
                        gameStatus();
                    }
                }
            }
        });

        ((TextView) findViewById(R.id.roomcreationroomid)).setText(roomid);
        diamonds = getIntent().getIntExtra("diamonds",-1);
        if(diamonds > 0) {
            ((TextView) findViewById(R.id.diamonds_amount_view)).setText(diamonds + " diamonds game");
            findViewById(R.id.diamonds_amount_view).setVisibility(View.VISIBLE);
        }
        name = CommonUtils.getName(getApplicationContext());
        playersList = new ArrayList<>();
        roomFunctions = new RoomFunctions(){
            public void roomJoined(int number){
                playernumber = number;
            }
        };
//        roomFunctions.joinRoom(name,roomid);
        addListenrsToFirebase();
    }

    public void addListenrsToFirebase(){
        new LobbyFirebaseListeners(roomid){
            public void playerJoined(String[] data){
                playersList.add(data);
                updateUI();
            }
            public void gameStarted(boolean[] players,String[] names){
                Intent intent = new Intent(RoomJoinActivity.this, OnlineGame_V2.class);
                intent.putExtra("roomid",roomid);
                intent.putExtra("table_id",table_id);
                intent.putExtra("playernumber",playernumber);
                intent.putExtra("players",players);
                intent.putExtra("names",names);
                intent.putExtra("diamonds",diamonds);
                startActivity(intent);
                finish();
            }
        };
    }

    public void updateUI(){
        for(int i=0;i<4;i++){
            if(i<playersList.size()) {
                findViewById(views[i][0]).setVisibility(View.VISIBLE);
                ((TextView) findViewById(views[i][1])).setText(playersList.get(i)[1]);
            }
            else
                findViewById(views[i][0]).setVisibility(View.INVISIBLE);
        }

    }

    Timer timerstatus;
    int timertime = 6 * 1000;
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

    private void getGameStatusData() {
        Functions.LOGD("OnlineGame","table_id : "+table_id);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ludoApiRepository.call_api_gameStatus(""+table_id,-1).observe(RoomJoinActivity.this, new Observer<Resource<UserdataModelResponse>>() {
                    @Override
                    public void onChanged(Resource<UserdataModelResponse> userdataModelResponseResource) {
                        switch (userdataModelResponseResource.status)
                        {
                            case SUCCESS:
                            {
                                UserdataModelResponse userDatum = userdataModelResponseResource.data;
                                if(userDatum.getCode() == 200)
                                {

                                    playersList.clear();

                                    for (int i = 0; i < userDatum.getTable_users().size(); i++) {
                                        UserdataModelResponse.UserDatum userData = userDatum.getTable_users().get(i);

                                        String[] username = new String[2];
                                        username[0] = ""+(i != 0 ? 2 : 0);
                                        username[1] = userData.getName();

                                        playersList.add(username);
                                    }
                                    updateUI();
                                    gameStartButton();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public void gameStartButton(){

        if(playersList.size() > 1)
        {
            timerstatus.cancel();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    boolean[] players= {true, false, true, false};
                    String[] names = new String[4];
                    int position = 0;
                    for (int i = 0; i < playersList.size(); i++) {
//                        names[i] = null;
                        names[position+i] = playersList.get(i)[1];
                        position++;
                    }

                    Intent intent = new Intent(RoomJoinActivity.this, OnlineGame_V2.class);
                    intent.putExtra("roomid",roomid);
                    intent.putExtra("table_id",table_id);
                    intent.putExtra("playernumber",playernumber);
                    intent.putExtra("players",players);
                    intent.putExtra("names",names);
                    intent.putExtra("diamonds", diamonds);
                    startActivity(intent);
                    finish();

//                    functions.startGame(roomid);
                }
            },2000);
        }
        else {
            Toast.makeText(this, "Please Wait for Others User.", Toast.LENGTH_SHORT).show();
        }


    }

    private void rotation_animation(View view, int animation) {

        Animation circle = Functions.AnimationListner(this, animation, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

            }
        });
        view.setAnimation(circle);
        circle.startNow();

    }

    @Override
    protected void onDestroy() {
        timerstatus.cancel();
        super.onDestroy();
    }
}