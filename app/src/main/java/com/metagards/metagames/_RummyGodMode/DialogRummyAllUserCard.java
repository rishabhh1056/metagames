package com.metagards.metagames._RummyGodMode;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;
import static com.metagards.metagames.Utils.Functions.convertDpToPixel;
import static com.metagards.metagames._RummyPull.RummyPullGame.removeLastChars;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogRummyAllUserCard {
    
    public static DialogRummyAllUserCard mInstance;
    Context context;

    public static DialogRummyAllUserCard getInstance(Context context) {
        if (null == mInstance) {
            // To make thread safe
            synchronized (DialogRummyAllUserCard.class) {
                // check again as multiple treads ca reach above step
                if (null == mInstance) {
                    mInstance = new DialogRummyAllUserCard();
                }
            }
        }

        if(mInstance != null)
            mInstance.init(context);

        return mInstance;
    }

    public DialogRummyAllUserCard init(Context context) {
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
    ArrayList<GameUsersCardModel> arrayList = new ArrayList<>();
    UserCardsListAdapter adapter;
    DialogRummyAllUserCard initDialog(){
        if(dialog == null)
        {
            dialog = Functions.DialogInstance(context);
            dialog.setContentView(R.layout.dialog_rummy_userscardlist);
            dialog.setTitle("Title...");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            recUsercardlist = dialog.findViewById(R.id.recUsercardlist);
            recUsercardlist.setLayoutManager(new LinearLayoutManager(context));
            adapter = new UserCardsListAdapter(context,arrayList);
            recUsercardlist.setAdapter(adapter);
        }

        dialog.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissed();
            }
        });


        call_api_getUserCards();
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
    public final int RUMMY_POINT = 0;
    public final int RUMMY_DEAL = 1;
    public final int RUMMY_POOL = 2;

    public DialogRummyAllUserCard setGAME_TYPE(int GAME_TYPE) {
        this.GAME_TYPE = GAME_TYPE;
        return this;
    }

    void call_api_getUserCards(){

        String url = Const.RUMMY_USER_CARD;
        if(GAME_TYPE == RUMMY_DEAL)
        {
            url = Const.RummyDeal_USER_CARD;
        }
        else if(GAME_TYPE == RUMMY_POOL)
        {
            url = Const.Rummypool_USER_CARD;
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
                            final GameUsersCardModel apiResponseModel = gson.fromJson(reader, GameUsersCardModel.class);
                            arrayList.clear();
                            arrayList.addAll(apiResponseModel.getGameUsers());
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
        ArrayList<GameUsersCardModel> arrayList;

        public UserCardsListAdapter(Context context, ArrayList<GameUsersCardModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_cardslist,parent,false);
            return new myholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myholder holder, int position) {

            GameUsersCardModel model = arrayList.get(position);


            if(model.getName() != null && !model.getName().equals(""))
            {
                ((TextView) holder.itemView.findViewById(R.id.tv_username)).setText(""+arrayList.get(position).getName());
            }
            else {
                ((TextView) holder.itemView.findViewById(R.id.tv_username)).setText(""+arrayList.get(position).getId());
            }


            ((TextView) dialog.findViewById(R.id.txt_title))
                    .setText(""+arrayList.get(position).getGameId());

            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


            if(model.getUserId().equalsIgnoreCase(prefs.getString("user_id", "")))
            {
                holder.itemView.findViewById(R.id.tv_you).setVisibility(View.VISIBLE);
            }
            else {
                holder.itemView.findViewById(R.id.tv_you).setVisibility(View.INVISIBLE);
            }


            if(arrayList.get(position).getCards() != null)
            {
                grouplist_size = 1;
                holder.lnr_declareview.removeAllViews();
                for (int i = 0; i < grouplist_size ; i++) {

                    List<GameUsersCardModel.Card> cardModelArrayList = arrayList.get(position).getCards();

                    CreateGroups(cardModelArrayList,i,holder.lnr_declareview,
                            arrayList.get(position).getJoker());

                }
            }

            ImageView imgjokercard = dialog.findViewById(R.id.imgjokercard);

            String joker_card = model.getJoker();

            if(joker_card.substring(joker_card.length() - 1).equalsIgnoreCase("_"))
            {
                joker_card = removeLastChars(joker_card,1);
            }

            String urijokar = "@drawable/" + joker_card.toLowerCase();  // where myresource " +
            //
            int imageResource2 = context.getResources().getIdentifier(urijokar, null,
                    context.getPackageName());
            if(imageResource2 > 0)
                imgjokercard.setImageDrawable(Functions.getDrawable(context,imageResource2));

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

        int grouplist_size = 0;
        private void CreateGroups(List<GameUsersCardModel.Card> cardImageList,int count,LinearLayout rlt_addcardview,String joker_card) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_grouplayout,  null);
            TextView tv_status = view.findViewById(R.id.tv_status);
            ImageView iv_status = view.findViewById(R.id.iv_status);
            LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);
            RelativeLayout rlt_icons = view.findViewById(R.id.rlt_icons);
            rlt_icons.setVisibility(View.GONE);

//            tv_status.setText(""+cardImageList.get(0).group_value);
//            lnr_group_card.setTag(""+cardImageList.get(0).value_grp);




            for (int i = 0; i < cardImageList.size() ; i++) {

                AddCardtoGroup(cardImageList.get(i).getCard(),i,lnr_group_card,cardImageList,joker_card);

            }



            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int valueInPixels = (int) context.getResources().getDimension(R.dimen.margin_left_group);


//        if(grouplist_size >= 4)
//        {
            valueInPixels = 10 * grouplist_size;
//        }

            if(grouplist_size == 1)
                valueInPixels = 20;

            int leftmargin = 0;
            if(count == 0)
            {
                leftmargin = (int) convertDpToPixel(valueInPixels,context);
            }

            layoutParams.setMargins((int) leftmargin, (int) convertDpToPixel(0,context), 0, 0);

            ViewGroup.LayoutParams params = rlt_addcardview.getLayoutParams();

            rlt_addcardview.setLayoutParams(params);
            rlt_addcardview.requestLayout();

            rlt_addcardview.addView(view,layoutParams);

        }

        private void AddCardtoGroup(String image_card , final int countnumber, LinearLayout addlayout,
                                    final List<GameUsersCardModel.Card> arrayList,String joker_card) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_card_win,  null);
            ImageView imgcards = view.findViewById(R.id.imgcard);
            final ImageView iv_jokercard = view.findViewById(R.id.iv_jokercard);

            view.setTag(image_card+"");

            if(joker_card.substring(joker_card.length() - 1).equalsIgnoreCase("_"))
            {
                joker_card = removeLastChars(joker_card,1);
            }

            String src_joker_cards = joker_card;


            if(src_joker_cards != null && !src_joker_cards.equals(""))
            {
                if(src_joker_cards.contains(image_card.substring(image_card.length() - 1)))
                {

                    iv_jokercard.setVisibility(View.VISIBLE);

                }
                else {
                    iv_jokercard.setVisibility(View.GONE);
                }
            }


            String imagename = image_card;
            if(!image_card.equalsIgnoreCase("backside_card") && image_card.contains("id")) {
                imagename = image_card.substring(11);
            }

            Functions.LOGE("DeclareAdapter","imagename : "+imagename);

            if(imagename.substring(imagename.length() - 1).equalsIgnoreCase("_"))
            {
                imagename = removeLastChars(imagename,1);
            }


            int imageResource1 = Functions.GetResourcePath(imagename.toLowerCase(),context);
            Glide.with(context).load(imageResource1).into(imgcards);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            layoutParams.setMargins(0, (int) convertDpToPixel(0,context), (int) convertDpToPixel(-25,context), 0);

            addlayout.addView(view,layoutParams);


            if(countnumber == (arrayList.size() - 1))
            {
                ViewGroup.LayoutParams params = addlayout.getLayoutParams();
                float initial_width = params.width;
//            params.width = (int) (initial_width + convertDpToPixel(50,context));

                if(arrayList.size() == 1)
                {
                    params.width = (int) convertDpToPixel(45 ,context) * arrayList.size();
                }
                else
                if(arrayList.size() <= 3)
                {
                    params.width = (int) convertDpToPixel(30,context) * arrayList.size();
                }
                else {
                    params.width = (int) convertDpToPixel(20,context) * arrayList.size();
                }

                addlayout.setLayoutParams(params);
                addlayout.requestLayout();
            }

        }


    }

}
