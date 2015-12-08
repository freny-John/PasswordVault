package passwordholder.bridge.com.passwordholder.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Anu on 11/27/2015.
 * For storing prefrence values
 */
public class AppPreferenceManager
{

    public static final String FIRST_TIME_USER="FirstTimeUser";
    public static final String USER_EMAIL="UserEmail";
    public static final String USER_PASSWORD="Password";
    public static final String SECURITY_QUESTION="SecurityQuestion";
    public static final String SECURITY_ANSWER="SecurityAnswer";

    public static void setValue(Context mContext,String key,String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, Crypto.setEncrypt(value,mContext));
        editor.commit();
    }

    public static String getValue(Context mContext,String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return Crypto.getEncrypt(prefs.getString(key, ""), mContext);
    }

    public static void setFirstTimeUser(Context mContext,boolean firstTimeUser) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(FIRST_TIME_USER,firstTimeUser);
        editor.commit();
    }

    public static void setUserEmail(Context mContext,String email) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_EMAIL, Crypto.setEncrypt(email,mContext));
        editor.commit();
    }

    public static void setUserPassword(Context mContext,String password) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_PASSWORD,Crypto.setEncrypt(password,mContext));
        editor.commit();
    }

    public static boolean getFirstTimeUser(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getBoolean(FIRST_TIME_USER, true);
    }

    public static String getUserEmail(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return Crypto.getEncrypt(prefs.getString(USER_EMAIL, ""), mContext);
    }

    public static String getUserPassword(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return Crypto.getEncrypt(prefs.getString(USER_PASSWORD, ""), mContext);
    }


}
