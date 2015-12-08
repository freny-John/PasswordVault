package passwordholder.bridge.com.passwordholder.apimanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import passwordholder.bridge.com.passwordholder.Utils.CommonUtils;

/**
 * Created by Anu on 12/8/2015.
 */
public class PasswordApiclient {

    public static boolean isOnline(Context context)
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }
    public static String postUrl(String myurl,String parameters) throws IOException
    {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(20000 );
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Accept-Encoding", "gzip");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(parameters);
            out.close();
            int responseCode = conn.getResponseCode();
            if(responseCode==200){
                final String encoding=conn.getHeaderField("Content-Type");
                if(encoding!=null&&encoding.contains("gzip"))
                    is=new GZIPInputStream(conn.getInputStream());
                else
                    is = conn.getInputStream();
                return CommonUtils.getString(is);
            }else {
                //TODO handle error
            }



        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }


}
