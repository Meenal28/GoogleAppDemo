package com.app.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sudesi infotech on 8/4/2016.
 */
public class PrefrenceUtils {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "app_demo";

    public static String KEY_SEARCH_LIST ="search_list";

    // Constructor
    public PrefrenceUtils(Context context) {
        try {
            this._context = context;
            pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * save dependents update status for syncing
     */
    public void saveSearchList(List<String> dependentsIdsList) {


        Set<String> set = new HashSet<String>();
        //set = pref.getStringSet(KEY_SEARCH_LIST, new HashSet<String>());
        set.addAll(dependentsIdsList);
        editor.putStringSet(KEY_SEARCH_LIST, set);
        editor.commit();
    }

    /**
     * Get dependents update status for syncing
     */
    public List<String> getSearchList() {


        List<String> dependentsIdsList = new ArrayList<String>();

        Set<String> set = pref.getStringSet(KEY_SEARCH_LIST, new HashSet<String>());
        if (set != null && set.size() > 0) {
            dependentsIdsList = new ArrayList<String>(set);
        }
        return dependentsIdsList;
    }

}
