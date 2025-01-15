package com.metagards.metagames._AdharBahar.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.metagards.metagames.Utils.Functions;
import com.metagards.metagames._AdharBahar.Adapter.GamesListAdapter;
import com.metagards.metagames._AdharBahar.Model.GameModel;
import com.metagards.metagames.R;
import com.metagards.metagames.ApiClasses.Const;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class GameFragment extends Fragment {


    public static final String MY_PREFS_NAME = "Login_data";

    public GameFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View view;
    RecyclerView recyclerView;
    GamesListAdapter adapter;
    ArrayList<GameModel> gameModelArrayList = new ArrayList<>();
    Activity context;
    TextView txtNodata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game, container, false);
        context = getActivity();



        Initialization();
        getAllRooms();
        return view;
    }

    private void Initialization() {
        recyclerView = view.findViewById(R.id.rec_gameslist);

        txtNodata = view.findViewById(R.id.txtNodata);

        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        adapter = new GamesListAdapter(context,gameModelArrayList);
        recyclerView.setAdapter(adapter);

    }

    private void getAllRooms() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GETROOM,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Log.v("responce_game" , response);

                            if (code.equalsIgnoreCase("200")) {
                                gameModelArrayList.clear();

                                JSONArray arraygame_dataa = jsonObject.getJSONArray("room_data");

                                for (int i = 0; i < arraygame_dataa.length(); i++) {
                                    JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                     GameModel model = new GameModel();
                                      model.setId(welcome_bonusObject.getString("id"));
                                      model.setMin_coin(welcome_bonusObject.getString("min_coin"));
                                      model.setMax_coin(welcome_bonusObject.getString("max_coin"));
                                      model.setAdded_date(welcome_bonusObject.getString("added_date"));
                                      model.setOnline(welcome_bonusObject.getString("online"));
                                      gameModelArrayList.add(model);


                                }
                            }
                            adapter.notifyDataSetChanged();

                            if (gameModelArrayList.size() > 0){
                                txtNodata.setVisibility(View.GONE);
                            }else {
                                txtNodata.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(getActivity(), "Something went wrong");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("room_id", "1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

}