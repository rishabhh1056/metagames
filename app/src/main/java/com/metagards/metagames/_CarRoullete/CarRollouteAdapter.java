package com.metagards.metagames._CarRoullete;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.metagards.metagames.Interface.OnItemClickListener;
import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;

import java.util.List;

public class CarRollouteAdapter extends RecyclerView.Adapter<CarRollouteAdapter.holder> {

    List<CarRollouteModel> arraylist;
    OnItemClickListener callback;
    Context context;

    public CarRollouteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carroullete_type,parent,false));
    }

    public void setArraylist(List<CarRollouteModel> arraylist) {
        this.arraylist = arraylist;
        notifyDataSetChanged();
    }

    public void onItemSelectListener(OnItemClickListener callback){
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        CarRollouteModel model = arraylist.get(position);

        if(model != null)
            holder.bind(model);

    }

    @Override
    public int getItemCount() {
        return arraylist != null ? arraylist.size() : 0;
    }

    class holder extends RecyclerView.ViewHolder{
        public holder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(CarRollouteModel model){
            int postion = getAdapterPosition();
            ImageView ivContainerbg = itemView.findViewById(R.id.ivContainerbg);
            Drawable topleft_corner = Functions.getDrawable(context,R.drawable.ic_carrounlate_topleft_side);
            Drawable topright_corner = Functions.getDrawable(context,R.drawable.ic_carrounlate_topright_side);
            Drawable center_corner = Functions.getDrawable(context,R.drawable.ic_carrounlate_centerside);
            Drawable bottomleft_corner = Functions.getDrawable(context,R.drawable.ic_carrounlate_btmleftside);
            Drawable bottomright_corner = Functions.getDrawable(context,R.drawable.ic_carrounlate_btmright_side);
            Drawable itembackground = center_corner;
            if(postion == 0)
            {
                itembackground = topleft_corner;
            }
            else if(postion == 3)
            {
                itembackground = topright_corner;
            }
            else if(postion == 4)
            {
                itembackground = bottomleft_corner;
            }
            else if(postion+1 == arraylist.size())
            {
                itembackground = bottomright_corner;
            }

            ivContainerbg.setBackground(itembackground);
            ivContainerbg.clearAnimation();
            TextView tvJackpotHeading = itemView.findViewById(R.id.tvJackpotHeading);
            TextView tvJackpotamount = itemView.findViewById(R.id.tvJackpotamount);
            TextView tvJackpotSelectamount = itemView.findViewById(R.id.tvJackpotSelectamount);
            TextView tvUsersAddedAmount = itemView.findViewById(R.id.tvUsersAddedAmount);
            ImageView ivCarroulate = itemView.findViewById(R.id.ivCarroulate);
            RelativeLayout rltAmountadded = itemView.findViewById(R.id.rltAmountadded);
            TextView btninto = itemView.findViewById(R.id.btninto);
            btninto.setText(model.into);

            tvJackpotHeading.setText(model.rule_type);
            tvJackpotamount.setText(""+model.added_amount);
            tvJackpotSelectamount.setText(""+model.select_amount);

            if (!tvJackpotSelectamount.getText().equals("0")){
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(150); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tvJackpotSelectamount.startAnimation(anim);

//                rltContainer.setBackgroundResource(R.drawable.background_border_withcolor);
//                rltContainer.startAnimation(anim);
                ivContainerbg.setBackground(Functions.getDrawable(context,R.drawable.btn_blue_declare));
//                initiAnimation();
//                ivContainerbg.startAnimation(blinksAnimation);
            }

            Glide.with(context).load(Functions.getDrawable(context,model.getRule_icon())).into(ivCarroulate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //    ((CarRoullete_A)context).playButtonTouchSound();
                    ((CarRoullete_Socket)context).playButtonTouchSound();
                    Animation animBounce = AnimationUtils.loadAnimation(context,
                            R.anim.bounce);
                    animBounce.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    itemView.startAnimation(animBounce);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callback.Response(v,postion,model);
                        }
                    },700);



                }
            });

            rltAmountadded.setVisibility(View.GONE);

            if(model.isAnimatedAddedAmount()
                    && model.getLast_added_id() != SharePref.getInstance().getInt(SharePref.lastAmountAddedID))
            {
                SharePref.getInstance().putInt(SharePref.lastAmountAddedID,model.getLast_added_id());
                rltAmountadded.setVisibility(View.VISIBLE);
                tvUsersAddedAmount.setText("+"+model.getLast_added_amount());
                SlideToAbove(rltAmountadded,model);
            }

            if(model.isWine())
            {
                model.setWine(false);
                ivContainerbg.setBackground(Functions.getDrawable(context,R.drawable.ic_jackpot_rule_bg_selected));
                initiAnimation();
                ivContainerbg.startAnimation(blinksAnimation);
            }
        }
    }

    Animation blinksAnimation;
    private void initiAnimation() {
        blinksAnimation = AnimationUtils.loadAnimation(context,R.anim.blink);
        blinksAnimation.setDuration(500);
        blinksAnimation.setRepeatCount(Animation.INFINITE);
//        blinksAnimation.setStartOffset(700);
    }

    public void SlideToAbove(View animationView, CarRollouteModel model) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(1000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        animationView.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                animationView.clearAnimation();
                animationView.setVisibility(View.GONE);
                model.setAnimatedAddedAmount(false);
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        rl_footer.getWidth(), rl_footer.getHeight());
//                // lp.setMargins(0, 0, 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                rl_footer.setLayoutParams(lp);

            }

        });

    }
}
