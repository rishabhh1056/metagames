package com.metagards.metagames.model;

import java.io.Serializable;

public class TableModel implements Serializable {

    private String id;
    private String bootValue;
    private String maximumBlind;
    private String chaalLimit;
    private String potLimit;
    private String addedDate;
    private String updatedDate;
    private String isDeleted;
    private String onlineMembers;
    private String point_value = "";
    private String max_player = "";
    private String table_name = "";
    private String viewer_status = "";
    private String invitation_code = "";
    private String winning_amount = "";
    private String founder_id = "";
    private String tableType = "";
    private String password = "";
    private int ongoin = 0;

    public int getOngoin() {
        return ongoin;
    }

    public void setOngoin(int ongoin) {
        this.ongoin = ongoin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getFounder_id() {
        return founder_id;
    }

    public void setFounder_id(String founder_id) {
        this.founder_id = founder_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getViewer_status() {
        return viewer_status;
    }

    public void setViewer_status(String viewer_status) {
        this.viewer_status = viewer_status;
    }

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getWinning_amount() {
        return winning_amount;
    }

    public void setWinning_amount(String winning_amount) {
        this.winning_amount = winning_amount;
    }

    public String getMax_player() {
        return max_player;
    }

    public void setMax_player(String max_player) {
        this.max_player = max_player;
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

    public String getPoint_value() {
        return point_value;
    }

    public void setPoint_value(String point_value) {
        this.point_value = point_value;
    }

}
