package com.metagards.metagames._CarRoullete.menu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames._LuckJackpot.JackpotRulesModel;

import java.util.ArrayList;

public class DialogCarRollouteGamesRule {

    Context context;
    Callback callback;
    private static DialogCarRollouteGamesRule mInstance;


    private final String SET = "SET";
    private final String PURE_SEQ = "PURE SEQ";
    private final String SEQ = "SEQ";
    private final String COLOR = "COLOR";
    private final String PAIR = "PAIR";
    private final String HIGH_CARD = "HIGH CARD";


    public static DialogCarRollouteGamesRule getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DialogCarRollouteGamesRule.class) {
                if (null == mInstance) {
                    mInstance = new DialogCarRollouteGamesRule(context);
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
    public DialogCarRollouteGamesRule init(Context context) {
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
    
    LinearLayout lnrRuleslist ;
    ArrayList<JackpotRulesModel> rulesModelArrayList = new ArrayList<>();
    private DialogCarRollouteGamesRule initDialog() {
        dialog = Functions.DialogInstance(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_luckygamesrules);

        lnrRuleslist = dialog.findViewById(R.id.lnrRuleslist);
        rulesModelArrayList.clear();
        rulesModelArrayList.addAll(getRuleValue());
        lnrRuleslist.removeAllViews();
        for (JackpotRulesModel model: rulesModelArrayList) {
            addRulesonView(model);
        }


        return mInstance;
    }

    private ArrayList<JackpotRulesModel> getRuleValue() {
        ArrayList<JackpotRulesModel> rulesModelList = new ArrayList<>();
        rulesModelList.add(new JackpotRulesModel("Result",0,0,0,"Payoff"));
        rulesModelList.add(new JackpotRulesModel(HIGH_CARD,1,0,0,"3X"));
        rulesModelList.add(new JackpotRulesModel(PAIR,2,0,0,"4X"));
        rulesModelList.add(new JackpotRulesModel(COLOR,3,0,0,"5X"));
        rulesModelList.add(new JackpotRulesModel(SEQ,4,0,0,"6X"));
        rulesModelList.add(new JackpotRulesModel(PURE_SEQ,5,0,0,"10X"));
        rulesModelList.add(new JackpotRulesModel(SET,6,0,0,"Split the JACKPOT*20%"));
        return rulesModelList;
    }

    private void addRulesonView(JackpotRulesModel rulesModel) {
        View view = Functions.CreateDynamicViews(R.layout.item_jackpot_rules_list,lnrRuleslist,context);
        TextView tvRules = view.findViewById(R.id.tvRules);
        TextView tvRulespoint = view.findViewById(R.id.tvRulespoint);

        tvRules.setText(""+rulesModel.getRule_type());
        tvRulespoint.setText(""+rulesModel.getInto());

    }

    public DialogCarRollouteGamesRule(Context context) {
        this.context = context;
    }

    public DialogCarRollouteGamesRule() {
    }
    Dialog dialog;

    public DialogCarRollouteGamesRule show() {

        dialog.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Functions.setDialogParams(dialog);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.DialogAnimation);
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

}
