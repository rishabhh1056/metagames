package com.metagards.metagames._RummyGodMode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameUsersCardModel {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("game_users")
    @Expose
    private List<GameUsersCardModel> gameUsers = null;
    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("joker")
    @Expose
    private String joker;

    public String getJoker() {
        return joker;
    }

    public void setJoker(String joker) {
        this.joker = joker;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GameUsersCardModel> getGameUsers() {
        return gameUsers;
    }

    public void setGameUsers(List<GameUsersCardModel> gameUsers) {
        this.gameUsers = gameUsers;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("card")
    @Expose
    private String card;
    @SerializedName("packed")
    @Expose
    private String packed;
    @SerializedName("added_date")
    @Expose
    private String addedDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("isDeleted")
    @Expose
    private String isDeleted;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("cards")
    @Expose
    private List<Card> cards = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPacked() {
        return packed;
    }

    public void setPacked(String packed) {
        this.packed = packed;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public class Card {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("card")
        @Expose
        private String card;
        @SerializedName("card_group")
        @Expose
        private String cardGroup;

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
            return cardGroup;
        }

        public void setCardGroup(String cardGroup) {
            this.cardGroup = cardGroup;
        }

    }
}
