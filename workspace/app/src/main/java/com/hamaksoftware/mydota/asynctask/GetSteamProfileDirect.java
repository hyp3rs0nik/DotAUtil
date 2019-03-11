package com.hamaksoftware.mydota.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.hamaksoftware.mydota.model.SteamProfileDirect;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;


public class GetSteamProfileDirect extends AsyncTask<Void, Void, SteamProfileDirect> {
    public static final String ASYNC_ID = "GETSTEAMPROFILEDIRECT";
    public IAsyncTaskListener asyncTaskListener;
    protected Context ctx;
    protected String steamId;

    public GetSteamProfileDirect(Context ctx, String steamId){
        this.steamId = steamId;
        this.ctx = ctx;
    }

    class SteamAPIResponse{
        SteamAPIPlayers response;
    }

    class SteamAPIPlayers{
        List<SteamProfileDirect> players;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskWorking(ASYNC_ID);
    }

    @Override
    protected SteamProfileDirect doInBackground(Void... params) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=1D590860D494B74F01AF6B49D4D1552B&steamids=" + this.steamId;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            Gson gson = new GsonBuilder().create();
            String raw = response.body().string();
            SteamAPIResponse res = gson.fromJson(raw, new TypeToken<SteamAPIResponse>() {}.getType());
            return res.response.players.get(0);
        }catch(IOException ioe){
            return null;
        }
    }

    @Override
    protected void onPostExecute(SteamProfileDirect steamProfile) {
        asyncTaskListener.onTaskCompleted(steamProfile, ASYNC_ID);
    }
}
