package com.example.ykk.hw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Ykk on 16/3/13.
 */
public class Pref extends PreferenceActivity {
    public static final String PREF_MIN = "PAST_MIN";
    public static final String PREF_SEC = "PAST_SEC";


    //Load
    public static String getMin(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_MIN,"");
    }
    public static String getSec(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_SEC,"");
    }

    //Store
    public static void setPast(Context context, String past_min, String past_sec){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_MIN, past_min);
        editor.putString(PREF_SEC, past_sec);
        editor.commit();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
}