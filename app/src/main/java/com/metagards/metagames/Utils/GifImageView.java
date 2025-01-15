package com.metagards.metagames.Utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class GifImageView extends androidx.appcompat.widget.AppCompatImageView {
    
    public GifImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GifImageView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public GifImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        String android_schemas = "http://schemas.android.com/apk/res/android";
        int srcId = attrs.getAttributeResourceValue(android_schemas, "src", -1);

        if(srcId <= 0)
        {
            srcId = attrs.getAttributeResourceValue(android_schemas, "background", -1);
        }

        setBackground(null);
        Glide.with(getContext())
                .load(srcId)
                .into(this);

    }


}
