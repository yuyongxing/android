package com.collectioncar.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 1.string
 * 2.boolean
 * 3.int
 *
 * @author Administrator
 */
public class SPUtil {

    public static final String config = "config";

    public static SharedPreferences getSharedPreferences() {

        return UIUtils.getContext().getSharedPreferences(config, Activity.MODE_PRIVATE);
    }

    public static String getString(String key) {

        return getSharedPreferences().getString(key, null);
    }

    public static String getString(String key, String defValue) {

        return getSharedPreferences().getString(key, defValue);
    }

    public static int getInt(String key) {

        return getSharedPreferences().getInt(key, 0);
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }


    public static void put(String key, Object object) {
        Editor editor = getSharedPreferences().edit();
        if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof String) {
            editor.putString(key, (String) object);
        }else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

}
