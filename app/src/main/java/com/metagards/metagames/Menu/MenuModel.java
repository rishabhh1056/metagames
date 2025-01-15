package com.metagards.metagames.Menu;

public class MenuModel {

    private int id;
    private String item_name;
    private int item_icon;

    public MenuModel(int id, String item_name, int item_icon) {
        this.id = id;
        this.item_name = item_name;
        this.item_icon = item_icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_icon() {
        return item_icon;
    }

    public void setItem_icon(int item_icon) {
        this.item_icon = item_icon;
    }

}
