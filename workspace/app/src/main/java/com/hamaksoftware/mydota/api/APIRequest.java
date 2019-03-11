package com.hamaksoftware.mydota.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.hamaksoftware.mydota.activeandroid.model.Item;
import com.hamaksoftware.mydota.enums.CommunityVisibilityState;
import com.hamaksoftware.mydota.enums.HeroType;
import com.hamaksoftware.mydota.enums.LeaverStatus;
import com.hamaksoftware.mydota.enums.LobbyType;
import com.hamaksoftware.mydota.enums.PersonaState;
import com.hamaksoftware.mydota.gson.deserializers.CommunityVisibilityStateDeserializer;
import com.hamaksoftware.mydota.gson.deserializers.HeroDeserializer;
import com.hamaksoftware.mydota.gson.deserializers.HeroTypeDeserializer;
import com.hamaksoftware.mydota.gson.deserializers.ItemDeserializer;
import com.hamaksoftware.mydota.gson.deserializers.LeaverStatusDeserializer;
import com.hamaksoftware.mydota.gson.deserializers.LobbyTypeDeserializer;
import com.hamaksoftware.mydota.gson.deserializers.PersonaStateDeserializer;
import com.hamaksoftware.mydota.model.League;
import com.hamaksoftware.mydota.model.Match;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class APIRequest {
    public static final String API_ENDPOINT = "http://besiera.info:3000";
    private static APIRequest api;
    private static DotAService dotaService;
    Gson gson;
    OkHttpClient client;

    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestInterceptor.RequestFacade request) {
            //future placeholder for manipulating SHARED header information
            //request.addHeader("Authorization", "Bearer someapikeyhere");
        }
    };

    private APIRequest() {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //prevent circular reference
                        //enum mappings
                .registerTypeAdapter(CommunityVisibilityState.class, new CommunityVisibilityStateDeserializer())
                .registerTypeAdapter(PersonaState.class, new PersonaStateDeserializer())
                .registerTypeAdapter(HeroType.class, new HeroTypeDeserializer())
                .registerTypeAdapter(LobbyType.class, new LobbyTypeDeserializer())
                .registerTypeAdapter(Hero.class, new HeroDeserializer())
                        //.registerTypeAdapter(Item.class, new ItemDeserializer())
                .registerTypeAdapter(LeaverStatus.class, new LeaverStatusDeserializer())
                .create();


        client = new OkHttpClient();
        client.setReadTimeout(5, TimeUnit.MINUTES);
        client.setConnectTimeout(30, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setErrorHandler(new APIErrorHandler())
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(client))
                .build();

        dotaService = restAdapter.create(DotAService.class);
    }

    public static APIRequest getInstance() {
        if (api == null) {
            api = new APIRequest();
        }

        return api;
    }

    //Add all needed API mappings here.

    public List<Hero> getHeroes() throws APIRequestException{
        Type dataType = new TypeToken<ResponseTemplate<List<Hero>>>() {
        }.getType();
        ResponseTemplate<List<Hero>> response = gson.fromJson(gson.toJson(dotaService.getHeroes(), dataType), dataType);
        return response.getData();
    }


    public List<Item> getItems(){
        Type dataType = new TypeToken<ResponseTemplate<List<Item>>>() {
        }.getType();
        ResponseTemplate<List<Item>> response = gson.fromJson(gson.toJson(dotaService.getItems(), dataType), dataType);
        return response.getData();
    }

    public List<Match> getUserMatches(String steamId, int count){
        Type dataType = new TypeToken<ResponseTemplate<List<Match>>>() {
        }.getType();
        ResponseTemplate<List<Match>> response = gson.fromJson(gson.toJson(dotaService.getUserMatches(steamId, count), dataType), dataType);
        return response.getData();
    }

    public List<Match> getUserMatches(String steamId, int count, long heroId){
        Type dataType = new TypeToken<ResponseTemplate<List<Match>>>() {
        }.getType();
        ResponseTemplate<List<Match>> response = gson.fromJson(gson.toJson(dotaService.getUserMatches(steamId, count, heroId), dataType), dataType);
        return response.getData();
    }

    public Match getMatch(long id) {
        //cant remember why i had to create new instance of gson and the adapter here.
        //well check later
        //TODO: check if code is redundant

        Gson g = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LobbyType.class, new LobbyTypeDeserializer())
                .registerTypeAdapter(Hero.class, new HeroDeserializer())
                .registerTypeAdapter(Item.class, new ItemDeserializer())
                .registerTypeAdapter(LeaverStatus.class, new LeaverStatusDeserializer())
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(g))
                .setErrorHandler(new APIErrorHandler())
                .setRequestInterceptor(requestInterceptor)
                .build();

        DotAService s = restAdapter.create(DotAService.class);
        Type dataType = new TypeToken<ResponseTemplate<Match>>() {
        }.getType();
        ResponseTemplate<Match> response = g.fromJson(g.toJson(s.getMatch(id), dataType), dataType);
        return response.getData();
    }

    public List<League> getLeagues(){
        Type dataType = new TypeToken<ResponseTemplate<List<League>>>() {
        }.getType();
        ResponseTemplate<List<League>> response = new Gson().fromJson(gson.toJson(dotaService.getLeagues(), dataType), dataType);
        return response.getData();
    }

}
