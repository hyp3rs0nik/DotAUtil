package com.hamaksoftware.mydota.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.activeandroid.model.Item;
import com.hamaksoftware.mydota.adapters.DrawerGroupAdapter;
import com.hamaksoftware.mydota.api.APIRequestException;
import com.hamaksoftware.mydota.asynctask.GetHeroes;
import com.hamaksoftware.mydota.asynctask.GetItems;
import com.hamaksoftware.mydota.asynctask.GetSteamProfileDirect;
import com.hamaksoftware.mydota.asynctask.IAsyncTaskListener;
import com.hamaksoftware.mydota.fragments.IActivityListener;
import com.hamaksoftware.mydota.fragments.MatchByHeroFragment;
import com.hamaksoftware.mydota.fragments.MatchDetailFragment;
import com.hamaksoftware.mydota.fragments.MatchLatestFragment;
import com.hamaksoftware.mydota.fragments.NewsBlogFragment;
import com.hamaksoftware.mydota.fragments.NewsBlogViewerFragment;
import com.hamaksoftware.mydota.model.SteamProfileDirect;
import com.hamaksoftware.mydota.utils.AppPref;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Main extends Activity implements IAsyncTaskListener{
    /* private members */
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    private DrawerGroupAdapter listDrawerAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private AppPref pref;

    //private String cat, subcat;

    //private static String strfilter;

    /* public members */
    public IActivityListener activityListener;
    public int currentFragmentTag;
    FragmentManager fragmentManager;
    private View sideBarHeader;

    private void prepareListData() {

        //find me in res/values

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        String[] categories = getResources().getStringArray(R.array.categories);
        for (int i = 0; i < categories.length; i++) {
            try {
                JSONArray jsonarr = new JSONArray(categories[i]);
                listDataHeader.add(jsonarr.getString(0));
                List<String> obj = new ArrayList<String>(0);
                for (int j = 1; j < jsonarr.length(); j++) {
                    obj.add(jsonarr.getString(j));
                    listDataChild.put(listDataHeader.get(i), obj);
                }


            } catch (JSONException e) {
                Log.e("prepareData", e.getMessage());
            }
        }

    }

    public void startSteamAuth(){
        Intent i = new Intent(Main.this,AuthSteamAPIWeb.class);
        i.putExtra("uri","");
        startActivityForResult(i, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = new AppPref(getApplicationContext());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();


        ImageLoader.getInstance().init(config);


        mTitle = mDrawerTitle = getResources().getString(R.string.app_name);
        //mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        prepareListData();

        sideBarHeader = getLayoutInflater().inflate(R.layout.drawer_main_group_header, null);
        if (sideBarHeader != null) {
            mDrawerList.addHeaderView(sideBarHeader);
            LinearLayout holder = (LinearLayout) sideBarHeader.findViewById(R.id.profile_holder);
            ImageView profileIcon = (ImageView) sideBarHeader.findViewById(R.id.profile_icon);
            if (holder != null) {
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startSteamAuth();
                    }
                });
            }

            if (profileIcon != null && !pref.getSteamProfilePic().equals("")) {
                profileIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pref.getSteamProfileLink()));
                        startActivity(browserIntent);
                    }
                });
            }
        }



        listDrawerAdapter = new DrawerGroupAdapter(this, listDataHeader, listDataChild);
        mDrawerList.setAdapter(listDrawerAdapter);

        fragmentManager = getFragmentManager();

        final DrawerChildClickListener listener = new DrawerChildClickListener();

        mDrawerList.setOnGroupClickListener(new DrawerGroupItemClickListener());
        mDrawerList.setOnChildClickListener(listener);

        mDrawerList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                LinearLayout parent = (LinearLayout) mDrawerList.getChildAt(i + 1); //because of the header
                ImageView expand = (ImageView) parent.findViewById(R.id.drawer_collapse);
                if (expand != null) {
                    expand.setImageResource(R.drawable.ic_action_expand);
                }
            }
        });

        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                LinearLayout parent = (LinearLayout) mDrawerList.getChildAt(i + 1); ////because of the header
                ImageView collapse = (ImageView) parent.findViewById(R.id.drawer_collapse);
                if (collapse != null) {
                    collapse.setImageResource(R.drawable.ic_action_collapse);
                }
            }
        });

        listener.onChildClick(null, null, pref.getDefaultCat(), pref.getDefaultSubCat(), 100000L);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                if (currentFragmentTag == R.string.fragment_tag_news_blog) {
                    Bundle args = new Bundle();
                    launchFragment(R.string.fragment_tag_news_blog, args, false);
                }

                if (currentFragmentTag == R.string.fragment_tag_match_history_latest) {
                    Bundle args = new Bundle();
                    launchFragment(R.string.fragment_tag_match_history_latest, args, false);
                }

                if (currentFragmentTag == R.string.fragment_tag_match_history_by_hero) {
                    Bundle args = new Bundle();
                    launchFragment(R.string.fragment_tag_match_history_by_hero, args, false);
                }

            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            //selectItem(0);
        }

        launchFragment(R.string.fragment_tag_news_blog, null, true);


        if (!pref.getSteamId().equals("")) {
            updateSteamInfo();
        }


        //just checking if the db is populated.
        /*
        boolean haveHeroes = new Select().from(Hero.class).count() > 0;

        if (haveHeroes) {
            GetHeroes async = new GetHeroes();
            async.asyncTaskListener = this;
            async.execute();
        }
        */
        GetHeroes h = new GetHeroes();
        h.asyncTaskListener = this;
        h.execute();

        boolean haveItems = new Select().from(Item.class).execute().size() > 0;

        if(!haveItems){
            GetItems async = new GetItems();
            async.asyncTaskListener = this;
            async.execute();
        }

    }

    public int getScreenWidth(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public void launchFragment(int fragmentTag, Bundle params, boolean force) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (fragmentTag) {
            case R.string.fragment_tag_news_blog:
                NewsBlogFragment blogFragment = (NewsBlogFragment) fragmentManager
                        .findFragmentByTag(getString(R.string.fragment_tag_news_blog));

                if(blogFragment == null){
                    blogFragment = new NewsBlogFragment();
                    blogFragment.setArguments(params);
                }
                transaction.replace(R.id.content_frame, blogFragment, getString(R.string.fragment_tag_news_blog));
                break;
            case R.string.fragment_tag_news_blog_viewer:
                NewsBlogViewerFragment viewerFragment = (NewsBlogViewerFragment) fragmentManager
                        .findFragmentByTag(getString(R.string.fragment_tag_news_blog_viewer));

                if(viewerFragment == null){
                    viewerFragment = new NewsBlogViewerFragment();
                    viewerFragment.setArguments(params);
                }
                transaction.replace(R.id.content_frame, viewerFragment, getString(R.string.fragment_tag_news_blog_viewer));
                break;

            case R.string.fragment_tag_match_history_latest:
                MatchLatestFragment matchesFragment = (MatchLatestFragment) fragmentManager
                        .findFragmentByTag(getString(R.string.fragment_tag_match_history_latest));

                if(matchesFragment == null){
                    matchesFragment = new MatchLatestFragment();
                    matchesFragment.setArguments(params);
                }else{
                    matchesFragment.getArguments().clear();
                    matchesFragment.getArguments().putBoolean("force", true);
                    matchesFragment.getArguments().putLong("heroId", params.getLong("heroId"));
                }
                transaction.replace(R.id.content_frame, matchesFragment, getString(R.string.fragment_tag_match_history_latest));
                break;
            case R.string.fragment_tag_match_history_by_hero:
                MatchByHeroFragment matchByHeroFragment  = (MatchByHeroFragment)fragmentManager
                        .findFragmentByTag(getString(R.string.fragment_tag_match_history_by_hero));

                if(matchByHeroFragment == null){
                    matchByHeroFragment = new MatchByHeroFragment();
                    matchByHeroFragment.setArguments(params);
                }

                transaction.replace(R.id.content_frame, matchByHeroFragment, getString(R.string.fragment_tag_match_history_by_hero));
                break;
            case R.string.fragment_tag_match_detail:
                MatchDetailFragment detailFragment = (MatchDetailFragment) fragmentManager
                        .findFragmentByTag(getString(R.string.fragment_tag_match_detail));

                if(detailFragment == null){
                    detailFragment = new MatchDetailFragment();
                    detailFragment.setArguments(params);
                }
                transaction.replace(R.id.content_frame, detailFragment, getString(R.string.fragment_tag_match_detail));
                break;

        }

        transaction.addToBackStack(null);
        transaction.commit();
        invalidateOptionsMenu();
        currentFragmentTag = fragmentTag; //just in case

        Log.i("steamid",pref.getSteamId());

    }



    public void updateSteamInfo(){
        GetSteamProfileDirect async = new GetSteamProfileDirect(getApplicationContext(),pref.getSteamId());
        async.asyncTaskListener = this;
        async.execute();
    }


    public void updateSideBarUISteamInfo(){
        if(sideBarHeader != null){
            PrettyTime prettyTime = new PrettyTime();
            Date dte = new Date(pref.getSteamLastLogoff() * 1000L);
            TextView profileName = (TextView)sideBarHeader.findViewById(R.id.profile_name);
            profileName.setText(pref.getSteamName());
            TextView lastLogoff = (TextView)sideBarHeader.findViewById(R.id.profile_activity);
            lastLogoff.setText(getText(R.string.steam_info_activity).toString().replace("$",
                    prettyTime.format(dte)));
            ImageView profileIcon = (ImageView)sideBarHeader.findViewById(R.id.profile_icon);
            ImageLoader.getInstance().displayImage(pref.getSteamProfilePic(), profileIcon);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            System.out.println("activity result called.");
            if(resultCode == RESULT_OK){
                boolean success = data.getBooleanExtra("success",false);
                if(success){
                    updateSideBarUISteamInfo();
                }
            }

            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.heroes_grid_sort, menu);
        */
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        /*If the nav drawer is open, hide action items related to the content view
        final boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_server_status_on).setVisible(false);
        menu.findItem(R.id.action_server_status_off).setVisible(true);
		*/


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTaskWorking(String ASYNC_ID) {

    }

    private class DrawerGroupItemClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {

            if(listDrawerAdapter.getChildrenCount(groupPosition) <= 0 ) {
                String category = listDataHeader.get(groupPosition);
                if(category.equals(getString(R.string.cat_dota_news))){
                    currentFragmentTag = R.string.fragment_tag_news_blog;
                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }

            return false;
        }
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerChildClickListener implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            activityListener = null; //reset listener to avoid "unnecessary" triggering of async on onDrawerClosed

            String cat = listDataHeader.get(groupPosition);
            if(listDrawerAdapter.getChildrenCount(groupPosition) > 0) { //ensure that this category has children
                String subcat = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                if (cat.equals(getString(R.string.cat_match_history)) && subcat.equals(getString(R.string.subcat_match_history_latest))) {
                    currentFragmentTag = R.string.fragment_tag_match_history_latest;
                }

                if(cat.equals(getString(R.string.cat_match_history)) && subcat.equals(getString(R.string.subcat_match_history_by_hero))){
                    currentFragmentTag = R.string.fragment_tag_match_history_by_hero;
                }

                mDrawerList.setItemChecked(childPosition, true);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
            return false;
        }

    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        if(data != null){
            if(ASYNC_ID.equals(GetSteamProfileDirect.ASYNC_ID)){
                SteamProfileDirect p = (SteamProfileDirect)data;
                pref.setSteamName(p.personaname);
                pref.setSteamProfilePic(p.avatarfull);
                pref.setSteamLastLogoff(p.lastlogoff);
                pref.setSteamProfileLink(p.profileurl);
                updateSideBarUISteamInfo();
            }
        }
    }

    @Override
    public void onTaskError(Exception e, String ASYNC_ID) {
        if(ASYNC_ID.equalsIgnoreCase(GetHeroes.ASYNC_ID) || ASYNC_ID.equalsIgnoreCase(GetItems.ASYNC_ID)){
            APIRequestException err = (APIRequestException)e;
            Log.e("api",err.getStatus().toString());
        }
    }


}