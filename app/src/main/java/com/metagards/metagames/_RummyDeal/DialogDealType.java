package com.metagards.metagames._RummyDeal;

import static com.metagards.metagames._RummyDeal.Fragments.RummyDealActiveTables_BF.SEL_TABLE;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;
import com.metagards.metagames.model.TableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class DialogDealType {
    Context context;
    public DialogDealType(Context context){
        this.context = context;
        CALL_API_getTableList();
        initDialog();
    }

    Dialog dialog;
    LinearLayout lnrSelectTable;
    TextView tvSelectBoot;
    int selectedPosition = 0;
   void initDialog(){
       // custom dialog
        dialog = new Dialog(context,
               android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
       dialog.setContentView(R.layout.fragment_rummy_deal_table);
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

       ((TextView) dialog.findViewById(R.id.txtheader)).setText("Deal Rummy");

       lnrSelectTable = dialog.findViewById(R.id.lnrSelectTable);
       tvSelectBoot = dialog.findViewById(R.id.tvSelectBoot);
       lnrSelectTable.removeAllViews();
       ((ImageView)dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();
           }
       });

       dialog.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               playGame();
               dialog.dismiss();
           }
       });

   }

    private void playGame() {
        TableModel usermodel = usermodelsList.get(selectedPosition);

        float min_try = 0;
            min_try = Float.parseFloat(usermodel.getBootValue());
            float wallet_amount = Float.parseFloat(SharePref.getInstance().getString("wallet","0"));

            if(min_try > wallet_amount)
            {
                Functions.SmartAlertDialog(context, "Sorry you dont have enough wallet amount.", "Sorry you dont have enough wallet amount.", "Ok", null, new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {

                    }
                });
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("table_id",""+usermodel.getId());
            bundle.putInt("ongoing",usermodel.getOngoin());
            bundle.putString("min_entry",""+min_try);
            bundle.putSerializable("tableModel", (Serializable) usermodel);
            bundle.putString(SEL_TABLE,usermodel.getBootValue());
                bundle.putString("player2","player2");
            startGame(bundle);
    }

    public void show(){
       if(dialog != null)
           dialog.show();
   }

   public void dismiss(){
       if(dialog != null)
           dialog.dismiss();
   }

    private void CALL_API_getTableList() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", SharePref.getU_id());
        params.put("token", SharePref.getAuthorization());
        String URL = "";
            URL = Const.RummyDealgetTableMaster;

        ApiRequest.Call_Api(context, URL, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    ParseResponse(resp);
                }

            }
        });
    }

    ArrayList<TableModel> usermodelsList = new ArrayList<>();
    private void ParseResponse(String resp) {
        usermodelsList.clear();
        try {
            JSONObject jsonObject = new JSONObject(resp);
            int code = jsonObject.optInt("code",404);
            String message = jsonObject.optString("message","Something went wrong");

            if(code == 205  || message.equals("You are Already On Table"))
            {

                JSONObject tableObject = jsonObject.getJSONArray("table_data").getJSONObject(0);
                TableModel tableModel = new TableModel();
                tableModel.setId(tableObject.getString("table_id"));
                Bundle bundle = new Bundle();
                bundle.putString("table_id",tableModel.getId());
                bundle.putString("player2","player2");

                startGame(bundle);
                dismiss();
                return;
            }
            lnrSelectTable.removeAllViews();

            if (code == 200)
            {

                JSONArray tableArray = jsonObject.getJSONArray("table_data");

                for (int i = 0; i < tableArray.length() ; i++) {
                    JSONObject tableObject = tableArray.getJSONObject(i);
                    TableModel model = new TableModel();
                    model.setId(tableObject.optString("id",""));
                    model.setBootValue(tableObject.optString("boot_value","0"));
                    model.setMaximumBlind(tableObject.optString("maximum_blind","0"));
                    model.setChaalLimit(tableObject.optString("chaal_limit","0"));
                    model.setPotLimit(tableObject.optString("pot_limit","0"));
                    model.setOnlineMembers(tableObject.optString("online_members","0"));
                    model.setPoint_value(tableObject.optString("point_value","0"));
                    model.setMax_player(tableObject.optString("max_player","0"));
                    model.setTable_name("Deal-"+tableObject.optString("game_count","0"));

                        float poin_value = Float.parseFloat(model.getPoint_value());
                        model.setChaalLimit(""+(poin_value * 100));
                        model.setPotLimit("2");

                    int tabtype = tableObject.optInt("private",0);
                    int ongoing = tableObject.optInt("ongoing");

                    model.setOngoin(ongoing);

                    usermodelsList.add(model);
                    createAddDealType(tableObject.optString("game_count","0"));
                }


            }
            else {
                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    boolean defaultSelected = false;
    private void createAddDealType(String game_count) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_deal_table,null);
        lnrSelectTable.addView(view);
        view.setTag(""+game_count);
        TextView tvDeal = view.findViewById(R.id.tvDeal);
        tvDeal.setBackground(context.getResources().getDrawable(R.drawable.ic_circle_red_unactvite));
        tvDeal.setText(game_count);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDealType(game_count);
            }
        });
        if(!defaultSelected)
        {
            defaultSelected = true;
            selectDealType(game_count);
        }
    }

    private void selectDealType(String game_count) {
        for (int i = 0; i < lnrSelectTable.getChildCount() ; i++) {
            View parenView = lnrSelectTable.getChildAt(i);
            TextView tvDeal = parenView.findViewById(R.id.tvDeal);
            int dealBackground = R.drawable.ic_circle_red_unactvite;

            if(parenView.getTag().toString().equals(game_count))
            {
                selectedPosition = i;
                dealBackground = R.drawable.ic_circle_red_actvite;
                try {
                    tvSelectBoot.setText("Selected Boot Amount: "+usermodelsList.get(i).getBootValue());
                }
                catch (IndexOutOfBoundsException e)
                {

                }
            }

            tvDeal.setBackground(context.getResources().getDrawable(dealBackground));
        }
    }

    protected abstract void startGame(Bundle bundle);

}
