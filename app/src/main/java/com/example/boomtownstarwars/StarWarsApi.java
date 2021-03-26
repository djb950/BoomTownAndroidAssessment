package com.example.boomtownstarwars;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StarWarsApi {

    @GET("people/")
    Call<Response> getAllPeople(@Query("page") int page);
}
