package com.metagards.metagames.Menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.account.LoginScreen;
import com.metagards.metagames.Comman.DialogWebviewContents;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.RedeemCoins.RedeemActivity;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.Variables;

import java.util.ArrayList;
import java.util.List;

public class DialogGameMenu {

    Context context;
    Callback callback;
    private static DialogGameMenu mInstance;


    public static DialogGameMenu getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogGameMenu.class) {
                if (null == mInstance) {
                    mInstance = new DialogGameMenu(context);
                }
            }
        }

        if(mInstance != null)
            mInstance.init(context);

        return mInstance;
    }

    /**
     * initialization of context, use only first time later it will use this again and again
     *
     * @param context app context: first time
     */
    public DialogGameMenu init(Context context) {
        try {

            if (context != null) {
                this.context = context;
                initDialog();
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return mInstance;
    }


    MenuListAdapter adapter;
    List<MenuModel> menuModelList = new ArrayList<>();
    int[] menu_icons = {
//            R.drawable.ic_menu_kyc,
//            R.drawable.ic_menu_withdraw,
//            R.drawable.ic_menu_refer,
//            R.drawable.ic_menu_termsandcondtion,
//            R.drawable.ic_menu_responsible,
////            R.drawable.ic_menu_feedback,
//            R.drawable.ic_menu_help,
//            R.drawable.ic_menu_logout
    };

    String[] menu_text = {
//            "KYC",
            "Withdraw",
            "Refer & Earn",
            "Term & Condition",
            "Responsible Gaming",
//            "Feedback",
            "Help",
            "Logout"};

    private DialogGameMenu initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.custom_dialog_game_menu);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        menuModelList.clear();
        getMenuItems();
        adapter = new MenuListAdapter(menuModelList);
        recyclerView.setAdapter(adapter);

        dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return mInstance;
    }

    private void getMenuItems() {
        for (int i = 0; i <menu_text.length ; i++) {
            menuModelList.add(new MenuModel(i,menu_text[i],menu_icons[i]));
        }
    }

    public DialogGameMenu(Context context) {
        this.context = context;
    }

    public DialogGameMenu() {
    }
    Dialog dialog;

    public DialogGameMenu show() {
        dialog.setCancelable(true);
        dialog.show();
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return mInstance;
    }

    public void dismiss(){
        if(dialog != null)
            dialog.dismiss();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.myHolder>{
        List<MenuModel> rulesModelArrayList;
        public MenuListAdapter(List<MenuModel> rulesModelArrayList) {
            this.rulesModelArrayList = rulesModelArrayList;
        }

        public void setDataList(List<MenuModel> rulesModelArrayList){
            this.rulesModelArrayList.addAll(rulesModelArrayList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new myHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_menu,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull myHolder holder, int position) {
            MenuModel winnermodel = rulesModelArrayList.get(position);
            if(winnermodel != null)
                holder.bind(winnermodel);
        }

        @Override
        public int getItemCount() {
            return rulesModelArrayList.size();
        }

        public  class myHolder extends RecyclerView.ViewHolder{
            public myHolder(@NonNull View itemView) {
                super(itemView);
            }

            float total_amount = 0;
            public void bind(MenuModel winnermodel) {

                String item = winnermodel.getItem_name();

                getTextView(R.id.tvMenuText)
                        .setText(""+item);

                ((ImageView) itemView.findViewById(R.id.ivMenuIcon))
                        .setImageDrawable(Functions.getDrawable(context,winnermodel.getItem_icon()));

                itemView.findViewById(R.id.rltMenuParent)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                                if(item.equalsIgnoreCase("KYC"))
                                {

                                }
                                else if(item.equalsIgnoreCase("Withdraw"))
                                {
                                    context.startActivity(new Intent(context, RedeemActivity.class));
                                }
                                else if(item.equalsIgnoreCase("Refer & Earn"))
                                {
                                    DialogReferandEarn.getInstance(context).show();

                                }else if(item.equalsIgnoreCase("Term & Condition"))
                                {
                                    new DialogWebviewContents(context).showDialog(Variables.TERMS_CONDITION);

                                }else if(item.equalsIgnoreCase("Responsible Gaming"))
                                {
                                    new DialogWebviewContents(context).showDialog(Variables.PRIVACY_POLICY);

                                }else if(item.equalsIgnoreCase("Feedback"))
                                {

                                }else if(item.equalsIgnoreCase("Help"))
                                {
                                    new DialogWebviewContents(context).showDialog(Variables.SUPPORT);
                                }
                                else if(item.equalsIgnoreCase("Logout"))
                                {
                                    SharedPreferences.Editor editor = context.getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("user_id", "");
                                    editor.putString("name", "");
                                    editor.putString("mobile", "");
                                    editor.putString("token", "");
                                    editor.apply();
                                    Intent intent = new Intent(context, LoginScreen.class);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                }

                            }
                        });
            }

            TextView getTextView(int id){
                return  ((TextView) itemView.findViewById(id));
            }
        }


    }


}
