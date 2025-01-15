package com.metagards.metagames._CarRoullete;

public class CarRollouteModel {
    boolean isWine;
    boolean animatedAddedAmount;
    int last_added_id;
    int last_added_amount;
    int last_added_rule_value;
    String rule_type;
    int rule_icon;
    int rule_value;
    int added_amount;
    int select_amount;
    String into;


    public CarRollouteModel() {
    }

    public CarRollouteModel(String rule_type,int rule_icon,int rule_value, int added_amount, int select_amount, String into) {
        this.rule_type = rule_type;
        this.rule_icon = rule_icon;
        this.rule_value = rule_value;
        this.added_amount = added_amount;
        this.select_amount = select_amount;
        this.into = into;
    }

    public String getRule_type() {
        return rule_type;
    }

    public void setRule_type(String rule_type) {
        this.rule_type = rule_type;
    }

    public int getAdded_amount() {
        return added_amount;
    }

    public void setAdded_amount(int added_amount) {
        this.added_amount = added_amount;
    }

    public int getSelect_amount() {
        return select_amount;
    }

    public void setSelect_amount(int select_amount) {
        this.select_amount = select_amount;
    }

    public String getInto() {
        return into;
    }

    public void setInto(String into) {
        this.into = into;
    }

    public boolean isAnimatedAddedAmount() {
        return animatedAddedAmount;
    }

    public void setAnimatedAddedAmount(boolean animatedAddedAmount) {
        this.animatedAddedAmount = animatedAddedAmount;
    }

    public int getLast_added_amount() {
        return last_added_amount;
    }

    public void setLast_added_amount(int last_added_amount) {
        this.last_added_amount = last_added_amount;
    }

    public int getLast_added_rule_value() {
        return last_added_rule_value;
    }

    public void setLast_added_rule_value(int last_added_rule_value) {
        this.last_added_rule_value = last_added_rule_value;
    }

    public int getLast_added_id() {
        return last_added_id;
    }

    public void setLast_added_id(int last_added_id) {
        this.last_added_id = last_added_id;
    }

    public boolean isWine() {
        return isWine;
    }

    public void setWine(boolean wine) {
        isWine = wine;
    }

    public int getRule_icon() {
        return rule_icon;
    }

    public void setRule_icon(int rule_icon) {
        this.rule_icon = rule_icon;
    }
}
