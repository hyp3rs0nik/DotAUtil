package com.hamaksoftware.mydota.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.hamaksoftware.mydota.api.APIRequest;
import com.hamaksoftware.mydota.model.Match;

import java.util.ArrayList;
import java.util.List;

public class GetUserMatches extends AsyncTask<Void, Void, List<Match>> {
    public static final String ASYNC_ID = "GETUSERMATCHES";
    public IAsyncTaskListener asyncTaskListener;

    private ArrayList<Match> matches;
    private String steamId;
    private long heroId;

    public GetUserMatches(String steamId, long heroId) {
        this.steamId = steamId;
        this.heroId = heroId;
        matches = new ArrayList<Match>(0);
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskWorking(ASYNC_ID);
    }

    @Override
    protected  List<Match> doInBackground(Void... params) {

        try {
            List<Match> _matches = null;
            if(heroId <= 0){
                _matches =  APIRequest.getInstance().getUserMatches(steamId, 25);
            }else{
                _matches = APIRequest.getInstance().getUserMatches(steamId, 25, heroId);
            }

            return _matches;

        } catch (Exception e) {
            Log.e(ASYNC_ID, "Error parsing JSON Text: " + e.getMessage());
            e.printStackTrace();
        }

        return matches;
    }

    protected void onPostExecute(List<Match> matches) {
        asyncTaskListener.onTaskCompleted(matches, ASYNC_ID);
    }
}
