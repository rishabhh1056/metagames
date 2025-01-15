package com.metagards.metagames.PaymentScreen;


import static com.metagards.metagames.PaymentScreen.Payment_A.checkedPosition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metagards.metagames.R;

import java.util.ArrayList;

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.SingleViewHolder> {

    private Context context;
    private ArrayList<Payment_model> employees;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
   // public static int checkedPosition = 0;

    public SingleAdapter(Context context, ArrayList<Payment_model> employees) {
        this.context = context;
        this.employees = employees;
    }

    public void setEmployees(ArrayList<Payment_model> employees) {
        this.employees = new ArrayList<>();
        this.employees = employees;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment, viewGroup, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(employees.get(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView image;
        LinearLayout lyPhonePay;

        SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvMobile);
            image = itemView.findViewById(R.id.image);
            lyPhonePay = itemView.findViewById(R.id.lyPhonePay);
        }

        void bind(final Payment_model employee) {
            if (checkedPosition == -1) {
                textView.setVisibility(View.GONE);
                lyPhonePay.setBackground(context.getResources().getDrawable(R.drawable.d_background_border_gray));
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    textView.setVisibility(View.VISIBLE);
                    lyPhonePay.setBackground(context.getResources().getDrawable(R.drawable.round_bor_colored));
                } else {
                    textView.setVisibility(View.GONE);
                    lyPhonePay.setBackground(context.getResources().getDrawable(R.drawable.d_background_border_gray));

                }
            }
            textView.setText(employee.getMobile());
            String payWith = employee.getName();
            switch (payWith) {
                case "paytm":
                    Glide.with(context).load(R.drawable.ic_payment_paytm).into(image);
                    break;
                case "gpay":
                    Glide.with(context).load(R.drawable.gpay).into(image);
                    break;
                case "paypal":
                    Glide.with(context).load(R.drawable.ic_payment_paypal_logo).into(image);
                    break;
                case "amazon":
                    Glide.with(context).load(R.drawable.ic_payment_amzon_pay).into(image);
                    break;
                case "jio":
                    Glide.with(context).load(R.drawable.ic_payment_jio_money).into(image);
                    break;
                default:
                    Glide.with(context).load(R.drawable.ic_payment_new_phonepe).into(image);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lyPhonePay.setBackground(context.getResources().getDrawable(R.drawable.round_bor_colored));
                    textView.setVisibility(View.VISIBLE);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                    ((Payment_A) context).onClickCalled(employee, 0, "1");

                }
            });
        }


    }

    public Payment_model getSelected() {
        if (checkedPosition != -1) {
            return employees.get(checkedPosition);
        }
        return null;
    }
}