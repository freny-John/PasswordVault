package com.bridge.passwordholder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bridge.passwordholder.Utils.AppPreferenceManager;

public class LoginActivity extends AppCompatActivity {

    private TextView headText;
    private TextView textHeadSub;
    private CoordinatorLayout snackBarLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //To hide keyboard
        snackBarLocation=(CoordinatorLayout) findViewById(R.id.snack_bar_location);

        initUi();
        if( AppPreferenceManager.getIntegerValue(this, AppPreferenceManager.FAILED_ATTEMPTS)<AppPreferenceManager.MAX_ATTEMPTS) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new PinFragment()).commit();
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new SecurityQnFragment()).commit();
        }
    }

    private void initUi() {

        headText=(TextView) findViewById(R.id.head_text);
        textHeadSub=(TextView) findViewById(R.id.text_head_sub);
        headText.setText(R.string.welcome_back);
        textHeadSub.setText(R.string.login_text);
        viewZoomIn(headText);
    }

    public void setTitleTexts(String title,String sub){
        headText.setText(title);
        textHeadSub.setText(sub);
    }

    public void gotoMainActivity() {
        hideSoftKeyboard();
        Intent startMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(startMain);
        finish();
    }
    private void viewZoomIn(View v) {

        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        zoomIn.setDuration(2000);
        v.startAnimation(zoomIn);
        v.setVisibility(View.VISIBLE);
    }
    public void showSnackBar(String message,String actionText,View.OnClickListener actionListener,int color){
        Snackbar snackBar=  Snackbar.make(snackBarLocation, message, Snackbar.LENGTH_LONG);
        if(actionText!=null) {
            snackBar.setAction(actionText, actionListener).show();
        }
        if(color!=-1){
            View snackBarView = snackBar.getView();
            snackBarView.setBackgroundColor(Color.GREEN);

        }
        snackBar.show();
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
