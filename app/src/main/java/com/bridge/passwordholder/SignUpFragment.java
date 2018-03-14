package com.bridge.passwordholder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import com.bridge.passwordholder.Utils.AppPreferenceManager;
import com.bridge.passwordholder.Utils.PLog;
import com.bridge.passwordholder.Utils.ValidationUtils;
import com.bridge.passwordholder.apimanager.SignUpApiManager;
import com.bridge.passwordholder.apimanager.onHttpListener;

import com.bridge.passwordholder.R;

public class SignUpFragment extends Fragment implements onHttpListener {

    private Button SignUp;
    private EditText firstName;
    private EditText lastName;
    private EditText Email;
    private Activity myActivity;
    ProgressDialog mProgressDialog;


    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_signup, container, false);
        initUi(v);
        mProgressDialog=new ProgressDialog(myActivity);
        mProgressDialog.setMessage(myActivity.getString(R.string.loading));
        SignUp.setOnClickListener(view -> doValidation());
        return v;
    }



    private void gotoPinFragment() {

        Fragment newFragment = new PinFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right,R.anim.slide_left);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private String u_username;
    private String u_lastName;
    private String u_email;

    private boolean doValidation() {

        u_username= firstName.getText().toString().trim();
        u_lastName= this.lastName.getText().toString().trim();
        u_email= Email.getText().toString().trim();

        if(!(TextUtils.isEmpty(u_username))){
            if(!TextUtils.isEmpty(u_email)){
                if(ValidationUtils.validateEmail(Email))
                {
                    ((SignUpActivity) myActivity).hideSoftKeyboard();
                    if(mProgressDialog!=null&&!mProgressDialog.isShowing()){
                        mProgressDialog.show();}

                    new SignUpApiManager(myActivity,u_username,u_lastName,u_email,SignUpFragment.this).execute();
                    return  true;

                }
                else
                {
                    showSnackBarOnError(Email, myActivity.getString(R.string.email_invalid));
                    Email.requestFocus();
                }
            }else{
                showSnackBarOnError(Email, myActivity.getString(R.string.email_empty_error));
                Email.requestFocus();
            }

        }else{
            showSnackBarOnError(firstName,myActivity.getString(R.string.f_name_error));
            firstName.requestFocus();
        }
        return false;
    }

    private void storeUserCredentials(String username, String password,String email) {

        AppPreferenceManager.setUserEmail(myActivity, email);
        AppPreferenceManager.setValue(myActivity, AppPreferenceManager.USER_NAME, username);
        AppPreferenceManager.setUserPassword(myActivity, password);
        AppPreferenceManager.setIntegerValue(myActivity, AppPreferenceManager.SIGN_UP_INTERMEDIATE, 1);//TODO comment this while working with api

    }

    private void showSnackBarOnError(View v ,String message){

        Snackbar.make(myActivity.findViewById(R.id.snack_bar_location),message,Snackbar.LENGTH_SHORT).show();
    }

    private void initUi(View v) {

        ((SignUpActivity) myActivity).setTitleTexts(myActivity.getString(R.string.welcome),myActivity.getString(R.string.set_your_details));

        SignUp = (Button) v.findViewById(R.id.btn_signup);
        firstName = (EditText)v. findViewById(R.id.first_name);
        lastName = (EditText)v. findViewById(R.id.last_name);
        Email = (EditText)v. findViewById(R.id.email);
        validateEmailText();

    }

    private void validateEmailText() {

        Email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ValidationUtils.validateEmail(Email)) {
                    Email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                } else {
                    Email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myActivity = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //  mListener = null;
    }

    @Override
    public void onError(String message) {
        if(mProgressDialog!=null&&mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if(!TextUtils.isEmpty(message)){
            Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), message,Snackbar.LENGTH_SHORT).show();
        }
        PLog.e("Sign up fragment on error"+message);
    }

    @Override
    public void onSuccess(String response) {
        if(mProgressDialog!=null&&mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        PLog.e("Sign up api on response: " + response);
        if(!TextUtils.isEmpty(response)) {
            try {
                JSONObject jb=new JSONObject(response);
                String status=jb.optString("status");
                if(status.equalsIgnoreCase("success"))
                {
                    String id=jb.optString("User_id");
                    AppPreferenceManager.setIntegerValue(myActivity,AppPreferenceManager.SIGN_UP_INTERMEDIATE,1);
                    AppPreferenceManager.setValue(myActivity,AppPreferenceManager.USER_ID,id);
                    storeUserCredentials(u_username, u_lastName, u_email);
                    gotoPinFragment();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onProgressDismiss() {

    }

}
