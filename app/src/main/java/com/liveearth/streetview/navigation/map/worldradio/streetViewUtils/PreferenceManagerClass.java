package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerClass {

    private final SharedPreferences preferences;

    public PreferenceManagerClass(Context context) {
        preferences= context.getSharedPreferences(ConstantsStreetView.sharedPrefName, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key,Boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public Boolean getBoolean(String key,Boolean defValue){
        return preferences.getBoolean(key,defValue);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key,String defValue){
        return  preferences.getString(key,defValue);
    }


    public void clear(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


}
