package com.first_player_games;

import android.app.Application;

import com.first_player_games.Api.LudoApiRepository;

public class LudoApplication extends Application {

    public LudoApiRepository ludoApiReposatry;

    @Override
    public void onCreate() {
        super.onCreate();
        ludoApiReposatry = LudoApiRepository.getInstance().init(getApplicationContext());
    }
}
