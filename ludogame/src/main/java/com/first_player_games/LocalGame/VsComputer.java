package com.first_player_games.LocalGame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.first_player_games.BaseActivity;
import com.first_player_games.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class VsComputer extends BaseActivity {


    DotsView dotview;
    Gamestate state;

    LinearLayout dicecontainer;
    ImageView dice;
    DiceHandler diceHandler;

    EditText cheatview;

    int[] playersIdentity;
    boolean[] allowedPlayers;
    ImageView arrowView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs_computer);

        if(getIntent().hasExtra("players")){
            playersIdentity = getIntent().getExtras().getIntArray("players");
            allowedPlayers = getAllowedPlayers(playersIdentity);
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interetitianl_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        dotview = findViewById(R.id.ludodotview);
        dicecontainer = findViewById(R.id.dicecontainer);
        cheatview = findViewById(R.id.cheatview);
        dice = findViewById(R.id.dice);
        arrowView = findViewById(R.id.arrow_view);

        dotview.post(new Runnable() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void run() {
                state = new Gamestate(dotview.positionHandler,getApplicationContext(),allowedPlayers){
                    public void onNextTurn() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(state.isComputer())
                                    makeComputerRollDice();
                                else
                                    arrowView.setVisibility(View.VISIBLE);
                                dicecontainer
                                        .setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(),state.player[state.turn].colorResId));


                            }
                        },400);

                    }
                    public void onGameWon(int playernumber){
                        AlertDialog.Builder builder = new AlertDialog.Builder(VsComputer.this,R.style.CustomDialog);
                        LinearLayout linerlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_local_game_won,null);
                        builder.setView(linerlayout);

                        if(playernumber == 0){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_red));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Red Won");
                            //finish();
                        }
                        else if(playernumber == 1){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_yello));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Yellow Won");
                            //finish();
                        }
                        else if(playernumber == 2){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_blue));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Blue Won");
                            //finish();
                        }
                        else if(playernumber == 3){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_green));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Green Won");
                            //finish();
                        }
                        final AlertDialog dialogue = builder.create();
                        dialogue.show();
                        linerlayout.findViewById(R.id.localgamewonrestart).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mInterstitialAd.isLoaded())
                                    mInterstitialAd.show();
                                state.startNewGame();
                                dialogue.dismiss();
                                finish();
                            }
                        });
                        linerlayout.findViewById(R.id.localgamewoncancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mInterstitialAd.isLoaded())
                                    mInterstitialAd.show();
                                dialogue.dismiss();
                                finish();
                            }
                        });
                    }
                    public void onMovementFinished(){
                        if(state.isComputer())
                            makeComputerRollDice();
                        else
                            arrowView.setVisibility(View.VISIBLE);

                    }
                };
                state.setPlayersIdentity(playersIdentity);
                state.initTurns();
                diceHandler = new DiceHandler(getApplicationContext(),new ImageView[]{dice}, MediaPlayer.create(VsComputer.this,R.raw.dice_rolling_effect)){
                    public void onDiceResule(){
                        arrowView.setVisibility(View.INVISIBLE);
                        int rolledCheck = state.diceRolled();
                        if(state.isComputer()) {
                            if (rolledCheck == 1)
                                makeComputerMove();
                            else if (rolledCheck == -1){
                                makeComputerRollDice();
                            }

                        }
                        dotview.setEnabled(true);

                    }

                };

                dotview.setGamestate(state);
                dotview.setOnTouchListener(new DotTouchListner(state.player,allowedPlayers,state){
                    public void touchDetected(int playernumber, int piecenumber){
                        if(diceHandler.rolled && !state.isComputer()){
                            state.pieceClicked(playernumber, piecenumber);
                                dotview.setEnabled(false);

                        }

                    }

                });

                state.setDiceHandler(diceHandler);
                if(playersIdentity[0] == -1)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makeComputerRollDice();
                        }
                    },500);


            }

        });

        dicecontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!diceHandler.rolled && !state.isComputer()) {
                    diceHandler.rollDice();
                    diceHandler.rolled = true;

                }
            }
        });
    }

    public boolean[] getAllowedPlayers(int[] players){
        boolean[] a = new boolean[4];
        for(int i=0;i<4;i++)
            if(players[i] != 0)
                a[i] = true;
        return a;
    }

    public void makeComputerMove(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                state.makeRandomMove();
            }
        },1000);
    }

    public void makeComputerRollDice(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!diceHandler.rolled) {
                    diceHandler.rollDice();
                    diceHandler.rolled = true;
                }
            }
        },1000);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.alert_exit_game,null);
        final AlertDialog alert = new AlertDialog.Builder(VsComputer.this).setView(relativeLayout).create();
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

}