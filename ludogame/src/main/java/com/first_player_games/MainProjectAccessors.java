package com.first_player_games;

import android.app.Application;

import com.first_player_games.Others.SharePref;

public interface MainProjectAccessors {
    // Marking it as optional just in case
    // you will not be able to get a context
    // from an object that implemented ContextAccessor

     public SharePref getSharePrefClass();
}
