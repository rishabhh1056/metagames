package com.metagards.metagames.MyAccountDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.Variables;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Utils.Functions.makeFistLaterCaptial;

public class MyWinningAdapte extends RecyclerView.Adapter<MyWinningAdapte.Myholder> {

    Context context;
    ArrayList<MyWinnigmodel> myWinnigmodelArrayList;

    public MyWinningAdapte(Context context, ArrayList<MyWinnigmodel> myWinnigmodelArrayList) {

        this.context = context;
        this.myWinnigmodelArrayList = myWinnigmodelArrayList;

    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.winnig_itemview,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        SharedPreferences prefs = context.getSharedPreferences("Login_data", MODE_PRIVATE);
        View view = holder.itemView;
        MyWinnigmodel myWinnigmodel = myWinnigmodelArrayList.get(position);
        getTextView(view,R.id.tvSerial).setText(""+(position + 1));
        getTextView(view,R.id.tvDates).setText(""+myWinnigmodelArrayList.get(position).added_date);

        if(myWinnigmodel.ViewType == myWinnigmodel.TRANSACTION_LIST)
        {

            String price = myWinnigmodelArrayList.get(position).price;

            if(price.equalsIgnoreCase("0"))
                price = "admin";

            if(!price.equalsIgnoreCase("admin"))
                price = Variables.CURRENCY_SYMBOL +myWinnigmodelArrayList.get(position).price;

            getTextView(view,R.id.tvGames).setText(""+ price);
            holder.txtammount.setText(""+ myWinnigmodelArrayList.get(position).coin);
        }
        else {

            String user_id = prefs.getString("user_id", "");
            String name = prefs.getString("name", "");
            String winner_id = myWinnigmodelArrayList.get(position).getWinner_id();



            holder.txtid.setText("Table id : "+myWinnigmodelArrayList.get(position).table_id);
            holder.txtusername.setText(""+makeFistLaterCaptial(name));
            holder.txtammount.setText(""+ Variables.CURRENCY_SYMBOL +myWinnigmodelArrayList.get(position).amount);


            ((TextView) view.findViewById(R.id.tvGames)).setText(""+myWinnigmodel.game_type);


            holder.itemView.findViewById(R.id.imgreward).setVisibility(View.GONE);
             if(Functions.checkisStringValid(myWinnigmodel.amount) && !myWinnigmodel.amount.equalsIgnoreCase("0.00"))
            {
//                if(myWinnigmodel.game_type != myWinnigmodel.RUMMY)
                    holder.txtammount.setText("+"+ Variables.CURRENCY_SYMBOL +myWinnigmodelArrayList.get(position).amount);
//                else
//                    holder.txtammount.setText(Variables.CURRENCY_SYMBOL +myWinnigmodelArrayList.get(position).amount);

                holder.txtammount.setTextColor(context.getResources().getColor(R.color.green));
//            holder.itemView.findViewById(R.id.imgreward).setVisibility(View.VISIBLE);
            }
            else {
                if(myWinnigmodel.game_type != myWinnigmodel.RUMMY)
                    holder.txtammount.setText("-"+ Variables.CURRENCY_SYMBOL +
                        Functions.convertnulltoZero(""+myWinnigmodelArrayList.get(position).invest));
                else
                    holder.txtammount.setText(Variables.CURRENCY_SYMBOL +
                            Functions.convertnulltoZero(""+myWinnigmodelArrayList.get(position).invest));


                holder.txtammount.setTextColor(context.getResources().getColor(R.color.red));
                holder.itemView.findViewById(R.id.imgreward).setVisibility(View.GONE);
            }

        }



    }

    private TextView getTextView(View view,int id){
        return ((TextView) view.findViewById(id));
    }

    @Override
    public int getItemCount() {
        return myWinnigmodelArrayList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{

        TextView txtid,txtusername,txtammount;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            txtid = itemView.findViewById(R.id.txtid);
            txtusername = itemView.findViewById(R.id.txtusername);
            txtammount = itemView.findViewById(R.id.txtammount);

        }
    }

}
