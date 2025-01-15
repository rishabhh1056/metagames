package com.first_player_games.Others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

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
    private final String APPLICATION_STAGE ="cashfree_stage";
    public static final String PAYTM_MERCENT_ID ="paytm_mercent_id";

    public static final String PAYU_MERCENT_KEY ="payu_mercent_key";
    public static final String PAYTM_MERCHANT_SALT ="paytm_merchant_salt";


    public static final String isTeenpattiVisible ="isTeenpattiVisible";
    public static final String isPrivateVisible ="isPrivateVisible";
    public static final String isCustomVisible ="isCustomVisible";
    public static final String isRummyVisible ="isRummyVisible";
    public static final String isAndharBaharVisible ="isAndharBaharVisible";
    public static final String isDragonTigerVisible ="isDragonTigerVisible";
    public static final String isLoginWithPassword ="isLoginWithPassword";



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

    public static boolean getIsAndharBaharVisible() {
        return SharePref.getInstance().getBoolean(isAndharBaharVisible);
    }

    public static boolean getIsDragonTigerVisible() {
        return SharePref.getInstance().getBoolean(isDragonTigerVisible);
    }

    public static boolean getIsLoginWithPassword() {
        return SharePref.getInstance().getBoolean(isLoginWithPassword);
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
}
