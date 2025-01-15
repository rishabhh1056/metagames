package com.metagards.metagames._RummyPull.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames._RummyPull.RummyPullGame;
import com.metagards.metagames.model.TableModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RummyActiveTables_BF extends BottomSheetDialogFragment {


    public RummyActiveTables_BF() {
        // Required empty public constructor
    }

    ArrayList<TableModel> usermodelsList = new ArrayList<>();

    String game_tag = "";
    public RummyActiveTables_BF(String tag) {
        this.game_tag = tag;
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
    Activity context;
    UserPointAdapter adapter;
    public static String SEL_TABLE = "sel_table";
    public static String TEENPATTI = "teenpatti";
    public static String RUMMY2 = "rummy2";
    public static String RUMMY5 = "rummy5";
    public static String VIEWER_OPEN = "Viewer open";
    public static String VIEWER_CLOSE = "Viewer close";

    public static String PUBLIC_GAME = "public game";
    public static String PRIVATE_GAME = "private game";
    int tabposition  = 0;

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
        contentView = View.inflate(getContext(), R.layout.fragment_rummy_active_table, null);
        dialog.setContentView(contentView);


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        context = getActivity();

        contentView.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RecyclerView rec_user_points = contentView.findViewById(R.id.rec_user_points);
        rec_user_points.setLayoutManager(new LinearLayoutManager(context));
        adapter = new UserPointAdapter(this,usermodelsList);
        rec_user_points.setAdapter(adapter);


        contentView.findViewById(R.id.rltParentView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        contentView.findViewById(R.id.lnrCreateTable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Dialog_CreateTable();
            }
        });

        TabLayout tabLayout = contentView.findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("New Games"));
        tabLayout.addTab(tabLayout.newTab().setText("Private Games"));
        tabLayout.addTab(tabLayout.newTab().setText("OnGoing Games"));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabposition = tab.getPosition();
                CALL_API_getTableList();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        CALL_API_getTableList();
    }

    public void VerifyPassword(TableModel tableModel) {

        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_subimt);
        dialog.setTitle("Title...");
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
        TextView tv_heading = dialog.findViewById(R.id.tv_heading);
        tv_heading.setText("Verify Password");
       EditText edit_OTP = (EditText) dialog.findViewById(R.id.edit_OTP);
       edit_OTP.setTextColor(Functions.getColor(context,R.color.white));
       edit_OTP.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit_OTP.setHint("Enter password");

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        View imglogin = dialog.findViewById(R.id.imglogin);

        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_OTP.getText().toString().length() > 0) {

                    if(tableModel.getPassword().equalsIgnoreCase(Functions.getStringFromEdit(edit_OTP)))
                    {
                        dialog.dismiss();
                        Functions.showToast(context,"Password confirm!");
                        Bundle params = new Bundle();
                        params.putSerializable("tableModel", (Serializable) tableModel);
                        OpenRummyGames(params);
                    }
                    else {
                        Functions.showToast(context,"Password not match!");
                    }

                } else {
                    Toast.makeText(context, "Please Enter Code",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        Functions.setDialogParams(dialog);

    }

    private void OpenRummyGames(Bundle bundle){
        Intent intent = new Intent(context, RummyPullGame.class);
        if(bundle != null)
            intent.putExtras(bundle);

        if(context != null && !context.isFinishing())
            context.startActivity(intent);
    }



    SharedPreferences prefs;
    private void CALL_API_getTableList() {

        HashMap<String, String> params = new HashMap<String, String>();
         prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));
        String URL = "";
        if(game_tag.equals(TEENPATTI))
         URL = Const.get_table_master;
        else
         URL = Const.RummypoolgetTableMaster;

        ApiRequest.Call_Api(context, URL, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    ParseResponse(resp);
                }

            }
        });

         if(game_tag.equals(RUMMY2))
        {
            getTexView(contentView,R.id.txtheader).setText(getString(R.string.rummy_2_player));
        }
        else if(game_tag.equals(RUMMY5))
        {
            getTexView(contentView,R.id.txtheader).setText(getString(R.string.rummy_5_player));
        }




    }

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
                if(game_tag.equals(RUMMY2))
                    bundle.putString("player2","player2");

                if(game_tag.equals(RUMMY5) || game_tag.equals(RUMMY2))
                {
                    OpenRummyGames(bundle);
                }

                dismiss();
                return;
            }

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
//                    model.setTable_name("Pool-101");      //old
                    model.setTable_name("Pool-"+tableObject.optString("pool_point","0"));   //new
//                    model.setWinning_amount(tableObject.optString("winning_amount","0"));
//                    model.setFounder_id(tableObject.optString("founder_id","0"));
//                    model.setInvitation_code(tableObject.optString("invitation_code","0"));
//                    model.setPassword(tableObject.optString("password","0"));
//                    model.setViewer_status(tableObject.optString("viewer_status","0"));
//                    model.setTableType(tableObject.optString("private","0"));

//                    if (model.getViewer_status().equalsIgnoreCase("0"))
//                        model.setViewer_status(VIEWER_OPEN);
//                    else
//                        model.setViewer_status(VIEWER_CLOSE);
//
//                    if (model.getTableType().equalsIgnoreCase("1"))
//                        model.setTableType(PRIVATE_GAME);
//                    else
//                        model.setTableType(PUBLIC_GAME);

                    if(game_tag.equals(RUMMY5) || game_tag.equals(RUMMY2))
                    {
                        float poin_value = Float.parseFloat(model.getPoint_value());
                        model.setChaalLimit(""+(poin_value * 100));
                        if(game_tag.equals(RUMMY2))
                            model.setPotLimit("2");
                        else
                            model.setPotLimit("7");
                    }

                    int tabtype = tableObject.optInt("private",0);
                    int ongoing = tableObject.optInt("ongoing");

                    model.setOngoin(ongoing);

                    if((tabtype ==tabposition && ongoing == 0) || (tabposition == 2 && ongoing == 1))
                        usermodelsList.add(model);
                }


            }
            else {
                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private TextView getTexView(View view, int id) {

        return ((TextView) view.findViewById(id));
    }

    private View getView(View view, int id) {

        return ((View) view.findViewById(id));
    }



    public class UserPointAdapter extends RecyclerView.Adapter<UserPointAdapter.ViewHolder> {

        ArrayList<TableModel> arrayList;
        RummyActiveTables_BF giftBSFragment;

        public UserPointAdapter(RummyActiveTables_BF coupansFragment, ArrayList<TableModel> arrayList) {
            this.arrayList = arrayList;
            this.giftBSFragment = coupansFragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rummy_active_table, parent, false);

//            int height = parent.getMeasuredHeight() / 4;
//            int width = parent.getMeasuredWidth();

//            view.setLayoutParams(new RecyclerView.LayoutParams(width, height));
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            View view = holder.itemView;
            final TableModel usermodel = arrayList.get(position);

            View lnrListingbg= view.findViewById(R.id.lnrListingbg);

//            if(position % 2== 0)
//            {
//                lnrListingbg.setBackgroundColor(context.getResources().getColor(R.color.table_list_color));
//            }
//            else {
//                lnrListingbg.setBackgroundColor(context.getResources().getColor(R.color.table_list_color2));
//            }

            getTexView(view,R.id.tvValue1).setText(""+usermodel.getId());
            getTexView(view,R.id.tvValue2).setText(""+usermodel.getTable_name());
            getTexView(view,R.id.tvValue3).setText(""+usermodel.getBootValue());
            getTexView(view,R.id.tvValue4).setText(""+usermodel.getMax_player());
            getTexView(view,R.id.tvValue5).setText(""+usermodel.getOnlineMembers());
            getTexView(view,R.id.tvValue6).setText(""+usermodel.getTableType());
            getTexView(view,R.id.tvValue7).setText(""+usermodel.getViewer_status());

            if(usermodel.getOngoin() == 1)
                getTexView(view,R.id.tvValue8).setText(""+getString(R.string.view_game));

            getTexView(view,R.id.tvValue8).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float min_try = 0;
                    if(game_tag.equals(RUMMY5)|| game_tag.equals(RUMMY2))
                    {
                         min_try = Float.parseFloat(usermodel.getBootValue());
                        float wallet_amount = Float.parseFloat(prefs.getString("wallet","0"));

                        if(min_try > wallet_amount)
                        {
                            Functions.SmartAlertDialog(context, "Sorry you dont have enough wallet amount.", "Sorry you dont have enough wallet amount.", "Ok", null, new Callback() {
                                @Override
                                public void Responce(String resp, String type, Bundle bundle) {

                                }
                            });
                            return;
                        }

                    }
                    dismiss();


                    if(usermodel.getTableType().equalsIgnoreCase(PRIVATE_GAME))
                    {
                        VerifyPassword(usermodel);
                    }
                    else{
                    Intent intent = null;

                    if(game_tag.equals(RUMMY5) || game_tag.equals(RUMMY2))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("table_id",""+usermodel.getId());
                        bundle.putInt("ongoing",usermodel.getOngoin());
                        bundle.putString("min_entry",""+min_try);
                        bundle.putSerializable("tableModel", (Serializable) usermodel);
                        if(game_tag.equals(RUMMY2))
                            bundle.putString("player2","player2");
                        bundle.putString(SEL_TABLE,usermodel.getBootValue());
                        bundle.putString("pool_game",usermodel.getTable_name());
                        OpenRummyGames(bundle);

                    }

                    if(intent != null)
                    {
                        intent.putExtra(SEL_TABLE,usermodel.getBootValue());
                        startActivity(intent);
                    }

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