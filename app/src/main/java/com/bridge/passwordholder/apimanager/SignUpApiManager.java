package com.bridge.passwordholder.apimanager;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;

import com.bridge.passwordholder.R;
import com.bridge.passwordholder.Utils.BadRequestException;
import com.bridge.passwordholder.Utils.CommonUtils;
import com.bridge.passwordholder.Utils.EmailException;
import com.bridge.passwordholder.Utils.PLog;

/**
 * Created by Anu on 12/8/2015.
 */

public class SignUpApiManager extends AsyncTask<Void, Void, Boolean>{

    private final String first_name;
    private final String last_name;
    private final String email_id;
    private String response;
    private final String url="http://vault.bridge-delivery.com/api/users";
    private final Context c;
    private final onHttpListener monHttpListener;

    public SignUpApiManager(Context c, String first_name, String last_name, String email_id, onHttpListener monHttpListener)
    {
        this.c=c;
        this.first_name=first_name;
        this.last_name=last_name;
        this.email_id=email_id;
        this.monHttpListener=monHttpListener;

    }
    public String getResponse() {

        response="";
        if(PasswordApiClient.isOnline(c)){

            HashMap<String,String> resetApi= new HashMap<>();
            resetApi.put("first_name",first_name);
            resetApi.put("last_name",last_name);
            resetApi.put("email_id",email_id);

            try {

                response= PasswordApiClient.postUrl(url, CommonUtils.getJSON(resetApi));
            } catch (IOException e) {
                PLog.e("error getting"+e.getMessage());
                monHttpListener.onError(c.getString(R.string.io_exception));
                response="";
                e.printStackTrace();
            }
            catch (EmailException e){
                monHttpListener.onError(c.getString(R.string.email_already_exists));
            } catch (BadRequestException e) {
                monHttpListener.onError(c.getString(R.string.error_bad_request));

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
        getResponse();
        return null;
    }
}
