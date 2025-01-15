package com.first_player_games.OnlineGame.Lobby;

import androidx.annotation.NonNull;

import com.first_player_games.Others.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class RoomFunctions {

    FirebaseFunctions functions;
    public RoomFunctions(){
        functions = FirebaseFunctions.getInstance();
        if(Constants.UseEmulator)
            functions.useEmulator("10.0.2.2",5001);
    }

    public void createRoom(int diamonds){
        HashMap<String,String> map = new HashMap<>();
        if(diamonds>0)
            map.put("diamonds_game","yes");
        else
            map.put("diamonds_game","no");
        map.put("diamonds",String.valueOf(diamonds));

        functions.getHttpsCallable("createRoom").call(map)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (String) task.getResult().getData().toString();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) System.out.println("Task Failed      " + task.getException().getMessage());
                        else {
                            roomCreated(task.getResult());}
                        }
                });
    }
    public void createRoom(){
        createRoom(-1);
    }

    public void joinRoom(String name,String roomid){
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("roomid", roomid);
        functions.getHttpsCallable("joinRoom").call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (String) task.getResult().getData().toString();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) System.out.println("Task Failed      " + task.getException().getMessage());
                        else {
                            roomJoined(Integer.parseInt(task.getResult()));
                        }
                    }
                });
    }

    public void startGame(String roomid){

        functions.getHttpsCallable("startGame").call(roomid);
    }

    public void roomCreated(String roomid){}
    public void roomJoined(int number){}

}
