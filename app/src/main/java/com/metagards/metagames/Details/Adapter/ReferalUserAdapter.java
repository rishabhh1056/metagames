package com.metagards.metagames.Details.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Details.Menu.ReferralModel;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;

import java.util.ArrayList;

public class ReferalUserAdapter extends RecyclerView.Adapter<ReferalUserAdapter.holder> {

    Context context;
    ArrayList<ReferralModel> myWinnigmodelArrayList;
    OnItemClickListener callback;
    int clickPosition = -1;

    public ReferalUserAdapter(Context context, ArrayList<ReferralModel> myWinnigmodelArrayList) {

        this.context = context;
        this.myWinnigmodelArrayList = myWinnigmodelArrayList;

    }
    public void onItemSelectListener(OnItemClickListener callback) {
        this.callback = callback;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.referaluser_iteamview,parent,false));

    }



//    @Override
//    public void onBindViewHolder(@NonNull holder holder, int position) {
//        ReferralModel model = myWinnigmodelArrayList.get(position);
//        if(model != null){
//            ReferalUserAdapter.holder.bi
//        }
//        SharedPreferences prefs = context.getSharedPreferences("Login_data", MODE_PRIVATE);
//        View view = holder.itemView;
//        ReferralModel myWinnigmodel = myWinnigmodelArrayList.get(position);
//        getTextView(view,R.id.tvSerial).setText(""+(position + 1));
//        getTextView(view,R.id.tvAddedDate).setText(""+(myWinnigmodelArrayList.get(position).updated_date));
//        getTextView(view,R.id.tvNanme).setText(""+myWinnigmodelArrayList.get(position).name);
//        getTextView(view,R.id.txtammount).setText(""+myWinnigmodelArrayList.get(position).coin);
//
//
//
//    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        ReferralModel model = myWinnigmodelArrayList.get(position);
        if(model != null){
            holder.bind(model);
        }

    }
    private TextView getTextView(View view,int id){
        return ((TextView) view.findViewById(id));
    }


    @Override
    public int getItemCount() {
        return myWinnigmodelArrayList.size();
    }

    class holder extends RecyclerView.ViewHolder{


        public holder(@NonNull View itemView) {
            super(itemView);
        }


        public void bind(ReferralModel model) {
            int position = getBindingAdapterPosition();
            SharedPreferences prefs = context.getSharedPreferences("Login_data", MODE_PRIVATE);


            View view = holder.this.itemView;
            getTextView(view,R.id.tvSerial).setText(""+(position + 1));
            getTextView(view,R.id.tvAddedDate).setText(""+model.updated_date);
            getTextView(view,R.id.tvNanme).setText(""+model.name);
            getTextView(view,R.id.txtammount).setText(""+model.coin);
            getTextView(view,R.id.txtcount).setText(""+model.count);
            getTextView(view,R.id.tvEmail).setText(""+model.email);
            getTextView(view,R.id.tvReferalid).setText(""+model.referred_user_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.Response(v,position,model);
                }
            });
        }

    }

}
