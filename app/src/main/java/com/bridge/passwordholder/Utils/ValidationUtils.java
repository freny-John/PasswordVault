package com.bridge.passwordholder.Utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anu on 11/27/2015.
 */
public class ValidationUtils {

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

    public static boolean isPasswordSame(String pass, String conPass){
        return pass.equals(conPass);
    }
}
