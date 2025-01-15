package com.metagards.metagames.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;

import java.util.Locale;

public class LanguageUtils {

    public static boolean isLanguageSelected(Context context) {
        return getSelectedLanguage(context) != null;
    }

    public static Locale init(Activity context) {
        String selectedLangCode = getSelectedLanguageCode(context);
        if (selectedLangCode != null) {
            Locale locale = new Locale(selectedLangCode);
            Locale.setDefault(locale);
            Configuration config = context.getApplicationContext().getResources().getConfiguration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());


//            Locale locale = new Locale(selectedLangCode);
//            Locale.setDefault(locale);
//            Configuration configuration= new Configuration();
//            configuration.locale = locale;
//
//            context.getBaseContext().getResources().updateConfiguration(configuration,context.getBaseContext().getResources().getDisplayMetrics());

            return locale;
        }

        return null;
    }

    public static String getSelectedLanguage(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String lang = sharedPreferences.getString(SharePref.selected_languge, Variables.ENGLISH);

        return lang;
    }

    public static String getSelectedLanguageCode(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String lang = sharedPreferences.getString(SharePref.languge_code, "en");

        return lang;
    }

    public static void selectLanguage(Context context, String language, String language_code) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharePref.selected_languge, language);
        editor.putString(SharePref.languge_code, language_code);
        editor.apply();

        Locale locale = new Locale(language_code);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }


    //

    private static Locale sLocale;

    public static void setLocale(Context context) {
        Locale locale = new Locale(getSelectedLanguageCode(context));
        sLocale = locale;
        if(sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        if(sLocale != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration();
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }

    public static void updateConfig(Application app, Configuration configuration) {
        if (sLocale != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            // We must use the now-deprecated config.locale and res.updateConfiguration here,
            // because the replacements aren't available till API level 24 and 17 respectively.
            config.locale = sLocale;
            Resources res = app.getBaseContext().getResources();
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }


}
