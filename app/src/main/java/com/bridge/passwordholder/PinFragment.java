package com.bridge.passwordholder;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andexert.library.RippleView;
import com.bridge.passwordholder.Utils.AppPreferenceManager;
import com.bridge.passwordholder.Utils.PLog;

import com.bridge.passwordholder.R;

public class PinFragment extends Fragment implements View.OnClickListener{

    private Button key1;
    private Button key2;
    private Button key3;
    private Button key4;
    private Button key5;
    private Button key6;
    private Button key7;
    private Button key8;
    private Button key9;
    private Button key0;
    private Button keyBackSpace;
    private Button confirm;
    private Button ind1;
    private Button ind2;
    private Button ind3;
    private Button ind4;
    private static String passCode;
    private static int count;
    private Activity myActivity;
    private Toolbar myToolbar;
    private RelativeLayout keypadContainer;
    private ImageView back;
    RippleView backButtonRipple;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pin, container, false);
        initUi(v);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        PLog.e("on attach calling");
        try {
            if(myActivity==null){
                myActivity = activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDetailFragmentInteractionListener");
        }
    }

    private void initUi(View v) {

        clearPassCode();
        initComponents(v);
        setListeners();
        if(AppPreferenceManager.getFirstTimeUser(myActivity)&&myActivity instanceof SignUpActivity){
            //Sign up
            ((SignUpActivity) myActivity).setTitleTexts(myActivity.getString(R.string.welcome),myActivity.getString(R.string.set_your_pin));
        }
        else if(myActivity instanceof LoginActivity)
        {
            //login
            confirm.setVisibility(View.GONE);
        }

    }

    private void clearPassCode() {
        passCode ="";
        count=0;

    }

    private void resetImages() {
        ind1.setPressed(false);
        ind2.setPressed(false);
        ind3.setPressed(false);
        ind4.setPressed(false);
    }

    private void setListeners() {

        key1.setOnClickListener(this);
        key2.setOnClickListener(this);
        key3.setOnClickListener(this);
        key4.setOnClickListener(this);
        key5.setOnClickListener(this);
        key6.setOnClickListener(this);
        key7.setOnClickListener(this);
        key8.setOnClickListener(this);
        key9.setOnClickListener(this);
        key0.setOnClickListener(this);
        keyBackSpace.setOnClickListener(this);
        confirm.setOnClickListener(this);
        confirm.setEnabled(false); /**Active only if we entered a four digit password */
    }

    private void initComponents(View v) {

        key1=(Button)v. findViewById(R.id.key1);
        key2=(Button)v. findViewById(R.id.key2);
        key3=(Button)v. findViewById(R.id.key3);
        key4=(Button)v. findViewById(R.id.key4);
        key5=(Button)v. findViewById(R.id.key5);
        key6=(Button)v. findViewById(R.id.key6);
        key7=(Button)v. findViewById(R.id.key7);
        key8=(Button)v. findViewById(R.id.key8);
        key9=(Button)v. findViewById(R.id.key9);
        key0=(Button)v. findViewById(R.id.key0);
        keyBackSpace =(Button)v. findViewById(R.id.keyb);
        confirm=(Button) v.findViewById(R.id.confirm);
        myToolbar=(Toolbar) v.findViewById(R.id.my_toolbar);
        keypadContainer=(RelativeLayout) v.findViewById(R.id.keypad_container);

        ind1=(Button)v. findViewById(R.id.ind1);
        ind2=(Button)v. findViewById(R.id.ind2);
        ind3=(Button)v. findViewById(R.id.ind3);
        ind4=(Button)v. findViewById(R.id.ind4);
       // back=(ImageView) v.findViewById(R.id.back);
        backButtonRipple= (RippleView) v.findViewById(R.id.back_button_ripple);


        backButtonRipple.setOnRippleCompleteListener(rippleView -> myActivity.onBackPressed());

        if(myActivity instanceof MainActivity){
            myToolbar.setVisibility(View.VISIBLE);
            keypadContainer.setGravity(Gravity.CENTER);
        }
        else
        {
            myToolbar.setVisibility(View.GONE);
        }
    }


    private void addSecurityFragment() {

        Fragment newFragment = new SecurityQnFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right,R.anim.slide_left);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }


    private void setIndicator() {
        PLog.e("count getting"+count);
        switch (count){
            case 0:
                resetImages();
                break;
            case 1:
                ind1.setPressed(true);
                ind2.setPressed(false);
                ind3.setPressed(false);
                ind4.setPressed(false);
                break;
            case 2:
                ind1.setPressed(true);
                ind2.setPressed(true);
                ind3.setPressed(false);
                ind4.setPressed(false);
                break;
            case 3:
                ind1.setPressed(true);
                ind2.setPressed(true);
                ind3.setPressed(true);
                ind4.setPressed(false);
                break;
            case 4:
                ind1.setPressed(true);
                ind2.setPressed(true);
                ind3.setPressed(true);
                ind4.setPressed(true);
                break;
            default:break;

        }
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.key0:
            case R.id.key1:
            case R.id.key2:
            case R.id.key3:
            case R.id.key4:
            case R.id.key5:
            case R.id.key6:
            case R.id.key7:
            case R.id.key8:
            case R.id.key9:

                PLog.e("checking...count:" + count + "\npass code:"+passCode);
                if (count < 4) {
                    /**Adding password*/
                    count++;
                    passCode = passCode.concat(view.getTag().toString());
                }
                setIndicator();
                if(count==4){
                    /**maximum digit possible is over*/
                    if( myActivity instanceof LoginActivity) {
                        onConfirmClick(view);
                    }else {
                        confirm.setEnabled(true);
                        numberKeyActivate(false);
                    }
                }
                break;

            case R.id.keyb:
                if (confirm.getVisibility() == View.VISIBLE){
                    enableNumberKeys();
                }
                if(count>0){
                    count--;
                    passCode = passCode.substring(0, passCode.length()-1);
                }
                setIndicator();
                break;
            case R.id.confirm:
                //TODO add password validation for less than 4 digits
                onConfirmClick(view);
            default:
                break;

        }
    }

    private void onConfirmClick(View view) {

        if(myActivity instanceof LoginActivity) {
            if(passCode.equals(AppPreferenceManager.getUserPassword(myActivity))) {
                AppPreferenceManager.resetIntegerValue(myActivity,AppPreferenceManager.FAILED_ATTEMPTS);
                ((LoginActivity) myActivity).gotoMainActivity();
            }
            else
            {
                onInvalidPin(view);

            }
        }
        else
        {
            AppPreferenceManager.setIntegerValue(myActivity, AppPreferenceManager.SIGN_UP_INTERMEDIATE, 2);
            AppPreferenceManager.setUserPassword(myActivity, passCode);
            if(myActivity instanceof SignUpActivity) {
                addSecurityFragment();
            }
            else
            {
                myActivity.onBackPressed();
            }
        }
    }

    private void onInvalidPin(View view) {
        AppPreferenceManager.incrementIntegerValue(myActivity, AppPreferenceManager.FAILED_ATTEMPTS);
        if( AppPreferenceManager.getIntegerValue(myActivity, AppPreferenceManager.FAILED_ATTEMPTS)<AppPreferenceManager.MAX_ATTEMPTS) {
            PLog.e("not reached max");
            if(myActivity instanceof SignUpActivity) {
                Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), myActivity.getString( R.string.wrong_pin_msg), Snackbar.LENGTH_LONG).setAction(R.string.clear_all, view1 -> {
                    resetPassword();
                }).show();
            }
            else
            {
                resetPassword();
                Snackbar.make(myActivity.findViewById(R.id.snack_bar_location),  myActivity.getString(R.string.wrong_pin_msg), Snackbar.LENGTH_LONG).show();
            }
        }
        else
        {
            resetPassword();
            Snackbar.make(myActivity.findViewById(R.id.snack_bar_location),  myActivity.getString(R.string.wrong_pin_last_msg), Snackbar.LENGTH_LONG).show();
            addSecurityFragment();
        }
    }

    private void resetPassword() {
        clearPassCode();
        resetImages();
        enableNumberKeys();
    }

    private void enableNumberKeys() {
        confirm.setEnabled(false);
        numberKeyActivate(true);
    }

    private void numberKeyActivate(boolean enabled){

        key0.setEnabled(enabled);
        key1.setEnabled(enabled);
        key2.setEnabled(enabled);
        key3.setEnabled(enabled);
        key4.setEnabled(enabled);
        key5.setEnabled(enabled);
        key6.setEnabled(enabled);
        key7.setEnabled(enabled);
        key8.setEnabled(enabled);
        key9.setEnabled(enabled);

    }

}
