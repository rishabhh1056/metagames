package com.first_player_games.Api;

import com.first_player_games.ModelResponse.RegisterResponse;
import com.first_player_games.ModelResponse.UserdataModelResponse;
import com.first_player_games.ludoApi.TableMaster;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.email_login)
    Call<RegisterResponse> registerOrlogin(
            @Field("email") String email,
            @Field("name") String token,
            @Field("source") String source,
            @Field("gender") String gender,
            @Field("referral_code") String referral_code
    );

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.user_profile)
    Call<UserdataModelResponse> getUserData(
//            @Field("fcm") String fcm,
            @Field("id") String user_id,
            @Field("token") String token
    );


    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.get_table_master)
    Call<TableMaster> getTableMaster(
            @Field("user_id") String user_id,
            @Field("token") String token);

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.get_table)
    Call<TableMaster> getTable(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("boot_value") String boot_value,
            @Field("invite_code") String invite_code,
            @Field("table_id") String table_id
    );

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.join_table)
    Call<TableMaster> getJoinTable(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("invite_code") String ludo_table_id,
            @Field("table_id") String table_id);

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.start_game)
    Call<JSONObject> getStartGame(
            @Field("user_id") String user_id,
            @Field("token") String token);

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.make_winner)
    Call<JSONObject> getMakeWinner(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("winner_user_id") String winner_user_id,
            @Field("table_id") String table_id);

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.leave_table)
    Call<JSONObject> getLeaveTable(
            @Field("user_id") String user_id,
            @Field("token") String token
            );

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.gameStatus)
    Call<UserdataModelResponse> getGameStatus(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("invite_code") String ludo_table_id,
            @Field("ludo_table_id") String table_id);

    @Headers("token:"+EndPoints.token)
    @FormUrlEncoded
    @POST(EndPoints.rolldice)
    Call<JSONObject> roleDice(
            @Field("user_id") String user_id,
            @Field("token") String token);
}
