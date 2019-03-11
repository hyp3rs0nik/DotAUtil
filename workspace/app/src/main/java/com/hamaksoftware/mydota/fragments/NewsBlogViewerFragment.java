package com.hamaksoftware.mydota.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


import com.hamaksoftware.mydota.R;
import com.hamaksoftware.mydota.activities.Main;
public class NewsBlogViewerFragment extends Fragment {
    private Main base;
    private ProgressDialog dialog;
    int imageNumber = 1;
    private SpannableStringBuilder htmlSpannable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.news_blog_item_viewer, container, false);

        base = (Main) getActivity();

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(false); //i am using a dialog with a progress indicator, set to true if not tracking progress.
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage(getString(R.string.loader_working));

        String link = getArguments().getString("link");
        WebView webView = (WebView) rootView.findViewById(R.id.wbNewsBlogContent);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(link);

        return rootView;
    }



}


