package passwordholder.bridge.com.passwordholder.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CommonUtils {

	public static String SERVER_URL="http://inddev.pingstamp.mx/pfandl/mobile.php";


	public static String getDefaultPhoneNumber(Context context)
	{
		TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return(tMgr.getLine1Number());
	}


	public static String getDefaultEmail(Context context)
	{
		AccountManager am = AccountManager.get(context); // "this" references the current Context

		Account[] accounts = am.getAccountsByType("com.google");
		if(accounts.length>0){
			return accounts[0].name;
		}else
		{
			return null;
		}
	}


	public static String getServerURL(Context context)
	{
		return SERVER_URL+"?lang=en";
	}


	public static String decimalFormat(float val)
	{

		DecimalFormat twoDForm = new DecimalFormat("#.00");
		return twoDForm.format((double)val);
	}


	public static String getJSON(Map<String, Object> input)
	{
		try
		{
			final JSONObject json=new JSONObject(input);
			return json.toString();

		}catch (NullPointerException e) {
			return null;
		}
	}

	public static String getString(InputStream is)
	{
		BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);  
		String line;  
		StringBuilder sb =  new StringBuilder();  
		try {  
			while ((line = rd.readLine()) != null) {  
				sb.append(line);  
			}  
			rd.close();  

		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		String contentOfMyInputStream = sb.toString();  
		return contentOfMyInputStream;  
	}

	public static boolean validateEmail(EditText editText)
	{
		if((editText.getText().toString().trim().equals("")))
		{
			return false;
		}
		else
		{
			Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");

			String email =(editText.getText().toString()) ;

			Matcher matcher = pattern.matcher(email);

			if(matcher.matches())
			{


				return true;
			}

		}
		return false;
	}





	public static String getVersion()
	{
		return "Android "+Build.BRAND+" "+ Build.MODEL+" "+Build.VERSION.RELEASE;
	}
	public static String getDeviceID(Context context)
	{

		String deviceId;

		deviceId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		if(TextUtils.isEmpty(deviceId)){
			TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Activity.TELEPHONY_SERVICE);
			deviceId=telephonyManager.getDeviceId();
		}

		return deviceId;
	}

	public static void changeLanguage(String language,Context mContext)
	{
		Resources res = mContext.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(language.toLowerCase());
		res.updateConfiguration(conf, dm);
		Log.d("Language ","Changed");
	}


	public static File getCacheDir(Context context,String uniqueName)
	{

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			if(Build.VERSION.SDK_INT>7)
				return new File(context.getExternalCacheDir()+ File.separator + uniqueName);
			return new File(
					Environment.getExternalStorageDirectory()
					+ File.separator + "Android"
					+ File.separator + "data"
					+ File.separator + context.getPackageName()
					+ File.separator + "cache"
					+ File.separator + uniqueName);
		}
		return new File(context.getCacheDir()+ File.separator + uniqueName);
	}







	public static boolean writeToCache(String key,String data,File cacheDir)
	{
		final File cachedResponse=new File(cacheDir, key);
		cachedResponse.getParentFile().mkdirs();
		try
		{
			cachedResponse.createNewFile();
			FileOutputStream fos = new FileOutputStream(cachedResponse);
			fos.write(data.getBytes());
			fos.close();
			return true;
		}catch (IOException e) {
			return false;
		}

	}

	public static String getCachedResponse(String key,File cacheDir)
	{
		final File cachedResponse=new File(cacheDir, key);
		if(cachedResponse==null||!cachedResponse.exists())
			return null;

		FileInputStream is;
		try {
			is = new FileInputStream(cachedResponse);
			return getString(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void cleanCacheDir(File dir)
	{

		if(dir!=null&&dir.exists())
		{
			final File[] tempFiles = dir.listFiles();
			if(tempFiles==null)
				return;
			for(File tempFile:tempFiles)
			{
				tempFile.delete();
			}
		}
	}






}
