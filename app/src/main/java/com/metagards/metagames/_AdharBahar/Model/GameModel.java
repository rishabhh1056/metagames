package com.metagards.metagames._AdharBahar.Model;

public class GameModel {

    public String id;
    public String room_no;
    public String online_no;
    public String min_coin;

    public void setOnline(String online) {
        this.online = online;
    }

    public String getOnline() {
        return online;
    }

    public String online;

    public String getId() {
        return id;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public void setOnline_no(String online_no) {
        this.online_no = online_no;
    }

    public void setMin_coin(String min_coin) {
        this.min_coin = min_coin;
    }

    public void setMax_coin(String max_coin) {
        this.max_coin = max_coin;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getOnline_no() {
        return online_no;
    }

    public String getMin_coin() {
        return min_coin;
    }

    public String getMax_coin() {
        return max_coin;
    }

    public String getAdded_date() {
        return added_date;
    }

    public String max_coin;
    public String added_date;


}
