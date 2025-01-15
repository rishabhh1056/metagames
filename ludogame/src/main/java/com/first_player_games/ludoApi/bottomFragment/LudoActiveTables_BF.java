package com.first_player_games.ludoApi.bottomFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.first_player_games.Api.Resource;
import com.first_player_games.ClassCallback;
import com.first_player_games.Others.Functions;
import com.first_player_games.Others.SharePref;
import com.first_player_games.R;
import com.first_player_games.ludoApi.TableMaster;
import com.first_player_games.ludoApi.model.LudoViewModel;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class LudoActiveTables_BF extends BottomSheetDialogFragment {


    public LudoActiveTables_BF() {
        // Required empty public constructor
    }

    ArrayList<TableMaster.TableDatum> usermodelsList = new ArrayList<>();
    ClassCallback callback;

    public void setCallback(ClassCallback callback) {
        this.callback = callback;
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
    Activity context;
    UserPointAdapter adapter;
    public static String SEL_TABLE = "sel_table";

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
        contentView = View.inflate(getContext(), R.layout.fragment_ludo_active_table, null);
        dialog.setContentView(contentView);

        SharePref.getInstance().init(getContext());

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        context = getActivity();

        contentView.findViewById(R.id.imgclosetop).setOnClickListener(v -> dismiss());

        RecyclerView rec_user_points = contentView.findViewById(R.id.rec_user_points);
        rec_user_points.setLayoutManager(new LinearLayoutManager(context));
        adapter = new UserPointAdapter(this,usermodelsList);
        rec_user_points.setAdapter(adapter);

//        getTexView(contentView,R.id.txt_title).setText("Ludo Tables List");


        CALL_API_getTableList();
    }

    SharedPreferences prefs;
    private void CALL_API_getTableList() {

        LudoViewModel.getInstance().init(context).loadTableMaster();
        LudoViewModel.getInstance().getTableMaster().observe(this, new Observer<Resource<List<TableMaster.TableDatum>>>() {
            @Override
            public void onChanged(Resource<List<TableMaster.TableDatum>> listResource) {
                switch (listResource.status)
                {
                    case LOADING:
                        break;
                    case SUCCESS:
                    {
                        usermodelsList.clear();
                        usermodelsList.addAll(listResource.data);
                        adapter.notifyDataSetChanged();
                    }
                        break;
                    case ERROR:
                        if(listResource.message.equals("205"))
                        {
                            dismiss();
                            Functions.showToast(context,"You are Already On Table");
                            if(callback != null)
                                callback.Response(null,0,listResource.data != null
                                        ? listResource.data.get(0) : "");
                        }
                        break;
                }
            }
        });

    }

    private TextView getTexView(View view, int id) {

        return ((TextView) view.findViewById(id));
    }

    private View getView(View view, int id) {

        return ((View) view.findViewById(id));
    }



    public class UserPointAdapter extends RecyclerView.Adapter<UserPointAdapter.ViewHolder> {

        ArrayList<TableMaster.TableDatum> arrayList;
        LudoActiveTables_BF giftBSFragment;

        public UserPointAdapter(LudoActiveTables_BF coupansFragment, ArrayList<TableMaster.TableDatum> arrayList) {
            this.arrayList = arrayList;
            this.giftBSFragment = coupansFragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ludo_active_table, parent, false);

//            int height = parent.getMeasuredHeight() / 4;
//            int width = parent.getMeasuredWidth();

//            view.setLayoutParams(new RecyclerView.LayoutParams(width, height));
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            View view = holder.itemView;
            final TableMaster.TableDatum usermodel = arrayList.get(position);

            getTexView(view,R.id.tvBoot).setText(""+usermodel.getBootValue());
            getTexView(view,R.id.tvTotalPlayer).setText(""+usermodel.getOnlineMembers());

            getView(view,R.id.lnrValueBoot).setVisibility(Functions.checkStringisValid(usermodel.getBootValue()) ? View.VISIBLE : View.GONE);
//            getView(view,R.id.lnrValuePlayer).setVisibility(Functions.checkStringisValid(usermodel.getOnlineMembers()) ? View.VISIBLE : View.GONE);


            getTexView(view,R.id.tvPlaynow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(callback != null)
                        callback.Response(v,position,usermodel);

                    dismiss();


                }
            });

        }

        private TextView getTexView(View view, int id) {

            return ((TextView) view.findViewById(id));
        }

        private View getView(View view, int id) {

            return ((View) view.findViewById(id));
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ViewHolder(View itemView) {
                super(itemView);

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