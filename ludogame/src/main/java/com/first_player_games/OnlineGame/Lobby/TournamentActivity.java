package com.first_player_games.OnlineGame.Lobby;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.first_player_games.Api.LudoApiRepository;
import com.first_player_games.Api.Resource;
import com.first_player_games.BaseActivity;
import com.first_player_games.OnlineGame.OnlineGame_V2.OnlineGame_V2;
import com.first_player_games.Others.CommonUtils;
import com.first_player_games.Others.Functions;
import com.first_player_games.Others.ProgressPleaseWait;
import com.first_player_games.R;
import com.first_player_games.ludoApi.model.LudoViewModel;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.List;

public class TournamentActivity extends BaseActivity {

    FirebaseFunctions functions;
    ValueEventListener roolIdListener;
    DatabaseReference database;
    String roomid;
    RoomFunctions roomFunctions;
    int playernumber;
    int timeremaining = 10;
    String name;
    ProgressDialog dialog;
    int gameSize;
    private List<String[]> playersList;
    private int[][] views = new int[][]{
            {R.id.roomcreation_player_1,R.id.roomcreation_player_name_1},
            {R.id.roomcreation_player_2,R.id.roomcreation_player_name_2},
            {R.id.roomcreation_player_3,R.id.roomcreation_player_name_3},
            {R.id.roomcreation_player_4,R.id.roomcreation_player_name_4}
    };
    String timestamp;
    CountDownTimer timer;
    boolean gotRoomid = false,shouldjoin = true;
    int diamonds = -1;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        gameSize = getIntent().getIntExtra("gamesize",2);
        name = CommonUtils.getName(getApplicationContext());
        playersList = new ArrayList<>();
        functions = FirebaseFunctions.getInstance();
        dialog = ProgressPleaseWait.getDialogue(this);
        database = FirebaseDatabase.getInstance().getReference();
        roolIdListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String roomid = snapshot.getValue().toString();
                    gotRoomid(roomid);
                    database.child("roomsignal").removeEventListener(roolIdListener);
                    database.child("roomsignal").child(FirebaseAuth.getInstance().getUid()).removeValue();
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        };

        functions.getHttpsCallable("joinQueue").call(gameSize).continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                return (String) task.getResult().getData().toString();
            }
        }).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    timestamp = task.getResult();
                }
                else {
                    finish();
                }
            }
        });
        database.child("roomsignal").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(roolIdListener);

        timer = new CountDownTimer(30000,15000) {
            @Override
            public void onTick(long l) { }

            @Override public void onFinish() {
                if(!gotRoomid && timestamp != null)
                if(timestamp.length()>0) {
                    shouldjoin = false;
                    database.child("tournaments").child(String.valueOf(gameSize)).child(timestamp).removeValue();
                    LinearLayout relativeLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_game_not_found,null);
                    AlertDialog alert = new AlertDialog.Builder(TournamentActivity.this).setView(relativeLayout).create();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    relativeLayout.findViewById(R.id.room_not_found_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    alert.show();
                }
            }
        }.start();
    }

    String table_id;
    private void getTable(){
        LudoViewModel.getInstance().loadgetTable(getIntent().getStringExtra("boot_value"),TournamentActivity.this.roomid);
        LudoViewModel.getInstance().getTableData().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                switch (stringResource.status)
                {
                    case SUCCESS:
                        table_id = stringResource.data;
                }
            }
        });
    }

    private void join_table(){
        LudoApiRepository.getInstance().call_api_getJoinTable(roomid).observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                switch (stringResource.status)
                {
                    case SUCCESS:
                        table_id = stringResource.data;
                }
            }
        });
    }


    public void gotRoomid(final String roomid){
        gotRoomid = true;
        this.roomid = roomid;
//        if(playersList.size() <= 0)
//            getTable();
//        else
//            join_table();

        ((TextView) findViewById(R.id.roomcreationroomid)).setText(roomid);
        roomFunctions = new RoomFunctions(){
            public void roomJoined(int number){
                playernumber = number;
            }
        };
        database.child("rooms").child(roomid).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.getValue().toString().equals("created") && shouldjoin) {
                        roomFunctions.joinRoom(name,roomid);
                        database.child("rooms").child(roomid).child("status").removeEventListener(this);
                    }
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        addListenrsToFirebase();
    }

    public void addListenrsToFirebase(){
        new LobbyFirebaseListeners(roomid){
            public void playerJoined(String[] data){
                playersList.add(data);
                updateUI();
                if(playersList.size() == gameSize)
                    startTimer();
            }
            public void gameStarted(boolean[] players,String[] names){
                dialog.dismiss();
                Intent intent = new Intent(TournamentActivity.this, OnlineGame_V2.class);
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

    public void startTimer(){
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                ((TextView) findViewById(R.id.tournamentTimer)).setText("Starting Game in "+timeremaining);
                timeremaining--;
            }

            @Override
            public void onFinish() {
                if(Functions.isActivityExist(context))
                    dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        roomFunctions.startGame(roomid);
                    }
                },2000);
            }
        }.start();
    }


    @Override
    public void onBackPressed() {
        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.alert_exit_game,null);
        final androidx.appcompat.app.AlertDialog alert = new androidx.appcompat.app.AlertDialog.Builder(TournamentActivity.this).setView(relativeLayout).create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        relativeLayout.findViewById(R.id.alert_exit_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        relativeLayout.findViewById(R.id.alert_exit_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}