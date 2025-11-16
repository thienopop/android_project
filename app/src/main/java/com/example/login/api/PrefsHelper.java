//package com.example.login.api;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//public class PrefsHelper {
//    private static final String PREF_NAME = "MyAppPrefs";
//    private static final String KEY_TOKEN = "token";
//    private static final String KEY_USERNAME = "username";
//
//    private static SharedPreferences getPrefs(Context context) {
//        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//    }
//
//    // ✅ Lưu token
//    public static void saveToken(Context context, String token) {
//        getPrefs(context).edit()
//                .putString(KEY_TOKEN, token)
//                .apply();
//    }
//
//    // ✅ Lấy token
//    public static String getToken(Context context) {
//        return getPrefs(context).getString(KEY_TOKEN, null);
//    }
//
//    // ✅ Lưu tên người dùng (tuỳ chọn)
//    public static void saveUsername(Context context, String username) {
//        getPrefs(context).edit()
//                .putString(KEY_USERNAME, username)
//                .apply();
//    }
//
//    // ✅ Lấy tên người dùng
//    public static String getUsername(Context context) {
//        return getPrefs(context).getString(KEY_USERNAME, null);
//    }
//
//    // ✅ Xoá toàn bộ dữ liệu (thường dùng khi logout)
//    public static void clear(Context context) {
//        getPrefs(context).edit()
//                .clear()
//                .apply();
//    }
//}
package com.example.login.api;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {
    private static final String PREF_NAME = "APP_PREFS";
    private static final String TOKEN_KEY = "TOKEN";
    private static final String USER_ID_KEY = "USER_ID";

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(TOKEN_KEY, token).apply();
    }

    public static void saveUserId(Context context, int userId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(USER_ID_KEY, userId).apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);
    }

    public static int getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(USER_ID_KEY, -1);
    }

    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(TOKEN_KEY).apply();
    }

    public static void clearUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(USER_ID_KEY).apply();
    }
}
