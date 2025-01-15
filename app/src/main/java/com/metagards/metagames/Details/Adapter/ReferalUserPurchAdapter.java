package com.metagards.metagames.Details.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Details.Menu.ReferralUserPurchModel;
import com.metagards.metagames.R;

import java.util.ArrayList;

public class ReferalUserPurchAdapter extends RecyclerView.Adapter<ReferalUserPurchAdapter.Myholder> {

    Context context;
    ArrayList<ReferralUserPurchModel> myWinnigmodelArrayList;

    public ReferalUserPurchAdapter(Context context, ArrayList<ReferralUserPurchModel> myWinnigmodelArrayList) {

        this.context = context;
        this.myWinnigmodelArrayList = myWinnigmodelArrayList;

    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.referaluserpurch_iteamview,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, @SuppressLint("RecyclerView") final int position) {

        SharedPreferences prefs = context.getSharedPreferences("Login_data", MODE_PRIVATE);
        View view = holder.itemView;
        ReferralUserPurchModel myWinnigmodel = myWinnigmodelArrayList.get(position);
        getTextView(view,R.id.tvSerial).setText(""+(position + 1));
        getTextView(view,R.id.txtammount).setText(""+(myWinnigmodelArrayList.get(position).coin));
        getTextView(view,R.id.tvAddedDate).setText(""+(myWinnigmodelArrayList.get(position).updated_date));
        getTextView(view,R.id.tvNanme).setText(""+myWinnigmodelArrayList.get(position).name);
        getTextView(view,R.id.txtlevel).setText(""+myWinnigmodelArrayList.get(position).level);

// holder.itemView.setOnClickListener(new View.OnClickListener() {
//     @Override
//     public void onClick(View view) {
//         Intent intent = new Intent(context, ReferralPurchView.class);
//       //  intent.putExtra("name", (CharSequence) myWinnigmodelArrayList.get(position));
//         intent.putExtra("image_name",myWinnigmodelArrayList.get(position).name );
//         context.startActivity(intent);
//     }
// });

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
