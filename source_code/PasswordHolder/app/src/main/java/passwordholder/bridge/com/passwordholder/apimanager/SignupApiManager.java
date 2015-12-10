package passwordholder.bridge.com.passwordholder.apimanager;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;

import passwordholder.bridge.com.passwordholder.R;
import passwordholder.bridge.com.passwordholder.Utils.CommonUtils;

/**
 * Created by Anu on 12/8/2015.
 */

public class SignupApiManager extends AsyncTask<Void, Void, Boolean>{

    String first_name,last_name,email_id;
    String response;
    String url="/reset_pin";
    Context c;
    onHttpListener monHttpListener;

    public SignupApiManager(Context c, String first_name,String last_name,String email_id, onHttpListener monHttpListener)
    {
        this.c=c;
        this.first_name=first_name;
        this.last_name=last_name;
        this.email_id=email_id;
        this.monHttpListener=monHttpListener;

    }
    public String getResponse() {

        response="";
        if(PasswordApiclient.isOnline(c)){

            HashMap<String,String> resetApi=new HashMap<String,String>();
            resetApi.put("first_name",first_name);
            resetApi.put("last_name",last_name);
            resetApi.put("email_id",email_id);

            try {
                response= PasswordApiclient.postUrl(url, CommonUtils.getJSON(resetApi));
            } catch (IOException e) {
                monHttpListener.onError(c.getString(R.string.io_exception));
                response="";
                e.printStackTrace();
            }
            monHttpListener.onSuccess(response);

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
