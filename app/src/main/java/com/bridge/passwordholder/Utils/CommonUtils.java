package com.bridge.passwordholder.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.Build;
import android.widget.EditText;

public class CommonUtils {

	//private static final String SERVER_URL="http://inddev.pingstamp.mx/pfandl/mobile.php";


	public static String getJSON(Map<String, String> input)
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
		return sb.toString();
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



}
