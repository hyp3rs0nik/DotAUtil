package com.hamaksoftware.mydota.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.hamaksoftware.mydota.activities.Main;
import com.hamaksoftware.mydota.adapters.MatchByHeroAdapter;
import com.hamaksoftware.mydota.adapters.MatchLatestAdapter;
import com.hamaksoftware.mydota.asynctask.GetHeroes;
import com.hamaksoftware.mydota.asynctask.GetUserMatches;
import com.hamaksoftware.mydota.asynctask.IAsyncTaskListener;
import com.hamaksoftware.mydota.model.Match;
import com.hamaksoftware.mydota.utils.AppPref;

import java.util.ArrayList;
import java.util.List;

public class MatchByHeroFragment extends Fragment implements IAsyncTaskListener{

    private Main base;
    private AppPref pref;
    private MatchByHeroAdapter adapter;
    private GridView gv;
    private boolean force;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.heroes_grid_view, container, false);
        base = (Main) getActivity();
        pref = new AppPref(getActivity());

        gv = (GridView)rootView.findViewById(R.id.heroes_grid);
        if (adapter == null) {
            adapter = new MatchByHeroAdapter(getActivity());
            adapter.heroes = new ArrayList<Hero>(0);
        }

        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Bundle args = new Bundle();
                args.putLong("heroId",adapter.getItemId(index));
                args.putBoolean("force", true);
                base.launchFragment(R.string.fragment_tag_match_history_latest,args,true);
            }
        });

        base.invalidateOptionsMenu();

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        base.getActionBar().setTitle(getString(R.string.filter_match_by_hero));
        base.currentFragmentTag = R.string.fragment_tag_match_history_by_hero;
        base.invalidateOptionsMenu();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        base.setTitle(getString(R.string.app_name));
        base.currentFragmentTag = 0;
        base.invalidateOptionsMenu();
    }


    public void onActivityDrawerClosed() {
        GetHeroes getHeroes = new GetHeroes();
        getHeroes.asyncTaskListener = this;
        getHeroes.execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (force || adapter.heroes.size() <= 0) {
            adapter.heroes.clear();
            onActivityDrawerClosed();
        }
    }

    @Override
    public void onTaskWorking(String ASYNC_ID) {

    }

    @Override
    public void onTaskProgressUpdate(int progress, String ASYNC_ID) {

    }

    @Override
    public void onTaskProgressMax(int max, String ASYNC_ID) {

    }

    @Override
    public void onTaskUpdateMessage(String message, String ASYNC_ID) {

    }

    @Override
    public void onTaskCompleted(Object data, String ASYNC_ID) {
        if(ASYNC_ID.equals(GetHeroes.ASYNC_ID)){
            if(data != null){
                List<Hero> heroes = (List<Hero>)data;
                adapter.heroes = heroes;
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onTaskError(Exception e, String ASYNC_ID) {

    }
}