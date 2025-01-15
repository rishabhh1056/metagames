package com.metagards.metagames.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.Interface.ApiRequest;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames._Rummy.RummPoint;
import com.metagards.metagames._Rummy.RummPrivate;
import com.metagards.metagames._Rummy.RummyPointSocket;
import com.metagards.metagames._TeenPatti.PublicTable_New;
import com.metagards.metagames._TeenPatti.TeenPattiSocket;
import com.metagards.metagames.model.TableModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.metagards.metagames.Activity.Homepage.MY_PREFS_NAME;

public class ActiveTables_BF extends BottomSheetDialogFragment {


    public ActiveTables_BF() {
        // Required empty public constructor
    }

    ArrayList<TableModel> usermodelsList = new ArrayList<>();

    String game_tag = "";

    public ActiveTables_BF(String tag) {
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
    public static String RUMMY_PRIVATE_TABLE = "rummy_private_table";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);

            }
        });
        return dialog;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        contentView = View.inflate(getContext(), R.layout.fragment_active_table, null);
        dialog.setContentView(contentView);

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

        contentView.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RecyclerView rec_user_points = contentView.findViewById(R.id.rec_user_points);
        rec_user_points.setLayoutManager(new LinearLayoutManager(context));
        adapter = new UserPointAdapter(this, usermodelsList);
        rec_user_points.setAdapter(adapter);

        CALL_API_getTableList();

        if (game_tag.equals(ActiveTables_BF.RUMMY_PRIVATE_TABLE)) {
            contentView.findViewById(R.id.btn_join_private).setVisibility(View.VISIBLE);
        } else {
            contentView.findViewById(R.id.btn_join_private).setVisibility(View.GONE);
        }
        contentView.findViewById(R.id.btn_join_private).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // builder.setTitle("Choose Type Of Material");
                View rowList = getLayoutInflater().inflate(R.layout.join_private, null);
                EditText et_code = rowList.findViewById(R.id.et_code);
                Button btn_join = rowList.findViewById(R.id.btn_join_private);

                builder.setView(rowList);
                AlertDialog dialog = builder.create();
                dialog.show();

                btn_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_code = et_code.getText().toString().trim();
                        if (str_code.equals("")) {
                            Toast.makeText(context, "Please enter valid code to join!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            join_privateTable(str_code);
                        }
                    }
                });
            }
        });
    }

    private void join_privateTable(String code) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.join_private,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK_PRIVATE", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
                                String message = jsonObject.getString("message");
                                Functions.showToast(context, message);
                                Intent i = new Intent(context, RummPrivate.class);
                                i.putExtra("player5", "player5");
                                i.putExtra("call_status", "1");
//                                i.putExtra(SEL_TABLE,pool_boot);
                                i.putExtra("min_entry", "0");
                                i.putExtra("hide_hint", "1");
                                startActivity(i);
                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(context, message);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("code", "" + code);
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java_join", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    SharedPreferences prefs;

    private void CALL_API_getTableList() {

        HashMap<String, String> params = new HashMap<String, String>();
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));
        String URL = "";
        if (game_tag.equals(TEENPATTI))
            URL = Const.get_table_master;
        else
            URL = Const.RummygetTableMaster;

        ApiRequest.Call_Api(context, URL, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if (resp != null) {
                    ParseResponse(resp);
                }

            }
        });

        String title = "Game Listing";

        if (game_tag.equals(TEENPATTI)) {
            title = "Teen Patti";
        } else if (game_tag.equals(RUMMY2)) {
            title = "Point Rummy";
        } else if (game_tag.equals(RUMMY5)) {
            title = "Point Rummy";
        } else if (game_tag.equals(RUMMY_PRIVATE_TABLE)) {
            title = "Rummy Private";
        }

        getTexView(contentView, R.id.txtheader).setText(title);


    }

    private void ParseResponse(String resp) {
        usermodelsList.clear();
        try {
            JSONObject jsonObject = new JSONObject(resp);
            int code = jsonObject.optInt("code", 404);
            String message = jsonObject.optString("message", "Something went wrong");

            if (code == 205 || message.equals("You are Already On Table")) {
                Intent intent = null;
                if (game_tag.equals(TEENPATTI)) {
                    intent = getPublicTableIntent();
                } else if (game_tag.equals(RUMMY2) || game_tag.equals(RUMMY5)) {
                    intent = getRummyTableIntent();
                    if (game_tag.equals(RUMMY2))
                        intent.putExtra("player2", "player2");

                    if (game_tag.equals(ActiveTables_BF.RUMMY_PRIVATE_TABLE))
                        intent.putExtra("gametype", ActiveTables_BF.RUMMY_PRIVATE_TABLE);
                } else if (game_tag.equals(ActiveTables_BF.RUMMY_PRIVATE_TABLE)) {
                    intent = getRummyTableIntentPrivate();
                    if (game_tag.equals(RUMMY2))
                        intent.putExtra("player2", "player2");

                    if (game_tag.equals(ActiveTables_BF.RUMMY_PRIVATE_TABLE))
                        intent.putExtra("gametype", ActiveTables_BF.RUMMY_PRIVATE_TABLE);
                    intent.putExtra("private_table", "1");
                }

                if (intent != null)
                    startActivity(intent);

                dismiss();
                return;
            }

            if (code == 200) {

                JSONArray tableArray = jsonObject.getJSONArray("table_data");
                Log.d("json_array_table_", String.valueOf(tableArray));
                for (int i = 0; i < tableArray.length(); i++) {
                    JSONObject tableObject = tableArray.getJSONObject(i);
                    TableModel model = new TableModel();
                    model.setId(tableObject.optString("id", ""));
                    model.setBootValue(tableObject.optString("boot_value", ""));
                    model.setMaximumBlind(tableObject.optString("maximum_blind", ""));
//                    model.setChaalLimit(tableObject.optString("chaal_limit",""));
                    model.setChaalLimit(tableObject.optString("min_amount", ""));
                    model.setPotLimit(tableObject.optString("pot_limit", ""));
                    model.setOnlineMembers(tableObject.optString("online_members", ""));
                    model.setOnlineMembers(tableObject.optString("online_members", ""));
                    model.setPoint_value(tableObject.optString("point_value", ""));

                    if (game_tag.equals(RUMMY5) || game_tag.equals(RUMMY2)) {
                        float poin_value = Float.parseFloat(model.getPoint_value());
                        model.setChaalLimit("" + (poin_value * 100));
                        if (game_tag.equals(RUMMY2))
                            model.setPotLimit("2");
                        else
                            model.setPotLimit("5");

                        getTexView(contentView, R.id.tvChaalLimit).setText("Min Entry");
                        getTexView(contentView, R.id.tvPotLimite).setText("Max Players");
                    }

                    getView(contentView, R.id.lnrHeadBoot).setVisibility(Functions.isStringValid(model.getBootValue()) ? View.VISIBLE : View.GONE);
                    getView(contentView, R.id.lnrHeadMax).setVisibility(Functions.isStringValid(model.getMaximumBlind()) ? View.VISIBLE : View.GONE);
                    getView(contentView, R.id.lnrHeadChaal).setVisibility(Functions.isStringValid(model.getChaalLimit()) ? View.VISIBLE : View.GONE);
                    getView(contentView, R.id.lnrHeadPot).setVisibility(Functions.isStringValid(model.getPotLimit()) ? View.VISIBLE : View.GONE);
                    getView(contentView, R.id.lnrHeadPlayer).setVisibility(Functions.isStringValid(model.getOnlineMembers()) ? View.VISIBLE : View.GONE);

                    getView(contentView, R.id.lnrHeadMax).setVisibility(Functions.isStringValid(model.getPoint_value()) ? View.VISIBLE : View.GONE);
                    getTexView(contentView, R.id.tvMaxBlind).setText("Point Value");


                    if (game_tag.equals(RUMMY5) || game_tag.equals(RUMMY2)) {
                        getView(contentView, R.id.lnrHeadBoot).setVisibility(View.GONE);
                    }

                    usermodelsList.add(model);
                }


            } else {
                Functions.showToast(context, "" + message);
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
        ActiveTables_BF giftBSFragment;

        public UserPointAdapter(ActiveTables_BF coupansFragment, ArrayList<TableModel> arrayList) {
            this.arrayList = arrayList;
            this.giftBSFragment = coupansFragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_table, parent, false);

//            int height = parent.getMeasuredHeight() / 4;
//            int width = parent.getMeasuredWidth();

//            view.setLayoutParams(new RecyclerView.LayoutParams(width, height));
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            View view = holder.itemView;
            final TableModel usermodel = arrayList.get(position);

            getTexView(view, R.id.tvBoot).setText("" + usermodel.getBootValue());
            getTexView(view, R.id.tvMinBuy).setText("" + usermodel.getMaximumBlind());
            getTexView(view, R.id.tvChaalLimit).setText("" + usermodel.getChaalLimit());
            getTexView(view, R.id.tvPotLimi).setText("" + usermodel.getPotLimit());
            getTexView(view, R.id.tvTotalPlayer).setText("" + usermodel.getOnlineMembers());

            getView(view, R.id.lnrValueBoot).setVisibility(Functions.isStringValid(usermodel.getBootValue()) ? View.VISIBLE : View.GONE);
            getView(view, R.id.lnrValueMax).setVisibility(Functions.isStringValid(usermodel.getMaximumBlind()) ? View.VISIBLE : View.GONE);
            getView(view, R.id.lnrValueChaal).setVisibility(Functions.isStringValid(usermodel.getChaalLimit()) ? View.VISIBLE : View.GONE);
            getView(view, R.id.lnrValuePot).setVisibility(Functions.isStringValid(usermodel.getPotLimit()) ? View.VISIBLE : View.GONE);
            getView(view, R.id.lnrValuePlayer).setVisibility(Functions.isStringValid(usermodel.getOnlineMembers()) ? View.VISIBLE : View.GONE);

            getView(view, R.id.lnrValueMax).setVisibility(Functions.isStringValid(usermodel.getPoint_value()) ? View.VISIBLE : View.GONE);
            getTexView(view, R.id.tvMinBuy).setText("" + usermodel.getPoint_value());

            if (game_tag.equals(RUMMY5) || game_tag.equals(RUMMY2)) {
                getView(view, R.id.lnrValueBoot).setVisibility(View.GONE);
            }

            getTexView(view, R.id.tvPlaynow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float min_try = 0;
                    if (game_tag.equals(RUMMY5) || game_tag.equals(RUMMY2)) {
                        min_try = Float.parseFloat(usermodel.getChaalLimit());
                        float wallet_amount = Float.parseFloat(prefs.getString("wallet", "0"));

                        if (min_try > wallet_amount) {
                            Functions.SmartAlertDialog(context, "Sorry you dont have enough wallet amount.", "Sorry you dont have enough wallet amount.", "Ok", null, new Callback() {
                                @Override
                                public void Responce(String resp, String type, Bundle bundle) {

                                }
                            });
                            return;
                        }

                    }
                    dismiss();

                    Intent intent = null;
                    if (game_tag.equals(TEENPATTI)) {
                        intent = getPublicTableIntent();
                    } else if (game_tag.equals(RUMMY2) || game_tag.equals(RUMMY5)) {
                        intent = getRummyTableIntent();
                        if (game_tag.equals(RUMMY2))
                            intent.putExtra("player2", "player2");

                        intent.putExtra("min_entry", min_try);
                    } else if (game_tag.equals(RUMMY_PRIVATE_TABLE)) {
                        intent = getRummyTableIntentPrivate();
                        if (game_tag.equals(RUMMY2))
                            intent.putExtra("player2", "player2");
                        intent.putExtra("private_table", "1");
                        intent.putExtra("min_entry", min_try);
                    }

                    if (intent != null) {
                        if (game_tag.equals(ActiveTables_BF.RUMMY_PRIVATE_TABLE)) {
                            intent.putExtra("gametype", ActiveTables_BF.RUMMY_PRIVATE_TABLE);
                            intent.putExtra("bootvalue", usermodel.getBootValue());
                            intent.putExtra(SEL_TABLE, usermodel.getBootValue());
                        } else
                            intent.putExtra(SEL_TABLE, usermodel.getBootValue());

                        startActivity(intent);
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
        if (Functions.isActivityExist(getContext()))
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private Intent getPublicTableIntent() {
         return new Intent(context, PublicTable_New.class);
//        return new Intent(context, TeenPattiSocket.class);
    }

    private Intent getRummyTableIntent() {
         return new Intent(context, RummPoint.class);
//        return new Intent(context, RummyPointSocket.class);
    }

    private Intent getRummyTableIntentPrivate() {
        return new Intent(context, RummPrivate.class);
    }
}