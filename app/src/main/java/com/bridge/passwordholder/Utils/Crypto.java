package com.bridge.passwordholder.Utils;

import android.content.Context;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import com.bridge.passwordholder.R;

/**
 * Created by Anu on 12/1/2015.
 */
public class Crypto {

    public static String getPassword(String password,Context c) {

        String encryptedMsg="";
        try {
            encryptedMsg = AESCrypt.decrypt("u"+c.getString(R.string.pass_code), password);
        }
        catch ( GeneralSecurityException e ) {
            PLog.e(e.getMessage());
        }
        return encryptedMsg;
    }

    public static String setPassword(String password,Context c) {

        String encryptedMsg="";
        try {
            encryptedMsg = AESCrypt.encrypt("u"+c.getString(R.string.pass_code), password);
        }
        catch ( GeneralSecurityException e ) {
            PLog.e(e.getMessage());
        }

        return encryptedMsg;
    }

    public static String getEncrypt(String password,Context c) {

        String encryptedMsg="";
        try {
            encryptedMsg = AESCrypt.decrypt("c"+c.getString(R.string.pass_code1), password);
        }
        catch ( GeneralSecurityException e ) {
            PLog.e(e.getMessage());
        }
        return encryptedMsg;
    }

    public static String setEncrypt(String password,Context c) {

        String encryptedMsg="";
        try {
            encryptedMsg = AESCrypt.encrypt("c"+c.getString(R.string.pass_code1), password);
        } catch ( GeneralSecurityException e ) {
            PLog.e(e.getMessage());
        }
        return encryptedMsg;
    }

}
