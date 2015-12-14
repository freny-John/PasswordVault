package passwordholder.bridge.com.passwordholder.apimanager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import java.io.IOException;
import java.util.HashMap;

import passwordholder.bridge.com.passwordholder.R;
import passwordholder.bridge.com.passwordholder.Utils.CommonUtils;

/**
 * Created by Anu on 12/8/2015.
 */

public class ResetPinApiManager extends AsyncTask<Void, Void, Boolean>{

    String user_id;
    String response;
    String url="/reset_pin";
    Context c;
    onHttpListener monHttpListener;

    public ResetPinApiManager(Context c,String user_id,onHttpListener monHttpListener)
    {
        this.c=c;
        this.user_id=user_id;
        this.monHttpListener=monHttpListener;

    }
    public String getResponse(Context c,String user_id,onHttpListener monHttpListener) {

        response="";
        if(PasswordApiclient.isOnline(c)){

            HashMap<String,String> resetApi=new HashMap<String,String>();
            resetApi.put("user_id",user_id);

            try {
                response= PasswordApiclient.postUrl(url, CommonUtils.getJSON(resetApi));
            } catch (IOException e) {
                monHttpListener.onError(c.getString(R.string.something_went_wrong));
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
        getResponse(c,user_id,monHttpListener);
        return null;
    }
}
