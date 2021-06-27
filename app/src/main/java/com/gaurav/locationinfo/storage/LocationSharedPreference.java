package com.gaurav.locationinfo.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.gaurav.locationinfo.model.LocationDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Sharma on 28-06-2021 on 01:57 .
 */
public class LocationSharedPreference {

    private static final String PREFS_NAME = "LocationInfo";


    public static <T> void setList(Context context ,String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings != null) {
            SharedPreferences.Editor prefsEditor = settings.edit();
            prefsEditor.putString(key, json);
            prefsEditor.commit();
        }
    }


    public static List<LocationDetails> getSavedLocationDetailsList(Context context, String key) {
        List<LocationDetails> list = new ArrayList<LocationDetails>();
        SharedPreferences mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if(mPrefs.contains(key)){

            Gson gson = new Gson();
            String json = mPrefs.getString(key, "");
            if (json.isEmpty()) {
                list = new ArrayList<LocationDetails>();
            } else {
                Type type = new TypeToken<List<LocationDetails>>() {
                }.getType();
                list = gson.fromJson(json, type);
            }
            return list;

        }
        return null;
    }



}
