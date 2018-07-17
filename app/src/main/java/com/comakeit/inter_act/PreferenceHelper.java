package com.comakeit.inter_act;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private static final PreferenceHelper sInstance = new PreferenceHelper();

    public static final String PREFERENCE_ONBOARDING = "preference_onboarding";

    //TODO use single editor
    public static PreferenceHelper getInstance() {
        return sInstance;
    }

    private PreferenceHelper() {
    }

    public void setPreference(Context context, String pref_name, boolean preferenceState) {
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = myPreference.edit();
        editor.putBoolean(pref_name, preferenceState);
        editor.apply();
    }

    public boolean getPreference(Context context, String pref_name, boolean defaultState) {
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreference.getBoolean(pref_name, defaultState);
    }
}
