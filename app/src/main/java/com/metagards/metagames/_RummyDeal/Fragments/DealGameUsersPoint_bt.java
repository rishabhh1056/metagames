package com.metagards.metagames._RummyDeal.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.R;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.model.Usermodel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class DealGameUsersPoint_bt extends BottomSheetDialogFragment {


    public DealGameUsersPoint_bt() {
        // Required empty public constructor
    }

    ArrayList<Usermodel> usermodelsList = new ArrayList<>();
    String game_id = "";
    public DealGameUsersPoint_bt(ArrayList<Usermodel> usermodelsList, String game_id) {
        this.usermodelsList = usermodelsList;
        this.game_id = game_id;
        // Required empty public constructor
    }

    public interface StickerListener {
        void onStickerClick(String bitmap, String ammount);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    View contentView;

    @NonNull
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        contentView = View.inflate(getContext(), R.layout.dialog_users_point, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        contentView.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RecyclerView rec_user_points = contentView.findViewById(R.id.rec_user_points);
        rec_user_points.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        TextView txt_title = contentView.findViewById(R.id.txt_title);
        txt_title.setText(Functions.getString(getContext(),R.string.game_points_game_id)+" #"+game_id);

        Usermodel roundModel = new Usermodel();
        roundModel.pointlist = new ArrayList<>();
        boolean isSerialHave = false;
        if (usermodelsList.size() > 0) {
            Usermodel model = usermodelsList.get(0);
            for (int j = 0; j < model.pointlist.size(); j++) {
                Usermodel pointMOdel = new Usermodel();
                pointMOdel.user_points = "" + (j + 1);
                pointMOdel.VIEW_TYPE = 1;

                if(model.pointlist.get(j).VIEW_TYPE == 1)
                    isSerialHave = true;

                roundModel.pointlist.add(pointMOdel);
            }



            if(!isSerialHave)
                usermodelsList.add(0, roundModel);
        }


        rec_user_points.setAdapter(new UserPointAdapter(this,usermodelsList));

    }

    public class UserPointAdapter extends RecyclerView.Adapter<UserPointAdapter.ViewHolder> {

        ArrayList<Usermodel> arrayList;
        DealGameUsersPoint_bt giftBSFragment;

        public UserPointAdapter(DealGameUsersPoint_bt coupansFragment, ArrayList<Usermodel> arrayList) {
            this.arrayList = arrayList;
            this.giftBSFragment = coupansFragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_point,parent,false);

//            int height = parent.getMeasuredHeight() / 4;
//            int width = parent.getMeasuredWidth();

//            view.setLayoutParams(new RecyclerView.LayoutParams(width, height));
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            View view = holder.itemView;
            Usermodel usermodel = arrayList.get(position);
            TextView tvPlayer = view.findViewById(R.id.tvPlayer);
            TextView tvPlayerno = view.findViewById(R.id.tvPlayerno);
            TextView tvTotalPoints = view.findViewById(R.id.tvTotalPoints);
            if(position == 0)
            {
                tvPlayerno.setText(getString(R.string.rounds));
                tvPlayer.setVisibility(View.INVISIBLE);
            }
            else {
                tvPlayer.setVisibility(View.VISIBLE);
                tvPlayerno.setText(getString(R.string.players)+" "+(position));
                if(Functions.checkisStringValid(usermodel.userName))
                    tvPlayer.setText(""+usermodel.userName);
            }

            holder.CALL_API_getPoint(usermodel,position);


        }

        @Override
        public int getItemCount() {
            return arrayList != null ? arrayList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout lnr_pointslist;

            ViewHolder(View itemView) {
                super(itemView);
                lnr_pointslist = itemView.findViewById(R.id.lnr_pointslist);
            }

            private void CALL_API_getPoint(Usermodel usermodel,int user_position){
                lnr_pointslist.removeAllViews();
                int position = getAdapterPosition();

                for (int i = 0; i < usermodel.pointlist.size(); i++) {
                    addPointtoUsers(usermodel.pointlist.get(i),i);
                }
            }

            private void addPointtoUsers(Usermodel model, int position){
                View view = Functions.CreateDynamicViews(R.layout.item_point,lnr_pointslist,getActivity());
                TextView tvPoints = view.findViewById(R.id.tvPoints);

                tvPoints.setText(Html.fromHtml(model.user_points));

            }
        }


    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


}
