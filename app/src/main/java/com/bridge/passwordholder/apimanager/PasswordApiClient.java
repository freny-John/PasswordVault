package com.bridge.passwordholder.apimanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import com.bridge.passwordholder.Utils.BadRequestException;
import com.bridge.passwordholder.Utils.CommonUtils;
import com.bridge.passwordholder.Utils.EmailException;
import com.bridge.passwordholder.Utils.PLog;


/**
 * Created by Anu on 12/8/2015.
 */
class PasswordApiClient {

    public static boolean isOnline(Context context)
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }
    public static String postUrl(String myUrl,String parameters) throws IOException, EmailException, BadRequestException {
        InputStream is = null;
        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty("X-API-KEY", "12345");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(parameters);
            out.close();
            int responseCode = conn.getResponseCode();
            PLog.e("response code:"+responseCode);
            if(responseCode==200||responseCode==201){
                final String encoding=conn.getHeaderField("Content-Type");
                if(encoding!=null&&encoding.contains("gzip"))
                    is=new GZIPInputStream(conn.getInputStream());
                else
                    is = conn.getInputStream();
                return CommonUtils.getString(is);
            }
            else if(responseCode==409){
                throw new EmailException();
            }
            else if(responseCode==400){
                throw new BadRequestException();
            }else {
                PLog.e("error");
            }

        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }
    public static String getUrl(String myUrl) throws IOException
    {
        InputStream is = null;
        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty("www-request-api-version", "1.0");
            conn.addRequestProperty("X-API-KEY", "12345");
            conn.setDoInput(true);

            int responseCode = conn.getResponseCode();

            if(responseCode==200){
                final String encoding=conn.getHeaderField("Content-Type");
                if(encoding!=null&&encoding.contains("gzip"))
                    is=new GZIPInputStream(conn.getInputStream());
                else
                    is = conn.getInputStream();

                return CommonUtils.getString(is);
            }else {
                PLog.e("error");
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
