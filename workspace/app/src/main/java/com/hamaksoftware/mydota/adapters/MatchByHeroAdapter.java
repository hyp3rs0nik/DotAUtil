package com.hamaksoftware.mydota.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MatchByHeroAdapter extends BaseAdapter{
    public List<Hero> heroes;
    private Context ctx;
    private final String cdnHeroImageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/$_lg.png";

    class ViewHolder{
        ImageView heroImage;
        TextView  heroName;
    }

    public MatchByHeroAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return heroes.size();
    }

    @Override
    public Hero getItem(int position) {
        return heroes.get(position);
    }

    @Override
    public long getItemId(int i) {
        return heroes.get(i).heroId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.heroes_grid_row, null);
            holder = new ViewHolder();
            holder.heroImage = (ImageView)convertView.findViewById(R.id.heroes_grid_img);
            holder.heroName = (TextView)convertView.findViewById(R.id.heroes_grid_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Hero hero = heroes.get(position);

        ImageLoader.getInstance().displayImage(
                cdnHeroImageUrl.replace("$", hero.name.replace("npc_dota_hero_", "")),holder.heroImage);

        holder.heroName.setText(hero.localizedName);

        return convertView;
    }

}
