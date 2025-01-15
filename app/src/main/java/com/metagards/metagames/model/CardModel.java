package com.metagards.metagames.model;

import com.metagards.metagames.Utils.Functions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CardModel {
    public int total;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("cards_list")
    @Expose
    private List<CardModel> cards = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<CardModel> getCards() {
        return cards;
    }

    public void setCards(List<CardModel> cards) {
        this.cards = cards;
    }

    public CardModel(int cardItem) {
        CardItem = cardItem;
    }
    public CardModel(String image) {
        Image = image;
    }

    public CardModel() {

    }

    public String CardColor;
    public int CardNumber;

    public String getCardColor() {
        return CardColor;
    }

    public void setCardColor(String cardColor) {
        CardColor = cardColor;
    }

    public int getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(int cardNumber) {
        CardNumber = cardNumber;
    }

    public String user_id;
    public String winner_user_id;
    public String joker_card = "";
    public String status = "";
    public int score = 0;
    public int won = 0;
    public String user_name;
    public String game_id;


    public String group_value_params = "";
    public String group_value_response = "";


    public int CardItem;
    public String card_id;
    public String Image;
    public int tags;
    public int value_grp = 0;
    public String group_value = "Invalid";
    public String group_points = "0";
    public boolean isSelected;
    public ArrayList<CardModel> groups_cards;
    public int result = 0;
    public int packed = 0;
    public int pockerGroup_value = 0;
    private boolean isUserLeaveTable = false;


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("cards")
    @Expose
    private String card;
    @SerializedName("card_group")
    @Expose
    public String card_group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCardGroup() {
        return card_group;
    }

    public void setCardGroup(String cardGroup) {
        this.card_group = cardGroup;
    }

    public int getPockerGroup_value() {
        return pockerGroup_value;
    }

    public void setPockerGroup_value(int pockerGroup_value) {
        this.pockerGroup_value = pockerGroup_value;
    }

    public String getGroup_points() {
        return group_points;
    }

    public int getGroupPoints(){
        return Functions.checkisStringValid(group_points)
                ? Integer.parseInt(group_points) : 0;
    }

    public void setGroup_points(String group_points) {
        this.group_points = group_points;
    }

    public boolean isUserLeaveTable() {
        return isUserLeaveTable;
    }

    public void setUserLeaveTable(boolean userLeaveTable) {
        isUserLeaveTable = userLeaveTable;
    }

    @Override
    public String toString() {
        return "CardModel{" +
                "id='" + id + '\'' +
                ", CardItem=" + CardItem +
                ", Image='" + Image + '\'' +
                ", tags=" + tags +
                ", card_group='" + card_group + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
