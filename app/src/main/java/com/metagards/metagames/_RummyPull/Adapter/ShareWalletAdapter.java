package com.metagards.metagames._RummyPull.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;
import static com.metagards.metagames.Utils.Functions.getString;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.R;
import com.metagards.metagames.model.CardModel;

import java.util.ArrayList;

public class ShareWalletAdapter extends RecyclerView.Adapter<ShareWalletAdapter.myholder> {
    Context context;
    ArrayList<CardModel> arrayList;

    public ShareWalletAdapter(Context context, ArrayList<CardModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_declare, parent, false);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.itemView.findViewById(R.id.lnrWinnerStatus).setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.lnrCardsList).setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.lnrSerialno).setVisibility(View.VISIBLE);
        CardModel model = arrayList.get(position);
        View view = holder.itemView;
        getTextView(view, R.id.tvSerialnumber).setText("" + (position + 1));
        if (model.user_name != null && !model.user_name.equals("")) {
            getTextView(view, R.id.tv_username).setText("" + arrayList.get(position).user_name);
        } else {
            getTextView(view, R.id.tv_username).setText("" + arrayList.get(position).user_id);
        }
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if (model.user_id.equalsIgnoreCase(prefs.getString("user_id", ""))) {
            getTextView(view, R.id.tv_score).setText(getString(context, R.string.send_request));
        } else {
            getTextView(view, R.id.tv_score).setText("" + model.status);
        }
    }

    private TextView getTextView(View view, int id) {
        return ((TextView) view.findViewById(id));
    }

    private void ChangeTextViewColor(TextView textView, String colors) {
        if (colors.equalsIgnoreCase("white")) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.blackbg));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder {
        public myholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
