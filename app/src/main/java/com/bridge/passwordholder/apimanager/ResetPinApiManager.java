package com.bridge.passwordholder.apimanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import com.bridge.passwordholder.R;
import com.bridge.passwordholder.Utils.AppPreferenceManager;
import com.bridge.passwordholder.Utils.PLog;

/**
 * Created by Anu on 12/8/2015.
 */

public class ResetPinApiManager extends AsyncTask<Void, Void, Boolean>{

    private final String user_id;
    private String response;
  //  private final String url="http://vault.bridge-delivery.com/api/users/";

    private final String url="http://vault.bridge-global.com/index.php/api/users/";;
    private final Context c;
    private final onHttpListener monHttpListener;

    public ResetPinApiManager(Context c,String user_id,onHttpListener monHttpListener)
    {
        this.c=c;
        this.user_id=user_id;
        this.monHttpListener=monHttpListener;

    }
    public String getResponse(Context c, String user_id, onHttpListener monHttpListener) {

        response="";
        if(PasswordApiClient.isOnline(c)){

        String url1=url + AppPreferenceManager.getValue(c,AppPreferenceManager.USER_ID)+"/reset_password";
            Log.e("myapp","url:"+url1);
            try {
                response= PasswordApiClient.getUrl(url1);
            } catch (IOException e) {



                monHttpListener.onError(c.getString(R.string.something_went_wrong));
                response="";
                e.printStackTrace();

            }
            if(response!=null) {
                monHttpListener.onSuccess(response);
            }
            else
            {
                monHttpListener.onError(c.getString(R.string.something_went_wrong));
            }

        }
        else
        {
            monHttpListener.onError(c.getString(R.string.network_problem));
        }
        return response;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        getResponse(c,user_id,monHttpListener);
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
