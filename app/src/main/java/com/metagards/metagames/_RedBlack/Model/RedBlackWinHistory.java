package com.metagards.metagames._RedBlack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedBlackWinHistory {

    @SerializedName("winners")
    @Expose
    private List<Winner> winners = null;
    @SerializedName("code")
    @Expose
    private Integer code;

    public List<Winner> getWinners() {
        return winners;
    }

    public void setWinners(List<Winner> winners) {
        this.winners = winners;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public class UserDatum {

        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("winning_amount")
        @Expose
        private String winningAmount;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getWinningAmount() {
            return winningAmount;
        }

        public void setWinningAmount(String winningAmount) {
            this.winningAmount = winningAmount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

    }

    public class Winner {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("rewards")
        @Expose
        private String rewards;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("winners")
        @Expose
        private String winners;
        @SerializedName("user_data")
        @Expose
        private List<UserDatum> userData = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRewards() {
            return rewards;
        }

        public void setRewards(String rewards) {
            this.rewards = rewards;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWinners() {
            return winners;
        }

        public void setWinners(String winners) {
            this.winners = winners;
        }

        public List<UserDatum> getUserData() {
            return userData;
        }

        public void setUserData(List<UserDatum> userData) {
            this.userData = userData;
        }


    }

}

