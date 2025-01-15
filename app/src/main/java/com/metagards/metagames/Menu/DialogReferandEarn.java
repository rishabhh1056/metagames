package com.metagards.metagames.Menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.Utils.Variables;

public class DialogReferandEarn {

    Context context;
    Callback callback;
    private static DialogReferandEarn mInstance;


    public static DialogReferandEarn getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogReferandEarn.class) {
                if (null == mInstance) {
                    mInstance = new DialogReferandEarn(context);
                }
            }
        }

        if(mInstance != null)
            mInstance.init(context);

        return mInstance;
    }

    /**
     * initialization of context, use only first time later it will use this again and again
     *
     * @param context app context: first time
     */
    public DialogReferandEarn init(Context context) {
        try {

            if (context != null) {
                this.context = context;
                initDialog();
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return mInstance;
    }

    String referral_code;
    private DialogReferandEarn initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.custom_dialog_invite);
        dialog.setTitle("Title...");
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclosetop);


        referral_code = SharePref.getInstance().getString("referal_code");

        TextView txtchipsbelow = (TextView) dialog.findViewById(R.id.txtchipsbelow);
        TextView txtheader = (TextView) dialog.findViewById(R.id.txtheader);
        txtheader.setText("SHARE");
        TextView tvInviteCoins = (TextView) dialog.findViewById(R.id.tvInviteCoins);

        tvInviteCoins.setText(Variables.CURRENCY_SYMBOL+ SharePref.getInstance().getString(SharePref.referral_amount));
        txtchipsbelow.setText(Variables.CURRENCY_SYMBOL+SharePref.getInstance().getString(SharePref.referral_amount));

        TextView txtReferalcode = (TextView) dialog.findViewById(R.id.txtReferalcode);
        txtReferalcode.setText(referral_code);
        TextView txtAnd = (TextView) dialog.findViewById(R.id.txtAnd);
        ImageView imgfb = (ImageView) dialog.findViewById(R.id.imgfb);
        ImageView imgwhats = (ImageView) dialog.findViewById(R.id.imgwhats);
        ImageView imgmail = (ImageView) dialog.findViewById(R.id.imgmail);
        imgmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.google.android.gm");



                whatsappIntent.putExtra(Intent.EXTRA_TEXT, getReferralMessage());
                try {
                    context.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });

        imgwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, getReferralMessage());
                try {
                    context.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });

        imgfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.facebook.katana");

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, getReferralMessage());

                try {
                    context.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });
        TextView txtyourfrind = (TextView) dialog.findViewById(R.id.txtyourfrind);
        TextView txtFooter = (TextView) dialog.findViewById(R.id.txtFooter);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        return mInstance;
    }


    public DialogReferandEarn(Context context) {
        this.context = context;
    }

    public DialogReferandEarn() {
    }
    Dialog dialog;

    public DialogReferandEarn show() {


        dialog.setCancelable(true);
        dialog.show();
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return mInstance;
    }

    private String getReferralMessage() {
        final String appPackageName = context.getPackageName();
        String applink = Variables.invite_link;

        if(applink.contains("google"))
            applink = applink + appPackageName;

        String refferal_link = SharePref.getInstance().getString(SharePref.referral_link);
        if(Functions.checkisStringValid(refferal_link))
            applink = refferal_link;

        String shareMessage = context.getString(R.string.share_message);

        String apiMessgae = SharePref.getInstance().getString(SharePref.SHARE_APP_TEXT);

//        if(Functions.checkisStringValid(apiMessgae))
//            shareMessage  = apiMessgae;

        String referral_message = shareMessage+ " Use the referral code    " +
                referral_code + " Download the App now. Link:-" +applink;

        return referral_message;
    }


    public void dismiss(){
        if(dialog != null)
            dialog.dismiss();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

}
