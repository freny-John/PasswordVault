package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
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

import java.util.HashMap;

import passwordholder.bridge.com.passwordholder.Utils.AppPreferenceManager;
import passwordholder.bridge.com.passwordholder.Utils.ValidationUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignUpFragment extends Fragment {

    Button SignUp;
    EditText firstName, lastName, Email;
    Activity myActivity;

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_signup, container, false);
        initUi(v);

        //doInitialSetup();

        SignUp.setOnClickListener(view -> {
            if (doValidation()) {
                gotoPinFragment();
            }
            // rxValidate();
        });
        return v;
    }



    private void gotoPinFragment() {

        Fragment newFragment = new PinFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //  transaction.setCustomAnimations(R.anim.mainfadein, R.anim.splashfadeout);
        transaction.setCustomAnimations(R.anim.slide_right,R.anim.slide_left);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private boolean doValidation() {

        String username,lastName,email;

        username= firstName.getText().toString().trim();
        lastName= this.lastName.getText().toString().trim();
        email= Email.getText().toString().trim();

        if(!(TextUtils.isEmpty(username))){
            if(!TextUtils.isEmpty(email)){
                if(ValidationUtils.validateEmail(Email))
                {
                    storeUserCredentials(username, lastName);
                    return  true;

                }
                else
                {
                    showSnackBarOnError(Email, getString(R.string.email_invalid));
                    Email.requestFocus();
                }
            }else{
                showSnackBarOnError(Email, getString(R.string.email_empty_error));
                Email.requestFocus();
            }

        }else{
            showSnackBarOnError(firstName,getString(R.string.fname_error));
            firstName.requestFocus();
        }
        return false;
    }

    private void storeUserCredentials(String username, String password) {

        AppPreferenceManager.setUserEmail(myActivity, username);
        AppPreferenceManager.setUserPassword(myActivity, password);
         AppPreferenceManager.setFirstTimeUser(myActivity, false);
    }

    private void showSnackBarOnError(View v ,String message){

        Snackbar.make(v.getRootView(),message,Snackbar.LENGTH_SHORT).show();
    }

    private void initUi(View v) {

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



  /*  public Observable<HashMap> getNetworkCall() {
        NetworkUtils.postUrl();
    }
*/
   /* Subscription subscription = getNetworkCall()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HashMap>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HashMap hashMap) {

                }
            });
*/

/*
    Observable<Boolean> emailObservable = RxTextView.textChanges(firstName)
            .map(inputText -> (inputText.length() != 0) || inputText.toString().matches(".+@.+\\.[a-z]+"))
            .distinctUntilChanged();

    Observable<Boolean> passwordObservable = RxTextView.textChanges(lastName)
            .map(inputText -> (inputText.length() != 0))
            .distinctUntilChanged();

    Observable<Boolean> conpasswordObservable = RxTextView.textChanges(Email)
            .map(inputText -> (inputText.length() != 0) && inputText.toString().equals(lastName.getText().toString()))
            .distinctUntilChanged();

    private void rxValidate() {
        Observable.combineLatest(
                emailObservable,
                passwordObservable, conpasswordObservable,
                (emailValid, PasswordValid, ConpassValid) -> emailValid && PasswordValid && ConpassValid)
                .distinctUntilChanged()
                .subscribe(valid ->
                {
                    if (valid) {
                        gotoPinActivity();
                    }
                });
    }*/

   /* private void doInitialSetup() {
        layoutUserMail.setError(getString(R.string.email_error));
        emailObservable.subscribe(isValid -> layoutUserMail.setErrorEnabled(isValid));

        layoutPassword.setError(getString(R.string.account_password_error));
        passwordObservable.subscribe(isValid -> layoutPassword.setErrorEnabled(isValid));

        layoutConfirmPassword.setError(getString(R.string.confirm_password_error));
        conpasswordObservable.subscribe(isValid -> layoutConfirmPassword.setErrorEnabled(isValid));
    }*/





}
