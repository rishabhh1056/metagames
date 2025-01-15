package com.metagards.metagames;

import android.content.Context;
import android.content.Intent;

import com.metagards.metagames._TeenPatti.PublicTable_New;

class IntentHelper {

    private  volatile static IntentHelper mInstance;

    public static IntentHelper getInstance() {
        if (null == mInstance) {
            // To make thread safe
            synchronized (IntentHelper.class) {
                // check again as multiple treads ca reach above step
                if (null == mInstance) {
                    mInstance = new IntentHelper();
                }
            }
        }

        return mInstance;
    }
    
    Intent TeenpattiTableList(Context context){
        return new Intent(context, PublicTable_New.class);
    }
}
