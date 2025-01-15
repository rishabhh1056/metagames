package com.metagards.metagames.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCategoryResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_category")
    @Expose
    private List<UserCategory> userCategory = null;
    @SerializedName("code")
    @Expose
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserCategory> getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(List<UserCategory> userCategory) {
        this.userCategory = userCategory;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public class UserCategory {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("percentage")
        @Expose
        private String percentage;
        @SerializedName("added_date")
        @Expose
        private String addedDate;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;
        @SerializedName("isDeleted")
        @Expose
        private String isDeleted;

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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
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
    }

}
