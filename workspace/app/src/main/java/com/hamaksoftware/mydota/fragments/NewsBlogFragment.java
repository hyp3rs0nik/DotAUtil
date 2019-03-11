package com.hamaksoftware.mydota.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.activities.Main;
import com.hamaksoftware.mydota.adapters.NewsBlogAdapter;
import com.hamaksoftware.mydota.asynctask.GetNewsBlog;
import com.hamaksoftware.mydota.asynctask.IAsyncTaskListener;
import com.hamaksoftware.mydota.model.BlogFeed;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;


/* always implement IAsyncTaskListener,IActivityListener on fragments. 
 * do not do expensive operations on onCreateView (even if it is asynctask)
 * it would stutter the drawerlayout closing animations.
 * instead, override onActivityDrawerClosed and run asynctask from there.
 * this would ensure that the drawer has completely closed before doing operation
 * on the fragment.
 */
public class NewsBlogFragment extends Fragment implements IAsyncTaskListener {

    private PullToRefreshListView lv;
    private GetNewsBlog async;
    private NewsBlogAdapter adapter;
    private Main base;
    private ProgressDialog dialog;
    private String cat;
    private String subcat;
    private boolean force;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.news_blog_main, container, false);

        base = (Main) getActivity();

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true); //i am using a dialog with a progress indicator, set to true if not tracking progress.
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.loader_working));

        lv = (PullToRefreshListView)rootView.findViewById(R.id.lvNewsBlog);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                lv.setRefreshing();
                onActivityDrawerClosed();
            }
        });

        if (adapter == null) {
            adapter = new NewsBlogAdapter(getActivity());
            adapter.setBlogFeeds(new ArrayList<BlogFeed>());
        }

        lv.getRefreshableView().setAdapter(adapter);

        lv.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Bundle args = new Bundle();
                String link = adapter.getBlogFeeds().get(index).getLink();
                args.putString("link", link);
                base.launchFragment(R.string.fragment_tag_news_blog_viewer, args, true);
            }
        });

        base.invalidateOptionsMenu();

        return rootView;
    }

    public void onActivityDrawerClosed() {
        async = new GetNewsBlog(getActivity());
        async.asyncTaskListener = this; //set this class as observer to listen to asynctask events
        async.execute();
    }


    @Override
    public void onResume() {
        super.onResume();
        base.getActionBar().setTitle(getString(R.string.app_name) + " - " + getString(R.string.title_latest_dota_news));
        base.currentFragmentTag = R.string.fragment_tag_news_blog;
        base.invalidateOptionsMenu();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (force || adapter.getBlogFeeds().size() <= 0) {
            adapter.getBlogFeeds().clear();
            onActivityDrawerClosed();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        base.setTitle(getString(R.string.app_name));
        base.currentFragmentTag = 0;
        base.invalidateOptionsMenu();
    }

    @Override
    public void onTaskCompleted(Object data, String asyncId) {
        if (data != null) {

            //this data is coming from GetHeroes Asynctask
            if (asyncId.equalsIgnoreCase(GetNewsBlog.ASYNC_ID)) {
                if(lv.isRefreshing()) lv.onRefreshComplete();
                ArrayList<BlogFeed> blogFeeds = (ArrayList<BlogFeed>) data;
                if (blogFeeds.size() <= 0) {
                    /*
                    String title = getResources().getString(R.string.loader_title_request_result);
					String msg = getResources().getString(R.string.result_listing_error);
					String btnPosTitle = getResources().getString(R.string.dialog_button_ok);
					Utility.showDialog(getActivity(),title, msg, btnPosTitle, null, false,null);
					*/
                    //we need to make a more elegant way of showing dialogs.


                } else {
                    adapter.setBlogFeeds(blogFeeds);
                    adapter.notifyDataSetChanged();
                }
            }

            //create a new branch if data is coming from different asynctask.
            // if(){}


        } else {
            //TODO Handle
            //error occurred or no data at all.
        }
        dialog.dismiss();
    }

    @Override
    public void onTaskProgressUpdate(int progress, final String asyncId) {
        dialog.setProgress(progress);
    }

    @Override
    public void onTaskProgressMax(int max, final String asyncId) {
        dialog.setMax(max);
    }

    @Override
    public void onTaskUpdateMessage(final String message, final String asyncId) {
        //this is called from doInBackground which is not on UI thread.
        //show the dialog message on UI thread instead.
        base.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage(message);
            }
        });
    }


    @Override
    public void onTaskWorking(final String asyncId) {
        if(dialog != null && !lv.isRefreshing()){
            dialog.show();
        }
    }


    @Override
    public void onTaskError(Exception e, final String asyncId) {
        Log.e("err", e.getMessage());
    }


}


