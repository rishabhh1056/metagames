package com.first_player_games.ModelResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private List<User> user = null;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("code")
    @Expose
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("bank_detail")
        @Expose
        private String bankDetail;
        @SerializedName("adhar_card")
        @Expose
        private String adharCard;
        @SerializedName("upi")
        @Expose
        private String upi;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("referral_code")
        @Expose
        private String referralCode;
        @SerializedName("referred_by")
        @Expose
        private String referredBy;
        @SerializedName("wallet")
        @Expose
        private String wallet;
        @SerializedName("winning_wallet")
        @Expose
        private String winningWallet;
        @SerializedName("fcm")
        @Expose
        private String fcm;
        @SerializedName("table_id")
        @Expose
        private String tableId;
        @SerializedName("rummy_table_id")
        @Expose
        private String rummyTableId;
        @SerializedName("ander_bahar_room_id")
        @Expose
        private String anderBaharRoomId;
        @SerializedName("dragon_tiger_room_id")
        @Expose
        private String dragonTigerRoomId;
        @SerializedName("jackpot_room_id")
        @Expose
        private String jackpotRoomId;
        @SerializedName("seven_up_room_id")
        @Expose
        private String sevenUpRoomId;
        @SerializedName("rummy_pool_table_id")
        @Expose
        private String rummyPoolTableId;
        @SerializedName("game_played")
        @Expose
        private String gamePlayed;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("app_version")
        @Expose
        private String appVersion;
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

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getBankDetail() {
            return bankDetail;
        }

        public void setBankDetail(String bankDetail) {
            this.bankDetail = bankDetail;
        }

        public String getAdharCard() {
            return adharCard;
        }

        public void setAdharCard(String adharCard) {
            this.adharCard = adharCard;
        }

        public String getUpi() {
            return upi;
        }

        public void setUpi(String upi) {
            this.upi = upi;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getReferredBy() {
            return referredBy;
        }

        public void setReferredBy(String referredBy) {
            this.referredBy = referredBy;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getWinningWallet() {
            return winningWallet;
        }

        public void setWinningWallet(String winningWallet) {
            this.winningWallet = winningWallet;
        }

        public String getFcm() {
            return fcm;
        }

        public void setFcm(String fcm) {
            this.fcm = fcm;
        }

        public String getTableId() {
            return tableId;
        }

        public void setTableId(String tableId) {
            this.tableId = tableId;
        }

        public String getRummyTableId() {
            return rummyTableId;
        }

        public void setRummyTableId(String rummyTableId) {
            this.rummyTableId = rummyTableId;
        }

        public String getAnderBaharRoomId() {
            return anderBaharRoomId;
        }

        public void setAnderBaharRoomId(String anderBaharRoomId) {
            this.anderBaharRoomId = anderBaharRoomId;
        }

        public String getDragonTigerRoomId() {
            return dragonTigerRoomId;
        }

        public void setDragonTigerRoomId(String dragonTigerRoomId) {
            this.dragonTigerRoomId = dragonTigerRoomId;
        }

        public String getJackpotRoomId() {
            return jackpotRoomId;
        }

        public void setJackpotRoomId(String jackpotRoomId) {
            this.jackpotRoomId = jackpotRoomId;
        }

        public String getSevenUpRoomId() {
            return sevenUpRoomId;
        }

        public void setSevenUpRoomId(String sevenUpRoomId) {
            this.sevenUpRoomId = sevenUpRoomId;
        }

        public String getRummyPoolTableId() {
            return rummyPoolTableId;
        }

        public void setRummyPoolTableId(String rummyPoolTableId) {
            this.rummyPoolTableId = rummyPoolTableId;
        }

        public String getGamePlayed() {
            return gamePlayed;
        }

        public void setGamePlayed(String gamePlayed) {
            this.gamePlayed = gamePlayed;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
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
