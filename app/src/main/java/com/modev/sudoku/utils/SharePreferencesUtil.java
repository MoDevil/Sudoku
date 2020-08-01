package com.modev.sudoku.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author: modev
 * @description: SharePreferences帮助类
 * @date: Created in 2020/7/9 18:02
 * @version: 1.0
 */
public class SharePreferencesUtil {


    /**
     * @author: modev
     * @date: 2020/7/7 20:04
     * @description: 从SharedPreferences里面把String类型信息取出来
     * @param: key
     * @return: java.lang.String
     * */
    public static String getStringFromSharedPreference(Context context, String filename, String key) {
        SharedPreferences sp = context.getSharedPreferences( filename, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
    /**
     * @author: modev
     * @date: 2020/7/7 20:04
     * @description: 从SharedPreferences里面把博哦了按类型信息取出来
     * @param: key
     * @return: java.lang.String
     * */
    public static boolean getBooleanFromSharedPreference(Context context, String filename, String key) {
        SharedPreferences sp = context.getSharedPreferences( filename, Context.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }
    /**
     * @author: modev
     * @date: 2020/7/7 20:04
     * @description: 将String类型的信息存储在SharedPreferences中
     * @param: kay
     * @param: value
     * @return: void
     * */
    public static void setStringToSharedPreference(Context context, String filename, String kay, String value) {
        SharedPreferences sp = context.getSharedPreferences( filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(kay, value);
        editor.apply();
    }
    /**
     * @author: modev
     * @date: 2020/7/7 20:04
     * @description: 将Boolean类型的信息存储在SharedPreferences中
     * @param: kay
     * @param: value
     * @return: void
     * */
    public static void setBooleanToSharedPreference(Context context, String filename, String kay, boolean value) {
        SharedPreferences sp = context.getSharedPreferences( filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(kay, value);
        editor.apply();
    }
}
