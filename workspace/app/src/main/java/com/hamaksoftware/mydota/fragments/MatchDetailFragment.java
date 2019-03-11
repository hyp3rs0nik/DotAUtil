package com.hamaksoftware.mydota.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.hamaksoftware.mydota.activities.Main;
import com.hamaksoftware.mydota.asynctask.GetMatchDetails;
import com.hamaksoftware.mydota.asynctask.IAsyncTaskListener;
import com.hamaksoftware.mydota.model.AbilityUpgrade;
import com.hamaksoftware.mydota.model.Match;
import com.hamaksoftware.mydota.model.Player;
import com.hamaksoftware.mydota.utils.Utility;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchDetailFragment extends Fragment implements IAsyncTaskListener {

    private Main base;
    private ProgressDialog dialog;
    private LinearLayout graphHolder;
    private long matchId;
    private View rootView;


    private int toDp(int pixel) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pixel * scale + 0.5f);
    }

    private View getSeparator() {
        View v = new View(getActivity());
        v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
        v.setBackgroundColor(getResources().getColor(R.color.background_light_grey));
        return v;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_detail, container, false);

        base = (Main) getActivity();

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.loader_working));

        Bundle args = getArguments();
        matchId = args.getLong("matchId");

        base.invalidateOptionsMenu();
        return rootView;
    }

    public void onActivityDrawerClosed() {
        GetMatchDetails async = new GetMatchDetails(matchId);
        async.asyncTaskListener = this;
        async.execute();
    }

    Comparator<AbilityUpgrade> cmp = new Comparator<AbilityUpgrade>() {
        @Override
        public int compare(AbilityUpgrade o1, AbilityUpgrade o2) {
            return o1.time - o2.time;
        }
    };


    public int searchSpanTotal(int min, int max, List<AbilityUpgrade> upgrades) {
        int ret = 0;
        if (min < 0) min = 0;
        for (AbilityUpgrade upgrade : upgrades) {
            if (upgrade.time > min && upgrade.time < max) {
                ret += Utility.getInstance(getActivity()).getExpTable().get(upgrade.level);
            }
        }
        return ret;
    }

    public static String getUnit(int num) {
        final int K = 1000;
        final int M = K * K;
        final int B = M * M;
        if (num >= K && num < M) {
            return "K";
        } else if (num >= M && num < B) {
            return "M";
        } else if (num >= B) {
            return "B";
        } else {
            return num + "";
        }
    }


    public void drawGraph(List<AbilityUpgrade> radiant, List<AbilityUpgrade> dire, LinearLayout parent) {
        int span = 10;
        Collections.sort(radiant, cmp);
        Collections.sort(dire, cmp);

        int radiantMax = radiant.get(radiant.size() - 1).time;
        int direMax = dire.get(dire.size() - 1).time;

        int max = radiantMax > direMax ? radiantMax : direMax;

        int points = max / span;

        GraphView.GraphViewData[] datas = new GraphView.GraphViewData[span];

        final double MIN_IN_SECONDS = 0.0166666667;
        for (int i = 1; i <= span; i++) {
            int _min = (i * points) - points;
            int _max = i * points;
            int radiantTotal = searchSpanTotal(_min,_max, radiant);
            int direTotal = searchSpanTotal(_min, _max, dire);
            datas[i - 1] = new GraphView.GraphViewData(i * points * MIN_IN_SECONDS, (double) (radiantTotal - direTotal));
        }


        int screenW = toDp(base.getScreenWidth());
        LinearLayout.LayoutParams parentParam = new LinearLayout.LayoutParams(screenW / 2, screenW / 2);

        parent.setLayoutParams(parentParam);

        GraphView graphView = new LineGraphView(getActivity(), "");
        graphView.addSeries(new GraphViewSeries(datas));
        graphView.setScalable(true);
        graphView.getGraphViewStyle().setGridColor(getResources().getColor(R.color.grid_chart));
        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
        graphView.getGraphViewStyle().setGridColor(Color.BLACK);
        graphView.getGraphViewStyle().setTextSize(22f);
        ((LineGraphView) graphView).setDrawDataPoints(true);

        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (!isValueX) {
                    return Utility.getInstance(getActivity()).getPrettyNum(value);
                } else {
                    return Utility.getInstance(getActivity()).getPrettyMin(value + "");
                }
            }
        });
        graphView.setDisableTouch(true);
        graphView.setScrollable(false);
        graphView.setClickable(false);

        parent.addView(graphView);

    }

    @Override
    public void onResume() {
        super.onResume();
        //base.getActionBar().setTitle(getString(R.string.app_name) + " - " + getString(R.string.title_latest_dota_news));
        base.currentFragmentTag = R.string.fragment_tag_match_detail;
        base.invalidateOptionsMenu();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        onActivityDrawerClosed();
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
            if (asyncId.equalsIgnoreCase(GetMatchDetails.ASYNC_ID)) {
                TableLayout radiant = (TableLayout) rootView.findViewById(R.id.radiant_table);
                TableLayout dire = (TableLayout) rootView.findViewById(R.id.dire_table);
                graphHolder = (LinearLayout) rootView.findViewById(R.id.graph_holder);
                Match match = (Match)data;

                base.setTitle("Match Id: " + match.id + " (" + (match.radiantWin ? "Radiant" : "Dire") + " won)");

                final String cdnHeroImageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/$_lg.png";

                ArrayList<AbilityUpgrade> radiantUpgrades = new ArrayList<AbilityUpgrade>(0);
                ArrayList<AbilityUpgrade> direUpgrades = new ArrayList<AbilityUpgrade>(0);

                for (Player player : match.players) {


                    boolean isRadiant = (player.slot + "").length() <= 2;

                    if (isRadiant) {
                        radiantUpgrades.addAll(player.abilityUpgrades);
                    } else {
                        direUpgrades.addAll(player.abilityUpgrades);
                    }


                    TableRow row = new TableRow(getActivity());
                    TableRow.LayoutParams rowParam = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    rowParam.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

                    rowParam.bottomMargin = rowParam.topMargin = rowParam.leftMargin = rowParam.rightMargin = 0;
                    row.setPadding(toDp(5), toDp(8), toDp(5), toDp(8));
                    row.setLayoutParams(rowParam);

                    final ImageView imgIcon = new ImageView(getActivity());

                    //the api has heroId = 0; but its not going to happen in real data.

                    if (player.hero == null) {
                        //player.hero = new Select().from(Hero.class).orderBy("RANDOM()").executeSingle();
                    }


                    imgIcon.setAdjustViewBounds(true);
                    imgIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    String imageUri = cdnHeroImageUrl.replace("$", player.hero.name.replace("npc_dota_hero_", ""));
                    ImageLoader.getInstance().displayImage(imageUri, imgIcon);

                    LinearLayout imgHolder = new LinearLayout(getActivity());
                    TableRow.LayoutParams holderParams = new TableRow.LayoutParams(toDp(96), toDp(54));


                    TextView dire_hero_header = (TextView) rootView.findViewById(R.id.dire_header_name);
                    TableRow.LayoutParams headerParam = new TableRow.LayoutParams(holderParams.width, TableRow.LayoutParams.WRAP_CONTENT);
                    dire_hero_header.setLayoutParams(headerParam);

                    TextView radiant_hero_header = (TextView) rootView.findViewById(R.id.radiant_header_name);
                    radiant_hero_header.setLayoutParams(headerParam);


                    //holderParams.weight = .15f;
                    holderParams.rightMargin = toDp(6);
                    holderParams.gravity = Gravity.CENTER_HORIZONTAL;
                    imgHolder.setLayoutParams(holderParams);
                    imgHolder.addView(imgIcon);

                    row.addView(imgHolder);


                    TextView name = new TextView(getActivity());
                    TableRow.LayoutParams tParam = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    tParam.weight = .25f;
                    name.setSingleLine(true);
                    name.setEllipsize(TextUtils.TruncateAt.END);
                    name.setLayoutParams(tParam);

                    String userName = player.user == null? "Unknown" : player.user.personaName;

                    name.setText(userName);
                    name.setTextColor(isRadiant ? getResources().getColor(R.color.radiant_color) : getResources().getColor(R.color.dire_color));
                    row.addView(name);


                    TextView lvl = new TextView(getActivity());
                    TableRow.LayoutParams lvlParam = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    lvlParam.weight = .125f;
                    lvl.setLayoutParams(lvlParam);
                    //lvl.setPadding(5,5,5,5);
                    lvl.setText(player.level + "");
                    row.addView(lvl);

                    TextView kda = new TextView(getActivity());
                    TableRow.LayoutParams kdaParam = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    kdaParam.weight = .133f;
                    kda.setText(player.kills + "/" + player.deaths + "/" + player.assists);
                    kda.setLayoutParams(kdaParam);
                    //kda.setPadding(5,5,5,5);
                    row.addView(kda);

                    TextView gpm = new TextView(getActivity());
                    TableRow.LayoutParams gpmParam = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    gpmParam.weight = .125f;
                    gpm.setText(player.goldPerMin + "");
                    gpm.setLayoutParams(gpmParam);
                    //gpm.setPadding(5,5,5,5);
                    row.addView(gpm);

                    TextView xpm = new TextView(getActivity());
                    TableRow.LayoutParams xpmParam = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    xpmParam.weight = .125f;
                    xpm.setText(player.xpPerMin + "");
                    xpm.setLayoutParams(xpmParam);
                    //xpm.setPadding(5,5,5,5);
                    row.addView(xpm);

                    if (isRadiant) { //radiant
                        radiant.addView(row);
                        radiant.addView(getSeparator());
                    } else {
                        dire.addView(row);
                        dire.addView(getSeparator());
                    }


                }

                drawGraph(radiantUpgrades, direUpgrades, graphHolder);
            }


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
        //create if branch if necessary to identify which asynctask is triggering
        //this method.
        if (dialog != null) {
            dialog.show();
        }
    }


    @Override
    public void onTaskError(Exception e, final String asyncId) {
        Log.e("err", e.getMessage());
    }


}


