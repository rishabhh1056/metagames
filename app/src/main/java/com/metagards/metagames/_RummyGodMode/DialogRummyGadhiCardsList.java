package com.metagards.metagames._RummyGodMode;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;
import static com.metagards.metagames._Rummy.RummPoint.removeLastChars;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.CardModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DialogRummyGadhiCardsList {
    
    public static DialogRummyGadhiCardsList mInstance;
    Context context;

    public static DialogRummyGadhiCardsList getInstance(Context context) {
        if (null == mInstance) {
            // To make thread safe
            synchronized (DialogRummyGadhiCardsList.class) {
                // check again as multiple treads ca reach above step
                if (null == mInstance) {
                    mInstance = new DialogRummyGadhiCardsList();
                }
            }
        }

        if(mInstance != null)
            mInstance.init(context);

        return mInstance;
    }

    public DialogRummyGadhiCardsList init(Context context) {
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

        return this;
    }

    Dialog dialog;
    RecyclerView recUsercardlist;
    ArrayList<CardModel> arrayList = new ArrayList<>();
    UserCardsListAdapter adapter;
    String myChangingCard;

    public DialogRummyGadhiCardsList setMyChangingCard(String myChangingCard){
        this.myChangingCard = myChangingCard;
        return this;
    }

    DialogRummyGadhiCardsList initDialog(){
        if(dialog == null)
        {
            dialog = Functions.DialogInstance(context);
            dialog.setContentView(R.layout.dialog_gadhicardlist);
            dialog.setTitle("Title...");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            recUsercardlist = dialog.findViewById(R.id.recUsercardlist);
            recUsercardlist.setLayoutManager(new LinearLayoutManager(context));

        }


        call_api_getGadhiCards();
        return this;
    }

    public void show(){
        if(dialog != null && Functions.isActivityVisible(context))
            dialog.show();
        Functions.setDialogParams(dialog);
    }

    public void dismissed(){
        if(dialog != null && Functions.isActivityVisible(context))
            dialog.dismiss();
    }

    int GAME_TYPE = 0;
    public static final int RUMMY_POINT = 0;
    public static final int RUMMY_DEAL = 1;
    public static final int RUMMY_POOL = 2;

    public DialogRummyGadhiCardsList setGAME_TYPE(int GAME_TYPE) {
        this.GAME_TYPE = GAME_TYPE;
        return this;
    }

    void call_api_getGadhiCards(){

        String url = Const.RUMMY_GET_AVAILABLE_CARDS;
        if(GAME_TYPE == RUMMY_DEAL)
        {
            url = Const.RUMMYDEAL_GET_AVAILABLE_CARDS;
        }
        else if(GAME_TYPE == RUMMY_POOL)
        {
            url = Const.RUMMYPOOL_GET_AVAILABLE_CARDS;
        }

        HashMap params = new HashMap();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));

        ApiRequest.Call_Api(context, url, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        int code = jsonObject.getInt("code");

                        if(code == 200)
                        {
                            Gson gson = new Gson();
                            Reader reader = new StringReader(resp);
                            final CardModel apiResponseModel = gson.fromJson(reader, CardModel.class);
                            arrayList.clear();
                            arrayList.addAll(apiResponseModel.getCards());
                            adapter = new UserCardsListAdapter(context,arrayList);
                            recUsercardlist.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public class UserCardsListAdapter extends RecyclerView.Adapter<UserCardsListAdapter.myholder> {

        Context context;
        ArrayList<CardModel> arrayList;
        int current_position = -1;
        String selectedCard = "";

        public UserCardsListAdapter(Context context, ArrayList<CardModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gadhi_card,parent,false);
            return new myholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myholder holder, int position) {

            CardModel model = arrayList.get(position);

            final ImageView iv_jokercard = holder.itemView.findViewById(R.id.iv_jokercard);
            ImageView imgcards = holder.itemView.findViewById(R.id.imgcard);
            ImageView ivSelected = holder.itemView.findViewById(R.id.ivSelected);

            String joker_card = "";
            String image_card = model.getCard();

//            if(joker_card.substring(joker_card.length() - 1).equalsIgnoreCase("_"))
//            {
//                joker_card = removeLastChars(joker_card,1);
//            }
//
//            String src_joker_cards = joker_card;
//
//
//            if(src_joker_cards != null && !src_joker_cards.equals(""))
//            {
//                if(src_joker_cards.contains(image_card.substring(image_card.length() - 1)))
//                {
//
//                    iv_jokercard.setVisibility(View.VISIBLE);
//
//                }
//                else {
//                    iv_jokercard.setVisibility(View.GONE);
//                }
//            }



            String imagename = image_card;
            if(!image_card.equalsIgnoreCase("backside_card") && image_card.contains("id")) {
                imagename = image_card.substring(11);
            }

            if(imagename.substring(imagename.length() - 1).equalsIgnoreCase("_"))
            {
                imagename = removeLastChars(imagename,1);
            }
            int imageResource1 = Functions.GetResourcePath(imagename.toLowerCase(),context);
            if(imageResource1 > 0)
                imgcards.setImageDrawable(Functions.getDrawable(context,imageResource1));

            imgcards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    current_position = position;
                    notifyDataSetChanged();
                }
            });
            Button btnConfirm = dialog.findViewById(R.id.btnconfirm);

            if(current_position == position)
            {
                selectedCard = model.getCard();
                ivSelected.setVisibility(View.VISIBLE);

            }
            else {
                ivSelected.setVisibility(View.GONE);
            }
            btnConfirm.setVisibility(Functions.checkisStringValid(selectedCard) ? View.VISIBLE : View.GONE);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call_api_swipeCards(selectedCard);
                }
            });
        }

        private void ChangeTextViewColor(TextView textView,String colors){

            if(colors.equalsIgnoreCase("white"))
            {
                textView.setTextColor(context.getResources().getColor(R.color.white));
            }
            else {
                textView.setTextColor(context.getResources().getColor(R.color.blackbg));
            }


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        class myholder extends RecyclerView.ViewHolder{

            LinearLayout lnr_declareview;

            public myholder(@NonNull View itemView) {
                super(itemView);

                lnr_declareview = itemView.findViewById(R.id.lnr_declareview);

            }
        }
    }

    void call_api_swipeCards(String new_card){

        String url = Const.RUMMY_SWAP_CARD;
        if(GAME_TYPE == RUMMY_DEAL)
        {
            url = Const.RUMMYDEAL_SWAP_CARD;
        }
        else if(GAME_TYPE == RUMMY_POOL)
        {
            url = Const.RUMMYPOOL_SWAP_CARD;
        }

        HashMap params = new HashMap();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",""+prefs.getString("user_id", ""));
        params.put("token",""+prefs.getString("token", ""));
        params.put("my_card",""+myChangingCard);
        params.put("new_card",""+new_card);

        ApiRequest.Call_Api(context, url, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        int code = jsonObject.getInt("code");

                        if(code == 200)
                        {
                            callback.Response(null,0,new_card);
                            dismissed();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    OnItemClickListener callback;
    public DialogRummyGadhiCardsList returnCallback(OnItemClickListener callback){
        this.callback = callback;
        return mInstance;
    }

}
