package com.metagards.metagames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metagards.metagames.Interface.itemClick;
import com.metagards.metagames.ApiClasses.Const;

import com.metagards.metagames.R;
import com.metagards.metagames.model.GiftModel;

import java.util.ArrayList;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.myholder> {


    Context context;
    ArrayList<GiftModel> giftModelArrayList;
    itemClick onGitsClick;

    public GiftsAdapter(Context context, ArrayList<GiftModel> giftModelArrayList, itemClick onGitsClick) {
        this.context = context;
        this.giftModelArrayList = giftModelArrayList;
        this.onGitsClick = onGitsClick;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(context).inflate(R.layout.gift_itemview,parent,false);

        return new myholder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, final int position) {

        holder.txtgiftname.setText(""+giftModelArrayList.get(position).getName());
        ((TextView)holder.itemView.findViewById(R.id.tvAmount)).setText(""+giftModelArrayList.get(position).getCoin());
        //Picasso.with(context).load(Const.IMGAE_PATH + giftModelArrayList.get(position).getImage()).into(holder.imggift);

        Glide.with(context)
//
                .load(Const.IMGAE_PATH + giftModelArrayList.get(position).getImage())
//                .skipMemoryCache(true)
//                .apply(RequestOptions.diskCacheStrategyOf( DiskCacheStrategy.RESOURCE)
//                        .placeholder(context.getResources().getDrawable(R.drawable.app_icon)).centerCrop())
                .into(holder.imggift);

        final GiftModel model = giftModelArrayList.get(position);

        holder.cd_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGitsClick.OnDailyClick(""+model.getId(),""+model.getCoin(),model.getImage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return giftModelArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        TextView txtgiftname;
        ImageView imggift;
        View cd_items;

        public myholder(@NonNull View itemView) {
            super(itemView);

            txtgiftname = itemView.findViewById(R.id.txtgiftname);
            imggift = itemView.findViewById(R.id.imggift);
            cd_items = itemView.findViewById(R.id.cd_items);

        }
    }

}
