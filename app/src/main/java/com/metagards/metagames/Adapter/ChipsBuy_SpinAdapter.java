package com.metagards.metagames.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Activity.BuyChipsDetails;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.Variables;
import com.metagards.metagames.model.ChipsBuyModel;

import java.util.ArrayList;

public class ChipsBuy_SpinAdapter extends RecyclerView.Adapter<ChipsBuy_SpinAdapter.ViewHolder> {
    Activity context;

    ArrayList<ChipsBuyModel> historyModelArrayList;
    float final_amt = 0;

    public ChipsBuy_SpinAdapter(Activity context, ArrayList<ChipsBuyModel> historyModelArrayList) {
        this.context = context;
        this.historyModelArrayList = historyModelArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chips_buy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChipsBuyModel model = historyModelArrayList.get(position);
        ((ViewHolder) holder).bind(model, position, historyModelArrayList, holder);


    }

    @Override
    public int getItemCount() {

        return historyModelArrayList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_discount_amt, txt_discount,
                txtAmount, txtproname;
        ImageView imgbuy, imalucky;
        RelativeLayout rel_layout;
        Typeface helvatikabold, helvatikanormal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgbuy = itemView.findViewById(R.id.imgbuy);
            imalucky = itemView.findViewById(R.id.imalucky);
            //txtDescription = itemView.findViewById(R.id.txtDescription);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtproname = itemView.findViewById(R.id.txtproname);
            txt_discount_amt = itemView.findViewById(R.id.txt_discount_amt);
            rel_layout = itemView.findViewById(R.id.rel_layout);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            helvatikabold = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Helvetica-Bold.ttf");


            //txt_date = itemView.findViewById(R.id.txt_date);

        }

        @SuppressLint("SetTextI18n")
        public void bind(final ChipsBuyModel model, int position, ArrayList<ChipsBuyModel> historyModelArrayList, ViewHolder holder) {
            int val = position % 2;


            // " +

            String uri2 = "";
            if (val == 1) {
                uri2 = "@drawable/bulkchipsgreen";  // where myresource

            } else {
                uri2 = "@drawable/bulkchipsred";  // where myresource

            }
            int imageResource2 = context.getResources().getIdentifier(uri2,
                    null,
                    context.getPackageName());

            ((ImageView) itemView.findViewById(R.id.imalucky)).setImageDrawable(
                    Variables.CURRENCY_SYMBOL.equalsIgnoreCase(Variables.RUPEES) ? Functions.getDrawable(context, R.drawable.ic_currency_rupee) :
                            Variables.CURRENCY_SYMBOL.equalsIgnoreCase(Variables.DOLLAR) ? Functions.getDrawable(context, R.drawable.ic_currency_dollar) :
                                    Functions.getDrawable(context, R.drawable.ic_currency_chip));


//            Picasso.with(context).load(imageResource2).into(imalucky);

            String title = model.getCoin();

            if (Functions.checkisStringValid(model.title))
                title = model.title;

            txtproname.setText(title);
            txtAmount.setText(Variables.CURRENCY_SYMBOL + historyModelArrayList.get(position).getAmount());

            txt_discount.setText(model.getSpinner_result() + "%OFF");
            if ((historyModelArrayList.get(position).getSpinner_result().equals("0") || historyModelArrayList.get(position).getSpinner_result().equals(""))) {
                final_amt = Integer.parseInt(historyModelArrayList.get(position).getAmount());
                txt_discount_amt.setText(Variables.CURRENCY_SYMBOL + historyModelArrayList.get(position).getAmount());
            } else {
                int data = Integer.parseInt(historyModelArrayList.get(position).getSpinner_result());
                int data1 = Integer.parseInt(historyModelArrayList.get(position).getAmount());
                float percentage_amt = (float) (data1 * (data / 100.0f));
                final_amt = percentage_amt + data1;
                txt_discount_amt.setText(Variables.CURRENCY_SYMBOL + final_amt);
            }
            Log.e("final_amt_one", "onClick: " + final_amt);
            txtproname.setTypeface(helvatikabold);
            txt_discount_amt.setTypeface(helvatikabold);
            txtAmount.setTypeface(helvatikabold);

            if (!model.getSpinner_result().equals("0")) {
            rel_layout.setOnClickListener(view -> {
                int data = Integer.parseInt(historyModelArrayList.get(position).getSpinner_result());
                int data1 = Integer.parseInt(historyModelArrayList.get(position).getAmount());
                float percentage_amt = (float) (data1 * (data / 100.0f));
                float final_amt = percentage_amt + data1;
                Intent intent = new Intent(context, BuyChipsDetails.class);
                intent.putExtra("plan_id", model.getId());
                intent.putExtra("chips_details", model.getCoin());
                intent.putExtra("amount", String.valueOf(final_amt));
                context.startActivity(intent);
            });
            txtAmount.setOnClickListener(view -> {
                int data = Integer.parseInt(historyModelArrayList.get(position).getSpinner_result());
                int data1 = Integer.parseInt(historyModelArrayList.get(position).getAmount());
                float percentage_amt = (float) (data1 * (data / 100.0f));
                float final_amt = percentage_amt + data1;
                Intent intent = new Intent(context, BuyChipsDetails.class);
                intent.putExtra("plan_id", model.getId());
                intent.putExtra("chips_details", model.getCoin());
                intent.putExtra("amount", String.valueOf(final_amt));
                context.startActivity(intent);
                });
            }else{
                rel_layout.setOnClickListener(view -> {
                Intent intent = new Intent(context, BuyChipsDetails.class);
                intent.putExtra("plan_id", model.getId());
                intent.putExtra("chips_details", model.getCoin());
                intent.putExtra("amount", model.getAmount());
                context.startActivity(intent);
                });
            }

        }
    }

}

