package com.bridge.passwordholder;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.bridge.passwordholder.R;
/**
 * Created by Anu on 12/9/2015.
 * Page to verify the code entered.
 */
public class VerificationCodeFragment extends Fragment {

    private Activity myActivity;
    private Button btnSignUp;
    private EditText mEditVerCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_verification, container, false);
        btnSignUp = (Button) v.findViewById(R.id.btn_verify);
        mEditVerCode = (EditText) v.findViewById(R.id.ver_code);

        if(myActivity!=null&&myActivity instanceof LoginActivity){
            ((LoginActivity) myActivity).setTitleTexts(myActivity.getString(R.string.verification_code), myActivity.getString(R.string.enter_code));
        }
        Bundle b=getArguments();
        String verCode=b.getString("verCode");

        btnSignUp.setOnClickListener(view -> {
           if(mEditVerCode.getText().toString().trim().equals(verCode)){
               ((LoginActivity) myActivity).gotoMainActivity();
           }
            else
           {
               Snackbar.make(myActivity.findViewById(R.id.snack_bar_location), myActivity.getString(R.string.wrong_verification_code),Snackbar.LENGTH_LONG).show();
           }
        });
        return v;
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
        SecurityQnFragment.monResumeFragmentListener.onResumePFragment();
        myActivity = null;
    }

}
