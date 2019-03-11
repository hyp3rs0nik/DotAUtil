package com.hamaksoftware.mydota.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

import java.util.UUID;

public class AppPref {

    private SharedPreferences _sharedPrefs;
    public Editor editor;
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private Context c;

    public AppPref(Context context) {
        //this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        c = context;
        _sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = _sharedPrefs.edit();
    }

    public SharedPreferences getPreference() {
        return _sharedPrefs;
    }

    public int getDefaultCat() {
        return _sharedPrefs.getInt("default_cat", 0);
    }

    public void setDefaultCat(int data) {
        editor.putInt("default_cat", data);
        editor.commit();
    }

    public int getDefaultSubCat() {
        return _sharedPrefs.getInt("default_subcat", 0);
    }

    public void setDefaultSubCat(int data) {
        editor.putInt("default_subcat", data);
        editor.commit();
    }

    public String getSteamId(){
        return _sharedPrefs.getString("steam_id", "");
    }
    public void setSteamId(String steamId){
        editor.putString("steam_id", steamId);
        editor.commit();
    }

    public String getSteamName(){
        return _sharedPrefs.getString("steam_name", "");
    }
    public void setSteamName(String steamName){
        editor.putString("steam_name", steamName);
        editor.commit();
    }

    public String getSteamProfilePic(){
        return _sharedPrefs.getString("steam_profile_pic", "");
    }
    public void setSteamProfilePic(String steamPicUrl){
        editor.putString("steam_profile_pic", steamPicUrl);
        editor.commit();
    }

    public Long getSteamLastLogoff(){
        return _sharedPrefs.getLong("steam_last_logoff", 0);
    }
    public void setSteamLastLogoff(Long lastLogoff){
        editor.putLong("steam_last_logoff", lastLogoff);
        editor.commit();
    }

    public String getSteamProfileLink(){
        return _sharedPrefs.getString("steam_profile_link", "");
    }
    public void setSteamProfileLink(String steamProfileUrl){
        editor.putString("steam_profile_link", steamProfileUrl);
        editor.commit();
    }


    public String getDeviceRegId() {
        return _sharedPrefs.getString("device_reg_id", "");
    }

    public void setDeviceRegId(String data) {
        editor.putString("device_reg_id", data);
        editor.commit();
    }

    public int getAppRegVersion() {
        return _sharedPrefs.getInt("app_reg_version", Integer.MIN_VALUE);
    }

    public void setAppRegVersion(String data) {
        editor.putInt("app_reg_version", Integer.MIN_VALUE);
        editor.commit();
    }


    public synchronized String getDeviceId() {
        String id = Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
        if (id != null) return id;
        if (uniqueID == null) {
            uniqueID = _sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                Editor editor = _sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }


}
