package com.hamaksoftware.mydota.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.model.BlogFeed;
import com.hamaksoftware.mydota.utils.Utility;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JV on 6/17/2014.
 */


public class NewsBlogAdapter extends BaseAdapter {
    private List<BlogFeed> blogFeeds;
    private List<BlogFeed> copyFeeds;
    private Context ctx;


    class ViewHolder {
        TextView title;
        TextView pubdate;
        TextView description;
    }


    public NewsBlogAdapter(Context context) {
        this.ctx = context;
    }

    public void setBlogFeeds(ArrayList<BlogFeed> blogFeeds) {
        this.blogFeeds = blogFeeds;
    }

    public List<BlogFeed> getBlogFeeds() {
        return this.blogFeeds;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.heroes_list_row, null);
            convertView = inflater.inflate(R.layout.news_blog_main_row, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.txtNewsBlogTitle);
            holder.pubdate = (TextView) convertView.findViewById(R.id.txtNewsPubDate);
            holder.description = (TextView) convertView.findViewById(R.id.txtNewsDescription);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BlogFeed blogFeed = blogFeeds.get(position);
        holder.title.setText(blogFeed.getTitle());
        holder.description.setText(Jsoup.parse(blogFeed.getDescription()).text());

        return convertView;
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return blogFeeds.size();
    }

    @Override
    public Object getItem(int position) {
        return blogFeeds.get(position);
    }
}
