package com.metagards.metagames._CoinFlip;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;

public class DialogRulesHeadtail {

    private  Context context;
    Callback callback;
    private static DialogRulesHeadtail mInstance;

    int[] rummy_rules = {
            R.drawable.ic_ab_rule1,
            R.drawable.ic_ab_rule2,
    };

    public static DialogRulesHeadtail getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogRulesHeadtail.class) {
                if (null == mInstance) {
                    mInstance = new DialogRulesHeadtail(context);
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
    public DialogRulesHeadtail init(Context context) {
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

    TextView txtheader;
    LinearLayout lnrRuleslist ;
    private DialogRulesHeadtail initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_rules_head);

        lnrRuleslist = dialog.findViewById(R.id.lnrRuleslist);
//        lnrRuleslist.removeAllViews();
//        for (int item: rummy_rules) {
//            addRulesonView(item);
//        }
        txtheader = dialog.findViewById(R.id.txtheader);
        txtheader.setText("How To Play");


        return mInstance;
    }


    private void addRulesonView(int itemrules) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.dp300));
        imageView.setLayoutParams(layoutParams);
        imageView.setBackground(Functions.getDrawable(context,itemrules));
        lnrRuleslist.addView(imageView);
    }

    public DialogRulesHeadtail(Context context) {
        this.context = context;
    }

    public DialogRulesHeadtail() {
    }
    Dialog dialog;

    public DialogRulesHeadtail show() {

        dialog.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return mInstance;
    }

    public void dismiss(){
        if(dialog != null)
            dialog.dismiss();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

}
