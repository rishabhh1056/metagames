package com.metagards.metagames._AdharBahar.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames._AdharBahar.Andhar_Bahar_NewUI;
import com.metagards.metagames._AdharBahar.GameActivity;
import com.metagards.metagames._AdharBahar.Model.GameModel;
import com.metagards.metagames.R;

import java.util.ArrayList;

public class GamesListAdapter extends RecyclerView.Adapter<GamesListAdapter.myholder> {
    Activity context;
    ArrayList<GameModel> gameModelArrayList;

    public GamesListAdapter(Activity context, ArrayList<GameModel> gameModelArrayList) {
        this.context = context;
        this.gameModelArrayList = gameModelArrayList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gamelist,parent,false);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, final int position) {
        holder.txtroomid.setText("Room "+gameModelArrayList.get(position).getId());
        holder.txtmin.setText(""+gameModelArrayList.get(position).getMin_coin());
        holder.txtmax.setText(""+gameModelArrayList.get(position).getMax_coin());
        holder.txtonline.setText(""+gameModelArrayList.get(position).getOnline());
        holder.itemView.findViewById(R.id.btnplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, GameActivity.class);
                if(Variables.HORIZONTAL_ANDHARBAHAR)
                    intent = new Intent(context, Andhar_Bahar_NewUI.class);

                intent.putExtra("room_id" ,gameModelArrayList.get(position).getId());
                intent.putExtra("min_coin" ,gameModelArrayList.get(position).getMin_coin());
                intent.putExtra("max_coin" ,gameModelArrayList.get(position).getMax_coin());
               context.startActivity(intent);
              //  context.startActivity(new Intent(context, TestAimationAcitivty.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return gameModelArrayList.size();
    }

    class  myholder extends RecyclerView.ViewHolder{
        TextView txtroomid,txtmin,txtmax,txtonline;
        public myholder(@NonNull View itemView) {
            super(itemView);

            txtroomid = itemView.findViewById(R.id.txtroomid);
            txtmin = itemView.findViewById(R.id.txtmin);
            txtmax = itemView.findViewById(R.id.txtmax);
            txtonline = itemView.findViewById(R.id.txtonline);



        }
    }
}
