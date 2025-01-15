package com.metagards.metagames._Aviator;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;


import com.metagards.metagames.R;

public class LoadingDialog {

        Dialog dialog;

        public void showDialog(Activity activity){
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.common_loading);
            dialog.show();

        }

        public void dismiss() {
            if(dialog != null)
            {
                dialog.dismiss();
            }
        }
    }
