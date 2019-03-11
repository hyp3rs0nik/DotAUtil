package com.hamaksoftware.mydota.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hamaksoftware.mydota.model.BlogFeed;
import com.hamaksoftware.mydota.utils.RSSReader;

import java.util.ArrayList;


public class GetNewsBlog extends AsyncTask<Void, Void, ArrayList<BlogFeed>> {
    public static final String ASYNC_ID = "GETNEWSBLOG";
    public IAsyncTaskListener asyncTaskListener;

    private Context ctx;
    private String response = "";
    private ArrayList<BlogFeed> blogFeeds;

    public GetNewsBlog(Context ctx) {
        this.ctx = ctx;
        blogFeeds = new ArrayList<BlogFeed>(0);
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskWorking(ASYNC_ID);
    }

    @Override
    protected ArrayList<BlogFeed> doInBackground(Void... params) {

        try {
            String feed = "http://blog.dota2.com/feed/";
            blogFeeds = RSSReader.getInstance(ctx).getXmlHandler().getLatestFeeds(feed);
        } catch (Exception e) {
            Log.e(ASYNC_ID, "Error parsing JSON Text: " + e.getMessage());
        }

        return blogFeeds;

    }

    protected void onPostExecute(ArrayList<BlogFeed> blogFeeds) {
        asyncTaskListener.onTaskCompleted(blogFeeds, ASYNC_ID);
    }


}
