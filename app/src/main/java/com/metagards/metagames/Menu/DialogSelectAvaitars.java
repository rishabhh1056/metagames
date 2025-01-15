package com.metagards.metagames.Menu;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogSelectAvaitars {

    Context context;

    private volatile static DialogSelectAvaitars mInstance;
    Callback callback;
    String selected_image = "";
    public static DialogSelectAvaitars getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogSelectAvaitars.class) {
                if (null == mInstance) {
                    mInstance = new DialogSelectAvaitars();
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
    public DialogSelectAvaitars init(Context context) {
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
    

    Dialog dialog;
    ArrayList<String> arrayList = new ArrayList<>();
    private DialogSelectAvaitars initDialog(){
        dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_select_avaitars);
        dialog.setTitle("Title...");

        RecyclerView recylerview = dialog.findViewById(R.id.recylerview);
        recylerview.setLayoutManager(new GridLayoutManager(context,4));
        TextView tv_heading = dialog.findViewById(R.id.txtheader);
        tv_heading.setText("Select Avatar");

        try {
            arrayList.clear();
            JSONArray jsonArray = new JSONArray(SharePref.getInstance().getString(SharePref.avator));
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(String.valueOf(jsonArray.get(i)));
            }

            recylerview.setAdapter(new avaitorListAdapter());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnconfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Functions.isStringValid(selected_image))
                {
                    UserUpdateProfile(selected_image);
                    dialog.dismiss();
                }

            }
        });

        return mInstance;
    }
    public DialogSelectAvaitars show() {
        dialog.show();
        Functions.setDialogParams(dialog);
        Functions.setDialogParams(dialog);

        return mInstance;
    }
    
    public DialogSelectAvaitars dismiss(){
        dialog.dismiss();
        return mInstance;
    }

    private class avaitorListAdapter extends RecyclerView.Adapter<avaitorListAdapter.myholder>
    {
        int select_position = -1;
        @NonNull
        @Override
        public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itm_avaitor,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull myholder holder, int position) {
            String model = arrayList.get(position);
            if(model != null)
                holder.bind(model);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class myholder extends RecyclerView.ViewHolder{
            public myholder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(String model) {
                int position = getAdapterPosition();
                View view = itemView;
                ImageView ivUserAvaitor = view.findViewById(R.id.ivUserAvaitor);
                RelativeLayout rltSelectedview = view.findViewById(R.id.rltSelectedview);
                rltSelectedview.setVisibility(
                        position == select_position ? View.VISIBLE : View.GONE
                );
                Glide.with(context).load(Const.IMGAE_PATH + model).into(ivUserAvaitor);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected_image = model;
                        select_position = position;
                        notifyDataSetChanged();
                    }
                });
            }


        }
    }

    private void UserUpdateProfile(String image_name) {
        HashMap params = new HashMap();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("avatar",""+image_name);
        params.put("token", prefs.getString("token", ""));

        ApiRequest.Call_Api(context, Const.USER_UPDATE, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    if (code.equalsIgnoreCase("200")) {


                        if(callback != null)
                            callback.Responce("update","",null);

                        dismiss();
                    } else {
                        if (jsonObject.has("message")) {
                            String message = jsonObject.getString("message");
                            Functions.showToast(context, message);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public DialogSelectAvaitars returnCallback(Callback callback){
        this.callback = callback;
        return mInstance;
    }


}
