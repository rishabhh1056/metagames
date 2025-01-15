
package com.metagards.metagames.sample;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("jsonschema2pojo")
public class TableDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("no_of_participant")
    @Expose
    private String noOfParticipant;
    @SerializedName("registration_fee")
    @Expose
    private String registrationFee;
    @SerializedName("first_price")
    @Expose
    private String firstPrice;
    @SerializedName("second_price")
    @Expose
    private String secondPrice;
    @SerializedName("third_price")
    @Expose
    private String thirdPrice;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_date")
    @Expose
    private String addedDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("isDeleted")
    @Expose
    private String isDeleted;
    @SerializedName("is_registered")
    @Expose
    private String isRegistered;
    @SerializedName("participants")
    @Expose
    private List<Participant> participants = null;

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

    public String getNoOfParticipant() {
        return noOfParticipant;
    }

    public void setNoOfParticipant(String noOfParticipant) {
        this.noOfParticipant = noOfParticipant;
    }

    public String getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(String firstPrice) {
        this.firstPrice = firstPrice;
    }

    public String getSecondPrice() {
        return secondPrice;
    }

    public void setSecondPrice(String secondPrice) {
        this.secondPrice = secondPrice;
    }

    public String getThirdPrice() {
        return thirdPrice;
    }

    public void setThirdPrice(String thirdPrice) {
        this.thirdPrice = thirdPrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

}
