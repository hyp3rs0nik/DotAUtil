package com.hamaksoftware.mydota.asynctask;

import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.hamaksoftware.mydota.api.APIRequest;
import com.hamaksoftware.mydota.api.APIRequestException;


import java.util.List;


public class GetHeroes extends AsyncTask<Void, Void, List<Hero>> {
    public static final String ASYNC_ID = "GETHEROES";
    public IAsyncTaskListener asyncTaskListener;


    public GetHeroes() {
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskWorking(ASYNC_ID);
    }

    @Override
    protected List<Hero> doInBackground(Void... params) {

        try {
            List<Hero> h = APIRequest.getInstance().getHeroes();
            ActiveAndroid.beginTransaction();
            new Delete().from(Hero.class).execute();
            try {
                for (Hero hero : h) {
                    hero.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
            return h;
        }catch (APIRequestException e){
            asyncTaskListener.onTaskError(e,ASYNC_ID);
        }

        return null;

    }

    @Override
    protected void onPostExecute(List<Hero> heroes) {
        asyncTaskListener.onTaskCompleted(heroes, ASYNC_ID);
    }


}
