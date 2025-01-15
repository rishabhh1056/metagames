package com.metagards.metagames.PaymentScreen;

public class Payment_model {
    String id;
    String name;
    String image;
    String pay_with;
    String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPay_with() {
        return pay_with;
    }

    public void setPay_with(String pay_with) {
        this.pay_with = pay_with;
    }
}
