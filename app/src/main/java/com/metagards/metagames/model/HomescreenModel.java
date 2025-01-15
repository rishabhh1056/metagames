package com.metagards.metagames.model;

import android.content.Intent;

public class HomescreenModel {
    int id;
    String itemName;
    Enum itemTag;
    int itemImage;
    Intent gameintent;
    String itemType;


    public HomescreenModel() {
    }

    public HomescreenModel(int id, Enum itemTag, int itemImage) {
        this.id = id;
        this.itemTag = itemTag;
        this.itemImage = itemImage;
    }

    public HomescreenModel(int id, Enum itemTag, int itemImage,String itemType) {
        this.id = id;
        this.itemTag = itemTag;
        this.itemImage = itemImage;
        this.itemType = itemType;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public Enum getItemTag() {
        return itemTag;
    }

    public void setItemTag(Enum itemTag) {
        this.itemTag = itemTag;
    }


    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
