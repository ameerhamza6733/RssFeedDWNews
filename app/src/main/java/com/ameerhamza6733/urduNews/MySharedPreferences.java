package com.ameerhamza6733.urduNews;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by DELL 3542 on 8/3/2016.
 */
public class MySharedPreferences  {



    public void deletePrefs(String key , Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();



    }
    public void savePrefs(String key, Boolean value,Context context){
        SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void saveStringPrefs(String key, String value,Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveintPrefs(String key, int value,Context context){
        SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //get prefs
    public Boolean loadPrefs(String key, Boolean value,Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean data = sharedPreferences.getBoolean(key, value);
        return data;
    }

    public String loadStringPrefs(String key, String value,Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sharedPreferences.getString(key, value);
        return data;
    }
    public int loadIntPrefs(String key, int value,Context context){
        SharedPreferences   sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int data = sharedPreferences.getInt(key, value);
        return data;
    }



}
