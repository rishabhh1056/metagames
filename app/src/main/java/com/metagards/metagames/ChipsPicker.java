package com.metagards.metagames;

public class ChipsPicker {

    private volatile static ChipsPicker mInstance;

    public static ChipsPicker getInstance(){

        if(null == mInstance)
        {
            synchronized (ChipsPicker.class)
            {
                if(null == mInstance)
                {
                    mInstance = new ChipsPicker();
                }
            }
        }

        return mInstance;
    }

    private int[] chipslist = {
            /*R.drawable.ic_dt_chips,*/R.drawable.coin_10
            ,R.drawable.coin_50,R.drawable.coin_100,R.drawable.coin_1000,R.drawable.coin_5000
//            ,R.drawable.ic_chip_6,R.drawable.ic_chip_7
    };

    private int currentColorIndex = 0;

    public int getChip(){
        currentColorIndex = (currentColorIndex + 1) % chipslist.length;
        return chipslist[currentColorIndex];
    }

}
