package com.metagards.metagames.Details.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Details.GameDetailsModel;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;

import java.util.List;

public class GameDetailsAdapter extends RecyclerView.Adapter<GameDetailsAdapter.holder> {

    Context context;
    List<GameDetailsModel> arrayList;
    OnItemClickListener callback;
    int clickPosition = -1;

    public void onItemSelectListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    public GameDetailsAdapter(Context context) {
        this.context = context;
    }

    public void setArrayList(List<GameDetailsModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gamesdetails,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        GameDetailsModel model = arrayList.get(position);
        if(model != null){
            holder.bind(model);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }

    class holder extends RecyclerView.ViewHolder{
        public holder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(GameDetailsModel model) {
            int position = getAdapterPosition();
            model.setSelected(false);
            if(clickPosition == position)
                model.setSelected(!model.isSelected());

            TextView tvHeading = itemView.findViewById(R.id.tvHeading);
            ImageView ivIcon = itemView.findViewById(R.id.ivIcon);
            tvHeading.setText(""+model.getTitle());
            ivIcon.setImageDrawable(Functions.getDrawable(context,model.getImage()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPosition = position;
                    callback.Response(v,position,model);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
