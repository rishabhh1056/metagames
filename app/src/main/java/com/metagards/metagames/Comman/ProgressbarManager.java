package com.metagards.metagames.Comman;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.metagards.metagames.R;


public class ProgressbarManager extends Dialog {

    private ProgressBar progressBar;

    public ProgressbarManager(Context context) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        progressBar = new ProgressBar(context);
        progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.jz_loading));

        layout.addView(progressBar, params);
        addContentView(layout, params);
    }

//    private ImageView imageView;
//
//    public ProgressbarManager(Context context) {
//        super(context, R.style.TransparentProgressDialog);
//        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
//        wlmp.gravity = Gravity.CENTER;
//        getWindow().setAttributes(wlmp);
//        setTitle(null);
//        setCancelable(false);
//        setOnCancelListener(null);
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setBackgroundColor(Color.TRANSPARENT);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//
//        imageView = new ImageView(context);
//        Glide.with(context)
//                .load(R.raw.loading)
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
//                .into(new DrawableImageViewTarget(imageView));
//
//        layout.addView(imageView, params);
//        addContentView(layout, params);
//    }
}