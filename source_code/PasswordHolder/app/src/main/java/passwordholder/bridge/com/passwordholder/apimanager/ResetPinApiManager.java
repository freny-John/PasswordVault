package passwordholder.bridge.com.passwordholder.apimanager;

import android.content.Context;
import android.support.design.widget.Snackbar;

import java.util.HashMap;

import passwordholder.bridge.com.passwordholder.R;
import passwordholder.bridge.com.passwordholder.Utils.CommonUtils;

/**
 * Created by Anu on 12/8/2015.
 */

public class ResetPinApiManager {
    String user_id;

    public ResetPinApiManager(Context c,String url,String user_id,onHttpListener monHttpListener) {
        this.user_id = user_id;
        if(PasswordApiclient.isOnline(c)){

            HashMap<String,String> resetApi=new HashMap<String,String>();
            resetApi.put("user_id",user_id);
         //   PasswordApiclient.postUrl(url, CommonUtils.getJSON(resetApi));
        }
        else
        {
            monHttpListener.onError(c.getString(R.string.network_problem));
        }
    }
}
