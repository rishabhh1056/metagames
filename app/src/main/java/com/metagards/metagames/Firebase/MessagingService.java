package com.metagards.metagames.Firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames._TeenPatti.PublicTable_New;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    public final String TAG = "MessagingService";
    private String table_id = "";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        if (getApplicationContext() != null)
            SharePref.getInstance().init(getApplicationContext());
        if (token != null) {
            SharePref.getInstance().StoreFCM(token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        SharePref.getInstance().init(getApplicationContext());

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("MyNotification","MyNotification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);
        }

        String title = "";
        String msg = "";

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> params = remoteMessage.getData();

            try {
//                JSONObject object = new JSONObject(params);

                JSONObject object = new JSONObject(remoteMessage.getData().toString());
                Functions.LOGE(TAG,""+object);
                String objectInner = object.optString("message");
                JSONObject objectMSg = new JSONObject(objectInner);

                title = objectMSg.optString("title");


                if (objectMSg.has("msg"))
                    msg = objectMSg.optString("msg");


                if (msg == null || msg.equals("null"))
                    msg = "";


                if (objectMSg.has("table_id")) {
                    table_id = objectMSg.getString("table_id");
                }


                showNotification( title, msg);

            }
            catch (JSONException e) {
                e.printStackTrace();

                //log data
                Log.e("TAG", "onMessageReceived: "
                        +remoteMessage.getData().get("title")
                        +remoteMessage.getData().get("description")
                        +remoteMessage.getData().get("body"));

//                type = remoteMessage.getData().get("type");

                showNotification(remoteMessage.getData().get("title")
                        ,remoteMessage.getData().get("description"));

            }

//            Intent intent = new Intent(Variables.REFRESH_DESHBOARD_LIST);
//            // You can also include some extra data.
//            intent.putExtra("type", ""+type);
//            intent.putExtra("appointment_id", ""+appointment_id);
//            intent.putExtra("appointment_count",appointment_count);
//            SharePref.isUserLogout = false;
//            if(type.equalsIgnoreCase(Variables.DEVICE_LOGOUT))
//                SharePref.isUserLogout = true;
//
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        }
        else {

            title = remoteMessage.getNotification().getTitle();
            msg = remoteMessage.getNotification().getBody();


            if (title != null && !title.equals(""))
                showNotification( title, msg);


        }

        super.onMessageReceived(remoteMessage);
    }

    private void showNotification(String title,String desc) {

        final String packageName = this.getPackageName();

        try {

//            Sound sound = new Sound();
//            if(ringTone > 0)
//                sound.PlaySong(notification,false,getApplicationContext());

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("signature", "");

        Intent notificationIntent;
        if (table_id.length() > 0){

            notificationIntent = new Intent(this, PublicTable_New.class);
            notificationIntent.putExtra("table_id",table_id);
        }else{

            notificationIntent = new Intent(this, Homepage.class);
        }

        //create pending intent
        PendingIntent contentIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity
                    (this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            contentIntent = PendingIntent.getActivity
                    (this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        //create builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"MyNotification")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(desc)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(desc))
//                .setSound(notification, STREAM_ALARM)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(123,builder.build());
    }

    private int getRingType(){

        return 0;
    }
}
