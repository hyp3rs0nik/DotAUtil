package com.hamaksoftware.mydota.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hamaksoftware.mydota.asynctask.GetSteamProfileDirect;
import com.hamaksoftware.mydota.asynctask.IAsyncTaskListener;
import com.hamaksoftware.mydota.model.SteamProfileDirect;
import com.hamaksoftware.mydota.utils.AppPref;

public class AuthSteamAPIWeb extends Activity implements IAsyncTaskListener {
    final String REALM_PARAM = "DotaUtil";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        final WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                //checks the url being loaded
                setTitle(url);
                Uri Url = Uri.parse(url);

                if (Url.getAuthority().equals(REALM_PARAM.toLowerCase())) {
                    webView.stopLoading();
                    Uri userAccountUrl = Uri.parse(Url.getQueryParameter("openid.identity"));
                    String steamId = userAccountUrl.getLastPathSegment();

                    if(steamId.length() > 0){
                        GetSteamProfileDirect getInfo = new GetSteamProfileDirect(getApplicationContext(), steamId);
                        getInfo.asyncTaskListener = AuthSteamAPIWeb.this;
                        getInfo.execute();
                    }


                }

            }
        });



        setContentView(webView);

        // Constructing openid url request
        String url = "https://steamcommunity.com/openid/login?" +
                "openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.identity=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.mode=checkid_setup&" +
                "openid.ns=http://specs.openid.net/auth/2.0&" +
                "openid.realm=https://" + REALM_PARAM + "&" +
                "openid.return_to=https://" + REALM_PARAM + "/signin/";

        webView.loadUrl(url);
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
        SteamProfileDirect profile = (SteamProfileDirect)data;

        AppPref pref = new AppPref(getApplicationContext());
        pref.setSteamId(profile.steamid);
        pref.setSteamName(profile.personaname);
        pref.setSteamProfilePic(profile.avatarfull);
        pref.setSteamLastLogoff(profile.lastlogoff);

        finishActivity(profile.steamid);
    }

    @Override
    public void onTaskError(Exception e, String ASYNC_ID) {
        finishActivity("");
    }


    public void finishActivity(String steamId){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("success",steamId.length() > 0);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
