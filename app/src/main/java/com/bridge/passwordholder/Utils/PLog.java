package com.bridge.passwordholder.Utils;

import android.util.Log;

/**
 * Created by Anu on 11/26/2015.
 */
public class PLog {
    private static  final boolean debug_enabled=false;
    private static  final String app_name="PasswordHolder";

    public static void w(String msg){
      if(debug_enabled){
          Log.w(app_name,"-->"+msg);
      }
    }
    public static void v(String msg){
      if(debug_enabled){
          Log.v(app_name,"-->"+msg);
      }
    }
    public static void e(String msg){
      if(debug_enabled){
          Log.e(app_name,"-->"+msg);
      }
    }
    public static void i(String msg){
      if(debug_enabled){
          Log.i(app_name,"-->"+msg);
      }
    }
    public static void d(String msg){
      if(debug_enabled){
          Log.d(app_name,"-->"+msg);
      }
    }
}
