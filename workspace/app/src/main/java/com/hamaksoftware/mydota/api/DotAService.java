package com.hamaksoftware.mydota.api;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

//implementation mappings here
public interface DotAService {

    @Headers("Cache-Control: max-age=640000")
    @GET("/heroes")
    ResponseTemplate getHeroes() throws APIRequestException;

    @Headers("Cache-Control: max-age=640000")
    @GET("/items")
    ResponseTemplate getItems();


    @GET("/users/{user}")
    ResponseTemplate getUser(@Path("user") String user);

    @GET("/users/{user}/matches")
    ResponseTemplate getUserMatches(@Path("user") String user,@Query("count") int count);

    @GET("/users/{user}/matches")
    ResponseTemplate getUserMatches(@Path("user") String user,@Query("count") int count, @Query("heroId") long heroId);

    @GET("/matches/{id}")
    ResponseTemplate getMatch(@Path("id") long id);

    @GET("/leagues")
    ResponseTemplate getLeagues();

}
