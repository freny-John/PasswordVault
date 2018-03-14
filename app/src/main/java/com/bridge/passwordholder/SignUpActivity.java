package com.bridge.passwordholder;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bridge.passwordholder.Utils.AppPreferenceManager;

import com.bridge.passwordholder.R;

public class SignUpActivity extends AppCompatActivity {

    private FrameLayout signUpContainer;
    private FrameLayout headerPane;
    private ImageView imageLogo;
    private TextView headText;
    private TextView textHeadSub;
    private CoordinatorLayout snackBarLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //To hide keyboard
        snackBarLocation=(CoordinatorLayout) findViewById(R.id.snack_bar_location);
        signUpContainer =(FrameLayout) findViewById(R.id.fragment_container);
        headerPane =(FrameLayout) findViewById(R.id.header_pane);
        imageLogo = (ImageView) findViewById(R.id.image_logo);
        headText= (TextView) findViewById(R.id.head_text);
        textHeadSub= (TextView) findViewById(R.id.text_head_sub);
        headText.setText(getString(R.string.welcome_back));
        textHeadSub.setText(getString(R.string.login_text));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageLogo.setTransitionName(getString(R.string.activity_image_trans));
            headerPane.setTransitionName(getString(R.string.activity_layout_trans));
        }
        fillWithSignUpFragment();

    }

    public void setTitleTexts(String title,String sub){
        headText.setText(title);
        textHeadSub.setText(sub);
    }

    private void fillWithSignUpFragment() {

        Fragment newFragment;

        if(AppPreferenceManager.getIntegerValue(SignUpActivity.this,AppPreferenceManager.SIGN_UP_INTERMEDIATE)==0) {
            newFragment = new SignUpFragment();
        }
        else
        {
            newFragment = new PinFragment();

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.mainfadein, R.anim.splashfadeout);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0); /**To avoid jerk due to shared element transition*/
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


}
