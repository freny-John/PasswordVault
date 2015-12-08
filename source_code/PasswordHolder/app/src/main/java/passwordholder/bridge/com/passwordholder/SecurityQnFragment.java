package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import passwordholder.bridge.com.passwordholder.Utils.AppPreferenceManager;

public class SecurityQnFragment extends Fragment {

    TextView title;
    AutoCompleteTextView txtSecurityQn;
    EditText securityAnswer;
    RelativeLayout buttonPane;
    Button btnGo;
    Activity myActivity;
    boolean isFromSignUp=true;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_security_qn, container, false);
        initUi(v);
        return v;
    }

    private void initUi(View v) {

        title=(TextView)v. findViewById(R.id.title);
        buttonPane= (RelativeLayout)v. findViewById(R.id.button_pane);
        txtSecurityQn = (AutoCompleteTextView)v. findViewById(R.id.autocomplete_qn);
        securityAnswer= (EditText)v. findViewById(R.id.answer);
        btnGo=(Button)v. findViewById(R.id.btn_Go);

        setUpUi();
    }

    private void setUpUi() {

        isFromSignUp=(myActivity instanceof SignUpActivity)?true:false;

        if(isFromSignUp){
            setUpValues();
        }
        else
        {
            getSecurityQuestion();
        }

        btnGo.setOnClickListener(view ->
        {
            if (isFromSignUp) {
                if (isValidationSuccess()) {
                    gotoMainActivity();
                }
            } else {
                String sec_ans = securityAnswer.getText().toString().trim();
                if(sec_ans.equals(AppPreferenceManager.getValue(myActivity,AppPreferenceManager.SECURITY_ANSWER)))
                {
                    AppPreferenceManager.resetIntegerValue(myActivity,AppPreferenceManager.FAILED_ATTEMPTS);
                    gotoMainActivity();
                }
                else {
                    Snackbar.make(view.getRootView(), myActivity.getString(R.string.error_security_answer),Snackbar.LENGTH_SHORT).show();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
    }

    private boolean isValidationSuccess() {
        String sec_qn=txtSecurityQn.getText().toString().trim();
        String sec_ans=securityAnswer.getText().toString().trim();

        if((!TextUtils.isEmpty(sec_qn))&&(!TextUtils.isEmpty(sec_ans)))
        {
            AppPreferenceManager.setValue(myActivity,AppPreferenceManager.SECURITY_QUESTION,sec_qn);
            AppPreferenceManager.setValue(myActivity,AppPreferenceManager.SECURITY_ANSWER,sec_ans);
            return true;
        }
        return false;
    }

    private void setUpValues() {

        String[] countries = getResources().getStringArray(R.array.qn_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(myActivity, android.R.layout.simple_list_item_1, countries);
        txtSecurityQn.setAdapter(adapter);
        viewFadein(title);
        viewSlideUp(buttonPane);
    }

    private void getSecurityQuestion() {

        String qn=AppPreferenceManager.getValue(myActivity,AppPreferenceManager.SECURITY_QUESTION);
        txtSecurityQn.setText(qn);
        txtSecurityQn.setEnabled(false);
    }

    private void viewFadein(View v) {

        Animation zoomin = AnimationUtils.loadAnimation(myActivity, R.anim.fadein);
        zoomin.setDuration(2000);
        v.startAnimation(zoomin);
        v.setVisibility(View.VISIBLE);
    }
    private void viewSlideUp(View v) {

        Animation slidein = AnimationUtils.loadAnimation(myActivity, R.anim.slide_in_bottom);
        slidein.setDuration(2000);
        v.startAnimation(slidein);
        v.setVisibility(View.VISIBLE);
    }


}
