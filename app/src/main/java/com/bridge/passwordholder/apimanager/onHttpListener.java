package com.bridge.passwordholder.apimanager;

/**
 * Created by Anu on 12/8/2015.
 */
public interface onHttpListener {

    void onError(String message);
    void onSuccess(String response);
    void onProgressDismiss();
}
