package com.metagards.metagames.Comman;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;

public class DialogRestrictUser {
    
    Context context;
    Callback callback;

    private volatile static DialogRestrictUser mInstance;


    public static DialogRestrictUser getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogRestrictUser.class) {
                if (null == mInstance) {
                    mInstance = new DialogRestrictUser();
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
    public DialogRestrictUser init(Context context) {
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

        return this;
    }

    Dialog dialog;
    DialogRestrictUser initDialog(){
        if(dialog == null)
        {
            dialog = Functions.DialogInstance(context);
            dialog.setContentView(R.layout.dialog_restrict);
            dialog.setTitle("Title...");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finishAffinity();
                dismiss();
            }
        });

        dialog.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finishAffinity();
                dismiss();
            }
        });

        return this;
    }

    public DialogRestrictUser setTitle(String title){

        TextView tvHeading = dialog.findViewById(R.id.tvHeading);
        tvHeading.setText(""+title);
        return this;
    }

    public DialogRestrictUser setDescription(String description){

        TextView tvDesription = dialog.findViewById(R.id.tvDescription);
        tvDesription.setText(""+description);

        return this;
    }

    public void show() {
        // custom dialog
           dialog.setCancelable(false);
        dialog.show();
        Functions.setDialogParams(dialog);
    }
    
    public void dismiss(){
        if(dialog != null) 
            dialog.dismiss();
        
    }

}
