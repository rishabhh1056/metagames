package com.metagards.metagames._AdharBahar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metagards.metagames.BaseActivity;
import com.metagards.metagames._AdharBahar.Fragments.GameFragment;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.squareup.picasso.Picasso;

import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;;

public class AndharBahar_Home_A extends BaseActivity {

    View lnrhistory,lnrhome;

    ImageView imghistory,imggame;
    TextView txthistory,txtgame;
    int version = 0 ;
    int startingPosition = 0;
    Fragment fragment;
    AndharBahar_Home_A context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhar_bahar__home_);


        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String name = sharedPreferences.getString("name","");
        String wallet = sharedPreferences.getString("wallet","");
        String profile_pic = sharedPreferences.getString("profile_pic","");


        Picasso.get()
                .load(Const.IMGAE_PATH + profile_pic)
                .into((ImageView) findViewById(R.id.imaprofile));


        if(!name.equals(""))
            ((TextView) findViewById(R.id.txtproname)).setText(""+name);

        if(!wallet.equals(""))
            ((TextView) findViewById(R.id.txtwallet)).setText(""+wallet);

        sharedPreferences.edit().putString("user_id1","9").commit();

        Initialization();

        ChangeBottomNavigation("home");

    }

    private void Initialization() {

        lnrhistory = findViewById(R.id.lnrhistory);
        lnrhome = findViewById(R.id.lnrhome);


        imghistory = findViewById(R.id.imghistory);
        imggame = findViewById(R.id.imggame);

        txthistory = findViewById(R.id.txthistory);
        txtgame = findViewById(R.id.txtgame);

        BottomnavOnClick();

    }

    private void BottomnavOnClick() {

        lnrhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeBottomNavigation("history");
            }
        });

        lnrhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeBottomNavigation("home");

            }
        });
    }



    private void ChangeBottomNavigation(String tag){

        if(tag.equals("home"))
        {
            txthistory.setTextColor(getResources().getColor(R.color.default_textcolor));
            txtgame.setTextColor(getResources().getColor(R.color.active_navcolor));

            imghistory.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.unactive_navcolor)));
            imggame.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.active_navcolor)));

            fragment = new GameFragment();
            loadFragment(fragment,1);


        }
        else if(tag.equals("history"))
        {
            txthistory.setTextColor(getResources().getColor(R.color.active_navcolor));
            txtgame.setTextColor(getResources().getColor(R.color.default_textcolor));

            imghistory.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.active_navcolor)));
            imggame.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.unactive_navcolor)));

        }

    }

    private void loadFragment(Fragment fragment, int newPosition) {

        if(fragment != null) {
            if(startingPosition > newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
//                        R.anim.enter_right_to_left , R.anim.exit_right_to_left);
                transaction.replace(R.id.home_container, fragment);
//                transaction.addToBackStack(null);
                transaction.commit();
            }
            if(startingPosition < newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left ,
//                        R.anim.enter_left_to_right , R.anim.exit_left_to_right);
                transaction.replace(R.id.home_container, fragment);
//                transaction.addToBackStack(null);
                transaction.commit();
            }

            startingPosition = newPosition;

        }


    }

    public void onBack(View view) {
        onBackPressed();
    }

}