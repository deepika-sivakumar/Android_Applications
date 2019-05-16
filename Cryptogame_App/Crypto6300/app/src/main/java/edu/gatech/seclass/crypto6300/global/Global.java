package edu.gatech.seclass.crypto6300.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.orm.SugarApp;

import static android.content.Context.MODE_PRIVATE;

public class Global /*extends Application*/ {
    private static SharedPreferences mSharedPref;
    // Shared Preference variable to persist throughout the application
    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_NAME = "key_username";

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        SugarContext.init
//    }

    private Global() { }

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    }

    public static String getUsername() {
        return mSharedPref.getString(KEY_NAME, null);
    }

    public static void setUsername(String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(KEY_NAME, value);
        prefsEditor.commit();
    }
}
