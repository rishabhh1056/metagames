package com.first_player_games.OnlineGame.Lobby;

import static com.first_player_games.Api.EndPoints.SERVER_PATH;

import androidx.lifecycle.Observer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.first_player_games.Others.ProgressPleaseWait;
import com.first_player_games.Others.SharePref;
import com.first_player_games.R;
import com.first_player_games.RandomString;
import com.first_player_games.ludoApi.model.LudoViewModel;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class RoomCreationActivity extends BaseActivity {

    private WebSocket webSocket;
    private RoomFunctions functions;
    private String name;
    private int playernumber;

    private DatabaseReference room;
    private List<String[]> playersList;
    private int[][] views = new int[][]{
            {R.id.roomcreation_player_1,R.id.roomcreation_player_name_1},
            {R.id.roomcreation_player_2,R.id.roomcreation_player_name_2},
            {R.id.roomcreation_player_3,R.id.roomcreation_player_name_3},
            {R.id.roomcreation_player_4,R.id.roomcreation_player_name_4}
    };
    private String roomid;
    private ProgressDialog dialog;

    private int diamonds = -1;
    private String TAG = "RoomCreationActivity";
    LudoApiRepository ludoApiRepository;
    String[] namesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_creation);
        Log.v("ActivityName " , "RoomCreationActivity");

        ludoApiRepository = LudoApiRepository.getInstance().init(getApplicationContext());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        rotation_animation(((ImageView) findViewById(R.id.img_dice)),R.anim.rotationback_animation);


        diamonds = getIntent().getIntExtra("diamonds",-1);
        if(diamonds > 0) {
            ((TextView) findViewById(R.id.diamonds_amount_view)).setText(diamonds + " diamonds game");
            findViewById(R.id.diamonds_amount_view).setVisibility(View.VISIBLE);
        }
        dialog = ProgressPleaseWait.getDialogue(this);
//        dialog.show();
        name = CommonUtils.getName(getApplicationContext());
        playersList = new ArrayList<>();

        RandomString randomString = new RandomString();
//        functions = new RoomFunctions(){
//            public void roomCreated(String roomid){
//                Log.v("Function Name " , "roomCreated");
//
//                dialog.dismiss();
//                RoomCreationActivity.this.roomid = roomid;
//                ((TextView) findViewById(R.id.roomcreationroomid)).setText(roomid);
//                //joinRoom(name,roomid);
//              //  addListenrsToFirebase();
//                getTable();
//            }
//
//            public void roomJoined(int number){
//                playernumber = number;
//            }
//        };
//        functions.createRoom(diamonds);
        String randoRoomid = randomString.nextString();
        if(randoRoomid.length() > 0)
        {
            RoomCreationActivity.this.roomid = randoRoomid;
            getTable();
        }
    }

    @Override
    public void onBackPressed() {
        removeFromRooms();
        super.onBackPressed();
    }

    private void removeFromRooms() {
        LudoApiRepository.getInstance().call_api_leaveTable();
    }

    public void addListenrsToFirebase(){
        new LobbyFirebaseListeners(roomid){
            public void playerJoined(String[] data){
                playersList.add(data);
                updateUI();

                for (String names: data) {
                    Functions.LOGD("RoommCreationActivity","names : "+names);
                }

                if(playersList.size() >= 2)
                {
                    gameStartButton(null);
                }

            }
            public void gameStarted(boolean[] players,String[] names){
                dialog.dismiss();
                Intent intent = new Intent(RoomCreationActivity.this, OnlineGame_V2.class);
                intent.putExtra("roomid",roomid);
                intent.putExtra("table_id",table_id);
                intent.putExtra("playernumber",playernumber);
                intent.putExtra("players",players);
                intent.putExtra("names",names);
                intent.putExtra("diamonds", diamonds);
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
            else {
                findViewById(views[i][0]).setVisibility(View.INVISIBLE);
            }
        }
    }

    String table_id;
    private void getTable(){
        Log.v("ApiCall " , "getTable");


        LudoViewModel.getInstance().loadgetTable(getIntent().getStringExtra("boot_value"),RoomCreationActivity.this.roomid);
        LudoViewModel.getInstance().getTableData().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                switch (stringResource.status)
                {
                    case SUCCESS:
                    {
                        table_id = stringResource.data;
                        Log.v("ApiCall " , "gameStatus");

                        gameStatus();
                    }
                }
            }
        });
    }

    public void gameStartButton(View view){

        if(playersList.size() > 1)
        {
            timerstatus.cancel();
            dialog.show();
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
                    dialog.dismiss();
                    Intent intent = new Intent(RoomCreationActivity.this, OnlineGame_V2.class);
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
        Log.v("ApiCall " , "getGameStatusData");

        Functions.LOGD("OnlineGame","table_id : "+table_id);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ludoApiRepository.call_api_gameStatus(""+table_id,-1).observe(RoomCreationActivity.this, new Observer<Resource<UserdataModelResponse>>() {
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
                                    if(playersList.size() >= 2)
                                    {
                                        gameStartButton(null);
                                    }

                                }
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        timerstatus.cancel();
        super.onDestroy();
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

}