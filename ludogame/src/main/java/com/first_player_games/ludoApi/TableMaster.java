package com.first_player_games.ludoApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TableMaster {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("table_data")
    @Expose
    private List<TableDatum> tableData = null;
    @SerializedName("code")
    @Expose
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TableDatum> getTableData() {
        return tableData;
    }

    public void setTableData(List<TableDatum> tableData) {
        this.tableData = tableData;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public class TableDatum {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("ludo_table_id")
        @Expose
        private String ludo_table_id;

        @SerializedName("boot_value")
        @Expose
        private String bootValue;
        @SerializedName("maximum_blind")
        @Expose
        private String maximumBlind;
        @SerializedName("chaal_limit")
        @Expose
        private String chaalLimit;
        @SerializedName("pot_limit")
        @Expose
        private String potLimit;
        @SerializedName("added_date")
        @Expose
        private String addedDate;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;
        @SerializedName("isDeleted")
        @Expose
        private String isDeleted;
        @SerializedName("online_members")
        @Expose
        private String onlineMembers;

        @SerializedName("invite_code")
        @Expose
        private String invite_code;

        @SerializedName("room_id")
        @Expose
        private String room_id;

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBootValue() {
            return bootValue;
        }

        public void setBootValue(String bootValue) {
            this.bootValue = bootValue;
        }

        public String getMaximumBlind() {
            return maximumBlind;
        }

        public void setMaximumBlind(String maximumBlind) {
            this.maximumBlind = maximumBlind;
        }

        public String getChaalLimit() {
            return chaalLimit;
        }

        public void setChaalLimit(String chaalLimit) {
            this.chaalLimit = chaalLimit;
        }

        public String getPotLimit() {
            return potLimit;
        }

        public void setPotLimit(String potLimit) {
            this.potLimit = potLimit;
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

        public String getOnlineMembers() {
            return onlineMembers;
        }

        public void setOnlineMembers(String onlineMembers) {
            this.onlineMembers = onlineMembers;
        }

        public String getLudo_table_id() {
            return ludo_table_id;
        }

        public void setLudo_table_id(String ludo_table_id) {
            this.ludo_table_id = ludo_table_id;
        }
    }
}
