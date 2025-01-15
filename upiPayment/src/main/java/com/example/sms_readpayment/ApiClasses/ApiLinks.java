package com.example.sms_readpayment.ApiClasses;

import android.content.Context;
import android.net.ConnectivityManager;

public class ApiLinks {

    public ApiLinks() {
    }

//    public static final String BASE_URL ="http://139.59.68.158/AllInOneDemo/api/UpiPayment/";
    public static final String BASE_URL ="https://upifreeway.com/api/UpiPayment/";
    public static final String Token="c7d3965d49d4a59b0da80e90646aee77548458b3377ba3c0fb43d5ff91d54ea28833080e3de6ebd4fde36e2fb7175cddaf5d8d018ac1467c3d15db21c11b6909";

    public static final String IMAGE_PATH="https://androappstech.com/wendor_dev/data/";
    public static final String API_BASE_URL =BASE_URL+"api/";

    public static final String initiate_payment=BASE_URL+"initiate_payment";
    public static final String update_payment=BASE_URL+"update_payment";
    public static final String check_status=BASE_URL+"check_status";
    public static final String update_utr=BASE_URL+"update_utr";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
