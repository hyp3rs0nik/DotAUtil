package com.hamaksoftware.mydota.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.hamaksoftware.mydota.api.APIRequest;
import com.hamaksoftware.mydota.model.Match;

public class GetMatchDetails extends AsyncTask<Void, Void, Match> {
    public static final String ASYNC_ID = "GETMATCHDETAILS";
    public IAsyncTaskListener asyncTaskListener;
    private long matchId;

    public GetMatchDetails(long matchId) {
        this.matchId = matchId;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskWorking(ASYNC_ID);
    }

    @Override
    protected Match doInBackground(Void... params) {

        try {

            Match match =  APIRequest.getInstance().getMatch(matchId);
            return match;

        } catch (Exception e) {
            Log.e(ASYNC_ID, "Error parsing JSON Text: " + e.getMessage());
        }

        return null;
    }

    protected void onPostExecute(Match match) {
        asyncTaskListener.onTaskCompleted(match, ASYNC_ID);
    }
}
