package com.first_player_games.Others;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.first_player_games.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Functions {
    public static Animation AnimationListner(Context context,int url_animation ,final Callback callback){

        Animation animation =  AnimationUtils.loadAnimation(context,
                url_animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.Responce("end","",null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return animation;
    }

    public static String formateAmount(String amount){

        try {
            Float _amount = Float.parseFloat(amount);
            return NumberFormat.getNumberInstance(Locale.US).format(_amount);
        }
        catch (Exception e)
        {
            return amount;
        }

    }

    public static Dialog DialogInstance(Context context){
        return new Dialog(context);
    }

    public static void setDialogParams(Dialog mdialog){
        if(mdialog == null)
            return;

        mdialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Window window = mdialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public static boolean isActivityExist(Context context){

        return context != null && !((Activity)context).isFinishing() ? true : false;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }


    public static Drawable getDrawable(Context context, int drawable) {

        return context.getResources().getDrawable(drawable);
    }

    public static String getString(Context context,int string) {

        return context.getResources().getString(string);
    }


    public static int getColor(Context context,int color) {

        return context.getResources().getColor(color);
    }

    public static String getStringFromEdit(EditText editText) {

        return editText.getText().toString().trim();
    }

    public static boolean showToast(Context context,String message) {
        Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
        return false;
    }

    public static String makeFistLaterCaptial(String text) {

        if (text.length() > 0) {
            text = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();

            return text;
        }

        return "";
    }

    public static String convertnulltoZero(String tag)
    {

        if(tag != null && !tag.trim().equals("")&& !tag.trim().equalsIgnoreCase("null") && !tag.trim().equalsIgnoreCase("0"))
            return tag;

        return "0";
    }

    public static boolean checkisStringValid(String tag)
    {

        if(tag != null && !tag.trim().equals("")&& !tag.trim().equalsIgnoreCase("null") && !tag.trim().equalsIgnoreCase("0"))
            return true;

        return false;
    }

    public static boolean isStringValid(String tag)
    {

        if(tag != null && !tag.trim().equals("")&& !tag.trim().equalsIgnoreCase("null"))
            return true;

        return false;
    }


    private static final String MY_PREFS_NAME = "Login_data";

    private static final boolean isDebug = true;

    public static final int ANIMATION_SPEED = 1000;
    public static final int Home_Page_Animation = 500;

    public static boolean checkStringisValid(String text) {

        if (text != null && !text.equals("") && !text.equals("null") && !text.equals("0")) {
            return true;
        }

        return false;
    }



    public static int GetResourcePath(String imagename,Context context){

        String uri1 = "@drawable/" + imagename.toLowerCase();  // where myresource " +
        int imageResource = context.getResources().getIdentifier(uri1, null,
                context.getPackageName());

        return imageResource;
    }

    public static void SetBackgroundImageAsDisplaySize(Activity context, RelativeLayout relativeLayout,int drawable){

        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(),drawable),size.x,size.y,true);

        ImageView imageview = new ImageView(context);
        RelativeLayout relativelayout = relativeLayout;
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Add image path from drawable folder.
        imageview.setImageBitmap(bmp);
        imageview.setLayoutParams(params);

        if(relativeLayout != null)
            relativelayout.addView(imageview);

    }

    public static AnimatorSet getViewToViewScalingAnimator(final RelativeLayout parentView,
                                                           final View viewToAnimate,
                                                           final Rect fromViewRect,
                                                           final Rect toViewRect,
                                                           final long duration,
                                                           final long startDelay) {
        // get all coordinates at once
        final Rect parentViewRect = new Rect(), viewToAnimateRect = new Rect();
        parentView.getGlobalVisibleRect(parentViewRect);
        viewToAnimate.getGlobalVisibleRect(viewToAnimateRect);

        viewToAnimate.setScaleX(1f);
        viewToAnimate.setScaleY(1f);

        // rescaling of the object on X-axis
        final ValueAnimator valueAnimatorWidth = ValueAnimator.ofInt(fromViewRect.width(), toViewRect.width());
        valueAnimatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newWidth = (int) valueAnimatorWidth.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();

                lp.width = newWidth;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // rescaling of the object on Y-axis
        final ValueAnimator valueAnimatorHeight = ValueAnimator.ofInt(fromViewRect.height(), toViewRect.height());
        valueAnimatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newHeight = (int) valueAnimatorHeight.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();
                lp.height = newHeight;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // moving of the object on X-axis
        ObjectAnimator translateAnimatorX = ObjectAnimator.ofFloat(viewToAnimate, "X", fromViewRect.left - parentViewRect.left, toViewRect.left - parentViewRect.left);

        // moving of the object on Y-axis
        ObjectAnimator translateAnimatorY = ObjectAnimator.ofFloat(viewToAnimate, "Y", fromViewRect.top - parentViewRect.top, toViewRect.top - parentViewRect.top);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator(1f));
        animatorSet.setDuration(duration); // can be decoupled for each animator separately
        animatorSet.setStartDelay(startDelay); // can be decoupled for each animator separately
        animatorSet.playTogether(valueAnimatorWidth, valueAnimatorHeight, translateAnimatorX, translateAnimatorY);

        return animatorSet;
    }

    public static void LOGE(String classname, String message) {

        try {
            if (message.length() > 4000) {

                Log.e("" + classname, "" + message.substring(0, 4000));

                LOGE(classname, message.substring(4000));
            } else
                Log.e("" + classname, "" + message);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    public static void LOGD(String classname, String message) {


        try {
            if (message.length() > 4000) {

                Log.e("" + classname, "" + message.substring(0, 4000));

                LOGD(classname, message.substring(4000));
            } else
                Log.d("" + classname, "" + message);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }


    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

//        Funtions.LOGE("MainActivity","DP : "+dp+" = "+px);

        return px;
    }
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static boolean check_permissions(Activity context) {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(PERMISSIONS, 2);
            }
        }else {

            return true;
        }

        return false;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String Bitmap_to_base64(Activity activity,Bitmap imagebitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos .toByteArray();
        String base64= Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

    public static void openWhatsappContact(Context context,String number) {
        String appPackage="";
        if (appInstalledOrNot(context, "com.whatsapp")) {
            appPackage = "com.whatsapp";
            //do ...
        }
        else
        if (appInstalledOrNot(context, "com.whatsapp.w4b")) {
            appPackage = "com.whatsapp.w4b";
            //do ...
        }
        else {
            Functions.showToast(context, "whatsApp is not installed");
        }

        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage(appPackage);
        context.startActivity(Intent.createChooser(i, ""));
    }

    public static boolean appInstalledOrNot(Context context,String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public static View CreateDynamicViews(int layout, ViewGroup addingview, Context context){
        View view  = LayoutInflater.from(context).inflate(layout,null);

        addingview.addView(view);
        return view;
    }

    public static String getStringFromTextView(TextView title) {
        return title.getText().toString().trim();
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static boolean isActivityVisible(Context context) {
        return context != null && !((Activity)context).isFinishing();
    }

    public static String[] LOCATION_PERMISSIONS;

    public static boolean check_location_permissions(Context context) {

        LOCATION_PERMISSIONS = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        if (!hasPermissions(context, LOCATION_PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                context.requestPermissions(PERMISSIONS, 123);
            }
        } else {

            return true;
        }

        return false;
    }
}
