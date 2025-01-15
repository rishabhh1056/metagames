package com.first_player_games.LocalGame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first_player_games.BaseActivity;
import com.first_player_games.Others.Functions;
import com.first_player_games.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class LocalGame extends BaseActivity {

    boolean isChaaldone = true;

    DotsView dotview;
    Gamestate state;
    LinearLayout dicecontainer;
    ImageView dice;
    DiceHandler diceHandler;
    EditText cheatview;
    boolean[] players;

    int[] diceContainers = new int[]{
            R.id.dicecontainer,
            R.id.dicecontainer_yellow,
            R.id.dicecontainer_blue,
            R.id.dicecontainer_green
    };

    ImageView[] arrows;
    ImageView[] dices;
    private InterstitialAd mInterstitialAd;

    Animation animBounce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);
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

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interetitianl_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        if(getIntent().hasExtra("players")){
            players = getIntent().getExtras().getBooleanArray("players");
        }

        dotview = findViewById(R.id.ludodotview);
        dicecontainer = findViewById(R.id.dicecontainer);
        cheatview = findViewById(R.id.cheatview);
        dice = findViewById(R.id.dice);



        dices = new ImageView[]{
                findViewById(R.id.dice),
                findViewById(R.id.dice_yellow),
                findViewById(R.id.dice_blue),
                findViewById(R.id.dice_green)
        };

        arrows = new ImageView[]{
                findViewById(R.id.arrow_red),
                findViewById(R.id.arrow_yellow),
                findViewById(R.id.arrow_blue),
                findViewById(R.id.arrow_green),
        };

        showArrow(0);

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
                                showArrow(state.turn);
                            }
                        },100);

                    }
                    public void onGameWon(int playernumber){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LocalGame.this,R.style.CustomDialog);
                        LinearLayout linerlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_local_game_won,null);
                        builder.setView(linerlayout);
                        if(playernumber == 0){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_red));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Red Won");
                        }
                        else if(playernumber == 1){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_yello));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Yellow Won");
                        }
                        else if(playernumber == 2){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_blue));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Blue Won");
                        }
                        else if(playernumber == 3){
                            ((ImageView) linerlayout.findViewById(R.id.game_won_trophy)).setImageDrawable(getResources().getDrawable(R.drawable.trophy_green));
                            ((TextView) linerlayout.findViewById(R.id.gamewon_playername)).setText("Green Won");
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
                        showArrow(state.turn);
                      //  dotview.setEnabled(false);

                    }
                };
                state.initTurns();
                showDice(state.turn);
                diceHandler = new DiceHandler(getApplicationContext(),dices,MediaPlayer.create(LocalGame.this,R.raw.dice_rolling_effect)){
                    public void onDiceResule(){
                        dices[state.turn].startAnimation(animBounce);
                        state.diceRolled();
                        dotview.setEnabled(true);
                        hideArrow();
                        isChaaldone = true;
                    }
                };
                dotview.setGamestate(state);
                dotview.setOnTouchListener(new DotTouchListner(state.player,players,state){
                    public void touchDetected(int playernumber, int piecenumber){
                        Toast.makeText(LocalGame.this, "disabled", Toast.LENGTH_SHORT).show();
//                        final Handler handler = new Handler(Looper.getMainLooper());
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                dotview.setEnabled(true);
//                                Toast.makeText(LocalGame.this, "enable", Toast.LENGTH_SHORT).show();
//                            }
//                        }, 500);

                        if(isChaaldone) {

                            if (diceHandler.rolled) {
                                state.pieceClicked(playernumber, piecenumber);
                                isChaaldone = false;
                            }
                        }


                    }
                });
                state.setDiceHandler(diceHandler);
            }
        });

        for(int i=0;i<diceContainers.length;i++) {
            final int t = i;
            findViewById(diceContainers[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!diceHandler.rolled && t == state.turn)
                    {
                        diceHandler.rollDice();
                        diceHandler.rolled = true;
                    }
                }
            });
        }
        Toast.makeText(this, "P1 click on Dice to start the game.", Toast.LENGTH_LONG).show();

//        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    public void showDice(int turn){
        dices[turn].setVisibility(View.VISIBLE);
        //dices[turn].startAnimation(animBounce);
        for(int i=0;i<dices.length;i++)
            if(i!=turn) {
                dices[i].clearAnimation();
                dices[i].setVisibility(View.INVISIBLE);
            }
    }

    public void showArrow(int turn){
        arrows[turn].setVisibility(View.VISIBLE);
        for(int i=0;i<arrows.length;i++)
            if(i!=turn)
                arrows[i].setVisibility(View.INVISIBLE);
//        dotview.setEnabled(true);
    }

    public void hideArrow(){
     //   dotview.setEnabled(true);

        for(int i=0;i<arrows.length;i++)
            arrows[i].setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.alert_exit_game,null);
        final AlertDialog alert = new AlertDialog.Builder(LocalGame.this).setView(relativeLayout).create();
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