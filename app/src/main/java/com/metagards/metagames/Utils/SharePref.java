package com.metagards.metagames.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.metagards.metagames.account.LoginScreen;

import java.util.Set;

public class SharePref {

    public static final String avator = "avator";

    public static final String TNC_ACCEPTED = "tnc_accepted";
    public static final String ADMIN_COMMISSION = "admin_commission";
    public static String selected_languge = "selected_languge";
    public static String languge_code = "languge_code";
    public final static String lastAmountAddedID = "lastAmountAddedID";
    public final static String referral_link = "referral_link";
    public static String isCardDrawn = "isCardDrawn";
    public static String storeChatID = "storeChatID";
    public static String isStartGameAccespted = "isStartGameAccespted";
    private volatile static SharePref mInstance;
    private Context mContext;


    private SharedPreferences mPref;

    public static final String pref_name="Login_data";
    public static final String u_id="user_id";
    public static final String u_name="name";
    public static final String u_pic="profile_pic";
    public static final String bank_detail="bank_detail";
    public static final String upi="upi";
    public static final String adhar_card="adhar_card";
    public static final String mobile="mobile";
    public static final String referal_code="referal_code";
    public static final String img_name="img_name";
    public static final String winning_wallet="winning_wallet";
    public static final String wallet="wallet";
    public static final String game_for_private="game_for_private";
    public static final String app_version="app_version";
    public static final String whats_no="whats_no";
    public static final String password ="password";
    public static final String isNew_user ="isNew_user";
    public static final String authorization ="token";
    public static final String BlockTime ="BlockTime";
    public static final String isLoginBlock ="isLoginBlock";
    public static final String isExtra ="isExtra";
    private final String spin_remaining ="spin_remaining";
    public static final String isEnable ="isEnable";
    public static final String spin_discount ="spin_discount";

    public static final String isBonusShow ="isbonusShow";
    public static final String isPaymentGateShow ="isPaymentGateShow";

    public static final String PaymentType ="PaymentType";
    public static final String UPI_ID ="upi_id";

    public static final String MerchantID ="merchant_id";
    public static final String MerchantSecret ="merchant_secret";

    public static final String SYMBOL_TYPE ="symbol_type";
    public static final String RAZOR_PAY_KEY ="razor_pay_key";
    public static final String SHARE_APP_TEXT ="share_app_text";
    public static final String referral_amount ="referral_amount";

    public static final String Profile_Field1 ="Profile_Field1";
    public static final String Profile_Field2 ="Profile_Field2";
    public static final String Profile_Field3 ="Profile_Field3";
    public static final String Profile_Field4 ="Profile_Field4";
    public static final String Profile_Field5 ="Profile_Field5";

    public static final String CASHFREE_CLIENT_ID ="cashfree_client_id";
    public static final String CASHFREE_STAGE ="cashfree_stage";
    public static final String PAYTM_MERCENT_ID ="paytm_mercent_id";

    public static final String PAYU_MERCENT_KEY ="payu_mercent_key";
    public static final String PAYTM_MERCHANT_SALT ="paytm_merchant_salt";


    public static final String isTeenpattiVisible ="isTeenpattiVisible";
    public static final String isPrivateVisible ="isPrivateVisible";
    public static final String isCustomVisible ="isCustomVisible";
    public static final String isRummyVisible ="isRummyVisible";
    public static final String isPrivateRummyVisible ="isPrivateRummyVisible";
    public static final String isDealRummyVisible ="isDealRummyVisible";
    public static final String isPointRummyVisible ="isPointRummyVisible";
    public static final String isPoolRummyVisible ="isPoolRummyVisible";
    public static final String isAndharBaharVisible ="isAndharBaharVisible";
    public static final String isDragonTigerVisible ="isDragonTigerVisible";
    public static final String isSevenUpVisible ="isSevenUpVisible";
    public static final String isCarRouletteVisible ="isCarRouletteVisible";
    public static final String isHomeJackpotVisible ="isHomeJackpotVisible";
    public static final String isAnimalRouletteVisible ="isAnimalRouletteVisible";
    public static final String isColorPredictionVisible ="isColorPredictionVisible";
    public static final String isColorPrediction1Visible ="isColorPrediction1Visible";
    public static final String isColorPrediction3Visible ="isColorPrediction3Visible";
    public static final String isAviatorVisible ="isAviatorVisible";
    public static final String isPokerVisible ="isPokerVisible";
    public static final String isHeadTailVisible ="isHeadtailVisible";
    public static final String isRedBlackVisible ="isRedBlackVisible";
    public static final String isLudoVisible ="isLudoVisible";      //local
    public static final String isLudoOnlineVisible ="isLudoOnlineVisible";      //online
    public static final String isLudoComputerVisible ="isLudoComputerVisible";      //computer
    public static final String isBacarateVisible ="isBacarateVisible";
    public static final String isJhandi_MundaVisible ="isJhandi_MundaVisible";
    public static final String isRouletteVisible ="isRouletteVisible";
    public static final String isTournamentVisible ="isTournamentVisible";

    public static final String isLoginWithPassword ="isLoginWithPassword";



    public static final String REFERRAL_AMOUNT = "referral_amount";
    public static final String JOINING_AMOUNT = "joining_amount";
    private final String USER_CATEGORY ="user_category";


    public static final String gender="u_gender";


    public static final String islogin="is_login";

    /**
     * A factory method for
     *
     * @return a instance of this class
     */
    public static SharePref getInstance() {
        if (null == mInstance) {
            synchronized (SharePref.class) {
                if (null == mInstance) {
                    mInstance = new SharePref();
                }
            }
        }
        return mInstance;
    }



    /**
     * initialization of context, use only first time later it will use this again and again
     *
     * @param context app context: first time
     */
    public void init(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (mPref == null) {
//            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            mPref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        }
    }

    public void putString(String key, String value) {

        init(mContext);

        if(mPref == null)
            return;

        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public Set<String> getStringSet(String key, Set<String> def) {
        return mPref.getStringSet(key, def);
    }

    public void putLong(String key, long value) {

        init(mContext);

        if(mPref == null)
            return;

        SharedPreferences.Editor editor = mPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {

        init(mContext);

        if(mPref == null)
            return;

        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {

        init(mContext);

        if(mPref == null)
            return;

        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public boolean getBoolean(String key) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getBoolean(key, false);

        return false;
    }

    public boolean getBoolean(String key, boolean def) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getBoolean(key, def);

        return false;
    }

    @Nullable
    public String getString(String key) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getString(key, "");

        return "";
    }

    @Nullable
    public String getString(String key, String def) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getString(key, def);

        return "";
    }

    public long getLong(String key) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getLong(key, 0);

        return 0;
    }

    public long getLong(String key, int defInt) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getLong(key, defInt);

        return 0;
    }

    public int getInt(String key) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getInt(key, 0);

        return 0;
    }

    public int getInt(String key, int defInt) {

        if (mContext != null)
        {
            init(mContext);
        }

        if(mPref != null)
            return mPref.getInt(key, defInt);

        return 0;
    }

    public static boolean getIsTeenpattiVisible() {
        return SharePref.getInstance().getBoolean(isTeenpattiVisible);
    }

    public static boolean getIsPrivateVisible() {
        return SharePref.getInstance().getBoolean(isPrivateVisible);
    }

    public static boolean getIsCustomVisible() {
        return SharePref.getInstance().getBoolean(isCustomVisible);
    }

    public static boolean getIsRummyVisible() {
        return SharePref.getInstance().getBoolean(isRummyVisible);
    }

    public static boolean getIsPointRummyVisible() {
        return SharePref.getInstance().getBoolean(isPointRummyVisible);
    }
    public static boolean getIsPoolRummyVisible() {
        return SharePref.getInstance().getBoolean(isPoolRummyVisible);
    }
    public static boolean getIsDealRummyVisible() {
        return SharePref.getInstance().getBoolean(isDealRummyVisible);
    }
    public static boolean getIsPrivateRummyVisible() {
        return SharePref.getInstance().getBoolean(isPrivateRummyVisible);
    }

    public static boolean getIsAndharBaharVisible() {
        return SharePref.getInstance().getBoolean(isAndharBaharVisible);
    }

    public static boolean getIsDragonTigerVisible() {
        return SharePref.getInstance().getBoolean(isDragonTigerVisible);
    }

    public static boolean getIsSevenUpVisible() {
        return SharePref.getInstance().getBoolean(isSevenUpVisible);
    }
    public static boolean getIsCarRouletteVisible() {
        return SharePref.getInstance().getBoolean(isCarRouletteVisible);
    }
    public static boolean getIsHomeJackpotVisible() {
        return SharePref.getInstance().getBoolean(isHomeJackpotVisible);
    }
    public static boolean getIsAnimalRouletteVisible() {
        return SharePref.getInstance().getBoolean(isAnimalRouletteVisible);
    }
    public static boolean getIsLoginWithPassword() {
        return SharePref.getInstance().getBoolean(isLoginWithPassword);
    }
    public static boolean getIsColorPredictionVisible() {
        return SharePref.getInstance().getBoolean(isColorPredictionVisible);
    }
    public static boolean getIsColorPrediction3Visible() {
        return SharePref.getInstance().getBoolean(isColorPrediction3Visible);
    }
    public static boolean getIsAviatorVisible() {
        return SharePref.getInstance().getBoolean(isAviatorVisible);
    }
    public static boolean getIsColorPrediction1Visible() {
        return SharePref.getInstance().getBoolean(isColorPrediction1Visible);
    }
    public static boolean getIsPokerVisible() {
        return SharePref.getInstance().getBoolean(isPokerVisible);
    }
    public static boolean getIsHeadTailVisible() {
        return SharePref.getInstance().getBoolean(isHeadTailVisible);
    }
    public static boolean getIsRedBlackVisible() {
        return SharePref.getInstance().getBoolean(isRedBlackVisible);
    }
    public static boolean getIsLudoVisible() {
        return SharePref.getInstance().getBoolean(isLudoVisible);
    }
    public static boolean getIsLudoOnlineVisible() {
        return SharePref.getInstance().getBoolean(isLudoOnlineVisible);
    }
    public static boolean getIsLudoComputerVisible() {
        return SharePref.getInstance().getBoolean(isLudoComputerVisible);
    }
    public static boolean getBacarateVisible() {
        return SharePref.getInstance().getBoolean(isBacarateVisible);
    }
    public static boolean getJhandi_MundaVisible() {
        return SharePref.getInstance().getBoolean(isJhandi_MundaVisible);
    }
    public static boolean getRouletteVisible() {
        return SharePref.getInstance().getBoolean(isRouletteVisible);
    }
    public static boolean getTournamentVisible() {
        return SharePref.getInstance().getBoolean(isTournamentVisible);
    }
    public boolean contains(String key) {
        return mPref.contains(key);
    }


    public void remove(String key) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public SharedPreferences.Editor edit() {

        if (mContext != null)
        {
            init(mContext);
        }

        if (mPref != null) {
//            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = mPref.edit();
            return editor;
        }

        return null;
    }

    public void clear() {

        init(mContext);

        if(mPref == null)
            return;

        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.apply();
    }


    public static String getU_id() {
        return SharePref.getInstance().getString(SharePref.u_id,"");
    }
    
    public static void setU_id(String value){
        SharePref.getInstance().putString(SharePref.u_id,value);
    }
    public static String getAuthorization() {
        return SharePref.getInstance().getString(SharePref.authorization,"");
    }

    public static void setAuthorization(String value){
        SharePref.getInstance().putString(SharePref.authorization,value);
    }

    public static boolean isLogin() {
        return SharePref.getInstance().getBoolean(SharePref.islogin,false);
    }

    public static boolean isNewUser(){
        return SharePref.getInstance().getBoolean(SharePref.isNew_user,true);
    }

    public static boolean isLoginBlock(){

        long block_time = SharePref.getInstance().getLong(SharePref.BlockTime, 0);
        if (block_time == 0 || block_time <= Functions.getCurrentTimeMillis()) {
            SharePref.getInstance().putLong(SharePref.BlockTime, 0);
            SharePref.getInstance().putBoolean(SharePref.isLoginBlock, false);
        }

        return SharePref.getInstance().getBoolean(SharePref.isLoginBlock,false);
    }

    public static void clearData(Activity context){
        SharePref.getInstance().clear();
        context.startActivity(new Intent(context, LoginScreen.class));
        context.finishAffinity();
    }

    public static final String ISGODMODEENABLE = "ISGODMODEENABLE";

    public boolean isGodmodeEnable() {
        return getBoolean(ISGODMODEENABLE,false);
    }

    public void putGodmodeValue(boolean value){
        putBoolean(ISGODMODEENABLE,value);
    }

    public boolean isSoundEnable(){
        String value = getString("issoundon", "1");
        return value.equals("1");
    }

    public void StoreFCM(String token) {
    }

    public String getSpin_remaining() {
        return SharePref.getInstance().getString(spin_remaining);
    }

    public void setSpin_remaining(String value) {
         SharePref.getInstance().putString(spin_remaining,value);
    }

    public boolean isApplicationStage() {
        return SharePref.getInstance().getString(CASHFREE_STAGE).equalsIgnoreCase("TEST");
    }

    public void setApplicationStage(String value) {
        SharePref.getInstance().putString(CASHFREE_STAGE,value);
    }


    public String getUSER_CATEGORY() {
        return SharePref.getInstance().getString(USER_CATEGORY);
    }

    public void setUSER_CATEGORY(String value) {
        SharePref.getInstance().putString(USER_CATEGORY,value);
    }
}
