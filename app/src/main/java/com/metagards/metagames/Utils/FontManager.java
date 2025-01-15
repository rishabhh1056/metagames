package com.metagards.metagames.Utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {
    public static final String ROOT = "fonts/",
//            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";
           Helvetica = ROOT+ "Helvetica.ttf",
          Marker_Felt = ROOT+ "Marker_Felt.ttf";
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
