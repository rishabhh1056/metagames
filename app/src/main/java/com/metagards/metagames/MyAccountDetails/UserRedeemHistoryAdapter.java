package com.metagards.metagames.MyAccountDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.R;
import com.metagards.metagames.RedeemCoins.RedeemModel;
import com.metagards.metagames.Utils.Variables;

import java.util.ArrayList;

public class UserRedeemHistoryAdapter extends RecyclerView.Adapter<UserRedeemHistoryAdapter.myholder> {

    Context homepage;
    ArrayList<RedeemModel> winnigmodelArrayList;

    public UserRedeemHistoryAdapter(Context homepage, ArrayList<RedeemModel> winnigmodelArrayList) {
        this.homepage = homepage;
        this.winnigmodelArrayList = winnigmodelArrayList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(homepage).inflate(R.layout.redeem_itemview, parent, false);

        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {

        //Picasso.with(homepage).load(Const.IMGAE_PATH + winnigmodelArrayList.get(position).getUserimage()).into(holder.imgprofile);

        View view = holder.itemView;
        RedeemModel model = winnigmodelArrayList.get(position);

        getTextView(view,R.id.tvSerial).setText(""+(position + 1));
        getTextView(view,R.id.tvDates).setText(""+model.getUpdated_date());
        getTextView(view,R.id.tvMobile).setText(""+model.getMobile());
        getTextView(view,R.id.tvCoins).setText(""+ Variables.CURRENCY_SYMBOL+model.getCoin());
        getView(view,R.id.lnrStatus).setVisibility(View.VISIBLE);

        String status = "Pending";
        if(model.getStatus().equalsIgnoreCase("1"))
            status = "Approved";
        else if(model.getStatus().equalsIgnoreCase("2"))
            status = "Rejected";

        getTextView(view,R.id.tvStatus).setText(""+status);



    }

    private TextView getTextView(View view,int id){
        return ((TextView) view.findViewById(id));
    }

    private View getView(View view,int id){
        return ((View) view.findViewById(id));
    }

    @Override
    public int getItemCount() {
        return winnigmodelArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder {

        public myholder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
