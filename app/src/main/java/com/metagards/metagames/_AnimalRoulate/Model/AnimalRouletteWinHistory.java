package com.metagards.metagames._AnimalRoulate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimalRouletteWinHistory {
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

    public static class Winner {

        public String rule_type;
        public int rule_value;
        public int wining_count;
        public Winner(String rule_type,int rule_value) {
            this.rule_type = rule_type;
            this.rule_value = rule_value;
        }

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("room_id")
        @Expose
        private String roomId;
        @SerializedName("card1")
        @Expose
        private String card1;
        @SerializedName("card2")
        @Expose
        private String card2;
        @SerializedName("card3")
        @Expose
        private String card3;
        @SerializedName("winning")
        @Expose
        private String winning;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("end_datetime")
        @Expose
        private String endDatetime;
        @SerializedName("added_date")
        @Expose
        private String addedDate;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getCard1() {
            return card1;
        }

        public void setCard1(String card1) {
            this.card1 = card1;
        }

        public String getCard2() {
            return card2;
        }

        public void setCard2(String card2) {
            this.card2 = card2;
        }

        public String getCard3() {
            return card3;
        }

        public void setCard3(String card3) {
            this.card3 = card3;
        }

        public String getWinning() {
            return winning;
        }

        public void setWinning(String winning) {
            this.winning = winning;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getEndDatetime() {
            return endDatetime;
        }

        public void setEndDatetime(String endDatetime) {
            this.endDatetime = endDatetime;
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
    }

}
