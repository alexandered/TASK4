package com.example.alexander.applicationtask4secondattemp;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface UserAPI {
    @FormUrlEncoded

    @POST("/questions")
    retrofit.Call<User> saveTransaction (@Field("Description")String description,@Field("Amount")Integer amount);
}