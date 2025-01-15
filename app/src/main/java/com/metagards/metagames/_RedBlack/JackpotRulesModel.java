package com.metagards.metagames._RedBlack;

public class JackpotRulesModel {
    public static final int TYPE_LOW = 0;
    public static final int TYPE_HIGH = 1;
    boolean isWine;
    int type = -1;
    boolean animatedAddedAmount;
    int last_added_id;
    int last_added_amount;
    int last_added_rule_value;
    String rule_type;
    int rule_value;
    int wining_rule_value = -1;
    int added_amount;
    int select_amount;
    String into;


    public JackpotRulesModel() {
    }

    public JackpotRulesModel(String rule_type,int rule_value, int added_amount, int select_amount, String into) {
        this.rule_type = rule_type;
        this.rule_value = rule_value;
        this.added_amount = added_amount;
        this.select_amount = select_amount;
        this.into = into;
    }

    public JackpotRulesModel(String rule_type,int rule_value, int added_amount, int select_amount, String into
            ,int view_type
    ) {
        this.rule_type = rule_type;
        this.rule_value = rule_value;
        this.added_amount = added_amount;
        this.select_amount = select_amount;
        this.into = into;
        this.type = view_type;
    }

    public JackpotRulesModel(String rule_type,int wining_rule_value,int rule_value, int added_amount, int select_amount, String into
            ,int view_type
    ) {
        this.rule_type = rule_type;
        this.rule_value = rule_value;
        this.wining_rule_value = wining_rule_value;
        this.added_amount = added_amount;
        this.select_amount = select_amount;
        this.into = into;
        this.type = view_type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWining_rule_value() {
        return wining_rule_value;
    }

    public void setWining_rule_value(int wining_rule_value) {
        this.wining_rule_value = wining_rule_value;
    }
}
