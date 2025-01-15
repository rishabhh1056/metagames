package com.metagards.metagames.Comman;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.Activity.Homepage;
import com.metagards.metagames.Interface.Callback;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames.Utils.SharePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DialogTermsAndCondition {

    Context context;
    Callback callback;
    public DialogTermsAndCondition(Context context, Callback callback) {
        this.callback = callback;
        this.context = context;
    }

    public DialogTermsAndCondition(Context context) {
        this.context = context;
    }

    public interface DealerInterface{

        void onClick(int pos);

    }

    TextView txtnotfound;
    public void showTermDialog(String tag) {
        // custom dialog
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_termandcondition);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        Functions.setDialogParams(dialog);


        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txtheader = dialog.findViewById(R.id.txtheader);
        txtheader.setText(""+tag);

        WebView webView = dialog.findViewById(R.id.webview);

        final CheckBox cb_termscondition = dialog.findViewById(R.id.cb_termscondition);

        webView.setBackgroundColor(Color.TRANSPARENT);


        UserTermsAndCondition(webView,dialog,tag);


        ((View)dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharePref.getInstance().putBoolean(SharePref.TNC_ACCEPTED,false);

                if(callback != null)
                {
                    callback.Responce("","",null);
                }

                dialog.dismiss();
            }
        });

        ((View)dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!cb_termscondition.isChecked())
                {
                    Functions.showToast(context, "Please check Terms and condition.");
                    return;
                }

                SharePref.getInstance().putBoolean(SharePref.TNC_ACCEPTED,true);

                if(callback != null)
                {
                    callback.Responce("","",null);
                }
                dialog.dismiss();
            }
        });

    }

    private void UserTermsAndCondition(final WebView webview, final Dialog dialog, final String tag) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.TERMS_CONDITION,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");


                            if (code.equalsIgnoreCase("200")) {


                                JSONObject jsonObject1 = jsonObject.getJSONObject("setting");

                                String data = "";

                                if(tag.equals("Privacy Policy"))
                                {
                                    data = jsonObject1.getString("privacy_policy");
                                }
                                else {
                                    data = jsonObject1.getString("terms");
                                }


                                if(data.equals(""))
                                {
                                    txtnotfound.setVisibility(View.VISIBLE);
                                }
                                else {
                                    txtnotfound.setVisibility(View.GONE);
                                }


                                data = data.replaceAll("&#39;","'");

                                String szMessage = "<font face= \"trebuchet\" size=3 color=\"#fff\"><b>"
                                        + data
                                        + "</b></font>";


                                webview.getSettings().setJavaScriptEnabled(true);
                                webview.loadDataWithBaseURL("",szMessage, "text/html", "UTF-8","");


                            } else {
                                if (jsonObject.has("message")) {

                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = context.getSharedPreferences(Homepage.MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
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



}
