package com.metagards.metagames._ColorPrediction;

public class Bots_list {
    private String id ;
    private String name;
    private String coin;
    private String avatar;
   //time

    public Bots_list(String id, String name, String coin, String avatar) {
        this.id = id;
        this.name = name;
        this.coin = coin;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
