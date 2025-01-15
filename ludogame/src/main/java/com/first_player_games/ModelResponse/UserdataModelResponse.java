package com.first_player_games.ModelResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserdataModelResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_data")
    @Expose
    private List<UserDatum> userData = null;

    @SerializedName("table_users")
    @Expose
    private List<UserDatum> table_users = null;
    @SerializedName("setting")
    @Expose
    private Setting setting;
    @SerializedName("code")
    @Expose
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserDatum> getUserData() {
        return userData;
    }

    public void setUserData(List<UserDatum> userData) {
        this.userData = userData;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<UserDatum> getTable_users() {
        return table_users;
    }

    public void setTable_users(List<UserDatum> table_users) {
        this.table_users = table_users;
    }

    public class Setting {

        @SerializedName("min_redeem")
        @Expose
        private String minRedeem;
        @SerializedName("referral_amount")
        @Expose
        private String referralAmount;
        @SerializedName("contact_us")
        @Expose
        private String contactUs;
        @SerializedName("terms")
        @Expose
        private String terms;
        @SerializedName("privacy_policy")
        @Expose
        private String privacyPolicy;
        @SerializedName("help_support")
        @Expose
        private String helpSupport;
        @SerializedName("game_for_private")
        @Expose
        private String gameForPrivate;
        @SerializedName("app_version")
        @Expose
        private String appVersion;
        @SerializedName("joining_amount")
        @Expose
        private String joiningAmount;
        @SerializedName("whats_no")
        @Expose
        private String whatsNo;
        @SerializedName("bonus")
        @Expose
        private String bonus;
        @SerializedName("payment_gateway")
        @Expose
        private String paymentGateway;
        @SerializedName("symbol")
        @Expose
        private String symbol;
        @SerializedName("razor_api_key")
        @Expose
        private String razorApiKey;
        @SerializedName("cashfree_client_id")
        @Expose
        private String cashfreeClientId;
        @SerializedName("cashfree_stage")
        @Expose
        private String cashfreeStage;
        @SerializedName("paytm_mercent_id")
        @Expose
        private String paytmMercentId;
        @SerializedName("payumoney_key")
        @Expose
        private String payumoneyKey;
        @SerializedName("share_text")
        @Expose
        private String shareText;
        @SerializedName("bank_detail_field")
        @Expose
        private String bankDetailField;
        @SerializedName("adhar_card_field")
        @Expose
        private String adharCardField;
        @SerializedName("upi_field")
        @Expose
        private String upiField;
        @SerializedName("referral_link")
        @Expose
        private String referralLink;
        @SerializedName("referral_id")
        @Expose
        private String referralId;

        public String getMinRedeem() {
            return minRedeem;
        }

        public void setMinRedeem(String minRedeem) {
            this.minRedeem = minRedeem;
        }

        public String getReferralAmount() {
            return referralAmount;
        }

        public void setReferralAmount(String referralAmount) {
            this.referralAmount = referralAmount;
        }

        public String getContactUs() {
            return contactUs;
        }

        public void setContactUs(String contactUs) {
            this.contactUs = contactUs;
        }

        public String getTerms() {
            return terms;
        }

        public void setTerms(String terms) {
            this.terms = terms;
        }

        public String getPrivacyPolicy() {
            return privacyPolicy;
        }

        public void setPrivacyPolicy(String privacyPolicy) {
            this.privacyPolicy = privacyPolicy;
        }

        public String getHelpSupport() {
            return helpSupport;
        }

        public void setHelpSupport(String helpSupport) {
            this.helpSupport = helpSupport;
        }

        public String getGameForPrivate() {
            return gameForPrivate;
        }

        public void setGameForPrivate(String gameForPrivate) {
            this.gameForPrivate = gameForPrivate;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getJoiningAmount() {
            return joiningAmount;
        }

        public void setJoiningAmount(String joiningAmount) {
            this.joiningAmount = joiningAmount;
        }

        public String getWhatsNo() {
            return whatsNo;
        }

        public void setWhatsNo(String whatsNo) {
            this.whatsNo = whatsNo;
        }

        public String getBonus() {
            return bonus;
        }

        public void setBonus(String bonus) {
            this.bonus = bonus;
        }

        public String getPaymentGateway() {
            return paymentGateway;
        }

        public void setPaymentGateway(String paymentGateway) {
            this.paymentGateway = paymentGateway;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getRazorApiKey() {
            return razorApiKey;
        }

        public void setRazorApiKey(String razorApiKey) {
            this.razorApiKey = razorApiKey;
        }

        public String getCashfreeClientId() {
            return cashfreeClientId;
        }

        public void setCashfreeClientId(String cashfreeClientId) {
            this.cashfreeClientId = cashfreeClientId;
        }

        public String getCashfreeStage() {
            return cashfreeStage;
        }

        public void setCashfreeStage(String cashfreeStage) {
            this.cashfreeStage = cashfreeStage;
        }

        public String getPaytmMercentId() {
            return paytmMercentId;
        }

        public void setPaytmMercentId(String paytmMercentId) {
            this.paytmMercentId = paytmMercentId;
        }

        public String getPayumoneyKey() {
            return payumoneyKey;
        }

        public void setPayumoneyKey(String payumoneyKey) {
            this.payumoneyKey = payumoneyKey;
        }

        public String getShareText() {
            return shareText;
        }

        public void setShareText(String shareText) {
            this.shareText = shareText;
        }

        public String getBankDetailField() {
            return bankDetailField;
        }

        public void setBankDetailField(String bankDetailField) {
            this.bankDetailField = bankDetailField;
        }

        public String getAdharCardField() {
            return adharCardField;
        }

        public void setAdharCardField(String adharCardField) {
            this.adharCardField = adharCardField;
        }

        public String getUpiField() {
            return upiField;
        }

        public void setUpiField(String upiField) {
            this.upiField = upiField;
        }

        public String getReferralLink() {
            return referralLink;
        }

        public void setReferralLink(String referralLink) {
            this.referralLink = referralLink;
        }

        public String getReferralId() {
            return referralId;
        }

        public void setReferralId(String referralId) {
            this.referralId = referralId;
        }

    }

    public class UserDatum {

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
