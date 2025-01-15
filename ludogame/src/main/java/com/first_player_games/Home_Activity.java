package com.first_player_games;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first_player_games.Api.LudoApiRepository;
import com.first_player_games.Api.Resource;
import com.first_player_games.Api.RetrofitClient;
import com.first_player_games.LocalGame.LocalGame;
import com.first_player_games.LocalGame.VsComputer;
import com.first_player_games.Menu.ComputerGameMenu;
import com.first_player_games.Menu.CreateRoomMenu;
import com.first_player_games.Menu.DiamondsRoomJoinMenu;
import com.first_player_games.Menu.Join_Room_menu;
import com.first_player_games.Menu.LocalGameMenu;
import com.first_player_games.Menu.PlayOnlineMenu;
import com.first_player_games.Menu.ReferIdMenu;
import com.first_player_games.Menu.TournamentOptionsMenu;
import com.first_player_games.ModelResponse.UserdataModelResponse;
import com.first_player_games.OnlineGame.Lobby.RoomCreationActivity;
import com.first_player_games.OnlineGame.Lobby.RoomJoinActivity;
import com.first_player_games.OnlineGame.Lobby.TournamentActivity;
import com.first_player_games.Others.CommonUtils;
import com.first_player_games.Others.Functions;
import com.first_player_games.Others.ProgressPleaseWait;
import com.first_player_games.Others.SharePref;
import com.first_player_games.ludoApi.TableMaster;
import com.first_player_games.ludoApi.bottomFragment.LudoActiveTables_BF;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Activity extends BaseActivity {

    Context context = this;
    LocalGameMenu localgamemenu;
    ComputerGameMenu computergamemenu;
    PlayOnlineMenu playOnlineMenu;
    Join_Room_menu join_room_menu;
    TournamentOptionsMenu tournamentOptionsMenu;
    CreateRoomMenu createRoomMenu;
    DiamondsRoomJoinMenu diamondsRoomJoinMenu;
    ReferIdMenu referIdMenu;

    MediaPlayer buttonEffect;
    MediaPlayer musicplayer;

    TextView updatesView;
    List<String[]> data;
    DatabaseReference db;
    ProgressDialog dialog;
    int tick;

    Animation animBounce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        SharePref.getInstance().init(this);

        animBounce = AnimationUtils.loadAnimation(this,
                R.anim.bounce);
        animBounce.setAnimationListener(new Animation.AnimationListener() {
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
       final ImageView imageView2 =findViewById(R.id.imageView2);
        imageView2.startAnimation(animBounce);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView2.startAnimation(animBounce);
            }
        });
        lnrludooption = findViewById(R.id.lnrludooption);

//        ((TextView) findViewById(R.id.nameview)).setText(CommonUtils.getName(getApplicationContext()));
        buttonEffect = MediaPlayer.create(Home_Activity.this,R.raw.effect_button_click);
        musicplayer = MediaPlayer.create(Home_Activity.this,R.raw.game_music);
        dialog = ProgressPleaseWait.getDialogue(this);
        db = FirebaseDatabase.getInstance().getReference();
        localgamemenu = new LocalGameMenu(this){
            public void onGameStartAction(boolean[] players){
                playButtonSound();
                localgamemenu.dismiss();
                Intent intent = new Intent(Home_Activity.this, LocalGame.class);
                intent.putExtra("players",players);
                startActivity(intent);
            }
        };

        computergamemenu = new ComputerGameMenu(this){
            public void onGameStartAction(int[] players){
                playButtonSound();
                computergamemenu.dismiss();
                Intent intent = new Intent(Home_Activity.this, VsComputer.class);
                intent.putExtra("players",players);
                startActivity(intent);
            }
        };

        playOnlineMenu = new PlayOnlineMenu(this){
            public void createRoom(){
                playButtonSound();
                playOnlineMenu.dismiss();
                createRoomMenu.show();
            }
            public void joinRoom(){
                playButtonSound();
                playOnlineMenu.dismiss();
                join_room_menu.showMenu();
            }
        };

        join_room_menu = new Join_Room_menu(this){
            public void roomFound(String roomid){
                Intent intent = new Intent(Home_Activity.this, RoomJoinActivity.class);
                intent.putExtra("roomid",roomid);
                startActivity(intent);
            }

            public void diamondsRoomFound(String roomid){
                join_room_menu.dismiss();
                diamondsRoomJoinMenu.show();
                diamondsRoomJoinMenu.setRoomid(roomid);
            }
        };

        tournamentOptionsMenu = new TournamentOptionsMenu(this){
            public void findGame(int players){
                Intent intent = new Intent(Home_Activity.this, TournamentActivity.class);
                intent.putExtra("gamesize",players);
                startActivity(intent);

            }
        };

        createRoomMenu = new CreateRoomMenu(this){
            public void craeteFreeRoom(){
                createRoomMenu.dismiss();
                startActivity(new Intent(Home_Activity.this, RoomCreationActivity.class));
            }
            public void craetePaidRoom(int diamonds){
                createRoomMenu.dismiss();
                Intent intent = new Intent(Home_Activity.this, RoomCreationActivity.class);
                intent.putExtra("diamonds",diamonds);
                startActivity(intent);
            }
        };

        diamondsRoomJoinMenu = new DiamondsRoomJoinMenu(this){
            public void canJoinGame(String roomid,int diamonds){
                diamondsRoomJoinMenu.dismiss();
                Intent intent = new Intent(Home_Activity.this, RoomJoinActivity.class);
                intent.putExtra("roomid",roomid);
                intent.putExtra("diamonds",diamonds);
                startActivity(intent);
            }
        };

        referIdMenu = new ReferIdMenu(this){
            public void onShareButtonPressed(String referid){
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "Here is my Refer ID \n\n"+referid;
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share Refer Id"));
            }
        };


        updatesView = findViewById(R.id.updatesView);
        startAnimation();


        if(CommonUtils.getSoundSetting(getApplicationContext()))
            musicplayer.start();

        updateDiamonds();
    }

    public void menuButtonLocalGame(View view){
        localgamemenu.showMenu();
        playButtonSound();
    }

    public void menuButtonVsComputer(View view){
        computergamemenu.showMenu();
        playButtonSound();
    }

    public void menuButtonPlayOnline(View view) {
        LudoActiveTables_BF ludoActiveTables_bf = new LudoActiveTables_BF();
        ludoActiveTables_bf.setCallback(new ClassCallback() {
            @Override
            public void Response(View v, int position, Object object) {
                if(object instanceof TableMaster.TableDatum)
                {
                    TableMaster.TableDatum tableDatum = (TableMaster.TableDatum) object;

                    if(!Functions.checkStringisValid(tableDatum.getInvite_code()) && !tableDatum.getOnlineMembers().equals("1"))
                    {
                        // Create Free Room
                        startActivity(new Intent(Home_Activity.this, RoomCreationActivity.class)
                                .putExtra("diamonds",0).putExtra("boot_value",tableDatum.getBootValue()));
                    }
                    else {
                        String roomid = tableDatum.getInvite_code();
                        Intent intent = new Intent(Home_Activity.this, RoomJoinActivity.class);
                        intent.putExtra("roomid",roomid);
                        startActivity(intent);
                    }

                }

            }
        });
        ludoActiveTables_bf.show(getSupportFragmentManager(),"lus");

        playButtonSound();
    }

    public void payoutButton(View view){

    }

    public void menuTournamentsButton(View view){
        playButtonSound();
        dialog.show();
        tournamentOptionsMenu.showMenu();

    }

    public void buyDiamondDice(View view){

    }

    public void settingsButton(View view){

    }

    public void shareApp(View view){

    }

    public void playButtonSound(){
        if(CommonUtils.getSoundSetting(getApplicationContext()))
            buttonEffect.start();
    }





    @Override
    protected void onStart() {
        super.onStart();
        if(CommonUtils.getSoundSetting(getApplicationContext()))
            if(!musicplayer.isPlaying())
                musicplayer.start();
    }

    LiveData<Resource<List<UserdataModelResponse>>> userDataObserver;

    @Override
    protected void onResume() {
        super.onResume();
//        ((TextView) findViewById(R.id.nameview)).setText(CommonUtils.getName(getApplicationContext()));
        if(CommonUtils.getSoundSetting(getApplicationContext()))
            if(!musicplayer.isPlaying())
                musicplayer.start();


            UserDataobserver();
            UserLudoLeaveTable();
    }

    private void UserLudoLeaveTable() {
        LudoApiRepository.getInstance().call_api_leaveTable();
    }

    private void UserDataobserver() {
        userDataObserver =  LudoApiRepository.getInstance().getUserData();
        userDataObserver.observe(this, new Observer<Resource<List<UserdataModelResponse>>>() {
            @Override
            public void onChanged(Resource<List<UserdataModelResponse>> listResource) {
                switch (listResource.status) {
                    case ERROR:
                        Functions.showToast(context,listResource.message);
                    break ;
                    case SUCCESS:

                        ((TextView) findViewById(R.id.diamonds_available_view)).setText(SharePref.getInstance().getString(SharePref.wallet));
                        ((TextView) findViewById(R.id.nameview)).setText(SharePref.getInstance().getString(SharePref.u_name));

                        break;
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        userDataObserver.removeObservers(this);
    }

    public void updateDiamonds(){

    }

    @Override
    protected void onPause() {
        super.onPause();
        musicplayer.pause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startAnimation(){
        data = new ArrayList<>();
        db.child("updates").orderByKey().limitToLast(4).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    String[] a = new String[]{
                            snapshot.child("name").getValue().toString(),
                            snapshot.child("diamonds").getValue().toString()
                    };
                    data.add(a);
                }catch (Exception e){}
            }
            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
        sliderAnimations();
    }

    public void sliderAnimations(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(data.size()>0) {
                    Animation out = AnimationUtils.loadAnimation(Home_Activity.this, R.anim.text_outside_slider_anim);
//                    updatesView.startAnimation(out);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String[] a = data.get(tick);
                            updatesView.setText(a[0]+" won "+a[1]+" Diamonds Recently");
                            Animation in = AnimationUtils.loadAnimation(Home_Activity.this, R.anim.text_inside_slider_anim);
//                            updatesView.startAnimation(in);
                            tick = (tick + 1)%data.size();
                        }
                    },1000);

                }
                sliderAnimations();
            }
        },10000);
    }


    View lnrludooption;
    public void menuLudoMenu(View view) {
        lnrludooption.setVisibility(View.VISIBLE);
    }

    public void menuLudoParentClick(View view){
        lnrludooption.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(lnrludooption.getVisibility() == View.VISIBLE)
        {
            lnrludooption.setVisibility(View.GONE);
        }
        else
            super.onBackPressed();
    }
}