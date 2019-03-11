package com.hamaksoftware.mydota.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.model.Match;
import com.hamaksoftware.mydota.model.Player;
import com.hamaksoftware.mydota.utils.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchLatestAdapter extends BaseAdapter {
    private List<Match> matches;
    private Context ctx;
    private final String cdnHeroImageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/$_lg.png";


    class ViewHolder {

        ImageView heroRadiant1;
        ImageView heroRadiant2;
        ImageView heroRadiant3;
        ImageView heroRadiant4;
        ImageView heroRadiant5;

        ImageView heroDire1;
        ImageView heroDire2;
        ImageView heroDire3;
        ImageView heroDire4;
        ImageView heroDire5;

        TextView heroRadiantName1;
        TextView heroRadiantName2;
        TextView heroRadiantName3;
        TextView heroRadiantName4;
        TextView heroRadiantName5;

        TextView heroDireName1;
        TextView heroDireName2;
        TextView heroDireName3;
        TextView heroDireName4;
        TextView heroDireName5;

        TextView lobbyType;
        TextView elapsed;

    }

    public MatchLatestAdapter(Context context) {
        this.ctx = context;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return this.matches;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.matches_latest_row, null);
            holder = new ViewHolder();

            holder.lobbyType = (TextView)convertView.findViewById(R.id.lobbyType);
            holder.elapsed = (TextView)convertView.findViewById(R.id.match_elapsed);
            //DIRE
            holder.heroDire1 = (ImageView) convertView.findViewById(R.id.imgMatchLatestDireHero1);
            holder.heroDire1.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroDire2 = (ImageView) convertView.findViewById(R.id.imgMatchLatestDireHero2);
            holder.heroDire2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroDire3 = (ImageView) convertView.findViewById(R.id.imgMatchLatestDireHero3);
            holder.heroDire3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroDire4 = (ImageView) convertView.findViewById(R.id.imgMatchLatestDireHero4);
            holder.heroDire4.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroDire5 = (ImageView) convertView.findViewById(R.id.imgMatchLatestDireHero5);
            holder.heroDire5.setScaleType(ImageView.ScaleType.FIT_CENTER);

            holder.heroRadiantName1 = (TextView)convertView.findViewById(R.id.heroRadiantName1);
            holder.heroRadiantName2 = (TextView)convertView.findViewById(R.id.heroRadiantName2);
            holder.heroRadiantName3 = (TextView)convertView.findViewById(R.id.heroRadiantName3);
            holder.heroRadiantName4 = (TextView)convertView.findViewById(R.id.heroRadiantName4);
            holder.heroRadiantName5 = (TextView)convertView.findViewById(R.id.heroRadiantName5);


            //Radiant
            holder.heroRadiant1 = (ImageView) convertView.findViewById(R.id.imgMatchLatestHeroRadiant1);
            holder.heroRadiant1.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroRadiant2 = (ImageView) convertView.findViewById(R.id.imgMatchLatestHeroRadiant2);
            holder.heroRadiant2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroRadiant3 = (ImageView) convertView.findViewById(R.id.imgMatchLatestHeroRadiant3);
            holder.heroRadiant3.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroRadiant4 = (ImageView) convertView.findViewById(R.id.imgMatchLatestHeroRadiant4);
            holder.heroRadiant4.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.heroRadiant5 = (ImageView) convertView.findViewById(R.id.imgMatchLatestHeroRadiant5);
            holder.heroRadiant5.setScaleType(ImageView.ScaleType.FIT_CENTER);

            holder.heroDireName1 = (TextView)convertView.findViewById(R.id.heroDireName1);
            holder.heroDireName2 = (TextView)convertView.findViewById(R.id.heroDireName2);
            holder.heroDireName3 = (TextView)convertView.findViewById(R.id.heroDireName3);
            holder.heroDireName4 = (TextView)convertView.findViewById(R.id.heroDireName4);
            holder.heroDireName5 = (TextView)convertView.findViewById(R.id.heroDireName5);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Match match = matches.get(position);

        holder.lobbyType.setText(match.lobbyType.getDescription());
        Date dte = new Date(match.startTime * 1000L);
        holder.elapsed.setText(Utility.getInstance(ctx).getPrettytime().format(dte));


        for (Player player : match.players) {
            if (player.hero != null) {
                String imageUri = cdnHeroImageUrl.replace("$", player.hero.name.replace("npc_dota_hero_", ""));

                String name = player.user == null? "Unknown" : player.user.personaName; //lol, use string resources later.
                switch (player.slot) {
                    case 0:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroRadiant1);
                        holder.heroRadiantName1.setText(name);
                        break;
                    case 1:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroRadiant2);
                        holder.heroRadiantName2.setText(name);
                        break;
                    case 2:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroRadiant3);
                        holder.heroRadiantName3.setText(name);
                        break;
                    case 3:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroRadiant4);
                        holder.heroRadiantName4.setText(name);
                        break;
                    case 4:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroRadiant5);
                        holder.heroRadiantName5.setText(name);
                        break;
                    case 128:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroDire1);
                        holder.heroDireName1.setText(name);
                        break;
                    case 129:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroDire2);
                        holder.heroDireName2.setText(name);
                        break;
                    case 130:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroDire3);
                        holder.heroDireName3.setText(name);
                        break;
                    case 131:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroDire4);
                        holder.heroDireName4.setText(name);
                        break;
                    case 132:
                        ImageLoader.getInstance().displayImage(imageUri, holder.heroDire5);
                        holder.heroDireName5.setText(name);
                        break;

                }
            }

        }


        return convertView;
    }

    public long getItemId(int position) {
        return matches.get(position).id;
    }

    public int getCount() {
        return matches.size();
    }

    @Override
    public Object getItem(int position) {
        return matches.get(position);
    }


}
