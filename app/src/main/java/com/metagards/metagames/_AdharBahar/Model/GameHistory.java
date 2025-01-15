package com.metagards.metagames._AdharBahar.Model;

public class GameHistory {

    public String id;
    public String ander_baher_id;
    public String bet;
    public String amount;
    public String room_id;
    public String payment;
    public String transactionid;

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public String getStatus() {
        return status;
    }

    public String status;

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAnder_baher_id(String ander_baher_id) {
        this.ander_baher_id = ander_baher_id;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setWinning_amount(String winning_amount) {
        this.winning_amount = winning_amount;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getId() {
        return id;
    }

    public String getAnder_baher_id() {
        return ander_baher_id;
    }

    public String getBet() {
        return bet;
    }

    public String getAmount() {
        return amount;
    }

    public String getWinning_amount() {
        return winning_amount;
    }

    public String getAdded_date() {
        return added_date;
    }

    public String winning_amount;
    public String added_date;



}
