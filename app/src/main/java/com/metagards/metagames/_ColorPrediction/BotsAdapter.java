package com.metagards.metagames._ColorPrediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.R;

import java.util.ArrayList;

public class BotsAdapter extends RecyclerView.Adapter<BotsAdapter.ViewHolder>{

    Context context;
    ArrayList<Bots_list> list;
    String entry_type="";

    public BotsAdapter(Context context, ArrayList<Bots_list> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.item_bots, parent, false);
//        listItem.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Bots_list bots_list = list.get(position);

        holder.tv_name.setText(bots_list.getName());
        holder.txt_wallet.setText("₹ "+bots_list.getCoin());

        Glide.with(context).load(Const.IMGAE_PATH + bots_list.getAvatar()).into(holder.img_profile);
        holder.img_profile.animate().rotationYBy(180);

    }

//    @Override
//    public int getItemCount() {
//        return list.size();
//    }

    @Override
    public int getItemCount() {
        if (list.size()>3){
            return 3;
        }
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView tv_name,txt_wallet,tv_c_name,tv_punch_in;
       ImageView img_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.txt_name);
            txt_wallet=itemView.findViewById(R.id.txt_wallet);
            img_profile=itemView.findViewById(R.id.img_profile);

        }
    }
}