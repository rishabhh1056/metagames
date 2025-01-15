package com.metagards.metagames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.HomescreenModel;

import java.util.ArrayList;
import java.util.List;

public class HomegameListAdapter extends RecyclerView.Adapter<HomegameListAdapter.myholder> implements Filterable {
    List<HomescreenModel> arrayList;
    List<HomescreenModel> seachList;
    Context context;
    OnItemClickListener callback;
    private int lastPosition =-1;

    public HomegameListAdapter(Context context) {
        this.context = context;
    }

    public void setCallback(OnItemClickListener callback) {
        this.callback = callback;
    }

    public void setArrayList(List<HomescreenModel> arrayList)
    {
        this.arrayList = arrayList;
        seachList = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homeicon,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        HomescreenModel model = arrayList.get(position);
        if(model != null)
            holder.bind(model);
        // Here you apply the animation when the view is bound
       setAnimation(holder.itemView, position);
      //  setFadeAnimation(holder.itemView);




    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else if ( position < lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder{
        public myholder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(HomescreenModel model) {
//            Log.d("ListAdapteritem--" , arrayList.size() + "");
//            for (int i = 0 ; i<arrayList.size() ; i++)
//            {
//                Log.d("Homegameadapter" , arrayList.get(i).getItemName());
//            }


            ImageView ivHomegamecard = itemView.findViewById(R.id.ivHomegamecard);
//            Glide.with(context)
//                    .load(Functions.getDrawable(context,model.getItemImage()))
//                    .into(ivHomegamecard);
//            Drawable temp =

            ivHomegamecard.setImageDrawable(Functions.getDrawable(context,model.getItemImage()));
            Glide.with(context)
                    .load(Functions.getDrawable(context,model.getItemImage()))
                    .into(ivHomegamecard);
//            ivHomegamecard.setBackgroundResource(model.getItemImage());
          /*  if(model.getId() == 10)
            {
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
                Log.d("AdapterHome" , "insidecondtion");
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
            }
            else if(model.getId() == 13)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 14)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 12)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 21)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 8)
            {
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 9)
            {
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 16)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 4)
            {
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 22)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 19) {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning()) {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 20)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 18)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 5)
            {
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 15)
            {
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
            }
            else if(model.getId() == 7)
            {
              *//*  ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 6)
            {
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 7)
            {
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 2)
            {
               *//* ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 17)
            {
                *//*ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 11)
            {
                *//*ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });*//*
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
            }
            else if(model.getId() == 1)
            {
                Glide.with(context)
                        .load(Functions.getDrawable(context,model.getItemImage()))
                        .into(ivHomegamecard);
*//*
                ivHomegamecard.post(new Runnable() {
                    @Override
                    public void run() {

                        AnimationDrawable spinnerAnim = (AnimationDrawable) ivHomegamecard.getDrawable();
                        if (!spinnerAnim.isRunning())
                        {
                            spinnerAnim.start();
                        }
                    }
                });
*//*
            }
            else{
                Glide.with(context)
                    .load(Functions.getDrawable(context,model.getItemImage()))
                    .into(ivHomegamecard);
            }*/

            ivHomegamecard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.Response(v,getAdapterPosition(),model.getItemTag());
                }
            });
        }
    }

    // that function will filter the result
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    arrayList = seachList;
                } else {
                    ArrayList<HomescreenModel> filteredList = new ArrayList<>();
                    for (HomescreenModel row : seachList) {
                        String selectedvalue = charString.toLowerCase();
                        String listvalue = row.getItemType().toLowerCase();
                        Functions.LOGD("HomeGameList","listvalue : "+listvalue);

                        if(selectedvalue.contains("all") || selectedvalue.contains(listvalue))
                        {
                            filteredList.add(row);
                        }
                    }

                    arrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList = (ArrayList<HomescreenModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public void updateList(List<HomescreenModel> list){
        arrayList = list;
        notifyDataSetChanged();
    }
}
