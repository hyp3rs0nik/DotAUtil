package com.hamaksoftware.mydota.asynctask;

import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;
import com.hamaksoftware.mydota.activeandroid.model.Item;
import com.hamaksoftware.mydota.api.APIRequest;

import java.util.List;


public class GetItems extends AsyncTask<Void, Void, Void> {
    public static final String ASYNC_ID = "GETITEMS";
    public IAsyncTaskListener asyncTaskListener;


    public GetItems(){}

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskWorking(ASYNC_ID);
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<Item> items =  APIRequest.getInstance().getItems();
        ActiveAndroid.beginTransaction();
        try {
            for(Item item: items){
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void obj) {
        asyncTaskListener.onTaskCompleted(null, ASYNC_ID);
    }


}
