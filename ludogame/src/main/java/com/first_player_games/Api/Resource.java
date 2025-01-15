package com.first_player_games.Api;

import static com.first_player_games.Api.Status.LOADING;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    @NonNull
    public final Status status;

    @NonNull
    public final T data;

    @NonNull
    public final String message;

    public Resource(@NonNull Status status, @NonNull T data, @NonNull String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data){
        return new Resource<>(Status.SUCCESS,data,null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data){
        return new Resource<>(Status.ERROR,data,msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

}
