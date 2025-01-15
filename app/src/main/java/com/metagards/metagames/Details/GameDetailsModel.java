package com.metagards.metagames.Details;

import java.util.ArrayList;
import java.util.List;

public class GameDetailsModel {
    String item_id;
    String title;
    int image;
    boolean isSelected;
    boolean isSublist;
    List<GameDetailsModel> gameDetailsModelList = new ArrayList<>();

    public GameDetailsModel(String item_id, String title,int image) {
        this.item_id = item_id;
        this.title = title;
        this.image = image;
    }
    public GameDetailsModel(String item_id, String title,boolean isSublist) {
        this.item_id = item_id;
        this.title = title;
        this.isSublist = isSublist;
    }

    public GameDetailsModel(String item_id, String title,List<GameDetailsModel> gameDetailsModelList) {
        this.item_id = item_id;
        this.title = title;
        this.gameDetailsModelList = gameDetailsModelList;
    }

    public boolean isSublist() {
        return isSublist;
    }

    public void setSublist(boolean sublist) {
        isSublist = sublist;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<GameDetailsModel> getGameDetailsModelList() {
        return gameDetailsModelList;
    }

    public void setGameDetailsModelList(List<GameDetailsModel> gameDetailsModelList) {
        this.gameDetailsModelList = gameDetailsModelList;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
