package com.metagards.metagames.Details.Menu;

public class ReferralUserPurchModel {


    public  String id;
    public  String user_id;
    public  String referred_user_id;

    public  String name;

    public  String added_date = "";
    public String bet = "";
    public String room_id = "";

    public String level;


    public String updated_date;
    public String price;
    public String coin;


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getuser_id() {
        return user_id;
    }

    public void setuser_id(String id) {
        this.user_id = user_id;
    }

    public String getReferred_user_id() {
        return referred_user_id;
    }

    public void setReferred_user_id(String referred_user_id) {
        this.referred_user_id = referred_user_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
