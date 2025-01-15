package com.first_player_games.Api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.first_player_games.ModelResponse.UserdataModelResponse;
import com.first_player_games.Others.Functions;
import com.first_player_games.Others.SharePref;
import com.first_player_games.R;
import com.first_player_games.ludoApi.TableMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LudoApiRepository {

    private static LudoApiRepository mInstance;
    private Context context;
    public static synchronized LudoApiRepository getInstance(){

        if(mInstance == null)
        {
            mInstance = new LudoApiRepository();
        }
        return mInstance;
    }

    public LudoApiRepository init(Context context){
        this.context = context;
        SharePref.getInstance().init(context);
        return this;
    }

    public LiveData<Resource<List<UserdataModelResponse>>> getUserData(){
        MutableLiveData<Resource<List<UserdataModelResponse>>> livedata = new MutableLiveData<Resource<List<UserdataModelResponse>>>();
        livedata.postValue(Resource.loading(null));
        Call<UserdataModelResponse> call = RetrofitClient.getInstance().getApi().getUserData(SharePref.getU_id(),SharePref.getAuthorization());

        call.enqueue(new Callback<UserdataModelResponse>() {
            @Override
            public void onResponse(Call<UserdataModelResponse> call, Response<UserdataModelResponse> response) {

                if(response.isSuccessful())
                {
                    UserdataModelResponse userdataModelResponse = response.body();

                    if(userdataModelResponse != null && userdataModelResponse.getCode() == 200)
                    {
                        UserdataModelResponse.UserDatum userData = userdataModelResponse.getUserData().get(0);
                        UserdataModelResponse.Setting gameSetting = userdataModelResponse.getSetting();

                        String bonus = gameSetting.getBonus();
                        String payment_gateway = gameSetting.getPaymentGateway();
                        String symbol = gameSetting.getSymbol();

                        if(Functions.checkStringisValid(bonus) && !bonus.equalsIgnoreCase("0"))
                            SharePref.getInstance().putBoolean(SharePref.isBonusShow,true);
                        else
                            SharePref.getInstance().putBoolean(SharePref.isBonusShow,false);

                        if(Functions.checkStringisValid(payment_gateway)
                                && !payment_gateway.equalsIgnoreCase("0")
                                && !payment_gateway.equalsIgnoreCase("2")
                                && !payment_gateway.equalsIgnoreCase("3"))
                            SharePref.getInstance().putBoolean(SharePref.isPaymentGateShow,false);
                        else
                            SharePref.getInstance().putBoolean(SharePref.isPaymentGateShow,true);



                        livedata.postValue(Resource.success(null));

                    }
                    else {
                        livedata.postValue(Resource.error(userdataModelResponse.getMessage(),null));
                    }

                }
                else {
                    livedata.postValue(Resource.error(response.message(),null));
                }

            }

            @Override
            public void onFailure(Call<UserdataModelResponse> call, Throwable t) {
                livedata.postValue(Resource.error(t.toString(),null));
            }
        });

        return livedata;
    }


    public LiveData<Resource<List<TableMaster.TableDatum>>> call_api_getTableMaster(){

        MutableLiveData<Resource<List<TableMaster.TableDatum>>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<TableMaster> call = RetrofitClient.getInstance().getApi().getTableMaster(SharePref.getU_id(),SharePref.getAuthorization());

        call.enqueue(new Callback<TableMaster>() {
            @Override
            public void onResponse(Call<TableMaster> call, Response<TableMaster> response) {

                if(response != null && response.isSuccessful())
                {
                    TableMaster tableMaster = response.body();
                    if(tableMaster.getCode() == 200)
                    {
                        liveData.postValue(Resource.success(tableMaster.getTableData()));
                    }
                    else if(tableMaster.getCode() == 205)
                    {
                        liveData.postValue(Resource.error(""+tableMaster.getCode(),tableMaster.getTableData()));
                    }
                    else {
                        liveData.postValue(Resource.error(tableMaster.getMessage(),null));
                    }
                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<TableMaster> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }

    public LiveData<Resource<String>> call_api_getTable(String boot_value,String room_id){

        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<TableMaster> call = RetrofitClient.getInstance().getApi().getTable(SharePref.getU_id(),SharePref.getAuthorization()
                ,boot_value,room_id,room_id);

        call.enqueue(new Callback<TableMaster>() {
            @Override
            public void onResponse(Call<TableMaster> call, Response<TableMaster> response) {

                if(response.body() != null && response.isSuccessful())
                {
                    String table_id = "";
                    TableMaster tableMaster = response.body();
                    try {

                        liveData.postValue(Resource.success(tableMaster.getTableData().get(0).getLudo_table_id()));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(context, "Required Minimum 100 Coins to Play", Toast.LENGTH_LONG).show();
                        Log.d("ludo_error", String.valueOf(e));
                    }
                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<TableMaster> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }

    public LiveData<Resource<String>> call_api_getJoinTable(String ludo_table_id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<TableMaster> call = RetrofitClient.getInstance().getApi().getJoinTable(SharePref.getU_id(),SharePref.getAuthorization(),ludo_table_id,ludo_table_id);

        call.enqueue(new Callback<TableMaster>() {
            @Override
            public void onResponse(Call<TableMaster> call, Response<TableMaster> response) {

                if(response.body() != null && response.isSuccessful())
                {
                    TableMaster tableMaster = response.body();
                    liveData.postValue(Resource.success(tableMaster.getTableData().get(0).getLudo_table_id()));
//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
//                        int code = jsonObject.getInt("code");
//                        if(code == 200)
//                        {
//                            String table_id = "";
//                            JSONArray table_data = jsonObject.getJSONArray("table_data");
//                            for (int i = 0; i < table_data.length() ; i++) {
//                                JSONObject tableObject = table_data.getJSONObject(i);
//                                table_id = tableObject.getString("ludo_table_id");
//                            }
//                            liveData.postValue(Resource.success(table_id));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<TableMaster> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }

    public LiveData<Resource<String>> call_api_getStartGame() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<JSONObject> call = RetrofitClient.getInstance().getApi().getStartGame(SharePref.getU_id(),SharePref.getAuthorization());

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                if(response != null && response.isSuccessful())
                {

                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }

    public LiveData<Resource<String>> call_api_getMakeWinner(String winner_user_id,String table_id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<JSONObject> call = RetrofitClient.getInstance().getApi().getMakeWinner(SharePref.getU_id()
                ,SharePref.getAuthorization(),winner_user_id,table_id);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                if(response != null && response.isSuccessful())
                {

                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }


    public LiveData<Resource<String>> call_api_leaveTable() {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<JSONObject> call = RetrofitClient.getInstance().getApi().getLeaveTable(SharePref.getU_id(),SharePref.getAuthorization());

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                if(response != null && response.isSuccessful())
                {

                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }

    public LiveData<Resource<UserdataModelResponse>> call_api_gameStatus(String table_id,int game_id) {
        MutableLiveData<Resource<UserdataModelResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<UserdataModelResponse> call = RetrofitClient.getInstance().getApi().getGameStatus(SharePref.getU_id()
                ,SharePref.getAuthorization(),table_id,table_id);

        call.enqueue(new Callback<UserdataModelResponse>() {
            @Override
            public void onResponse(Call<UserdataModelResponse> call, Response<UserdataModelResponse> response) {

                if(response.body() != null && response.isSuccessful())
                {
                    liveData.postValue(Resource.success(response.body()));
                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<UserdataModelResponse> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }

    public LiveData<Resource<Integer>> call_api_rolldice() {
        MutableLiveData<Resource<Integer>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));

        Call<JSONObject> call = RetrofitClient.getInstance().getApi().roleDice(SharePref.getU_id(),SharePref.getAuthorization());

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                if(response != null && response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        int diceCount = jsonObject.getInt("steps");
                        liveData.postValue(Resource.success(diceCount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    liveData.postValue(Resource.error("",null));
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                liveData.postValue(Resource.error(call.toString(),null));
            }
        });

        return liveData;
    }

}
