package com.first_player_games.OnlineGame.Lobby;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoomCreationLiveData extends ViewModel {

    private MutableLiveData<String> roomid;

    public MutableLiveData<String> getRoomid() {
        if (roomid == null) {
            roomid = new MutableLiveData<>();
            roomid.setValue("nothing");
        }
        return roomid;
    }

    public void setRoomId(String roomId){
        this.getRoomid().setValue(roomId);
    }

    public boolean roomIdNull(){
        return roomid == null;
    }


}
