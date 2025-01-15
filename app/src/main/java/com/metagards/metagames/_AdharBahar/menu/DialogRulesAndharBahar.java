package com.metagards.metagames._AdharBahar.menu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;

import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;

public class DialogRulesAndharBahar {

    private  Context context;
    Callback callback;
    private static DialogRulesAndharBahar mInstance;

    int[] rummy_rules = {
            R.drawable.ic_ab_rule1,
            R.drawable.ic_ab_rule2,
    };

    public static DialogRulesAndharBahar getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogRulesAndharBahar.class) {
                if (null == mInstance) {
                    mInstance = new DialogRulesAndharBahar(context);
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
    public DialogRulesAndharBahar init(Context context) {
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

    LinearLayout lnrRuleslist ;
    private DialogRulesAndharBahar initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_rulesdragontiger);

        lnrRuleslist = dialog.findViewById(R.id.lnrRuleslist);
        lnrRuleslist.removeAllViews();
        for (int item: rummy_rules) {
            addRulesonView(item);
        }


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

    public DialogRulesAndharBahar(Context context) {
        this.context = context;
    }

    public DialogRulesAndharBahar() {
    }
    Dialog dialog;

    public DialogRulesAndharBahar show() {

        dialog.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//Hello
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
