package com.first_player_games.Others;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonUtils {


    public static String getCity(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedpreferences.getString("city","nothing");
    }

    public static String getDob(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedpreferences.getString("dob","nothing");
    }

    public static String getName(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedpreferences.getString("name","nothing");
    }

    public static boolean getSoundSetting(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedpreferences.getString("issoundon","1") == "1";
    }

    public static String getPaymentsName(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("payouts", Context.MODE_PRIVATE);
        return sharedpreferences.getString("name","");
    }

    public static String getPaymentsEmail(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("payouts", Context.MODE_PRIVATE);
        return sharedpreferences.getString("email","");
    }
    public static String getPaymentsPhone(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("payouts", Context.MODE_PRIVATE);
        return sharedpreferences.getString("phone","");
    }
    public static String getPaymentsUPI(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("payouts", Context.MODE_PRIVATE);
        return sharedpreferences.getString("upi","");
    }
}
