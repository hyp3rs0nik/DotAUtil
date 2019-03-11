package com.hamaksoftware.mydota.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.activities.Main;
import com.hamaksoftware.mydota.adapters.MatchLatestAdapter;
import com.hamaksoftware.mydota.asynctask.GetUserMatches;
import com.hamaksoftware.mydota.asynctask.IAsyncTaskListener;
import com.hamaksoftware.mydota.model.Match;
import com.hamaksoftware.mydota.utils.AppPref;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
public class MatchLatestFragment extends Fragment implements IAsyncTaskListener {
    private PullToRefreshListView lv;
    private GetUserMatches async;
    private MatchLatestAdapter adapter;
    private Main base;
    private ProgressDialog dialog;
    private boolean force;
    private AppPref pref;
    private long heroId = -1;

    private final String cdnHeroImageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/$_lg.png";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.matches_latest_main, container, false);

        base = (Main) getActivity();

        pref= new AppPref(getActivity());

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true); //i am using a dialog with a progress indicator, set to true if not tracking progress.
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.loader_working));

        lv = (PullToRefreshListView ) rootView.findViewById(R.id.lvMatchesLatest);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                lv.setRefreshing();
                onActivityDrawerClosed();
            }
        });


        if (adapter == null) {
            adapter = new MatchLatestAdapter(getActivity());
            adapter.setMatches(new ArrayList<Match>());
        }

        Bundle args = getArguments();
        if(args.containsKey("heroId")){
            heroId = args.getLong("heroId");
        }

        force = args.containsKey("heroId");

        lv.getRefreshableView().setAdapter(adapter);

        lv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                int headerCount = 0;

                if(lv != null){
                    headerCount = lv.getRefreshableView().getHeaderViewsCount();
                }
                Bundle args = new Bundle();
                args.putLong("matchId",adapter.getItemId(index - headerCount));
                base.launchFragment(R.string.fragment_tag_match_detail,args,true);
            }
        });

        base.invalidateOptionsMenu();
        return rootView;
    }

    public void onActivityDrawerClosed() {
        async = new GetUserMatches(pref.getSteamId(), heroId);
        async.asyncTaskListener = this; //set this class as observer to listen to asynctask events
        async.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        base.getActionBar().setTitle(getString(R.string.app_name) + " - " + getString(R.string.title_latest_matches));
        base.currentFragmentTag = R.string.fragment_tag_match_history_latest;
        base.invalidateOptionsMenu();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (force || adapter.getMatches().size() <= 0) {
            adapter.getMatches().clear();
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
            if (asyncId.equalsIgnoreCase(GetUserMatches.ASYNC_ID)) {
                if(lv.isRefreshing()) lv.onRefreshComplete();
                ArrayList<Match> matches = (ArrayList<Match>) data;
                if (matches.size() <= 0) {
                    /*
                    String title = getResources().getString(R.string.loader_title_request_result);
					String msg = getResources().getString(R.string.result_listing_error);
					String btnPosTitle = getResources().getString(R.string.dialog_button_ok);
					Utility.showDialog(getActivity(),title, msg, btnPosTitle, null, false,null);
					*/
                    //we need to make a more elegant way of showing dialogs.
                } else {
                    adapter.setMatches(matches);
                    adapter.notifyDataSetChanged();
                }


            }

            //create a new branch if data is coming from different asynctask.
            // if(){}


        } else {
            //TODO Handle
            //error occurred or no data at all.
        }
        if(dialog.isShowing()) dialog.dismiss();

    }

    @Override
    public void onTaskWorking(String ASYNC_ID) {
        if(dialog != null && !lv.isRefreshing()){
            dialog.show();
        }
    }

    @Override
    public void onTaskProgressUpdate(int progress, String ASYNC_ID) {
        dialog.setProgress(progress);
    }

    @Override
    public void onTaskProgressMax(int max, String ASYNC_ID) {
        dialog.setMax(max);
    }

    @Override
    public void onTaskUpdateMessage(final String message, String ASYNC_ID) {

    }

    @Override
    public void onTaskError(Exception e, String ASYNC_ID) {
        Log.e("err", e.getMessage());
    }
}
