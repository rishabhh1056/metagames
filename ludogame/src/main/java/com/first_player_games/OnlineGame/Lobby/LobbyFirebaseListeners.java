package com.first_player_games.OnlineGame.Lobby;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.first_player_games.Others.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LobbyFirebaseListeners {
    DatabaseReference room;
    private String roomid;
    private ProgressDialog dialog;
    private String STATUS = "status";
    private String NOP = "number_of_players";
    private String PLAYERS = "players";
    private String[] names = new String[4];

    private ChildEventListener playerJoinedListner = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            names[Integer.parseInt(snapshot.getKey())] = snapshot.getValue().toString();
            playerJoined(new String[]{snapshot.getKey(),snapshot.getValue().toString()});
        }

        @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
        @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
        @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
        @Override public void onCancelled(@NonNull DatabaseError error) { }
    };

    private ValueEventListener gameStatListner = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists())
                if(snapshot.getValue().toString().equals("started")) {
                    room.child("allowed_players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean[] players = new boolean[4];
                            for(int i=0;i<4;i++)
                                if(snapshot.child(String.valueOf(i)).getValue().toString().equals("yes"))
                                    players[i] = true;
                            removeAllListners();
                            gameStarted(players,names);
                        }
                        @Override public void onCancelled(@NonNull DatabaseError error) { }
                    });

            }
        }
        @Override public void onCancelled(@NonNull DatabaseError error) { }
    };


    public LobbyFirebaseListeners(String roomid){
        this.roomid = roomid;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(Constants.UseEmulator)
            database.useEmulator("10.0.2.2", 9000);
        room = database.getReference().child("rooms").child(roomid);
        room.child(PLAYERS).addChildEventListener(playerJoinedListner);
        room.child(STATUS).addValueEventListener(gameStatListner);
    }

    public void playerJoined(String[] data){}
    public void gameStarted(boolean[] players,String[] names){}

    public void removeAllListners(){
        room.child(PLAYERS).removeEventListener(playerJoinedListner);
        room.child(STATUS).removeEventListener(gameStatListner);
    }
}
