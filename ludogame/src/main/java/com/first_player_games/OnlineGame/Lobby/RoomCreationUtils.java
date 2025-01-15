package com.first_player_games.OnlineGame.Lobby;

import java.util.Random;

public class RoomCreationUtils {

    public static String generateRoomId(){
        String roomid = String.valueOf(System.currentTimeMillis()
                + new Random().nextInt( Integer.parseInt(String.valueOf(System.currentTimeMillis()%100000))));
        roomid = encode16(roomid);
        roomid = roomid.toUpperCase();
        return roomid;
    }

    public static String encode16(String roomid){
        int a = Integer.parseInt(roomid.substring(0,roomid.length()/2));
        int b = Integer.parseInt(roomid.substring(roomid.length()/2));
        return Integer.toHexString(a) + Integer.toHexString(b);
    }
}
