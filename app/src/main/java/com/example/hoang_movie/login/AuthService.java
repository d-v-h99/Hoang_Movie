package com.example.hoang_movie.login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("tasks/by-date")
    Call<TasksResponse> getTasks(
            @Header("Authorization") String authToken,
            @Body DateRequest request
    );

}
