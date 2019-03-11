package com.hamaksoftware.mydota.asynctask;

import android.os.AsyncTask;

import com.hamaksoftware.mydota.api.APIRequest;
import com.hamaksoftware.mydota.model.League;

import java.util.List;


public class GetLeagues extends AsyncTask<Void, Void, List<League>> {
    public static final String ASYNC_ID = "GETLEAGUES";
    public IAsyncTaskListener asyncTaskListener;


    public GetLeagues(){}

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskWorking(ASYNC_ID);
    }

    @Override
    protected List<League> doInBackground(Void... params) {
        List<League> leagues =  APIRequest.getInstance().getLeagues();
        return leagues;
    }

    @Override
    protected void onPostExecute(List<League> leagues) {
        asyncTaskListener.onTaskCompleted(leagues, ASYNC_ID);
    }


}
