package com.bridge.passwordholder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.bridge.passwordholder.Interface.onResumeFragmentListener;
import com.bridge.passwordholder.Utils.AppPreferenceManager;
import com.bridge.passwordholder.Utils.PLog;
import com.bridge.passwordholder.apimanager.ResetPinApiManager;
import com.bridge.passwordholder.apimanager.onHttpListener;

import com.bridge.passwordholder.R;

/**For setting up security question*/

public class SecurityQnFragment extends Fragment implements onHttpListener,onResumeFragmentListener,DeleteConfirmationDialog.OnDialogInteractionListener {

    private TextView title;
    private TextView verTxt;
    private TextView qnTxtView;
    private AutoCompleteTextView txtSecurityQn;
    private EditText securityAnswer;
    private RelativeLayout buttonPane;
    private Button btnGo;
    private Activity myActivity;
    private boolean isFromSignUp=true;
    static onResumeFragmentListener monResumeFragmentListener;
    LinearLayout progressLayout;
    ProgressDialog mProgressDialog;
    private DialogFragment fragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        monResumeFragmentListener=SecurityQnFragment.this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_security_qn, container, false);
        initUi(v);
        return v;
    }

    private void initUi(View v) {

        title=(TextView)v. findViewById(R.id.title);
        buttonPane= (RelativeLayout)v. findViewById(R.id.button_pane);
        txtSecurityQn = (AutoCompleteTextView)v. findViewById(R.id.autocomplete_qn);
        qnTxtView = (TextView)v. findViewById(R.id.qn);
        securityAnswer= (EditText)v. findViewById(R.id.answer);
        btnGo=(Button)v. findViewById(R.id.btn_Go);
        verTxt=(TextView)v. findViewById(R.id.txt_Ver);
        progressLayout= (LinearLayout) v.findViewById(R.id.progress_layout);
        mProgressDialog=new ProgressDialog(myActivity);
        mProgressDialog.setMessage(myActivity.getString(R.string.loading));

        setUpUi();
        setUpVerText();
    }

    private void setUpVerText() {
        if(myActivity instanceof LoginActivity) {
            verTxt.setOnClickListener(view -> {
                fragment = DeleteConfirmationDialog.getDialogInstance(DeleteConfirmationDialog.DialogType.DIALOG_SECURITY_QN,
                        myActivity.getString(R.string.code_title), myActivity.getString(R.string.sending_verification_code), SecurityQnFragment.this);
                if(!fragment.isInLayout()) {
                    try {
                        fragment.show(getFragmentManager(), "myDialog");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else
        {
            verTxt.setVisibility(View.GONE);
        }
    }

    private void setUpUi() {
        viewFadeIn(title);
        viewSlideUp(buttonPane);
        isFromSignUp= (myActivity instanceof SignUpActivity);

        if(isFromSignUp){
            setUpValues();
        }
        else
        {
            getSecurityQuestion();
        }

        btnGo.setOnClickListener(view ->
        {
            PLog.e("button go click");
            if (isFromSignUp) {
                if (isValidationSuccess()) {

                    AppPreferenceManager.setFirstTimeUser(myActivity, false);
                    gotoMainActivity();
                }
            } else {
                String sec_ans = securityAnswer.getText().toString().trim();
                if (sec_ans.equals(AppPreferenceManager.getValue(myActivity, AppPreferenceManager.SECURITY_ANSWER))) {
                    AppPreferenceManager.resetIntegerValue(myActivity, AppPreferenceManager.FAILED_ATTEMPTS);
                    gotoMainActivity();
                } else {
                    Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), myActivity.getString(R.string.error_security_answer), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoMainActivity() {
        Intent i = new Intent(myActivity, MainActivity.class);
        startActivity(i);
        myActivity.finish();
        myActivity.overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if(myActivity==null){
                myActivity =(Activity)activity;
            }

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDetailFragmentInteractionListener");
        }
        setTitles();
    }

    private void setTitles() {
        if(myActivity!=null){
            if(myActivity instanceof SignUpActivity) {
                ((SignUpActivity) myActivity).setTitleTexts(myActivity.getString(R.string.security_qn), myActivity.getString(R.string.signup_sub));
            } else if(myActivity instanceof LoginActivity) {
                ((LoginActivity) myActivity).setTitleTexts(myActivity.getString(R.string.security_qn), myActivity.getString(R.string.login_subtitle));
            }

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
    }

    private boolean isValidationSuccess() {

        String sec_qn=txtSecurityQn.getText().toString().trim();
        String sec_ans=securityAnswer.getText().toString().trim();

        if((!TextUtils.isEmpty(sec_qn))){
            if(!TextUtils.isEmpty(sec_ans)) {

                AppPreferenceManager.setValue(myActivity, AppPreferenceManager.SECURITY_QUESTION, sec_qn);
                AppPreferenceManager.setValue(myActivity, AppPreferenceManager.SECURITY_ANSWER, sec_ans);
                return true;
            }
            else
            {
                try {
                Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), myActivity.getString(R.string.validate_security_qn),Snackbar.LENGTH_LONG).show();
                }catch (Exception e){}
            }
        }
        else {
            try {
                Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), myActivity.getString(R.string.validate_security_ans), Snackbar.LENGTH_LONG).show();
            }catch (Exception e){}
        }
        return false;
    }

    private void setUpValues() {

        String[] countries = getResources().getStringArray(R.array.qn_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(myActivity, R.layout.drop_down, countries);
        txtSecurityQn.setAdapter(adapter);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/HelveticaNeueLTPro-Lt.otf");
        txtSecurityQn.setTypeface(tf);

    }

    private void getSecurityQuestion() {

        String qn=AppPreferenceManager.getValue(myActivity, AppPreferenceManager.SECURITY_QUESTION);
        txtSecurityQn.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        qnTxtView.setVisibility(View.VISIBLE);
        qnTxtView.setText(qn);
    }

    private void viewFadeIn(View v) {

        Animation zoomIn = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
        zoomIn.setDuration(2000);
        v.startAnimation(zoomIn);
        v.setVisibility(View.VISIBLE);
    }

    private void viewSlideUp(View v) {

        Animation slideIn = AnimationUtils.loadAnimation(myActivity, R.anim.slide_in_bottom);
        slideIn.setDuration(2000);
        v.startAnimation(slideIn);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String message) {
        if(mProgressDialog!=null&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();}
        Snackbar.make(myActivity.findViewById(R.id.snack_bar_location),message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(String response) {
        /**show verification code entering page**/

        if(mProgressDialog!=null&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();}
        if(!TextUtils.isEmpty(response)) {
            PLog.e("response getting reset pin API:" + response);
            JSONObject jsonObj;
            try {
                jsonObj=new JSONObject(response);
                String status=jsonObj.optString("status");
                if(status.equalsIgnoreCase("success")){
                    String verCode=jsonObj.optString("verification_code");
                    addVerificationFragment(verCode);
                }
                else
                {
                    Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), myActivity.getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), myActivity.getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onProgressDismiss() {

    }



    private void addVerificationFragment(String verCode) {

        Fragment newFragment = new VerificationCodeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        Bundle b=new Bundle();
        b.putString("verCode", verCode);
        newFragment.setArguments(b);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack("securityQn");
        transaction.commit();
    }


    @Override
    public void onResumePFragment() {
        setTitles();
    }

    @Override
    public void onDialogDone() {
        if(mProgressDialog!=null&&!mProgressDialog.isShowing()){
            mProgressDialog.show();}
        new ResetPinApiManager(myActivity, AppPreferenceManager.getValue(myActivity,AppPreferenceManager.USER_ID), SecurityQnFragment.this).execute();
    }

    @Override
    public void onDialogCancel() {

    }
}
